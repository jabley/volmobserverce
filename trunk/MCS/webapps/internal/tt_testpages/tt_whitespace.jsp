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
        AFP     31/10/02     	 Created this file

      *************************************************************************
-->


<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="tt_whitespace" layoutName="standard" pageTitle="Themes: Box" >

<vt:pane name="main2">
<vt:p>
filename: tt_whitespace.jsp<vt:br/><vt:br/>
theme: tt_whitespace<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_text.jsp">Text</vt:a> =>
tt_whitespace.jsp
</vt:p>
<vt:p>This page tests the whitespace properties. This can be found in the Text Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test:<vt:br/>
.whitespace1{background-color:#ccf;whitespace:5.0px}<vt:br/>
.whitespace2{background-color:#cf9;whitespace:1.0px 2.0px 3.0px 4.0px}<vt:br/>
.whitespace3{background-color:#fcf;whitespace-top:4.0px;whitespace-bottom:7.0px}<vt:br/>
<vt:br/>
Expected Results: <vt:br/>
css: as above, except canvas => .VE-canvas<vt:br/>
Wap TV:<vt:br/>
whitespace1 on td tag => <vt:br/>
whitespace2 on td tag => <vt:br/>
whitespace3 on td tag => <vt:br/>
</vt:p>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="whitespace1">
This is within a table cell which has styleClass=whitespace1.

S  P  A  C  I  N  G.
Qualcomm controls 3G standards for mobile phones. 

The US communications company Qualcomm has reported soaring sales during its most recent quarter.The company's net income came in at $190m for the three months ending on 29 September, beating Wall Street estimates.We're clearly pleased here Gerard Klauer Mattison. This also compares with a loss of $75m in the same period last year.

Qualcomm is best known for developing Code Division Multiple Access (CDMA) technology, used in most US mobile phones.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="whitespace2">
This is within a table cell which has styleClass=whitespace2.

Qualcomm controls 3G standards for mobile phones. 

The US communications company Qualcomm has reported soaring sales during its most recent quarter.The company's net income came in at $190m for the three months ending on 29 September, beating Wall Street estimates.We're clearly pleased here Gerard Klauer Mattison. This also compares with a loss of $75m in the same period last year.

Qualcomm is best known for developing Code Division Multiple Access (CDMA) technology, used in most US mobile phones.
</vt:td>
</vt:tr>
</vt:table>

<vt:br/>

<vt:table cols="1">
<vt:tr>
<vt:td styleClass="whitespace3">
This is within a table cell which has styleClass=whitespace3.

Qualcomm controls 3G standards for mobile phones. 

The US communications company Qualcomm has reported soaring sales during its most recent quarter.The company's net income came in at $190m for the three months ending on 29 September, beating Wall Street estimates.We're clearly pleased here Gerard Klauer Mattison. This also compares with a loss of $75m in the same period last year.

Qualcomm is best known for developing Code Division Multiple Access (CDMA) technology, used in most US mobile phones.
</vt:td>
</vt:tr>
</vt:table>

<vt:p styleClass="whitespace1">
This is within a paragraph which has styleClass=whitespace1.
S  P  A  C  I  N  G
Qualcomm controls 3G standards for mobile phones.

The US communications company Qualcomm has reported soaring sales during its most recent quarter.The company's net income came in at $190m for the three months ending on 29 September, beating Wall Street estimates.We're clearly pleased here Gerard Klauer Mattison. This also compares with a loss of $75m in the same period last year.

Qualcomm is best known for developing Code Division Multiple Access (CDMA) technology, used in most US mobile phones.
</vt:p>

<vt:p styleClass="whitespace2">
This is within a paragraph which has styleClass=whitespace2.
S  P  A  C  I  N  G
Qualcomm controls 3G standards for mobile phones.

The US communications company Qualcomm has reported soaring sales during its most recent quarter.The company's net income came in at $190m for the three months ending on 29 September, beating Wall Street estimates.We're clearly pleased here Gerard Klauer Mattison. This also compares with a loss of $75m in the same period last year.

Qualcomm is best known for developing Code Division Multiple Access (CDMA) technology, used in most US mobile phones.
</vt:p>

<vt:p styleClass="whitespace3">
This is within a paragraph which has styleClass=whitespace3.
S  P  A  C  I  N  G
Qualcomm controls 3G standards for mobile phones.

The US communications company Qualcomm has reported soaring sales during its most recent quarter.The company's net income came in at $190m for the three months ending on 29 September, beating Wall Street estimates.We're clearly pleased here Gerard Klauer Mattison. This also compares with a loss of $75m in the same period last year.

Qualcomm is best known for developing Code Division Multiple Access (CDMA) technology, used in most US mobile phones.
</vt:p>


<vt:br/>
<vt:a href="tt_themes.jsp">Themes Index</vt:a><vt:br/>
<vt:a href="index.jsp">Main Index</vt:a>
</vt:pane>

</vt:canvas>

