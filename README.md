# 프로젝트 명 : 오늘의 학습 (프로젝트 명 변경 예정)


## 📖 프로젝트 소개
> 오늘의 학습은 학생들을 위한 학습 사이트입니다.
- 오늘의 학습은 학생들이 배운 내용을 쉽게 정리하고 공유할 수 있도록 도와주는 사이트입니다.

## 👥 팀원 소개
|이름|역할|담당 기능|GitHub|
|---|---|---|---|
|공미경|팀장|• 채팅<br>• 단체 채팅|[@GitHub](https://github.com/qoiol)|
|강경민|팀원|• 회원 관련 <br>• DB 설계<br>• 어드민 페이지|[@GitHub](https://github.com/GyeongMin2)|
|송수미|팀원|• 포스트 관련 <br>• 페이징 공통모듈 설계 <br>• 파일 업로드 |[@GitHub](https://github.com/SumiSong)|
|한인규|팀원|• 친구 관련<br>• 메인 페이지|[@GitHub](https://github.com/Haninqq)|

## 📅 프로젝트 일정

| 단계     | 12/05 | 12/06 | 12/07 | 12/08 | 12/09 | 12/10 | 12/11 | 12/12 | 12/13 |
| ------ | ----- | ----- | ----- | ----- | ----- | ----- | ----- | ----- | ----- |
| 요구분석   | ✓     |       |       |       |       |       |       |       |       |
| DB 설계  | ✓     |       |       |       |       |       |       |       |       |
| 기능 명세서 |       | ✓     | ✓     |       |       |       |       |       |       |
| 개발     |       | ✓     | ✓     | ✓     | ✓     | ✓     | ✓     |       |       |
| 테스트&배포 |       |       |       |       |       |       | ✓     | ✓     | ✓     |

## 🛠 개발 환경
### Frontend
- Thymeleaf
- JavaScript
- Bootstrap 5
- jQuery

### Backend
- Java
- Spring Framework
- JPA
- MyBatis
- Node.js
- JWT

### WebSocket
- Stomp

### Database & Server
- MariaDB & MySQL
- Spring Boot 3.3.6

### Tools
- Git & GitHub
- IntelliJ IDEA
- Postman
- JUnit

## 💡주요 기능
### 1. 회원 관리 (담당 : 강경민)
>Node.js를 이용한 MSA 구현 (JWT 토큰 사용)
- 회원가입
- 로그인
- 로그아웃
- 이메일 인증
- 정보 수정
- 이메일인증을 통한 비밀번호 변경

### 2. 학습 게시물 (담당 : 송수미)
- 학습 게시물 등록 수정 삭제 조회 ( 파일 업로드 )및 썸네일 생성
- 학습 게시물 좋아요
- 학습 게시물 공유
- 학습 게시물 페이징

### 3. 채팅 관리 (담당 : 공미경)
- stomp를 이용한 실시간 채팅
- 채팅 목록 조회
- 채팅 상세 조회
- 단체 채팅

### 4. 관리자 페이지 (담당 : 강경민)
- 회원 관리
- 학습 게시물 관리

### 5. 메인 페이지 (담당 : 한인규)
- 메인 페이지 디자인
- 메인 페이지 기능

### 6. 친구 관리 (담당 : 한인규)
- 친구 추가
- 친구 목록 조회
- 친구 삭제

### 7. 페이징 모듈 (담당 : 송수미)
- 백엔드 페이징 모듈
- 프론트엔드 페이징 모듈

## 📊 ERD & Class Diagram
![ERD](https://github.com/ChunjaeStudyShare/ChunjaeStudyShare/blob/main/note/%EC%BD%94%EB%94%A9%EC%9E%AC%ED%8C%90%EC%86%8C_ERD.png)
![Class Diagram](https://github.com/ChunjaeStudyShare/ChunjaeStudyShare/blob/main/note/%EC%BD%94%EB%94%A9%EC%9E%AC%ED%8C%90%EC%86%8C_classDiagram.png)

## 💻 주요 코드

### 공미경
#### 아무튼 코드
- 설명

```java

```

```javascript

```

### 강경민
#### Node.js를 이용한 로그인 기능 구현 (JWT 토큰 사용)
```javascript   
async login(userId, password, rememberMe) {
    // 1. 사용자 검증 및 계정 상태 확인
    const user = await UserModel.findById(userId);
    if (user.status !== 0) handleAccountStatus(user.status);

    // 2. 비밀번호 검증 및 로그인 시도 제한
    const hashedPassword = await this.hashPassword(password, user.salt);
    if (hashedPassword !== user.password) {
        await handleFailedLogin(userId, user.loginTry);
    }

    // 3. JWT 토큰 생성 (SHA-256)
    const token = jwt.sign(
        { userId: user.userId, email: user.email, name: user.name },
        process.env.JWT_SECRET,
        { 
            expiresIn: rememberMe ? '7d' : '1h',
            algorithm: 'HS256'
        }
    );

    return { token, user: {...user} };
}
```

#### java JWT 토큰 체크

```java
@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
    // 1. 토큰 추출 및 검증
    String token = extractToken(httpRequest);
    if (token != null && tokenService.isTokenValid(token)) {
        String userId = jwtUtil.getUserId(token);
        httpRequest.setAttribute("userId", userId);
        chain.doFilter(request, response);
    } else {
        handleUnauthorized(httpRequest, httpResponse);
    }
}
```

#### Node.js 속도 측정결과
 1. 부하 테스트 결과 요약
    1차 테스트 (5,000 요청)
    - 처리량: 1,934 req/sec
    - 평균 응답: 24.6ms
    - 성공률: 100%
    2차 테스트 (100,000 요청)
    - 처리량: 2,118 req/sec
    - 평균 응답: 258.4ms
    - 성공률: 100%
    
	2. 쿼리 성능비교

| 쿼리 유형 | 처리량 (req/sec) | 평균 응답 시간 |
|----------|-----------------|--------------|
| 단일 조회 | 1,816 | 33.2ms |
| 다중 조회 | 1,690 | 28.3ms |
| JOIN | 1,754 | 21.2ms |

3. 핵심 성과
    1. 안정성
        - 모든 테스트에서 100% 성공률 달성
        - 부하 증가에도 안정적 처리
    2. 성능
        - 초당 2,000건 이상의 요청 처리 가능
        - JOIN 쿼리도 평균 21.2ms의 빠른 응답
    3. 확장성
        - 부하 증가 시에도 처리량 증가
        - 시스템 안정성 유지

#### 주요 특징
1. 보안 강화
    - SHA-256 해시 알고리즘 사용
    - 로그인 시도 제한 (5회)
    - 토큰 만료 시간 설정
    - 계정 상태 관리 (활통/휴면/탈퇴/미인증/잠금 등)
2. 성능 최적화
    - 토큰 기반 인증으로 서버 부하 감소
    - DB인덱싱을 통한 조회 성능 향상
    - 비동기 처리로 응답속도 개선

### 송수미
#### 아무튼 코드설명
- 설명
- 설명
```java

```

### 한인규
#### 아무튼 코드설명
- 설명
- 설명
```java
```


## 🎯 트러블 슈팅

### 1. 알수없는 속도 저하 문제( 담당자 : 강경민 )

#### 문제상황
- 코드상 문제는 없는데 속도가 저하되는 현상이 발생
- Node.js서버의 속도 측정 결과도 안정적이었음
- Spring Boot 서버가 문제라고 판단

#### 해결 방안
- 자주 조회되는 테이블에 인덱싱 추가
    - ActiveTokens 테이블에 인덱싱 추가 (해당 테이블은 토큰의 유효성을 검사하는 테이블이라 자주 조회되었음)
- Nginx를 앞단에 두어 http/3 프로토콜을 사용하도록 설정
    - 내부적으로는 http/1.1 프로토콜을 사용하지만 클라이언트와 서버는 http/3 프로토콜을 사용하도록 설정
- 정적 요소를 필터에서 제외
- 정적 요소 압축

#### 개선 효과
- 속도 저하 문제 해결및 http/3 프로토콜 사용으로 속도 향상

***

### 2.문제상황 제목 ( 담당자 : * * *)

#### 문제상황
- 설명

#### 해결 방안
- 설명

#### 개선 효과
- 설명

***