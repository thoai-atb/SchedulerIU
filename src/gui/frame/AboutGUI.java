package gui.frame;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class AboutGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public AboutGUI() {
		super("About Scheduler");
		setSize(350, 300);
		setVisible(true);
		setResizable(false);
		
		JTextArea text = new JTextArea();
		text.setText("\tScheduler\nAuthor: Thoai Ly\nVersion: 1.1.0");
		text.setEditable(false);
		add(text);
		
		text.setBackground(Color.getHSBColor(0, 0, 0.8f));
		text.setFont(new Font("Serif", Font.PLAIN, 20));
	}

}
