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
 % $Header: /src/voyager/webapp/internal/webtest/ExternalTagTest1.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 12-Dec-01    Paul            VBM:2001121201 - Created.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<%@ include file="Volantis-test.jsp" %>

<%-- ==========================================================================
 % This page tests what happens when we have an external tag containing a
 % volantis block tag. The external tag simply evaluates its body but does
 % nothing with its body content. It should work as if the enclosing tag
 % was not there.
 % ======================================================================= --%>

<vt:canvas layoutName="error" pageTitle="ExternalTagTest1">

<test:evalBody>
  <vt:p pane="error">
    Evaluated
  </vt:p>
</test:evalBody>

</vt:canvas>
