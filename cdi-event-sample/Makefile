nop:
	@echo ""

up:
	docker run -d --name wildfly -p 8080:8080 -p 9990:9990 jboss/wildfly /opt/jboss/wildfly/bin/standalone.sh -b=0.0.0.0 -bmanagement=0.0.0.0
	@while [ "$$(docker logs wildfly|grep WFLYSRV0025)" = "" ]; do\
		sleep 0.1;\
	done;
	docker exec wildfly /opt/jboss/wildfly/bin/add-user.sh demo demo --silent

down:
	docker stop wildfly
	docker rm wildfly

deploy:
	mvn package wildfly:deploy

test:
	curl -s localhost:8080/sample/
	@echo ""

