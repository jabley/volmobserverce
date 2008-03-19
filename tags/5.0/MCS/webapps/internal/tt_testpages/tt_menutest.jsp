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
<!--
*************************************************************************
(c) Volantis Systems Ltd 2004. 
*************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas layoutName="/standard.vly" theme="/standard.vth" pageTitle="Menu Testing">

<vt:pane name="title">
	<vt:img src="/stars.vic"/>
	<vt:img src="/volantis.vic"/>
</vt:pane>

<vt:pane name="submenu">
<vt:table styleClass="menu1" cols="6">
<vt:tr>
	<vt:td styleClass="menu1"><vt:a href="http://www.bbc.co.uk/" styleClass="menu1">BBC</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="http://www.cnn.com/" styleClass="menu1">CNN</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="http://www.usatoday.com/" styleClass="menu1">USAToday</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="http://www.telegraph.co.uk/" styleClass="menu1">The Daily Telegraph</vt:a></vt:td>
	<vt:td styleClass="menu1"><vt:a href="http://www.metro.co.uk/" styleClass="menu1">Metro</vt:a></vt:td>
</vt:tr>
</vt:table>
</vt:pane>

 <vt:pane name="menu2">
	<vt:a href="http://www.slashdot.com/" styleClass="menu2">Slashdot</vt:a><vt:br/>
	<vt:a href="http://www.theregister.co.uk/" styleClass="menu2">The Register</vt:a><vt:br/>
	<vt:a href="http://java.sun.com/" styleClass="menu2">Java Technology</vt:a><vt:br/>
	<vt:a href="http://www.javaworld.com/" styleClass="menu2">JavaWorld</vt:a><vt:br/>
	<vt:a href="http://ant.apache.org/" styleClass="menu2">Ant</vt:a><vt:br/>
	<vt:a href="http://www.junit.org/" styleClass="menu2">JUnit</vt:a><vt:br/>	
	<vt:a href="http://www.intellij.com/" styleClass="menu2">IntelliJ</vt:a><vt:br/>
	<vt:a href="http://www.lastminute.com/" styleClass="menu2">Lastminute.com</vt:a><vt:br/>
	<vt:a href="http://www.expedia.co.uk/" styleClass="menu2">Expedia</vt:a><vt:br/>
	<vt:a href="http://www.ebay.co.uk/" styleClass="menu2">E-Bay</vt:a><vt:br/>
</vt:pane>

<vt:pane name="info">
<vt:p>
	Filename: tt_menutest.jsp <vt:br/>
	Layout: /standard.vly <vt:br/>
	Theme: /standard.vth <vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="main">
<vt:p>
This is a menu test page.  It displays a couple of menus.  Really <vt:code>:-)</vt:code>
</vt:p>
</vt:pane>

</vt:canvas>




<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Feb-04	2948/1	claire	VBM:2004020901 Additional test pages

 ===========================================================================
--%>
