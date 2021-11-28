package com.emgc.webclient.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("jobs")
public class ParamsController {

	@GetMapping("search")
	public Flux<Integer> searchJobs(
		@RequestParam int count,
		@RequestParam int page
	) {
		return Flux.just(count, page);
	}

}
