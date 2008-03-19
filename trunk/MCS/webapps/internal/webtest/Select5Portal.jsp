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
<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="tt_portal"
           pageTitle="Select Portal Test"
           type="portal">
    <vt:p pane="header">
        Header
        <% out.write("Name is " + request.getParameter("name") + ". "); %>
        <% out.write("Region is " + request.getParameter("region")); %>
    </vt:p>

    <vt:select expr="request:getParameter('region')">
        <vt:when expr="'Region1'">
            <vt:region name="Region1">
                <jsp:include page="Select5Portlet.jsp">
                    <jsp:param name="pane" value="test1"/>
                </jsp:include>
            </vt:region>
        </vt:when>
        <vt:when expr="'Region2'">
            <vt:region name="Region2">
                <jsp:include page="Select5Portlet.jsp">
                    <jsp:param name="pane" value="test2"/>
                </jsp:include>
            </vt:region>
        </vt:when>
    </vt:select>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Aug-03	1008/1	philws	VBM:2003080805 Provide implementation of the select, when and otherwise PAPI elements

 ===========================================================================
--%>
