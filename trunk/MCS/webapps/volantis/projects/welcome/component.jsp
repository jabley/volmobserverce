<?xml version="1.0" encoding="UTF-8"?>
<%@ page%>
<%
    String result = null;
    response.setContentType("x-application/vnd.xdime+xml");    
    String item = request.getParameter("item");
          
%>
<html xmlns="http://www.w3.org/2002/06/xhtml2" 
    xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs">
    <head>
        <title>Component information</title>
        
        <!-- Links to the layout and theme for this page -->
        <link rel="mcs:layout" href="/welcome.mlyt"/>
        <link rel="mcs:theme" href="/welcome.mthm"/>
    </head>
    <body>
       <div class="logo">
          <!-- the logo pane -->
          <object src="images/vol_logo.mimg">
             <span>Volantis Systems Ltd.</span>
          </object>
       </div>
        <h2 class="congratulations">Component information</h2>        
        <!-- The article -->
        <div class="background">
<% if (item.equals("mcs")) { %>
<h3>Multi-Channel Server</h3>
<p>Multi-Channel Server (MCS) helps you manage the
         complexity of delivering a wide variety of content to PCs,
         PDAs, mobile phones, interactive digital TV, internet
         appliances, games consoles, VoiceXML and interactive kiosks.</p>
      <p>To deliver to multiple channels, you need to present content,
         services and applications in a consistent way across all
         target devices. In MCS you can separate application design
         from device delivery, and build a cost-effective and scalable
         system, by defining policies.</p>
<%} else if (item.equals("map")) { %> 
          <h3>Media Access Proxy</h3>
           <p>The Media Access Proxy (MAP) is a web application that is used to
         perform transcoding and transformation operations on media files
         referenced by XDIME 2. It also handles XDIME content and ICS URLs in a
         backward compatible manner.</p>
<% } else { %> 
<h3>Message Preparation Server</h3>
       <p>Message Preparation Server (MPS) builds on the core functionality
         of MCS to allow the optimization of message-based or WAP push content. It
         provides the ability to write applications to generate and
         transmit messages to subscribers’ devices. This allows
         applications to be created that can support mass distribution
         of messages to provide significant end user function. The
         messages might, for example, contain information that users
         had subscribed to.</p>
           <% } %>
           <p><a href="simple_welcome.xdime">Welcome</a></p>          
        </div>
       <div class="copyright">
          <p class="copyr">Copyright &#x00A9; 2000-2008 Volantis Systems Ltd.
             All Rights Reserved.</p>
          <p class="copyr">Volantis&#x2122; is a trademark of Volantis
             Systems Ltd.</p>
       </div>           
   </body>
</html>
