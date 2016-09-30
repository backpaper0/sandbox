public class Sample3 {
    public void trySample() {
        try {
            System.out.println("到達可能");
        } catch(RuntimeException e) {
            System.out.println("到達可能");
        } catch(IllegalArgumentException e) {
            System.out.println("到達不能！");
        }
    }
}
