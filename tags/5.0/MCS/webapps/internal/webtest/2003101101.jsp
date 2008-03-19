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
<vt:canvas layoutName="2003101101" theme="2003101101">
    <vt:pane name="Head1">Standard 2x2 table via a grid</vt:pane>
    <vt:pane name="A">1.</vt:pane>
    <vt:pane name="B">News</vt:pane>
    <vt:pane name="C">2.</vt:pane>
    <vt:pane name="D">Sport</vt:pane>
    <vt:pane name="Head2">Tables nested within a 1x2 table via grid</vt:pane>
    <vt:table pane="Col1" cols="1">
        <vt:tr>
            <vt:td>td in nested table</vt:td>
        </vt:tr>
    </vt:table>
    <vt:table pane="Col2" cols="1">
        <vt:tr>
            <vt:td>td in nested another table</vt:td>
        </vt:tr>
    </vt:table>
    <vt:pane name="Head3">Grid with style</vt:pane>
    <vt:pane name="Red">RedRedRed<vt:br/>RedRedRed</vt:pane>
    <vt:pane name="Yellow">YellowYellowYellow<vt:br/>YellowYellowYellow<vt:br/>YellowYellowYellow</vt:pane>
    <vt:pane name="Blue">BlueBlueBlue<vt:br/>BlueBlueBlue<vt:br/>BlueBlueBlue<vt:br/>BlueBlueBlue</vt:pane>

    <vt:table styleClass="style" pane="StyledTable" cols="2">
        <vt:tr valign="right" bgColor="aqua">
            <vt:td colSpan="2" bgColor="green" align="left" rowSpan="2">Big Cell</vt:td>
        </vt:tr>
        <vt:tr bgColor="red">
            <vt:td bgColor="white">Little White Cell</vt:td>
            <vt:td bgColor="purple">Little Purple Cell</vt:td>
            <vt:td styleClass="style">Little Red Cell</vt:td>
        </vt:tr>
    </vt:table>

    <vt:pane name="SingleRC">Single Row/Column Grid with bgColor</vt:pane>
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Oct-03	1542/3	allan	VBM:2003101101 HTML_iMode single col table removal rules and pane style fixes - ported

 13-Oct-03	1540/3	allan	VBM:2003101101 Fix pane styles and single column table removal

 12-Oct-03	1540/1	allan	VBM:2003101101 Add emulated and native table support on HTML_iMode

 ===========================================================================
--%>
