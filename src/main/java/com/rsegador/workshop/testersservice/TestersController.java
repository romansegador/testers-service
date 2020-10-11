package com.rsegador.workshop.testersservice;

import com.rsegador.workshop.testersservice.dto.Tester;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TestersController {

    TestersService testersService;

    @GetMapping("/api/testers")
    public List<Tester> getAllTesters() {
        return testersService.getAllTesters();
    }
}
