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
<%@ page import="java.util.*" %>

<vt:canvas layoutName="error">

<vt:p pane="error">
<vt:h2>The request parameters and their values are:</vt:h2>
<vt:h3>Name and Value(s)  </vt:h3>

<%

  Enumeration e = request.getParameterNames();
  Hashtable table = new Hashtable();
  int maxColumns = 0;  
  int currentColumns = 0;
  Vector keys = new Vector();

 while(e.hasMoreElements())
 {
    
    String name = (String)e.nextElement();
    keys.add(name);
    String[] values = request.getParameterValues(name);
    currentColumns = values.length;
    if(currentColumns > maxColumns) maxColumns = currentColumns;
    
	table.put(name,values);
   
 }

 String columns = new Integer(maxColumns).toString();
 pageContext.setAttribute("COLUMNS",columns);
 
%>

<vt:table title="Names and Value(s)" cols="2">
<vt:tr><vt:td><vt:b>Variable name</vt:b></vt:td><vt:td><vt:b>Variable value(s)</vt:b></vt:td></vt:tr>

<%
	for (int index=0; index < keys.size(); index++ ) {
	
	String varname = (String)keys.get(index);
	String[] varvalues = (String[]) table.get(varname);
	
%>
	<vt:tr><vt:td><%=varname%></vt:td>
<%
	for(int i=0;i<varvalues.length;i++)
		{	
%>
	<vt:td><%=varvalues[i]%></vt:td>
<%
 		}
%>
	</vt:tr>
<%
	}

%>

</vt:table>
<%
pageContext.getOut().print("<br/>columns (String): "+columns+"<br/>maxColumns (int): "+maxColumns);
%>
</vt:p>
</vt:canvas>

