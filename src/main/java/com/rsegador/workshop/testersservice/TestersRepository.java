package com.rsegador.workshop.testersservice;

import com.google.common.collect.ImmutableList;
import com.rsegador.workshop.testersservice.dto.Tester;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rsegador.workshop.testersservice.TestersRepository.fixture.*;

@Component
public class TestersRepository {

    public List<Tester> getAllTesters() {
        return ImmutableList.of(lisa, janet, elisabeth, carlos, katrina, angie, john);
    }
    
    interface fixture {
        Tester lisa = Tester.builder()
                .firstName("Lisa")
                .lastName("Crispin")
                .twitter("@lisacrispin")
                .build();

        Tester janet = Tester.builder()
                .firstName("Janet")
                .lastName("Gregory")
                .twitter("@janetgregoryca")
                .build();

        Tester elisabeth = Tester.builder()
                .firstName("Elisabeth")
                .lastName("Hendrickson")
                .twitter("@testobsessed")
                .build();

        Tester carlos = Tester.builder()
                .firstName("Carlos")
                .lastName("Ble")
                .twitter("@carlosble")
                .build();

        Tester katrina = Tester.builder()
                .firstName("Katrina")
                .lastName("Clokie")
                .twitter("@katrinaclokie")
                .build();

        Tester angie = Tester.builder()
                .firstName("Angie")
                .lastName("Jones")
                .twitter("@techgirl1908")
                .build();

        Tester john = Tester.builder()
                .firstName("John")
                .lastName("Ferguson")
                .twitter("@wakaleo")
                .build();

    }
}
