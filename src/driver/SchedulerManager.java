package driver;

import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import scheduler.Scheduler;

public class SchedulerManager {
	
	private Scheduler scheduler;
	private String path = "";
	
	public SchedulerManager(String path) {
		this.path = path;
	}
	
	public Scheduler getScheduler() {
		return scheduler;
	}
	
	public void saveSchedulerJSON() {
		JSONObject jsonScheduler = scheduler.toJSON();
		
		try (FileWriter file = new FileWriter(path)) {
            file.write(jsonScheduler.toString());
            file.flush();
 
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void loadSchedulerJSON() throws Exception {
		JSONParser jsonParser = new JSONParser();
	    
	   try (FileReader reader = new FileReader(path))
	   {
	       //Read JSON file
	       JSONObject schedulerJSON = (JSONObject) jsonParser.parse(reader);
	       scheduler = new Scheduler(schedulerJSON);
	   } catch (ParseException | EOFException | FileNotFoundException e) {
		   scheduler = new Scheduler();
	   } catch (Exception e) {
		   e.printStackTrace();
		   throw e;
	   }
	}
}
