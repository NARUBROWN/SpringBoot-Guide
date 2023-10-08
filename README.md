# SpringBoot-Guide
스프링부트를 공부하는 저장소

CI/CD -> TDD -> Nginx를 통한 Reverse Proxy -> Spring Security -> 부하 분산 처리를 위한 Nginx를 이용한 로드밸런싱 -> 로드밸런싱을 이용한 무중단 배포구현 (롤)(완료)

<h2>CI/CD 파이프라인</h2>
master 브랜치에 push나 pull request 요청이 있을 경우 Action이 트리거 됨

- 메이븐 빌드

- 통합 테스트

- SCP를 통해 서버로 JAR 파일 전송

- 중복실행 여부 확인

- 서버 배포

* 통합 테스트를 통과하지 못할 경우 배포과정은 중단되게 됨

<h2>TDD(Test Driven Development) 개발 프로세스</h2>
디자인 -> 테스트 코드 작성 -> 코드 개발 -> 리팩토링 -> 테스트 코드 작성 -> 디자인 수정
<br>
TDD가 장점을 발하는 상황

- 개발자 본인에 대한 불확실성이 높은 경우
  
- 외부적인 불확실성이 높은 경우
  
- 코드가 많이 바뀔 것이라고 예상되는 경우
  
- 유지보수 주체가 내가 아니게 될 것으로 예상되는 경우

<h2>성능 개선</h2>
부하 테스트 (nGrinder) -> Redis Cache를 이용해 GET 응답 개선 -> 부하 테스트 (nGrinder)