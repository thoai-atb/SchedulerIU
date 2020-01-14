package gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.frame.CourseSetGUI;
import gui.frame.SchedulerGUI;
import gui.frame.TimeTableGUI;
import scheduler.CourseSet;
import scheduler.Scheduler;
import scheduler.TimeTable;

public class SchedulerControlPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private SchedulerGUI schedulerGUI;
	private JTextField nameTextField;
	
	public SchedulerControlPanel(SchedulerGUI schedulerGUI) {
		this.schedulerGUI = schedulerGUI;
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 5, 10, 5);
		
		JLabel nameLabel = new JLabel("Course Set :");
		c.gridx = 0;
		c.gridy = 0;
		add(nameLabel, c);

		nameTextField = new JTextField(10);
		c.gridwidth = 2;
		c.gridx ++;
		c.gridy = 0;
		add(nameTextField, c);
		c.gridwidth = 1;

		JButton newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				newCourseSet();
			}
		});
		c.gridx = 0;
		c.gridy ++;
		add(newButton, c);

		JButton modify = new JButton("Modify");
		modify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modifyCourseSet();
			}
		});
		c.gridx ++;
		add(modify, c);
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCourseSet();
			}
		});
		c.gridx ++;
		add(removeButton, c);
		
		JButton arrangeButton = new JButton("Arrange !");
		arrangeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arrange();
			}
		});
		c.insets = new Insets(40, 10, 10, 10);
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridy ++;
		c.gridx = 1;
		c.gridwidth = 1;
		add(arrangeButton, c);
	}
	
	private void newCourseSet() {
		String name = nameTextField.getText();
		if(name.isBlank()) {
			JOptionPane.showMessageDialog(null, "Please enter the Course Set name!");
			return;
		}
		
		if(schedulerGUI.hasCourseSet(name)) {
			JOptionPane.showMessageDialog(null, "The Course Set name already existed");
			return;
		}
		
		new CourseSetGUI(schedulerGUI, schedulerGUI.getManager().getScheduler().getDraftCourseSet(name));
	}
	
	private void modifyCourseSet() {
		String name = nameTextField.getText();
		if(name.isBlank()) {
			JOptionPane.showMessageDialog(null, "Please enter the Course Set name!");
			return;
		}
		
		CourseSet toModify = schedulerGUI.getManager().getScheduler().getCourseSet(name);
		
		if(toModify == null) {
			JOptionPane.showMessageDialog(null, "There is no such Course Set!");
			return;
		}

		new CourseSetGUI(schedulerGUI, toModify.clone());
	}
	
	private void removeCourseSet() {
		String name = nameTextField.getText();
		if(name.isBlank()) {
			JOptionPane.showMessageDialog(null, "Please enter the Course Set name!");
			return;
		}
		
		Scheduler scheduler = schedulerGUI.getManager().getScheduler();
		scheduler.removeCourseSet(name);
		
		nameTextField.setText("");
		schedulerGUI.loadTable();
		schedulerGUI.updateModified();
	}
	
	private void arrange() {
		ArrayList<TimeTable> tables = schedulerGUI.getManager().getScheduler().arrange();
		
		if(tables == null) {
			JOptionPane.showMessageDialog(null, "No courses to arrange!");
			return;
		}
		
		if(tables.size() == 0) {
			JOptionPane.showMessageDialog(null, "No solutions");
			return;
		}
		
		new TimeTableGUI(tables);
	}
	
	public void updateNameField(String name) {
		nameTextField.setText(name);
	}
}
