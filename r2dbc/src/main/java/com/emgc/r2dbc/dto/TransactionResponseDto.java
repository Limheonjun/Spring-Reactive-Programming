package com.emgc.r2dbc.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransactionResponseDto {

	private Integer userId;
	private Integer amount;
	private TransactionStatus status;

}
