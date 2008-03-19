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

<vt:canvas theme="tt_overflow_2" layoutName="standard" pageTitle="Themes: Format" >

<vt:pane name="main2">
<vt:p>
filename: tt_overflow_2.jsp<vt:br/><vt:br/>
theme: tt_overflow_2<vt:br/>
layout: standard<vt:br/>
Devices: Sky Wap TV/CSS browsers
</vt:p>

<vt:p>
<vt:a href="index.jsp">Main Index</vt:a> =>
<vt:a href="tt_themes.jsp">Themes</vt:a> =>
<vt:a href="tt_themes_format.jsp">Format</vt:a> =>
tt_overflow_2.jsp
</vt:p>


<vt:p>This page tests the mariner "overflow" property. This can be found in the format Tab of the Mariner GUI in the style properties window. <vt:br/><vt:br/>
Test: canvas {overflow: visible}<vt:br/>
Expected Result: &lt; card scroll="false" &gt; <vt:br/>
</vt:p>

</vt:pane>

</vt:canvas>

