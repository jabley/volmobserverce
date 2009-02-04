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
<vt:canvas 	layoutName="openwave_dissected_menu"
		theme="openwavemenu">

<vt:pane name="fileinfo">
  <vt:h1>Plain Text Menu Test</vt:h1>
  <vt:p>Filename: tt_menu1.jsp<vt:br/>
  	Layout: tt_iterator<vt:br/>
	Theme: tt_orientation
  </vt:p>
</vt:pane>

<vt:menu id="numeric-shortcut" pane="fileinfo" type="plaintext" styleClass="orienthoriz">
    <vt:menuitemgroup>
	    <vt:menuitem href="jill.html" text="Jill"/>
	    <vt:menuitem href="bob.html" text="Bob"/>
	    <vt:menuitem href="fred.html" text="Fred"/>
        <vt:menuitem href="jane.html" text="Jane"/>
	    <vt:menuitem href="claire.html" text="Claire"/>
	    <vt:menuitem href="sue.html" text="Sue"/>
    </vt:menuitemgroup>
    <vt:menuitem href="colin.html" text="Colin"/>
	<vt:menuitem href="brian.html" text="Brian"/>
	<vt:menuitem href="mandy.html" text="Mandy"/>
    <vt:menuitemgroup>
        <vt:menuitem href="bill.html" text="Bill"/>
	    <vt:menuitem href="alex.html" text="Alex"/>
    </vt:menuitemgroup>        
	<vt:menuitem href="ryan.html" text="Ryan"/>
    <vt:menuitem href="hilda.html" text="Hilda"/>
	<vt:menuitem href="nancy.html" text="Nancy"/>
	<vt:menuitem href="tom.html" text="Tom"/>
    <vt:menuitem href="tammy.html" text="Tammy"/>
	<vt:menuitem href="debbie.html" text="Debbie"/>
	<vt:menuitem href="bruce.html" text="Bruce"/>
</vt:menu>


<vt:menu pane="fileinfo" type="plaintext" styleClass="orienthoriz">
	<vt:menuitem href="this.html" text="HorizThis"/>
	<vt:menuitem href="that.html" text="HorizThat"/>
	<vt:menuitem href="other.html" text="HorizOther"/>
</vt:menu>
  
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 26-Sep-03	1423/1	doug	VBM:2003091701 try and commit so geoff can have his own wspace

 ===========================================================================
--%>
