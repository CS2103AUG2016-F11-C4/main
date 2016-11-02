package seedu.dailyplanner.hotkeys;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import seedu.dailyplanner.commons.events.model.*;
import seedu.dailyplanner.model.Model;

public class Shortcuts {
	
	private ArrayList<String> recordList = new ArrayList<String>();
	private int x = recordList.size();


	public ArrayList<String> getRecords() {
		return recordList;
	}

	public void setRecords(ArrayList<String> records) {
		this.recordList = records;
	}
	
	public void setQuery(String lastQuery){
		recordList.add(lastQuery);
	}
	
	public String getLastQuery(int ind){
		return recordList.get(ind);
	} 
	
	public String keyPressed(KeyEvent press){
		int button = press.getID();
		if (button == 38){
			if(x> -1){
				x--;
				return recordList.get(x);
			}
		}
		if(button == 40){
			if(x < recordList.size()){
				x++;
				return recordList.get(x);
			}
		}
		return null;
	}
	

}
