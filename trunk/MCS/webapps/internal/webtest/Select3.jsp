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
 !   This page demonstrates the "if...else if...else" construct variant of the
 !   select statement. This is very similar, in this case, to the "switch"
 !   statement variant, but only because the same parameter is tested each
 !   time.
 !
 !   This test page can generate four variants depending on the definition
 !   of the name URL parameter.
 -->
<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:select precept="matchfirst">
            <vt:when expr="request:getParameter('name') = 'Phil'">
                <vt:p>
                    Hello Phil. Don't forget to clean inside that car
                    sometime!
                </vt:p>
            </vt:when>
            <vt:when expr="request:getParameter('name') = 'Doug'">
                <vt:p>
                    Ho Doug. How's that swing?
                </vt:p>
            </vt:when>
            <vt:when expr="request:getParameter('name') = 'Byron'">
                <vt:p>
                    Oh man! Give me some sleep!
                </vt:p>
            </vt:when>
            <vt:otherwise>
                <vt:p>
                    Please try again and supply a name parameter on the URL
                    from the set:
                    <vt:ul>
                        <vt:li>Phil</vt:li>
                        <vt:li>Doug</vt:li>
                        <vt:li>Byron</vt:li>
                    </vt:ul>
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
