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
 % $Header: /src/voyager/webapp/internal/webtest/ParagraphTest2.jsp,v 1.1 2001/12/27 15:55:01 jason Exp $
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
           pageTitle="ParagraphTest2">

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1" styleClass="alignRight">
  Right aligned text before nested paragraph.
  <vt:p>
    Right aligned text inside nested paragraph.
  </vt:p>
  Right aligned text after nested paragraph.
</vt:p>

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1" styleClass="alignRight">
  Right aligned text before nested paragraph.
  <vt:p styleClass="alignCenter">
    Center aligned text inside nested paragraph.
    <vt:p styleClass="alignLeft">
      Left aligned text after nested paragraph.
    </vt:p>
  </vt:p>
</vt:p>

<vt:p pane="Pane1" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Pane1">
  Wrapped text before nested paragraph, this is long enough to wrap
  on most devices.
  <vt:p styleClass="noWrap">
    Unwrapped text inside first nested paragraph, this is long enough to wrap
    on most devices.
  </vt:p>
  <vt:p>
    Wrapped text inside second nested paragraph, this is long enough to wrap
    on most devices..
  </vt:p>
  <vt:p styleClass="noWrap">
    Unwrapped text inside third nested paragraph, this is long enough to wrap
    on most devices.
  </vt:p>
  Wrapped text after nested paragraph, this is long enough to wrap
  on most devices.
</vt:p>

<vt:p pane="Dissect1" styleClass="alignCenter">==========</vt:p>

<vt:pane name="Dissect1">
  Plain text outside paragraph for dissecting.
</vt:pane>

<vt:p pane="Dissect2" styleClass="alignCenter">==========</vt:p>

<vt:p pane="Dissect2">
  Plain text inside paragraph for dissecting.
</vt:p>

<vt:pane name="Cell1">
  Plain text outside paragraph in cell.
</vt:pane>

<vt:pane name="Cell2">
  Plain text before paragraph in cell.
  <vt:p>
    Plain text inside paragraph in cell.
  </vt:p>
  Plain text after paragraph in cell.
  <vt:p/>
</vt:pane>

<vt:p pane="Cell3">
  <vt:b>
    Bold text inside paragraph in cell.
  </vt:b>
</vt:p>

<vt:p pane="Cell4">
  <vt:b>
    Bold text before paragraph in cell.
    <vt:p>
      Bold text inside paragraph in cell.
    </vt:p>
    Bold text after paragraph in cell.
  </vt:b>
</vt:p>

</vt:canvas>
