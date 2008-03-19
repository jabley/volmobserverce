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

<vt:canvas layoutName="Test1" theme="Theme1" pageTitle="Test1 test page">  

<vt:p pane="pane1" styleClass="danger" id="p1" title="p1title">
A simple paragraph.
<vt:p styleClass="envy">A nested paragraph</vt:p>
Continued text from the nesting paragraph.
<vt:p id="p3">Second nested, with special id.</vt:p>
Continued text from the nesting paragraph.
</vt:p>

<vt:p pane="pane1">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>

</vt:canvas>
