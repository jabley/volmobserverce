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

<%@ page import="com.volantis.mcs.context.ContextInternals" %> 
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %> 

<vt:canvas layoutName="tt_protocol">

<vt:pane name="info">
<vt:p>
Filename: tt_protocol.jsp<vt:br/>
Layout: tt_protocol <vt:br/>
</vt:p>
</vt:pane>

<%
   String deviceName = marinerRequestContext.getDeviceName() ;
   String deviceProtocol = marinerRequestContext.getDevicePolicyValue ("protocol");
   String userAgent = request.getHeader ("User-Agent");
%>

<vt:pane name="main">
<vt:p>
<vt:br/>
<vt:b>Device Name:</vt:b> <%= deviceName %> <vt:br />
<vt:b>Device Protocol:</vt:b> <%= deviceProtocol %> <vt:br />
<vt:b>User Agent String:</vt:b> <%= userAgent %> <vt:br />
<vt:br/>
<vt:a href="tt_devices.jsp">Devices Index</vt:a></vt:br>
<vt:a href="index.jsp">Main Index</vt:a>
</vt:p>
</vt:pane>

</vt:canvas>

