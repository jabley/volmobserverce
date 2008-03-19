create table layout(
	name varchar2(32),
	rootformat number,
	device varchar2(32),
        defaultfragment varchar2(32))
/

rem this needs some work, particularly the device_layout_locks table needs a
rem primary key of device,layout otherwise locking will not work properly.
