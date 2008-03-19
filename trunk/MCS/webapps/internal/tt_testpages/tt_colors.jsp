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

<vt:canvas theme="tt_colors" layoutName="standard" pageTitle="Themes: Colors" >

<vt:pane name="main2">
<vt:p>
filename: tt_colors.jsp<vt:br/>
theme: tt_colors<vt:br/>
layout: standard<vt:br/>
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_format.jsp">Format</vt:a> =>
tt_colors.jsp
</vt:p>


<vt:span styleClass="fontRed">
This is within span tags. text should be red.
</vt:span>


<vt:p styleClass="green">This uses the paragraph tag, and should be green.</vt:p>
<vt:p styleClass="navy">This uses the paragraph tag, and should be navy.</vt:p>
<vt:h1 styleClass="red">This is h1. Should be red.</vt:h1>
<vt:h2 styleClass="blue">This is h2. Should be blue.</vt:h2>
<vt:h3>This is h3. And should be yellow.</vt:h3>
</vt:pane>

</vt:canvas>

