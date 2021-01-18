package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;

public class ReorderExample8 implements Runnable {

	public static void main(final String[] args) throws Exception {
		final ReorderExample8 r = new ReorderExample8();
		final Thread t = new Thread(r);
		t.start();

		TimeUnit.SECONDS.sleep(1);
		r.stop();
	}

	private boolean alive = true;
	private final StringBuffer buf = new StringBuffer();

	@Override
	public void run() {
		long counter = 0L;
		while (isAlive()) {
			counter++;
		}
		System.out.printf("%,d", counter);
	}

	private boolean isAlive() {
		buf.length();
		return alive;
	}

	public void stop() {
		buf.length();
		alive = false;
		System.out.println("stop");
	}
}
