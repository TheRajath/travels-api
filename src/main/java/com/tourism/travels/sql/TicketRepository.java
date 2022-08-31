package com.tourism.travels.sql;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TicketRepository extends JpaRepository<TicketEntity, Integer>, QuerydslPredicateExecutor<TicketEntity> {

}
