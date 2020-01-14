package gui.frame;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.tablemodel.CourseSetTableModel;
import scheduler.Course;
import scheduler.CourseSet;
import scheduler.Day;
import scheduler.Section;

public class CourseSetGUI extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private CourseSet courseSet;
	private SchedulerGUI schedulerGUI;
	private JTextField idTextField;
	private JTable table;
	private CourseSetTableModel tableModel;
	private boolean modified = false;
	
	public CourseSetGUI(SchedulerGUI schedulerGUI, CourseSet courseSet) {
		super(courseSet.getName());
		this.schedulerGUI = schedulerGUI;
		this.courseSet = courseSet;
		setSize(600, 300);
		setVisible(true);
		setResizable(false);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent windowEvent) {
		    	if(!modified) {
		    		dispose();
		    		return;
		    	}
		    	
		    	int option = JOptionPane.showConfirmDialog(null, 
			            "Save Course Set?", "Closing Course Set", 
			            JOptionPane.YES_NO_CANCEL_OPTION,
			            JOptionPane.QUESTION_MESSAGE);
		    	switch(option) {
			    	case JOptionPane.YES_OPTION:
			    		save();
			    		dispose();
			    		break;
			    	case JOptionPane.NO_OPTION:
			    		dispose();
			    	case JOptionPane.CANCEL_OPTION:
			    	default:	
		    	}
		    }
		});

		this.setLayout(new GridLayout(0, 2));
		
		JMenuBar menuBar = new JMenuBar();
		JMenu courseSetMenu = new JMenu("Course Set");
		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		courseSetMenu.add(saveMenuItem);
		JMenuItem saveCloseMenuItem = new JMenuItem("Save & Close");
		saveCloseMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
				dispose();
			}
		});
		courseSetMenu.add(saveCloseMenuItem);
		JMenuItem renameMenuItem = new JMenuItem("Rename");
		renameMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rename();
			}
		});
		courseSetMenu.add(renameMenuItem);
		menuBar.add(courseSetMenu);
		this.setJMenuBar(menuBar);
		
		// Control Panel
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 30, 10);
		
		JButton newButton = new JButton("New");
		newButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCourse();
			}
		});
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		controlPanel.add(newButton, c);
		c.gridwidth = 1;
		
		c.insets = new Insets(10, 10, 10, 10);
		
		JLabel idLabel = new JLabel("Course ID:");
		c.gridx = 0;
		c.gridy ++;
		controlPanel.add(idLabel, c);

		idTextField = new JTextField(5);
		c.gridx ++;
		controlPanel.add(idTextField, c);

		JButton modifyButton = new JButton("Modify");
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				modifyCourse();
			}
		});
		c.gridx = 0;
		c.gridy ++;
		controlPanel.add(modifyButton, c);
		
		JButton removeButton = new JButton("Remove");
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCourse();
			}
		});
		c.gridx ++;
		controlPanel.add(removeButton, c);
		
		this.add(controlPanel);

		// Table Panel
		JPanel tablePanel = new JPanel();
		tablePanel.setBorder(BorderFactory.createTitledBorder("Course List"));
		String[] col = {"ID", "Day", "start", "duration"};
		tableModel = new CourseSetTableModel(col, 0);
		table = new JTable(tableModel);
		table.setEnabled(true);
		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateIDField();
			}
		});
		
		tablePanel.setLayout(new BorderLayout());
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		this.add(tablePanel);
		
		loadData();
	}
	
	public void updateModified() {
		modified = true;
	}
	
	private void updateIDField() {
		try {
			String id = tableModel.getID(table.getSelectedRow());
			idTextField.setText(id);
		} catch (ArrayIndexOutOfBoundsException e) {
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void loadData() {
		tableModel.loadData(courseSet.getCourseList());
	}
	
	private void modifyCourse() {
		try {
			int index = Integer.parseInt(idTextField.getText());
			if(index < 0 || index >= courseSet.getCourseList().size()) {
				throw new Exception("Invalid ID");
			}
			
			Course course = createCourse();
			courseSet.getCourseList().remove(index);
			courseSet.getCourseList().add(index, course);
			loadData();
			updateModified();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
	}
	
	private void removeCourse() {
		try {
			String s = idTextField.getText();
			
			int index = s.isBlank()? courseSet.getCourseList().size()-1 : Integer.parseInt(s);
			if(index < 0 || index >= courseSet.getCourseList().size()) {
				throw new Exception("Invalid ID");
			}
			
			courseSet.getCourseList().remove(index);
			idTextField.setText("");
			loadData();
			updateModified();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return;
		}
	}
	
	private void addCourse() {
		try {
			courseSet.addCourse(createCourse());
			loadData();
			updateModified();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
	}
	
	private Course createCourse() throws Exception {
		Course c = new Course(courseSet.getName());
		int numSections = Integer.parseInt(JOptionPane.showInputDialog(courseSet.getName() + ": Enter the number of sections"));
		if(numSections <= 0)
			throw new Exception("The number of sections must be greater than 0");
		
		for(int i = 0; i<numSections; i++) {
			c.sections.add(createSection(i+1));
		}
		
		return c;
	}
	
	private Section createSection(int id) throws Exception {
		Section s = new Section();
	    String dayInput = (String) JOptionPane.showInputDialog(null, "Section #" + id + ": Choose day",
	        null, JOptionPane.QUESTION_MESSAGE, null,
	        Day.dayStrings, // Array of choices
	        Day.dayStrings[1]); // Initial choice
	    s.day = Day.getDay(dayInput);
	    
	    s.start = Integer.parseInt(JOptionPane.showInputDialog("Section #" + id + ": start?"));
	    if(s.start < 1 || s.start > 12) {
	    	throw new Exception("Start is not valid: " + s.start);
	    }
	    	
	    s.duration = Integer.parseInt(JOptionPane.showInputDialog("Section #" + id + ": duration?"));
	    if(s.duration < 1 || s.duration > 12) {
	    	throw new Exception("Duration is not valid: " + s.duration);
	    }
	    
	    return s;
	}
	
	private void rename() {
		String name = JOptionPane.showInputDialog("Enter course set name :", courseSet.getName());
		if(name.isBlank())
			return;
		
		if(schedulerGUI.hasCourseSet(name)) {
			JOptionPane.showMessageDialog(null, "The Course Set name already existed");
			return;
		}
		
		courseSet.setName(name);
		this.setTitle(name);
		updateModified();
	}
	
	private void save() {
		if(schedulerGUI.addCourseSet(courseSet)) {
			schedulerGUI.loadTable();
			schedulerGUI.updateModified();
			modified = false;
		} else {
			JOptionPane.showMessageDialog(null, "Cannot save this Course Set");
		}
	}
}
