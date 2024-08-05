# 도서관 대여 서비스
Java &SQL 사용한 간단한 console 프로그램


<br/>
<br/>
<br/>

# 엔티티 관계도
<img width="233" alt="sql" src="https://github.com/user-attachments/assets/ec2308f2-64c0-44e3-a410-a094af2a214e">

<br/>
<br/>
<br/>

# 기능 목록
1. 회원 가입
    - name, email, password
2. 로그인
    - email, password
3. 현재 대출 가능한 책 목록 가져오기
    - 대출 여부 확인해서 가능한 책들만 가져오기
4. 책 대출하기
    - 로그인한 member가 책 빌리기
   - 반납할 날짜 계산 : 대출한 날짜 + 14일
5. 책 반납하기
    - 로그인한 member가 빌린 책 반납
   - 반납 기간 안에 제출한 경우 “반납을 완료했습니다” 문구 출력
    - 반납 기간 이후 반납한 경우 “[반납 기한] 일 이후에 반납했습니다” 문구 출력
6. 책 검색
    - 제목 중 일부만 검색해도 결과가 나오도록
7. 회원 정보
    - 회원 정보 수정 (password 수정)
    - 내가 현재 대출한 책 리스트
8. 로그아웃
    - 로그아웃 선택 시 다시 `회원 가입 / 로그인` 이동
9. 종료

<br/>
<br/>
<br/>

# console menu (숫자 입력하므로 필요)

## main menu

1. 회원 가입
2. 로그인

## book menu

1. 현재 대출 가능한 목록
2. 책 대출하기
3. 책 반납하기
4. 책 검색하기
5. My Page
    - user menu 이동하기
6. 로그아웃
    - main menu 이동하기

## user menu

1. password 변경하기
    - password 변경하고 user table update
2. 내가 현재 빌린 책 목록
    - 현재 날짜 기준 빌린 책 목록 출력하기
3. 이전으로
   - book menu로 다시 이동
5. 로그아웃
    - main menu 이동하기
6. 회원 탈퇴
    - main menu 이동하기




<br/>
<br/>
<br/>

# 실제 구현한 기능
- 회원가입
<img width="215" alt="회원가입" src="https://github.com/user-attachments/assets/834f8bee-27f0-44a3-a087-ad4759a81db1">

- 로그인
<img width="418" alt="로그인" src="https://github.com/user-attachments/assets/933d0527-d30d-4421-9ecc-c6c7fee708b3">

- 대출 가능한 책 목록
<img width="475" alt="책목록출력" src="https://github.com/user-attachments/assets/96c8d69f-f3a3-4971-a0d4-49648eda5ad5">

- 책 대출하기
<img width="464" alt="책대출" src="https://github.com/user-attachments/assets/c276c851-6e52-4f8b-938e-48fbb8e9184e">
<img width="517" alt="sql책대출" src="https://github.com/user-attachments/assets/3c0f50e4-b82e-4bab-82e4-5fa9a5d28434">

- 책 반납하기 (14일 계산하고 추후 추가 예정)
- 비밀번호 변경하기
<img width="422" alt="비밀번호 변경" src="https://github.com/user-attachments/assets/363a815d-4b91-48a2-9093-21b4604a591e">


- 로그인한 유저가 대출한 책 목록
<img width="423" alt="내가 대출한 책 목록" src="https://github.com/user-attachments/assets/9dc72b06-b94e-4bba-8a08-c26d49ea40de">

- 회원 탈퇴
<img width="465" alt="회원 탈퇴" src="https://github.com/user-attachments/assets/37ee569a-5c85-4267-ab43-06d757e653d5">
<img width="525" alt="sql회원 탈퇴" src="https://github.com/user-attachments/assets/3dab850c-0771-4f5b-82ca-da1f3fb5bc9d">

