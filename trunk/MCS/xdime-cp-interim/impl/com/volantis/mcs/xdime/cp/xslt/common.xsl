<?xml version="1.0" encoding="UTF-8"?>
<!--
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
-->


<!--
 ! Contains common templates that are used by all the modules.
 !
 ! This needs to be included in each module when run stand alone.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect">



    <!--
     ! Raise an error for unknown elements, attributes or unexpected text.
     !-->
    <xsl:template match="*">
        <xsl:call-template name="unexpected-element"/>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:call-template name="unexpected-attribute"/>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:call-template name="unexpected-text"/>
    </xsl:template>

    <!--
     ! Mode: unexpected
     !     Any templates that match in this mode are unexpected and so should
     !     fail just as for above. The reason this is done in another mode is
     !     to allow templates control over when it happens.
     !-->
    <xsl:template mode="unexpected" match="*">
        <xsl:call-template name="unexpected-element"/>
    </xsl:template>

    <xsl:template mode="unexpected" match="@*">
        <xsl:call-template name="unexpected-attribute"/>
    </xsl:template>

    <xsl:template mode="unexpected" match="text()">
        <xsl:call-template name="unexpected-text"/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-04	4630/5	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/6	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
