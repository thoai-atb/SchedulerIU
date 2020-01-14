package scheduler;
import org.json.simple.JSONObject;

import util.JSONConvertable;

public class Section implements JSONConvertable {
	
	public Day day;
	public int start, duration;
	
	public Section() {
	}
	
	public Section(JSONObject sectionJSON) {
		this(Day.getDay((String) sectionJSON.get("day")), 
				((Long) sectionJSON.get("start")).intValue(),
				((Long) sectionJSON.get("duration")).intValue());
	}
	
	public Section (Day day, int start, int duration) {
		this.day = day;
		this.start = start;
		this.duration = duration;
	}
	
	public Section clone() {
		Section s = new Section();
		s.day = day;
		s.start = start;
		s.duration = duration;
		return s;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSON() {
		JSONObject sectionJSON = new JSONObject();
		sectionJSON.put("day", Day.toString(day));
		sectionJSON.put("start", start);
		sectionJSON.put("duration", duration);
		return sectionJSON;
	}
	
	@Override
	public String toString() {
		return String.format("Section(start: %d, duration: %d)", start, duration);
	}
}
