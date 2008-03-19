<?xml version="1.0" encoding="UTF-8"?>
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
<%@ page import="java.lang.Math.*"%>
<%
    String result = null;
    response.setContentType("x-application/vnd.xdime+xml");
    String size = request.getParameter("size");
    String item = request.getParameter("item");
    double dstock = Math.random() * 100.0;
    long stock = Math.round(dstock);   
    
%>
<html xmlns="http://www.w3.org/2002/06/xhtml2"
    xmlns:urid="http://www.volantis.com/xmlns/marlin-uri-driver"
    xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs">
    <head>
        <title>Availability</title>
        
        <!-- Links to the layout and theme for this page -->
        <link rel="mcs:layout" href="/jivearticle.mlyt"/>
        <link rel="mcs:theme" href="/jive.mthm"/>
    </head>
    
    <!-- Page body -->
    <body>
    
        <!-- The common material for the page header -->
        <urid:fetch href="jiveheader.xdinc"/>
        
        <!-- Page Heading -->
        <h2 id="title">Availability</h2>
        
        <!-- The article -->
        <div id="article">
<% if (stock > 0) { %>
            We currently have <%=stock%> units of the <%=item%> in <%=size%> size in stock.
<% } else { %> 
            Unfortunately, the <%=item%> in <%=size%> size is temporarily out of stock.
<% } %>            
        </div>
        
        <!-- The common material for the page footer -->
        <urid:fetch href="jivefooter.xdinc"/>
   </body>
</html>
