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
 % $Header: /src/voyager/webapp/internal/webtest/AllEventHandlers.jsp,v 1.2 2002/05/20 16:13:57 pduffin Exp $
 % ============================================================================
 % (c) Volantis Systems Ltd 2002. 
 % ============================================================================
 % Change History:
 %
 % Date         Who             Description
 % =========    =============== ===============================================
 % 11-Mar-02    Paul            VBM:2001122105 - Added this header and also
 %                              added rollover image and rollover text menu
 %                              items.
 % 20-May-02    Paul            VBM:2001122105 - Added address.
 % ======================================================================= --%>

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="AllEventHandlers" theme="XFFormTest2"
      onload="{statusOnLoad}"
      onunload="{statusOnUnload}"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">

<vt:p pane="index">
<vt:script src="{openEventWindow}"/>
</vt:p>

<vt:p pane="index">
The standard first program written in a new programming language.<vt:br/>
</vt:p>
<vt:p pane="index">
<vt:script src="{HelloWorld}"/>
</vt:p>

<vt:p pane="index">
Select, change and focus on the form elements and watch the events being logged in the event window.  You can also partially hide the browser window to test if you get an OnBlur event.
</vt:p>

<vt:p pane="index">
<vt:a href="nowhere"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeypress="{statusKeyPress}"
      onkeyup="{statusKeyUp}"
      onmousedown="{statusMouseDown}"
      onmousemove="{statusMouseMove}"
      onmouseout="{OnMouseOut}"
      onmouseover="{OnMouseOver}"
      onmouseup="{statusMouseUp}">
<vt:img id="stars" src="stars"/>
</vt:a>
</vt:p>

<vt:p pane="index">
<vt:br/>
</vt:p>


<vt:xfform     
      name="Form"
      action="tt_xfformsubmit03.jsp"
      help="{tt_formhelp}"
      prompt="{tt_formprompt}">

<vt:xfsiselect
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        active="true"
        name="field1"
        caption="{field1Caption}"
        captionPane="xfform"
        entryPane="xfform"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}">
  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>
</vt:xfsiselect>

<vt:xfmuselect
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        active="false"
        name="field2"
        captionPane="xfform"
        entryPane="xfform"
        caption="{field1Caption}"
        errmsg="{field1Error}"
        help="{field1Help}"
        prompt="{field1Prompt}">
  <vt:xfoption caption="coffee" value="coffee"/>
  <vt:xfoption caption="tea" value="tea"/>
  <vt:xfoption caption="milk" value="milk"/>
  <vt:xfoption caption="nothing" value="nothing"/>
</vt:xfmuselect>

<vt:xfboolean
        onblur="{statusBlur}"
        onchange="{statusChange}"
        onfocus="{statusFocus}"
        onselect="{statusSelect}"
        name="field2"
        caption="{field2Caption}"
        captionPane="xfform"
        entryPane="xfform"
        help="{field2Help}"
        prompt="{field2Prompt}"
        onchange="{OnMouseOver}"
        falseValue="{field2FalseValues}"
        trueValue="{field2TrueValues}"/>

<vt:xftextinput name="drink"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                help="{tt_fieldhelp4}"
                prompt="{tt_fieldprompt4}"
                validate="{tt_fieldvalidate4}"
                errmsg="{tt_fielderrmsg4}"
                initial="17:30"
                shortcut="{one}"
                maxLength="6"
                type="text"
                caption="{tt_fieldcaption4}"
                captionPane="xfform"
                entryPane="xfform"/>

<vt:xfimplicit  name="myhiddenfield"
                value="The hidden value. Hurrah!"/>

<vt:xftextinput name="sugar"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                help="{tt_fieldhelp5}"
                prompt="{tt_fieldprompt5}"
                validate="{tt_fieldvalidate5}"
                errmsg="{tt_fielderrmsg5}"
                initial="ABC"
                shortcut="{two}"
                maxLength="6"
                type="password"
                caption="{tt_fieldcaption5}"
                captionPane="xfform"
                entryPane="xfform"/>

<vt:xftextinput name="papers"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                help="{tt_fieldhelp6}"
                prompt="{tt_fieldprompt6}"
                validate="{tt_fieldvalidate6}"
                errmsg="{tt_fielderrmsg6}"
                initial="0.00"
                shortcut="{three}"
                maxLength="6"
                type="text"
                caption="{tt_fieldcaption6}"
                captionPane="xfform"
                entryPane="xfform"/>

<vt:xfaction    type="submit"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                help="{tt_actionhelp}"
                prompt="{tt_actionprompt}"
                caption="{tt_actioncaption1}"
                captionPane="xfform"
                entryPane="xfform"/>

<vt:xfaction    type="reset"
                onblur="{statusBlur}"
                onchange="{statusChange}"
                onfocus="{statusFocus}"
                onselect="{statusSelect}"
                help="{tt_actionhelp}"
                prompt="{tt_actionprompt}"
                caption="{tt_actioncaption2}"
                captionPane="xfform"
                entryPane="xfform"/>

</vt:xfform>


<vt:p pane="para"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this text and watch the events logged in the status bar!
</vt:p>

<vt:p pane="para">
<vt:a href="myfakedestination.jsp"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"
      onblur="{statusBlur}"
      onfocus="{statusFocus}"
      title="A fake link but does it react to events?">
A fake link but does it react to events?
</vt:a>
</vt:p>

<vt:p pane="para">
<vt:va href="myfakedestination.jsp"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"
      onblur="{statusBlur}"
      onfocus="{statusFocus}"
      title="A fake validating link but does it react to events?">
A fake link but does it react to events?
</vt:va>
</vt:p>

<vt:p pane="para">
<vt:b
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this BOLD text and watch the events logged in the status bar!
</vt:b>
</vt:p>

<vt:p pane="para">
<vt:big
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this BIG text and watch the events logged in the status bar!
</vt:big>
</vt:p>

<vt:p pane="para">
<vt:small
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this SMALL text and watch the events logged in the status bar!
</vt:small>
</vt:p>

<vt:p pane="para">
<vt:span
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this SPAN text and watch the events logged in the status bar!
</vt:span>
</vt:p>

<vt:p pane="para">
<vt:strong
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this STRONG text and watch the events logged in the status bar!
</vt:strong>
</vt:p>

<vt:p pane="para">
<vt:tt
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this TT text and watch the events logged in the status bar!
</vt:tt>
</vt:p>

<vt:p pane="para">
<vt:u
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this UNDERLINE text and watch the events logged in the status bar!
</vt:u>
</vt:p>

<vt:p pane="para">
Click and move mouse over this 
<vt:sub
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      SUBSCRIPT
</vt:sub>
&nbsp;and&nbsp;
<vt:sup
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      SUPERSCRIPT
</vt:sup>
&nbsp;text and watch the events logged in the event window.
</vt:p>

<vt:p pane="para">
<vt:address
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this ADDRESS text and watch the events logged in the status bar!
</vt:address>
</vt:p>

<vt:p pane="para">
<vt:blockquote
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this BLOCKQUOTE text and watch the events logged in the status bar!
</vt:blockquote>
</vt:p>

<vt:div
      pane="para"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"
      styleClass="div1"/>

<vt:p pane="para">
<vt:cite
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this CITE text and watch the events logged in the status bar!
</vt:cite>
</vt:p>

<vt:div
      pane="para"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"
      styleClass="div2"/>

<vt:p pane="para">
<vt:code
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this CODE text and watch the events logged in the status bar!
</vt:code>
</vt:p>

<vt:p pane="para">
<vt:dl
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    <vt:dt
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"> 
      Check the events for this Definition Term
    </vt:dt>
    <vt:dd
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Check the events for this Definition Data
    </vt:dd>
</vt:dl>
</vt:p>

<vt:p pane="para">
<vt:em
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this EMPHASIS text and watch the events logged in the status bar!
</vt:em>
</vt:p>

<vt:p pane="para">
<vt:h1
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Check the events for HEADING 1
</vt:h1>
</vt:p>

<vt:p pane="para">
<vt:h2
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Check the events for HEADING 2
</vt:h2>
</vt:p>

<vt:p pane="para">
<vt:h3
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Check the events for HEADING 3
</vt:h3>
</vt:p>

<vt:p pane="para">
<vt:h4
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Check the events for HEADING 4
</vt:h4>
</vt:p>

<vt:p pane="para">
<vt:h5
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Check the events for HEADING 5
</vt:h5>
</vt:p>

<vt:p pane="para">
<vt:h6
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Check the events for HEADING 6
</vt:h6>
</vt:p>

<vt:p pane="para">
<vt:hr
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"/>
</vt:p>

<vt:p pane="para">
<vt:i
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this ITALIC text and watch the events logged in the status bar!
</vt:i>
</vt:p>

<vt:p pane="para">
<vt:img
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeypress="{statusKeyPress}"
      onkeyup="{statusKeyUp}"
      onmousedown="{statusMouseDown}"
      onmousemove="{statusMouseMove}"
      onmouseout="{OnMouseOut}"
      onmouseover="{OnMouseOver}"
      onmouseup="{statusMouseUp}"
      id="stars"
      src="stars"/>
</vt:p>

<vt:p pane="para">
<vt:kbd
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this KEYBOARD tag text and watch the events logged in the status bar!
</vt:kbd>
</vt:p>

<vt:p pane="para">
<vt:ol
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
<vt:li
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Ordered list Item 1
</vt:li>
<vt:li
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Ordered list Item 2
</vt:li>
</vt:ol>
</vt:p>

<vt:p pane="para">
<vt:ul
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
<vt:li
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Unordered list Item 1
</vt:li>
<vt:li
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
      Unordered list Item 2
</vt:li>
</vt:ul>
</vt:p>

<vt:p pane="para">
<vt:menu 
      type="plaintext"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  <vt:menuitem href="all.jsp" text="MenuItem 1"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"/>
  <vt:menuitem href="all.jsp" text="MenuItem 2"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"/>
  <vt:menuitem href="all.jsp" text="MenuItem 3"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"/>
</vt:menu>
</vt:p>

<vt:p pane="para">
<vt:menu 
      type="rolloverimage"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  <vt:menuitem href="all.jsp" text="MenuItem 3"
      onImage="volantis" offImage="stars"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"/>
</vt:menu>
</vt:p>

<vt:p pane="para">
<vt:menu 
      type="rollovertext"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
  <vt:menuitem href="all.jsp" text="MenuItem 3"
      onColor="red" offColor="green"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}"/>
</vt:menu>
</vt:p>

<vt:pane name="para"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
<vt:p>
    How do pane event attributes get translated?
</vt:p>
</vt:pane>

<vt:p pane="para">
<vt:pre
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this PRE tag text and watch the events logged in the status bar!
</vt:pre>
</vt:p>

<vt:p pane="para">
<vt:samp
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
    Click and move mouse over this SAMP tag text and watch the events logged in the status bar!
</vt:samp>
</vt:p>


<vt:table cols="1" title="Test of tables" pane="para"
      onmouseup="{statusMouseUp}"
      onmousedown="{statusMouseDown}"
      onmouseover="{statusMouseOver}"
      onmouseout="{statusMouseOut}"
      onmousemove="{statusMouseMove}"
      onclick="{statusClick}"
      ondblclick="{statusDblClick}"
      onkeydown="{statusKeyDown}"
      onkeyup="{statusKeyUp}"
      onkeypress="{statusKeyPress}">
<vt:tr bgColor="#00FF00">
<vt:td>This coloured</vt:td>
<vt:td bgColor="#FFFF00">table has event attributes</vt:td>
<vt:td>set only on the</vt:td>
</vt:tr>
<vt:tr bgColor="#0000FF">
<vt:td>TABLE tag</vt:td>
<vt:td> </vt:td>
<vt:td rowSpan="2">This cell spans two rows</vt:td>
</vt:tr>
<vt:tr bgColor="#FF0000">
<vt:td>A red row</vt:td>
</vt:tr>
</vt:table>



<vt:table cols="2" title="This table lists SI list" pane="para" >
             <vt:thead id="thead id" title="thead title" align="right"
                  onmouseup="{statusMouseUp}"
                  onmousedown="{statusMouseDown}"
                  onmouseover="{statusMouseOver}"
                  onmouseout="{statusMouseOut}"
                  onmousemove="{statusMouseMove}"
                  onclick="{statusClick}"
                  ondblclick="{statusDblClick}"
                  onkeydown="{statusKeyDown}"
                  onkeyup="{statusKeyUp}"
                  onkeypress="{statusKeyPress}">
               <vt:tr>
                 <vt:th>Name</vt:th>
                 <vt:th>Symbol</vt:th>
                 <vt:th>Quantity</vt:th>
               </vt:tr>
             </vt:thead>
             <vt:tbody id="12" title="tbody"
                  onmouseup="{statusMouseUp}"
                  onmousedown="{statusMouseDown}"
                  onmouseover="{statusMouseOver}"
                  onmouseout="{statusMouseOut}"
                  onmousemove="{statusMouseMove}"
                  onclick="{statusClick}"
                  ondblclick="{statusDblClick}"
                  onkeydown="{statusKeyDown}"
                  onkeyup="{statusKeyUp}"
                  onkeypress="{statusKeyPress}">
               <vt:tr>
                 <vt:td>meter</vt:td>
                 <vt:td>m</vt:td>
                 <vt:td>length</vt:td>
               </vt:tr>
               <vt:tr>
                 <vt:td>kilogram</vt:td>
                 <vt:td>kg</vt:td>
                 <vt:td>mass</vt:td>
               </vt:tr>
             </vt:tbody>
             <vt:tbody>
               <vt:tr
                  onmouseup="{statusMouseUp}"
                  onmousedown="{statusMouseDown}"
                  onmouseover="{statusMouseOver}"
                  onmouseout="{statusMouseOut}"
                  onmousemove="{statusMouseMove}"
                  onclick="{statusClick}"
                  ondblclick="{statusDblClick}"
                  onkeydown="{statusKeyDown}"
                  onkeyup="{statusKeyUp}"
                  onkeypress="{statusKeyPress}">
                 <vt:td>hertz</vt:td>
                 <vt:td>Hz</vt:td>
                 <vt:td>frequency</vt:td>
               </vt:tr>
               <vt:tr>
                 <vt:td>pascal</vt:td>
                 <vt:td>Pa</vt:td>
                 <vt:td>pressure</vt:td>
               </vt:tr>
             </vt:tbody>
             <vt:tbody>
               <vt:tr>
                 <vt:td
                  onmouseup="{statusMouseUp}"
                  onmousedown="{statusMouseDown}"
                  onmouseover="{statusMouseOver}"
                  onmouseout="{statusMouseOut}"
                  onmousemove="{statusMouseMove}"
                  onclick="{statusClick}"
                  ondblclick="{statusDblClick}"
                  onkeydown="{statusKeyDown}"
                  onkeyup="{statusKeyUp}"
                  onkeypress="{statusKeyPress}">
                  radian</vt:td>
                 <vt:td>rad</vt:td>
                 <vt:td>plane angle</vt:td>
               </vt:tr>

             </vt:tbody>
	     <vt:tfoot
                  onmouseup="{statusMouseUp}"
                  onmousedown="{statusMouseDown}"
                  onmouseover="{statusMouseOver}"
                  onmouseout="{statusMouseOut}"
                  onmousemove="{statusMouseMove}"
                  onclick="{statusClick}"
                  ondblclick="{statusDblClick}"
                  onkeydown="{statusKeyDown}"
                  onkeyup="{statusKeyUp}"
                  onkeypress="{statusKeyPress}">
             <vt:tr>
             <vt:td>table footer</vt:td>
             </vt:tr>
             </vt:tfoot>
           </vt:table>


<vt:pane name="dynvis">
<vt:winvideo name="RealVideoTest"/>
</vt:pane>

</vt:canvas>
