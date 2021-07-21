package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RequestScoped
@Path("/tags")
public class TagResource {

	@Inject
	private TagService tagService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Tags findAll() {
		return tagService.findAll();
	}

	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Tag create(@FormParam("name") String name) {
		return tagService.create(name);
	}
}
