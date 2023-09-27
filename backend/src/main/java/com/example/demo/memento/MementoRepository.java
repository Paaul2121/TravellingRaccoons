package com.example.demo.memento;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MementoRepository extends JpaRepository<Memento, Long> {

    Optional<Memento> findMementoById(Long id);

}