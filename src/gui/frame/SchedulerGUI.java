package gui.frame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import driver.SchedulerManager;
import gui.panel.SchedulerControlPanel;
import gui.panel.SchedulerTablePanel;
import scheduler.CourseSet;

public class SchedulerGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private SchedulerTablePanel tablePanel;
	private SchedulerControlPanel controlPanel;
	private boolean modified = false;
	private SchedulerManager manager;
	
	public SchedulerGUI(SchedulerManager manager) {
		super("Scheduler - 1.1.0");
		this.manager = manager;
		
		setSize(600, 300);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	if(!modified) {
		    		System.exit(0);
		    		return;
		    	}
		    	
		    	int option = JOptionPane.showConfirmDialog(null, 
			            "Save file?", "Close Scheduler", 
			            JOptionPane.YES_NO_CANCEL_OPTION,
			            JOptionPane.QUESTION_MESSAGE);
		    	switch(option) {
			    	case JOptionPane.YES_OPTION:
			    		save();
			    		System.exit(0);
			    		break;
			    	case JOptionPane.NO_OPTION:
			    		System.exit(0);
			    	case JOptionPane.CANCEL_OPTION:
			    	default:	
		    	}
		    }
		});
		
		this.setLayout(new GridLayout(0, 2));
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		fileMenu.add(saveMenuItem);
		menuBar.add(fileMenu);
		
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutMenuItem = new JMenuItem("About Scheduler");
		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				about();
			}
		});
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		
		this.setJMenuBar(menuBar);
		
		controlPanel = new SchedulerControlPanel(this);
		this.add(controlPanel);

		tablePanel = new SchedulerTablePanel(this);
		this.add(tablePanel);
		
		// load data
		loadTable();
	}
	
	public void updateModified() {
		modified = true;
	}
	
	public SchedulerManager getManager() {
		return manager;
	}
	
	public boolean hasCourseSet(String name) {
		return manager.getScheduler().hasCourseSet(name);
	}
	
	public boolean addCourseSet(CourseSet courseSet) {
		return manager.getScheduler().addCourseSet(courseSet);
	}
	
	public void loadTable() {
		tablePanel.loadTable();
	}
	
	public void updateNameField(String name) {
		controlPanel.updateNameField(name);
	}

	public void updateCourseSetEnable(int id, boolean b) {
		if(manager.getScheduler().updateCourseSetEnable(id, b))
			updateModified();
	}
	
	public void save() {
		manager.saveSchedulerJSON();
		JOptionPane.showMessageDialog(null, "File saved successfully!");
		modified = false;
	}
	
	public void about() {
		new AboutGUI();
	}
}
