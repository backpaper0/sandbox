package com.example;

import java.io.IOException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IOExceptionMapper implements ExceptionMapper<IOException> {

	@Override
	public Response toResponse(IOException exception) {
		return Response.serverError().entity(exception.getMessage()).type(MediaType.TEXT_PLAIN)
				.build();
	}
}
