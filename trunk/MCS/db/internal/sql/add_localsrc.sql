alter table DeviceImageAsset add localSrc numeric(1);
alter table GenericImageAsset add localSrc numeric(1);
update deviceImageAsset set localSrc=0;
update genericImageAsset set localSrc=0;
commit;
