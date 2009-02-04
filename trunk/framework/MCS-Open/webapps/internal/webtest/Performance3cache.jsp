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
<vt:canvas layoutName="Performance3" base="http://localhost:8080/delta/" cacheScope="all" >
<vt:p pane="preamble"><vt:h1>Exercise Vs Food intake</vt:h1></vt:p>
<vt:form preamblePane="preamble" postamblePane="postamble" action="formaction.jsp" submitText="Eeee-ha!" resetText="Oi!">

<vt:textarea name="general" rows="5" cols="10" entryPane="entry1" labelPane="label1">
General Information</vt:textarea>

<vt:textinput name="snack" labelPane="label2" entryPane="entry2">What is your favorite snack?</vt:textinput>

<vt:select menuStyle="false" multiple="false" label="What do you do for exercise?" name="exercise" entryPane="entry3" labelPane="label3">
<vt:option value="thumbs">I twiddle my thumbs</vt:option>
<vt:option value="toes">I twiddle my toes</vt:option>
<vt:option value="squash">I blink my eyes</vt:option>
<vt:option value="lying">All of the above</vt:option>
<vt:option value="nothing">None of the above</vt:option>
</vt:select>

<vt:select menuStyle="false" multiple="true" entryPane="entry4" labelPane="label4" label="How many of the following have you eaten today?"
name="crap">
<vt:option value="kinder">Kinder surprise</vt:option>
<vt:option value="ice">Ice cream</vt:option>
<vt:option value="chips">Potato chips</vt:option>
<vt:option value="cookies">Cookies</vt:option>
</vt:select>

<vt:select menuStyle="true" multiple="false" entryPane="entry5" labelPane="label5" label="What exercise did you do today?" name="today">
<vt:option value="nothing">Went to Tesco's</vt:option>
<vt:option value="still_nothing">Walked to the loo</vt:option>
<vt:option value="yet_nothing">Walked downstairs</vt:option>
<vt:option value="lying_again">All of the above</vt:option>
</vt:select>

<vt:select menuStyle="true" multiple="true" entryPane="entry6" labelPane="label6" label="Which is irrelevant compared with the above?" name="irrelevant">
<vt:option value="kite">I like kite-flying</vt:option>
<vt:option value="skate">I like skating</vt:option>
<vt:option value="summer">Summer is the best season throughout the year</vt:option>
<vt:option value="nonsense">Fruteani jduej hbili lopfituy</vt:option>
</vt:select>

</vt:form>

<vt:p pane="postamble">
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Performance.jsp">Back</vt:a>
</vt:p>
</vt:canvas>

