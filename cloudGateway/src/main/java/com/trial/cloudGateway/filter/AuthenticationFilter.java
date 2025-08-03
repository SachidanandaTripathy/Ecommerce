package com.trial.cloudGateway.filter;
import com.trial.cloudGateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
                if (authHeaders == null || authHeaders.isEmpty()) {
                    return onError(exchange, "Missing Authorization header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = authHeaders.get(0);
                if (!authHeader.startsWith("Bearer ")) {
                    return onError(exchange, "Authorization header must start with Bearer", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);

                try {
                    String username = jwtUtil.extractUsername(token);
                    if (username == null || username.isEmpty() || !jwtUtil.validateToken(token, username)) {
                        return onError(exchange, "Invalid or expired token", HttpStatus.UNAUTHORIZED);
                    }
                } catch (Exception e) {
                    return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                }
            }
            return chain.filter(exchange);
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
