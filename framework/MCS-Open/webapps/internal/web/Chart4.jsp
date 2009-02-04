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
<vt:canvas layoutName="allan1" theme="flash_movies" pageTitle="A Chart">
<jsp:useBean id="chartData" class="volantis.mcs.example.utilities.ExampleChartData" scope="session">
</jsp:useBean>
<vt:chart pane="headlines" 
    styleClass="charts"
    title="A Chart"
    id="chart1"
    name="ExampleChart"
    data="<%= chartData.getData().toString() %>"
    labels="<%= chartData.getLabels().toString() %>"	
    altText="Hello Allan"/>
</vt:canvas>