String pythonCode = """
        import time
        time.sleep(1)
        print('Hello Python process')
        time.sleep(.5)
        """;

void main() throws Exception {
    var cdl = new CountDownLatch(1);
    var pb = new ProcessBuilder("uv", "run", "python", "-c", pythonCode).inheritIO();
    IO.println("1 " + Thread.currentThread());
    var p = pb.start();
    p.onExit().thenAccept(a -> {
        IO.println(a.toString() + " " + Thread.currentThread());
        cdl.countDown();
    });
    IO.println("2 " + Thread.currentThread());
    cdl.await();
}