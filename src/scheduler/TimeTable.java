package scheduler;

import java.util.ArrayList;

public class TimeTable implements Comparable<TimeTable> {
	
	ArrayList<Course> courses = new ArrayList<Course> ();
	
	public boolean push(Course course) {
		for(Course c : courses) {
			if(conflict(course, c)) {
				return false;
			}
		}
		
		courses.add(course);
		return true;
	}
	
	public boolean conflict(Course c1, Course c2) {
		for(Section a : c1.sections)
			for(Section b : c2.sections) {
				if(conflict(a, b))
					return true;
			}
		return false;
	}
	
	public boolean conflict(Section a, Section b) {
		if(a.day != b.day)
			return false;
		return a.start < b.start + b.duration && b.start < a.start + a.duration;
	}
	
	public String[][] getTable(){
		String[][] table = new String[12][Day.totalDays()];
		
		for(Course course : courses) {
			for(Section section : course.sections) {
				for(int i = 0; i<section.duration; i++) {
					table[section.start-1 + i][Day.getOrder(section.day)] = course.name; 
				}
			}
		}
		
		return table;
	}
	
	public int getNumFreeDays() {
		boolean[] occupied = new boolean[Day.values().length];
		for(Course course : courses)
			for(Section section : course.sections) 
				occupied[Day.getOrder(section.day)] = true;
		
		int count = 0;
		for(int i = 0; i<occupied.length; i++) 
			count += occupied[i]? 0 : 1;
		return count;
	}
	
	@Override
	public String toString() {
		return null;
	}

	@Override
	public int compareTo(TimeTable other) {
		return -Integer.compare(getNumFreeDays(), other.getNumFreeDays());
	}
}
