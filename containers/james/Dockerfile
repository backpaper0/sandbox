FROM openjdk:8 AS build-env
 
WORKDIR /work
 
ADD http://ftp.riken.jp/net/apache//james/server/james-binary-2.3.2.1.tar.gz /tmp/
ADD setup-james.sh /tmp/setup-james.sh
 
RUN apt update -y
RUN apt install -y unzip expect telnet
RUN tar zxvf /tmp/james-binary-2.3.2.1.tar.gz
RUN ln -s /work/james-2.3.2.1 /work/james-2.3.2.1
RUN unzip /work/james-2.3.2.1/apps/james.sar -d /work/james-2.3.2.1/apps/james
RUN sed -i -e 's/<port>25<\/port>/<port>8025<\/port>/g' /work/james-2.3.2.1/apps/james/SAR-INF/config.xml
RUN sed -i -e 's/<port>110<\/port>/<port>8110<\/port>/g' /work/james-2.3.2.1/apps/james/SAR-INF/config.xml
RUN sed -i -e 's/<port>119<\/port>/<port>8119<\/port>/g' /work/james-2.3.2.1/apps/james/SAR-INF/config.xml
RUN sed -i -e 's/password=".*"/password="root"/g' /work/james-2.3.2.1/apps/james/SAR-INF/config.xml
RUN sh /work/james-2.3.2.1/bin/phoenix.sh start && \
    sleep 3 && \
    expect /tmp/setup-james.sh && \
    sh /work/james-2.3.2.1/bin/phoenix.sh stop
 
FROM openjdk:8
 
COPY --from=build-env /work /work
 
RUN ln -s /work/james-2.3.2.1 /opt/james && \
    chmod +x /opt/james/bin/*.sh
 
CMD ["/opt/james/bin/run.sh"]

