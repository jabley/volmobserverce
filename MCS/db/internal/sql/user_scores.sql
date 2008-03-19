drop table user_scores;
create table user_scores
(
userid		number(10),
category_id	number(10),
score		number(10),
device		varchar2(255)
);


drop index idx_user_scores ;
create index idx_user_scores on user_scores(userid, category_id, device) tablespace INDX;
