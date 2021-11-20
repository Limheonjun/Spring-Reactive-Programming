package com.emgc.r2dbc.util;

import java.time.LocalDateTime;

import org.springframework.beans.BeanUtils;

import com.emgc.r2dbc.dto.TransactionRequestDto;
import com.emgc.r2dbc.dto.TransactionResponseDto;
import com.emgc.r2dbc.dto.TransactionStatus;
import com.emgc.r2dbc.dto.UserDto;
import com.emgc.r2dbc.entity.User;
import com.emgc.r2dbc.entity.UserTransaction;

public class EntityDtoUtil {

	public static UserDto toDto(User user) {
		UserDto dto = new UserDto();
		BeanUtils.copyProperties(user, dto);
		return dto;
	}

	public static User toEntity(UserDto dto) {
		User user = new User();
		BeanUtils.copyProperties(dto, user);
		return user;
	}

	public static UserTransaction toEntity(TransactionRequestDto requestDto){
		UserTransaction ut = new UserTransaction();
		ut.setUserId(requestDto.getUserId());
		ut.setAmount(requestDto.getAmount());
		ut.setTransactionDate(LocalDateTime.now());
		return ut;
	}

	public static TransactionResponseDto toDto(TransactionRequestDto requestDto, TransactionStatus status){
		TransactionResponseDto responseDto = new TransactionResponseDto();
		responseDto.setAmount(requestDto.getAmount());
		responseDto.setUserId(requestDto.getUserId());
		responseDto.setStatus(status);
		return responseDto;
	}

}
