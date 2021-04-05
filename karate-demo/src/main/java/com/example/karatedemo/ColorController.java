package com.example.karatedemo;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/colors")
public class ColorController {

	private final ColorService service;

	public ColorController(ColorService service) {
		this.service = service;
	}

	@GetMapping
	public List<Color> getColors() {
		return service.getColors();
	}

	@PostMapping
	public Color addColor(@RequestBody Color req) {
		return service.addColor(req.getName());
	}
}
