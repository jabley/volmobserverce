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

<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:exslt-common="http://exslt.org/common"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css"
    xmlns:str="http://exslt.org/strings"
    xmlns:si="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si">

    <!--
     ! Ideally the XSLT stripping mechanism should be used in order to strip
     ! out extraneous white space only text nodes. However, that does not
     ! adhere to the XHTML 2 specification which states that text node
     ! stripping and normalization must be done under the control of CSS.
     !-->

    <!-- Common templates, variables etc. -->
    <xsl:import href="common.xsl"/>

    <!-- Include XHTML 2 Module Templates. -->
    <xsl:import href="document-module.xsl"/>
    <xsl:import href="hypertext-module.xsl"/>
    <xsl:import href="list-module.xsl"/>
    <xsl:import href="meta-module.xsl"/>
    <xsl:import href="miscellaneous-module.xsl"/>
    <xsl:import href="object-module.xsl"/>
    <xsl:import href="structural-module.xsl"/>
    <xsl:import href="table-module.xsl"/>
    <xsl:import href="text-module.xsl"/>

    <!-- Include XForms Module Templates. -->
    <xsl:import href="xforms-core-module.xsl"/>
    <xsl:import href="xforms-controls-module.xsl"/>
    <xsl:import href="xforms-instance-data-module.xsl"/>

    <!-- Include DIWG Module Templates. -->
    <xsl:import href="diselect-module.xsl"/>

    <!-- Include XDIME Templates. -->
    <xsl:import href="layout-module.xsl"/>

    <!-- Workarounds for missing Peter's stuff. -->
    <xsl:template match="@class">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Sep-04	5380/18	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and reverted to interim si namespace

 27-Aug-04	5329/1	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 27-Aug-04	5310/2	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 17-Jun-04	4630/10	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/8	pduffin	VBM:2004060306 Some more changes

 15-Jun-04	4630/6	pduffin	VBM:2004060306 Added some more xdime cp stuff

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
