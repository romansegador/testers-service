package com.rsegador.workshop.testersservice.clients;

import com.rsegador.workshop.testersservice.dto.Book;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "booksclient", decode404 = true)
public interface BooksClient {

    @GetMapping("/api/books/byauthor")
    Optional<List<Book>> getBooksByAuthor(@RequestParam String firstName,
                                          @RequestParam String lastName);
}
