package com.example;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

	private final JdbcTemplate jdbc;

	public DemoController(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	@GetMapping
	public Object getAll() {
		RowMapper<Comic> rowMapper = new DataClassRowMapper<>(Comic.class);
		List<Comic> comics = jdbc.query("select id, title from comics order by id", rowMapper);
		return Map.of("comics", comics);
	}
}
