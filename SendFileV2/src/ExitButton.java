import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ExitButton extends JButton implements ActionListener {
	private Receiver receiver;

	public ExitButton(Receiver receiver) {
		super("Quit");
		this.receiver = receiver;
		this.addActionListener(this);
	}

	public void actionPerformed(ActionEvent arg0) {
		receiver.interrupt();
		try {
			receiver.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.exit(0);

	}

}
