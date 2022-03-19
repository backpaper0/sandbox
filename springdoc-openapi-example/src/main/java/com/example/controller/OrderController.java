package com.example.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.input.CreateOrderInput;
import com.example.domain.Order;

@RestController
@RequestMapping("/orders")
public class OrderController {

	@GetMapping("/{orderId}")
	public Order getOrder(@PathVariable int orderId) {
		return new Order();
	}

	@PostMapping
	public Order createOrder(@RequestBody @Validated CreateOrderInput input) {
		return new Order();
	}
}
