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

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Tests the {@link AsynchronousXMLValidator}. Note that tests using the
 * {@link #validator} member do not need to remember to call
 * {@link AsynchronousXMLValidator#dispose} since this is automatically done in
 * {@link #tearDown}.
 */
public class AsynchronousXMLValidatorTestCase extends TestCaseAbstract {
    /**
     * A testing centric dummy implementation of the ValidationListener
     * interface.
     */
    protected static class DummyListener implements ValidationListener {
        // javadoc inherited
        public void validated() {
        }
    }

    /**
     * A testing centric implementation of the XMLValidator interface.
     */
    protected static class CountingValidator implements XMLValidator {
        /**
         * Counts the number of calls to {@link #validate}.
         */
        private int validationCount;

        /**
         * Counts the number of calls to {@link #addValidationListener} and
         * {@link #removeValidationListener}.
         */
        private int listenerCount;

        // javadoc unnecessary
        public CountingValidator() {
            reset();
        }

        /**
         * Resets the counters
         */
        public void reset() {
            validationCount = 0;
            listenerCount = 0;
        }

        // javadoc unnecessary
        public int getValidationCount() {
            return validationCount;
        }

        // javadoc unnecessary
        public int getListenerCount() {
            return listenerCount;
        }

        // javadoc inherited
        public void validate() {
            validationCount++;
        }

        // javadoc inherited
        public void addValidationListener(ValidationListener listener) {
            listenerCount++;
        }

        // javadoc inherited
        public void removeValidationListener(ValidationListener listener) {
            listenerCount--;
        }
    }

    /**
     * For use by tests that actually use an {@link AsynchronousXMLValidator}.
     */
    protected AsynchronousXMLValidator validator;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        validator = null;
    }

    // javadoc inherited
    protected void tearDown() throws Exception {
        if (validator != null) {
            validator.dispose();
        }

        super.tearDown();
    }

    /**
     * Supporting method that permits specialist tests to change the actual
     * type of validator created.
     *
     * @param delegate the validator to be delegated to
     * @return the validator under test
     */
    protected AsynchronousXMLValidator createValidator(XMLValidator delegate) {
        return new AsynchronousXMLValidator(delegate);
    }

    /**
     * Supporting method that permits specialist tests to change the actual
     * type of validator created.
     *
     * @param delegate the validator to be delegated to
     * @param delay    the number of milliseconds delay expected before
     *                 validation is performed on the delegate
     * @return the validator under test
     */
    protected AsynchronousXMLValidator createValidator(XMLValidator delegate,
                                                       int delay) {
        return new AsynchronousXMLValidator(delegate, delay);
    }

    // javadoc unnecessary
    public void testAddValidationListener() throws Exception {
        CountingValidator counter = new CountingValidator();
        ValidationListener listener = new DummyListener();

        validator = createValidator(counter);

        validator.addValidationListener(listener);

        assertEquals("Count of added listeners not as",
                     1,
                     counter.getListenerCount());
    }

    // javadoc unnecessary
    public void testRemoveValidationListener() throws Exception {
        CountingValidator counter = new CountingValidator();
        ValidationListener listener = new DummyListener();

        validator = createValidator(counter);

        validator.removeValidationListener(listener);

        assertEquals("Count of removed listeners not as",
                     -1,
                     counter.getListenerCount());
    }

    public void testValidate() throws Exception {
        CountingValidator counter = new CountingValidator();
        Object monitor = new Object();

        final int TIMEOUT = 500;

        // Validation should happen 500 milliseconds after a call to validate,
        // where the timeout is re-started on each call to validate
        validator = createValidator(counter, TIMEOUT);

        validator.validate();

        assertEquals("Unexpected validation invocation [1]",
                     0,
                     counter.getValidationCount());

        validator.validate();

        assertEquals("Unexpected validation invocation [2]",
                     0,
                     counter.getValidationCount());

        // Pause for a while, but not as long as the timeout, above
        synchronized(monitor) {
            monitor.wait(TIMEOUT / 3 * 2);
        }

        validator.validate();

        assertEquals("Unexpected validation invocation [3]",
                     0,
                     counter.getValidationCount());

        // Pause for a while, but not as long as the timeout, above
        synchronized (monitor) {
            monitor.wait(TIMEOUT / 3 * 2);
        }

        validator.validate();

        // Pause for a while, but not as long as the timeout, above. Note that
        // the accumulated pause time is now longer than the timeout, checking
        // therefore that the timeout restarts on each validate call
        synchronized (monitor) {
            monitor.wait(TIMEOUT / 3 * 2);
        }

        assertEquals("Unexpected validation invocation [4]",
                     0,
                     counter.getValidationCount());

        // Wait for a whole timeout (plus the wait above)
        synchronized (monitor) {
            monitor.wait(TIMEOUT);
        }

        assertEquals("Validation invocation count not as",
                     1,
                     counter.getValidationCount());
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 ===========================================================================
*/
