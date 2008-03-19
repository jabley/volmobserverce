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

<!-- ==========================================================================
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/metis/typeSelector.xsl,v 1.2 2002/12/13 08:41:16 adrian Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who                        Description
 ! =========    =============== ===============================================
 ! 10-Dec-02    Adrian          VBM:2002100311 - created this transformation to
 !                              remove rules with invalid type selectors.
 ! ======================================================================== -->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt">
    
    <!-- Import common declarations. -->
    <xsl:import href="../common/common.xsl"/>
    
    
    <!--
    
    Transform Rules discarding those with invalid type selectors.
    
    -->
    <xsl:template match="deviceTheme/rule">
        <xsl:choose>
            <xsl:when test="simpleSelectorSequence/typeSelector/@type='dividehint'
            or simpleSelectorSequence/typeSelector/@type='meta'
            or simpleSelectorSequence/typeSelector/@type='region'
            or simpleSelectorSequence/typeSelector/@type='wsdirectives'
            or simpleSelectorSequence/typeSelector/@type='ssiconfig'">       
                <xsl:message terminate="no">WARNING: Excluding Rule. Element selector '<xsl:value-of select='simpleSelectorSequence/typeSelector/@type'/>'  in theme '<xsl:value-of select="../../@name"/>' is not a valid selector.</xsl:message>    
            </xsl:when>                                       
            <xsl:otherwise>
                <rule>
                    <xsl:apply-templates/>
                </rule>
            </xsl:otherwise>
        </xsl:choose>            
    </xsl:template>
    
</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
-->
