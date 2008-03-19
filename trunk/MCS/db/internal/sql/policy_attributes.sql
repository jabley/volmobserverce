drop table policy_attributes;
create table policy_attributes
(
	policy	varchar2(200) constraint C_POLICY not null,
	helptext	varchar2(2048),
	ui_type	varchar2(20) constraint C_UITYPE not null,
	default_value	varchar2(1024) constraint C_DEF_VALUE not null,
	category	varchar2(24)
	longname	varchar2(64)
);

drop index idx_polattr;
create unique index idx_devpol on policy_attributes (policy) tablespace INDX; 
