import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

static void printResult(int value) {
    System.out.printf("[Java] arg = %s%n", value);
    System.out.printf("Thread = %s%n", Thread.currentThread());
    new Throwable().printStackTrace(System.out);
}

void main() throws Throwable {
    var linker = Linker.nativeLinker();
    var lookup = SymbolLookup.libraryLookup("./libffmexample.so", Arena.global());

    var printResultMethod = MethodHandles.lookup().findStatic(this.getClass(), "printResult",
            MethodType.methodType(void.class, int.class));
    var callbackDescriptor = FunctionDescriptor.ofVoid(ValueLayout.JAVA_INT);
    var callbackStub = linker.upcallStub(printResultMethod, callbackDescriptor, Arena.global());

    var addWithCallbackDescriptor = FunctionDescriptor.of(
            ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT,
            ValueLayout.JAVA_INT,
            ValueLayout.ADDRESS);
    var addWithCallbackAddress = lookup.find("addWithCallback").get();
    var addWithCallback = linker.downcallHandle(addWithCallbackAddress, addWithCallbackDescriptor);

    addWithCallback.invoke(3, 5, callbackStub);
}
