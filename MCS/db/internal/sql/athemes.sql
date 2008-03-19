create table athemes (NAME VARCHAR2(20), DEVICE VARCHAR2(20), REVISION VARCHAR2(6))
/
insert into athemes (NAME, DEVICE, REVISION)
select name, device, revision from themes
/
rename themes to old_themes
/
rename athemes to themes   
/
