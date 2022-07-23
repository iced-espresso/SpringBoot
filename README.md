# SpringBoot
SprintBootProject

# lombok
- 반복되는 메소드를 Annotation 을 사용해서 자동으로 작성해주는 라이브러리다.
- lombok의 경우 project마다 매번 설정해줘야함.
  1) `build.gradle` &rarr; `compile('org.projectlombok:lombok')`
  2) `Settings > Build > Compiler > Annotation Processors` &rarr; `Enable annotation processing`

# H2 웹 콘솔
- spring boot에서 사용할 수 있는 로컬환경 데이터베이스
- `application.properties` &rarr; `spring.h2.console.enabled=true` 세팅
- application 실행 후 `localhost:8080/h2-console` 로 접속
- `JDBC URL` : `jdbc:h2:mem:testdb`로 설정후 connect 하면 console 관리화면 나옴