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
package com.volantis.mcs.eclipse.common;

import java.util.HashSet;

/**
 * Typesafe enumeration for stylesheet units.  Values are provided for the
 * standard length, time and angle units.
 *
 * The supported unit names are:<br>
 * "px", "%", "pt", "in", "cm", "mm", "pc", "ex", "deg", "grad", "rad",
 * "sec", "ms".
 */
public class ResourceUnits {

    private String unitName;
    private String localizedName;

    /** private constructor for typsesafe enumeration.     *
     * @param unit The standard css unit code for the unit.
     */
    private ResourceUnits (String unit){
        unitName = unit;
        // lookup errors are caught and logged in EclipseCommonMessages so do
        // not need to dealt with here.
        localizedName = EclipseCommonMessages.getString("ResourceUnits." + unit); //$NON-NLS-1$
    }

    /** return the name for this unit
     *
     * @return standard markup name of this unit
     */
    public String getUnit(){
        return unitName;
    }

    /** return the localized name for this unit
     *
     * @return the localized name of this unit for the current locale.
     */
    public String getLocalized(){
        return localizedName;
    }

    /** Look up the 'standard' unit name from the current localized name.
     *
     * @param localizedName The name of the unit in the current locale
     * @return The standard unit name or null if the parameter localized name
     *  did not correspond to an actual standard unit.
     */
    public static String getUnitFromLocalizedName (String localizedName) {
        String res = null;
        for ( int i = 0;  i < vals.length; i++ ){
            if (vals[i].getLocalized().equals(localizedName) ){
                res = vals[i].getUnit();
                break;
            }
        }
        return res;
    }

    /** Look up the localized name in the current locale for a standard unit name.
     *
     * @param unitName The standard unit name
     * @return the localised name
     */
    public static String getLocalizedFromUnitName (String unitName) {
        String res = null;
        for ( int i = 0;  i < vals.length; i++ ){
            if (vals[i].getUnit().equals(unitName) ){
                res = vals[i].getLocalized();
                break;
            }
        }
        return res;
    }


    /** enueration value for pixel length unit.                             */
    public static final ResourceUnits PIXEL = new ResourceUnits ("px"); //$NON-NLS-1$
    /** enumeration value for percent (relative length unit)                */
    public static final ResourceUnits PERCENT = new ResourceUnits ("%"); //$NON-NLS-1$
    /** enumeration value for point length unit (1/72 inch)                 */
    public static final ResourceUnits POINT = new ResourceUnits ("pt"); //$NON-NLS-1$
    /** enumneration value for inch length unit                             */
    public static final ResourceUnits INCH = new ResourceUnits ("in"); //$NON-NLS-1$
    /** enumeration value for centimetre length unit.                       */
    public static final ResourceUnits CENTIMETRE = new ResourceUnits ("cm"); //$NON-NLS-1$
    /** enumeration value for millimetre length unit                        */
    public static final ResourceUnits MILLIMETRE = new ResourceUnits ("mm"); //$NON-NLS-1$
    /** enumeration value for pica size unit (one sixth of an inch)         */
    public static final ResourceUnits PICA = new ResourceUnits ("pc"); //$NON-NLS-1$

    /**
     * enumeration value for size unit of the letter M.
     */
    public static final ResourceUnits EM = new ResourceUnits ("em"); //$NON-NLS-1$
    /** enumeration value for ex size unit (the "x height" of the font, ie
     *  lowercase height without descenders or ascenders.
     */
    public static final ResourceUnits EX = new ResourceUnits ("ex"); //$NON-NLS-1$
    /** enumeration value for degree angle unit.                            */
    public static final ResourceUnits DEGREE = new ResourceUnits ("deg"); //$NON-NLS-1$
    /** enumeration value for grad angle unit (one hundredth of a
     * right angle)
     * */
    public static final ResourceUnits GRAD = new ResourceUnits ("grad"); //$NON-NLS-1$
    /** enumeration value for radian angle unit.                            */
    public static final ResourceUnits RADIAN = new ResourceUnits ("rad"); //$NON-NLS-1$
    /** enumeration value for second time unit.                             */
    public static final ResourceUnits SECOND = new ResourceUnits ("s"); //$NON-NLS-1$
    /** enumeration value for millisecond time unit.                        */
    public static final ResourceUnits MILLISECOND = new ResourceUnits ("ms"); //$NON-NLS-1$

    /** enumeration value for hertz frequency unit.                         */
    public static final ResourceUnits HERTZ = new ResourceUnits ("hz"); //$NON-NLS-1$
    /** enumeration value for kHertz frequency unit.                        */
    public static final ResourceUnits KILOHERTZ = new ResourceUnits ("khz"); //$NON-NLS-1$

    private static ResourceUnits []    vals = {
        PIXEL, PERCENT, POINT, INCH, CENTIMETRE, MILLIMETRE, PICA,
        EM, EX, DEGREE, GRAD, RADIAN, SECOND, MILLISECOND, HERTZ, KILOHERTZ
    };
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 14-Jan-04	2526/1	doug	VBM:2003112607 Added the StyleValueComposite control

 09-Dec-03	2170/2	pcameron	VBM:2003102103 Added AssetTypeSection

 17-Nov-03	1903/3	tony	VBM:2003110610 removed (useless) commented out code and println()s

 17-Nov-03	1903/1	tony	VBM:2003110610 fixed merge problems on original commit

 13-Nov-03	1857/1	tony	VBM:2003110704 ResourceUnits and unit test ready for review

 ===========================================================================
*/
