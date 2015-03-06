public class SendFile {

	public static void main(String[] args) {
		if (args.length > 0 && args[0].equals("receiver")) {
			Thread receiver = new Receiver(new TerminalInformer(
					StatusInformer.RECEIVE));
			receiver.start();
		} else {
			new SFGUI();
		}
	}

}
