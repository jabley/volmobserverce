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
<%-- ==========================================================================
 % (c) Volantis Systems Ltd 2003. 
 % ======================================================================= --%>
<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:usePipeline>
            <vtxml:transform>
                <vtxml:transformation>
                    <vtxml:content>
                        <xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
                            <xsl:template match="/">
                                <h2>Keyword search for Douglas Adams</h2>
                                <table border="1">
                                    <tr bgcolor="#9acd32">
                                        <th align="left">Title</th>
                                        <th align="left">Price</th>
                                    </tr>
                                    <xsl:for-each select="catalog/cd">
                                        <tr>
                                            <td>
                                                <xsl:value-of select="response/Details/ProductName"/>
                                            </td>
                                            <td>
                                                <xsl:value-of select="response/Details/ListPrice"/>
                                            </td>
                                        </tr>
                                    </xsl:for-each>
                                </table>
                            </xsl:template>
                        </xsl:stylesheet>
                    </vtxml:content>
                </vtxml:transformation>
                <wsd:request>
                    <wsd:wsdl-operation
                        wsdl="http://www.xmethods.net/sd/2001/BabelFishService.wsdl"
                        portType="BabelFishPortType"
                        operation="BabelFish"/>
                    <wsd:message>
                        <!-- Inlined message content wrapped in a pipeline:content tag -->
                        <!-- http://www.volantis.com/web-service-driver -->
                        <pipeline:content>
                            <opns:translationmode xmlns:opns="http://www.volantis.com/">en_fr</opns:translationmode>
                            <opns:sourcedata xmlns:opns="http://www.volantis.com/">Hello</opns:sourcedata>
                        </pipeline:content>
                    </wsd:message>
                </wsd:request>
            </vtxml:transform>
        </vt:usePipeline>
    </vt:pane>
</vt:canvas>
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-03	625/1	byron	VBM:2003022823 Support web service integration within a JSP page

 ===========================================================================
--%>
