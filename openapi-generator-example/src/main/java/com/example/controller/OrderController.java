package com.example.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.openapi.api.OrdersApi;
import com.example.openapi.model.CreateOrderRequest;
import com.example.openapi.model.Order;
import com.example.openapi.model.OrderState;

@RestController
public class OrderController implements OrdersApi {

	@Override
	public ResponseEntity<Order> createOrder(@Valid CreateOrderRequest body) {
		return ResponseEntity.ok(new Order().id(123).state(OrderState.PENDING).amount(body.getAmount())
				.customerId(body.getCustomerId()));
	}

	@Override
	public ResponseEntity<Order> getOrder(Integer orderId) {
		return ResponseEntity.ok(new Order().id(orderId).state(OrderState.ACCEPTED).amount(456)
				.customerId(789));
	}
}
