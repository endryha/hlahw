package com.hla.writerapp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SuperheroRepository extends JpaRepository<SuperheroInfo, Long> {
}
