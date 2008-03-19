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
    xmlns="http://www.volantis.com/xmlns/device-repository/device-tac-identification"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <xsl:template match="/">
    <tacIdentification xsi:schemaLocation="http://www.volantis.com/xmlns/device-repository/device-tac-identification http://www.volantis.com/schema/device-repository/v3.0/device-tac-identification.xsd">
      <xsl:apply-templates/>
    </tacIdentification>
  </xsl:template>
  <xsl:template match="*[local-name()='device']">
    <device name="{@name}"/>
    <xsl:apply-templates/>
  </xsl:template>
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Aug-04	5167/2	adrianj	VBM:2004081107 Created MDPRArchiveAccessor for reading device repository

 ===========================================================================
-->
