package com.deutschebank;

import com.deutschebank.domain.Book;
import com.deutschebank.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TradeAssignmentApplication implements CommandLineRunner {

  public static void main(String[] args) {
        SpringApplication.run(TradeAssignmentApplication.class, args);
    }

  @Override
  public void run(String ... args) {

  }
}
