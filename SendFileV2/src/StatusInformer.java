

import javax.swing.ProgressMonitor;


public abstract class StatusInformer {

	private long count;
	private int last;
	private ProgressMonitor pm;
	public static final String SEND = "Sending file";
	public static final String RECEIVE = "Receiving file";
	protected String title;
	private long t1, t2;
	private double speed;
	private long bytes;
	
	public StatusInformer(String title){
		count = 0;
		last = 0;
		this.title = title;
		t1 = t2 = System.currentTimeMillis();
		speed = 0;
		bytes = 0;
	}
	
	public final void inform(long size, int bytesRead){
		t1 = System.currentTimeMillis();
		count += bytesRead;

		int percent = (int) Math
				.round((((double) count) / ((double) size)) * 100);
		
		long diff = t1 - t2;
		bytes += bytesRead;
		if (diff > 250) {
			speed = Math.round(((double) bytes) / diff);
			bytes = 0;
			t2 = t1;
		}
		
		if (percent - last > 1) {
			println(percent + " % \t " + speed + " KB/s");
			last = percent;
		}
		
		extraInfo(percent, speed);
	}

	public abstract void extraInfo(int percent, double speed);

	public void reset(){
		count = 0;
		last = 0;
	}
	
	public abstract void println(String line);

}
