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
<vt:canvas layoutName="NetgemExt" theme="NetgemExt" initialFocus="field6" pageTitle="NetgemExt">
<vt:pane name="one">
<vt:h5>
A heading at level 5 that has rather a lot of text in for a heading anyway so 
much so in fact that it might have a tendancy to wrap on some devices unless 
this was prevented some how...
</vt:h5>
<vt:p>
A paragraph that like the heading above probably contains enough text to wrap
in some devices. Now while this is normally perfectly acceptable and even 
desirable behaviour for paragraphs some people might like the option to
prevent paragraph text from wrapping.  
</vt:p>

<vt:table cols="1">
<vt:tr>
<vt:th> A Heading </vt:th>
<vt:td> Table Data that is quite wide but the table will probably just expand
anyway to cope with the wide. Nevertheless is should be wide enough to wrap or
not depeneding on what is required.</vt:td>
</vt:tr>
</vt:table>

</vt:pane>

<vt:xfform name="Form"
        method="get"
        action="XFFormSubmit.jsp"
        prompt="Prompt">

<vt:xfmuselect
        active="true"
        name="field1"
        caption="Caption"
        captionPane="Caption"
        entryPane="Entry"
        errmsg="ErrorMsg"
        help="Help"
        prompt="Prompt2">
 <vt:xfoption caption="Coffee" value="coffee"/>
 <vt:xfoption caption="Tea" value="tea"/>
 <vt:xfoption caption="Milk" value="milk"/>
 <vt:xfoption caption="Nothing" value="nothing"/>
</vt:xfmuselect>   
 
<vt:xftextinput
        active="true"
        styleClass="multipleRows"
        name="field5"
        caption="Caption"
        captionPane="Caption"
        entryPane="Entry"
        help="Help"
        prompt="Prompt"/>

<vt:xftextinput
        active="true"
        styleClass="multipleRows"
        name="field6"
        caption="Caption"
        captionPane="Caption"
        entryPane="Entry"
        help="Help"
        prompt="Prompt"/>

</vt:xfform> 

</vt:canvas>   
