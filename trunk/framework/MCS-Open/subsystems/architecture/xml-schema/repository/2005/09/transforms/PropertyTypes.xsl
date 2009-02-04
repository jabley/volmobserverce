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
<xsl:stylesheet version="1.0" xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <xsl:variable name="RT-PropertyTypes">
        <type name="mcs-border-color">BorderColorType</type>
        <type name="border-style">BorderStyleType</type>
        <type name="border-width">BorderWidthType</type>
        <type name="margin-width">MarginWidthType</type>
        <type name="padding-width">PaddingWidthType</type>
        <type name="mcs-position">PositionType</type>
    </xsl:variable>
    <xsl:variable name="PropertyTypes" select="common:node-set($RT-PropertyTypes)"/>
</xsl:stylesheet>
