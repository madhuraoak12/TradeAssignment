package com.deutschebank.repository;

import com.deutschebank.domain.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
    public List<Book> findById(int id);
    public List<Book> findByBookName(String bookName);
}
