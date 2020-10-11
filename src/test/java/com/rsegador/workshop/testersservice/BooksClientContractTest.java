package com.rsegador.workshop.testersservice;

import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.rsegador.workshop.testersservice.clients.BooksClient;
import com.rsegador.workshop.testersservice.dto.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "books-service", port="36000")
@SpringBootTest
@ActiveProfiles("test")
public class BooksClientContractTest {

    @Autowired
    BooksClient booksClient;

    @Pact(consumer = "testers-service")
    public RequestResponsePact booksAvailable(PactDslWithProvider builder) {
        return builder
                .given("books available for an author")
                .uponReceiving("A request to retrieve the books written by a tester")
                    .path("/api/books/byauthor")
                    .method(GET.name())
                    .matchQuery("firstName", ".*", "testersFirstName")
                    .matchQuery("lastName", ".*", "testersLastName")
                .willRespondWith()
                    .status(200)
                    .body(PactDslJsonArray
                            .arrayMinLike(1)
                                .stringType("title", "This is the title of the book"))
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "booksAvailable")
    void pactForBooksAvailableForAnAuthor(){
        List<Book> booksReceived = booksClient.getBooksByAuthor("testersFirstName", "testersLastName");

        Assertions.assertThat(booksReceived).containsExactly(TestExpectations.book);

    }

    interface TestExpectations {
        Book book = Book.builder()
                .title("This is the title of the book")
                .build();
    }
}
