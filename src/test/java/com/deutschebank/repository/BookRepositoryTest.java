package com.deutschebank.repository;

import com.deutschebank.domain.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private Book book1;

    private void insertBook(String bookName) {
        book1 = new Book();
        book1.setBookName(bookName);
        bookRepository.save(book1);
    }

    @BeforeEach
    public void setUp(){
        insertBook("B1");
    }

    @AfterEach
    public void tearDown(){
        bookRepository.delete(book1);
    }

    @Test
    public void testBookCreation() {
        assertNotNull(bookRepository.findById(1));
    }
}
