package com.example.controller.input;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateCustomerInput {

	@NotNull
	private String name;
	@NotNull
	private Integer creditLimit;
}
