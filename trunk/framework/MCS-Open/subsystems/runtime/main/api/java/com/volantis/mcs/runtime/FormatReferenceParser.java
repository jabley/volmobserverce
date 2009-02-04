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
 * $Header: /src/voyager/com/volantis/mcs/runtime/FormatReferenceParser.java,v 1.4 2003/03/26 11:19:06 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Oct-02    Chris W         VBM:2002111102 Created.
 * 10-Dec-02    Allan           VBM:2002110102 - Modified getPaneDimensions()
 *                              to use retrieveFormat() and FormatScope to
 *                              retrieve the Pane.
 * 03-Mar-03    Sumit           VBM:2003022828 - Removed System.out.println
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.MCSExpressionHelper;
import com.volantis.mcs.expression.FormatReferenceValue;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.FormatReference;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.Value;

/**
 * Converts string values of the form "stem{.x}" into
 * <code>FormatReference</code> objects.
 */
public class FormatReferenceParser {
    /**
     * The copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2004. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(FormatReferenceParser.class);
    
    /**
     * Converts the supplied pane reference from a string to a 
     * FormatReference object, accounting for incompletely or excessively
     * specified indices.
     * <p>
     * Note that this supports both the normal expression syntax and the
     * deprecated "pane.X.Y.Z" format. 
     *
     * @param formatRef The format reference as a String
     * @param pageContext
     * @return the generated format reference
     */
    public static FormatReference parsePane(String formatRef,
            MarinerPageContext pageContext) {

        // Panes support backwards compatible mode.
        return parse(formatRef, pageContext, FormatNamespace.PANE);
    }

    /**
     * Converts the supplied format reference from a string to a 
     * FormatReference object, accounting for incompletely or excessively
     * specified indices.
     * <p>
     * Note that this supports only the normal expression syntax. Specifically,
     * the "format.X.Y.Z" format is not supported.
     * 
     * @param formatRef The format reference as a String
     * @return the generated format reference
     */
    public static FormatReference parse(String formatRef,
            MarinerPageContext pageContext) {
        
        return parse(formatRef, pageContext, null);
    }
    
    /**
     * Converts the supplied format reference from a string to a 
     * FormatReference object, accounting for incompletely or excessively
     * specified indices.
     * <p>
     * If the formatType parameter is not null, then it will attempt to
     * use the deprecated "format.X.Y.Z" format if the format reference fails
     * to parse using the normal expression syntax.
     *
     * @param formatRef The format reference as a String
     * @return the generated format reference
     */
    private static FormatReference parse(String formatRef,
            MarinerPageContext pageContext, FormatNamespace namespace) {
        
        String stem = null;
                
        // If the name starts and ends with a {} pair, we use the pipeline
        // to evaluate the format. For iterated formats the expression will be
        // {layout:get[Format]Instance('stem',index,...)} which evaluates into a 
        // FormatReferenceValue. Otherwise, the expression is assumed to result
        // in a string which holds the old style iterated format reference such
        // as 'title.1' or 'title.2.3' in which case the string is treated in
        // the same way as before.
        FormatReference ref = null;
        if (formatRef.startsWith("{") && formatRef.endsWith("}")) {
            stem = formatRef.substring(1, formatRef.length() - 1);
            MarinerRequestContext mrc = pageContext.getRequestContext();
            ExpressionContext ec =
                    MCSExpressionHelper.getExpressionContext(mrc);

            try {
                Expression expr = MCSExpressionHelper.createUnquotedExpression(
                        stem, mrc);

                if (expr != null) {
                    Value value = expr.evaluate(ec); 

                    if (value instanceof FormatReferenceValue) {
                        ref = ((FormatReferenceValue)value).
                                asFormatReference();
                    } else {
                        formatRef = value.stringValue().asJavaString();
                    }
                }
            } catch (ExpressionException e) {
                logger.error("unexpected-exception");
            }
        }
        
        // If we have a reference, then the pipeline expression used for the 
        // format name was a {layout:get[Format]Instance()} expression. Otherwise we
        // have a 'Normal' format name or a pipeline expression that resulted
        // in a string... in either case we process this as a format name and
        // build a format reference from it.
        if (ref == null) {
            if (namespace != null) {
                if (pageContext.getFormat(formatRef, namespace) != null) {
                    stem = formatRef; 
                } else {
                    stem = getFormatReferenceStem(formatRef);
                }
    
                int layoutDim = getFormatDimensions(stem, namespace, pageContext);
            
                // When layoutDim == -1 the formats doesn't exist. When layoutDim == 0
                // it's a regular format. In either case the number of dimensions is 0. 
                if (layoutDim <= 0) {
                    ref = new FormatReference(
                            stem,
                            NDimensionalIndex.ZERO_DIMENSIONS);
                } else {
                    NDimensionalIndex tail = 
                                createIndex(formatRef, layoutDim);
                    ref = new FormatReference(stem, tail);
                }
            } else {
                ref = new FormatReference(formatRef, 
                        NDimensionalIndex.ZERO_DIMENSIONS);
            }
        }
        
        return ref;
    }

    /**
     * Returns the dimensions of a FormatIterator as determine by its layout.
     *
     * @param stem the base name of the format
     * @param pageContext the context providing access to the required format
     * @return int the number of dimensions that this format has
     */
    private static int getFormatDimensions(String stem,
                                         FormatNamespace namespace,
                                         MarinerPageContext pageContext) {
        Format format = pageContext.getFormat(stem, namespace);

        if (format == null) {
            // Format does not exist so it can't have any dimensions.
            return -1;
        }
        
        return format.getDimensions();        
    }
    
    /**
     * Helper method that creates and returns the index of a reference to a format
     * @param formatRef The format reference as a String.
     */
    private static NDimensionalIndex createIndex(
            String formatRef,
            int layoutDim) {
        int pos = formatRef.indexOf('.');

        if (pos == -1) {
            // Make sure the reference reflects the actual number of
            // dimensions
            if (layoutDim == 0) {
                return NDimensionalIndex.ZERO_DIMENSIONS;
            } else {
                return new NDimensionalIndex(new int[layoutDim], 0);
            }
        }
                        
        int nextPos = pos;
        
        // Store the ints in between each of the dots of the format reference's
        // tail in an array of ints.
        int[] coords;
        int specified = -1;

        try {
            coords = new int[layoutDim];
            
            boolean reachedEnd = false;
            
            // By trying to get the correct number of dimensions we deal with
            // references with too many indices by ignoring those on the right.
            for (int counter = 0; counter < layoutDim; counter++) {
                if (!reachedEnd) {
                    nextPos = formatRef.indexOf('.', pos + 1);
                    if (nextPos != -1) {
                        coords[counter] =
                            Integer.parseInt(formatRef.substring(pos + 1,
                                                                 nextPos));
                        pos = nextPos;
                    } else {
                        coords[counter] =
                            Integer.parseInt(formatRef.substring(pos + 1));
                        reachedEnd = true;
                    }

                    // We've had at least this many indices specified
                    specified = counter;
                } else {
                    // If we have reached the end of the string before we have
                    // got all the dimensions that we need we assume that those
                    // present refer to the outermost format iterators. Missing
                    // indices assume a value of zero e.g. if we expect 2
                    // dimensions and only one is supplied we interpret test.2
                    // as test.2.0
                    coords[counter] = 0;
                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                "formatRef " + formatRef + " is invalid");
        }

        return new NDimensionalIndex(coords, specified + 1);
    }
    
    /**  
     * Returns the stem of a pane name i.e. strip off any format iterator indices.
     * e.g. 1. The stem of a pane called test.0 is test      
     *      2. The stem of a pane called test is test  
     * @param formatRef The format reference as a String
     * @return String The stem of the format reference
     */
    private static String getFormatReferenceStem(String formatRef) {
        int pos = formatRef.indexOf('.');

        if (pos == -1) {
            return formatRef;
        } else {
            return formatRef.substring(0, pos);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/6	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 02-Jul-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/4	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 06-May-04	3999/3	philws	VBM:2004042202 Review updates

 06-May-04	3999/1	philws	VBM:2004042202 Handle automatic iteration allocation in Menus

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 08-Jan-04	2461/1	steve	VBM:2003121701 Patch pane name changes from Proteus2

 07-Jan-04	2389/4	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/2	steve	VBM:2003121701 Pre-test save

 ===========================================================================
*/
