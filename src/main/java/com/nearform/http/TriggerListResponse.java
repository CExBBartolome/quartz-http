package com.nearform.http;

import java.util.ArrayList;
import java.util.List;

public class TriggerListResponse {
	public List<TriggerResponse> triggers;

	public List<TriggerResponse> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<TriggerResponse> triggers) {
		this.triggers = triggers;
	}

	public TriggerListResponse() {
		triggers = new ArrayList<TriggerResponse>();
	}
	public void addTrigger(TriggerResponse triggerResponse){
		triggers.add(triggerResponse);
	}
	public String toHTMLTable(){
		String table = "<table><tr><td>ID</td><td>Next Fire Date</td></tr>";
		for(TriggerResponse triggerResponse: triggers){
			table += triggerResponse.toHTMLRow();
		}
		table += "</table>";
		return table;
	}
}
