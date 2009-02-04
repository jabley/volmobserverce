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
	MJ	12/10/2001	Modified to use tt_default layout
      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>   
<vt:canvas layoutName="tt_default" theme="tt_repeatimage" >

<vt:pane name="fileinfo">
  <vt:h1>Repeating Background Image Test</vt:h1>
  <vt:p>Filename: tt_image3.jsp	<vt:br/>
  	Layout: tt_default<vt:br/>
	Theme: tt_repeatimage
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>
     To confirm that repeating background image style attribute works
     with the vt:p tag.
  </vt:p>
  <vt:h3>Devices</vt:h3>
  <vt:p>This page should be tested on all browsers except mobiles. 
	Mobile browsers do not currently support background images.</vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>There should be two rows displayed. The first row should be 
        filled with repeating background images and the second should 
        only display one image.
  </vt:p>
</vt:pane>

<vt:p pane="mobileheader">Mobile browsers do not currently support
	background images.
</vt:p>


<vt:pane name="test" >
  <vt:p styleClass="repeatimg"> 
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
    This paragraph should be filled with a repeating image.
  </vt:p>
  <vt:p styleClass="norepeat"> 
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
    This paragraph should have a single background image.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_multimedia.jsp">Multimedia Index</vt:a>
  </vt:p>
</vt:pane>

</vt:canvas>
