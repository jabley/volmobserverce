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
<vt:canvas layoutName="adamLayoutPc" theme="p2g">
<%// This part tests the column iterator part /%>
<vt:form pane="coliter1" method="post" action="addNewTagSubmit.jsp" >
<vt:select name="test" default="1">
          <vt:option value="1"> option1 </vt:option>
          <vt:option value="2"> option2 </vt:option>
</vt:select>
</vt:form>
<vt:form pane="pay1" method="post" action="addNewTagSubmit.jsp" 
           preamblePane="pay1" 
           postamblePane="pay6" 
           action="OrderConfirm.jsp" 
           submitText="HELLO">
<vt:select multiple="false" 
           styleClass="yellowBGblackFont"
           menuStyle="false" 
           name="CCtype" 
           entryPane="pay3" 
           label=" "> 
         <vt:option value="Visa" >Visa</vt:option>
         <vt:option value="MC" >Master Card</vt:option>
         <vt:option value="Amex" >American Express</vt:option>
         <vt:option value="Disc" >Discovery</vt:option>
</vt:select>
<vt:textinput name="CCNumber" 
              labelPane="pay1" 
              entryPane="pay2" 
              maxLength="45" 
              size="20" 
              value="123">Number:
</vt:textinput>
<vt:select multiple="false" 
           styleClass="yellowBGblackFont"
           menuStyle="true" 
           name="CCExpMonth" 
           entryPane="pay6" 
           labelPane="pay5" 
           label="Expiration:" 
           default = "5"> 
        <vt:option value="1" styleClass="red">01</vt:option>
        <vt:option value="2">02</vt:option>
        <vt:option value="3">03</vt:option>
        <vt:option value="4">04</vt:option>
        <vt:option value="5">05</vt:option>
        <vt:option value="6">06</vt:option>
        <vt:option value="7">07</vt:option>
        <vt:option value="8">08</vt:option>
        <vt:option value="9">09</vt:option>
        <vt:option value="10">10</vt:option>
        <vt:option value="11">11</vt:option>
        <vt:option value="12">12</vt:option>
</vt:select>
</vt:form>
 <vt:p pane="rowiter1" styleClass="MedredBold">1</vt:p>
 <vt:p pane="rowiter1" styleClass="MedredBold">2</vt:p>
 <vt:p pane="rowiter1" styleClass="MedredBold">3</vt:p>
 <vt:p pane="rowiter1" styleClass="MedredBold">4</vt:p>
 <vt:p pane="rowiter1" styleClass="MedredBold">5</vt:p>

<vt:p pane="rowiter1"> Hello </vt:p>
<vt:menu pane="rowiter1" type="textonly" styleClass="v">
 <vt:menuitem href="www" text="View drinks" styleClass="BlueBox"/>
 <vt:menuitem href="www" text="View fridge" styleClass="GreenBox"/>
 <vt:menuitem href="www" text="Order History" styleClass="BlueBox"/>
 <vt:menuitem href="www" text="Edit Account" styleClass="BlueBox"/>
</vt:menu>

<vt:p pane="coliter1"> this text should be in the coliter1 and <vt:br/> break <vt:br/> here </vt:p>
<vt:p pane="pane1"> this text should be in pane1 and <vt:br/> break <vt:br/> here </vt:p>

</vt:canvas>
