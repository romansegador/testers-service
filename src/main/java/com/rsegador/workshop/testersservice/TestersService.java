package com.rsegador.workshop.testersservice;

import com.rsegador.workshop.testersservice.clients.BooksClient;
import com.rsegador.workshop.testersservice.dto.Book;
import com.rsegador.workshop.testersservice.dto.Tester;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.*;
import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@AllArgsConstructor
public class TestersService {

    TestersRepository testersRepository;
    BooksClient booksClient;

    public List<Tester> getAllTesters() {
        List<Tester> testers = testersRepository.getAllTesters();

        return testers.stream().map(this::enrichWithBooks).collect(toList());

    }

    private Tester enrichWithBooks(Tester tester) {
        List<Book> books = booksClient.getBooksByAuthor(tester.getFirstName(), tester.getLastName());
        return isEmpty(books) ? tester :
               tester.withBooks(books.stream().map(Book::getTitle).collect(toList()));
    }
}
