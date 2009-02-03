drop table sessions;
create table sessions 
(
	session_id	number(38),
	user_id		number(20),
	ttl		number(2)
);

drop index idx_sessions;
create index idx_sessions on sessions(session_id) tablespace INDX;
