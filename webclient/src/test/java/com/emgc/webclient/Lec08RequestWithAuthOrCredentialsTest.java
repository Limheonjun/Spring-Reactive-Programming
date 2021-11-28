package com.emgc.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.emgc.webclient.dto.MultiplyRequestDto;
import com.emgc.webclient.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec08RequestWithAuthOrCredentialsTest extends BaseTest{

	@Autowired
	private WebClient webClient;

	@Test
	public void headersTest() {
		Mono<Response> responseMono = webClient
			.post()
			.uri("reactive-math/multiply")
			.bodyValue(buildRequestDto(5, 2))
			//WebClient로 요청을 보낼 때 인증정보를 설정할 수도 있지만
			//WebClient를 Bean으로 등록 시 defaultHeader를 설정할 수도 있음
			// .headers(h -> h.setBasicAuth("username", "password"))
			.retrieve()
			.bodyToMono(Response.class)
			.doOnNext(System.out::println);

		StepVerifier.create(responseMono)
			.expectNextCount(1)
			.verifyComplete();

		StepVerifier.create(responseMono)
			.expectNextCount(1)
			.verifyComplete();
	}

	private MultiplyRequestDto buildRequestDto(int a, int b) {
		MultiplyRequestDto dto = new MultiplyRequestDto();
		dto.setFirst(a);
		dto.setSecond(b);
		return dto;
	}
}
