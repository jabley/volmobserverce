REM Rename accessKey to shortcut in attributes

update format_attributes set name='NextShardShortcut'
       where name='NextShardAccessKey';
 
update format_attributes set name='PreviousShardShortcut'
       where name='PreviousShardAccessKey';
