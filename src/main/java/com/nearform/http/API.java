package com.nearform.http;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.util.UUID;
import java.util.Date;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.DateBuilder.*;

import com.nearform.quartz.JobData;
import com.nearform.quartz.HttpJob;

public class API extends AbstractHandler {

  private ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
  private Scheduler scheduler;

  protected void doStart() throws Exception {
    scheduler = StdSchedulerFactory.getDefaultScheduler();
    scheduler.start();
    super.doStart();
  }

  protected void doStop() throws Exception {
    scheduler.shutdown();
    super.doStop();
  }

  public void handle(String target,
                     Request baseRequest,
                     HttpServletRequest request,
                     HttpServletResponse response)
                     throws IOException, ServletException {

    BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
    String json = "";
    if(br != null){
      String nextLine = br.readLine();
      while(nextLine != null) {
        json += nextLine;
        nextLine = br.readLine();
      }
      br.close();
    }
    System.out.println(json);

    JobData jobData = this.mapper.readValue(json, JobData.class);

    response.setContentType("application/json");

    try {
      ScheduleResponse responseContent = schedule(jobData);
      mapper.writeValue(response.getOutputStream(), responseContent);
    } catch(SchedulerException e) {
      mapper.writeValue(response.getOutputStream(), new ErrorResponse(e));
    }


  }

  private ScheduleResponse schedule(JobData jobData) throws SchedulerException {

    JobDetail job = newJob(HttpJob.class)
        .withIdentity(UUID.randomUUID().toString(), "http")
        .usingJobData("url", jobData.getUrl())
        .usingJobData("payload", jobData.getPayload())
        .build();

    Date startTime = new Date(jobData.getTimestamp());

    Trigger trigger = newTrigger()
        .withIdentity(UUID.randomUUID().toString(), "http")
        .startAt(startTime)
        .build();

    scheduler.scheduleJob(job, trigger);

    TriggerKey triggerKey = trigger.getKey();
    ScheduleResponse response = new ScheduleResponse();
    response.setKey(triggerKey.getGroup() + "::" + triggerKey.getName());

    System.out.println("Scheduling job " + startTime);

    return response;
  }

  private void unschedule(String key) {
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);

    server.setHandler(new API());

    server.start();
    server.join();
  }
}