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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatScope.java,v 1.2 2003/03/04 12:53:27 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-02    Allan           VBM:2002103107 - A register of Formats for
 *                              a layout based on the name and class of
 *                              the Format. This is a replacement for
 *                              FormatNamespace and all of its specific
 *                              implementations.
 * 10-Dec-02    Allan           VBM:2002110102 - Refactored to FormatScope.
 *                              Renamed register and unregister methods to
 *                              add and remove respectively. Fixed the
 *                              bug where all pane types were registered
 *                              in different name->format maps. Changed the
 *                              the formatClassMap to an array based on
 *                              a bunch of constants that provide the index
 *                              to the right name->format map and updated/
 *                              added methods to work with the array where
 *                              appropriate. Removed formatRegistered() since
 *                              this is no different from retrieveFormat()
 *                              with a null check. (Previous name for this
 *                              class was FormatRegister.)
 * 04-Mar-03    Allan           VBM:2003021802 - Implement equals() and 
 *                              hashCode(). 
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper and 
 *                              UndeclaredThrowableException moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.layouts;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.Map;

/**
 * A container that defines a scope of Formats. For example, all the
 * Formats in a Layout are the FormatScope of that Layout. None
 * of the methods in the class use synchronisation since at the moment there
 * is no identified need to do so.
 */
public class FormatScope implements Cloneable {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(FormatScope.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    FormatScope.class);

    /**
     * An array of name to format maps.
     */
    private HashMap[] formatMaps = new HashMap[
            FormatNamespace.FORMAT_NAMESPACE_COUNT];

    /**
     * A bit set representing the FormatTypes that are excluded from
     * this FormatScope.
     */
    private int excluded = 0;

    /**
     * Create a clone of this FormatScope.
     * @return A clone of this FormatScope with shallow copies of the
     * HashMaps.
     */
    protected Object clone() {
        FormatScope clone = null;

        try {
            clone = (FormatScope) super.clone();
            clone.formatMaps = new HashMap[
                    FormatNamespace.FORMAT_NAMESPACE_COUNT];

            for(int i = 0; i < FormatNamespace.FORMAT_NAMESPACE_COUNT; i++) {
                if(formatMaps[i] != null) {
                    clone.formatMaps[i] = (HashMap) formatMaps[i].clone();
                }
            }
        }
        catch(CloneNotSupportedException e) {
            logger.error("clone-not-supported", e);
            throw new UndeclaredThrowableException(e);
        }

        return clone;
    }

    /**
     * Retrieve the format map - a mapping of names to formats all of which
     * are the same class.
     * @param formatType The format index.
     * @return The format map for the specified Format or null if none exist.
     */
    protected HashMap retrieveFormatMap(FormatType formatType) {
        return retrieveFormatMap(formatType.getNamespace());
    }

    /**
     * Retrieve the format map - a mapping of names to formats all of which
     * are the same class.
     * 
     * @param namespace The format namespace.
     * @return The format map for the specified Format or null if none exist.
     */
    protected HashMap retrieveFormatMap(FormatNamespace namespace) {
        return formatMaps[namespace.getIndex()];
    }

    /**
     * Initialize a format map t the index specified by format index
     * in the formatMaps array. Note: this method is not synchronized
     * and should only be called from a synchronized block.
     * @param formatMap The HashMap that will become the formatMap for
     * the specified formatScopeIndex.
     * @param formatType The index of the formatMap to create.
     */
    protected void initializeFormatMap(HashMap formatMap,
                                       FormatType formatType) {
        formatMaps[formatType.getNamespace().getIndex()] = formatMap;
    }

    /**
     * Add a Format and all of its descendent Formats to this
     * FormatScope. Only those Formats that have names are added.
     * @param format The Format whose to recursively add.
     * @throws IllegalArgumentException If there is a problem adding
     * one of the descendent Formats.
     */
    protected void addFormatRecursive(Format format) {
        for(int i = 0; i < format.getNumChildren(); i++) {
            Format child = format.getChildAt(i);
            if(child != null && child.getName() != null) {
                addFormatRecursive(child);
            }
        }

        addFormat(format);
    }

    /**
     * Construct a new FormatScope.
     */
    public FormatScope() {
    }

    /**
     * Construct a new FormatScope that shares one or more name->Format
     * maps with another FormatScope
     * @param sharingScope The FormatScope that is the source of the
     * shared name->Format maps.
     * @param formatTypes The FormatTypes that identify the name->Format
     * maps to share.
     */
    public FormatScope(FormatScope sharingScope, FormatType [] formatTypes) {
        if(sharingScope!=null && formatTypes!=null && formatTypes.length>0) {
            for(int i=0; i<formatTypes.length; i++) {
                FormatType type = formatTypes[i];
                HashMap sharedMap = sharingScope.retrieveFormatMap(type);
                if(sharedMap==null) {
                    // The map that is being shared has not been initialized
                    // in the sharing FormatScope. So we need to do that
                    // before we can initialize it in this FormatScope
                    sharedMap = new HashMap(1);
                    sharingScope.initializeFormatMap(sharedMap, type);
                }
                initializeFormatMap(sharedMap, type);
            }
        }
    }

    /**
     * Supply an array of FormatTypes that should be excluded from this
     * FormatScope.
     * @param excludes A FormatType array of excluded FormatTypes.
     */
    public void setExcludedFormatTypes(FormatType [] excludes) {
        if(excludes!=null || excludes.length>0) {
            for(int i=0; i<excludes.length; i++) {
                FormatType formatType = excludes[i];
                int excludedIndex = formatType.getNamespace().getIndex();
                excluded |= 1<<excludedIndex;
            }
        }

    }
    /**
     * Add a Format to this FormatScope. The format is
     * expected to have a name property.
     * @param format The Format.
     * @throws IllegalArgumentException If the name of the Format is null or
     * a format with the same name and the same format type already
     * exists in this FormatScope.
     */
    public void addFormat(Format format) {
        String name = format.getName();

        if((excluded&(1<<format.getFormatType().getNamespace().getIndex()))!=0) {
            throw new IllegalArgumentException("Format types \"" +
                                               format.getFormatType() +
                                               "\" are excluded from this" +
                                               " FormatScope");
        }

        if(name == null) {
            throw new IllegalArgumentException("Name of format is null.");
        }

        Map formatMap = retrieveFormatMap(format.getFormatType());

        if(formatMap == null) {
            formatMap = new HashMap(1);
            initializeFormatMap((HashMap) formatMap, format.getFormatType());

            // Disable this validation since validation is now done via
            // the validation infrastructure after load.
//        } else if(formatMap.get(name) != null) {
//            throw new IllegalArgumentException("Format \"" + format +
//                                               "\" already exists");
        }

        if(logger.isDebugEnabled()) {
            logger.debug("Adding format \"" + name + "\" of type " +
                         format.getFormatType() + " to FormatScope");
        }

        formatMap.put(name, format);
    }

    /**
     * Remove a Format from this FormatScope. The format is expected
     * to have a name property. All added child Formats of the specified
     * Format are also removed.
     * @param format The Format to remove.
     * @return The removed Format or null if nothing was removed.
     * @throws IllegalArgumentException If the name of the Format is null.
     */
    public Format removeFormat(Format format) {
        String name = format.getName();
        if(name == null) {
            throw new IllegalArgumentException("Name of format is null.");
        }

        Map formatMap = retrieveFormatMap(format.getFormatType());

        if(formatMap == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("Tried to remove a Format without a " +
                             "Format class map. Format class was " +
                             format);
            }
            return null;
        }

        for(int i = 0; i < format.getNumChildren(); i++) {
            Format child = format.getChildAt(i);
            if(child != null && child.getName() != null) {
                removeFormat(child);
            }
        }

        if(logger.isDebugEnabled()) {
            logger.debug("Removing format \"" + name + "\" of type " +
                         format.getFormatType());
        }
        return (Format) formatMap.remove(name);

    }

    /**
     * Retrieve a Format from this FormatScope.
     * @param name The name of the Format to retrieve.
     * @param formatType The formatScopeIndex of format to retrieve.
     * @return The Format specified or null if not found.
     */
    public Format retrieveFormat(String name, FormatType formatType) {
        Map formatMap = retrieveFormatMap(formatType);

        if(formatMap == null) {
            if(logger.isDebugEnabled()) {
                logger.debug("Tried to retrieve a Format without a " +
                             "Format class map. Format index was " +
                             formatType);
            }
            return null;
        }

        return (Format) formatMap.get(name);
    }

    /**
     * Retrieve a Format from this namespace.
     * 
     * @param name The name of the Format to retrieve.
     * @param namespace The namespace of format to retrieve.
     * @return The Format specified or null if not found.
     */
    public Format retrieveFormat(String name, FormatNamespace namespace) {
        Format format;
        if (namespace == FormatNamespace.CONTAINER) {
            // Try pane and region.
            Format pane = getFormatInternal(name, FormatNamespace.PANE);
            Format region = getFormatInternal(name, FormatNamespace.REGION);
            if (pane != null && region != null) {
                throw new IllegalStateException(
                        EXCEPTION_LOCALIZER.format(
                                "xdime-cp.format.name.conflict", name));
            } else if (pane != null) {
                format = pane;
            } else {
                format = region;
            }
        } else {
            format = getFormatInternal(name, namespace);
        }

        return format;
    }

    private Format getFormatInternal(String name, FormatNamespace namespace) {
        Map formatMap = retrieveFormatMap(namespace);

        if(formatMap == null) {
            if(logger.isDebugEnabled()) {
                // NOTE: This seems to happen a lot with fragments. I guess it
                // is because fragments "hide" panes often and replace them
                // with links...
                logger.debug("Tried to retrieve a Format without a " +
                             "Format class map. Format namespace was " +
                             namespace);
            }
            return null;
        }

        return (Format) formatMap.get(name);
    }

    /**
     * Replace one Format with another. This means checking that the
     * Format to replace exists, checking that the replacement format
     * does not violate any name uniqness rules for the name->format
     * mappings it or any of its child Formats may use, removing the
     * Format to replace along with all its children and finally adding
     * the replacement Format along with all of its children.
     *
     * NOTE: This method is not synchronised since no two threads should
     * be calling this method on the same Layout at the same time.
     * And, be wary of using this method since it has significant side
     * effects. Currently only the dubious LayoutModifier class uses this
     * method and in fact that is the only reason for the existance of this
     * method.
     *
     * @param oldFormat The Format to replace.
     * @param newFormat The replacement Format.
     * @throws IllegalArgumentException If there is a problem replacing the
     * oldFormat with the new one.
     */
    public void replaceFormat(Format oldFormat, Format newFormat)
            throws IllegalArgumentException {
        Map mapForOldFormat =
                retrieveFormatMap(oldFormat.getFormatType());
        if(mapForOldFormat == null) {
            String message = "Tried to replace format " + oldFormat +
                    " but there is no reference to it in this" +
                    "FormatScope";
            throw new IllegalArgumentException(message);
        }

        // Check that the newFormat can be added ok without actually
        // adding it. To do this we need to verify that the newFormat
        // and none of its children are already in the FormatScope
        // excluding the oldFormat and its children. The easiest way
        // to do this is to clone this FormatScope, remove the
        // oldFormat from the clone, and then add the newFormat to
        // the clone.
        FormatScope clone = (FormatScope) clone();
        clone.removeFormat(oldFormat);
        // Adding the format to the clone will throw an exception if
        // the newFormat cannot be added successfully.
        clone.addFormatRecursive(newFormat);

        // Looks ok to replace the old format with the new one.
        removeFormat(oldFormat);
        addFormatRecursive(newFormat);
    }

    // javadoc inherited
    public boolean equals(Object o) {
        boolean equals = o instanceof FormatScope;

        if(equals) {
            FormatScope scope = (FormatScope) o;
            equals = excluded == scope.excluded;
        }

        return equals;
    }

    // javadoc inherited
    public int hashCode() {
        return excluded;
    }
}




/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 21-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Aug-03	1074/1	byron	VBM:2003070203 Illegal Argument exceptions adding objects to layouts

 ===========================================================================
*/
