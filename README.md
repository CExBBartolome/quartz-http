
Schedule HTTP callouts with a given payload through HTTP
========================================================

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

Currently supports Http POST, JSON content and one single date to add to the schedule and DELETE with the group::name on the url to remote a scheduled job.

# Run server:

## With Docker:

    docker pull cexbbartolome/quartz-http:latest
    docker run --name quartz-http cexbbartolome/quartz-http
    
Will pull the docker image and create a container named ```quartz-http```

    docker run -e HOST=127.0.0.1 -e PORT=8090 cexbbartolome/quartz-http:latest
    
Overrides the network interface (0.0.0.0) and port (8090) from the defaults (0.0.0.0:8080)

## Without Docker

    mvn jetty:run
    
Will download, build, run the server on the default port 8080

    mvn -Djetty.port=8090 jetty:run

Will download, build, run the server on port 8090

    mvn -Dorg.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX \
        -Dorg.quartz.jobStore.tablePrefix=QRTZ_ \
        -Dorg.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.PostgreSQLDelegate \
        -Dorg.quartz.jobStore.dataSource=qzDS \
        -Dorg.quartz.dataSource.qzDS.driver=org.postgresql.Driver \
        -Dorg.quartz.dataSource.qzDS.URL=jdbc:postgresql://localhost:5432/dbname \
        -Dorg.quartz.dataSource.qzDS.user=postgres \
        -Dorg.quartz.dataSource.qzDS.password=secret \
        -Dorg.quartz.dataSource.qzDS.maxConnections=30 \
        -e jetty:run

Will download, build, run the server, and persist jobs into the Postgres database specified

# Compile war file to /target directory:

    mvn package 
    
# Testing server

If you use a browser (or use curl with -GET) to navigate to the URL that the scheduler is running on your should receive this type of error:

HTTP ERROR 405
Problem accessing /scheduler/api. Reason:
HTTP method GET is not supported by this URL

This is good and what you should expect from a GET. The URL will only support POST, PUT and DELETE.

# CHANGELOG
- Added `load-on-startup` entry in `web.xml`
- Added JDBC drivers for PostgreSQL 9.4
- Added slf4j for logging
- Removed `virtualHosts` entry in `jetty-web.xml`
