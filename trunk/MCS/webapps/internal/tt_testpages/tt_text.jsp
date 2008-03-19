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
	Name  	Date  		Comment
	MJ	15/11/2001	Created this file
	AFP     15/11/2002      Updated Links

      *************************************************************************
-->
<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_default" theme="tt_text">

<vt:pane name="fileinfo">
  <vt:h1>Theme Text Test</vt:h1>
  <vt:p>Filename: tt_text.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: tt_text
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to ensure theme text sytles work.</vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>Different styles of text should be displayed as defined by the theme.
  </vt:p>
</vt:pane>

<vt:pane name="links">
<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_text.jsp">Text</vt:a> =>
tt_text.jsp
</vt:p>
</vt:pane>

<vt:pane name="test">
<vt:p>This is plain vt:p with no styleClass attribute.</vt:p>
<vt:p styleClass="textalign">text align = center</vt:p>
<vt:p styleClass="textindent">text indent = 2em</vt:p>
<vt:p>This paragraph contains <vt:span styleClass="valign">some text</vt:span> that has vertical align = top</vt:p>
<vt:p styleClass="whitespace">whitespace = nowrap. This paragraph is not allowed to word wrap, so the only way
 to end a line is by using the br tag. This line will go on forever and you'll have to scroll to see it.</vt:p>
<vt:p styleClass="letterspacing">letter spacing = 0.5em</vt:p>
<vt:p styleClass="wordspacing">the word spacing in this line should be 1em</vt:p>
<vt:p styleClass="lineheight">line height = 5. Should be larger space above and below this paragraph.</vt:p>
<vt:p styleClass="texttransform">text transform = capitalize. Each word should start with capital letter.</vt:p>
<vt:p styleClass="textdecoration">text decoration = underline</vt:p>

</vt:pane>
</vt:canvas>
