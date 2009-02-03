/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;

import com.volantis.testtools.mock.expectations.Report;
import com.volantis.testtools.mock.EventEffect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Stack;

/**
 * Used for creating reports from within the mock framework.
 */
public class ReportImpl implements Report {

    private Object marker;

    private IndentingWriter currentWriter;

    private final Stack indentingWriters;

    private final Stack stringWriters;

    ReportImpl(Writer out) {
        indentingWriters = new Stack();
        stringWriters = new Stack();
        currentWriter = new IndentingWriter(out);
    }

    public ReportImpl() {
        this(new StringWriter());
    }

    public Report append(int i) {
        currentWriter.flush();
        currentWriter.print(i);
        return this;
    }

    public Report append(String s) {
        currentWriter.flush();
        currentWriter.print(s);
        return this;
    }

    public Report append(Object o) {
        currentWriter.flush();
        currentWriter.print(o);
        return this;
    }

    public PrintWriter getPrintWriter() {
        return currentWriter;
    }

    public void addStackTrace() {
        Throwable t = new Throwable();
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        t.printStackTrace(printer);

        append(writer.getBuffer().toString());
    }

    public Object getMarker() {
        return marker;
    }

    public void setMarker(Object marker) {
        this.marker = marker;
    }

    public void beginBlock() {
        currentWriter.beginBlock();
    }

    public void endBlock() {
        currentWriter.endBlock();
    }

    // javadoc inherited
    public void startExpectation() {
        synchronized(this) {
            StringWriter out = new StringWriter();
            indentingWriters.push(currentWriter);
            currentWriter = new IndentingWriter(out);
            stringWriters.push(out);
        }
    }

    // javadoc inherited
    public void handleEventEffect(EventEffect effect) {
        synchronized(this) {
            if (effect == com.volantis.testtools.mock.EventEffect.MATCHED_EXPECTATION) {
                // This should never happen when verifying.
            } else if (effect == EventEffect.WOULD_FAIL) {
                currentWriter.println("[failed verification]");
            } else if (effect == EventEffect.WOULD_SATISFY) {

                // Don't bother reporting success.
//                currentWriter.println("[satisfied verification]");
                clearCurrentStringWriter();
            } else {
                throw new IllegalStateException(
                        "Unknown event effect: " + effect);
            }
            currentWriter.flush();
            currentWriter = (IndentingWriter) indentingWriters.pop();
            currentWriter.print(stringWriters.pop().toString());
        }
    }

    private void clearCurrentStringWriter() {
        stringWriters.pop();
        stringWriters.push(new StringWriter(0));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Jun-05	7997/3	pduffin	VBM:2005050324 Simplified internals of mock framework to make them easier to understand, and also as a consequence slightly more performant. Also added support for repeating groups of expectations in the same way as repeating individual expectations

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 ===========================================================================
*/
