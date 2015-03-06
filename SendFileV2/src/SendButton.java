import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SendButton extends JButton implements ActionListener {
	private SFGUI sfgui;

	public SendButton(SFGUI sfgui) {
		super("Send a file!");
		addActionListener(this);
		this.sfgui = sfgui;
	}

	public void actionPerformed(ActionEvent arg0) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = jfc.showOpenDialog(null);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = jfc.getSelectedFile();
			String dest = JOptionPane.showInputDialog(sfgui,
					"Where do you want to send it?", "Destination",
					JOptionPane.QUESTION_MESSAGE);
			if (dest != null && !dest.isEmpty()) {
				Sender sender = new Sender(dest, file, new GUIInformer(
						GUIInformer.SEND, sfgui));
				sender.start();
			}

		}

	}

}
