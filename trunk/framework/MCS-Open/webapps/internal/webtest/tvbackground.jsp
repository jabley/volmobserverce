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
 % $Header: /src/voyager/webapp/internal/webtest/tvbackground.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 31-Oct-01    Doug            VBM:2001092806 - Created
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="background" theme="mat">

<vt:p pane="pane"><vt:b>Pane data </vt:b>
</vt:p>
<vt:p pane="rowIter"><vt:b>Row Iterator data </vt:b>
</vt:p>
<vt:p pane="colIter"><vt:b>Column Iterator data </vt:b>
</vt:p>
<vt:p pane="dis"><vt:b>Dissecting pane data </vt:b>
</vt:p>

</vt:canvas>


