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
<!-- Test version of the Volantis e-portal
-->
<%@ include file="Volantis-mcs.jsp" %> 
<vt:canvas layoutName="adamLayout">
<vt:a href="http://cod:8080/volantis/TestApplication/AdamTestPortal.jsp" pane="logo1" image="stars"/></vt:a>
<vt:logo pane="pane1" src="volantis" alt="volantis"/> 
<vt:welcome pane="welcome"/>
<vt:h2 pane="shop">Shop for Cool Stuff at <a href="shopcart.jsp">Shop Volantis</a></vt:h2>
<vt:HeadlineTag pane="headlines" show="2" />
<vt:AdTag pane="adverts"show="2" /> 
<vt:ContentTag pane="cpynews"/>
</vt:canvas>
