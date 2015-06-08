#!bin/sh 
sed -i "s/_TARGETIP_/${_TARGETIP_}/" ./src/main/webapp/WEB-INF/jetty-web.xml
echo ${_TARGETIP_}
cat ./src/main/webapp/WEB-INF/jetty-web.xml
mvn -Djetty.host=${HOST} -Djetty.port=${PORT} -e jetty:run
