rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002050901.sql,v 1.1 2002/05/09 16:55:51 aboyd Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 09-May-02    Allan           VBM:2002050901 - Created. Remove the type
rem                              attribute for flashinpage plugin attributes
rem                              and update the codebase and pluginspage 
rem                              attributes.
rem ---------------------------------------------------------------------------
delete from vmbrplugin_attrs where name='flashinpage' and attrname='MimeType';
update vmbrplugin_attrs set attrvalue='http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,23,0' where name='flashinpage' and attrname='CodeBase';
update vmbrplugin_attrs set attrvalue='http://www.macromedia.com/go/getflashplayer' where name='flashinpage' and attrname='PlugInsPage';
commit;
