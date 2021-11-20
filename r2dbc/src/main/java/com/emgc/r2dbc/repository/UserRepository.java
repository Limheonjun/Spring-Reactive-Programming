package com.emgc.r2dbc.repository;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.emgc.r2dbc.entity.User;

import reactor.core.publisher.Mono;

@Repository //ReactiveCrudRepository에는 Repository애노테이션 필수
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

	//업데이트가 정상적으로로 되었는지 확인
	@Modifying //수정쿼리에는 Modifying이 필수
	@Query(
		"update users " +
		"set balance = balance - :amount " + //:변수
		"where id = :userId " +
		"and balance >= :amount"
	)
	Mono<Boolean> updateUserBalance(int userId, int amount);

}
