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
 ! This stylesheet adds additional templates in order to allow the Object Module
 ! to be unit tested.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css"
    xmlns:cfg="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/cfg"
    exclude-result-prefixes="xh2 mcs xf sel css cfg">

    <xsl:import href="../common.xsl"/>
    <xsl:import href="../common-functions.xsl"/>
    <xsl:import href="../document-module.xsl"/>

    <xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>

    <xsl:include href="../layout-module.xsl"/>
    <xsl:include href="../miscellaneous-module.xsl"/>
    <xsl:include href="../object-module.xsl"/>


    <xsl:template match="/mcs:unit">
        <xsl:element name="unit">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template name="get-customer-configuration">
        <cfg:configuration>
            <cfg:projects>
                <cfg:default>
                    <cfg:prefix>http://www.volantis.com/peterca/</cfg:prefix>
                </cfg:default>
            </cfg:projects>
        </cfg:configuration>
    </xsl:template>


    <xsl:template mode="get-style" match="xh2:div | xh2:p">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>DivPane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:object">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>object-pane</css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Sep-04	5447/3	pcameron	VBM:2004090711 object src attribute supports servlets

 21-Jun-04	4645/13	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/6	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/3	pcameron	VBM:2004060306 Fixed test cases after integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
