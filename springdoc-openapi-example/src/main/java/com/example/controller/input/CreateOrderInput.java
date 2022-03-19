package com.example.controller.input;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateOrderInput {

	@NotNull
	private Integer customerId;
	@NotNull
	private Integer amount;
}
