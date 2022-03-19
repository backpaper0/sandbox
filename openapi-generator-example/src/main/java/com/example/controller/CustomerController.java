package com.example.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.openapi.api.CustomersApi;
import com.example.openapi.model.CreateCustomerRequest;
import com.example.openapi.model.CreditReservation;
import com.example.openapi.model.Customer;

@RestController
public class CustomerController implements CustomersApi {

	@Override
	public ResponseEntity<Customer> createCustomer(@Valid CreateCustomerRequest body) {
		return ResponseEntity.ok(new Customer().id(123).name(body.getName()).creditLimit(body.getCreditLimit())
				.creditReservations(List.of()));
	}

	@Override
	public ResponseEntity<Customer> getCustomer(Integer customerId) {
		return ResponseEntity.ok(new Customer().id(customerId).name("Bob").creditLimit(1000000)
				.creditReservations(List.of(new CreditReservation().id(1).customerId(customerId).amount(100),
						new CreditReservation().id(2).customerId(customerId).amount(200))));
	}
}
