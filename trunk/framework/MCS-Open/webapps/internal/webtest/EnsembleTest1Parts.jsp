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
 % $Header: /src/voyager/webapp/internal/webtest/EnsembleTest1Parts.jsp,v 1.2 2002/08/07 01:49:02 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 31-Jul-02    Paul            VBM:2002080509 - Created to test multiple
 %                              cards.
 % ======================================================================= --%>


<%@ include file="VolantisNoError-mcs.jsp" %>   

<vt:canvas layoutName="EnsembleTest1Parts" theme="EnsembleTest1"
           id="main" title="Main" type="inclusion"
           overlay="true" ontimer="{TimerEvent1}" onenter="{TimerEvent1}">
  <vt:timer duration="4000"/>
  <vt:p pane="Pane1">
    Main canvas in package
  </vt:p>
  <vt:p pane="Pane2">
    <vt:a href="#canvas2" target="nosuchregion">Canvas 2</vt:a>
  </vt:p>
</vt:canvas>

<vt:canvas layoutName="EnsembleTest1Parts" theme="EnsembleTest1"
           id="canvas2" title="Canvas2" type="inclusion"
           overlay="true">
  <vt:timer duration="6000"/>
  <vt:p pane="Pane1">
    Second canvas in package
  </vt:p>
  <vt:p pane="Pane2">
    <vt:a href="#canvas3" target="regionwithoutdestinationarea">Canvas 3</vt:a>
  </vt:p>
</vt:canvas>

<vt:canvas layoutName="EnsembleTest1Parts" theme="EnsembleTest1"
           id="canvas3" title="Canvas3" type="inclusion"
           overlay="true">
  <vt:timer duration="8000"/>
  <vt:p pane="Pane1">
    Third canvas in package
  </vt:p>
  <vt:p pane="Pane3">
    <vt:a href="#main" target="Region1">Main</vt:a>
    <vt:table cols="1">
      <vt:tr>
        <vt:td id="cell1" onclick="{TimerEvent1}">
          This is a cell
        </vt:td>
      </vt:tr>
    </vt:table>
  </vt:p>
</vt:canvas>

