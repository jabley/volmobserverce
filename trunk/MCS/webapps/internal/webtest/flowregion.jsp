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
<%-- ==========================================================================
 % (c) Volantis Systems Ltd 2003. 
 % ======================================================================= --%>

<%@ include file="VolantisNoError-mcs.jsp" %>

<vt:canvas layoutName="flowregion" pageTitle="Region Flow Test" type="portal">

<vt:p pane="Pane1" >
  <vt:img src="stars" />
</vt:p>

<vt:p pane="Pane2">
  This test tests flow elements embedded directly within regions. The elements do not require Panes or Canvases to function correctly.
</vt:p>

<vt:region name="Region1">
  <vt:pre>
      This is    pre-formatted text.
  </vt:pre>
  <vt:phonenumber fullNumber="1234-5678">Call Me: 1234-5678</vt:phonenumber>
  <vt:br />
  <vt:img src="volantis" />
  <vt:p> <vt:i> Italic </vt:i> <vt:b> Bold </vt:b> <vt:big> Big </vt:big> <vt:small> Small </vt:small> <vt:em> Emphasized </vt:em> </vt:p>
</vt:region>

<vt:region name="Region2">
  No Paragraph
  <vt:p>Text    in  a    paragraph.  Whitespace   handling   should  work.</vt:p>
  <vt:hr/>
  <vt:h1>H1</vt:h1>
  <vt:h2>H2</vt:h2>
  <vt:h3>H3</vt:h3>
  <vt:h4>H4</vt:h4>
  <vt:h5>H5</vt:h5>
  <vt:h6>H6</vt:h6>
  <vt:div>Div Element</vt:div>
  <vt:ul>
  <vt:li>List 1</vt:li>
  <vt:li>List 2</vt:li>
  <vt:li>List 3</vt:li>
  <vt:li>List 4</vt:li>
  </vt:ul>
  <vt:ol>
  <vt:li>List 1</vt:li>
  <vt:li>List 2</vt:li>
  <vt:li>List 3</vt:li>
  <vt:li>List 4</vt:li>
  </vt:ol>
</vt:region>

</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jun-03	407/1	steve	VBM:2002121215 jsp test file

 ===========================================================================
--%>
