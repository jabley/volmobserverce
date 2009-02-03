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

<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
        xmlns:mcs="http://www.volantis.com/xmlns/mcs/config"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        exclude-result-prefixes="mcs"
>
    <!--
     ! Specify text output
     !-->
    <xsl:output method="text"/>

    <xsl:template match="/">
mcs.context.url=<xsl:value-of select="//mcs:mps/@internal-base-url"/>
    </xsl:template>
    
</xsl:stylesheet>
