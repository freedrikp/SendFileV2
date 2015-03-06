

import javax.swing.ProgressMonitor;

public class GUIInformer extends StatusInformer {

	private ProgressMonitor pm;
	private SFGUI sfgui;

	public GUIInformer(String title, SFGUI sfgui) {
		super(title);
		this.sfgui = sfgui;
		pm = new ProgressMonitor(sfgui, title, "", 0, 100);
		pm.setMillisToPopup(0);

	}

	public void reset() {
		super.reset();
		pm = new ProgressMonitor(sfgui, title, "", 0, 100);
	}

	public void println(String line) {
		sfgui.append(line + "\n");
		System.out.println(line);
	}

	public void extraInfo(int percent, double speed) {
		pm.setProgress(percent);
		pm.setNote(percent + " % completed! \t " + speed + " KB/s");
	}

}
