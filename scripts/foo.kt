interface Foo {
    val hoge: String
    val fuga: Int
    fun foobar() = hoge + fuga
}

//@ApplicationScoped
class FooImpl(
    //@Inject
    override val hoge: String,
    //@Inject
    override val fuga: Int
) : Foo {
}
