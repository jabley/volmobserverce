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
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import com.volantis.mcs.eclipse.common.DelayedTaskExecutor;

/**
 * An instance of this class can be used to asynchronously cause invocations
 * of a given delegate validator's {@link XMLValidator#validate} method.
 *
 * <p><strong>WARNING</strong>: it is important that {@link #dispose} is called
 * when the given instance is no longer required.</p>
 */
public class AsynchronousXMLValidator implements XMLValidator {

    /**
     * Defines the default delay before validation will be performed.
     */
    private final static int DEFAULT_DELAY = 1000;

    /**
     * The thread used to handle the asynchronous nature of the validation.
     */
    private DelayedTaskExecutor validationThread;

    /**
     * The validator to which this validator delegates.
     */
    private XMLValidator validator;

    /**
     * Initializes the new instance using the given parameters and a default
     * delay interval.
     *
     * @param validator the validator to be delegated to
     */
    public AsynchronousXMLValidator(XMLValidator validator) {
        this(validator, DEFAULT_DELAY);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param validator     the validator to be delegated to
     * @param delayInMillis the number of milliseconds delay that must elapse
     *                      after a call to {@link #validate} before validation
     *                      is performed. Must be a natural number
     */
    public AsynchronousXMLValidator(final XMLValidator validator,
                                 int delayInMillis) {
        if (delayInMillis <= 0) {
            throw new IllegalArgumentException(
                "The delay (" + delayInMillis + //$NON-NLS-1$
                ") must be a positive number of milliseconds"); //$NON-NLS-1$
        }

        this.validator = validator;

        validationThread =
                new DelayedTaskExecutor("AsynchronousXMLValidator.validationThread",
                delayInMillis) {
            // javadoc inherited.
            public void executeTask() {
                validator.validate();
            }
        };

        validationThread.start();
    }

    // javadoc inherited
    public void validate() {
        if (validationThread.isDisposed()) {
            throw new IllegalStateException(
                "Validation requested after dispose"); //$NON-NLS-1$
        }

        // By interrupting the validation thread, we tell it that validation
        // is required. The actual validation will happen some time latter
        validationThread.interrupt();
    }

    // javadoc inherited
    public void addValidationListener(ValidationListener listener) {
        validator.addValidationListener(listener);
    }

    // javadoc inherited
    public void removeValidationListener(ValidationListener listener) {
        validator.removeValidationListener(listener);
    }

    // javadoc unnecessary
    public XMLValidator getValidator() {
        return validator;
    }

    /**
     * Allows the validator to which the asynchronous validator delegates to be
     * changed.
     *
     * @param validator the new validator to delegate to
     */
    public void setValidator(XMLValidator validator) {
        if (validator == null) {
            throw new IllegalArgumentException(
                "A non-null validator is required"); //$NON-NLS-1$
        }

        this.validator = validator;

        // Make sure that any outstanding validation is cancelled
        validationThread.cancelTask();
    }

    /**
     * This <strong>must</strong> be called when the asynchronous validator is
     * no longer required.
     */
    public void dispose() {
        // Cause the thread to terminate
        validationThread.dispose();
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Apr-04	4016/1	allan	VBM:2004031010 DevicePoliciesPart and CategoriesSection.

 27-Feb-04	3242/1	byron	VBM:2003121906 Image Asset Directory Scanning

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 ===========================================================================
*/
