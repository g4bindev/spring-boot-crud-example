package com.example.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.crud.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

}
