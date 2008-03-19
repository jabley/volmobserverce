/*
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
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.response.attributes;

import com.volantis.mcs.protocols.widgets.attributes.WidgetAttributes;

/**
 * Holds attributes specific to date-picker element from widgets-response namespace 
 */
public class ResponseDatePickerAttributes extends WidgetAttributes {
	
	private String currentDate = null;
	private String rangeStart = null;
	private String rangeEnd = null;
	/**
	 * @return Returns the currentDate.
	 */
	public String getCurrentDate() {
		return currentDate;
	}
	/**
	 * @param currentDate The currentDate to set.
	 */
	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}
	/**
	 * @return Returns the rangeEnd.
	 */
	public String getRangeEnd() {
		return rangeEnd;
	}
	/**
	 * @param rangeEnd The rangeEnd to set.
	 */
	public void setRangeEnd(String rangeEnd) {
		this.rangeEnd = rangeEnd;
	}
	/**
	 * @return Returns the rangeStart.
	 */
	public String getRangeStart() {
		return rangeStart;
	}
	/**
	 * @param rangeStart The rangeStart to set.
	 */
	public void setRangeStart(String rangeStart) {
		this.rangeStart = rangeStart;
	}
	
	
}
