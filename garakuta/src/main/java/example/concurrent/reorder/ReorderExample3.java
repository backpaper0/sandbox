package example.concurrent.reorder;

import java.util.concurrent.TimeUnit;

public class ReorderExample3 implements Runnable {

	public static void main(final String[] args) throws Exception {
		final ReorderExample3 r = new ReorderExample3();
		final Thread t = new Thread(r);
		t.start();

		TimeUnit.SECONDS.sleep(1);
		r.stop();
	}

	private boolean alive = true;

	@Override
	public void run() {
		long counter = 0L;
		while (isAlive()) {
			counter++;
		}
		System.out.printf("%,d", counter);
	}

	//aliveを使用している箇所をsynchronizedメソッド/ブロックにしてメモリを同期する

	private synchronized boolean isAlive() {
		return alive;
	}

	public void stop() {
		synchronized (this) {
			alive = false;
		}
		System.out.println("stop");
	}
}
