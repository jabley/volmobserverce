drop table headlines;
create table headlines
(
article 	varchar2(40),
url		varchar2(256),
headline_text 	varchar2(256),
source 		varchar2(50),
media_type 	varchar2(100),
cluster_name 	varchar2(50),
tagline 	varchar2(256),
document_url 	varchar2(256),
harvest_time 	date,
access_registration varchar2(256),
access_status 	varchar2(10),
category_id	number(10)
);
