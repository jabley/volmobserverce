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
<!--  *************************************************************************
        (c) Volantis Systems Ltd 2001. 
      *************************************************************************
        Revision Info
        Name    Date            Comment
        MJ      31/01/2002      Created this file

      *************************************************************************
-->
<%@page import="java.text.DateFormat" %>
<%@page import="java.util.*" %>
<%@include file="/Volantis-mcs.jsp" %>

<%  
  DateFormat full =
    DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
  String message = "Date is: " + full.format(new Date());
  System.out.println("Executing cacheTest at " + full.format(new
Date()));
%>
<vt:canvas cacheScope="all" maxAge="60"
  layoutName="tt_default" pageTitle="Cache Test">

<vt:pane name="fileinfo">
  <vt:h1>Page Cache Test</vt:h1>
  <vt:p>Filename: tt_cache1.jsp<vt:br/>
        Layout: tt_default<vt:br/>
        Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to ensure the mariner page cache works.</vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A page should be created with a date time stamp. </vt:p>
  <vt:p><vt:b>Refresh this page to see if the time changes.</vt:b></vt:p>
  <vt:p>The time should not change on every page refresh since the page should be read from a cache. NB: The page has a 60 second cache life. If the time does change on every page refresh, either the cache is not working or has not been configured correctly.
  </vt:p>
  <vt:p><vt:b>Ensure your mariner-config.xml has page cache enabled, and the directory to main_jsp_dir\cache exists, and the url to hostname:port/main_jsp_dir/cache will resolve ok.</vt:b></vt:p> 
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_performance.jsp">Performance Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
<vt:h2>Caching Test</vt:h2>
<vt:p><%= message %></vt:p>
</vt:pane>
</vt:canvas>


