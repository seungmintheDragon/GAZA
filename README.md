# 서비스 : 가자
최근 엔데믹을 맞이하며, 여행의 수요 증가와 함께 자신의 취향과 관심에 맞는 여행에 대한 수요가 증가하고 있습니다.<br>
이러한 수요에 맞추어 가자 서비스는 가이드와 관광객이 화상 상담 서비스와 공유 지도를 통해 함께 일정을 계획하고, 실시간으로 현지 정보를 받아볼 수 있는 서비스입니다.<br>



## 개발 환경
📕 형상 관리<br>
Gitlab <br>

📖 이슈 관리<br>
Jira <br>

📗 커뮤니케이션<br>
Mattermost, Notion <br>

📘 OS<br>
Windows10 <br>

📚 UI/UX<br>
Figma <br>

📄 IDE<br>
IntelliJ 2022.3.1, Visual Studio Code<br>

📜 DataBase<br>
MySQL 8.0.30<br>

📒 Server<br>
AWS EC2 / Ubuntu 20.04 / Docker 23.0.0 / AWS S3 <br>

📓 Front-End<br>
Vue 3 <br>

📰 Back-End <br>
Java11, Springboot 2.7.7 Gradle 7.6, Swagger2<br>

📄 기타
Postman <br>

📓 외부 서비스
OpenVidu 2.25.0<br>
카카오맵<br>
S3<br>
Bootstrap<br>
Redis 7.0.8<br>




## 서비스 아키텍처
![가자_아키텍처_구성도](/upload/%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98_%EA%B5%AC%EC%84%B1%EB%8F%84.png)




## 주요 기능

서비스 설명 : 맞춤 여행을 원하는 관광객이 가이드와의 화상상담을 통해 여행 일정을 세울 수 있는 서비스<br>

- 주요 기능 : <br>
    - 가이드와의 상담을 위한 예약 서비스 <br>
    - openvidu를 사용한 화상 상담 서비스<br>
        - 소켓을 활용한 지도 공유 서비스<br>
        - Redis pub/sub를 활용한 채팅 서비스<br>
        - 여행 일정을 엑셀로 다운로드하는 서비스 <br>



## 가자 서비스 화면


### 메인 페이지

![01_메인페이지_UI](/upload/01_메인페이지_UI.PNG)



### 가이드 상세 페이지

![02_가이드상세페이지_UI](/upload/02_가이드상세페이지_UI.PNG)




### 예약 페이지

![03_예약페이지UI](/upload/03_예약페이지UI.PNG)




### 사용자 예약 내역 확인

![06_사용자_예약내역확인_UI](/upload/06_사용자_예약내역확인_UI.PNG)



### 상담 화면 

![화상상담_캡쳐](/upload/화상상담_캡쳐.PNG)



### 상담 완료 후 

![상담완료후_UI](/upload/상담완료후_UI.PNG)






## Team 소개

[BackEnd]<br>

이도연(팀장)<br>
이해민<br>
최지성<br>

[Frontend]<br>

이재훈<br>
용승민<br>


[인프라] <br>
용승민


