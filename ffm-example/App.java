import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;

public class App {

    public static void main(String[] args) throws Throwable {
        var linker = Linker.nativeLinker();
        var lookup = SymbolLookup.libraryLookup("./libffmexample.so", Arena.global());

        var function = FunctionDescriptor.of(
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT,
                ValueLayout.JAVA_INT);
        var address = lookup.find("add").get();
        var add = linker.downcallHandle(address, function);

        int result = (int) add.invoke(3, 5);
        System.out.println(result); // 8
    }
}