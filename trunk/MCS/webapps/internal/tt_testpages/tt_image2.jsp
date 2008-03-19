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
<vt:canvas layoutName="tt_algorithmfromhell">

<vt:pane name="fileinfo">
  <vt:h1>Algorithm From Hell Test</vt:h1>
  <vt:p>Filename: tt_image2.jsp	<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>To ensure the Algorithm From Hell selects the most appropriate image.</vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on HTML browser, modifying device attributes.
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:h4>Modify your Device policy with the following configurations. Refresh this page to test each configuration returns the correct results.</vt:h4>
  <vt:p><vt:b>TEST CONFIGURATION 1:</vt:b><vt:br/>
	PNG, GIF and JPEG images supported within web pages<vt:br/>
	200 pixel width of useable display area<vt:br/>
	12-bit (color) pixel depth
  </vt:p>
  <vt:p><vt:b>TEST CONFIGURATION 2:</vt:b><vt:br/>
	PNG, GIF and JPEG images supported within web pages<vt:br/>
	200 pixel width of useable display area<vt:br/>
	4-bit (color) pixel depth
  </vt:p>
  <vt:p><vt:b>TEST CONFIGURATION 3:</vt:b><vt:br/>
	GIF and JPEG images supported within web pages<vt:br/>
	200 pixel width of useable display area<vt:br/>
	4-bit (color) pixel depth
  </vt:p>
  <vt:p><vt:b>TEST CONFIGURATION 4:</vt:b><vt:br/>
	GIF and JPEG images supported within web pages<vt:br/>
	200 pixel width of useable display area<vt:br/>
	12-bit (color) pixel depth
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
  <vt:a href="tt_MultimediaIndex.jsp">Multimedia Index</vt:a>
  </vt:p>
</vt:pane>

<vt:pane name="test1">
  <vt:p>Expected Results<vt:br/>
	Config 1. PNG<vt:br/>
	Config 2. PNG<vt:br/>
	Config 3. GIF<vt:br/>
	Config 4. GIF<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="test2">
  <vt:p>Results<vt:br/>
	100<vt:br/>
  	100<vt:br/>
	No image PNG unsupported<vt:br/>
	No image PNG unsupported<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="test3">
  <vt:p>Results<vt:br/>
	12-bit PNG<vt:br/>
  	12-bit PNG<vt:br/>
  	12-bit JPEG<vt:br/>
  	12-bit JPEG<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="test4">
  <vt:p>Results<vt:br/>
	1-bit PNG<vt:br/>
  	1-bit PNG<vt:br/>
  	1-bit GIF<vt:br/>
  	1-bit GIF<vt:br/>
  </vt:p>
</vt:pane>

<vt:pane name="test5">
  <vt:p>Results<vt:br/>
	12-bit PNG<vt:br/>
  	4-bit PNG<vt:br/>
  	4-bit GIF<vt:br/>
  	12-bit JPEG<vt:br/>
  </vt:p>
</vt:pane>


<vt:pane name="image1">
 <vt:p>Image 1</vt:p>
 <vt:img src="tt_algorithmfromhell1" altText="Failed to find suitable Image 1"/>
</vt:pane>
<vt:pane name="image2">
 <vt:p>Image 2</vt:p>
 <vt:img src="tt_algorithmfromhell2" altText="Failed to find suitable Image 2"/>
</vt:pane>
<vt:pane name="image3">
 <vt:p>Image 3</vt:p>
 <vt:img src="tt_algorithmfromhell3" altText="Failed to find suitable Image 3"/>
</vt:pane>
<vt:pane name="image4">
 <vt:p>Image 4</vt:p>
 <vt:img src="tt_algorithmfromhell4" altText="Failed to find suitable Image 4"/>
</vt:pane>
<vt:pane name="image5">
 <vt:p>Image 5</vt:p>
 <vt:img src="tt_algorithmfromhell5" altText="Failed to find suitable Image 5"/>
</vt:pane>

<vt:pane name="reference">
  <vt:h3>For Reference</vt:h3>
  <vt:p>IMAGE 1: Device must support file type.<vt:br/>
        8-bit color pallete PNG, GIF and JPEG images.<vt:br/>
	Used to test image Hierarchy - PNG > GIF > JPEG<vt:br/>
  </vt:p>
  <vt:p>IMAGE 2: Width hint test.<vt:br/>
	Several PNG images of different sizes.<vt:br/>
	Image should be 50% of usable display area, so for 200px expect 100px image.
  </vt:p>
  <vt:p>IMAGE 3: 1-bit v other color depth.<vt:br/>
	1-bit, 12-bit and 24-bit color JPEG, PNG and GIF images.<vt:br/>
	Used to test algorithm will choose image > device depth if the only image < device depth is 1-bit and device supports > 1-bit<vt:br/>
  </vt:p>
  <vt:p>IMAGE 4: Encoding match (B&W / color).<vt:br/>
	1-bit color and  8-bit greyscale JPEG, PNG and GIF images.<vt:br/>
	Used to test algorithm will choose color image if device supports color.
  </vt:p>
  <vt:p>IMAGE 5: Color depth and file type hierarchy<vt:br/>
	24-bit, 12-bit, 8-bit, 4-bit and 1-bit color JPEG, PNG and GIF images.<vt:br/>
	Used to test algorithm selects image of appropriate color depth, and changes file type hierarchy if PNG not supported.<vt:br/>
	If device color depth >=1 and < 4 expect 1-bit GIF<vt:br/>
	If device color depth >=4 and < 8 expect 4-bitGIF<vt:br/>
	If device color depth >=8 and < 12 expect 8-bitGIF<vt:br/>
	If device color depth >=12 and < 24 expect 12-bit JPEG<vt:br/>
	If device color depth >=24 and expect 24-bit JPEG<vt:br/>
</vt:p>

<vt:pre><vt:b>AFH RULES</vt:b>
1. Image file type must be supported by the device.
   If multiple types are supported see rule 5.

2. Image width must not exceed width hint percentage of useable display.

3. Image must be closest color depth <= device color depth
   or closest color depth > device color depth 
   if the only image <= device color depth is 1-bit and device supports > 1-bit.

4. Image must match device encoding (B&W / color) if possible.

5. If several appropriate file types are supported:
      If color depth <= 8-bit PNG > GIF > JPEG.
      If color depth >  8-bit PNG > JPEG > GIF.</vt:pre>
</vt:pane>


</vt:canvas>
