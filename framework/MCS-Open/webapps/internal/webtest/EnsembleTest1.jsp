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
 % $Header: /src/voyager/webapp/internal/webtest/EnsembleTest1.jsp,v 1.2 2002/08/07 01:49:02 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 31-Jul-02    Paul            VBM:2002080509 - Created to test multiple
 %                              cards.
 % ======================================================================= --%>


<%@ include file="Volantis-mcs.jsp" %>   

<vt:canvas layoutName="EnsembleTest1"
           pageTitle="EnsembleTest 1">

  <jsp:include page="EnsembleTest1Parts.jsp" flush="false"/>

</vt:canvas>
