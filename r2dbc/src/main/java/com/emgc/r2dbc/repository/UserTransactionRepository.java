package com.emgc.r2dbc.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emgc.r2dbc.entity.UserTransaction;

import reactor.core.publisher.Flux;

@Repository
public interface UserTransactionRepository extends ReactiveCrudRepository<UserTransaction, Integer> {

	Flux<UserTransaction> findByUserId(int userId);

}
