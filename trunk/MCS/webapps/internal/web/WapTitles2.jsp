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
<vt:canvas layoutName="Banner" theme="test">
<vt:pane name="main">
<vt:img pane="banner" src="volantis"/>
<vt:p>
Above this text there should be a banner image.
Here is a table with titles on the row and column:
<vt:table cols="1">
<vt:tr title = "Table Row Title">
<vt:td title = "Table Data Title">
Here is a link with a title:
</vt:td>
</vt:tr>
</vt:table>
</vt:p>
</vt:pane>
</vt:canvas>

