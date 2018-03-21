public class AnonymousClass {
    public static void main(String[] args) {
        var x = new Object() {};
        System.out.println(x.getClass());
    }
}
