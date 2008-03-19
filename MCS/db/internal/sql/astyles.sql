create table astyles (THEME_NAME VARCHAR2(20), DEVICE VARCHAR2(20), TAG VARCHAR2(20), CLASS VARCHAR2(20), ID VARCHAR2(20), NAME VARCHAR2(25), VALUE VARCHAR2(150), REVISION VARCHAR2(6))
/
insert into astyles (THEME_NAME, DEVICE, TAG,CLASS,ID,NAME,VALUE,REVISION)
select themes.name, themes.device, styles.TAG,styles.CLASS,styles.ID,styles.NAME,styles.VALUE,styles.REVISION from styles, themes where styles.THEME_ID=themes.THEME_ID
/
rename styles to old_styles
/
rename astyles to styles   
/
