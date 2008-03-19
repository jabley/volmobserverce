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
 !   This page demonstrates conditional processing with what is essentially
 !   a sequence of unrelated "if" statements with a final "if none of those
 !   matched" statement.
 !
 !   The page can generate four variants depending on the definitions of the
 !   'name' and 'drink' URL parameters. If 'name' is set to 'Phil' a message
 !   is generated, possibly with a second message, depending on the setting of
 !   the 'drink' parameter.
 !
 !   If 'drink' is unset or set to a value other than 'diet coke' a message is
 !   generated.
 !
 !   If 'drink' is set to 'diet coke' and 'name' is unset or set to a value
 !   other than 'Phil' a separate message is generated.
 -->
<vt:canvas layoutName="error">
    <vt:pane name="error">
        <vt:select precept="matchevery">
            <vt:when expr="request:getParameter('name') = 'Phil'">
                <vt:p>
                    Hello Phil. Don't forget to clean inside that car
                    sometime!
                </vt:p>
            </vt:when>
            <vt:when expr="request:getParameter('drink') != 'diet coke'">
                <vt:p>
                    Had enough of your usual snort?
                </vt:p>
            </vt:when>
            <vt:otherwise>
                <vt:p>
                    Intruder Alert!
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
