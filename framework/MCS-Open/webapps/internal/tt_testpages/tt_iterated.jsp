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
<vt:canvas layoutName="tt_Iterated">



<vt:pane name="plain">
<vt:h2>Row Iterator Panes</vt:h2>
<vt:p>
filename: tt_iterated.jsp<vt:br/>
layout: tt_Iterated<vt:br/>
</vt:p>
<vt:a href="index.jsp">Main Index</vt:a>
<vt:a href="tt_layouts.jsp">Layouts Index</vt:a>
<vt:br/>
</vt:pane>


<vt:pane name="iterated">
<%
for (int c=0; c < 5 ; c++)
    {
%>
<vt:p>This line should be repeated 5 times</vt:p>
<%
    }
%>
</vt:pane>

</vt:canvas>
