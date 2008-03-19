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
<vt:canvas layoutName="adamLayoutPc" theme = "adam">
<%// This part tests the column iterator part /%>
<vt:a pane="coliter1" href="www.www.www"> www.anchortag.test </vt:a>
<vt:p pane="coliter1"> this tests the break <vt:br/> tag to see if it <vt:br/> breaks </vt:p>
<vt:p pane="coliter1"><vt:cite>coliter 1 cite</vt:cite> </vt:p>
<vt:p pane="coliter1" ><vt:code>coliter 1 code</vt:code></vt:p>
<vt:p pane="coliter1" > <vt:em> emphasis </vt:em> </vt:p>
<vt:form pane="coliter1" method="post" action="addNewTagSubmit.jsp">
</vt:form>

<%// Test some rowiterations //%>
<vt:a pane="rowiter1" href="www.www.www"> www.anchortag.test </vt:a>
<vt:p pane="rowiter1"> this tests the break <vt:br/> tag to see if it <vt:br/> breaks </vt:p>
<vt:p pane="rowiter1"><vt:cite>coliter 1 cite</vt:cite> </vt:p>
<vt:p pane="rowiter1" ><vt:code>coliter 1 code</vt:code></vt:p>
<vt:p pane="rowiter1" > <vt:em> emphasis </vt:em> </vt:p>
<vt:form pane="rowiter1" method="post" action="addNewTagSubmit.jsp">
</vt:form>
<%// end here //%>
<vt:p pane="coliter1" > you got to <vt:big> big </vt:big> it up </vt:p>
<vt:p pane="coliter1" > sometimes you need to <vt:blockquote> blockquote </vt:blockquote> some text </vt:p>
<vt:p pane="coliter1"> but that is a very <vt:b> BOLD </vt:b> thing to do </vt:p>
<vt:h1 pane="coliter1" > Heading 1 </vt:h1>
<vt:h2 pane="coliter1" > Heading 2 </vt:h2>
<vt:h3 pane="coliter1" > Heading 3 </vt:h3>
<vt:h4 pane="coliter1" > Heading 4 </vt:h4>
<vt:h5 pane="coliter1" > Heading 5 </vt:h5>
<vt:h6 pane="coliter1" > Heading 6 </vt:h6>
<% // <vt:p pane="coliter1" ><vt:hr></vt:hr> </vt:p> %>
<%//<vt:img pane="coliter1" src="stars" alttext="no picture"// >%>
<vt:h3 pane="coliter1"><vt:i> Italic text </vt:i></vt:h3>
<vt:h4 pane ="coliter1"><vt:kbd> keyboard inline </vt:kbd></vt:h4>
<vt:ul pane="coliter1"> <vt:li> ul unordered item 1</vt:li><vt:li> ul second list item 1</vt:li></vt:ul>
<vt:ol pane ="coliter1"><vt:li> ol list item 1</vt:li><vt:li> ol list item 2</vt:li> </vt:ol>
<vt:logo pane="coliter1" src="stars" />
<% //DefinitionTerm, DefinitionList,DefinitionData /%>
<vt:dl pane="coliter1">
<vt:dt> Chicken </vt:dt>
<vt:dd> Tastes foul </vt:dd>
<vt:dt> Cow </vt:dt>
<vt:dd> Tastes nothing like chicken </vt:dd>
</vt:dl>
<%// FLASH Tag /%>
<vt:mmflash pane="coliter1"  
styleClass="flash_movies" id="movie1" name="snake" play="true"
loop="true" 
menu="true" 
altImg="volantis" 
altText="Hello Allan"/> 

<%// MenuTag and MenuItem /%>
<vt:menu pane="coliter1" type="rolloverimage" orientation="vertical">
<vt:menuitem text="menuitem" href="addImage.jsp" onImage="BDon0.gif" offImage="BDoff0.gif"/>
</vt:menu>

<%// DefinitionTerm, DefinitionList,DefinitionData /%>

<vt:p pane="coliter1"> <vt:tt> this should be a monospacefont </vt:tt></vt:p>
<vt:pre pane="coliter1"> pretext,
formats just
        like it is written </vt:pre>
<vt:h4 pane="coliter1"><vt:samp > sample text formatted in monospacefont</vt:samp></vt:h4>
<vt:p pane="coliter1"><vt:small> this is small text </vt:small></vt:p>
<vt:p pane="coliter1"><vt:strong> this is strong text </vt:strong></vt:p>
<vt:p pane="coliter1"><vt:sub> this is subscript text </vt:sub></vt:p>
<vt:p pane="coliter1"><vt:sup> this is superscript text </vt:sup></vt:p>
<vt:ul pane="coliter1"> <vt:li> another ul unordered item 1</vt:li><vt:li> ul second list item 1</vt:li></vt:ul> 
<vt:ol pane ="coliter1"><vt:li> another ol list item 1</vt:li><vt:li> ol list item 2</vt:li> </vt:ol> 

<%// Table tag /%>
<vt:table pane="coliter1" cols="2"> 
<vt:thead>
<vt:tr><vt:th>row Date </vt:th><vt:th> Start / Stop</vt:th> </vt:tr>
<vt:tr><vt:td> 02/25 </vt:td><vt:td> 9:30-10:45A</vt:td></vt:tr>
</vt:thead>
<vt:tbody>
<vt:tr><vt:th>h1 r2 </vt:th><vt:th> h2 r2 </vt:th> </vt:tr>
</vt:tbody>
<vt:tfoot>
<vt:tr><vt:td> data1 </vt:td><vt:td> data2 </vt:td></vt:tr>
</vt:tfoot>
</vt:table>

<%//<vt:p pane = "coliter1"> <vt:hr> horizontal rule tag </vt:hr> </vt:p> // %>
<vt:p pane = "coliter1"> <vt:u> underlined tag </vt:u></vt:p>
</vt:canvas>
