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
        <vt:p title="%{request:getHeaders('accept')}">
            Look at the title of this paragraph to see the accept headers
        </vt:p>
        <vt:p title="%{request:getHeaders('unknown')}">
            Look at the title of this paragraph to see the unknown headers
        </vt:p>
        <vt:p title="%{request:getHeaders('unknown', 'header was unknown')}">
            Look at the title of this paragraph to see the unknown headers
            default
        </vt:p>
    </vt:pane>
</vt:canvas>
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Aug-03	928/1	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 ===========================================================================
--%>
