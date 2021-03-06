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
 % $Header: /src/voyager/webapp/internal/webtest/ParagraphTests.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 02-Oct-01    Paul            VBM:2001100503 - Created to link paragraph
 %                              test pages.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 

<vt:canvas layoutName="ParagraphTest" theme="ParagraphTest"
           pageTitle="ParagraphTests">

<vt:menu pane="Pane1" type="plaintext">
  <vt:menuitem href="ParagraphTest1.jsp" text="Paragraph Test 1"/>
  <vt:menuitem href="ParagraphTest2.jsp" text="Paragraph Test 2"/>
  <vt:menuitem href="ParagraphTest3.jsp" text="Paragraph Test 3"/>
  <vt:menuitem href="ParagraphTest4.jsp" text="Paragraph Test 4"/>
</vt:menu>

</vt:canvas>
