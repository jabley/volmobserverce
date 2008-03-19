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
        AFP     15/11/2001      Created this file
        AFP     15/11/2002      Updated Links

      *************************************************************************
-->


<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas theme="tt_fonts" layoutName="standard" pageTitle="Themes: Fonts" >

<vt:pane name="main2">
<vt:p>
filename: tt_fonts.jsp<vt:br/>
theme: tt_fonts<vt:br/>
layout: standard<vt:br/>
Devices: All
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_font.jsp">Fonts</vt:a> =>
tt_fonts.jsp
</vt:p>

<vt:p>
All text should be of font-family Arial, Verdana, sans_serif (where supported).This is plain vt:p with no styleClass attribute and should be size small.</vt:p>
<vt:p styleClass="smallcaps">this font should be as vt:p but in smallcaps.</vt:p>
<vt:p styleClass="boldSmall">This font should be as vt:p but x-small and in bold</vt:p>
<vt:h1>This is h1. Should be large and bold.</vt:h1>
<vt:h2>This is h2. Should be medium and italic.</vt:h2>
<vt:a styleClass="anchor1">This is anchor text. font-family=Times</vt:a><vt:br/>

</vt:pane>

</vt:canvas>

