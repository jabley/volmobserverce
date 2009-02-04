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
 % $Header: /src/voyager/webapp/internal/webtest/Chart2002052705.jsp,v 1.2 2002/07/26 11:12:03 philws Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Jul-02    Phil W-S        VBM:2002052705 Created
 % ======================================================================= --%>

<%-- This JSP is used to check that WBMP charts can be generated for devices
     that only support WBMP. It can also be used to check that the precedence
     rules on pixel depth vs image type operate correctly --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="IndexLayout">  
    <vt:chart pane="index"
              name="Chart2002052705"
              data="50 60 70,80 60 40"
              altText="The chart asset may be missing or an appropriate image type not found"
              labels="Jan Feb Mar" title="The chart title"/>  

    <vt:p pane="index">
        <vt:br/>There should be a chart above that uses a chart asset called Chart2002052705.
    </vt:p>
</vt:canvas>
