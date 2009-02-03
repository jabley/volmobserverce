
drop table LOCKS;
drop table LOCK_HOLDERS;

create table LOCKS (
       class VARCHAR (64) not null,
       name VARCHAR (256) not null,
       key VARCHAR (1024) not null,
       CONSTRAINT PK_LOCKS PRIMARY KEY (class, name, key)
);

create table LOCK_HOLDERS (
       class VARCHAR (64) not null,
       name VARCHAR (256) not null,
       key VARCHAR (1024) not null,
       username VARCHAR (32),
       hostname VARCHAR (128),
       CONSTRAINT PK_LOCK_HOLDERS PRIMARY KEY (class, name, key)
);

