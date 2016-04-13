interface Foo {
    val hoge: String
    val fuga: Int
    fun foobar() = hoge + fuga
}

class FooImpl(
    override val hoge: String,
    override val fuga: Int
) : Foo {
}
