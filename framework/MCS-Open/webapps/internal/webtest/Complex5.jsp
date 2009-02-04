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
 % $Header: /src/voyager/webapp/internal/webtest/Complex5.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 08-Oct-01    Doug            VBM:2001100401 - Created this header and 
 %                              replaced all component name objects with 
 %                              mariner expressions
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>   

<vt:canvas layoutName="Complex5" >

<vt:p pane="preamble"><vt:h1>Please spare everybody's time and don't fill this form</vt:h1></vt:p>

<vt:form  pane="preamble" preamblePane="preamble" postamblePane="postamble" action="dosomething.jsp" submitText="Do NOT do It" resetText="Good thinking">
<vt:p>This should also be in the preamble pane of the form.</vt:p>

<vt:p pane="databasetypelabel">Search the database.</vt:p>

<vt:textarea accessKey="{red}" pane="databasetypeentry" name="trivial1"></vt:textarea>

<vt:p pane="sourcelabel">Source label.</vt:p>

<vt:textinput pane="sourceentry" name="trivial">Source entry: </vt:textinput>

<vt:p pane="userlabel">User label.</vt:p>

<vt:textinput accessKey="9" pane="userentry" name="username">Login username: </vt:textinput>

<vt:p pane="passwordlabel">Password label.</vt:p>

<vt:textinput pane="passwordentry" name="password" type="password">Please
enter your password: </vt:textinput>

<vt:p pane="portlabel">How did you dislocate your shoulder?</vt:p>

<vt:select pane="portentry" name="shoulder" label="Shoulder info" menuStyle="true" default="I do it for fun" multiple="false" styleClass="false">
<vt:option value="fun">I do it for fun</vt:option>
<vt:option value="paintballing">Playing paintball</vt:option>
<vt:option value="sneeze">I sneezed</vt:option>
<vt:option value="stretch">It happens when I stretch</vt:option>
</vt:select>

<vt:p pane="machinelabel">What music do you listen?</vt:p>

<vt:select pane="machineentry" name="music" menuStyle="false" label="Music info"  multiple="true" styleClass="false">
<vt:option value="grunge">Grunge</vt:option>
<vt:option value="hiphop">Hip-Hop</vt:option>
<vt:option value="trance">Techno-Trance-Industrial</vt:option>
<vt:option value="grindcore">Grind-core</vt:option>
<vt:option value="speed">Speed metal</vt:option>
</vt:select>

</vt:form>
<vt:p pane="postamble">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="AllForms.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:canvas>

