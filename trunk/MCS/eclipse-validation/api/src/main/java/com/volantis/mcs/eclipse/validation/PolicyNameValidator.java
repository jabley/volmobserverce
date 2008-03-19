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

package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.utilities.FaultTypes;
import org.eclipse.core.runtime.Status;

/**
 * A Validator for Policy names. By default a name can only contain
 * alpha-numeric characters, underscores and hyphens and /s. However, the user
 * is allowed to add more invalid characters.
 *
 * The PolicyNameValidator delegates to a CharacterValidator internally. It is
 * not a kind of CharacterValidator since certain characters are always
 * valid. If CharacterValidator were specialized setValidationChars() and
 * setValidationType() would need to be overridden to avoid calls to the
 * parent class which is quite messy (a typically shortcoming of the
 * hierarchical model).
 *
 * In addition Policy names must not start is a "." even if "." is allowed in
 * the name.
 */
public class PolicyNameValidator extends SimpleValidator {
    /**
     * Volantis copyright.
     */
    private static String mark =
            "(c) Volantis Systems Ltd 2002. "; //$NON-NLS-1$

    /**
     * The maximum number of chars allowed for Policy names.
     */
    private static final int POLICY_NAME_MAX_CHARS = 254;

    /**
     * Array of additional invalid characters.
     */
    private char[] additionalInvalidChars;

    /**
     * The extensions of the policy names that are available. This may be
     * null or empty if extensions are not part of the validation.
     */
    private final FileExtension[] extensions;

    /**
     * A specialized CharacterValidator to handle the additional
     * requirements of the PolicyNameValidator.
     */
    private CharacterValidator characterValidator =
            new CharacterValidator() {
                // javadoc inherited
                public boolean isValidChar(char c) {
                    boolean valid = true;
                    if (additionalInvalidChars != null) {
                        for (int i = 0; i < additionalInvalidChars.length &&
                                valid; i++) {
                            valid = c != additionalInvalidChars[i];
                        }
                    }

                    if (valid) {
                        valid = Character.isDigit(c) || Character.isLetter(c)
                                || c == '-' || c == '_' || c == '/'
                                || c == '.';
                    }

                    return valid;
                }
            };


    /**
     * The CharacterValidator delegate.
     */
    private CharacterSetValidator delegate =
            new CharacterSetValidator(characterValidator);

    /**
     * Flag whether or not the policy name must start with a '/'.
     */
    private boolean forwardSlashRequired = false;

    /**
     * Constructs a new PolicyNameValidator with default values.
     */
    public PolicyNameValidator() {
        this(null, null, false);
    }

    /**
     * Constructs a new PolicyNameValidator.
     * @param additionalInvalidChars An array of additional invalid characters
     * - can bu null. This can be used when one or more characters are invalid
     * under some circumstances but valid at other times e.g. '.'.
     * @param extensions A set of allowable extensions. This may be null
     * or empty to indicate that extensions are not part of the validation.
     * @param valueRequired If true indicates that the value of the policy name
     * must contain at least one valid character otherwise there may be 0
     * characters.
     */
    public PolicyNameValidator(char[] additionalInvalidChars,
                               FileExtension[] extensions, boolean valueRequired) {

        this(additionalInvalidChars, extensions, valueRequired, false);
    }

    /**
     * Constructs a new PolicyNameValidator.
     * @param additionalInvalidChars An array of additional invalid characters
     * - can bu null. This can be used when one or more characters are invalid
     * under some circumstances but valid at other times e.g. '.'.
     * @param extensions A set of allowable extensions. This may be null
     * or empty to indicate that extensions are not part of the validation.
     * @param valueRequired If true indicates that the value of the policy name
     * must contain at least one valid character otherwise there may be 0
     * characters.
     * @param forwardSlashRequired true if the validation must ensure the name
     * starts with a '/', false otherwise.
     */
    public PolicyNameValidator(char[] additionalInvalidChars,
                               FileExtension[] extensions,
                               boolean valueRequired,
                               boolean forwardSlashRequired) {

        // Copy the parameter data in
        this.additionalInvalidChars = additionalInvalidChars;
        this.extensions = extensions;
        this.forwardSlashRequired = forwardSlashRequired;

        setMinChars(valueRequired ? 1 : 0);
        setMaxChars(POLICY_NAME_MAX_CHARS);
    }

    // javadoc inherited
    public void setMinChars(int minChars) {
        delegate.setMinChars(minChars);
    }


    // javadoc inherited
    public void setMaxChars(int maxChars) {
        delegate.setMaxChars(maxChars);
    }

    /**
     * Checks whether the given extension is contained within the array of
     * FileExtensions taken by the constructor.
     * @param extension The extension to check.
     * @return Returns true if and only if the array of FileExtensions passed to
     * the constructor of this class is non-null and non-empty and contains
     * the given extension.
     */
    public boolean isValidExtension(String extension) {
        boolean found = false;
        if (extensions != null) {
            for (int i = 0; i < extensions.length && !found; i++) {
                found = extensions[i].matches(extension);
            }
        }
        return found;
    }

    /**
     * Validate the specified object using the given message builder to
     * build any generated error messages. If the super class finds the object
     * to be invalid, then its fault type and format args will be used.
     * The fault types and message format args produced specifically by this
     * method are as follows:
     *
     * Fault type: INVALID_CHARACTER; args: object, invalid character, invalid
     * character position.
     * Fault type: INVALID_EXTENSION; args: object, valid extension
     * Fault type: INVALID_FIRST_CHARACTER; args: object, invalid character
     */
    // rest of javadoc inherited
    public ValidationStatus validate(Object object,
                                     ValidationMessageBuilder messageBuilder) {
        ValidationStatus status = delegate.validate(object, messageBuilder);

        String s = object == null ? null : object.toString();

        // Is there an object to test which is "OK so far", and are there
        // extensions to test against
        if (s != null && s.length() > 0 && status.getSeverity() == Status.OK) {

            if (s.startsWith(".")) {
                String faultType =
                        FaultTypes.INVALID_FIRST_CHARACTER;
                Object formatArgs[] = new Object[2];
                formatArgs[0] = object;
                formatArgs[1] = ".";

                String message =
                        messageBuilder.buildValidationMessage(faultType,
                                formatArgs);
                status = new ValidationStatus(Status.ERROR, message);
            } else if (extensions != null && extensions.length > 0) {

                // Get the position of the dot before the extension
                final int separatorIndex = s.indexOf('.');

                // Test the extension against the instance extensions
                boolean extensionOk = false;
                if (separatorIndex >= 0) {
                    extensionOk = isValidExtension(s.substring(separatorIndex
                            + 1));
                }

                if (!extensionOk) {
                    String faultType =
                            FaultTypes.INVALID_EXTENSION;

                    Object formatArgs[] = new Object[2];

                    formatArgs[0] = object;
                    StringBuffer extsBuff =
                            new StringBuffer(extensions[0].getExtension());
                    for (int i = 1; i < extensions.length; i++) {
                        extsBuff.append(", " + extensions[i].getExtension()); //$NON-NLS-1$
                    }
                    formatArgs[1] = extsBuff.toString();

                    String message =
                            messageBuilder.buildValidationMessage(faultType,
                                    formatArgs);

                    status = new ValidationStatus(Status.ERROR, message);

                } else if (forwardSlashRequired && !s.startsWith("/")) {
                    String faultType = FaultTypes.MUST_START_WITH;
                    Object formatArgs[] = new Object[1];
                    formatArgs[0] = "/";

                    String message = messageBuilder.buildValidationMessage(
                            faultType, formatArgs);
                    status = new ValidationStatus(Status.ERROR, message);
                }
            }
        }
        return status;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Mar-04	3471/1	byron	VBM:2004030504 Component Wizard does not add / to front of fallback components

 27-Feb-04	3200/1	allan	VBM:2004022410 Basic Update Client Wizard.

 18-Feb-04	3068/1	allan	VBM:2004021115 Validate fallback extensions in wizards.

 12-Feb-04	2962/1	allan	VBM:2004021113 Replace old 3 char file extensions with new 4 char ones.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 05-Dec-03	2128/5	pcameron	VBM:2003112105 Renamed FileExtension.match to matches

 05-Dec-03	2128/1	pcameron	VBM:2003112105 Added TextDefinition and refactored PolicySelector

 18-Nov-03	1878/13	richardc	VBM:2003110901 Refactor and use IFolder for session stuff

 17-Nov-03	1878/9	richardc	VBM:2003110901 No extensions IS allowed + updated JUnit

 14-Nov-03	1878/5	richardc	VBM:2003110901 fileExtensions may not be null or empty

 14-Nov-03	1878/3	richardc	VBM:2003110901 VBM 2003110901

 23-Oct-03	1502/6	allan	VBM:2003092202 Fix logic of extension check in validation.

 20-Oct-03	1502/2	allan	VBM:2003092202 Completed validation for PolicySelector.

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
