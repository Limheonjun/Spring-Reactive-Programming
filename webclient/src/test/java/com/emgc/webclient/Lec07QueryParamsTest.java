package com.emgc.webclient;

import java.net.URI;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

//ParamsController클래스가 요청 처리
public class Lec07QueryParamsTest extends BaseTest {

	@Autowired
	private WebClient webClient;

	private String queryString = "http://localhost:8080/jobs/search?count={count}&page={page}";

	@Test
	public void queryParmasTest() {
		URI uri = UriComponentsBuilder.fromUriString(queryString).build(10, 20);

		Flux<Integer> integerFlux = webClient.get()
			.uri(uri)
			.retrieve()
			.bodyToFlux(Integer.class)
			.doOnNext(System.out::println);

		StepVerifier.create(integerFlux)
			.expectNextCount(2)
			.verifyComplete();
	}

	//쿼리스트링을 보낼때 uri내부에 UriBuilder를 사용하는 방법도 존재
	@Test
	public void queryParmasTest2() {
		// URI uri = UriComponentsBuilder.fromUriString(queryString).build(10, 20);

		Flux<Integer> integerFlux = webClient.get()
			.uri(b -> b.path("jobs/search").query("count={count}&page={page}").build(10,20))
			.retrieve()
			.bodyToFlux(Integer.class)
			.doOnNext(System.out::println);

		StepVerifier.create(integerFlux)
			.expectNextCount(2)
			.verifyComplete();
	}

	//java9부터 Map.of를 사용하여 쿼리파라미터 정의 후 전달 가능
	@Test
	public void queryParmasTest3() {
		Map<String, Integer> map = Map.of(
			"count", 10,
			"page", 20
		);

		Flux<Integer> integerFlux = webClient.get()
			.uri(b -> b.path("jobs/search").query("count={count}&page={page}").build(map))
			.retrieve()
			.bodyToFlux(Integer.class)
			.doOnNext(System.out::println);

		StepVerifier.create(integerFlux)
			.expectNextCount(2)
			.verifyComplete();
	}

}
