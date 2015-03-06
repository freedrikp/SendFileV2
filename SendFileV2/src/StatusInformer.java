import javax.swing.ProgressMonitor;

public abstract class StatusInformer {

	private long count;
	private int last;
	private ProgressMonitor pm;
	public static final String SEND = "Sending file";
	public static final String RECEIVE = "Receiving file";
	protected String title;
	private long currentTimestamp, previousTimestamp;
	private double speed;
	private long bytes;

	public StatusInformer(String title) {
		count = 0;
		last = 0;
		this.title = title;
		currentTimestamp = previousTimestamp = System.currentTimeMillis();
		speed = 0;
		bytes = 0;
	}

	public final void inform(long size, int bytesRead) {
		currentTimestamp = System.currentTimeMillis();
		count += bytesRead;

		int percent = (int) Math
				.round((((double) count) / ((double) size)) * 100);

		long diff = currentTimestamp - previousTimestamp;
		bytes += bytesRead;
		if (diff > 250) {
			speed = Math.round(((double) bytes) / diff);
			bytes = 0;
			previousTimestamp = currentTimestamp;
		}

		if (percent - last > 1) {
			println(percent + " % \t " + speed + " KB/s");
			last = percent;
		}

		extraInfo(percent, speed);
	}

	public abstract void extraInfo(int percent, double speed);

	public void reset() {
		count = 0;
		last = 0;
	}

	public abstract void println(String line);

}
