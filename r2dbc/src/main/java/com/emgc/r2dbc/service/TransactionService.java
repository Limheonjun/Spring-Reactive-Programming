package com.emgc.r2dbc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.emgc.r2dbc.dto.TransactionRequestDto;
import com.emgc.r2dbc.dto.TransactionResponseDto;
import com.emgc.r2dbc.dto.TransactionStatus;
import com.emgc.r2dbc.entity.UserTransaction;
import com.emgc.r2dbc.repository.UserRepository;
import com.emgc.r2dbc.repository.UserTransactionRepository;
import com.emgc.r2dbc.util.EntityDtoUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserTransactionRepository transactionRepository;

	public Mono<TransactionResponseDto> createTransaction(final TransactionRequestDto requestDto){
		return this.userRepository.updateUserBalance(requestDto.getUserId(), requestDto.getAmount())
			.filter(Boolean::booleanValue)
			.map(b -> EntityDtoUtil.toEntity(requestDto))
			.flatMap(this.transactionRepository::save)
			.map(ut -> EntityDtoUtil.toDto(requestDto, TransactionStatus.APPROVED))
			.defaultIfEmpty(EntityDtoUtil.toDto(requestDto, TransactionStatus.DECLINED));
	}

	public Flux<UserTransaction> getByUserId(int userId){
		return this.transactionRepository.findByUserId(userId);
	}

}
