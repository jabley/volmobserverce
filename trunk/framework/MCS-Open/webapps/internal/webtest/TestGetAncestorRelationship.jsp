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
<!--
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 -->
<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:p title="%{device:getAncestorRelationship('Master')}">
            Look at the title of this paragraph to see your devices 
            Relationship to the Master Device.
        </vt:p>
	<vt:p title="%{device:getAncestorRelationship('Yet To Be Invented')}">
            Look at the title of this paragraph to see your devices 
            Relationship to the 'Yet To Be Invented' Device.
        </vt:p>
    </vt:pane>
</vt:canvas>
