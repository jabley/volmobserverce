<?xml version="1.0"?>
<!DOCTYPE wml PUBLIC "-//WAPFORUM//DTD WML 1.1//EN" "http://www.wapforum.org/DTD/wml_1.1.xml">
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
<%@page import="com.volantis.mcs.devices.Device,
                com.volantis.mcs.utilities.*,
                com.volantis.mcs.repository.*,
                java.sql.*,
                com.volantis.mcs.runtime.ServletRequestHeaders,
                com.volantis.mcs.http.HttpHeaders,
                com.volantis.mcs.http.servlet.HttpServletFactory,
                com.volantis.mcs.devices.DeviceRepository,
                com.volantis.mcs.servlet.MarinerServletApplication" %>
<%@ include file="Volantis-mcs.jsp" %>
<%
    // Translate servlet headers into generic headers
    HttpHeaders httpHeaders = HttpServletFactory.getDefaultInstance().
            getHTTPHeaders(request);

    // Retrieve runtime version of the the device repository.
    DeviceRepository deviceRepository =
            marinerApplication.getRuntimeDeviceRepository();

    // Get the device from the device repository using the headers to id it.
    Device device = deviceRepository.getDevice(httpHeaders);

    // Oldish way of doing it, for reference.
    //Device device = volantisBean.getDevice(connection,
    //        new ServletRequestHeaders(httpHeaders));

    String userAgent = request.getHeader("User-Agent");
    String deviceName = device.getName ();
    String protocol = device.getPolicyValue("protocol");
%>

<% if ( protocol.startsWith("HTML") ) {
        out.clear ();
%><html> <body>
  <pre>		
  <b>USER AGENT STRING: </b>	<%= userAgent %> <br>
  <b>DEVICE MATCH: </b>		<%= deviceName %> <br>
  <b>PROTOCOL MATCH</b> 	<%= protocol %> <br>
  </pre>
  <a href="index.jsp">Home</a><br/>
 </body> </html>
<% } %>

<% if ( protocol.startsWith("Voice") ) {
        out.clear ();
%><html> <body>
  <pre>		
  <b>USER AGENT STRING: </b>	<%= userAgent %> <br>
  <b>DEVICE MATCH: </b>		<%= deviceName %> <br>
  <b>PROTOCOL MATCH</b> 	<%= protocol %> <br>
  </pre>
  <a href="index.jsp">Home</a><br/>
 </body> </html>
<% } %>

<% if ( protocol.startsWith("WML") ) {
	response.setContentType("text/vnd.wap.wml");
        out.clear ();
%> 	
 <wml> <card>
  <p>		
  <strong>USER AGENT STRING: </strong>	<%= userAgent %> <br/>
  <strong>DEVICE MATCH: </strong>		<%= deviceName %> <br/>
  <strong>PROTOCOL MATCH: </strong> 	<%= protocol %> <br/>
  <a href="index.jsp">Home</a><br/>
  </p>
 </card> </wml>
<% } %>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Aug-04	5034/1	geoff	VBM:2004080201 Implement the missing runtime access for device repository

 23-Jul-04	4937/1	byron	VBM:2004072201 Public API for Device Repository: retrieve Device based on Request Headers

 05-Aug-03	928/1	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 ===========================================================================
--%>
