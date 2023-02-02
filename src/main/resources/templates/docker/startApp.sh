nohup /opt/filebeat-6.4.3-linux-x86_64/filebeat -e -c /opt/filebeat-6.4.3-linux-x86_64/filebeat.yml >/dev/null 2>&1 &
java -Duser.timezone=Asia/Shanghai -jar /opt/${appName}.jar