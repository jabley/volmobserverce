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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.widgets.attributes;

/**
 * Holds properties specific to DatePicker attributes.
 */
public class DatePickerAttributes extends WidgetAttributes {
    
    /**
     * Attributes of the loadable interface
     */    
    private LoadAttributes loadAttributes;

    public LoadAttributes getLoadAttributes() {
        return loadAttributes;
    }

    public void setLoadAttributes(LoadAttributes loadAttributes) {
        this.loadAttributes = loadAttributes;
    }
    
    /**
     * id of input field into which selected date will be typed
     */
    private String inputField;
    
    /**
     * id of input field into which selected day will be typed
     */
    private String dayInputField;
    
    /**
     * id of input field into which selected month will be typed
     */
    private String monthInputField;
    
    /**
     * id of input field into which selected year will be typed
     */
    private String yearInputField;
    
    //getters
    public String getInputField(){
        return inputField;
    }
    
    public String getDayInputField(){
        return dayInputField;
    }
    
    public String getMonthInputField(){
        return monthInputField;
    }
    
    public String getYearInputField(){
        return yearInputField;
    }
    
    //setters
    public void setInputField(String field){
        inputField = field;
    }
    
    public void setDayInputField(String field){
        dayInputField = field;
    }
    
    public void setMonthInputField(String field){
        monthInputField = field;
    }
    
    public void setYearInputField(String field){
        yearInputField = field;
    }
}
