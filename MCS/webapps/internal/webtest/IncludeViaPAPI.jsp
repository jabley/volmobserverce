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
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 -->
<%@ include file="Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.papi.IncludeElement" %>
<%@ page import="com.volantis.mcs.papi.PAPIElement" %>
<%@ page import="com.volantis.mcs.papi.IncludeAttributes" %>

<%-- This tests that the include element works correctly and properly --%>
<%-- integrates the pipeline with the MCS PAPI framework --%>
<vt:canvas layoutName="error">
    <vt:pane name="error">
        <%
            IncludeElement include = new IncludeElement();
            IncludeAttributes attributes = new IncludeAttributes();

            attributes.setHref("IncludeViaPAPI.xml");
            attributes.setParse("xml");

            include.elementStart(marinerRequestContext,
                                 attributes);
            include.elementEnd(marinerRequestContext,
                               attributes);

            attributes.setHref("IncludeViaPAPI.txt");
            attributes.setParse("text");

            include.elementStart(marinerRequestContext,
                                 attributes);
            include.elementEnd(marinerRequestContext,
                               attributes);
        %>
        <vt:b>
            <%
                attributes.setHref("IncludeViaPAPI.txt");
                attributes.setParse("text");

                include.elementStart(marinerRequestContext,
                                     attributes);
                include.elementEnd(marinerRequestContext,
                                   attributes);
            %>
        </vt:b>
    </vt:pane>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Jun-03	552/1	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 ===========================================================================
--%>
