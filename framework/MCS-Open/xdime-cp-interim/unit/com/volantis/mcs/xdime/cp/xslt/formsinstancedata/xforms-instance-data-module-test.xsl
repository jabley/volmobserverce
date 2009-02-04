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
 ! This stylesheet adds supporting templates in order to allow the
 ! xforms-instance-data module to be unit tested.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css"
    xmlns:si="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si"
    exclude-result-prefixes="xh2 mcs xf sel css xsi si">

    <xsl:import href="../common.xsl"/>
    <xsl:import href="../document-module.xsl"/>

    <xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>

    <xsl:include href="../common-functions.xsl"/>
    <xsl:include href="../layout-module.xsl"/>
    <xsl:include href="../xforms-core-module.xsl"/>
    <xsl:include href="../xforms-controls-module.xsl"/>
    <xsl:include href="../xforms-instance-data-module.xsl"/>

    <xsl:template match="/mcs:unit">
        <xsl:element name="unit">
            <!--
            <xsl:apply-templates select="@*"/>
            -->
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="get-style" match="xf:input | xf:select |
                                          xf:textarea | xf:select1">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>myEPane</css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Sep-04	5380/14	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and reverted to interim si namespace

 27-Aug-04	5310/2	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 ===========================================================================
-->
