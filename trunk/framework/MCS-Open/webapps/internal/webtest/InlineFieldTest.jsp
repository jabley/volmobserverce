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
 % $Header: /src/voyager/webapp/internal/webtest/InlineFieldTest.jsp,v 1.2 2002/08/16 15:23:32 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 15-Aug-02    Paul            VBM:2002081421 - Created to test actions
 %                              outside forms.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="InlineFieldTest"
           theme="InlineFieldTest"
           pageTitle="InlineFieldTest">

<%
  String script;
  String script1;

  String protocol = marinerRequestContext.getDevicePolicyValue ("protocol");
  if (protocol.indexOf ("HTML") != -1) {
    script = "document.write ('click')";
    script1 = "document.write ('action 1')";
  } else if (protocol.indexOf ("WML") != -1) {
    script = "<go href='nowhere'/>";
    script1 = "<go href='action 1'/>";
  } else {
    script = "unknown protocol";
    script1 = "unknown protocol action 1";
  }
%>

<vt:p pane="Pane">
Some plain text with an inline action 
<vt:xfaction type="perform"
        styleClass="image"
        shortcut="{right}"
        caption="Inline Action"
        name="inlineAction"
        onclick="<%=script%>"/>
followed by some more text.
</vt:p>

<vt:xfaction type="perform"
             captionPane="Caption1"
             entryPane="Entry1"
             caption="Action "
             name="action2"
             onclick="<%=script1%>"/>

<%--
<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action1"
        prompt="<%=SubmitPrompt%>"
        value="action1"/>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 2\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action2"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        styleClass="image"
        shortcut="<%=shortcut%>"
        caption="<%=\"Action 3\"%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        styleClass="image"
        active="false"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption4%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        name="action4"
        prompt="<%=SubmitPrompt%>"
        value="action4"/>

<vt:xfaction type="submit"
        styleClass="image"
        active="false"
        shortcut="<%=shortcut%>"
        caption="<%=SubmitCaption5%>"
        captionPane="Fields"
        entryPane="Fields"
        help="<%=SubmitHelp%>"
        prompt="<%=SubmitPrompt%>"/>

<vt:xfaction type="submit"
        name="submitbutton"
        styleClass="text"
        shortcut="<%=shortcut%>"
        value="action6"
        caption="Button 6"
        captionPane="Fields"
        entryPane="Fields" />

<vt:xfaction type="submit"
        name="submitbutton"
        styleClass="text"
        name="submitbutton"
        shortcut="<%=shortcut%>"
        value="action7"
        caption="Button 7"
        captionPane="Fields"
        entryPane="Fields" />

--%>

</vt:canvas>
