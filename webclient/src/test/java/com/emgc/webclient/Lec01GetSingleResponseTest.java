package com.emgc.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import com.emgc.webclient.dto.Response;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec01GetSingleResponseTest extends BaseTest {

	@Autowired
	private WebClient webClient;

	@Test
	public void blockTest() {
		Response response = this.webClient
			.get()
			.uri("reactive-math/square/{number}", 5)
			.retrieve() //response를 받아 디코딩하는 메소드
			.bodyToMono(Response.class) // 반환값은 Mono<Response> 타입
			.block();

		System.out.println(response);
	}

	@Test
	public void testWithStepVerifier() {
		Mono<Response> response = this.webClient
			.get()
			.uri("reactive-math/square/{number}", 5)
			.retrieve() //response를 받아 디코딩하는 메소드
			.bodyToMono(Response.class) // 반환값은 Mono<Response> 타입
			;

		StepVerifier.create(response)
					.expectNextMatches(res ->res.getOutput() == 25)
					.verifyComplete();
	}

}
