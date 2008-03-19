rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2001122803.sql,v 1.1 2002/02/11 18:24:50 adrian Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 11-Feb-02    Adrian          VBM:2001122803 - sql to insert the missing
rem                              quicktime plugin attributes codebase and
rem                              classid
rem ---------------------------------------------------------------------------

INSERT INTO VMbrplugin_attrs (name, attrname, attrvalue) VALUES ('qtimeinpage', 'ClassId', 'clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B');
INSERT INTO VMbrplugin_attrs (name, attrname, attrvalue) VALUES ('qtimeinpage', 'CodeBase', 'http://www.apple.com/qtactivex/qtplugin.cab');
commit;
