REM Add primary keys to the layout tables to improve the integrity
REM of the repository.

REM Remove any null devices or layouts.
delete from layouts where layout is null;
delete from device_layouts where layout is null or device is null;
delete from layout_formats where layout is null or device is null;
delete from format_attributes where layout is null or device is null;

REM Add primary keys.
alter table layouts
 add constraint pk_layouts
  primary key (layout) 
;

alter table device_layouts
 add constraint pk_devicelayouts
  primary key (layout, device) 
;

alter table layout_formats
 add constraint pk_layoutformats
  primary key (layout, device, instance) 
;

alter table format_attributes
 add constraint pk_formatattributes
  primary key (layout, device, instance, name) 
;
