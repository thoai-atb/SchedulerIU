package driver;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import gui.frame.SchedulerGUI;

public class Driver {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					String path = "data.scdl";
					path = (String) JOptionPane.showInputDialog(null, "Choose file name: ", 
							"Scheduler", JOptionPane.QUESTION_MESSAGE, null, null, path);
					SchedulerManager manager = new SchedulerManager(path);
					manager.loadSchedulerJSON();
					new SchedulerGUI(manager);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});
	}
	
	
}
