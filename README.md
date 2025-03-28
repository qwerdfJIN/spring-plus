## ✅ 필수 기능  

### 🟡 Level 1

#### 1. 코드 개선 퀴즈 - @Transactional의 이해  
할 일 저장 기능을 구현한 `/todos` API 호출 시 발생하던  
`Connection is read-only` 오류를 해결했습니다.  
트랜잭션 설정을 수정하여 데이터 저장이 정상적으로 동작하도록 개선하였습니다.  


#### 2. 코드 추가 퀴즈 - JWT의 이해  
`User` 엔티티에 `nickname` 컬럼을 추가하였으며, 중복 허용으로 설계하였습니다.  
JWT 토큰에 `nickname` 정보가 포함되도록 수정하여  
프론트엔드에서 해당 값을 추출해 화면에 표시할 수 있도록 구현하였습니다.  


#### 3. 코드 개선 퀴즈 - JPA의 이해  
할 일 검색 시 `weather` 조건을 선택적으로 검색할 수 있도록 구현하였습니다.  
수정일 기준으로 시작일과 종료일 조건을 활용해 기간 필터링이 가능하도록 하였습니다.  
필요 시 조건에 따라 동적으로 JPQL 쿼리가 실행되도록 서비스 레이어에서 분기 처리하였습니다.  


#### 4. 테스트 코드 퀴즈 - 컨트롤러 테스트의 이해  
`org.example.expert.domain.todo.controller` 패키지의  
`todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다()` 테스트에서 발생하던 문제를 수정하였습니다.  
정상적으로 예외가 발생하고, 테스트가 통과되도록 개선하였습니다.  


#### 5. 코드 개선 퀴즈 - AOP의 이해  
`UserAdminController` 클래스의 `changeUserRole()` 메서드 실행 전에  
AOP가 정상적으로 동작하도록 `AdminAccessLoggingAspect` 클래스를 수정하였습니다.  
메서드 실행 전 로그가 남도록 AOP 동작을 보장하였습니다.  
<br>

### 🟡 Level 2  

#### 6. JPA Cascade  
새로운 할 일을 저장할 때 해당 할 일을 생성한 유저가 자동으로 담당자로 등록되도록  
JPA `cascade` 기능을 적용하였습니다.  


#### 7. N+1 문제 해결  
`CommentController` 클래스의 `getComments()` API 호출 시 발생하던 N+1 문제를 해결하였습니다.  
`fetch join`과 `@EntityGraph`를 활용하여 쿼리를 최적화하였습니다.  


#### 8. QueryDSL 전환  
`TodoService.getTodo()` 메서드 내의 기존 JPQL `findByIdWithUser` 쿼리를 QueryDSL로 변경하였습니다.  
관련 데이터를 한 번에 가져올 수 있도록 최적화하여 N+1 문제가 발생하지 않도록 구현하였습니다.  


#### 9. Spring Security 전환  
기존의 `Filter`와 `Argument Resolver` 기반 인증 방식을 제거하고  
Spring Security 기반의 JWT 인증 및 권한 관리로 전환하였습니다.  
기존의 접근 권한 및 유저 권한 기능은 그대로 유지하도록 구성하였습니다.  

<br>

## ☑️ 도전 기능  

### 🟡 Level 3  

#### 10. QueryDSL 을 사용하여 검색 기능 만들기  
QueryDSL을 사용하여 일정을 검색할 수 있는 기능을 구현하였습니다.  
검색 조건으로는 일정 제목(부분 일치), 생성일 범위, 담당자 닉네임(부분 일치)을 지원하며  
검색 결과는 페이징 처리 및 최신순 정렬로 반환됩니다.  
Projection을 적용하여 일정 제목, 담당자 수, 댓글 수를 포함하도록 구성하였습니다.  


#### 11. Transaction 심화  
매니저 등록 요청 시 요청 로그를 별도로 기록하는 로그 테이블(`log`)을 생성하였습니다.  
`@Transactional`의 `REQUIRES_NEW` 옵션을 활용하여 매니저 등록과 로그 기록이  
독립적으로 처리되도록 구성하였습니다.  
매니저 등록이 실패하더라도 로그는 항상 저장되며, 생성 시각과 요청 정보를 포함하도록 설계하였습니다.  


#### 12. AWS 활용  
EC2, RDS, S3를 활용하여 프로젝트를 배포 및 관리하였습니다.  
각 서비스 간의 보안 그룹을 적절히 구성하여 보안을 강화하였습니다.  


#### 12-1. EC2  
EC2 인스턴스를 생성하여 애플리케이션을 실행하였으며,  
탄력적 IP를 설정하여 외부 접속이 가능하도록 구성하였습니다.  
헬스 체크 API(`/health`)를 구현하여 누구나 접근 가능하도록 하였으며  
서버 상태를 확인할 수 있도록 설정하였습니다.  

<br>

**EC2 인스턴스 요약 화면**

![Image](https://github.com/user-attachments/assets/875d40ee-b224-4cf4-85f2-0fa910370ff3)

<br>

**서버 실행 완료 화면 (Git Bash)**
![Image](https://github.com/user-attachments/assets/44129394-887e-41fa-ad49-ff432dd5bcf7)

<br>

**보안 그룹 인바운드 규칙**
![Image](https://github.com/user-attachments/assets/9bc30f48-281c-4f77-92ce-b60872400f8e)

<br>

**헬스 체크 API 호출 결과**
![Image](https://github.com/user-attachments/assets/334f7949-b85e-4504-8e1e-fc5c86e80b98)

<br>

#### 12-2. RDS  
RDS 인스턴스를 생성하고, EC2에서 실행 중인 애플리케이션과 연결하였습니다.  
보안 그룹 및 연결 설정을 완료하였습니다.  

<br>

**EC2 인스턴스 요약 화면**
![Image](https://github.com/user-attachments/assets/33247e13-ea77-475a-b014-b4eb07be4b6b)
