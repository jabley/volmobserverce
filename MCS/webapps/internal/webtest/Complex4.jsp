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
<vt:canvas layoutName="Complex4">

<vt:p pane="laimage">
Some text in the local authority image. 
Should be the stars below.<vt:br/>
<vt:img src="stars"/>
</vt:p>


<vt:p pane="question3">
Some text in the question 3.
</vt:p>

<vt:p pane="question2">
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Mixed.jsp">Back</vt:a>
</vt:p>


<vt:p pane="question2">
Some text in the question 2.
</vt:p>

<vt:p pane="question1">
Some text in the question 1.
</vt:p>

<vt:p pane="globalheading">This is the g;obal heading, should be a table below.</vt:p>
<vt:table pane="globalheading" styleClass="mystyleclass" id="some_id"
title="The_table" cols="3">
<vt:tr>
<vt:td>Data</vt:td><vt:td>Data</vt:td><vt:td>Data</vt:td> </vt:tr>
<vt:tr>
<vt:td>Data</vt:td><vt:td>Data</vt:td><vt:td>Data</vt:td> </vt:tr>
<vt:tr>
<vt:td>Data</vt:td><vt:td>Data</vt:td><vt:td>Data</vt:td>
</vt:tr>
</vt:table>

<vt:p pane="bodyleft">Rollover text menu below in the bodyleft, oncolor green, offcolor blue.</vt:p>

<vt:menu pane="bodyleft" type="rollovertext"> 
<vt:menuitem text="bananas" href="some_bananas.html" onColor="green" offColor="blue"/> 
<vt:menuitem text="fajitas" href="some_fajitas.html" onColor="green" offColor="blue"/>  
<vt:menuitem text="burritos" href="some_burritos.html"  onColor="green" offColor="blue"/>  
</vt:menu>


<vt:menu pane="nav_bottom" type="plaintext">
<vt:p>A plaintext menu list below inside the nav_bottom pane</vt:p>
<vt:menuitem text="Choice1" href=""/>
<vt:menuitem text="Choice2" href=""/>
<vt:menuitem text="Choice3" href=""/>
<vt:menuitem text="Choice4" href=""/>
<vt:menuitem text="Choice5" href=""/>
</vt:menu>

<vt:p pane="debug">Debug link: <vt:a  href="http://www.debug.com">debug pane.</vt:a></vt:p> 

<vt:form pane="pay_pre" action="dosomething" method="POST" preamblePane="pay_pre" postamblePane="pay_post" 
submitText="SBMT" resetText="RST"><vt:p>This is a form.</vt:p>
<vt:textinput name="var1" entryPane="pay_em_imp" labelPane="pay_em_txt" type="text">Input em</vt:textinput>
<vt:textinput name="var2" entryPane="pay_pw_imp" labelPane="pay_pw_txt" type="text">Input pw</vt:textinput>
</vt:form>

<vt:p pane="con_of_pay">Inside the con_of_pay pane</vt:p>

</vt:canvas>

