FROM adoptopenjdk/openjdk8:jre8u222-b10

#install filebeat
RUN apt-get update \
    && apt-get install -y wget \
    && rm -rf /var/lib/apt/lists/* \
    && wget https://artifacts.elastic.co/downloads/beats/filebeat/filebeat-6.4.3-linux-x86_64.tar.gz -O /opt/filebeat.tar.gz \
	&& cd /opt/ \
	&& tar zvxf filebeat.tar.gz
COPY DevOps/filebeat.yml /opt/filebeat-6.4.3-linux-x86_64/

COPY target/${appName}.jar /opt/${appName}.jar
COPY DevOps/startApp.sh /opt/startApp.sh

EXPOSE ${bindPort}

ENTRYPOINT ["sh", "/opt/startApp.sh"]