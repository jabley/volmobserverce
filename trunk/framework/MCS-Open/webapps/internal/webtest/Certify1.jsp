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
<%@page import="com.volantis.mcs.utilities.*, com.volantis.mcs.repository.*, com.volantis.mcs.styles.*, java.sql.*, com.volantis.mcs.protocols.*, com.volantis.mcs.repository.jdbc.JDBCRepositoryConnection;"%>
<%
RepositoryConnection conn = volantis.getConnection();
Connection sqlConnection = ((JDBCRepositoryConnection)conn).getConnection();
Device device = volantis.getDevice(conn,request);
String agent = request.getHeader("User-Agent");


String protocol = device.getPolicyValue("protocol");

VolantisProtocol vp = (VolantisProtocol)volantis.getVolantisProtocol(protocol);

String accept = request.getHeader("accept");

if(null != accept) {
 if(accept.indexOf("wap")>0) {
    response.setContentType("text/vnd.wap.wml");
        } else response.setContentType("text/html");
    }

Vector[] policiesValues = new Vector[2];
policiesValues[0] = new Vector();
policiesValues[1] = new Vector();



String devicename = volantis.getDeviceNameFromRequest(conn,request); 


String select = "select distinct policy from policy_values";
Statement stmt = sqlConnection.createStatement();
ResultSet rset = stmt.executeQuery(select);

out.println(vp.protocolString());

if(protocol.indexOf("WML")>=0 || protocol.indexOf("HDML")>=0) {
     out.println("<wml><card><p>");
    }
else out.println("<html><body>");

%>

General information:<br/>
User agent: <%=agent%><br/>
Device match: <%=devicename%><br/>
<br/>


<br/>
<table <%=columns(protocol)%> >
<tr><td>Policy Name</td><td>Policy Value</td></tr>

<%
    int i=0;
    
while(rset.next()) {

    policiesValues[0].add(rset.getString(1));
    policiesValues[1].add(device.getPolicyValue((String)policiesValues[0].elementAt(i)));
    

out.println("<tr><td>"+(String)policiesValues[0].elementAt(i)+"</td><td>"+(String)policiesValues[1].elementAt(i)+"</td></tr>");
    i++;
}

%>

</table><br/>

<%=(i)  %> policies in total.<br/>

<a href="index.jsp">Home</a><br/>
<a href="Certification.jsp">Back</a><br/>

<%
if(protocol.indexOf("WML")>=0 || protocol.indexOf("HDML")>=0) {
 out.println("</p></card></wml>");
    }
else out.println("</body></html>");
%>

<%!

private String columns(String protocol) {
 if (protocol.startsWith("WML")) return " columns=\"2\"";
 else return "";
}
%>









