drop table policy_values;
create table policy_values
(
	name	varchar2(20),
	policy	varchar2(200),
	value	varchar2(1024),
);

create index idx_dev on policy_values (name,policy) tablespace INDX; 
/
