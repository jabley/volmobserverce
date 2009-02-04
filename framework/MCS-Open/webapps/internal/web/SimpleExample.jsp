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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!-- 
Illustration of the use of a bean with a page and a tag library. 
-->
<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="TestLayout1">
<html>
<head>
<title>Volantis Demo Tag</title>
<link rel=stylesheet
      href="css/JSP-Styles.css"
      type="text/css">
</head>
<body>
<h3>The Simple Example Page</h3>
<h4>This next paragraph uses the Volantis paragraph tag</h4>
<vt:p pane="Articles">
Informix Upgrades Java Database.
Informix (NASDAQ: IFMX) says it has upgraded its Cloudscape Java-based database to 
support Windows CE  and Pocket PC. In addition to adding platforms, Cloudscape 3.5
has added greater security. 
</vt:p>
<vt:p pane="Articles">
Cloudscape 3.5 consists of the Cloudscape database management system, Cloudsync for 
data and application synchronization and Cloudconnector, a server framework for 
Internet connections to the Cloudscape DBMS. 
</vt:p>
<vt:p pane="Articles">
The company is demonstrating the update at the JavaOne conference this week in San Francisco.
 It will be commercially available in July 2000. Server pricing starts at $1,999.
</vt:p>
<p>Here we retrieve the <b>announce</b> property of the bean directly.
The value is:<b><%=volantis.getAnnounce()%></b></p>
<p>And this was all built with the new make!</p>
</body>
</html>
</vt:canvas>
