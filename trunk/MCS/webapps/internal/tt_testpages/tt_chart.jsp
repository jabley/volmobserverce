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
<vt:canvas layoutName="tt_default" theme="tt_chart" pageTitle="Chart Test Page">  


<vt:chart pane="test" styleClass="mychart" name="ChartTest" data="50 60 70,80 60 40"
altText="the altText from the JSP" labels="Jan Feb Mar" title="The chart title" />  

<vt:p pane="test"><vt:br/>There should be a chart above that does not use a chart asset.</vt:p>

<vt:p pane="test">
<vt:br/>
The chart below is gereated from the named chart asset "tt_chart"
<vt:chart pane="test" styleClass="mychart" labels="Jan Feb Mar" name="tt_chart1" data="50 60 70,80 40 20" altText="the altText from the JSP" title="The chart title" />  
<vt:br/>

</vt:p>
<vt:p pane="links">
<vt:a href="index.jsp">Main Index</vt:a>
<vt:a href="tt_charts.jsp">Chart Index</vt:a>
</vt:p>

</vt:canvas>

