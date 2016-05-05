fun main(args: Array<String>) {
    val s: String? = "hello"
    s?.let { p(it) }

    val s2: String? = null
    s2?.let { p(it) }
}

fun p(s: String) = println(s)
