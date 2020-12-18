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

test1:
	@echo Exceptionをスロー
	@curl -s localhost:8080/1 > /dev/null
	@docker logs --tail=1000 wildfly | grep demo | tail -n 1
	@echo ""

test2:
	@echo RuntimeExceptionをスロー
	@curl -s localhost:8080/2 > /dev/null
	@docker logs --tail=1000 wildfly | grep demo | tail -n 1
	@echo ""

test3:
	@echo Exceptionのサブクラスをスロー
	@curl -s localhost:8080/3 > /dev/null
	@docker logs --tail=1000 wildfly | grep demo | tail -n 1
	@echo ""

test4:
	@echo RuntimeExceptionのサブクラスをスロー
	@curl -s localhost:8080/4 > /dev/null
	@docker logs --tail=1000 wildfly | grep demo | tail -n 1
	@echo ""

test5:
	@echo @javax.ejb.ApplicationExceptionを付けたRuntimeExceptionのサブクラスをスロー
	@curl -s localhost:8080/5 > /dev/null
	@docker logs --tail=1000 wildfly | grep demo | tail -n 1
	@echo ""

test6:
	@echo @javax.ejb.ApplicationException\(rollback = true\)を付けたRuntimeExceptionのサブクラスをスロー
	@curl -s localhost:8080/6 > /dev/null
	@docker logs --tail=1000 wildfly | grep demo | tail -n 1
	@echo ""

testall: test1 test2 test3 test4 test5 test6

