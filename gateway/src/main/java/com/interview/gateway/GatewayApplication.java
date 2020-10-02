package com.interview.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	@Value("${orderserviceuri}")
	private String orderServiceUri;
	
	@Value("${customerserviceuri}")
	private String customerServiceUri;
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
			.route("order_route", r -> r.path("/order/*")
				.uri(orderServiceUri))
			.route("customer_route", r -> r.path("/customer/*")
					.uri(customerServiceUri))
			.build();
	}

}
