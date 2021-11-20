package com.emgc.webfluxrouter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.emgc.webfluxrouter.dto.InputFailedValidationResponse;
import com.emgc.webfluxrouter.dto.MultiplyRequestDto;
import com.emgc.webfluxrouter.dto.Response;
import com.emgc.webfluxrouter.exception.InputValidationnException;
import com.emgc.webfluxrouter.service.ReactiveMathService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

	@Autowired
	private ReactiveMathService mathService;

	public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
		int input = Integer.parseInt(serverRequest.pathVariable("input")); //pathVariable에 접근
		Mono<Response> responseMono = this.mathService.findSquare(input); //파이프라인 구성(구독 전이라 아무일도 발생하지 않음)\

		//String과 같은 객체를 반환하는 경우엔 bodyValue, 단지 publisher에게 객체를 전달하는 경우엔 body
		//여기선 라우터에 Mono객체를 넘겨 알아서 처리하도록 해야 하기 때문에 body 사용용
		return ServerResponse.ok().body(responseMono, Response.class);
	}

	public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
		int input = Integer.parseInt(serverRequest.pathVariable("input"));
		Flux<Response> responseFlux = this.mathService.multiplicationTable(input);

		//Flux이지만 Mono타입을 반환하는 이유
		//우리는 Flux로 래핑된 Response들을 반환하는 것이 목적이 아님
		//responseFlux객체를 들고 있는 ServerResponse를 하나만 반환하는 것이기 때문에 반환타입은 Mono
		return ServerResponse.ok().body(responseFlux, Response.class);
	}

	public Mono<ServerResponse> tableStreamHandler(ServerRequest serverRequest) {
		int input = Integer.parseInt(serverRequest.pathVariable("input"));
		Flux<Response> responseFlux = this.mathService.multiplicationTable(input);
		return ServerResponse.ok()
			.contentType(MediaType.TEXT_EVENT_STREAM)
			.body(responseFlux, Response.class);
	}

	public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
		Mono<MultiplyRequestDto> requestDtoMono = serverRequest.bodyToMono(MultiplyRequestDto.class);
		Mono<Response> responseMono = this.mathService.multiply(requestDtoMono);
		return ServerResponse.ok().body(responseMono, Response.class);
	}

	public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
		int input = Integer.parseInt(serverRequest.pathVariable("input"));

		// if(input < 10 || input > 20) {
		// 	InputFailedValidationResponse response = new InputFailedValidationResponse();
		// 	return ServerResponse.badRequest().bodyValue(response); //예외의 경우 바로 예외메시지를 반환하면 되기 때문에 bodyValue메소드 사용
		// }
		if(input < 10 || input > 20) {
			return Mono.error(new InputValidationnException(input)); //위 코드의 축약형형
	}

		Mono<Response> responseMono = this.mathService.findSquare(input);
		return ServerResponse.ok().body(responseMono, Response.class);
	}

}
