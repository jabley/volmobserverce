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
<vt:canvas layoutName="mgtregister">
<vt:logo pane="logo1" src="stars" altText="stars"/>
<vt:logo pane="logo2" src="volantis" altText="volantis"/>
<vt:h2 pane="preamble">Register User</vt:h2> 
<vt:form  pane="preamble" preamblePane="preamble"
               postamblePane="postamble"
               action="ProcessReg.jsp" 
               submitText="Register" 
               resetText="Clear">

<vt:select multiple="false" menuStyle="true" labelPane="titlelabel" entryPane="titleentry"
                    name="Title" default="Select Title" label="Title:">
<vt:option value="Mr">Mr</vt:option>
<vt:option value="Ms">Ms</vt:option>
<vt:option value="Mrs">Mrs</vt:option>
<vt:option value="Miss">Miss</vt:option>
</vt:select>
<vt:textinput labelPane="namelabel" entryPane="nameentry" 
                    name="Forename" size="20" maxLength="20">Forename: </vt:textinput>
<vt:textinput labelPane="mnamelabel" entryPane="mnameentry" 
                    name="Middlename" size="20" maxLength="20">Middle Name: </vt:textinput>
<vt:textinput labelPane="surnamelabel" entryPane="surnameentry" 
                    name="Surname" size="20" maxLength="20">Surname: </vt:textinput>
<vt:textinput labelPane="houselabel" entryPane="houseentry" 
                    name="Housedetails" size="20" maxLength="20">Address Detail: </vt:textinput>
<vt:textinput labelPane="citylabel" entryPane="cityentry" 
                    name="City" size="20" maxLength="20">City: </vt:textinput>
<vt:textinput labelPane="arealabel" entryPane="areaentry" 
                    name="Area" size="20" maxLength="20">Area: </vt:textinput>
<vt:textinput labelPane="postcodelabel" entryPane="postcodentry" 
                    name="Postcode" size="20" maxLength="20">Postcode: </vt:textinput>
<vt:textinput labelPane="countrylabel" entryPane="countryentry" 
                    name="Country" size="20" maxLength="20">Country: </vt:textinput>
<vt:textinput labelPane="phone1label" entryPane="phone1entry" 
                    name="Phone1" size="20" maxLength="20">Phone: </vt:textinput>
<vt:textinput labelPane="phone2label" entryPane="phone2entry" 
                    name="Phone2" size="20" maxLength="20"></vt:textinput>
<vt:textinput labelPane="emaillabel" entryPane="emailentry" 
                    name="Email" size="20" maxLength="20">Email: </vt:textinput>
<vt:textinput labelPane="cardlabel" entryPane="cardentry" 
                    name="Cardnumber" size="20" maxLength="20">Sky Digital Number: </vt:textinput>
</vt:form>
</vt:canvas>
