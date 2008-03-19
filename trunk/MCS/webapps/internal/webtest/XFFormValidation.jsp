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
<%-- ==========================================================================
 % $Header: /src/voyager/webapp/internal/webtest/XFFormValidation.jsp,v 1.2 2002/02/12 12:17:28 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2000. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 30-Jul-01    Paul            VBM:2001071609 - Added this header and added
 %                              some extra markup.
 % 04-Sep-01    Paul            VBM:2001081707 - Use text component validDate
 %                              to test the encoding checking.
 % 02-Oct-01    Doug            VBM:2001092805 - set the errmsg attribute for
 %                              for some of the xftextinput elements
 % 08-Oct-01    Doug            VBM:2001100401 - Replaced all component name
 %                              objects with mariner expressions
 % 11-Feb-02    Paul            VBM:2001122105 - Removed reference to
 %                              pagehelpers package.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="XFFormTest" theme="XFFormTest"
           pageTitle="XFFormValidation">

<vt:p pane="Welcome">
Some text <b>bold </b>hello.
<vt:p>
Some nested text
</vt:p>
</vt:p>

<vt:p pane="Form Header">
Some text
<vt:p>
Some nested text
</vt:p>
</vt:p>

<vt:xfform
        name="Form"
        method="get"
        action="XFFormSubmit.jsp"
        prompt="{formPrompt}">

<vt:xftextinput
        styleClass="singleRow"
        name="field4"
        caption="Number ####"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="N:####"
	errmsg="Ahh I can't accept that entry"/>


<vt:xftextinput
        styleClass="singleRow"
        name="field5"
        caption="Date DD/MM/YYYY"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="{validDate}"
	errmsg="{field1Error}"/>

<vt:xftextinput
        styleClass="singleRow"
        name="field6"
        caption="Phone No +##-####-##*#"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="P:+##-####-##*#"/>

<vt:xftextinput
        styleClass="singleRow"
        name="field7"
        caption="Text AA# #AA"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="M:AA# #AA"/>



<vt:xftextinput
        styleClass="singleRow"
        name="field10"
        caption="Date and time DD/MM/YYYY:HH:mm:ss"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="D:DD/MM/YYYY:HH:mm:ss"/>

<vt:xftextinput
        styleClass="singleRow"
        name="field11"
        caption="IP Address ###.###.###.###"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="N:###.###.###.###"/>

<vt:xftextinput
        styleClass="singleRow"
        name="field12"
        caption="Bad Pattern"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="D:######"/>

<vt:xftextinput
        styleClass="singleRow"
        name="field13"
        caption="Not empty"
        captionPane="Fields"
        entryPane="Fields"
        help="{field1Help}"
        prompt="{field1Prompt}"
        validate="M:M*M"/>

<vt:xfaction type="submit"
        shortcut="vnd.wtv-y"
        caption="{submitCaption}"
        captionPane="Fields"
        entryPane="Fields"
        help="{submitHelp}"
        prompt="{submitPrompt}"/>

</vt:xfform>
</vt:canvas>
