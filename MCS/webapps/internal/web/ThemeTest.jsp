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
<vt:canvas layoutName="e_portal" theme="mat">
<vt:p styleClass="warning" pane="welcome">Hello</vt:p>
<vt:p pane="logo1"><vt:b>Hi again </vt:b>
<vt:big>Big!</vt:big><vt:br/>
<vt:strong>Strong</vt:strong><vt:br/>
<vt:blockquote>BlockQuote</vt:blockquote><vt:br/>
<vt:code>code text</vt:code>
<vt:dl pane="logo1">
<vt:dt>Chicken</vt:dt>
<vt:dd>An animal that tastes nice</vt:dd>
<vt:dt>Cow</vt:dt>
</vt:dl>
</vt:p>
<vt:pane name="welcome">
<vt:table cols="1" styleClass="tablebackground">
<vt:th styleClass="thbackground">
<vt:td styleClass="tdbackground">
A table.
</vt:td>
</vt:th>
</vt:table>
</vt:pane>
</vt:canvas>
