package com.blibli.cart.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/cart")
public class CartController {

    @GetMapping("/get")
    public String getProduct(){
        return "cart page";
    }
}
