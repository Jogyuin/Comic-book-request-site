CREATE TABLE BOOK (
   BOOKNUM   NUMBER      PRIMARY KEY,
   BOOKNAME   VARCHAR2(50)   NOT NULL,
   BOOKWRITER   VARCHAR2(30)   NOT NULL,
   GENRE      VARCHAR2(20)   NOT NULL,
   BOOKLOCATION   VARCHAR2(20)   NOT NULL,
   PUBLISHER      VARCHAR2(30)   NOT NULL
);

CREATE TABLE COMICMEMBER(
    MEMBERID  VARCHAR2(30) PRIMARY KEY,
    MEMBERPW VARCHAR2(100) NOT NULL,
    MEMBERNAME  VARCHAR2(100) NOT NULL,
    NICKNAME  VARCHAR2(30),
    AGE  NUMBER(3),
    PHONE VARCHAR2(20),
    ENABLED  NUMBER(1) DEFAULT 1  CONSTRAINT ENABLED CHECK(ENABLED IN(0, 1)) ,
    ROLENAME VARCHAR2(10) DEFAULT 'ROLE_USER' CONSTRAINT ROLENAME CHECK(ROLENAME IN('ROLE_ADMIN', 'ROLE_USER'))
);

CREATE TABLE RECOMMENDBOARD(
    BOARDNUM NUMBER PRIMARY KEY,
    memberID VARCHAR2(30) REFERENCES COMICMEMBER(MEMBERID),
    NICKNAME VARCHAR2(30),
    BOOKNAME VARCHAR2(50) NOT NULL,
    BOOKWRITER VARCHAR2(30) NOT NULL,
    TITLE VARCHAR2(100) NOT NULL,
    CONTENT VARCHAR2(2000) NOT NULL,
    VIEWCOUNT NUMBER DEFAULT 0,
    INPUTDATE DATE DEFAULT SYSDATE,
    RECOMMENDCOUNT NUMBER DEFAULT 0,
    ORIGINALFILE VARCHAR2(200),
    SAVEDFILE VARCHAR2(200)
);

CREATE TABLE RECOMMEND(   
    RECOMMENDNUM NUMBER PRIMARY KEY,
    memberID VARCHAR2(30) REFERENCES COMICmember (memberID) ON DELETE CASCADE,
    BOARDNUM NUMBER REFERENCES RECOMMENDBOARD (BOARDNUM) ON DELETE CASCADE, 
    RECOMMENDDATE DATE DEFAULT SYSDATE
);

CREATE TABLE BOARDREPLY(
    REPLYNUM NUMBER PRIMARY KEY,
    BOARDNUM NUMBER REFERENCES RECOMMENDBOARD(BOARDNUM) ON DELETE CASCADE,
    MEMBERID VARCHAR2(20) REFERENCES COMICMEMBER (MEMBERID),
    NICKNAME VARCHAR2(30),
    REPLYTEXT  VARCHAR2(1000) NOT NULL,
    INPUTDATE DATE DEFAULT SYSDATE
);

CREATE SEQUENCE RECOMMENDBOARDNUM_SEQ;
CREATE SEQUENCE RECOMMENDBOARDREPLY_SEQ;
CREATE SEQUENCE RECOMMENDNUM_SEQ;
CREATE SEQUENCE BOOK_SEQ;