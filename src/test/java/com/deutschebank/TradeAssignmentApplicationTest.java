package com.deutschebank;

import com.deutschebank.VO.TradeVO;
import com.deutschebank.domain.Book;
import com.deutschebank.domain.Trade;
import com.deutschebank.domain.TradeVersion;
import com.deutschebank.exception.InvalidTradeException;
import com.deutschebank.messaging.TradeConsumer;
import com.deutschebank.repository.BookRepository;
import com.deutschebank.repository.CounterPartyRepository;
import com.deutschebank.repository.TradeRepository;
import com.deutschebank.repository.TradeVersionRepository;
import com.deutschebank.services.TradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class TradeAssignmentApplicationTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TradeConsumer tradeConsumer;
    @Autowired
    private TradeService tradeService;

    @Autowired
    private TradeVersionRepository tradeVersionRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CounterPartyRepository  counterPartyRepository;
    
    @Test
    public void contextLoads() {

    }
    
    @AfterEach
    public void tearDown() {
        tradeVersionRepository.deleteAll();
        tradeRepository.deleteAll();
        bookRepository.deleteAll();
        counterPartyRepository.deleteAll();
    }

    @Test
    public void testBookCreation() {
        Book book = new Book();
        book.setBookName("B1");
        bookRepository.save(book);
        List<Book> books = (List<Book>) bookRepository.findById(book.getId());
        assertEquals(1, books.size());
        assertEquals("B1", books.get(0).getBookName());
    }

    @Test
    public void testCreateTradeWithLaterMaturityDate() throws InterruptedException {
        TradeVO trade = new TradeVO();
        trade.setBookId("B1");
        trade.setCounterPartyId("CP-1");
        trade.setTradeId("T1");
        trade.setVersion(1);
        trade.setCreatedDate(LocalDate.now().format(formatter));
        trade.setMaturityDate(LocalDate.now().plus(1,ChronoUnit.DAYS).format(formatter));
        restTemplate.postForObject("http://localhost:" + port + "/trade", trade, TradeVO.class);;
        Thread.sleep(10000);
        TradeVersion tradeVersion = tradeConsumer.getTradeVersion();
        assertNotNull(tradeVersion);
        assertEquals('N', tradeVersion.getExpired());
        
    }

    @Test
    public void testCreateTradeWithEarlierMaturityDate() throws InterruptedException {
        TradeVO trade = new TradeVO();
        trade.setBookId("B1");
        trade.setCounterPartyId("CP-1");
        trade.setTradeId("T2");
        trade.setVersion(1);
        trade.setCreatedDate(LocalDate.now().format(formatter));
        trade.setMaturityDate(LocalDate.now().minus(1,ChronoUnit.DAYS).format(formatter));
        restTemplate.postForObject("http://localhost:" + port + "/trade", trade, TradeVO.class);
        Thread.sleep(10000);
        TradeVersion tradeVersion = tradeConsumer.getTradeVersion();
        assertNotNull(tradeVersion);
        assertEquals('Y', tradeVersion.getExpired());
    }

    @Test
    public void testCreateTradeWithMultipleTradeVersions() throws InterruptedException {
        TradeVO trade1 = new TradeVO();
        trade1.setBookId("B2");
        trade1.setCounterPartyId("CP-2");
        trade1.setTradeId("T3");
        trade1.setVersion(1);
        trade1.setCreatedDate(LocalDate.now().format(formatter));
        trade1.setMaturityDate(LocalDate.now().plus(1,ChronoUnit.DAYS).format(formatter));
        restTemplate.postForObject("http://localhost:" + port + "/trade", trade1, TradeVO.class);
        Thread.sleep(100);
        TradeVO trade2 = new TradeVO();
        trade2.setBookId("B2");
        trade2.setCounterPartyId("CP-2");
        trade2.setTradeId("T3");
        trade2.setVersion(2);
        trade2.setCreatedDate(LocalDate.now().format(formatter));
        trade2.setMaturityDate(LocalDate.now().plus(1,ChronoUnit.DAYS).format(formatter));
        restTemplate.postForObject("http://localhost:" + port + "/trade", trade2, TradeVO.class);
        Thread.sleep(10000);
        TradeVersion tradeVersion1 = tradeConsumer.getTradeVersion();
        assertNotNull(tradeVersion1);
        assertEquals('N', tradeVersion1.getExpired());
        Thread.sleep(10000);
        TradeVersion tradeVersion2 = tradeConsumer.getTradeVersion();
        assertNotNull(tradeVersion2);
        assertNotNull(tradeVersion2.getTrade());
        Trade tradeObj = tradeVersion2.getTrade();
        assertEquals('N', tradeVersion2.getExpired());
        assertEquals(tradeVersion1.getTrade().getId(), tradeVersion2.getTrade().getId());
        List <TradeVersion> tradeVersions = tradeService.findTradeVersionsByTradeId(tradeObj.getId());
        assertEquals(2, tradeVersions.size());
        assertEquals(1, tradeVersions.get(0).getVersionNumber());
        assertEquals(2, tradeVersions.get(1).getVersionNumber());
        assertEquals(tradeVersions.get(0).getTrade().getId(),tradeVersions.get(1).getTrade().getId());
    }

    @Test
    public void testCreateTradeWithLowerTradeVersions() throws InterruptedException {
        //TradeVersion tradeVersion11 = tradeConsumer.getTradeVersion();
        TradeVO trade1 = new TradeVO();
        trade1.setBookId("B3");
        trade1.setCounterPartyId("CP-3" );
        trade1.setTradeId("T5");
        trade1.setVersion(2);
        trade1.setCreatedDate(LocalDate.now().format(formatter));
        trade1.setMaturityDate(LocalDate.now().plus(1,ChronoUnit.DAYS).format(formatter));
        restTemplate.postForObject("http://localhost:" + port + "/trade", trade1, TradeVO.class);
        Thread.sleep(100);
        TradeVO trade2 = new TradeVO();
        trade2.setBookId("B3");
        trade2.setCounterPartyId("CP-3");
        trade2.setTradeId("T5");
        trade2.setVersion(1);
        trade2.setCreatedDate(LocalDate.now().format(formatter));
        trade2.setMaturityDate(LocalDate.now().plus(1,ChronoUnit.DAYS).format(formatter));
        restTemplate.postForObject("http://localhost:" + port + "/trade", trade2, TradeVO.class);
        Thread.sleep(10000);
        TradeVersion tradeVersion1 = tradeConsumer.getTradeVersion();
        assertNotNull(tradeVersion1);
        assertEquals(2,tradeVersion1.getVersionNumber());
        Thread.sleep(10000);
        Exception exception = assertThrows(InvalidTradeException.class, () -> {
            TradeVersion tradeVersion2 = tradeConsumer.getTradeVersion();
        });

        String expectedMessage = "Higher version of trade exists";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage,actualMessage);

    }

}
