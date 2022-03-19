package com.example.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.controller.input.CreateCustomerInput;
import com.example.domain.Customer;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@PostMapping
	public Customer createCustomer(@Validated @RequestBody CreateCustomerInput input) {
		return new Customer();
	}

	@GetMapping("/{customerId}")
	public Customer getCustomer(@PathVariable int customerId) {
		return new Customer();
	}
}
