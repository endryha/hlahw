package com.hla.writerapp;

import com.github.javafaker.Faker;
import com.github.javafaker.Superhero;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;

@RequestMapping("/superhero")
@RestController
public class ApiController {

    private final Faker faker;
    private final SuperheroRepository repository;

    public ApiController(Faker faker, SuperheroRepository repository) {
        this.faker = faker;
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> insert() {
        SuperheroInfo superheroInfo = newSuperhero();
        repository.save(superheroInfo);
        repository.flush();
        return ResponseEntity.ok(superheroInfo.getId());
    }

    private SuperheroInfo newSuperhero() {
        Superhero superhero = faker.superhero();
        SuperheroInfo superheroInfo = new SuperheroInfo();
        superheroInfo.setName(superhero.name());
        superheroInfo.setPower(superhero.power());
        superheroInfo.setBirthdate(getBirthdate());
        return superheroInfo;
    }

    private LocalDate getBirthdate() {
        return faker.date().birthday().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
}
