package com.rsegador.workshop.testersservice;

import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.rsegador.workshop.testersservice.clients.BooksClient;
import com.rsegador.workshop.testersservice.dto.Book;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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

    @Pact(consumer = "testers-service")
    public RequestResponsePact noBooksAvailable(PactDslWithProvider builder) {
        return builder
                .given("no books available for an author")
                .uponReceiving("A request to retrieve the books for a tester without books")
                    .path("/api/books/byauthor")
                    .method(GET.name())
                    .matchQuery("firstName", ".*", "testersFirstName")
                    .matchQuery("lastName", ".*", "testersLastName")
                .willRespondWith()
                    .status(404)
                .toPact();
    }


    @Pact(consumer = "testers-service")
    public RequestResponsePact badBooksRequest(PactDslWithProvider builder) {
        return builder
                // No Given needed, as is the default behavior when parameters are not send
                .uponReceiving("A request to retrieve the books without the needed parameters")
                    .path("/api/books/byauthor")
                    .method(GET.name())
                .willRespondWith()
                    .status(400)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "booksAvailable")
    void pactForBooksAvailableForAnAuthor(){
        Optional<List<Book>> booksReceived = booksClient.getBooksByAuthor("testersFirstName", "testersLastName");

        assertThat(booksReceived).isPresent();
        assertThat(booksReceived.get()).containsExactly(TestExpectations.book);

    }

    @Test
    @PactTestFor(pactMethod = "noBooksAvailable")
    void pactForNoBooksAvailableForAnAuthor(){
        Optional<List<Book>> booksReceived = booksClient.getBooksByAuthor("testersFirstName", "testersLastName");

        assertThat(booksReceived).isNotPresent();
    }

    @Test
    @PactTestFor(pactMethod = "badBooksRequest")
    void pactForBadBooksRequestForAnAuthor(){
        assertThatExceptionOfType(FeignException.BadRequest.class).isThrownBy(() ->
                booksClient.getBooksByAuthor(null, null)).withMessageContaining("[400 Bad Request] during [GET] to " +
                "[http://booksclient/api/books/byauthor]");
    }

    interface TestExpectations {
        Book book = Book.builder()
                .title("This is the title of the book")
                .build();
    }
}
