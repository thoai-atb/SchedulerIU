package scheduler;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.JSONConvertable;

public class CourseSet implements JSONConvertable {
	
	ArrayList<Course> courses = new ArrayList<Course>();
	String name;
	int id;
	boolean enable = true;
	
	public CourseSet(JSONObject courseSetJSON) {
		this((String) courseSetJSON.get("name"), ((Long) courseSetJSON.get("id")).intValue());
		enable = (boolean) courseSetJSON.get("enable");
		JSONArray courseArray = (JSONArray) courseSetJSON.get("courses");
		for(Object obj : courseArray.toArray()) {
			JSONObject courseJSON = (JSONObject) obj;
			courses.add(new Course(courseJSON, name));
		}
	}
	
	public CourseSet(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public boolean isEnable() {
		return enable;
	}
	
	public int getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getAmount() {
		return courses.size();
	}
	
	public ArrayList<Course> getCourseList(){
		return courses;
	}
	
	public boolean setEnable(boolean enable) {
		boolean changed = this.enable != enable;
		this.enable = enable;
		return changed;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addCourse(Course course) {
		courses.add(course);
	}
	
	public CourseSet clone() {
		CourseSet c = new CourseSet(name, id);
		for(Course course : courses) {
			c.courses.add(course.clone());
		}
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject courseSetJSON = new JSONObject();
		courseSetJSON.put("name", name);
		courseSetJSON.put("id", id);
		courseSetJSON.put("enable", enable);
		JSONArray courseArray = new JSONArray();
		for(Course c : courses) {
			courseArray.add(c.toJSON());
		}
		courseSetJSON.put("courses", courseArray);
		return courseSetJSON;
	}
	
	@Override
	public String toString() {
		return String.format("CourseSet(Name: %s, ID: %d, Courses: %s)", name, id, courses);
	}
}
