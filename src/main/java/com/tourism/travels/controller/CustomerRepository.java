package com.tourism.travels.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {

    Optional<CustomerEntity> findByEmail(String email);

}
