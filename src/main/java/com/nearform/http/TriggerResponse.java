package com.nearform.http;

import java.util.Date;

public class TriggerResponse {

	String jobKey;
	Date nextFireDate;
	
	
	public TriggerResponse(Date nextFireDate, String jobKey) {
		super();
		this.nextFireDate = nextFireDate;
		this.jobKey = jobKey;
	}
	public Date getNextFireDate() {
		return nextFireDate;
	}
	public void setNextFireDate(Date nextFireDate) {
		this.nextFireDate = nextFireDate;
	}
	public String getJobKey() {
		return jobKey;
	}
	public void setJobKey(String jobKey) {
		this.jobKey = jobKey;
	}
	public String toHTMLRow() {
		return "<tr><td>"+ getJobKey() + "</td><td>" + getNextFireDate().toString() + "</td></tr>";
	}
}
