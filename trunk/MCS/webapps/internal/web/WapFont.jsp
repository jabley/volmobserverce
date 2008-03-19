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

<vt:canvas layoutName="wapfont" theme="wapfont">

<vt:pane name="logo" >
<vt:img src="vol_logo" altText="Volantis Systems Ltd" />
</vt:pane>


<vt:pane name="congratulations" >
<vt:p>This paragraph is xxsmall and blue in colour</vt:p>
</vt:pane>

<vt:pane name="smallprint">
<vt:p styleClass="supersmall">
This paragraph is large, bolder and purple in colour.  
</vt:p>
</vt:pane>


<vt:pane name="bigprint">
<vt:p styleClass="small">This paragraph is xxlarge,italic and red in colour 
</vt:p>
</vt:pane>


</vt:canvas>

