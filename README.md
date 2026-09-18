# 자바8 Spring Boot / Maven Project 생성

## 1.IntelliJ New Project 생성:

좌측 메뉴에서 Java (또는 New Project) 선택
Build system: Maven 선택
JDK: 1.8 (설치된 Java 8 지정)



## 2.pom.xml 파일 수정(Spring Boot 2.7.x 및 Java 8 의존성 설정):
프로젝트가 생성되면 루트 디렉터리의 pom.xml 파일을 열고 전체 내용을 아래 코드로 교체
com.fastcampus.ch1
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <!-- Spring Boot 2.7.18 버전 상속 -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
        <relativePath/>
    </parent>

    <!-- groupId는 수정할 필요 없으나, 보통은 패키지경로 -->
    <!-- artifactId, name은 프젝명으로 수정해야함 -->
    <groupId>com.example</groupId> 
    <artifactId>demo</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>demo</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
        <https.protocols>TLSv1.2</https.protocols>
    </properties>

    <dependencies>
        <!-- Spring Web (웹 개발 필수 스타터) -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 테스트 모듈 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Spring Boot 패키징 및 실행 플러그인 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>


※수정 후 우측 상단에 나타나는 Maven 새로고침 아이콘(M 모양 파란색 버튼 / Ctrl+Shift+O)을 눌러 동기화



## 3.Application.java 생성 및 실행:

새로만들기 -> Spring구성요소 -> 애플리케이션

만약 템플릿이 없다면 아래 자바파일 만들 때마다 아래 코드 삽입

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class 클래스명 {

    public static void main(String[] args) {
        SpringApplication.run(클래스명.class, args);
    }

}

콘솔창에 Tomcat started on port(s): 8080 로그가 뜨면 정상적으로 실행된 것





## Maven 종속성 에러
mvn clean compile -U 로 처리하거나



## Maven 패키징 하기
Maven 터미널(Maven Goal)에서 package선택
구버전 자바이므로 Maven 패키징시 에러 발생.
Build, Execution, Deployment > Build Tools > Maven > Importing 이동 후 VM options for importer 란에 아래 내용을 추가
-Dhttps.protocols=TLSv1.2

다시 Maven Goal에서 clean 명령 후 package 실행하면

## 패키지 톰캣 실행
터미널 경로를 패키지파일 있는 곳으로 이동
java -jar 패키지명.jar (포트 충돌시 포트지정 : --server.port=80)
<br>
<br>
<br>
<br>
<br>
<br>


# 백엔드 개발
## 기술스택
- JDK : 1.8
- Build : Maven
- Framework: Spring Boot 2.7.18
- Database: 테스트단계:MySQL 최종배포:Oracle
- ORM: Spring Data JPA

## 구성페이지

### 로그인페이지
로그인폼 노출, 관리자 계정으로 로그인 시, 관리자페이지로 이동

### 직원페이지
- 메인
- 스케줄 조회
- 출퇴근 체크
- 휴가 신청
- 요청 상태(휴가신청현황)
- 마이페이지

### 관리자페이지
- 메인
- 근태관리
- 스케줄 배정
- 승인 대기함
- 직원 관리
- 알림

### 9월 16일 개발현황
- 테스트용 MySQL DB 제작 完
- 로그인컨트롤러 제작 完 (어드민, 유저 check 完)

### 9월 17일 개발현황
- 유저 대시보드, 일정관리 제작 完. schedule 테이블 필요한 속성추가


### 9월 18일 개발현황
- 근태관리 구현 完, 전자결재 post 과정 오류 발생.


