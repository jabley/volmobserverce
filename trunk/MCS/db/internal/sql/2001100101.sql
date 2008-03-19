-- ---------------------------------------------------------------------------
-- $Header: /src/voyager/db/internal/sql/2001100101.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
-- ---------------------------------------------------------------------------
-- (c) Volantis Systems Ltd 2000. 
-- ---------------------------------------------------------------------------
-- Change History:
--
-- Date         Who             Description
-- ---------    --------------- ----------------------------------------------
-- 01-Oct-01    Allan           VBM:2001100101 - Created.
-- 10-Oct-01    Allan           VBM:2001100106 - Some fields were 
--                              incorrectly set to not null.
-- 16-Oct-01    Allan           VBM:2001101504 - Add generationTime to
--                              license table and use it in the key instead
--                              of licenseFileName. Added this change history.
-- ---------------------------------------------------------------------------

drop table customer;
create table customer
(
	customerNumber		varchar(6) not null,
	salesForceId		varchar(64),
	companyName		varchar(64) not null,
	constraint pk_customer	primary key (customerNumber)
);

drop table site;
create table site
(
	customerNumber		varchar(6) references customer (customerNumber)
                match full
                on delete no action
                on update cascade,
	siteName		varchar(64),
	siteNumber		varchar(6) not null,
	addressLine1		varchar(128),
	addressLine2		varchar(128),
	addressLine3		varchar(128),
	addressLine4		varchar(128),
	city			varchar(64),
	stateProvinceCounty	varchar(64),
	postOrZipCode		varchar(12),
	country			varchar(64),
	phoneNumber		varchar(20),
	faxNumber		varchar(20),
	constraint pk_site	primary key (customerNumber, siteNumber)
);

drop table license;
create table license 
(
	version			varchar(6) not null,
	edition			varchar(32) not null,
	customerNumber		varchar(6) references customer (customerNumber)
                match full
                on delete no action
                on update cascade,
	siteNumber		varchar(6) references site (siteNumber)
                match full
                on delete no action
                on update cascade,
        generationTime          varchar(14) not null,
	emergency		varchar(5) not null,
	product			varchar(32) not null,
	expirationDate		varchar(10),
	machineOS		varchar(32) not null,
	machineInternetName	varchar(64),
	altInternetName1	varchar(64),
	altInternetName2	varchar(64),
	altInternetName3	varchar(64),
	authorisedBy		varchar(64) not null,
	recipientName		varchar(64) not null,
	recipientPhone		varchar(20),
	recipientFax		varchar(20),
	recipientEmail		varchar(64),
	licenseFileName		varchar(128) not null,
	constraint pk_license	primary key (customerNumber, siteNumber,
						generationTime)
);



