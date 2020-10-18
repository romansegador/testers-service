package com.rsegador.workshop.testersservice.logs;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class HttpCallLog {

    String path;
    LocalDateTime localDateTime;

}
