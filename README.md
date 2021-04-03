# 엘라스틱 서치 Java Query DSL 예제

* example1.json을 통해서 학습가능
* 각 Java File 별 Main 메소드를 통해 실행 
* 각 파일 설명 src/main/java 경로에 소스가 있음

### 기본 쿼리 DSL 만드는 법 설명
[ 쿼리 DSL 이란 Domain Specific Language의 약어로  Elasticsearch 는 검색을 위한 쿼리 기능이며 JSON 형식의 구조 입니다.]


> java 에서 Query Dsl 작성시 QueryBuilders 클래스를 이용하여 만드는 방법과 new 연산자를 이용하여 Query를 만듭니다.

  ```java 
  예시 -- term 쿼리 작성 
   
  import org.elasticsearch.index.query.*;
  // 1. QueryBuilders 이용하여 작성 
      QueryBuilders.termQuery("키","값");
  // 2. new 연산자를 이용하여 작성
      new TermQueryBuilder("키","값");
  
  ```
### 파일 설명
* SimpleMakeQuery.java : Query DSL JAVA로 만드는 두가지의 방법 
* example1.json : Query DSL(JSON) 예제
* ElasticSerchConnection.java : 엘라스틱 커넥션 연결부터 서치 쿼리 수행
* QueryGuide.java : Query Dsl을 자바로 만드는 법
* ExampleQueryGuide : example1.json 을 기반으로 쿼리 생성 부터 전송까지 수행 
     

