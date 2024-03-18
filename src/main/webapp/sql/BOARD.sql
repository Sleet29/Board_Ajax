DROP TABLE BOARD CASCADE CONSTRAINTS PURGE;
CREATE TABLE BOARD(
	BOARD_NUM			NUMBER,				-- 글 번호
	BOARD_NAME			VARCHAR2(30),		-- 작성자
	BOARD_PASS			VARCHAR2(30),		-- 비밀번호
	BOARD_SUBJECT		VARCHAR2(300),		-- 제목
	BOARD_CONTENT		VARCHAR2(4000),		-- 내용
	BOARD_FILE			VARCHAR2(50),		-- 첨부될 파일
	BOARD_RE_REF		NUMBER,				-- 답변 글 작성시 참조
	BOARD_RE_LEV		NUMBER,				-- 답변 글의 깊이
	BOARD_RE_SEQ		NUMBER,				-- 답변 글의 순서
	BOARD_READCOUNT		NUMBER,				-- 글의 조회수
	BOARD_DATE		DATE DEFAULT SYSDATE,
	PRIMARY KEY(BOARD_NUM)	
);

SELECT * FROM BOARD;
DELETE FROM BOARD;
SELECT * FROM COMM;

insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
VALUES (1,'처음','admin', 1);
insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
VALUES (2,'둘째','admin', 2);
insert into board (BOARD_NUM, BOARD_SUBJECT, BOARD_NAME, BOARD_RE_REF)
VALUES (3,'셋째','admin', 3);

insert into comm (num, id, comment_board_num) values(1,'admin',1);
insert into comm (num, id, comment_board_num) values(2,'admin',1);
insert into comm (num, id, comment_board_num) values(3,'admin',2);
insert into comm (num, id, comment_board_num) values(4,'admin',2);

update  board
set 	board_subject = '오늘도 행복하세요'
where	board_num=1;

-- 1. comm 테이블에서 comment_board_num별 갯수를 구합니다.
COMMENT_BOARD_NUM	CNT
1					2
2					2

SELECT COMMENT_BOARD_NUM, COUNT(*) AS CNT
FROM COMM
GROUP BY COMMENT_BOARD_NUM;

-- 2. board와 조인을 합니다.
BOARD_NUM BOARD_SUBJECT CNT
1		  오늘도 행복하세요 	2
2		  둘째			2

SELECT BOARD_NUM, BOARD_SUBJECT, CNT
FROM BOARD JOIN (SELECT COMMENT_BOARD_NUM, COUNT(*) CNT
				 FROM COMM
				 GROUP BY COMMENT_BOARD_NUM)
ON BOARD_NUM=COMMENT_BOARD_NUM
ORDER BY BOARD_NUM;

-- 문제점) 만약 board 테이블에는 글이 있지만 댓글이 없는 경우 조회가 되지 않음

-- 3.outer join을 이용해서 board의 글이 모두 표시되고 cnt가 null인 경우 0으로 표시되도록 합니다.
BOARD NUM		BOARD_SUBJECT	CNT
3				세째				0
2				둘째				2
1				오늘도 행복하세요		2

-- 1단계) 게시판 글에 댓글이 없는 경우 cnt가 null입니다.
select board_num, board_subject, cnt
from board left outer join (select comment_board_num, count(*) cnt
							from comm
							group by comment_board_num)
on board_num=comment_board_num;

BOARD_NUM BOARD_SUBJECT	CNT
1		  오늘도 행복하세요	2
2		  둘째			2
3		  세째			NULL

-- 2단계) CNT가 NULL인 경우 0으로 만들기
SELECT BOARD_NUM, BOARD_SUBJECT, NVL(CNT,0) AS CNT
FROM BOARD LEFT OUTER JOIN (SELECT COMMENT_BOARD_NUM, COUNT(*) CNT
							FROM COMM
							GROUP BY COMMENT_BOARD_NUM)
ON BOARD_NUM=COMMENT_BOARD_NUM
ORDER BY BOARD_RE_REF DESC,
BOARD_RE_SEQ ASC;

