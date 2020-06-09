package com.example;

import java.io.IOException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("")
public class Api {

	/*
	 * 例外をスローせずメソッドを終了したらContainerResponseFilterの処理が行われる。
	 */
	@GET
	@Path("/200")
	@Produces(MediaType.TEXT_PLAIN)
	public String ok() {
		return "OK";
	}

	/*
	 * スローされるのがWebApplicationExceptionサブクラスなら
	 * ContainerResponseFilterの処理が行われる。
	 */
	@GET
	@Path("/400")
	@Produces(MediaType.TEXT_PLAIN)
	public String badRequest() {
		throw new BadRequestException();
	}

	@GET
	@Path("/404")
	@Produces(MediaType.TEXT_PLAIN)
	public String notFound() {
		throw new NotFoundException();
	}

	@GET
	@Path("/500")
	@Produces(MediaType.TEXT_PLAIN)
	public String internalServerError() {
		throw new InternalServerErrorException();
	}

	/*
	 * ExceptionMapperでレスポンスにマッピングされない例外の場合は
	 * ContainerResponseFilterの処理がされない。
	 */
	@GET
	@Path("/npe")
	@Produces(MediaType.TEXT_PLAIN)
	public String npe() {
		throw new NullPointerException();
	}

	/*
	 * ExceptionMapperでレスポンスにマッピングされる例外の場合は
	 * ContainerResponseFilterの処理が行われる。
	 * (IOExceptionMapperでIOExceptionをレスポンスへマッピングしている)
	 */
	@GET
	@Path("/ioe")
	@Produces(MediaType.TEXT_PLAIN)
	public String ioe() throws IOException {
		throw new IOException();
	}

	/*
	 * /xxx のようにリソースメソッドが存在しないパスへのリクエストを行うと
	 * ContainerRequestFilterの処理は行われず、ContainerResponseFilterの
	 * 処理のみが行われる。
	 */

	/*
	 * POST /200 のようにサポートされないHTTPメソッドでリクエストを行うと
	 * ContainerRequestFilterの処理は行われず、ContainerResponseFilterの
	 * 処理のみが行われる。
	 */
}
