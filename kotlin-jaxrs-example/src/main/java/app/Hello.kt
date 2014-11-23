package app

import javax.ws.rs.core.MediaType
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam as q

Path("hello")
Produces(MediaType.TEXT_PLAIN)
class Hello {

    GET fun get(q("name") name: String): String = "Hello, ${name}!"
}
