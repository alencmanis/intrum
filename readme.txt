#run
start mvn spring-boot:run
#controler test
mvn surefire:test -Dtest=*ControllerTest > controller.log
#service test
mvn surefire:test -Dtest=*ServiceTest > service.log
#repository test
mvn surefire:test -Dtest=*RepositoryTest > repository.log
#unit tests
mvn surefire:test -Dtest=DebtUtilTest > unit.log

