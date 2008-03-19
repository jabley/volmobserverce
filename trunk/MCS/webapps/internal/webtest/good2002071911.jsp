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
 % $Header: /src/voyager/webapp/internal/webtest/good2002071911.jsp,v 1.1 2002/07/22 15:34:37 philws Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Jul-02    Phil W-S        VBM:2002071911 Created
 % ======================================================================= --%>

<%-- This file is used to test the occurance of block elements within --%>
<%-- a pane element and with a pane name specification. This should --%>
<%-- result in a successful page rendering --%>
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="tt_layout" pageTitle="Pane Identification Test">
    <vt:pane name="pane1">
        <vt:p>If you can see this page the test has passed</vt:p>
    </vt:pane>
    <vt:p pane="pane2">Something is in pane 2 as well</vt:p>
</vt:canvas>
