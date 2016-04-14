package sample

import javax.annotation.*
import javax.enterprise.concurrent.*
import javax.enterprise.context.*
import javax.enterprise.inject.Typed
import javax.enterprise.inject.Produces as CdiProduces
import javax.inject.*
import javax.ws.rs.*
import javax.ws.rs.container.*
import javax.ws.rs.core.*

@ApplicationPath("api")
class SampleApp : Application()

interface HelloApi {

    val service: HelloService
    val executor: ManagedExecutorService

    @GET
    @Produces("text/plain")
    fun say(@QueryParam("name") @DefaultValue("world") name: String, @Suspended ar: AsyncResponse): Unit {
        executor.submit(Runnable {
            val said = service.say(name)
            ar.resume(said)
        })
    }
}

@RequestScoped
@Typed(HelloApi::class)
@Path("hello")
class HelloApiImpl @Inject constructor(
    override val service: HelloService,
    override val executor: ManagedExecutorService
) : HelloApi

@Dependent
class HelloService {
    fun say(name: String): String = "Hello, ${name}!"
}

@Dependent
class Resources {

    @Resource
    lateinit private var executor: ManagedExecutorService

    @CdiProduces
    fun executor(): ManagedExecutorService = executor
}
