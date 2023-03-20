CREATE SCHEMA IF NOT EXISTS SCOTT;

DROP TABLE IF EXISTS SCOTT.MEMBER CASCADE;
CREATE TABLE SCOTT.MEMBER(
    id integer not null,
    member_name varchar(255),
    primary key (id)
);

insert into SCOTT.MEMBER(id, member_name) values (1, 'tiger');
insert into SCOTT.MEMBER(id, member_name) values (2, 'lion');
insert into SCOTT.MEMBER(id, member_name) values (3, 'tiger king');

