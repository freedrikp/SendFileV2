

public class TerminalInformer extends StatusInformer {

	public TerminalInformer(String title) {
		super(title);
	}

	public void extraInfo(int percent, double speed) {
	}

	public void println(String line) {
		System.out.println(line);

	}

}
