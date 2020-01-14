package gui.panel;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.frame.SchedulerGUI;
import gui.tablemodel.SchedulerTableModel;

public class SchedulerTablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private SchedulerGUI schedulerGUI;
	private JTable table;
	private SchedulerTableModel tableModel;
	
	public SchedulerTablePanel(SchedulerGUI schedulerGUI) {
		this.schedulerGUI = schedulerGUI;
		
		setBorder(BorderFactory.createTitledBorder("Course Set List"));
		String[] col = {"Name", "Amount", "Enable"};
		
		tableModel = new SchedulerTableModel(schedulerGUI, col, 0);
		
		table = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public Class<?> getColumnClass(int column) {
				if(column == 2)
					return Boolean.class;
				else
					return super.getColumnClass(column);
			}
		};
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateNameField();
			}
		});
		
		setLayout(new BorderLayout());
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
	
	public void loadTable() {
		tableModel.loadTable();
	}
	
	public void updateNameField() {
		try {
			String name = (String) tableModel.getValueAt(table.getSelectedRow(), 0);
			schedulerGUI.updateNameField(name);
		} catch (java.lang.ArrayIndexOutOfBoundsException e) {
			
		} catch (Exception e) {
			throw e;
		}
	}
}
