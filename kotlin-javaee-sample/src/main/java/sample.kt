package sample

import javax.enterprise.context.*
import javax.enterprise.inject.Typed
import javax.inject.*
import javax.ws.rs.*
import javax.ws.rs.core.*

@ApplicationPath("api")
class SampleApp : Application()

interface HelloApi {

    val service: HelloService

    @GET
    @Produces("text/plain")
    fun say(@QueryParam("name") @DefaultValue("world") name: String): String = service.say(name)
}

@RequestScoped
@Typed(HelloApi::class)
@Path("hello")
class HelloApiImpl @Inject constructor(
    override val service: HelloService
) : HelloApi

@Dependent
class HelloService {
    fun say(name: String): String = "Hello, ${name}!"
}
