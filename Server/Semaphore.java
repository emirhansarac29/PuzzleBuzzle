public class Semaphore {

	int value;

	public Semaphore(int value) {
		this.value = value;
	}

	public Semaphore() {
		this.value = 1;
	}

	public synchronized int getValue() {
		return value;
	}

	public synchronized void request() throws InterruptedException {
		while (value == 0)
			wait();
		value--;
	}

	public synchronized void release() {
		value++;
		notify();
	}

}
