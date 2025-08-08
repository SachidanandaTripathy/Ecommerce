package com.blibli.Customer.controller;
import com.blibli.Customer.dto.AuthRequest;
import com.blibli.Customer.dto.CustomerRegistrationDTO;
import com.blibli.Customer.dto.GenericResponse;
import com.blibli.Customer.dto.LoginResponseDTO;
import com.blibli.Customer.exception.CustomException;
import com.blibli.Customer.services.AuthServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthServices service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public GenericResponse<String> addNewUser(@RequestBody CustomerRegistrationDTO customerRegistrationDTO) {
        String result = service.saveUser(customerRegistrationDTO);

        return new GenericResponse<>(
                true,
                "Registration successful",
                result,
                LocalDateTime.now()
        );
    }

    @PostMapping("/login")
    public GenericResponse<LoginResponseDTO> getToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getCustomerEmail(),
                        authRequest.getCustomerPassword()
                )
        );

        if (authentication.isAuthenticated()) {
            LoginResponseDTO loginResponseDTO = service.generateToken(authRequest.getCustomerEmail());

            return new GenericResponse<>(
                    true,
                    "Login successful",
                    loginResponseDTO,
                    LocalDateTime.now()
            );
        } else {
            throw new CustomException("Invalid email or password");
        }
    }

    @GetMapping("/customer/{customerEmail}")
    public GenericResponse<LoginResponseDTO> getUserByEmail(@PathVariable String customerEmail) {
        LoginResponseDTO customer = service.getUserByEmail(customerEmail);

         return new GenericResponse<>(
                true,
                "Customer fetched successfully",
                customer,
                LocalDateTime.now()
        );
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam String token) {
            String customerEmail = service.validateToken(token);
            log.info(customerEmail);
            return ResponseEntity.ok(customerEmail);
    }
}
