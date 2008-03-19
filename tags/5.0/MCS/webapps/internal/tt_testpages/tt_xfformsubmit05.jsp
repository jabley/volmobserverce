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
<!--  *************************************************************************
	(c) Volantis Systems Ltd 2001. 
      *************************************************************************
	Revision Info 
	Name  	Date  		Comment
	MJ	15/11/2001	Created this file
      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<!--%@ page import="com.volantis.mcs.context.ContextInternals" %-->
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 
<!--%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %-->


<vt:canvas layoutName="tt_default">

<vt:pane name="header">
 <vt:p>	This page is not intended to be accessed directly.
	It is called from tt_xfform pages and returns confirmation
	of form selections.
 </vt:p>
 <vt:p>	The paper validation only checks the first paper parameter
	passed to the page. This could be looped to confirm all
	paper selections.
 </vt:p>
</vt:pane>

<vt:pane name="mobileheader">
 <vt:p>	The paper validation only identifies the first paper selected. 
 </vt:p>
</vt:pane>

<vt:pane name="voiceheader">
 <vt:p>	The paper validation only identifies the first paper selected. 
 </vt:p>
</vt:pane>

<vt:p pane="test">
<%
  String myhiddenfield = marinerRequestContext.getParameter ("myhiddenfield");
  String drink = marinerRequestContext.getParameter ("drink");
  String drink2 = marinerRequestContext.getParameter ("drink2");
  String drink3 = marinerRequestContext.getParameter ("drink3");
  String papers = marinerRequestContext.getParameter ("papers");
  if ("no".equals(marinerRequestContext.getParameter("papers")) ||
     " ".equals(marinerRequestContext.getParameter("papers"))) {
     papers="no";
     }
  Integer sugar;  
  if (marinerRequestContext.getParameter("sugar")!=null){
     sugar = Integer.valueOf (marinerRequestContext.getParameter ("sugar"));
     }
  else {
     sugar=Integer.valueOf ("0");
     }
  String pattern = new String("1st Field {0} <br/>2nd Field {1,choice,0#false|1#true} <br/>3rd Field {2} <br/>4th Field {3} and <br/>5th Field {4}");
  Object [] args = new Object [] {
    drink,
    sugar,
    papers,
    drink2,
    drink3
  };
  out.print (java.text.MessageFormat.format (pattern, args));
%>

  <vt:br/>
  <vt:p>
<%
  String action = "*** ERROR: Unable to identify which submit button was used. Check the parameters passed to this page! *** ";
  String v = "temp string";
  String value = "Unknown Value";
  if (( v = marinerRequestContext.getParameter ("submitbutton4")) != null) {
    action = "You used the button named 'submitbutton4' which has the value of ";
    value = v;
  } 
  if (( v = marinerRequestContext.getParameter ("submitbutton5")) != null) {
    action = "You used the button named 'submitbutton5' which has the value of ";
    value = v;
  } 
  if (( v = marinerRequestContext.getParameter ("submitimagebutton2")) != null) {
    action = "You used the image button named 'submitimagebutton2' which has the value of ";
    value = v;
  } 
  if (( v = marinerRequestContext.getParameter ("submitimagebutton3")) != null) {
    action = "You used the image button named 'imagebutton3' which has the value of ";
    value = v;
  } 
  if (( v = marinerRequestContext.getParameter ("submitbutton")) != null) {
    action = "You used a button named 'submitbutton' which has the value of ";
    value = v;
  } 
  out.print (action + "'" + value+ "'");
%>
  </vt:p>

 Goodbye.
</vt:p>


</vt:canvas>
