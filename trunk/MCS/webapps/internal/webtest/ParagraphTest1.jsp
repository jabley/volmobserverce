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
 % $Header: /src/voyager/webapp/internal/webtest/ParagraphTest1.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 19-Sep-01    Paul            VBM:2001091013 - Created.
 % 02-Oct-01    Paul            VBM:2001100104 - Fixed page title
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>
<%@ page import="com.volantis.mcs.context.MarinerPageContext" %> 

<vt:canvas layoutName="ParagraphTest" theme="ParagraphTest"
           pageTitle="ParagraphTest1">

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:pane name="Pane1">
  Plain text outside paragraph.
</vt:pane>

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1">
  Plain text inside paragraph.
</vt:p>

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1">
  <vt:p>
    Plain text inside nested paragraph.
  </vt:p>
</vt:p>

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1">
  Plain text before nested paragraph.
  <vt:p>
    Plain text inside nested paragraph.
  </vt:p>
  Plain text after nested paragraph.
</vt:p>

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1">
  Plain text before nested paragraph.
  <vt:b>
    Bold text before nested paragraph.
    <vt:p>
      Bold text inside nested paragraph.
    </vt:p>
    Bold text after nested paragraph.
  </vt:b>
  Plain text after nested paragraph.
</vt:p>

</vt:canvas>
