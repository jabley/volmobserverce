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
 ! Supports layout mapping done within XDIME.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css">

    <!--
     ! ========================================================================
     ! Mode: get-style
     !
     !     Templates that match in this node must return the appropriate
     !     style to use for the specified element.
     !
     !     The style is represented as
     ! ========================================================================
     !-->

    <!--
     ! Prevent any other templates from matching in this mode.
     !-->
    <xsl:template mode="get-style" match="node()[not(self::*)]" priority="1000">
        <xsl:message terminate="yes">
            <xsl:text>Mode: get-style only applies to elements.</xsl:text>
        </xsl:message>
    </xsl:template>

    <!--
     ! Get the default style.
     !-->
    <xsl:template mode="get-style" match="*">
        <css:style/>
    </xsl:template>

    <!--
     ! Named Template: get-style
     !
     !     Get the style for the current element.
     !
     ! This applies the templates in mode 'get-style' to the current element
     ! and returns the result.
     !-->
    <xsl:template name="get-style">
        <xsl:apply-templates mode="get-style" select="."/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 ===========================================================================
-->
