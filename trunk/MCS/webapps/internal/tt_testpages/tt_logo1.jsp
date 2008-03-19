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
	MJ	08/10/2001	Added this header
				Ensured all text is enclosed in p or h tags

      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="tt_default">

<vt:pane name="fileinfo">
  <vt:h1>Logo Asset Fallback Test</vt:h1>
  <vt:p>Filename: tt_logo1.jsp<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To confirm that the logo 
	tag will display an image asset or the appropriate fallback asset.<vt:br/>
	This test is identical to the Image Asset Fallback Test.
<vt:pre>	logo	src, altText</vt:pre>
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all devices.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>Several logo tags are tested with valid and invalid references. 
	The code in backets indicates whether an image, alt text or 
	fallback text asset is expected.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_images.jsp">Images Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test">
  <vt:p>
    1. Valid image component with valid asset (expect IMG):
    <vt:logo src="tt_bluebox1" altText="The alt text"/>
  </vt:p>
  <vt:p>
    2. Valid image component with valid asset, 
    but the image file doesn't exist (expect ALT):
    <vt:logo src="tt_imgcompwinvalidasset" altText="The alt text"/>
  </vt:p>
  <vt:p>
    3. Valid image component with no valid image asset (expect ALT)
    <vt:logo src="tt_imgcompwnoassets" altText="The alt text"/>
  </vt:p>
  <vt:p>
    4. Valid image component with no valid image asset, 
    but valid image fallback (expect fallback IMG):
    <vt:logo src="tt_imgcompwimgfallback" altText="The alt text"/>
  </vt:p>
  <vt:p>
    5. Valid image component with no valid image asset, or alt text,
    but valid text fallback (expect fallback TXT):
    <vt:logo src="tt_imgcompwtextfallback"/>
  </vt:p>
  <vt:p>
    6. Invalid image component (expect ALT):
    <vt:logo src="thisimagedoesnotexist" altText="The alt text"/>
  </vt:p>
</vt:pane>

</vt:canvas>
