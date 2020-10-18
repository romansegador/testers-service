package com.rsegador.workshop.testersservice;

import com.rsegador.workshop.testersservice.dto.Tester;
import com.rsegador.workshop.testersservice.logs.HttpCallLog;
import com.rsegador.workshop.testersservice.logs.HttpCallLogPublisher;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class TestersController {

    TestersService testersService;

    HttpCallLogPublisher httpCallLogPublisher;

    @GetMapping("/api/testers")
    public List<Tester> getAllTesters() {
        httpCallLogPublisher.sendLogMessage(HttpCallLog.builder().path("/api/testers").localDateTime(LocalDateTime.now()).build());
        return testersService.getAllTesters();
    }
}
