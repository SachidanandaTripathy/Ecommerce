package com.trial.cloudGateway.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customer-microservice", url = "http://localhost:8081")
public interface CustomerClient {

    @GetMapping("/auth/validate")
    String validateToken(String token);
}
