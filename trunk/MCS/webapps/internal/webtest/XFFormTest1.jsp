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
 % $Header: /src/voyager/webapp/internal/webtest/XFFormTest1.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 15-Oct-01    Paul            VBM:2001101204 - Created.
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="XFFormTest" pageTitle="XFFormTest1">

<%-- Test what happens when a field's entry pane does not exist. --%>

<vt:xfform
        name="Form"
        method="get"
        action="XFFormSubmit.jsp">

<vt:xfboolean
        name="field1"
        caption="Field 1"
        captionPane="Fields"
        entryPane="Fields"/>

<vt:xfboolean
        name="hiddenField1"
        caption="Hidden Field 1"
        captionPane=""
        entryPane=""/>

<vt:xftextinput
        name="hiddenField2"
        caption="Hidden Field 2"
        captionPane=""
        entryPane=""/>

<vt:xfsiselect
        name="hiddenField3"
        caption="Hidden Field 3"
        captionPane=""
        entryPane="">

  <vt:xfoption caption="Coffee" value="coffee"/>
  <vt:xfoption caption="Tea" value="tea"/>
  <vt:xfoption caption="Milk" value="milk"/>
  <vt:xfoption caption="Nothing" value="nothing"/>

</vt:xfsiselect>

<vt:xfmuselect
        name="hiddenField4"
        caption="Hidden Field 4"
        captionPane=""
        entryPane="">

  <vt:xfoption caption="Coffee" value="coffee"/>
  <vt:xfoption caption="Tea" value="tea"/>
  <vt:xfoption caption="Milk" value="milk"/>
  <vt:xfoption caption="Nothing" value="nothing"/>

</vt:xfmuselect>

<vt:xfaction type="submit"
        caption="Hidden Action 1"
        captionPane=""
        entryPane=""
        name="hiddenAction1"/>

<vt:xfaction type="submit"
        caption="Action 1"
        captionPane="Fields"
        entryPane="Fields"
        name="action1"/>

</vt:xfform>

</vt:canvas>
