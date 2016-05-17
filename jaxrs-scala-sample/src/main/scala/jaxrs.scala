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
