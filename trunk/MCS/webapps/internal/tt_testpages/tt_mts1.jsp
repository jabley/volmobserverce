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
<vt:canvas layoutName="tt_default">

<vt:pane name="fileinfo">
  <vt:h1>MTS Image Asset Fallback Test</vt:h1>
  <vt:p>Filename: tt_mts1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To confirm that the img tag will display a convertible image asset or the appropriate fallback asset.
<vt:pre>	img	src, altText</vt:pre>
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>Several img tags are tested with valid and invalid references. 
	The code in backets indicates whether an image, alt text or 
	fallback text asset is expected.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_MultimediaIndex.jsp">Multimedia Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>
    1. Valid image component with valid MTS asset (expect IMG):
    <vt:img src="tt_mtsmaster" altText="The alt text"/>
  </vt:p>
  <vt:p>
    2. Valid image component with valid MTS asset, 
    but the image file doesn't exist (expect ALT):
    <vt:img src="tt_mtscompwinvalidasset" altText="The alt text"/>
  </vt:p>
  <vt:p>
    3. Valid image component with no valid MTS image asset, 
    but valid MTS image fallback (expect fallback IMG):
    <vt:img src="tt_mtscompwmtsfallback" altText="The alt text"/>
  </vt:p>
</vt:pane>

</vt:canvas>
