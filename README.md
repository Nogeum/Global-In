# 근태관리시스템

## 프로젝트 소개 Introduction

## 개발환경
- IntelliJ IDEA
- Visual Studio Code
- Apache Tomcat 9
- Oracle 11g

## 기술스택
- JDK : 1.8
- Build : Maven
- Framework: Spring Boot 2.7.18
- ORM: Spring Data JPA

## 
- LEAVE_BALANCE (연차사용관련 테이블)
- LEAVE_REQUEST(휴가신청관련 테이블)
- OVERTIME_REQUEST(잔업수당 신청테이블)
- PDS_BOARD(자료실페이지 관련 테이블)
- SCHEDULE(일정관리페이지 관련 테이블)
- ATTENDANCE(근태관리페이지 관련 테이블)
- DEPARTMENT(부서코드, 부서이름 관리 테이블)


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


