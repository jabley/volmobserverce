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
<%@ page import="com.volantis.mcs.styles.*" %>
<%@ page import="com.volantis.mcs.context.*, com.volantis.mcs.repository.RepositoryConnection" %>

<vt:canvas layoutName="error" theme="Certify2">
<vt:pane name="error">

<vt:img src="volantis" altText="The alternate text"/>

<vt:ol>
<vt:li>List item 1</vt:li>
<vt:li>List item 2</vt:li>
<vt:li>List item 3</vt:li>
</vt:ol>

<vt:p>A sample text without any decoration.</vt:p>
<vt:p styleClass="spacing">The letter spacing should be 3 and the word spacing set to 5.</vt:p>
<vt:p styleClass="height">The line height should be 5.</vt:p>
<vt:p styleClass="align">The text alignement is set.</vt:p>
<vt:p styleClass="decoration">The text decoration is set to underline.</vt:p>
<vt:p styleClass="indent">The text indent is set to 10.</vt:p>
<vt:p styleClass="transform">the text tranform is set to uppercase.</vt:p>
<vt:p styleClass="space">The white space is set to pre     so, the white spaces  should remain intact.</vt:p>


<vt:table cols="2">
<vt:tr><vt:td id="11">Data 1 1</vt:td><vt:td id="12">Data 1 2</vt:td></vt:tr>
<vt:tr><vt:td id="21">Data 2 1</vt:td><vt:td id="22">Data 2 2</vt:td></vt:tr>
<vt:tr id="3"><vt:td>Row 3 with background color</vt:td><vt:td>Row with background color</vt:td></vt:tr>
<vt:tr id="4"><vt:td>Row 4 with backgroung image and repeat</vt:td><vt:td>Row with image and repeat</vt:td></vt:tr>
</vt:table>

<vt:p styleClass="family">The font-family of this paragraph is set</vt:p>
<vt:p styleClass="size">The size is set</vt:p>
<vt:p styleClass="style">The style is set</vt:p>
<vt:p styleClass="variant">The variant is set</vt:p>
<vt:p styleClass="weight">The weight is set</vt:p>

</vt:pane>

<vt:menu pane="error" type="plaintext">
<vt:menuitem text="Item 1 of the menu" href="Certify2.jsp"/>
<vt:menuitem text="Item 2 of the menu" href="Certify2.jsp"/>
</vt:menu>

<vt:pane name="error">

<vt:p>
<% 
MarinerPageContext mc = (MarinerPageContext)pageContext.getAttribute("volantis.context");
Theme th = mc.getTheme();
RepositoryConnection conn = volantis.getConnection();
out.println("<b>The stylesheet below was used for the formating of this page:</b><br/>");
out.println(th.outputStyleSheet(conn,volantis,request));
%>

<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Certification.jsp">Back</vt:a>

</vt:p>

</vt:pane>
</vt:canvas>

