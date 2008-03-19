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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.xml.pipeline.sax.dynamic.rules;

import com.volantis.xml.pipeline.sax.XMLProcessImpl;
import com.volantis.xml.pipeline.sax.XMLProcess;

import java.util.Iterator;

import org.xml.sax.SAXException;

/**
 * Simple proccess which outputs the value of it's text property
 */
public class ProcessWhichUsesProperties extends XMLProcessImpl {

    /**
     * The text to output
     */
    private String text;
    private int intValue;
    private double doubleValue;
    private boolean booleanValue;
    private static final String CONFIG_PARAMETERS_TITLE =
        " Config parameters: ";

    //javadoc unnecessary
    public void setText(String text) {
        this.text = text;
    }

    public void setCount(int value) {
        intValue = value;
    }

    public void setSetterWithLongName(double value) {
        doubleValue = value;
    }

    public void setSetterWithLongName2(boolean value) {
        booleanValue = value;
    }

    //javadoc inherited
    public void stopProcess() throws SAXException {
        final XMLProcess nextProcess = getNextProcess();
        nextProcess.characters(text.toCharArray(), 0, text.length());
        final String intStr = " i: " + Integer.toString(intValue);
        nextProcess.characters(intStr.toCharArray(), 0, intStr.length());
        final String doubleStr = " d: " + Double.toString(doubleValue);
        nextProcess.characters(doubleStr.toCharArray(), 0, doubleStr.length());
        final String booleanStr = " b: " + Boolean.toString(booleanValue);
        nextProcess.characters(booleanStr.toCharArray(), 0, booleanStr.length());
        final ParametersConfiguration configuration = (ParametersConfiguration)
            getPipelineContext().getPipelineConfiguration().
                retrieveConfiguration(getClass());
        if (configuration != null) {
            Iterator iter = configuration.getParameterNames();
            if (iter.hasNext()) {
                nextProcess.characters(CONFIG_PARAMETERS_TITLE.toCharArray(), 0,
                    CONFIG_PARAMETERS_TITLE.length());
                while (iter.hasNext()) {
                    final String name = (String) iter.next();
                    final String value = configuration.getParameterValue(name);
                    final String line = name + " - " + value;
                    nextProcess.characters(
                        line.toCharArray(), 0, line.length());
                    if (iter.hasNext()) {
                        nextProcess.characters(
                            ", ".toCharArray(), 0, ", ".length());
                    }
                }
            }
        }
        super.stopProcess();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	32/1	trynne	VBM:2005103112 Added javabean style process initialization

 ===========================================================================
*/
