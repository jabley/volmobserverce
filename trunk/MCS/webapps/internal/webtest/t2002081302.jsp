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
<%-- ========================================================================== % $Header: /src/voyager/webapp/internal/webtest/t2002081302.jsp,v 1.2 2002/08/14 10:58:36 aboyd Exp $
 % ===========================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ===========================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===========================================
 % 14-Aug-02    Allan           VBM:2002081302 - Created
 % ====================================================================== --%>
<%-- =========================================================================
 % A page with no theme that specifies a styleClass on the pane. This test
 % should not cause a NullPointerException in the protocol class.
 % ====================================================================== --%>

<%@ include file="/Volantis-mcs.jsp" %>

<vt:canvas layoutName="error" type="portal" title="Themes">
<vt:pane name="error" styleClass="test">Test</vt:pane>
</vt:canvas>
