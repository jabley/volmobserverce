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
 * $Header: /src/voyager/com/volantis/charset/configuration/Charset.java,v 1.2 2003/04/28 15:36:22 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003040701 - Created to hold information
 *                              on a Charset
 * 22-Apr-03    Mat             VBM:2003040701 - Updated javadoc and added
 *                              equals() method
 * 09-May-03   	Mat		VBM:2003040701 - Added an extra test to equals()
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset.configuration;

import java.util.ArrayList;

/**
 * Holds information for a charset
 * @author  mat
 */
public class Charset {

    /**
     * The character set name
     */
    private String name;

    /** 
     * The MIBenum assigned to the character set 
     * 0 is defined to be "not registered at IANA". 
     * @see <a href="http://www.iana.org/assignments/character-sets">Iana</a>
     */
    private int MIBenum;

    /** Indicates whether the charset can represent all encodings.
     * If this is true, a NoEncoding encoding will be used for 
     * the charset.
     */
    private boolean complete;

    /** Indicates whether the charset encoding class is to be 
     * generated at startup, or only when referenced.
     */
    private boolean preload;

    /** Holds a list of the aliases for this charset. */
    private ArrayList aliasList = null;

    /** Creates a new instance of CharsetConfiguration */
    public Charset() {
    }

    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }

    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name.toLowerCase();
    }

    /** Getter for property MIBenum.
     * @return Value of property MIBenum.
     *
     */
    public int getMIBenum() {
        return MIBenum;
    }

    /** Setter for property MIBenum.
     * @param MIBenum New value of property MIBenum.
     *
     */
    public void setMIBenum(int MIBenum) {
        this.MIBenum = MIBenum;
    }

    /** Getter for property complete.
     * @return Value of property complete.
     *
     */
    public boolean isComplete() {
        return complete;
    }

    /** Setter for property complete.
     * @param complete New value of property complete.
     *
     */
    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /** Getter for property preload.
     * @return Value of property preload.
     *
     */
    public boolean isPreload() {
        return preload;
    }

    /** Setter for property preload.
     * @param preload New value of property preload.
     *
     */
    public void setPreload(boolean preload) {
        this.preload = preload;
    }

    /** Add an alias
     * @param alias The alias to add
     */
    public void addAlias(Alias alias) {
        if (aliasList == null) {
            aliasList = new ArrayList();
        }
        aliasList.add(alias);
    }

    /**
     * Get the aliases for this charset
     *
     * @return The aliases
     */
    public ArrayList getAlias() {
        return aliasList;
    }

    /** Get a string representation of this class.
     * @return The string representation of this class.
     */
    public String toString() {
        return "Charset: name = " + getName() +
                " MIBenum = " + getMIBenum() +
                " preload = " + isPreload() +
                " complete = " + isComplete() +
                " aliases = " + getAlias();
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Charset)) {
            return false;
        }

        Charset c1 = (Charset) o;

        if (getName().equals(c1.getName()) &&
                getMIBenum() == c1.getMIBenum() &&
                isPreload() == c1.isPreload() &&
                isComplete() == c1.isComplete()) {
            return true;
        } else {
            return false;
        }
    }
        
        
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 ===========================================================================
*/
