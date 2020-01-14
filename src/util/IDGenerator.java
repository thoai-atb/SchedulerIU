package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IDGenerator {

	Random random = new Random();
	ArrayList<Integer> usedIDs = new ArrayList<Integer>();
	
	public static final int DRAFT_ID = -1;
	
	public IDGenerator() {}
	
	public IDGenerator(List<Integer> usedIDs) {
		this.usedIDs.addAll(usedIDs);
	}
	
	public boolean isDraftID(int id) {
		return id == DRAFT_ID;
	}
	
	public int createID() {
		int id = 0;
		while(usedIDs.contains(id))
			id++;
		addID(id);
		return id;
	}
	
	public void addID(int id) {
		usedIDs.add(id);
	}
	
	public void removeID(int id) {
		usedIDs.remove(id);
	}
	
}