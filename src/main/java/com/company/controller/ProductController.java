package com.company.controller;

import java.util.ArrayList;
import java.util.List;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;


@RestController
@RequestMapping("/products")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	@GetMapping("")
	void getProducts() {
		
		List<String> products = new ArrayList<String>();
		//Flux.just("Apple","Banana","Orange","iPhone").log().subscribe(products::add);
		Flux.just("Apple","Banana","Orange","iPhone").log().subscribe(new Subscriber() {

			private Subscription s;
			private int count;

			@Override
			public void onSubscribe(Subscription s) {
				//this will limit the request to 2 elements only
				s.request(2);
				this.s = s;
			}

			@Override
			public void onNext(Object t) {
				products.add((String) t);
				count++;

				if (count == 2) {
					// if count is 2, it means 2 elements have been requested, so we reset the counter and call
					//the request again. When there's no more elements, it will call onComplete method.
					count = 0;
					s.request(2);
				}
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}
		});
		
		logger.info(products.toString());
	}

}
