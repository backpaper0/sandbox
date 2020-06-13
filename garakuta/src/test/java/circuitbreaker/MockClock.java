package circuitbreaker;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;

public class MockClock implements LongSupplier {

	private final AtomicLong now = new AtomicLong(0);

	public void plus(long amountToAdd, TimeUnit unit) {
		now.addAndGet(unit.toMillis(amountToAdd));
	}

	@Override
	public long getAsLong() {
		return now.get();
	}
}
