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
<vt:canvas layoutName="error">
<vt:h2 pane="error">The form reported the following fields</vt:h2> 
<%
String outline; 

for (Enumeration e = request.getParameterNames() ; e.hasMoreElements(); ) {
    String name = (String) e.nextElement();
    String values[] = request.getParameterValues(name);
    if (values.length == 1) {
        outline = "<b>" + name + " </b>has value " + values[0];
%>
            <vt:p pane="error"><%=outline%></vt:p>
<%
    }
    else {
        for (int i = 0; i < values.length ; i++) { 
            outline = "<b>" + name + " </b>has value " + values[i];
%>
            <vt:p pane="error"><%=outline%></vt:p>
<%
        }
    }
}
%>
</vt:canvas>

