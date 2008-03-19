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
<vt:canvas layoutName="K_CurrentTest" theme="K_CurrentTest" >
<vt:pane name="test">


<vt:p> This tag is written to test <vt:span styleClass="test">span</vt:span> tag </vt:p>

<vt:div styleClass="test">
<vt:p> this paragrapg is written to test div tag</vt:p>
</vt:div>

<vt:p>
This is to test iframe tag 
<vt:iframe><vt:a href="TryForms.html"> click here </vt:a>yyy </vt:iframe>
</vt:p>

<vt:p>
This is to test ilayer tag. This tag is only supported by netscapep. So if you can't see
anything on Internet Explorer then don't Wory
</vt:p>
<vt:ilayer styleClass="test" src="TryForms.html"></vt:ilayer>

<vt:multicol cols="2">
This is to test multicol tag, which is only supported by Netscape. If you can't see multicol in Internet Explorer 
then it is not your fault aaaa bbbb cccc dddd eeee ffff gggg hhhh iiii jjjj kkkk llll mmmm nnnn oooo pppp qqqq rrrr ssss tttt
uuuu vvvv wwww xxxx yyyy zzzz
</vt:multicol>

<vt:nobr> <vt:h1>This is to test nobr tag. this line should be appear in one line aaa bbb ccc ddd eee fff ggg hhh iii jjj kkk lll mmm nnn ooo ppp qqq rrr sss ttt uuu vvv www xxx yyy zzz </vt:h1></vt:nobr>

<vt:nobr><vt:h1>This is to test wbr tag. this line should be break after this <vt:wbr /> and continue in the next line aaa bbb ccc ddd eee fff ggg hhh iii jjj kkk lll mmm nnn ooo ppp qqq rrr sss ttt uuu vvv www xxx yyy zzz </vt:h1></vt:nobr>

<vt:p> This is to test noscript tag. If you can't see anythig that mean you browser support script</vt:p>
<vt:noscript>I am not supported by your browser </vt:noscript>

<vt:p> Test is to test embed tag <vt:embed src="cool_music.wav"> did you manage to here some music? </vt:p>





</vt:pane>



</vt:canvas>
