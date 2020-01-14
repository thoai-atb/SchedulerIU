package scheduler;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import util.JSONConvertable;

public class Course implements JSONConvertable {
	
	public ArrayList<Section> sections = new ArrayList<Section> ();
	public String name;

	public Course(String name) {
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Course(JSONObject courseJSON, String name) {
		this(name);
		JSONArray sectionArray = (JSONArray) courseJSON.get("sections");
		for(Object obj : sectionArray.toArray()) {
			JSONObject sectionJSON = (JSONObject) obj;
			sections.add(new Section(sectionJSON));
		}
	}

	public Course clone() {
		Course c = new Course(name);
		for(int i = 0; i<sections.size(); i++) {
			c.sections.add(sections.get(i).clone());
		}
		return c;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject courseJSON = new JSONObject();
		JSONArray sectionArray = new JSONArray();
		for(int i = 0; i<sections.size(); i++)
			sectionArray.add(sections.get(i).toJSON());
		courseJSON.put("sections", sectionArray);
		return courseJSON;
	}
	
	@Override
	public String toString() {
		return String.format("Course(Name: %s, Sections: %s)", name, sections);
	}
}
