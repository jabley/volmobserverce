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
<%@ include file="VolantisNoError-mcs.jsp" %>
<vt:canvas layoutName="welcome" >
<vt:p pane="logo">Native markup test follows. For HTML browsers, view the page source and check that the 
&lt;head&gt; element contains a &lt;BASE&gt; and &lt;LINK&gt; element which have been written through
native markup.</vt:p>
<vt:nativemarkup  expr="true()" targetLocation="html.head">
	<BASE HREF="http://www.somewhere.org/stuff/blob.html"/>
	<LINK HREF="http://www.somewhere.org/stuff/index.html" REL="index"/>
</vt:nativemarkup>
<vt:pane name="background">
        <vt:nativemarkup expr="true()" targetLocation="here">
            <em>Inside native markup (with an expr attribute)</em>
            <p>
                <em>Should appear on all protocols</em>
            </p>
        </vt:nativemarkup>
</vt:pane>
<vt:pane name="background">
        <vt:nativemarkup expr="false()" targetLocation="here">
            <em>Inside native markup (with an expr attribute)</em>
            <p>
                <em>This should not appear as the expr is false</em>
            </p>
        </vt:nativemarkup>
</vt:pane>
<vt:pane name="background">
        <vt:nativemarkup targetLocation="here">
            <em>Inside native markup (without an expr attribute)</em>
            <p>
                <em>You should see this only on WML protocols</em>
            </p>
        </vt:nativemarkup>
</vt:pane>
<vt:pane name="background">
        <vt:nativemarkup expr="true()" targetLocation="wml.card.beforebody">
            <setvar name="menuhref" value="This should appear on WML, other protocols should reject the targetLocation" />
        </vt:nativemarkup>
</vt:pane>
<vt:pane name="background">
        <vt:select expr="request:getParameter('name')">
            <vt:when expr="'Mat'">
                <vt:nativemarkup targetLocation="here">
                <vt:p>
                    Should appear on all protocols, as the nativemarkup
                    is inside a select
                </vt:p>
                </vt:nativemarkup>
            </vt:when>
            <vt:otherwise>
                <vt:nativemarkup targetLocation="here">
                <vt:p>
                    Please try again, specifying the parameter name=Mat
                </vt:p>
                </vt:nativemarkup>
            </vt:otherwise>
        </vt:select>
</vt:pane>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-03	2169/1	steve	VBM:2003120506 native markup html.head output patched from Proteus

 08-Dec-03	2164/1	steve	VBM:2003120506 html.head support

 ===========================================================================
--%>
