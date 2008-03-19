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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.servlet;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;


/**
 * A class which parses and then represents an accept-* header, as defined in
 * RFC 2616 (HTTP 1.1 spec). <p> Note that the relevant parts of the
 * specification are mostly contained within sections 3.9 and 14.2. <p>
 *
 * @volantis-api-include-in InternalAPI
 */
public class AcceptParser {

    /**
     * this is a Comparator class which is used to sort table of
     * AcceptableLanguages by using values of qualities or lexicographically in
     * the event of the qualities will be the same
     */
    private class AcceptComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            AcceptEntry a1 = (AcceptEntry) o1;
            AcceptEntry a2 = (AcceptEntry) o2;
            int result = a1.quality.compareTo(a2.quality);

            // In the event of the qualities are the same we have to sort
            // AcceptableLanguages lexicographically
            if (alphaSort && (result == 0)) {
                result = a1.entry.compareToIgnoreCase(a2.entry);
            }
            return result;
        }
    }
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(AcceptParser.class);

    /**
     * A constant QValue of 1.
     */
    private static final BigDecimal QValueOne = new BigDecimal("1.0");

    /**
     * A constant QValue of 0.
     */
    private static final BigDecimal QValueZero = new BigDecimal("0.0");


    /**
     * An object whos completed method is called when adding headers is
     * complete.
     *
     * It proivides an opertunity to add extra entries depending on the
     * presense of absense of other entries.
     */
    private OnComplete onComplete;

    /**
     * The list of entries as parsed (from R to L) from the header field(s),
     * minus any "not acceptable" entries.
     */
    private List acceptableEntryList;

    /**
     * Set of acceptable values (i.e those with qvalue > 0).
     */
    private Set acceptableValueSet;

    /**
     * Set of "not acceptable" values (i.e. those with qvalue = 0).
     */
    private Set notAcceptableValueSet;

    /**
     * The variable indicating if it is need to sort lexicographically of
     * AcceptEntries set
     */
    private boolean alphaSort = false; 

    /**
     * Initializes the new instance.
     */
    public AcceptParser() {
        this(false);
    }

    /**
     * Initializes the new instance using the given parameters. 
     * 
     * @param alphaSort
     * indicates whether header entries of the same quality should 
     * be sorted or left in the order they are specified
     */
    public AcceptParser(final boolean alphaSort) {
        this(new OnComplete() {
            public void completed(AcceptParser p) {
            }
        }, alphaSort);
    }

    /**
     * Initializes the new instance using the given parameters.
     * 
     * @param onComplete
     *            the onComplete to call when all the headers have been added
     */
    public AcceptParser(final OnComplete onComplete) {
        this(onComplete, false);
    }

    /**
     * Initializes the new instance using the given parameters.
     * 
     * @param onComplete
     *            the onComplete to call when all the headers have been added
     * 
     * @param alphaSort
     *            indicates whether header entries of the same quality should be
     *            sorted or left in the order they are specified
     */
    public AcceptParser(final OnComplete onComplete, final boolean alphaSort) {
        this.onComplete = onComplete;
        acceptableEntryList = new ArrayList();
        acceptableValueSet = new HashSet();
        notAcceptableValueSet = new HashSet();
        this.alphaSort = alphaSort;
    }

    /**
     * Add the value of an "Accept-*:" header field to this object.
     * <p>
     * This will parse the field into it's constituent values and qvalues and
     * populate the internal structures accordingly.
     * <p>
     * This can be called multiple times, for example if there were mutiple
     * Accept-* headers in the request.
     * 
     * @param field
     *            a single Accept-*: header field value.
     */
    public void addHeaderField(String field) {

        // Parse the field into the internal representation.
        if (logger.isDebugEnabled()) {
            logger.debug("Parsing header '" + field + "'");
        }

        // This isn't very fast or memory efficient, but then it won't happen
        // that often either - this is O(n) per page serve, where n is the
        // number of accept values (which will be small).

        // Parse the list of charsets which are a comma separated list
        StringTokenizer commas = new StringTokenizer(field, ",");
        while (commas.hasMoreTokens()) {
            // Parse a charser declaration in the form "value;q=x.y"
            String listValue = commas.nextToken();
            String name;
            // We assume a default qvalue of 1.0.
            BigDecimal qValue = QValueOne;
            // Search for the first ;
            // We expect the list value to have only one parameter - q
            // A more general algorithm would search for multiple semicolons.
            int semicolon = listValue.indexOf(";");
            if (semicolon >= 0) {
                // We found a semicolon, so parse out the qvalue
                name = listValue.substring(0, semicolon).trim();
                String parameter = listValue.substring(semicolon + 1);
                StringTokenizer equals = new StringTokenizer(parameter, "=");
                if (equals.hasMoreTokens() &&
                    equals.nextToken().trim().equals("q") &&
                    equals.hasMoreTokens()) {
                    String qString = equals.nextToken().trim();
                    try {
                        BigDecimal qFoundValue = new BigDecimal(qString);
                        if (qFoundValue.scale() <= 3) {
                            float qFloat = qFoundValue.floatValue();
                            if (!(qFloat < 0f || qFloat > 1f)) {
                                // Found a valid qvalue, so use it.
                                qValue = qFoundValue;
                            } else {
                                // else, ignore numbers outside range.
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Ignoring invalid qvalue " +
                                                 qFoundValue + " - outside range");
                                }
                            }
                        } else {
                            // else, ignore numbers with more than 3 DP.
                            if (logger.isDebugEnabled()) {
                                logger.debug("Ignoring invalid qvalue " +
                                             qFoundValue + " - too many DP");
                            }
                        }
                    } catch (NumberFormatException e) {
                        // ignore invalid numbers
                        if (logger.isDebugEnabled()) {
                            logger.debug("Ignoring invalid qvalue " +
                                         qString + " - invalid numeric");
                        }
                    }
                } else {
                    // else, ignore invalid q parameters
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "Ignoring invalid parameter " + parameter);
                    }
                }
            } else {
                name = listValue.trim();
            }
            addEntry(name, qValue);
        }
    }

    /**
     * Creates a sorted accept header from the headers added
     * 
     * @return an Header which holds the sorted entries from the added headers
     */
    public Header build() {
        onComplete.completed(this);
        // Create a sorted version of the entry list.
        // This sorts in natural (lowest to highest order) so it relies on
        // the fact that we created negated qvalues as documented in addEntry.
        List sortedAcceptableEntryList = new ArrayList(acceptableEntryList);
        Collections.sort(sortedAcceptableEntryList, new AcceptComparator());
        return new Header(sortedAcceptableEntryList);
    }
    
    /**
     * Returns <tt>true</tt> if this this accept header contains the value
     * specified, with a qvalue > 0, using case insensitive comparison.
     * <p>
     * <strong> Note</strong>: this currently does an exact match rather than a
     * logical match; i.e. it does not match any value if the "*" value is
     * present.
     * </p>
     * 
     * @param value
     * @return boolean flag
     */
    public boolean containsAcceptable(String value) {
        return acceptableValueSet.contains(canonicaliseName(value));
    }

    /**
     * Returns <tt>true</tt> if this this accept header contains the value
     * specified, with a qvalue == 0, using case insensitive comparison. <p>
     * Note this currently does an exact match rather than a logical match;
     * i.e. it does not match any value if the "*" value is present.
     *
     * @param value
     * @return boolean flag
     */
    public boolean containsNotAcceptable(String value) {
        return notAcceptableValueSet.contains(canonicaliseName(value));
    }

    /**
     * Private helper method to add a name, qvalue entry to our internal
     * structures.
     *
     * @param name   the name of charset.
     * @param qValue the quality value associated with this charset. 0 is
     *               defined as "not acceptable" as per the spec.
     */
    private void addEntry(String name, BigDecimal qValue) {
        // Create the "canonical" name we use for name searches.
        String canonicalName = canonicaliseName(name);
        // If the value is non-zero
        // NOTE: use compareTo() 'cos .equals() doesn't work like this!
        if (!(qValue.compareTo(QValueZero) == 0)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding acceptable charset: name='" +
                             canonicalName + "' qvalue=" + qValue);
            }
            // Add it to the set of acceptable entries.
            // We negate the qvalue so that they sort in the correct
            // order (reverse natural) from largest to smallest, but without
            // reordering equal values. I tried using a comparator to do it,
            // but found I would need to parse the entries from right to left
            // to avoid reordering equal values which was too hard :-).
            qValue = qValue.negate();
            // Add the entry to the map we use for sorted iteration, with
            // the name as specified in the accept header.
            acceptableEntryList.add(new AcceptEntry(name, qValue));
            // Add the canonicalised name to set of acceptable names
            acceptableValueSet.add(canonicalName);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Adding not acceptable charset: name='" +
                             canonicalName + "'");
            }
            // Add the canonicalised name to the set of unacceptable names.
            notAcceptableValueSet.add(canonicalName);
        }

    }

    /**
     * Private helper method to convert names to a "canonical" format which is
     * suitable for name searches.
     *
     * @param name the name to canonicalise.
     * @return the canonicalised name.
     */
    private String canonicaliseName(String name) {
        return name.toLowerCase();
    }

    /**
     * Private class to store an accept entry.
     */
    private class AcceptEntry {

        private String entry;

        /**
         * note these are stored in range -1 to 0 to make sorting easier!
         */
        private BigDecimal quality;

        AcceptEntry(String entry, BigDecimal quality) {
            this.entry = entry;
            this.quality = quality;
        }
    }

    /**
     * An Internal Iterator interface for the acceptable values belonging to an
     * accept header. <p> Internal Iterators are defined <a
     * href="http://www.c2.com/cgi/wiki?InternalIterator">here</a>
     *
     * @see Header#forEachAcceptable
     */
    public interface InternalAcceptableIterator {

        /**
         * This method will be called once, before any values are processed.
         * <p> It will be called even if there are zero children in the
         * element.
         */
        void before();

        /**
         * This method will be called once for each acceptable value of the
         * accept header.
         *
         * @param value the value to process.
         */
        void next(String value);

        /**
         * This method will be called once, after any values are processed. <p>
         * It will be called even if there are zero values in the parser.
         */
        void after();
    }
    
    /**
     * Represents a parsed and sorted accept-* header
     */
    public static class Header {

        /**
         * The list of sorted acceptable entries
         */
        private final List sortedAcceptableEntryList;

        /**
         * Creates a header using the provided list of sorted acceptable
         * entries
         *
         * @param sortedAcceptableEntryList the entries in the header
         */
        private Header(List sortedAcceptableEntryList) {
            this.sortedAcceptableEntryList = sortedAcceptableEntryList;
        }

        /**
         * Iterates over the entries in their qvalue order, from highest to
         * lowest, ignoring entries with a qvalue of <tt>0</tt>.
         *
         * @param iterator internal acceptable entry iterator.
         */
        public void forEachAcceptable(InternalAcceptableIterator iterator) {
            iterator.before();
            // Iterate over the appropriately sorted entries.
            Iterator itr = sortedAcceptableEntryList.iterator();
            while (itr.hasNext()) {
                AcceptEntry entry = (AcceptEntry) itr.next();
                String name = entry.entry;
                iterator.next(name);
            }
            iterator.after();
        }
    }

    /**
     * An interface used as a hook to make changes to the entries once all the
     * official headers have been added
     */
    public interface OnComplete {

        /**
         * Called duing build after the headers have been added and parsed
         *
         * @param parser the parser which contains the headers added before
         *               build was called
         */
        public void completed(AcceptParser parser);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jun-05	8675/4	trynne	VBM:2005052602 added javadoc and moved inner classes and interfaces to the bottom of the class

 06-Jun-05	8675/2	trynne	VBM:2005052602 Generalised AcceptCharsetParser to AcceptParser

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 24-Jul-03	807/6	geoff	VBM:2003071405 use fallbacks more often and allow user to set it themselves if we can't

 23-Jul-03	807/4	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
