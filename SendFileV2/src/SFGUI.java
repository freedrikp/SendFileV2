

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class SFGUI extends JFrame implements WindowListener {
	private JTextArea ta;
	private Receiver receiver;
	
	public SFGUI() {
		super("SendFile");
		receiver = new Receiver(new GUIInformer(GUIInformer.RECEIVE,
				this));
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		JPanel panel = new JPanel();
		ta = new JTextArea(5, 35);
		ta.setText("");
		JScrollPane scrollPane = new JScrollPane(ta);
		ta.setEditable(false);
		panel.add(scrollPane);
		panel.add(new SendButton(this));
		panel.add(new ExitButton(receiver));
		add(panel);
		pack();
		receiver.start();
		addWindowListener(this);
	}

	public void append(String line) {
		ta.append(line);
		ta.setCaretPosition(ta.getDocument().getLength());
		pack();
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void windowClosing(WindowEvent e) {
		receiver.interrupt();
		try {
			receiver.join(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.exit(0);
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
