drop table user_prefs;
create table user_prefs
(
userid		number(2),
type		varchar2(10),
preference	varchar2(50),
device		varchar2(255)
);

drop index user_prefs_idx1;
create index idx_user_prefs1 on user_prefs(userid) tablespace indx;

insert into user_prefs values (0,'news', 'Top stories');
