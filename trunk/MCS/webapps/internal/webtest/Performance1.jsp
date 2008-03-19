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
<vt:canvas layoutName="error">

<vt:menu pane="error" type="plaintext">
<vt:menuitem text="Choice 1" href="http://www.volantis.com" />
<vt:menuitem text="Choice 2" href="http://www.volantis.com" />
<vt:menuitem text="Choice 3" href="http://www.volantis.com" />
<vt:menuitem text="Choice 4" href="http://www.volantis.com" />
<vt:menuitem text="Choice 5" href="http://www.volantis.com" />
<vt:menuitem text="Choice 6" href="http://www.volantis.com" />
</vt:menu>

<vt:menu pane="error" type="rollovertext">
<vt:menuitem text="Choice 1" href="http://www.volantis.com" onColor="red" offColor="green" />
<vt:menuitem text="Choice 2" href="http://www.volantis.com" onColor="red" offColor="green" />
<vt:menuitem text="Choice 3" href="http://www.volantis.com" onColor="red" offColor="green" />
<vt:menuitem text="Choice 4" href="http://www.volantis.com" onColor="red" offColor="green" />
<vt:menuitem text="Choice 5" href="http://www.volantis.com" onColor="red" offColor="green" />
<vt:menuitem text="Choice 6" href="http://www.volantis.com" onColor="red" offColor="green" />
</vt:menu>

<vt:menu pane="error" type="rolloverimage">
<vt:menuitem text="Choice 1" href="http://www.volantis.com" onImage="planets" offImage="cheetah" />
<vt:menuitem text="Choice 2" href="http://www.volantis.com" onImage="planets" offImage="cheetah" />
<vt:menuitem text="Choice 3" href="http://www.volantis.com" onImage="planets" offImage="cheetah" />
<vt:menuitem text="Choice 4" href="http://www.volantis.com" onImage="planets" offImage="cheetah" />
<vt:menuitem text="Choice 5" href="http://www.volantis.com" onImage="planets" offImage="cheetah" />
<vt:menuitem text="Choice 6" href="http://www.volantis.com" onImage="planets" offImage="cheetah" />
</vt:menu>

<vt:p pane="error">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Performance.jsp">Back</vt:a>
</vt:p>

</vt:canvas>
