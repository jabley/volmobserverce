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
 % $Header: /src/voyager/webapp/internal/webtest/PackageTest1.jsp,v 1.2 2002/08/02 10:35:19 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 31-Jul-02    Paul            VBM:2002073008 - Created to test packages.
 % ======================================================================= --%>


<%@ include file="Volantis-mcs.jsp" %>   

<vt:package>

  <vt:canvas layoutName="PackageTest1" theme="PackageTest1"
             id="main" title="Main">
    <vt:p pane="Pane1">
      Main canvas in package
    </vt:p>
    <vt:p pane="Pane2">
      <vt:a href="#canvas2">Canvas 2</vt:a>
    </vt:p>
  </vt:canvas>

  <vt:canvas layoutName="PackageTest1" theme="PackageTest1"
             id="canvas2" title="Canvas2">
    <vt:p pane="Pane1">
      Second canvas in package
    </vt:p>
    <vt:p pane="Pane2">
      <vt:a href="#canvas3">Canvas 3</vt:a>
    </vt:p>
  </vt:canvas>

  <vt:canvas layoutName="PackageTest1" theme="PackageTest1"
             id="canvas3" title="Canvas3">
    <vt:p pane="Pane1">
      Third canvas in package
    </vt:p>
    <vt:p pane="Pane3">
      <vt:a href="#main">Main</vt:a>
    </vt:p>
  </vt:canvas>

</vt:package>

