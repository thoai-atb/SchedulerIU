package gui.tablemodel;

import java.util.ArrayList;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import gui.frame.SchedulerGUI;
import scheduler.CourseSet;

public class SchedulerTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<CourseSet> courseSets = new ArrayList<CourseSet>();
	private SchedulerGUI schedulerGUI;
	
	public SchedulerTableModel(SchedulerGUI schedulerGUI, Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
		this.schedulerGUI = schedulerGUI;
		
		addTableModelListener(new TableModelListener() {
			@Override
			public void tableChanged(TableModelEvent e) {
				for(int i = 0; i<courseSets.size(); i++) {
					schedulerGUI.updateCourseSetEnable(courseSets.get(i).getID(), (boolean) getValueAt(i, 2));
				}
			}
		});
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
        return column == 2;
    }
	
	public void loadTable() {
		courseSets = new ArrayList<CourseSet>();
		setRowCount(0);
		for(CourseSet set : schedulerGUI.getManager().getScheduler().getList()) {
			courseSets.add(set);
			Object[] objs = {set.getName(), set.getAmount(), set.isEnable()};
			addRow(objs);
		}
	}
}