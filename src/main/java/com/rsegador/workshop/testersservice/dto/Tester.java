package com.rsegador.workshop.testersservice.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
public class Tester {

    String firstName;
    String lastName;
    String twitter;
    List<Book> books;

}
