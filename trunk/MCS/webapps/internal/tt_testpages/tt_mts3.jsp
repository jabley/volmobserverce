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
<!--  *************************************************************************
	(c) Volantis Systems Ltd 2001. 
      *************************************************************************
	Revision Info 
	Name  	Date  		Comment
	MJ	21/10/2002	Added this header

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="tt_mtswidth" theme="tt_tablewidth">

<vt:pane name="fileinfo">
  <vt:h1>MTS Image Width Test</vt:h1>
  <vt:p>Filename: tt_mts3.jsp<vt:br/>
  	Layout: tt_mtswidth<vt:br/>
	Theme: tt_tablewidth
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To confirm that the MTS images will be the correct size.
<vt:pre>	img	src, altText</vt:pre>
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>Several imgages should be displayed. <vt:br/>
	Row 1 has an image at 101px and 377px.<vt:br/>
	Rows 2, 3 and 4 have an image at 50pc.<vt:br/>
	Row 5 has an image at 101px and 377px.<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_MultimediaIndex.jsp">Multimedia Index</vt:a>
  </vt:p>
</vt:pane>

<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="pane101pixels"/>
<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="pane377pixels"/>
<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="pane50pc"/>
<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="grid50pc"/>
<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="gridcol50pc"/>
<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="gridcol101px"/>
<vt:img src="tt_mts_24cj" altText="An MTS image should be here" pane="gridcol377px"/>


<vt:pane name="test">
  <vt:p>
    Check all 7 images above display at right size. View source to do this.
  </vt:p>

  <vt:table cols="2">
  <vt:tr>
  <vt:td><vt:img src="tt_mts_24cj" altText="An MTS image should be here"/>
  </vt:td>
  <vt:td><vt:p>The MTS image to the left should be ???</vt:p>
  </vt:td>
  </vt:tr>
  </vt:table>

  <vt:table cols="2">
  <vt:tr>
  <vt:td styleClass="width101px"><vt:img src="tt_mts_24cj" altText="An MTS image should be here"/>
  </vt:td>
  <vt:td><vt:p>The MTS image to the left should be 101px</vt:p>
  </vt:td>
  </vt:tr>
  </vt:table>

  <vt:div><vt:img src="tt_mts_24cj" altText="An MTS image should be here" styleClass="width101px"/>
  The MTS image to the left should be 101px</vt:div>

</vt:pane>

</vt:canvas>
