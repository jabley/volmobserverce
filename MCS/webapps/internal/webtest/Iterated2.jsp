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
<vt:canvas layoutName="Iterated2">


<%
    int columns = 0;
    boolean validParameter = false; //We dont really need this, it doesn't do anything...
    String parameter = request.getParameter("columns");
    
    if(parameter != null)
        {
            try {
                    columns = Integer.parseInt(parameter);
                    if( (columns < 1) || (columns > 10)) columns = 0;
                    validParameter = true;
                }
            catch (Exception e)
                {
                    //Do Nothing!!
                }
        }

%>


<vt:pane name="plain">

<vt:h3>Testing columns iterated panes</vt:h3>

<vt:form action="Iterated2.jsp">

<vt:textinput name="columns" size="2" maxLength="2">
Enter a number between 1 and 10 for the number of columns: 
</vt:textinput>

</vt:form>

<vt:p>
<vt:br/>
<vt:a href="index.jsp">Home</vt:a>
<vt:a href="Layouts.jsp">Back</vt:a>
<vt:br/>
</vt:p>

</vt:pane>


<%
for(int c=0; c < columns; c++)
    {
%>

<vt:p pane="iterated">Column: <%= (c+1) %> </vt:p>

<%
    }

%>


</vt:canvas>

