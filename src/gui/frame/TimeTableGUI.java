
package gui.frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import scheduler.TimeTable;

public class TimeTableGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	DefaultTableModel tableModel;
	JTable jTable;
	JButton nextButton, previousButton;
	JLabel indexLabel;
	int currentIndex = 0;
	ArrayList<TimeTable> tables;
	
	public TimeTableGUI(ArrayList<TimeTable> tables) {
		super("Time Table");
		setSize(600, 300);
		setVisible(true);
		setResizable(false);
		
		this.tables = tables;

		String[] col = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
		tableModel = new DefaultTableModel(col, 0);
		jTable = new JTable(tableModel);
		jTable.setEnabled(false);

		setLayout(new BorderLayout());
		add(new JScrollPane(jTable), BorderLayout.CENTER);
		
		JPanel controller = new JPanel();
		controller.setLayout(new GridLayout(0, 3, 50, 20));
		
		previousButton = new JButton("Previous");
		previousButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentIndex --;
				if(currentIndex < 0)
					currentIndex = 0;
				loadTable();
			} 
		});
		nextButton = new JButton("Next");
		nextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentIndex ++;
				if(currentIndex >= tables.size())
					currentIndex = tables.size() - 1;
				loadTable();
			} 
		});
		
		indexLabel = new JLabel("", JLabel.CENTER);
		controller.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
		controller.add(previousButton);
		controller.add(indexLabel);
		controller.add(nextButton);
		add(controller, BorderLayout.SOUTH);
		
		loadTable();
	}
	
	private void loadTable() {
		tableModel.setRowCount(0);
		if(tables.size() == 0)
			return;
		String[][] table = tables.get(currentIndex).getTable();
		for(int i = 0; i<table.length; i++) {
			Object[] objs = (Object[]) table[i];
			tableModel.addRow(objs);
		}
		
		indexLabel.setText(currentIndex+1 + "/" + tables.size());
	}
	
}
