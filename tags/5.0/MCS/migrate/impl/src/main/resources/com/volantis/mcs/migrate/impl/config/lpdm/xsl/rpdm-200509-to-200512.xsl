<?xml version="1.0" encoding="ISO-8859-1"?>
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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.volantis.com/xmlns/2005/12/marlin-lpdm"
    xmlns:old="http://www.volantis.com/xmlns/2005/09/marlin-rpdm"
    xmlns:rpdm="http://www.volantis.com/xmlns/2005/12/marlin-rpdm"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="old">

    <xsl:import href="lpdm-200509-to-200512.xsl"/>

    <xsl:output method="xml" indent="yes"
        xmlns:xalan="http://xml.apache.org/xslt"
        xalan:indent-amount="4"/>

    <!--
      - Copy anything unrecognised with low priority, updating the namespace
      -->
    <xsl:template match="old:*" priority="-3">
        <xsl:element name="rpdm:{local-name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

<!--    <xsl:template match="node()|@*" priority="-5">-->
<!--        <xsl:copy>-->
<!--            <xsl:apply-templates select="node()|@*"/>-->
<!--        </xsl:copy>-->
<!--    </xsl:template>-->

    <!--
      - Upgrade schema version where appropriate
      -->
    <xsl:template match="@xsi:schemaLocation[.='http://www.volantis.com/xmlns/2005/09/marlin-rpdm http://www.volantis.com/schema/2005/09/marlin-rpdm.xsd']">
        <xsl:attribute name="xsi:schemaLocation">http://www.volantis.com/xmlns/2005/12/marlin-rpdm http://www.volantis.com/schema/2005/12/marlin-rpdm.xsd</xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
