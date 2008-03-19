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

<vt:canvas layoutName="tt_xfform" theme="XFFormTest2"
      onload="{statusOnLoad}"
      onunload="{OnAny}"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">

<vt:p pane="fileinfo">

This is to test event handlers such as onMouseOver, onKeyPress, etc.
</vt:p>

</vt:canvas>
