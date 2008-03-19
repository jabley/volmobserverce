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
<!--
*************************************************************************
(c) Volantis Systems Ltd 2004. 
*************************************************************************
-->

<%@ include file="Volantis-mcs.jsp" %>

<vt:canvas layoutName="/tt_default.vly"
           theme="/tt_text.vth"
           pageTitle="Testing Assets">

<vt:pane name="fileinfo">
    <vt:h1>Asset Test</vt:h1>
    <vt:p>
    Filename: tt_assettest.jsp<vt:br/>
    Layout: /tt_default.vly<vt:br/>
    Theme: /tt_text.vth
    </vt:p>
</vt:pane>

<vt:pane name="header">
    <vt:h3>Purpose</vt:h3>
    <vt:p>
    To ensure that all asset types and menus work with projects
    </vt:p>
    <vt:h3>Devices</vt:h3>
    <vt:p>
    All EXCEPT VoiceXML and mobiles. No prompt parameters are defined on
    this page. Mobile browsers do not currently support background images.
    </vt:p>
    <vt:h3>Expected Results - Images</vt:h3>
    <vt:p>
    Several img tags are tested with valid and invalid references. The code
    in brackets indicates whether an image, alt text or fallback text asset
    is expected.
    </vt:p>
    <vt:h3>Expected Results - Scripts</vt:h3>
    <vt:p>
    A page should be created with a script calling tt_helloworld.js script
    file. <vt:b>CHECK THE SOURCE of this page to check the script attributes
    have been generated correctly.</vt:b>
    </vt:p>
    <vt:h3>Expected Results - Text</vt:h3>
    <vt:p>
    Different styles of text should be displayed as defined by the theme.
    </vt:p>
    <vt:h3>Expected Results - Audio</vt:h3>
    <vt:p>
    Audio components should be included in this page. <vt:b>VIEW PAGE SOURCE
    to check audio elements have correct markup. </vt:b>
    </vt:p>
</vt:pane>

<vt:p pane="mobileheader">
    Mobile browsers do not currently support background images.
</vt:p>

<vt:pane name="test">
     <%-- Testing images --%>
    <vt:ol>
    <vt:li>
        Valid image component with valid asset (expect IMG):<vt:br/>
        <vt:img src="/tt_masterimage.vic" altText="The alt text"/>
    </vt:li>
    <vt:li>
        Valid image component with valid asset, but the image file doesn't
        exist (expect ALT):<vt:br/>
        <vt:img src="/tt_imgcompwinvalidasset.vic" altText="An image of a green scribble"/>
    </vt:li>
    <vt:li>
        Valid image component with no image assets (expect ALT)<vt:br/>
        <vt:img src="/tt_imgcompwnoassets.vic" altText="An image of a green scribble"/>
    </vt:li>
    <vt:li>
        Valid image component with no image assets, but valid image fallback
        (expect fallback IMG):<vt:br/>
        <vt:img src="/tt_imgcompwimgfallback.vic" altText="An image of a green scribble"/>
    </vt:li>
     <%--
    This causes an error in the page at the moment (13/02/04)
    <vt:li>
        Valid image component with no valid image asset, or alt text, but valid
        text fallback (expect fallback TXT):<vt:br/>
        <vt:img src="/tt_imgcompwtextfallback.mimg"/>
    </vt:li>
     --%>
    <vt:li>
        Image component that doesn't exist (expect ALT):<vt:br/>
        <vt:img src="/thisimagedoesnotexist.vic" altText="An image that isn't there!"/>
    </vt:li>
    </vt:ol>

    <%-- Testing a script --%>
    <vt:p>
    About to test script element. Check the attributes in the source.
    </vt:p>
    <vt:script src="/tt_brandscript.vsc"
               language="javascript"
               type="text/javascript"
               charSet="ISO-8859-1"
               defer="true"/>

    <%-- Testing text --%>
    <vt:p>This is plain vt:p with no styleClass attribute.</vt:p>
    <vt:p styleClass="/textalign.vtc">text align = center</vt:p>
    <vt:p styleClass="/textindent">text indent = 2em</vt:p>
    <vt:p>
    This paragraph contains <vt:span styleClass="/valign">some text</vt:span>
    that has vertical align = top
    </vt:p>
    <vt:p styleClass="/whitespace">whitespace = nowrap. This paragraph is not
    allowed to word wrap, so the only way to end a line is by using the br tag.
    This line will go on forever and you'll have to scroll to see it.
    </vt:p>
    <vt:p styleClass="/letterspacing">letter spacing = 0.5em</vt:p>
    <vt:p styleClass="/wordspacing">
    The word spacing in this line should be 1em
    </vt:p>
    <vt:p styleClass="/lineheight">
    line height = 5. Should be larger space above and below this paragraph.
    </vt:p>
    <vt:p styleClass="/texttransform">
    text transform = capitalize. Each word should start with capital letter.
    </vt:p>
    <vt:p styleClass="/textdecoration">text decoration = underline</vt:p>
               
    <%-- Testing audio --%>
    <vt:p>
    Audio declarations included in the file here.
    </vt:p>
<%--
    This causes an error in the page at the moment (13/02/04)
    <vt:realaudio name="/Audio.vac"/>
    <vt:winaudio name="/Audio.vac"/>
    --%>
</vt:pane>

<%-- Testing dynamic visual --%>
<vt:dynvis pane="/TV.vdc" name="tv" altText="dynvis tv element" />
    
</vt:canvas>

<%--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Feb-04	2948/1	claire	VBM:2004020901 Additional test pages

 ===========================================================================
--%>
