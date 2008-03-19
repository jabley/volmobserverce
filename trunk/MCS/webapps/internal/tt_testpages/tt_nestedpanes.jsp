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
<vt:canvas layoutName="tt_nestedpanes" pageTitle="Nested Panes">

<vt:pane name="pane1">
<vt:p>
P1- top left<vt:br/>
filename: tt_nestedpanes.jsp<vt:br/>
layout: tt_nestedpanes<vt:br/>
5 panes, 2 in l col, and 3 in r col.<vt:br/>
</vt:p>
</vt:pane>

<vt:pane name="pane2">
<vt:p>
P2 - bottom left
</vt:p>
</vt:pane>

<vt:pane name="pane3">
<vt:p>
P3 - top right
</vt:p>
</vt:pane>

<vt:pane name="pane4">
<vt:p>
P4 - middle right</vt:p>
<vt:a href="tt_layouts.jsp">Layouts Index</vt:a><vt:br/>
<vt:a href="index.jsp">Main Index</vt:a>
</vt:pane>

<vt:pane name="pane5">
<vt:p>
P5 - bottom right</vt:p>
</vt:pane>

</vt:canvas>



