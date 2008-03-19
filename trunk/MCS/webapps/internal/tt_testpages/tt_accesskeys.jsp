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
<!--%@ page import="com.volantis.mcs.pagehelpers.*" %-->
<vt:canvas layoutName="iDTV1" theme="iDTV">
<vt:pane name="main">
<vt:h2>TV Soft Keys Test Page</vt:h2>
<vt:p>
	filename: tt_accesskeys.jsp<vt:br/>
	purpose: soft keys, key mappings<vt:br/>
	devices: TV, mobile<vt:br/>
	layout: iDTV1<vt:br/>
	theme: iDTV<vt:br/>
</vt:p>

<vt:p>

<vt:a accessKey="{green}" href="index.jsp">
Green Button => Main Test Page index</vt:a><vt:br/>

<vt:a accessKey="{yellow}" href="welcome.jsp">
Yellow Button => Welcome Page</vt:a><vt:br/>

<vt:a accessKey="{blue}" href="TVindex.jsp">
Blue Button => TV Test Pages Index</vt:a><vt:br/>

</vt:p>
</vt:pane>
</vt:canvas>


