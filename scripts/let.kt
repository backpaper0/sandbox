fun main(args: Array<String>) {
    val s: String? = "hello"
    s?.let { p(it) }

    val s2: String? = null
    s2?.let { p(it) }

    p2(twice(len("hello")))

    "hello".let { len(it).let { twice(it).let { p2(it) } } }

    "hello" x { len(it) } x { twice(it) } x { p2(it) }

    "hello" x (::len) x (::twice) x (::p2)
}

fun p(s: String) = println(s)

fun len(s: String): Int = s.length

fun twice(i: Int): Int = i * 2

fun p2(i: Int) = println(i)

infix fun <T, U> T.x(f: (T) -> U): U = f(this)
