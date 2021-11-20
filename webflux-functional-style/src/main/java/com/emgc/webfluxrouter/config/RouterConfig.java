package com.emgc.webfluxrouter.config;

import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.emgc.webfluxrouter.dto.InputFailedValidationResponse;
import com.emgc.webfluxrouter.exception.InputValidationnException;

import reactor.core.publisher.Mono;

@Configuration
public class RouterConfig {

	@Autowired
	private RequestHandler requestHandler;

	@Bean
	public RouterFunction<ServerResponse> highLevelRouter() {
		return RouterFunctions.route()
			.path("router", this::serverResponseRouterFunction) //맨 앞 경로가 router인 것에 대해서 serverResponseRouterFunction로 연결시켜줌
			.build();
	}

	//path이하에 등록되었으면 Bean 애노테이션 삭제 가능, private으로 접근지정자 변경 가능
	// @Bean
	public RouterFunction<ServerResponse> serverResponseRouterFunction() {
		return RouterFunctions.route()
				.GET("square/{input}", RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")), requestHandler::squareHandler) //Predicate을 사용하여 조건에 맞는 변수가 오는 경우에만 핸들러 실행, 10~19만 와야함
				.GET("square/{input}", req -> ServerResponse.badRequest().bodyValue("only 10-19 allowed")) //10~19이외의 값은 여기서 처리
				.GET("table/{input}", requestHandler::tableHandler)
				.GET("table/{input}/stream", requestHandler::tableStreamHandler)
				.POST("multiply", requestHandler::multiplyHandler)
				.GET("square/{input}/validation", requestHandler::squareHandlerWithValidation)
				.onError(InputValidationnException.class, exceptionHandler()) //ExceptionHandling
			.build();
	}

	private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
		return (err, req) -> {
			InputValidationnException ex = (InputValidationnException) err;
			InputFailedValidationResponse response = new InputFailedValidationResponse();
			response.setInput(ex.getInput());
			response.setMessage(ex.getMessage());
			response.setErrorCode(ex.getErrorCode());
			return ServerResponse.badRequest().bodyValue(response);
		};
	}
}
