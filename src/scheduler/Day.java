package scheduler;

public enum Day {
	MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;
	public static String[] dayStrings = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	public static Day getDay(String s) {
		for(int i = 0; i<dayStrings.length; i++) {
			if(s.equalsIgnoreCase(dayStrings[i]))
				return values()[i];
		}
		
		return null;
	}
	
	public static int totalDays() {
		return values().length;
	}
	
	public static String toString(Day day) {
		return dayStrings[getOrder(day)];
	}
	
	public static int getOrder(Day day) {
		for(int i = 0; i<values().length; i++)
			if(day == values()[i])
				return i;
		return -1;
	}
}
