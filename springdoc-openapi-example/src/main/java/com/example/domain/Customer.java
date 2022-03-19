package com.example.domain;

import java.util.List;

import lombok.Data;

@Data
public class Customer {

	private int id;
	private String name;
	private int creditLimit;
	private List<CreditReservation> creditReservations;
}
