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
<vt:canvas layoutName="Iterated">


<%

//I have been changing these sort of pages every 1.5 weeks
//in order to conform with the latest code at the time. It is not my fault
//that everything keeps changing every 1 week or so, so please show some
//tolerance if they break once in a while ;-) Alex

RepositoryConnection reConnection = volantis.getConnection();
Connection connection = ((JDBCRepositoryConnection)reConnection).getConnection();
Device device = volantis.getDevice(reConnection,request);
String agent = request.getHeader("User-Agent");

String devicename = volantis.getDeviceNameFromRequest(reConnection,request); 

String select = "select distinct policy from policy_values";
Statement stmt = connection.createStatement();
ResultSet rset = stmt.executeQuery(select);

Vector policies = new Vector();
Vector values = new Vector();

while(rset.next())
    {
        String policyName = rset.getString(1);
        String policyValue = "NULL";

        if(device.getPolicyValue(policyName) != null)
        {
            policyValue = device.getPolicyValue(policyName);
        }
        
        policies.add(policyName);
        values.add(policyValue);

    }

%>


<vt:pane name="plain">
<vt:p>
<vt:h3>General Information</vt:h3>
<vt:b>The User agent String was: <%= agent %></vt:b><vt:br/>
<vt:b>This device was identified as: <%= devicename %></vt:b><vt:br/>
</vt:p>

<vt:p>
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Layouts.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:pane>


<vt:p pane="iterated">
<vt:h3>Policies for this device are:</vt:h3>
<vt:b>Policy Name and Policy Value</vt:b><vt:br/>
</vt:p>

<%
for(int c=0; c < (Math.min(policies.size(),values.size())) ; c++)
    {
%>
<vt:p pane="iterated">
<vt:b><%= (String)policies.elementAt(c)%> <%= (String)values.elementAt(c) %></vt:b>
</vt:p>
<%
    }
%>

<vt:p pane="iterated">
<vt:h3>There where <%= Math.min(policies.size(), values.size()) %> policies found</vt:h3>
</vt:p>

</vt:canvas>
