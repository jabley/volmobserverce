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
<%
// The following code is used to validate policy attributes.
// Make sure that you free the connection in your code.
 String msg = null;
	
    String policy = request.getParameter("policy");
	String UI = request.getParameter("UI");
	String values = request.getParameter("values");
	String defaultValue = request.getParameter("default");
	String helptext = request.getParameter("helptext");

	if (policy == null || UI == null || helptext == null
	|| values == null || defaultValue == null) {
    	out.print("<p class=error>Could not find all the form data.</p>");
		throw new Exception("Could not find all the form data.");
	}
	UI = UI.trim();
	values = values.trim();
	defaultValue = defaultValue.trim();
	helptext = helptext.trim();
	Connection conn = volantis.getConnection();
	// ok if we didn't get thrown back by now, do some validation
    if  (new StringTokenizer(policy).countTokens() > 1 ) {
		msg = "Error - policy name must be only one word";
    } 
	else if (defaultValue.length() == 0) {
		msg = "No default value specified";
	}
	else if (!"text".equals(UI) && values.length() == 0) {
		/*
		 If user has specified a UI other than text, check to see if values
		 have been given
		*/
		msg = "UI type " + UI + " must have some values";
	}
	/*
	 If the t/f shortcut has been used, make sure that the default
	 value is in lower case.
	*/
	StringTokenizer stv = new StringTokenizer(values, ",");
	while(stv.hasMoreTokens()) {
		String token = stv.nextToken();
		if ("t/f".equals(token.toLowerCase())) {
			if (!"true".equals(defaultValue.toLowerCase()) &&
 				!"false".equals(defaultValue.toLowerCase())) {
				msg = "Default value for t/f shortcut must be either true or false";
			}
		}
		if (token.startsWith("%list")) {
			// Get a StringTokenizer so that we can get the values from the token
			StringTokenizer st = new StringTokenizer(token);
			if(st.countTokens() != 3) {
				msg = "Syntax for %list is %list {from} {to}";
			}
			else {
				st.nextToken(); // Skip over %list
				int from;
				int to;
				try {
				  from = Integer.parseInt(st.nextToken());
				  to = Integer.parseInt(st.nextToken());
				  if (from >= to) {
					msg = "to value must be greater than from value";
				}
        		}
        		catch (NumberFormatException nfe) {
					msg = "from and to values must be numeric";
				}
			}
		}
	}
%>
