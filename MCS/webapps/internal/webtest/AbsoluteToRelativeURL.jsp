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

<%-- This test page demonstrates the Absolute to Relative URL conversion by
    including an XML file containing the conversion pipeline markup and some
    nested MAML XML --%>
<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:include href="AbsoluteToRelativeURL.xml"/>
    </vt:pane>
</vt:canvas>
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Aug-03	1035/1	byron	VBM:2003080802 Provide ConvertElementCase pipeline process

 08-Aug-03	1001/1	byron	VBM:2003080507 Provide ConvertAbsoluteToRelativeURL pipeline process

 ===========================================================================
--%>
