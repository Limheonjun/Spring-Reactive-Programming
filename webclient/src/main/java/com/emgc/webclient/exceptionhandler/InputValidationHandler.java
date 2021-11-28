package com.emgc.webclient.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.emgc.webclient.dto.InputFailedValidationResponse;
import com.emgc.webclient.exception.InputValidationnException;

@ControllerAdvice
public class InputValidationHandler {

	@ExceptionHandler(InputValidationnException.class)
	public ResponseEntity<InputFailedValidationResponse> handlerException(InputValidationnException ex) {
		InputFailedValidationResponse response = new InputFailedValidationResponse();
		response.setErrorCode(ex.getErrorCode());
		response.setInput(ex.getInput());
		response.setMessage(ex.getMessage());
		return ResponseEntity.badRequest().body(response);
	}
}
