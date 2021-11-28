package com.emgc.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		//WebClient의 mutate메소드
		//WebClient는 한번 생성되면 불변이기 때문에 동일한 값을 반복해서 사용하고자 하는 경우 mutate 사용
		//defaultHeaders로 기본 인증 헤더 설정 가능
		//filter메소드는 filterchain의 맨 마지막에 인자로 받은 필터를 추가함, 여기선 사용자의 요청을 받아서 인증토큰을 추가해주었음
		return WebClient.builder()
			.baseUrl("http://localhost:8080")
			// .filter(this::sessionToken)
			.filter(this::changeAuthStrategy)
			.build();
	}

	// private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
	// 	System.out.println("generation session token");
	// 	//request는 불변이기 때문에 복사해서 사용해야함
	// 	ClientRequest clientRequest = ClientRequest.from(request).headers(h -> h.setBearerAuth("some-lengthy-jwt")).build();
	// 	return ex.exchange(clientRequest);
	// }

	private Mono<ClientResponse> changeAuthStrategy(ClientRequest request, ExchangeFunction ex) {
		// auth -> basic or oauth
		ClientRequest clientRequest = request.attribute("auth") //request에서 auth 프로퍼티가 있는지 확인
			.map(v -> v.equals("basic") ? withBasicAuth(request) : withOauth(request)) //auth프로퍼티에 따라 BasicAuth와 Oauth로 선택
			.orElse(request);

		return ex.exchange(clientRequest);
	}

	private ClientRequest withBasicAuth(ClientRequest request) {
		return ClientRequest.from(request)
						.headers(h -> h.setBasicAuth("username", "password"))
						.build();
	}

	private ClientRequest withOauth(ClientRequest request) {
		return ClientRequest.from(request)
			.headers(h -> h.setBearerAuth("some-token"))
			.build();
	}

}
