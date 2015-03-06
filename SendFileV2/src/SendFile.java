



public class SendFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Thread receiver = new Receiver();
		//receiver.start();
//		String film ="E:/Filmer/Star.Trek-All.11.Films.1080p.Bluray.x264.AC3-5.1-PeN/Star.Trek.11.Star.Trek.2009.BluRay.1080p.x264.AC3-5.1-PeN.mkv";
//		String txt = "C:/test.txt";
//		String tv ="E:/TV-Serier/According to jim/According to Jim Season 4/according.to.jim.s04e01.avi";
//		Thread sender = new Sender("127.0.0.1", new File(film));
//		sender.start();
		if (args.length > 0 && args[0].equals("receiver")){
			Thread receiver = new Receiver(new TerminalInformer(StatusInformer.RECEIVE));
			receiver.start();
		}else{
			
			new SFGUI();
		}
	}

}
