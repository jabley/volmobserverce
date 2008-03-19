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

<!--
 !   This page verifies that a duff expression generates a PAPIException.
 -->
<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:select expr="this is an illegal expression">
            <vt:otherwise>
                <vt:p>
                    Should not be displayed.
                </vt:p>
            </vt:otherwise>
        </vt:select>
    </vt:pane>
</vt:canvas>
<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Aug-03	1008/1	philws	VBM:2003080805 Provide implementation of the select, when and otherwise PAPI elements

 ===========================================================================
--%>
