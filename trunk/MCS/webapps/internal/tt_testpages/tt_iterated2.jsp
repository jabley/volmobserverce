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
<vt:canvas layoutName="Iterated2">

<vt:pane name="plain">

<vt:h3>Testing columns iterated panes</vt:h3>


<vt:p>
<vt:br/>
<vt:a href="index.jsp">Main Index</vt:a>
<vt:a href="tt_layouts.jsp">Layouts Index</vt:a>
<vt:br/>
</vt:p>

</vt:pane>


<%
for(int c=0; c < 4; c++)
    {
%>

<vt:p pane="iterated">There should be 4 columns </vt:p>

<%
    }

%>


</vt:canvas>

