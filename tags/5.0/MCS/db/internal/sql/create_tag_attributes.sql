create table tag_attributes
(  tag varchar2(200) constraint C_TAG not null,
   helptext varchar2(2048) ,
   ui_type varchar2(20) constraint C_UITYPE not null,
   default_value varchar2(1024) constraint C_DEF_VALUE not null,
   revision varchar2(6)
)
