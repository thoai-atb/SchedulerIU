package scheduler;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.IDGenerator;
import util.JSONConvertable;

public class Scheduler implements JSONConvertable {
	
	ArrayList<CourseSet> courseSets = new ArrayList<CourseSet> ();
	IDGenerator idGenerator;
	
	public Scheduler() {
		idGenerator = new IDGenerator();
	}
	
	public Scheduler(JSONObject schedulerJSON) {
		JSONArray courseSetArray = (JSONArray) schedulerJSON.get("courseSets");
		for(Object obj : courseSetArray.toArray()) {
			JSONObject courseSetJSON = (JSONObject) obj;
			courseSets.add(new CourseSet(courseSetJSON));
		}
		
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(CourseSet c : courseSets)
			ids.add(c.id);
		idGenerator = new IDGenerator(ids);
	}
	
	public int createID() {
		return idGenerator.createID();
	}
	
	public ArrayList<CourseSet> getList() {
		return courseSets;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject jsonScheduler = new JSONObject();
		JSONArray courseSetArray = new JSONArray();
		for(CourseSet c : courseSets) 
			courseSetArray.add(c.toJSON());
		jsonScheduler.put("courseSets", courseSetArray);
		return jsonScheduler;
	}
	
	@Override
	public String toString() {
		return String.format("Scheduler(CourseSets:%s)", courseSets.toString());
	}
	
	public CourseSet getCourseSet(String name) {
		for(CourseSet c : getList())
			if(c.getName().equalsIgnoreCase(name))
				return c;
		return null;
	}
	
	public boolean hasCourseSet(String name) {
		return getCourseSet(name) != null;
	}
	
	public CourseSet getDraftCourseSet(String name) {
		return new CourseSet(name, IDGenerator.DRAFT_ID);
	}
	
	public boolean updateCourseSetEnable(int id, boolean enable) {
		for(CourseSet c : courseSets)
			if(c.id == id) {
				return c.setEnable(enable);
			}
		return false;
	}
	
	public boolean addCourseSet(CourseSet cs) {
		if(idGenerator.isDraftID(cs.id)) {
			if(hasCourseSet(cs.getName()))
				return false;
			cs.id = idGenerator.createID();
			courseSets.add(cs);
			return true;
		} else {
			for(int i = 0; i<courseSets.size(); i++) {
				CourseSet courseSet = courseSets.get(i);
				if(courseSet.id == cs.id) {
					courseSets.remove(i);
					courseSets.add(i, cs);
					return true;
				}
			}
			return false;
		}
	}
	
	public void removeCourseSet(String name) {
		for(int i = 0; i<courseSets.size(); i++) {
			CourseSet courseSet = courseSets.get(i);
			if(courseSet.getName().equalsIgnoreCase(name)) {
				courseSets.remove(courseSet);
				idGenerator.removeID(courseSet.id);
				break;
			}
		}
	}
	
	public ArrayList<TimeTable> arrange() {
		
		ArrayList<CourseSet> enabledList = new ArrayList<CourseSet>();
		
		for(CourseSet c : courseSets)
			if(c.isEnable())
				enabledList.add(c);
		
		if(enabledList.size() == 0)
			return null;
		
		ArrayList<TimeTable> tables = new ArrayList<TimeTable> ();

		for(CourseSet c : enabledList)
			if(c.getCourseList().size() == 0)
				return tables;
		
		int[] indices = new int[enabledList.size()];
		
		boolean over = false;
		while(!over) {
			TimeTable timeTable = new TimeTable();
			boolean valid = true;
			for(int i = 0; i<enabledList.size(); i++) {
				Course course = enabledList.get(i).getCourseList().get(indices[i]);
				if(!timeTable.push(course)) {
					valid = false;
					break;
				}
			}
			
			if(valid)
				tables.add(timeTable);
			
			for(int i = 0; i<indices.length; i++) {
				indices[i] ++;
				if(indices[i] < enabledList.get(i).getCourseList().size())
					break;
				
				indices[i] = 0;
				if(i == indices.length-1)
					over = true;
			}
		}
		
		tables.sort(null);
		return tables;
	}
}
