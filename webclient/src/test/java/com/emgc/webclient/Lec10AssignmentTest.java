package com.emgc.webclient;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec10AssignmentTest extends BaseTest {

	private static final String FORMAT = "%d %s %d = %s";
	private static final int A = 10;

	@Autowired
	private WebClient webClient;

	@Test
	public void test() {
		//flatMap : Mono나 Flux형태의 반환값을 추출하여 데이터만 반환하도록 만드는 역할, Mono 또는 Flux형태로 감싸져 있는 데이터만 반환 가능
		Flux<String> flux = Flux.range(1, 5)
			//1. Mono로 감싸져있는 send의 반환값에서 데이터를 추출하여 Flux<String>형태로 만듦, 만약 추출하지 않았다면 Flux<Mono<String>>형태
			//2. Flux<String> = {"a + b = ab", ...} 와 같은 형태의 Flux에서 데이터만 추출하여 Stream<String>형태를 반환하고 이를 doOnNext로 바로 호출 가능
			.flatMap(b -> Flux.just("+", "-", "*", "/")
				.flatMap(op -> send(b, op))
			)
			.doOnNext(System.out::println);

		StepVerifier.create(flux)
			.expectNextCount(20)
			.verifyComplete();

	}

	private Mono<String> send(int b, String op) {
		return webClient.get()
			.uri("calculator/{a}/{b}", A, b)
			.headers(h -> h.set("OP", op))
			.retrieve()
			.bodyToMono(String.class)
			.map(v -> String.format(FORMAT, A, op, b, v))
			;

	}
}
