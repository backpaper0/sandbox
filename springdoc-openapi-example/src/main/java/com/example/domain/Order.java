package com.example.domain;

import lombok.Data;

@Data
public class Order {

	private int id;
	private OrderState state;
	private int customerId;
	private int amount;
}
