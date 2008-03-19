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
 % $Header: /src/voyager/webapp/internal/webtest/papi/AnchorTest1.jsp,v 1.1 2001/12/27 15:55:02 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 14-Oct-01    Paul            VBM:2001111402 - Created
 % 19-Nov-01    Paul            VBM:2001110202 - Removed references to
 %                              MarinerPageContext.
 % 07-Dec-01    Pether          VBM:2001111402 - Corrected.
 % ======================================================================= --%>

<%-- ==========================================================================
 % This page tests the anchor element works inside a paragraph.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<%@ page import="com.volantis.mcs.context.ContextInternals" %> 
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 
<%@ page import="com.volantis.mcs.papi.*" %> 

<%@ page import="com.volantis.mcs.jsp.MarinerJspRequestContext" %> 

<vt:canvas layoutName="PAPITest" 
           pageTitle="PAPI Anchor Test 1">

<%
  // Get the current JSP PAPI Page Context.
  MarinerJspRequestContext ppc = MarinerJspRequestContext.getCurrent (pageContext);

  int result;

  ParagraphAttributes paragraphAttributes = new ParagraphAttributes ();
  paragraphAttributes.setPane ("Pane1");

  ParagraphElement paragraphElement = new ParagraphElement ();
  result = paragraphElement.elementStart (ppc, paragraphAttributes);
  if (result == PAPIElement.PROCESS_ELEMENT_BODY) {

    paragraphElement.elementContent (ppc, paragraphAttributes,
                                     "Text in paragraph 1 before anchor. ");

    AnchorAttributes anchorAttributes = new AnchorAttributes ();
    anchorAttributes.setHref ("me.html");

    AnchorElement anchorElement = new AnchorElement ();
    anchorElement.elementStart (ppc, anchorAttributes);

    anchorElement.elementContent (ppc, anchorAttributes,
                                  "Text inside anchor before bold. ");

    BoldAttributes boldAttributes = new BoldAttributes ();
    
    AbstractPAPIDelegatingElement boldElement = new BoldElement ();
    boldElement.elementStart (ppc, boldAttributes);
    boldElement.elementContent (ppc, boldAttributes, "Bold text. ");
    boldElement.elementEnd (ppc, boldAttributes);
    boldElement.elementReset (ppc);

    ItalicAttributes italicAttributes = new ItalicAttributes ();

    ItalicElement italicElements = new ItalicElement ();
    italicElements.elementStart (ppc, italicAttributes);
    italicElements.elementContent (ppc, italicAttributes, "Italic text. ");
    italicElements.elementEnd (ppc, italicAttributes);
    italicElements.elementReset (ppc);

    anchorElement.elementEnd (ppc, anchorAttributes);

  } else {
    VolantisEnvironment.log ("Skipped body for some reason");
  }

  paragraphElement.elementEnd (ppc, paragraphAttributes);
%>

</vt:canvas>

<%--
 % Local variables:
 % mode: java
 % End:
 --%>
