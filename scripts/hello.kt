fun main(args: Array<String>) {
    println("Hello, korlin!")
    "Hello, kotlin!".println()
    val hello = Hello()   
    hello()
}

fun String.println() = println(this)

class Hello {
    operator fun invoke() = println("Hello, kotlin!")
}
