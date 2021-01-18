package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;

public class ReorderExample7 implements Runnable {

	public static void main(final String[] args) throws Exception {
		final ReorderExample7 r = new ReorderExample7();
		final Thread t = new Thread(r);
		t.start();

		TimeUnit.SECONDS.sleep(1);
		r.stop();
	}

	private boolean alive = true;
	private volatile String s = "";

	@Override
	public void run() {
		long counter = 0L;
		while (isAlive()) {
			counter++;
		}
		System.out.printf("%,d", counter);
	}

	private boolean isAlive() {
		s.length();
		return alive;
	}

	public void stop() {
		s.length();
		alive = false;
		System.out.println("stop");
	}
}
