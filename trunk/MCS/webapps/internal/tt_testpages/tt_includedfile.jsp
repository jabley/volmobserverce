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

<!-- This files is used by tt_include64klimit.jsp and
     tt_includesmall.jsp and is used as part of the
     tests for jsp:include, including working around
     the 64k limit.

-->

<vt:pane name="pane4">
<vt:p>
Include has worked!!
Pane 4.<vt:br/> 
Border width=3,<vt:br/> 
cell padding = 5,<vt:br/> 
cell spacing=5,<vt:br/> 
horiz align=right.
</vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
</vt:pane>
