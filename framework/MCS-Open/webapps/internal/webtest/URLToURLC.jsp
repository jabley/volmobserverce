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

<%-- This test page demonstrates the URL to URLC conversion by including an
     XML file containing the conversion pipeline markup and some nested
     MAML XML --%>
<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:include href="URLToURLC.xml"/>
    </vt:pane>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Aug-03	981/1	philws	VBM:2003080605 Provide Transforce PictureIQ specific pipeline URL to URLC conversion

 ===========================================================================
--%>
