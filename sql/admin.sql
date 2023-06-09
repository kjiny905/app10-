drop user c##spring;
--계정생성
CREATE USER c##spring IDENTIFIED BY spring1234
    DEFAULT TABLESPACE users
    TEMPORARY TABLESPACE temp
    PROFILE DEFAULT;
--권한부여
GRANT CONNECT, RESOURCE TO c##spring;
GRANT CREATE VIEW, CREATE SYNONYM TO c##spring;
GRANT UNLIMITED TABLESPACE TO c##spring;
--락 풀기
ALTER USER c##product ACCOUNT UNLOCK;