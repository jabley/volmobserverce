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
 % $Header: /src/voyager/webapp/internal/webtest/StyleCollision.jsp,v 1.2 2002/03/21 19:05:23 doug Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============   ===============================================
 % 08-Jan-02    Doug            VBM:2001121005 - Created
 % 21-Mar-02    Doug            VBM:2002011101 - Added an id & stylClass 
 %                              attribute to the xfform tag. 
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="StyleCollision" theme="StyleCollision">

<vt:h1 id="222" pane="left">
 Hello
</vt:h1>

<vt:xfform
        id="222"
        styleClass="test class"
        name="form"
        method="get"
        action="StyleCollision.jsp"
        prompt="Prompt Text">

<vt:xfsiselect
        active="true"
        name="animals"
        caption="Please choose a pet"
        captionPane="left"
        entryPane="left"
        prompt="Pick a pet">

	<vt:xfoption caption="dog" value="dog"/>
	<vt:xfoption caption="cat" value="cat"/>
        <vt:xfoption caption="lion" value="lion"/>
        <vt:xfoption caption="hippo" value="hippo"/>
</vt:xfsiselect>


<vt:xfmuselect
        active="true"
        name="countries"
        caption="Please choose a country"
        captionPane="left"
        entryPane="left"
        prompt="Pick a country">

	<vt:xfoption caption="Australia" value="OZ"/>
        <vt:xfoption caption="France" value="FR"/>
        <vt:xfoption caption="Ireland" value="IR"/>
	<vt:xfoption caption="United States" value="US"/>
        <vt:xfoption caption="United Kingdom" value="UK"/>
</vt:xfmuselect>


<vt:xfboolean
        name="field2"
        caption="Check This"
        captionPane="left"
        entryPane="left"
        help="Checkbox"
        prompt="aaaa"
        falseValue="ok"
        trueValue="no way"/>

<vt:xftextinput
        active="true"
        styleClass="greenMultiple"
        name="forname"
        caption="Please Enter a Forename"
        captionPane="left"
        entryPane="left"/>

<vt:xftextinput
        active="true"
        styleClass="greenMultiple"
        name="field5"
        caption="Please Enter a Surname"
        captionPane="left"
        entryPane="left"/>

<vt:xfaction type="submit"
	id="action1"
        caption="Submit 1"
        captionPane="left"
        entryPane="left"
        name="action1"/>

<vt:xfaction type="submit"			  
        id="action2"
        caption="Submit 2"
        captionPane="left"
        entryPane="left"
        name="action2"/>

<vt:xfimplicit name="name" value="Doug"/>




</vt:xfform>


</vt:canvas>
