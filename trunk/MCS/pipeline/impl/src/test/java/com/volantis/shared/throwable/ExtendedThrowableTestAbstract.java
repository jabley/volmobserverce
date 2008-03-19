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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.throwable;

import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class ExtendedThrowableTestAbstract
        extends TestCaseAbstract {

    private final Class exceptionClass;

    protected ExtendedThrowableTestAbstract(Class exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    protected String checkException(
            Throwable throwable,
            final String message) {
        String stackTrace = getStackTrace(throwable);

        String prefix = exceptionClass.getName() +
                (message == null ? "" : ": " + message);
        assertTrue("Stack trace doesn't start with the expected value of '" +
                prefix + "' but rather is " + stackTrace,
                stackTrace.startsWith(prefix));
        assertEquals("message not as expected", message,
                throwable.getMessage());

        return stackTrace;
    }

    protected String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        String s = sw.toString();
        return s;
    }

    protected Throwable createExceptionCause() {
        return new MyException("I am the cause");
    }

    protected void checkCause(
            Throwable throwable, final Class exceptionClass,
            final String message) {

        String stackTrace = checkException(throwable, message);

        String causeLine = MyException.class.getName() + ":" +
                " I am the cause";
        assertTrue("Cause not found in " + stackTrace,
                stackTrace.indexOf(causeLine) != -1);
    }

    /*
     * Test for no arg constructor.
     */
    public void testExtendedException() {
        try {
            Throwable t = createException();
            throw t;
        } catch (Throwable e) {
            checkException(e, null);
        }
    }

    /*
     * Test for (String) constructor.
     */
    public void testExtendedExceptionString() {
        try {
            Throwable t = createException("test");
            throw t;
        } catch (Throwable e) {
            checkException(e, "test");
        }
    }

    /*
     * Test for (String, Throwable) constructor
     */
    public void testExtendedExceptionStringThrowable() {
        try {
            Throwable t = createCausedException("test");
            throw t;
        } catch (Throwable e) {
            checkCause(e, ExtendedException.class, "test");
        }
    }

    /*
     * Test for (Throwable) constructor
     */
    public void testExtendedExceptionThrowable() {
        try {
            Throwable t = createCausedException();
            throw t;
        } catch (Throwable e) {
            checkCause(e, ExtendedException.class, "I am the cause");
        }
    }

    /**
     * Create an exception with no message.
     */
    protected abstract Throwable createException();

    /**
     * Create an exception with a message.
     */
    protected abstract Throwable createException(String s);

    /**
     * Create an exception with a message and a cause.
     */
    protected abstract Throwable createCausedException(String s);

    /**
     * Create an exception with a message and a cause.
     */
    protected abstract Throwable createCausedException(String s, Throwable t);

    /**
     * Create an exception with no message but with a cause.
     */
    protected abstract Throwable createCausedException();

    /**
     * Tests the getCause() method.
     * @throws Exception if an error occurs
     */
    public void testGetCause() throws Exception {
        // construct a Throwable with a root cause
        Exception rootCause = new Exception();

        Throwable t = createCausedException("message", rootCause);

        assertEquals("Root cause should be the throwable passed in on " +
                     "construction ", rootCause,
                ((ThrowableExtensions) t).getCause());
    }

    /**
     * Tests the getCause() method.
     * @throws Exception if an error occurs
     */
    public void testGetCauseWhenNoCause() throws Exception {
        Throwable t = createCausedException("message", null);

        assertNull("Root cause should be null", t.getCause());
    }

    private class MyException
            extends Exception {

        public MyException() {
            super();
        }

        public MyException(String s) {
            super(s);
        }
    }
}
