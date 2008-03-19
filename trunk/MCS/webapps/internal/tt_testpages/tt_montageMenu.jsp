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
<vt:canvas layoutName="error" theme="tt_montage">

  <vt:pane name="error">
    <vt:p>
      <vt:a href="tt_layouts.jsp" segment="content">Layouts</vt:a>
    </vt:p>
    <vt:p>
      <vt:a href="tt_themes.jsp" segment="content">Themes</vt:a>
    </vt:p>
    <vt:p>
      <vt:a href="tt_devices.jsp" segment="content">Devices</vt:a>
    </vt:p>
  </vt:pane>

<vt:p pane="error">
This is side frame<vt:br/>
Should be 20% wide.<vt:br/>
and 85% high. <vt:br/>
</vt:p>

</vt:canvas>
