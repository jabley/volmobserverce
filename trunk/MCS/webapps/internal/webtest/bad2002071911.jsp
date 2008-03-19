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
 % $Header: /src/voyager/webapp/internal/webtest/bad2002071911.jsp,v 1.1 2002/07/22 15:34:37 philws Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Jul-02    Phil W-S        VBM:2002071911 Created
 % ======================================================================= --%>

<%-- This file is used to test the occurance of block elements outside --%>
<%-- any pane element and without a pane name specification. This must --%>
<%-- result in an exception being thrown and therefore a page output --%>
<%-- failure --%>
<%@ include file='Volantis-mcs.jsp' %>

<vt:canvas layoutName='error'>
    <vt:p>This text should not appear as the element is not in or</vt:p>
    <vt:p>associated with a pane</vt:p>
</vt:canvas>
