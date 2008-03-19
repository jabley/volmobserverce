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
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2005. 
 ! ============================================================================
 !-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <!--
     ! To get XSLT to output a DOCTYPE declaration the output has to be
     ! specified. Note that this assumes we're using the Servlet 2.3
     ! specification.
     !-->
    <xsl:output method="xml"
        doctype-system="http://java.sun.com/dtd/web-app_2_3.dtd"
        doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"/>

    <!--
     ! Conditionally only process the web-app element content if the
     ! markup to insert doesn't already exist in the file
     !-->
    <xsl:template match="web-app">
        <xsl:choose>
            <xsl:when test="@markup-exists@">
                <xsl:copy-of select="."/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:apply-templates/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
     ! When the markup doesn't exist, copy all "standard" markup.
     !-->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--
     ! When the markup doesn't exist, match the given existing equivalent
     ! markup, copy it and insert the required new markup after and/or before
     ! the insertion point.
     !-->
    <xsl:template match="@insert-point@">
        @insert-before-markup@
        <xsl:copy>
            <xsl:apply-templates/>
        </xsl:copy>
        @insert-after-markup@
    </xsl:template>

</xsl:stylesheet>
