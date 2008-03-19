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
      * Change History:
      * Date         Who             Description
      ************************************************************************* 
      * 02-Aug-02    Sumit           VBM:2002073109 - optgroup test page
      * 01-Oct-02    Allan           VBM:2002093002 - Added prompt attributes
      *                              to an xfoptgroup and xfoption tag.
      * 07-Oct-02    Allan           VBM:2002100708 - Added a prompt attribute
      *                              to an optgroup tag whose caption is an
      *                              expression that will evaluate to null.
      *************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>
<vt:canvas 	layoutName="tt_xfform" theme="tt_xfform">

<vt:pane name="formHeader">
  <vt:p>Please choose from the options below</vt:p>
</vt:pane>

<vt:xfform 	name="orderForm" 
		action="tt_xfformsubmit02.jsp"
		prompt="{tt_formprompt}">

<vt:xfmuselect	name="drink" 
		active="true"
		initial="Hot Chocolate"
		errmsg="{tt_fielderrmsg1}"
		shortcut="{one}"
		styleClass="menuformat"
		caption="{tt_fieldcaption1}"
		prompt="{tt_fieldprompt1}"
		help="{tt_fieldhelp1}"
    		captionPane="formCaption1"
		entryPane="formField1">
	<vt:xfoptgroup caption="Hot drinks" prompt="Another prompt">
        <vt:xfoption caption="Tea" value="Tea" prompt="A prompt"/>
        <vt:xfoption caption="Coffee" value="Coffee"/>
        <vt:xfoption caption="Hot Chocolate" value="Hot Chocolate"/>
        <vt:xfoptgroup caption="{non_existant_component}" prompt="A Soup prompt">
          <vt:xfoption caption="Tomato" value="Tomato"/>
          <vt:xfoption caption="Chicken" value="Chicken"/>
        </vt:xfoptgroup>
  </vt:xfoptgroup>
	<vt:xfoptgroup caption="Cold drinks">
        <vt:xfoption caption="Orange Juice" value="Orange Juice"/>
        <vt:xfoption caption="Apple Juice" value="Apple Juice"/>
  </vt:xfoptgroup>
</vt:xfmuselect>

<!--**********************************************
    * Action tag in middle to prove the output   *
    * can deal with fields after the action tag  *
    **********************************************-->

<vt:xfaction	type="submit"
		caption="Submit"
		captionPane=""
		entryPane="formFooter"/>

<vt:xfaction	type="reset"
		caption="Reset"
		captionPane=""
		entryPane="formFooter"/>

	
</vt:xfform>

</vt:canvas>
