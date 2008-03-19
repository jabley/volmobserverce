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
/* ----------------------------------------------------------------------------
 * 
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Nov-01    MJ              Created
 * 10-Jan-02    Adrian          VBM:2002010901 - use java comments instead of
 *                              XML comments to exclude from output.
 * ---------------------------------------------------------------------------
 */

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform">

<vt:pane name="fileinfo">
  <vt:h1>Simple XFForm Test 1</vt:h1>
  <vt:p>Filename: tt_xfform1.jsp<vt:br/>
  	Layout: tt_xfform<vt:br/>
	Theme: none
  </vt:p>
</vt:pane>

<vt:pane name="header">
  <vt:h3>Purpose</vt:h3>
  <vt:p>This test is to ensure the vt:xfform tag works.</vt:p>
  <vt:pre>
     xfform       name, action
     xftextinput  name, caption, captionPane, entryPane
     xfaction     type, caption, captionPane, entryPane
  </vt:pre>
  <vt:h3>Devices</vt:h3>
  <vt:p>All EXCEPT VoiceXML. No prompt parameters are defined on this page. 
  </vt:p>
  <vt:h3>Expected Results</vt:h3>
  <vt:p>A form should be created with a single text field.
        There should also be an option to submit the form.
  </vt:p>
</vt:pane>

<vt:pane name="links">
  <vt:p>
    <vt:a href="tt_FormIndex.jsp">Form Index</vt:a>
  </vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit01.jsp">

<vt:xfsiselect name="stealth"
               caption="Do you want this to be a stealth alias?"
               captionPane="formCaption1"
               entryPane="formCaption1"
               initial="n">
<vt:xfoption value="y"
             caption="Yes"
/>
<vt:xfoption value="n"
             caption="No"
/>
    </vt:xfsiselect>



<vt:xfaction	type="submit"
		caption="Submit Order"
		captionPane="formCaption1"
		entryPane="formCaption1"/>
		
</vt:xfform>

</vt:canvas>
