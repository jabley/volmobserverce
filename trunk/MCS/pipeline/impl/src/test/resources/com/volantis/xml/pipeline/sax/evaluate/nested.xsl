<?xml version="1.0"?>
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

<!-- ==========================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ======================================================================== -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="toBeTransformed">
        <pipeline:evaluate xmlns:pipeline="http://www.volantis.com/xmlns/marlin-pipeline">
            <pipeline:transform href="include.xsl">
                <willBeTransformed/>
            </pipeline:transform>
        </pipeline:evaluate>
    </xsl:template>
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 18-Oct-04	906/1	doug	VBM:2004101313 Added an Evaluate pipeline process

 ===========================================================================
-->
