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
<vt:canvas layoutName="Complex1">

<vt:p pane="headlines">The headlines pane. Should be a menu below.</vt:p>
<vt:menu pane="headlines" type ="required">
<vt:menuitem text="Item 1" href="someref1.jsp"/>
<vt:menuitem text="Item 2" href="someref2.jsp"/>
<vt:menuitem text="Item 3" href="someref3.jsp"/>
<vt:menuitem text="Item 4" href="someref4.jsp"/>
<vt:menuitem text="Item 5" href="someref5.jsp"/>
<vt:menuitem text="Item 6" href="someref6.jsp"/>
</vt:menu>


<vt:pane name="logo1">
<vt:p>The logo 1. Should be an ordered list below.</vt:p>
<vt:ol>
<vt:li>Item 1 inside the ordered list</vt:li>
<vt:li>Item 2 inside the ordered list</vt:li>
<vt:li>Item 3 inside the ordered list</vt:li>
<vt:li>Item 4 inside the ordered list</vt:li>
<vt:li>Item 5 inside the ordered list</vt:li>
</vt:ol>
</vt:pane>

<vt:pane name="logo2">
<vt:p>The logo 2. Should be an unordered list below.</vt:p>
<vt:ul>
<vt:li>Item 1 inside the unordered list</vt:li>
<vt:li>Item 2 inside the unordered list</vt:li>
<vt:li>Item 3 inside the unordered list</vt:li>
<vt:li>Item 4 inside the unordered list</vt:li>
</vt:ul>
</vt:pane>

<vt:pane name="welcome">
<vt:p>The welcome pane. Should be a definition list below.</vt:p>
<vt:dl>
<vt:dt>Term 1</vt:dt><vt:dd>Definition 1 inside the definition list</vt:dd>
<vt:dt>Term 2</vt:dt><vt:dd>Definition 2 inside the definition list</vt:dd>
<vt:dt>Term 3</vt:dt><vt:dd>Definition 3 inside the definition list</vt:dd>
<vt:dt>Term 4</vt:dt><vt:dd>Definition 4 inside the definition list</vt:dd>
</vt:dl>
</vt:pane>

<vt:pane name="shop">
<vt:p>This is the shop pane. Should be a form below.</vt:p>
<vt:form  action="Somepage.jsp" submitText="Click to continue" preamblePane="adverts" postamblePane="cpynews">
<vt:textinput pane="adverts"  name="Something" size="5" maxLength="5">Enter input, should <vt:br/>be in the adverts pane.</vt:textinput> 
</vt:form>
</vt:pane>

<vt:pane name="adverts">
<vt:p>The adverts pane. Various kinds of text below.</vt:p>
<vt:p><vt:br/><vt:a href="somelink.html">A link somewhere</vt:a></vt:p>
<vt:p><vt:br/><vt:va href="nowhere.jsp">A link nowhere</vt:va></vt:p>
<vt:p><vt:br/><vt:b>Bold text</vt:b></vt:p>
<vt:p><vt:br/><vt:sub>Subscript text</vt:sub></vt:p>
<vt:p><vt:br/><vt:sup>Some superscript text</vt:sup></vt:p>
<vt:p><vt:br/><vt:strong>Strong text</vt:strong></vt:p>
<vt:p><vt:br/><vt:code>Text as programming code.</vt:code></vt:p>
<vt:p><vt:br/><vt:tt>Monospace text.</vt:tt></vt:p>
<vt:p><vt:br/><vt:small>Text in small font.</vt:small></vt:p>
<vt:p><vt:br/><vt:u>Underlined text.</vt:u></vt:p>
<vt:p><vt:br/><vt:i>Italics text.</vt:i></vt:p>
<vt:p><vt:br/><vt:cite>Citation text.</vt:cite></vt:p>
<vt:p><vt:br/><vt:big>Big text.</vt:big></vt:p>
<vt:p><vt:br/><vt:em>Emphasised text.</vt:em></vt:p>
<vt:p><vt:br/><vt:kbd>Keyboard text.</vt:kbd></vt:p>
<vt:p><vt:br/><vt:samp>Sample text.</vt:samp></vt:p>

<vt:p>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>

</vt:pane>

<vt:p pane="cpynews">Should be in the cpynews pane, with the postamble from the form.</vt:p>

<vt:pane name="script">

<vt:onPaneActive>
<%! String boo = "BOO!"; %>
</vt:onPaneActive>

<vt:p>
The boo string is <%= boo %>
</vt:p>
</vt:pane>

</vt:canvas>


