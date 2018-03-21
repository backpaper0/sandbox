public class AnonymousClass {
    public static void main(String[] args) {
        var x = new Object() {
            void adhoc() {
            }
        };
        x.adhoc();
        System.out.println(x.getClass());
    }
}
