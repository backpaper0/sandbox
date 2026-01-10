void main() throws Exception {
    var classfile = ClassFile.of().build(ClassDesc.of("HelloImpl"), classBuilder -> classBuilder

            // implements Hello
            .withInterfaces(classBuilder.constantPool().classEntry(ClassDesc.of("Hello")))

            // Constructor
            .withMethod(ConstantDescs.INIT_NAME, ConstantDescs.MTD_void, ClassFile.ACC_PUBLIC,
                    methodBuilder -> methodBuilder.withCode(codeBuilder -> codeBuilder
                            .aload(0)
                            .invokespecial(ConstantDescs.CD_Object, ConstantDescs.INIT_NAME,
                                    ConstantDescs.MTD_void)
                            .return_()))

            // sayHello method
            .withMethod("sayHello", MethodTypeDesc.of(
                    ConstantDescs.CD_String,
                    ConstantDescs.CD_String),
                    ClassFile.ACC_PUBLIC, methodBuilder -> methodBuilder.withCode(codeBuilder -> codeBuilder
                            .ldc(classBuilder.constantPool().stringEntry("Hello, %s!"))
                            .iconst_1()
                            .anewarray(ConstantDescs.CD_Object)
                            .dup()
                            .iconst_0()
                            .aload(1)
                            .aastore()
                            .invokevirtual(ConstantDescs.CD_String, "formatted",
                                    MethodTypeDesc.of(ConstantDescs.CD_String,
                                            ConstantDescs.CD_Object.arrayType()))
                            .areturn())));

    var classLoader = new ClassLoader() {
        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            if (name.equals("HelloImpl")) {
                return defineClass("HelloImpl", classfile, 0, classfile.length);
            }
            throw new ClassNotFoundException(name);
        }
    };
    var clazz = Class.forName("HelloImpl", false, classLoader);
    var hello = (Hello) clazz.getConstructor().newInstance();
    IO.println(hello.sayHello("World"));
}
