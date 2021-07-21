package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.lettuce.core.api.sync.RedisCommands;

@RequestScoped
@Path("/cache")
public class CacheResource {

	@Inject
	private RedisCommands<String, String> redisCommands;

	@Path("/{key}")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getCache(@PathParam("key") String key) {
		return redisCommands.get(key);
	}

	@Path("/{key}")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void setCache(@PathParam("key") String key, @FormParam("value") String value) {
		redisCommands.set(key, value);
	}
}
