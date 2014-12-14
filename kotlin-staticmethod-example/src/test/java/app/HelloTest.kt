package app

import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTest
import org.junit.Test as test

import kotlin.test.expect

public class HelloTest : JerseyTest() {

  test fun testSay() {
    expect("Hello, world!") {
      target("hello")!!.queryParam("name", "world")!!.request()!!.get(javaClass<String>())!!
    }
  }

  override fun configure() = ResourceConfig().register(javaClass<Hello>())

}

