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
<%@page import="java.util.*, javax.mail.*, javax.mail.internet.*" %>
<%@include file="/Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.context.ContextInternals" %>
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %>
<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %>
<% String deviceName = null;
   String deviceProtocol = null;
   String userAgent = null;
   String messagefull = null;
   String idSpec = null;
   long full = 0L;
   long start = 1016720862705L;
%>

<vt:canvas layoutName="device_capture" pageTitle="Submission">

<%
   deviceName = marinerRequestContext.getDeviceName() ;
   deviceProtocol = marinerRequestContext.getDevicePolicyValue ("protocol");
   userAgent = request.getHeader ("User-Agent");
   full = Math.abs((new Date().getTime() - start)/36000);
   messagefull = "Tracking number is: " + full;
%>

<vt:pane name="error">
<vt:h2><%= messagefull %></vt:h2>
<vt:p>Device information has been captured. Please quote this on your submission form.<vt:br/> If you want to resubmit your device details with a description and your name, please enter them below and press the submit the button.<vt:br/><vt:br/><vt:br/></vt:p>
</vt:pane>
<vt:pane name="Paness1"><vt:p> </vt:p></vt:pane>
<vt:pane name="Paness2"><vt:p> </vt:p></vt:pane>
<vt:pane name="Paness3"><vt:p> </vt:p></vt:pane>
<vt:xfform      name="Form1"
                action="index.jsp">
<vt:xftextinput name="id"
                caption="Description: "
                captionPane="Paneq1"
                entryPane="Panea1"/>
<vt:xftextinput name="name"
                caption="Name: "
                captionPane="Paneq2"
                entryPane="Panea2"/>
<vt:xfaction    type="submit"
                caption="Submit"
                captionPane="Paneb1"
                entryPane="Paneb1"/>
<vt:xfaction    type="reset"
                caption="Reset"
                captionPane="Paneb2"
                entryPane="Paneb2"/>
</vt:xfform>
</vt:canvas>

<%  
        Properties props = new Properties();
        props.put("mail.stmp.host", "10.20.30.4");
	Session s = Session.getInstance(props,null);
        MimeMessage message = new MimeMessage(s);

        InternetAddress from = new InternetAddress("Device Capture <jason@volantis.com>");
        message.setFrom(from);
        InternetAddress to = new InternetAddress("jason@volantis.com");
        message.addRecipient(Message.RecipientType.TO, to);
        to = new InternetAddress("martinj@volantis.com");
        message.addRecipient(Message.RecipientType.TO, to);
        to = new InternetAddress("alexia@volantis.com");
        message.addRecipient(Message.RecipientType.TO, to);

        message.setSubject("DC: " + full);
	String a = "\nDevice Name: " + deviceName  +
		"\nDevice Protocol: " + deviceProtocol +
		"\nUser Agent String: " + userAgent  +
		"\n\nHeaders\n";
	

      	Enumeration headers = request.getHeaderNames();
      	while (headers.hasMoreElements() ) {
	        String HeaderName = headers.nextElement().toString();
   	     	Enumeration values = request.getHeaders(HeaderName);

        	while (values.hasMoreElements() ) {
			String HeaderValue = values.nextElement().toString();
			a = a + HeaderName + ": " + HeaderValue + "\n";
		}
	}

	Enumeration params = request.getParameterNames();
	if (params.hasMoreElements()) {
		a = a + "\nParameters\n";

      		while (params.hasMoreElements() ) {
        		String ParamName = params.nextElement().toString();
			String Parameter = request.getParameter(ParamName).toString();
			a = a + ParamName  + ": " + Parameter  + "\n";
      		}
	}

	a = a + "\nRequesting IP Address: " + request.getRemoteAddr() +
		"\nRequesting Id        : " + request.getParameter ("id") +
		"\nRequesting Name      : " + request.getParameter ("name");
	

        message.setText(a);
        Transport.send(message);
%>
