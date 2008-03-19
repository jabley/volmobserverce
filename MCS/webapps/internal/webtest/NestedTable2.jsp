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
 % $Header: /src/voyager/webapp/internal/webtest/NestedTable2.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 14-Dec-01    Doug            VBM:2001110103 - Created
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %> 
<vt:canvas layoutName="error"> 
<vt:pane name="error">
<vt:table cols="2"> 
<vt:tr>
	  <vt:td>t1r1c1</vt:td>
	  <vt:td>t1r1c2</vt:td>
</vt:tr>

<vt:tr>
	  <vt:td>
	    <vt:table cols="2"> 
	      <vt:tr>
		<vt:td>t2r1c1</vt:td>
		<vt:td>t2r1c2</vt:td>
	      </vt:tr>

	    </vt:table> 
	  </vt:td>
	  <vt:td>t1r2c2</vt:td>
</vt:tr>

<vt:tr>
	  <vt:td>t1r3c1</vt:td>
	  <vt:td>
	    <vt:table cols="3"> 
	      <vt:tr>
		<vt:td>t3r1c1</vt:td>
		<vt:td>t3r1c2</vt:td>
		<vt:td>t3r1c3</vt:td>
	      </vt:tr>
	      <vt:tr>
		<vt:td>t3r2c1</vt:td>
		<vt:td>t3r2c2</vt:td>
		<vt:td>t3r2c3</vt:td>
	      </vt:tr>

	    </vt:table> 
	  </vt:td>

</vt:tr>

</vt:table> 
</vt:pane> 
</vt:canvas> 

