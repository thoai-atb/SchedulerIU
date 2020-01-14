package gui.tablemodel;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import scheduler.Course;
import scheduler.Day;
import scheduler.Section;

public class CourseSetTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 1L;
	
	public CourseSetTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
    }
	
	public String getID(int row) {
		Object id = getValueAt(row, 0);
		while(id instanceof String) {
			id = getValueAt(--row, 0);
		}
		
		return Integer.toString((int) id);
	}
	
	public void loadData(List<Course> arr) {
		setRowCount(0);
		for(Course c : arr) {
			for(Section section : c.sections) {
				Object[] objs = {c.sections.indexOf(section)==0? arr.indexOf(c) : "",
						Day.toString(section.day),
						section.start,
						section.duration};
				
				addRow(objs);
			}
		}
	}
}
