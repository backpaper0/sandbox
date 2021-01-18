package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;

public class ReorderExample2 implements Runnable {

	public static void main(final String[] args) throws Exception {
		final ReorderExample2 r = new ReorderExample2();
		final Thread t = new Thread(r);
		t.start();

		TimeUnit.SECONDS.sleep(1);
		r.stop();
	}

	//volatileを付けてaliveを使用するときは必ずメモリを同期する
	private volatile boolean alive = true;

	@Override
	public void run() {
		long counter = 0L;
		while (isAlive()) {
			counter++;
		}
		System.out.printf("%,d", counter);
	}

	private boolean isAlive() {
		return alive;
	}

	public void stop() {
		alive = false;
		System.out.println("stop");
	}
}
