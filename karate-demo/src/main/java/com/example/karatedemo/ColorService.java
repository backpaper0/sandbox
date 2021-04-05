package com.example.karatedemo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ColorService {

	private final ColorRepository repos;

	public ColorService(ColorRepository repos) {
		this.repos = repos;
	}

	public List<Color> getColors() {
		return repos.findAll(Sort.by(Order.asc("id")));
	}

	public Color addColor(String name) {
		var entity = new Color();
		entity.setName(name);
		return repos.save(entity);
	}
}
