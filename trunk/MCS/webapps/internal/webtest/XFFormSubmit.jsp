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
 % $Header: /src/voyager/webapp/internal/webtest/XFFormSubmit.jsp,v 1.5 2002/11/28 10:23:17 geoff Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 14-Sep-01    Paul            VBM:2001091405 - Added this header and checked
 %                              the action which was used to submit the form.
 % 20-Sep-01    Paul            VBM:2001091202 - Added name to message
 %                              to test implicit values.
 % 30-Nov-01    Paul            VBM:2001112909 - Use new way of getting the
 %                              MarinerPageContext.
 % 07-Dec-01    Paul            VBM:2001120702 - Added import of
 %                              ContextInternals.
 % 19-Feb-02    Paul            VBM:2001100102 - Modified to use
 %                              marinerRequestContext to retrieve parameters.
 % 04-Mar-02    Paul            VBM:2001101803 - Removed unnecessary import.
 % 05-Apr-02    Ian             VBM:2002030606 - Additional tests. 
 % 18-Nov-02    Geoff           VBM:2002111504 - Avoid deprecated methods in 
 %                              page context
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.context.ContextInternals" %> 
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 
<%@ page import="com.volantis.mcs.components.TextComponentIdentity" %> 

<vt:canvas layoutName="XFFormSubmit">

<vt:p pane="Result">
<%
  String drink = marinerRequestContext.getParameter ("field1");
  Integer sugar = null;
  try {
    sugar = Integer.valueOf (marinerRequestContext.getParameter ("field2"));
  } catch (Exception ie) {
    sugar = new Integer(0);
  }
  String name = marinerRequestContext.getParameter ("name");

  MarinerPageContext context
    = ContextInternals.getMarinerPageContext (marinerRequestContext);

  TextComponentIdentity id = new TextComponentIdentity("submitPattern");
  String pattern = context.getTextFromTextAsset (id);

  Object [] args = new Object [] {
    name,
    drink,
    sugar
  };
  out.print (java.text.MessageFormat.format (pattern, args));
%>
<vt:br/>
<vt:p>
<%
  String action = "unknown action";
  String value = "unknown value";
  String v;
  String x="not set";
  String y="not set";
  if ((v = marinerRequestContext.getParameter ("action1")) != null) {
    x=marinerRequestContext.getParameter ("action1.x");
    y=marinerRequestContext.getParameter ("action1.x");
    action = "Action 1";
    value = v;
  } else if ((v = marinerRequestContext.getParameter ("action2")) != null) {
    x=marinerRequestContext.getParameter ("action2.x");
    y=marinerRequestContext.getParameter ("action2.x");
    action = "Action 2";
    value = v;
  } else if ((v = marinerRequestContext.getParameter ("action3")) != null) {
    x=marinerRequestContext.getParameter ("action3.x");
    y=marinerRequestContext.getParameter ("action3.x");
    action = "Action 3";
    value = v;
  } else if ((v = marinerRequestContext.getParameter ("action4")) != null) {
    action = "Action 4";
    value = v;
  } else if ((v = marinerRequestContext.getParameter ("action5")) != null) {
    action = "Action 5";
    value = v;
  } else if ((v = marinerRequestContext.getParameter ("submitbutton")) != null) {
    action = "Submit Button";
    value = v;
  }
  out.print ("You used '" + action + "' which has value '" + value + "'");
%>
<vt:br/>
<%
  out.print ("X="+x+" Y="+y);
%>
</vt:p>

Goodbye.
</vt:p>

</vt:canvas>
