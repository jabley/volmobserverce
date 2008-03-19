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
<vt:canvas layoutName="K_CurrentTest">
<vt:pane name="test1">
<% String access5 = new String("5"); %>
<vt:a accessKey="<%=access5%>" href="http://www.sun.com"> 5 go to sun</vt:a>
<% String access8 = new String("8"); %>
<vt:a accessKey="<%=access8%>" href="http://www.google.com"> 8 go to google</vt:a>
<% String access1 = new String("1"); %> 
<vt:a accessKey="<%=access1%>" href="http://swordfish:8080/volantis/index.jsp"> 1 volantis</vt:a>
</vt:pane>
</vt:canvas>
