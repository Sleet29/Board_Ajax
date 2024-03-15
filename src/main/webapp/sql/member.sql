drop table member cascade constraints purge;
-- 1. index.jsp에서 시작합니다.
-- 2. 관리자 계정 admin, 비번 1234를 만듭니다.
-- 3. 사용자 계정을 3개 만드비다.

create table member(
	id 			varchar2(12),
	password	varchar2(10),
	name		varchar2(15),
	age 		number(2),
	gender 		varchar2(3),
	email 		varchar2(30),
	memberfile 	varchar2(50),
	PRIMARY KEY(id)
);

-- memberfile은 회원 정보 수정시 적용함.

select * from member;

INSERT INTO MEMBER
VALUES('admin','1234','관리자','29','m','admin@google.com','');

INSERT INTO MEMBER
VALUES('user1','1234','유저1','24','f','user1@google.com','');

INSERT INTO MEMBER
VALUES('user2','1234','유저2','27','m','user2@google.com','');

INSERT INTO MEMBER
VALUES('user3','1234','유저3','18','f','user3@google.com','');

