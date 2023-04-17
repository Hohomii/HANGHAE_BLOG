# Spring_Lv1_Assignment
스프링 부트로 로그인 기능이 없는 나만의 항해 블로그 백엔드 서버 만들기


## 1. Use Case

<img src="https://user-images.githubusercontent.com/127713815/232392641-8e73792f-56bb-48ce-8549-072d5bbb6e49.jpeg" width="400" height="500"/>

## 2. API 명세서

Function|Method|URL|Request|Response
:---:|:---:|:---:|:---:|:---:
메인 페이지|GET|/| | {"createdAt" : "작성일자", "id":"(자동생성)", "title":"제목", "content":"본문", "writer":"작성자"}
게시글 작성|POST|/create | {"title":"제목", "content":"본문", "writer":"작성자", "password":"비밀번호"}
선택한 게시글 조회|GET|/posting/{id} | | {"createdAt" : "작성일자", "id":"(자동생성)", "title":"제목", "content":"본문", "writer":"작성자"}
게시글 수정|PUT|/update/{id} | {"title":"제목", "content":"본문", "writer":"작성자", "password":"비밀번호"}d | {"createdAt" : "작성일자", "id":"(자동생성)", "title":"제목", "content":"본문", "writer":"작성자"}
게시글 삭제|DELETE|/delete/{id} | {"password":"비번" | "success":true}

## 3. 과제 제출시 고민해볼 것
### 1) 수정, 삭제 API의 request를 어떤 방식으로 사용하셨나요? (param, query, body)
JSON 데이터를 주고 받는 데 용이한 RequestBody를 사용했습니다.

### 2) 어떤 상황에 어떤 방식의 request를 써야 하나요?
게시글 조회 : get
게시글 작성 : post
게시글 수정 : put
게시글 삭제 : delete

### 3) RESTful한 API를 설계했나요? 어떤 부분이 그런가요? 어떤 부분이 그렇지 않나요?
아직까지 제가 아는 한에서는 api의 리소스 식별자를 중복 없이 만들었으며 각 api에 적절한 http메서드를 사용한 것을 확인했기 때문에 RESTful하게 설계했다고 할 수 있습니다.

### 4) 적절한 관심사 분리를 적용하였나요? (Controller, Repository, Service)
네.

### 5) API 명세서 작성 가이드라인을 검색하여 직접 작성한 API 명세서와 비교해보세요!
검색하여 function, method, URL, Request, Response 를 넣어 작성했습니다.

