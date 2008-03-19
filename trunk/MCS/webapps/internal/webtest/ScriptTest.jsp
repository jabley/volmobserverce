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
<%-- ==========================================================================
 % $Header: /src/voyager/webapp/internal/webtest/ScriptTest.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package and updated to use new
 %                              script components.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="IndexLayout" onload="{OnAny}" onunload="{OnAny}">

<vt:p pane="index">
The standard first program written in a new programming language.<vt:br/>
</vt:p>
<vt:p pane="index">
<vt:script src="{HelloWorld}"/>
</vt:p>

<vt:p pane="index">
Move over this text and press the mouse to test the mouse events.
</vt:p>

<vt:p pane="index">
<vt:a href="nowhere"
      onclick="{OnAny}"
      ondblclick="{OnAny}"
      onkeydown="{OnAny}"
      onkeypress="{OnAny}"
      onkeyup="{OnAny}"
      onmousedown="{OnAny}"
      onmousemove="{OnAny}"
      onmouseout="{OnMouseOut}"
      onmouseover="{OnMouseOver}"
      onmouseup="{OnAny}">
<vt:img id="stars" src="stars"/>
</vt:a>
</vt:p>

</vt:canvas>
