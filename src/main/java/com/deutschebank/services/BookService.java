package com.deutschebank.services;

import com.deutschebank.VO.TradeVO;
import com.deutschebank.domain.Book;
import com.deutschebank.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book findOrCreateBook(TradeVO tradeVO) {
        List<Book> books = (List<Book>) bookRepository.findByBookName(tradeVO.getBookId());
        Book book = null;
        if(books != null && !books.isEmpty()) {
            book = books.get(0);
        }
        else {
            book = new Book();
            book.setBookName(tradeVO.getBookId());
            bookRepository.save(book);
        }
        return book;
    }

}
