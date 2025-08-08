package com.trial.cloudGateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final WebClient webClient;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.build();
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String skipAuth = exchange.getRequest().getHeaders().getFirst("X-Skip-Auth");
            if ("false".equalsIgnoreCase(skipAuth)) {
                return chain.filter(exchange);
            }

            List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeaders == null || authHeaders.isEmpty()) {
                return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
            }

            String token = authHeaders.get(0);

//            return Mono.fromCallable(() -> customerClient.validateToken(token))
//                    .flatMap(customerEmail -> {
//                        if (customerEmail == null || customerEmail.isEmpty()
//                                || customerEmail.equalsIgnoreCase("User not found")
//                                || customerEmail.equalsIgnoreCase("Token expired")
//                                || customerEmail.equalsIgnoreCase("Invalid token")) {
//                            return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
//                        }
//
//                        ServerWebExchange mutatedExchange = exchange.mutate()
//                                .request(builder -> builder.header("X-Customer-Email", customerEmail))
//                                .build();
//
//                        return chain.filter(mutatedExchange);
//                    })
//                    .onErrorResume(e -> onError(exchange, "Token validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED));

            return webClient.get()
                    .uri("http://localhost:8081/auth/validate?token=" + token)
                    .retrieve()
                    .onStatus(status -> status.isError(), clientResponse ->
                            Mono.error(new RuntimeException("Token validation failed with status " + clientResponse.statusCode()))
                    )
                    .bodyToMono(String.class)
                    .flatMap(customerEmail -> {
                        if (customerEmail == null || customerEmail.isEmpty()
                                || customerEmail.equalsIgnoreCase("User not found")
                                || customerEmail.equalsIgnoreCase("Token expired")
                                || customerEmail.equalsIgnoreCase("Invalid token")) {
                            return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
                        }

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(builder -> builder.header("X-Customer-Email", customerEmail))
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .onErrorResume(e -> onError(exchange, "Token validation failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED));
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String message, HttpStatus status) {
        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"error\": \"" + message + "\"}";
        var buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    public static class Config {
    }
}
