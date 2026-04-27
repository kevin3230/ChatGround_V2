--#使用cmd建立MySQL DB
--create database chatground;

--表格建好後執行下列sql
--#sys_role
insert into sys_role (id, cn_name, role, available, description) values(1, 'admin', 'ADMIN', 1 , null);
insert into sys_role (id, cn_name, role, available) values(2, 'user', 'USER', 1);

--#sys_permission
insert into sys_permission (id, available, name, parent_id, parent_ids, permission, resource_type, url)
values(1, 1, '/chatground/ground', 0, null, 'ground', 'menu', '/chatground/ground');
--#sys_role_permission
insert into sys_role_permission (role_id, permission_id) values(1, 1);
insert into sys_role_permission (role_id, permission_id) values(2, 1);

--#member_sysrole  有利用testMemberRepository.testSave新增會員才執行這行
insert into member_sysrole (mem_id, role_id) values(1, 1);

-- 此script僅當參考用,使用spring.jpa.hibernate.ddl-auto=update 自動建立表格
DROP TABLE MESSAGE;
DROP TABLE MEM_CHAT_ROOM_SET;
DROP TABLE CHAT_ROOM;
DROP TABLE SYSTEM_NOTIFICATION;
DROP TABLE MEMBER;
DROP SEQUENCE SEQ_MEMCHATROOMSETNO;
DROP SEQUENCE SEQ_MEMNO;
DROP SEQUENCE SEQ_MSGNO;
DROP SEQUENCE SEQ_CRNO;
DROP SEQUENCE SEQ_SNNO;

create table MEMBER(
    id bigint PRIMARY KEY NOT NULL,
    account VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nick_name VARCHAR(10),
    email VARCHAR(30) NOT NULL,
    birth DATE,
    gender VARCHAR(255),
    reg_date datetime2(7) NOT NULL,
    status VARCHAR(255),
    picture VARBINARY(max)
);

CREATE TABLE CHAT_ROOM(
    id bigint NOT NULL PRIMARY KEY,
    nick_name VARCHAR(20) NOT NULL
);

CREATE TABLE MEM_CHAT_ROOM_SET (
    id bigint PRIMARY KEY NOT NULL,
    cr_id bigint NOT NULL,
    mem_id bigint NOT NULL
    /*
    CONSTRAINT FK_MEMCHATROOMSET_MEMNO_MEM_NO
    FOREIGN KEY(MEMCHATROOMSET_MEMNO) REFERENCES MEMBER(MEM_NO),
    CONSTRAINT FK_MEMCHATROOMSET_CRNO_CR_NO
    FOREIGN KEY(MEMCHATROOMSET_CRNO) REFERENCES CHATROOM(CR_NO)
    */
);

CREATE TABLE MESSAGE (
    id bigint PRIMARY KEY NOT NULL,
    sender bigint NOT NULL,
    time datetime2(7) NOT NULL,
    CONTENT VARCHAR(500) NOT NULL,
    chatroom_id bigint NOT NULL,
    status VARCHAR(255)
    /*
    CONSTRAINT FK_MSG_FROM_MEM_NO
    FOREIGN KEY(MSG_FROM) REFERENCES MEMBER(MEM_NO),
    CONSTRAINT FK_MESSAGE_CHATROOM
    FOREIGN KEY(MSG_CRNO) REFERENCES CHATROOM(CR_NO)
    */
);

CREATE TABLE SYSTEM_NOTIFICATION(
    id bigint NOT NULL PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    time datetime2(7) NOT NULL
);

CREATE SEQUENCE SEQ_MEMNO
    MAXVALUE 999999999;

CREATE SEQUENCE SEQ_MSGNO
    MAXVALUE 9999999999999999999;
    
CREATE SEQUENCE SEQ_CRNO
    MAXVALUE 9999999999999999999;

CREATE SEQUENCE SEQ_SNNO
    MAXVALUE 999999999999999999;
    
CREATE SEQUENCE SEQ_MEMCHATROOMSETNO
    MAXVALUE 999999999999999999;
