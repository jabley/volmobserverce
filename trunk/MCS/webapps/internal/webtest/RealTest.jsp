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
 % $Header: /src/voyager/webapp/internal/webtest/RealTest.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 22-Oct-01    Pether          VBM:2001041201 Added a styleClass entry to
 % 				the realvideo tag.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="IndexLayout" >  
<vt:pane name="index">

<vt:realvideo styleClass="from_styleClass" name="missiles" altText="The alternate text"/>

</vt:pane>

<vt:p pane="index">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Multimedia.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>

