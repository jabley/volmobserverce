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
package com.volantis.mcs.repository.jdbc;

import com.volantis.mcs.repository.RepositoryException;

import java.util.Map;
import java.util.HashMap;

/**
 * A class used to interpret SQLSTATE error codes. Can be used to map to
 * equivalent {@link com.volantis.mcs.repository.RepositoryException} error
 * codes.
 *
 * <p>SQLSTATE error codes are of the form:
 *
 * <p><i>CC</i><i>SSS</i></p>
 *
 * <p>where:
 *
 * <dl>
 *
 * <dt>CC</dt>
 *
 * <dd>is the error class</dd>
 *
 * <dt>SSS</dt>
 *
 * <dd>is the error sub-class</dd>
 *
 * </dl>
 *
 * <p>Each character in the code can be one of digits 0..9 or letters A..Z
 * (each being a simple one-byte character). Known error classes (and some key
 * sub-classes) are listed in ISO/IEC 9075-2:1999 (or the older ISO/ANSI SQL92
 * standard), section 22.1.</p>
 *
 * <p>MCS is only interested in the following error classes:</p>
 *
 * <dl>
 *
 * <dt>23</dt>
 *
 * <dd>Integrity Constraint Violation</dd>
 *
 * <dt>42</dt>
 *
 * <dd>Table or View does not exist</dd>
 *
 * <dt>61</dt>
 *
 * <dd>Resource busy</dd>
 *
 * <dt>72</dt>
 *
 * <dd>Invalid UserName or Password</dt>
 *
 * </dl>
 */
public class SQLState {

    /**
     * The number of characters in a SQLSTATE class.
     */
    static final int CLASS_LENGTH = 2;

    /**
     * The number of characters in a SQLSTATE subclass.
     */
    private static final int SUBCLASS_LENGTH = 3;

    /**
     * The number of characters in a full SQLSTATE code.
     */
    private static final int SQLSTATE_LENGTH = CLASS_LENGTH + SUBCLASS_LENGTH;

    /**
     * The set of all literals. Keyed on the SQLSTATE class, mapping to the
     * SQLState equivalent.
     * <p/>
     * NB: This static member *must* appear before the static instances
     * for this to work. If it does not, the access of this variable within
     * the instance construction (within this class's constructor) will find
     * this variable to be null (i.e. it won't have been initialized yet).
     * The Java Language Spec second edition, section 8.7, specifically
     * states that initialization is performed in "textual order".
     */
    private static final Map entries = new HashMap();

    /**
     * Represents the integrity constraint violation class.
     */
    public static final SQLState INTEGRITY_CONSTRAINT_VIOLATION =
        new SQLState("23",
                     RepositoryException.INTEGRITY_CONSTRAINT_VIOLATION);

    /**
     * Represents the table or view does not exist class.
     */
    public static final SQLState TABLE_OR_VIEW_DOES_NOT_EXIST =
        new SQLState("42",
                     RepositoryException.ERROR);

    /**
     * Represents the table or view does not exist class.
     */
    public static final SQLState RESOURCE_BUSY =
        new SQLState("61",
                     RepositoryException.ERROR);

    /**
     * Represents the invalid username or password class.
     */
    public static final SQLState INVALID_USER_NAME_OR_PASSWORD =
        new SQLState("72",
                     RepositoryException.AUTHORISATION_ERROR);

    /**
     * The classification for the given SQLState instance. Can be a class or
     * fully specified class and sub-class. Must be two or five digits.
     */
    private String classification;

    /**
     * The {@link RepositoryException} error code associated with the given
     * classification.
     */
    private int code;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param classification the two or five digit SQLSTATE value
     * @param code           the {@link RepositoryException} error code
     *                       associated with the given classification
     * @throws IllegalArgumentException if the classification is invalid
     */
    protected SQLState(String classification,
                       int code) {
        if ((classification.length() != CLASS_LENGTH) &&
            (classification.length() != SQLSTATE_LENGTH)) {
            throw new IllegalArgumentException(
                "SQLSTATE classifications may be " + CLASS_LENGTH + " or " +
                SQLSTATE_LENGTH + " digits only " +
                "(was given " + classification.length() + " digit value \"" +
                classification + "\")");
        } else {
            char ch;

            for (int i = 0;
                 i < classification.length();
                 i++) {
                ch = classification.charAt(i);

                // This checking specifically doesn't use the
                // {@link Character#isDigit}, {@link Character#isUpperCase}
                // and {@link Character#isLetter} methods in order to conform
                // to the ISO specification for SQLSTATE codes
                if (!((ch >= '0' && ch <= '9') ||
                      (ch >= 'A' && ch <= 'Z'))) {
                    throw new IllegalArgumentException(
                        "SQLSTATE codes may only contain 0..9 or A..Z, " +
                        "found '" + ch + "' at index " + i + " in " +
                        classification);
                }
            }
        }

        this.classification = classification;
        this.code = code;

        if (entries.containsKey(classification)) {
            throw new IllegalArgumentException(
                "SQLSTATE classification \"" + classification + "\" has " +
                "already been registered");
        } else {
            entries.put(classification, this);
        }
    }

    /**
     * The instance registered against the given SQLSTATE code, or the given
     * code's class (if a full SQLSTATE code has been supplied rather than a
     * SQLSTATE class) is returned, if any.
     *
     * @param sqlstate the SQLSTATE code for which a SQLState instance is
     *                 required
     * @return the SQLState instance matching the sqlstate, or null if none is
     *         available
     */
    public static SQLState instance(String sqlstate) {
        SQLState instance = null;

        instance = (SQLState)entries.get(sqlstate);

        if ((instance == null) && (sqlstate.length() > CLASS_LENGTH)) {
            String clazz = sqlstate.substring(0, CLASS_LENGTH);

            instance = (SQLState)entries.get(clazz);
        }

        return instance;
    }

    /**
     * Convenience method used to return the {@link RepositoryException} error
     * code associated with a given SQLSTATE code. Any unrecognised SQLSTATE
     * code will return {@link RepositoryException#ERROR}.
     *
     * @param sqlstate the SQLSTATE code for which a RepositoryException error
     *                 code is required
     * @return a RepositoryException error code equivalent to the given
     *         SQLSTATE code
     */
    public static int code(String sqlstate) {
        int code = RepositoryException.ERROR;
        SQLState instance = instance(sqlstate);

        if (instance != null) {
            code = instance.getCode();
        }

        return code;
    }

    /**
     * Returns the named attribute's current value.
     *
     * @return the named attribute's value
     */
    public String getClassification() {
        return classification;
    }

    /**
     * Returns the named attribute's current value.
     *
     * @return the named attribute's value
     */
    private int getCode() {
        return code;
    }

    /**
     * Returns true if the SQLState's classification matches the given SQLSTATE
     * code. Matching is either exact or by having the SQLSTATE code start with
     * the given SQLState's classification (where the classification is only a
     * class).
     *
     * @param sqlstate the SQLSTATE code to be checked for a match
     * @return true if the SQLSTATE code matches the SQLState classification
     */
    public boolean matches(String sqlstate) {
        if (sqlstate.length() != SQLSTATE_LENGTH) {
            throw new IllegalArgumentException(
                "Must provide a full SQLSTATE code");
        }

        return sqlstate.startsWith(classification);
    }

    /**
     * For test case usage only.
     *
     * <p><strong>DO NOT CHANGE THE ACCESS ON THIS METHOD</strong></p>
     */
    void kill() {
        entries.remove(classification);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Oct-03	1524/1	philws	VBM:2003090101 Port of SQLSTATE handling from PROTEUS

 09-Oct-03	1522/1	philws	VBM:2003090101 Ensure that SQLSTATE codes are matched against class codes only

 18-Mar-02  cvs     ianw    VBM:2002031203 Changed log4j Category from class to string

 25-Jan-01  cvs     pduffin VBM:unknown Created

 ===========================================================================
 */
