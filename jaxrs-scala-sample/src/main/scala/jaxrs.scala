package sample

import javax.ws.rs._
import javax.ws.rs.core._
import javax.ws.rs.ext._
import javax.ws.rs.container._

@ApplicationPath("api")
class ScalaxRs extends Application

@Path("hello")
class HelloApi {
  @GET
  def say(@QueryParam("name") name: String, @Context uriInfo: UriInfo): String = s"Hello, ${name}! : ${uriInfo.getAbsolutePath()}"
}

@Path("echo")
class EchoApi {
  @POST
  @Consumes(Array("application/x-www-form-urlencoded"))
  @Produces(Array("text/plain"))
  def echo(@FormParam("data") data: String): Option[String] = Option(data)
}

@Provider
class OptionProvider extends WriterInterceptor {
  def aroundWriteTo(ctx: WriterInterceptorContext) {
    ctx.getEntity match {
      case Some(entity) => ctx.setEntity(entity)
      case None => ctx.setEntity(null)
      case _ =>
    }
    ctx.proceed()
  }
}
