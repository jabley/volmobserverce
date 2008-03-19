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
<%@ page import="java.io.*" %>
<vt:canvas layoutName="error">

<%
String fileOut = "volantis.cfg";
int j = 1;
OutputStreamWriter op = new OutputStreamWriter(new FileOutputStream(fileOut));
op.write("############################################################\n");
op.write("#          Volantis Systems Ltd Configuration File         #\n");
op.write("#               Do not edit or change this file            #\n");
op.write("############################################################\n");
op.write("#                    Database Configuration                #\n");
op.write("############################################################\n");
for (Enumeration e = request.getParameterNames() ; e.hasMoreElements(); ) {
    String name = (String) e.nextElement();
    String values[] = request.getParameterValues(name);
    if (values.length == 1) {
        op.write(j + ":" + name + ":" + values[0]+"\n");
        j++;
%>
<%
    }
    else {
        for (int i = 0; i < values.length ; i++) { 
            op.write(i + ":" + name + ":" + values[i]+"\n");
%>
<%
        }
    }
}
op.write("############################################################\n");
op.write("#          Volantis Systems Ltd Configuration File         #\n");
op.write("############################################################\n");
op.flush();
op.close();
%>
            <vt:p pane="error"><%="Database Configuration Successful"%></vt:p>
</vt:canvas>
