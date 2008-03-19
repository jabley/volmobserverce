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

<%@ page
  contentType="x-application/vnd.xdime+xml"
  import="java.util.*" %>
<html
  xmlns="http://www.w3.org/2002/06/xhtml2"
  xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs"
  xmlns:template="http://www.volantis.com/xmlns/marlin-template"
  xmlns:widget="http://www.volantis.com/xmlns/2006/05/widget">

<head>
  <title>Received form</title>
  <link rel="mcs:theme" href="/themes/main.mthm"/>
  <link rel="mcs:layout" href="/layouts/main.mlyt"/>
  <style type="text/css">
    table {
      border-collapse: collapse;
      margin: 0.25em;
    }
    td, th {
      border: 1px solid black;
      padding: 0.25em;
    }
  </style>
</head>

<body>
  <template:apply href="templates/demo-main.xdtpl">
    <template:binding name="title" value="Received form"/>
    <template:binding name="content">
      <template:complexValue>
<%
      Map params = request.getParameterMap();
      if (!params.isEmpty()) {
%>
				<div class="widget">
        <table>
          <tr>
            <th>Name</th>
            <th>Value</th>
          </tr>
<%
          Iterator i = params.keySet().iterator();
          while(i.hasNext()) {
            String paramName = (String)i.next();
            String[] values = (String[])params.get(paramName);
            StringBuffer buffer = new StringBuffer();
            for (int j = 0; j < values.length; j++) {
              if (j > 0) {
                buffer.append(", ");
              }
              buffer.append(values[j]);
            }
            String value = buffer.toString();
%>
            <tr>
              <td><%=paramName%></td>
              <td><%=value%></td>
            </tr>
<%
          }
%>
        </table>
        </div>
<%
      } else {
%>
        Empty form received
<%
      }
%>
      </template:complexValue>
    </template:binding>
  </template:apply>
</body>
</html>

