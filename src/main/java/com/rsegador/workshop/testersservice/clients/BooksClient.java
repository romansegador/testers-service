package com.rsegador.workshop.testersservice.clients;

import com.rsegador.workshop.testersservice.dto.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "booksclient")
public interface BooksClient {

    @GetMapping("/api/books/byauthor")
    List<Book> getBooksByAuthor(@RequestParam String firstName,
                                @RequestParam String lastName);
}
