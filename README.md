
Schedule HTTP callouts with a given payload through HTTP
========================================================
# To view a list of jobs scheduled
    GET localhost:8080/scheduler/api
    
# To view a list of jobs scheduled in html format
    GET localhost:8080/scheduler/api?contentType=html
    
# To view a specific scheduled job
    GET localhost:8080/scheduler/api/id/*
    
# To add a job to the queue:

    POST localhost:8080/scheduler/api
    {
      "timestamp": 1397733237027,
      "url": "http://localhost:3000",
      "payload": "{\"Hello\":\"world\"}"
    }
    
will schedule a callout ```POST http://localhost:3000/``` with ```{"Hello": "world"}``` as the body.

You will receive back a JSON object that has the group::name pair. You will need to hang on to this if you ever intend to cancel the job.

# To remove a job from the queue:

    DELETE localhost:8080/scheduler/api/group::name

This will remove a previously scheduled job with the unique key of group::name. The JSON returned will show failure if the job cannot be found or it was not possible to cancel it. 

# HTTP verbs supported

Currently supports Http GET, POST, JSON content and one single date to add to the schedule and DELETE with the group::name on the url to remote a scheduled job.

# run server:

    mvn jetty:run
    
Will download, build, run the server on the default port 8080

    mvn -Djetty.port=8090 jetty:run

Will download, build, run the server on port 8090

# compile war file to /target directory:

    mvn package 
    

