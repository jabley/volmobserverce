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
 ! This stylesheet adds additional templates in order to allow the Caption Module
 ! to be unit tested.
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
    exclude-result-prefixes="xh2 mcs xf sel css xsi">

    <xsl:import href="../common.xsl"/>
    <xsl:import href="../document-module.xsl"/>

    <xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>

    <xsl:include href="../miscellaneous-module.xsl"/>
    <xsl:include href="../layout-module.xsl"/>
    <xsl:include href="../caption-module.xsl"/>
    <xsl:include href="../table-module.xsl"/>


    <xsl:template mode="get-style" match="xh2:table">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>CaptionPane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:caption[@class='pane-test']">
        <css:style>
            <css:pane-name>CaptionPane</css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/11	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/8	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/6	pcameron	VBM:2004060306 Fixed test cases after integration

 11-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
