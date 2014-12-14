package app

import javax.ws.rs.GET as get
import javax.ws.rs.Path as path
import javax.ws.rs.QueryParam as q

path("hello")
public class Hello {
  get fun say(q("name") name: ValueObj) = "Hello, " + name.value + "!"
}

