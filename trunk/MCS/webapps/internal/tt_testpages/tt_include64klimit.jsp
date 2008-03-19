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

<!-- This file is used to test that jsp:include works as expected on the 64k
     boundary
-->

<vt:canvas layoutName="tt_layout" pageTitle="Layout Properties">

<vt:pane name="pane1">
<vt:p>
filename: tt_layoutprops.jsp<vt:br/>
layout: tt_layout<vt:br/>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
</vt:p>
</vt:pane>
<vt:pane name="pane2">
<vt:p>
Pane 2. Background colour = grey.
</vt:p>
</vt:pane>

<jsp:include page="tt_includedfile.jsp"/>

<vt:pane name="pane5">
<vt:p>
<vt:a href="tt_layouts.jsp">Layouts Index</vt:a><vt:br/>
<vt:a href="index.jsp">Main Index</vt:a>
</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
<vt:p><vt:b>bold</vt:b></vt:p>
<vt:p>normal</vt:p>
</vt:pane>

</vt:canvas>
