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
package com.volantis.synergetics.log;

import com.volantis.synergetics.localization.Category;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.Priority;

/**
 * A {@link LogDispatcher} implementation that delegates to Log4j to perfom the
 * logging.
 */
public class Log4jLogDispatcher implements LogDispatcher {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(Log4jLogDispatcher.class);

    /**
     * Fully qualified class name of this class. This is used when delegating
     * to log4j to indicate that this is a wrapper on log4j and this should be
     * taken into account when generating the line numbers, methods etc that
     * have performed the log.
     */
    private static final String FQCN = Log4jLogDispatcher.class.getName();

    /**
     * The key under which the context is stored for Log4J
     */
    public static final String MDC_KEY = "com.volantis.log.dicontext";

    /**
     * The key for the stack
     */
    public static final String MDC_STACK_KEY = MDC_KEY +".stack";

    /**
     * This variable allows access to the thread local storage for the DI
     * context
     */
    private static final ThreadLocal DICONTEXT = new ThreadLocal() {
        // initialize the value of the thread local
        protected synchronized Object initialValue() {
            // stack is synchronized (which does not matter as we have to
            // synchronize it anyway).
            return new Stack();
        }
    };

    /**
     * The prefix used to log
     */
    private static final String DICONTEXT_PREFIX = "com.volantis.log.dicontext";

    /**
     * The Log4j Logger that will be used to perfom logging. Note - not called
     */
    private Logger log4jLogger;

    /**
     * The logger to use for DIContext logging
     */
    private Logger dicontextLogger;

    /**
     * Used to localize messages
     */
    private MessageLocalizer messageLocalizer;

    /**
     * Initalizes a <code>Log4jLogDispatcher<code> instance with the given
     * arguments.
     *
     * @param clientClass        the Class of the client that will be using
     *                           this LogDispatcher
     * @param productIndentifier an identifier used to identify the Volantis
     *                           product that is requiring logging facilities.
     */
    public Log4jLogDispatcher(Class clientClass, String productIndentifier) {
        this.log4jLogger = Logger.getLogger(clientClass);
        this.dicontextLogger = Logger.getLogger(DICONTEXT_PREFIX);
        this.messageLocalizer =
            MessageLocalizer.getLocalizer(clientClass,
                                          productIndentifier);
    }

    // Javadoc inherited
    public void popDIContext() {
        Stack context = (Stack) DICONTEXT.get();
        synchronized(context) {
            if (!context.isEmpty()) {
                String oldName = ((Stack.Context)context.pop()).getContextName();
                if (!context.isEmpty()) {
                    for (int i=context.size()-1; 0==i; i--) {
                        if (context.getContextName(i).equals(oldName)){
                            MDC.put(context.getContextName(i),
                                    context.getContextValue(i));
                        }
                    }
                }
            }
        }
    }

    // Javadoc inherited
    public void pushDIContext(
        String contextName, String context, LogLevel level) {
        if (contextName.indexOf(' ') != -1) {
            throw new RuntimeException(
                EXCEPTION_LOCALIZER.format(
                    "invalid-character", new Character(' ')));
        }
        Stack contextStack = (Stack) DICONTEXT.get();
        synchronized (contextStack) {
            contextStack.push(new Stack.Context(contextName, context, level));
        }
        MDC.put(contextName, context);
    }

    // Javadoc inherited.
    public void popDiagnosticContext() {
        NDC.pop();
    }

    // Javadoc inherited
    public void pushDiagnosticContext(String context) {
        NDC.push(context);
    }

    /**
     * Return a true for if the current contexts log level when compared to the
     * limit log level allows logging. If this method returns true it copies
     * the fullContext into a MDC for Log4J to use. You <b>MUST</b> call
     * {@link #cleanupContexts}
     *
     * @param limit the log level that the context must be above to allow
     * logging.
     * @return true if there is a current context and its log level allows
     * logging when compared to the limit; false otherwise
     */
    private static boolean isEnabledAndMDCInit(LogLevel limit) {
        boolean result = false;
        Stack stack = (Stack) DICONTEXT.get();
        if (!stack.isEmpty()) {
            Stack.Context se = (Stack.Context) stack.peek();
            // put data in the MDC if the top of the stack has a LogLevel
            // that is active.
            if (se.getLevel().isLevelEnabled(limit)) {
                MDC.put(MDC_KEY, se.getFullContext());
                result = true;
            }
        }
        return result;
    }

    /**
     * Clean up the 
     */
    private static void cleanupContexts() {
        MDC.remove(MDC_KEY);
        Stack stack = (Stack) DICONTEXT.get();
    }

    /**
     * Return a true for if the current contexts log level when compared to the
     * limit log level allows logging.
     *
     * @param limit the log level that the context must be above to allow
     * logging.
     * @return true if there is a current context and its log level allows
     * logging when compared to the limit; false otherwise
     */
    private static boolean isEnabled(LogLevel limit) {
        boolean result = false;
        Stack context = (Stack) DICONTEXT.get();
        if (!context.isEmpty()) {
            Stack.Context se = (Stack.Context) context.peek();
            if (se.getLevel().isLevelEnabled(limit)) {
                result = true;
            }
        }
        return result;
    }

    // javadoc inherited
    public void debug(Throwable throwable) {
        log4jLogger.debug(throwable);
    }

    // javadoc inherited
    public void debug(Object message) {
        log4jLogger.log(FQCN, Priority.DEBUG, message, null);
    }

    // javadoc inherited
    public void debug(String message, Throwable throwable) {
        log4jLogger.log(FQCN, Priority.DEBUG, message, throwable);
    }

    // javadoc inherited
    public void info(Throwable throwable) {
        log4jLogger.info(throwable);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.info(throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void info(String key) {
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key),
                        null);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.log(FQCN,
                                Priority.INFO,
                                messageLocalizer.format(Category.INFO, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void info(String key, Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.log(FQCN,
                                Priority.INFO,
                                messageLocalizer.format(Category.INFO, key),
                                throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void info(String key, Object substitutionArg) {
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key,
                                                substitutionArg),
                        null);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.log(FQCN,
                                Priority.INFO,
                                messageLocalizer.format(Category.INFO, key,
                                                        substitutionArg),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void info(String key,
                     Object substitutionArg,
                     Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key,
                                                substitutionArg),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.log(FQCN,
                                Priority.INFO,
                                messageLocalizer.format(Category.INFO, key,
                                                        substitutionArg),
                                throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void info(String key, Object[] substitutionArgs) {
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key,
                                                substitutionArgs),
                        null);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.log(FQCN,
                                Priority.INFO,
                                messageLocalizer.format(Category.INFO, key,
                                                        substitutionArgs),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void info(String key,
                     Object[] substitutionArgs,
                     Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key,
                                                substitutionArgs),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.INFO)) {
            dicontextLogger.log(FQCN,
                                Priority.INFO,
                                messageLocalizer.format(Category.INFO, key,
                                                        substitutionArgs),
                                throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(Throwable throwable) {
        log4jLogger.warn(throwable);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.info(throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(String key) {
        log4jLogger.log(FQCN,
                        Priority.WARN,
                        messageLocalizer.format(Category.WARN, key),
                        null);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.log(FQCN,
                                Priority.WARN,
                                messageLocalizer.format(Category.WARN, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(String key, Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.WARN,
                        messageLocalizer.format(Category.WARN, key),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.log(FQCN,
                                Priority.WARN,
                                messageLocalizer.format(Category.WARN, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(String key, Object substitutionArg) {
        log4jLogger.log(FQCN,
                        Priority.WARN,
                        messageLocalizer.format(Category.WARN, key,
                                                substitutionArg),
                        null);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.log(FQCN,
                                Priority.WARN,
                                messageLocalizer.format(Category.WARN, key),
                                null);;
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(String key,
                     Object substitutionArg,
                     Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.WARN,
                        messageLocalizer.format(Category.WARN, key,
                                                substitutionArg),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.log(FQCN,
                                Priority.WARN,
                                messageLocalizer.format(Category.WARN, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(String key, Object[] substitutionArgs) {
        log4jLogger.log(FQCN,
                        Priority.WARN,
                        messageLocalizer.format(Category.WARN, key,
                                                substitutionArgs),
                        null);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.log(FQCN,
                                Priority.WARN,
                                messageLocalizer.format(Category.WARN, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void warn(String key,
                     Object[] substitutionArgs,
                     Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.WARN,
                        messageLocalizer.format(Category.WARN, key,
                                                substitutionArgs),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.WARN)) {
            dicontextLogger.log(FQCN,
                                Priority.WARN,
                                messageLocalizer.format(Category.WARN, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(Throwable throwable) {
        log4jLogger.error(throwable);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.info(throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(String key) {
        log4jLogger.log(FQCN,
                        Priority.ERROR,
                        messageLocalizer.format(Category.ERROR, key),
                        null);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.log(FQCN,
                                Priority.ERROR,
                                messageLocalizer.format(Category.ERROR, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(String key, Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.ERROR,
                        messageLocalizer.format(Category.ERROR, key),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.log(FQCN,
                                Priority.ERROR,
                                messageLocalizer.format(Category.ERROR, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(String key, Object substitutionArg) {
        log4jLogger.log(FQCN,
                        Priority.ERROR,
                        messageLocalizer.format(Category.ERROR, key,
                                                substitutionArg),
                        null);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.log(FQCN,
                                Priority.ERROR,
                                messageLocalizer.format(Category.ERROR, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(String key,
                      Object substitutionArg,
                      Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.ERROR,
                        messageLocalizer.format(Category.ERROR, key,
                                                substitutionArg),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.log(FQCN,
                                Priority.ERROR,
                                messageLocalizer.format(Category.ERROR, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(String key, Object[] substitutionArgs) {
        log4jLogger.log(FQCN,
                        Priority.ERROR,
                        messageLocalizer.format(Category.ERROR, key,
                                                substitutionArgs),
                        null);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.log(FQCN,
                                Priority.ERROR,
                                messageLocalizer.format(Category.ERROR, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void error(String key,
                      Object[] substitutionArgs,
                      Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.ERROR,
                        messageLocalizer.format(Category.ERROR, key,
                                                substitutionArgs),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.ERROR)) {
            dicontextLogger.log(FQCN,
                                Priority.ERROR,
                                messageLocalizer.format(Category.ERROR, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(Throwable throwable) {
        log4jLogger.fatal(throwable);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.info(throwable);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(String key) {
        log4jLogger.log(FQCN,
                        Priority.FATAL,
                        messageLocalizer.format(Category.FATAL, key),
                        null);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.log(FQCN,
                                Priority.FATAL,
                                messageLocalizer.format(Category.FATAL, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(String key, Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.FATAL,
                        messageLocalizer.format(Category.FATAL, key),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.log(FQCN,
                                Priority.FATAL,
                                messageLocalizer.format(Category.FATAL, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(String key, Object substitutionArg) {
        log4jLogger.log(FQCN,
                        Priority.FATAL,
                        messageLocalizer.format(Category.FATAL, key,
                                                substitutionArg),
                        null);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.log(FQCN,
                                Priority.FATAL,
                                messageLocalizer.format(Category.FATAL, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(String key,
                      Object substitutionArg,
                      Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.FATAL,
                        messageLocalizer.format(Category.FATAL, key,
                                                substitutionArg),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.log(FQCN,
                                Priority.FATAL,
                                messageLocalizer.format(Category.FATAL, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(String key, Object[] substitutionArgs) {
        log4jLogger.log(FQCN,
                        Priority.FATAL,
                        messageLocalizer.format(Category.FATAL, key,
                                                substitutionArgs),
                        null);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.log(FQCN,
                                Priority.FATAL,
                                messageLocalizer.format(Category.FATAL, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public void fatal(String key,
                      Object[] substitutionArgs,
                      Throwable throwable) {
        log4jLogger.log(FQCN,
                        Priority.FATAL,
                        messageLocalizer.format(Category.FATAL, key,
                                                substitutionArgs),
                        throwable);
        if (isEnabledAndMDCInit(LogLevel.FATAL)) {
            dicontextLogger.log(FQCN,
                                Priority.FATAL,
                                messageLocalizer.format(Category.FATAL, key),
                                null);
            cleanupContexts();
        }
    }

    // javadoc inherited
    public boolean isDebugEnabled() {

        return log4jLogger.isEnabledFor(Priority.DEBUG);
    }

    // javadoc inherited
    public boolean isInfoEnabled() {
        return log4jLogger.isEnabledFor(Priority.INFO) ||
            isEnabled(LogLevel.INFO);
    }

    // javadoc inherited
    public boolean isWarnEnabled() {
        return log4jLogger.isEnabledFor(Priority.WARN) ||
            isEnabled(LogLevel.WARN);
    }

    // javadoc inherited
    public boolean isErrorEnabled() {
        return log4jLogger.isEnabledFor(Priority.ERROR) ||
            isEnabled(LogLevel.ERROR);
    }

    // javadoc inherited
    public boolean isFatalEnabled() {
        return log4jLogger.isEnabledFor(Priority.FATAL) ||
            isEnabled(LogLevel.ERROR);
    }

    // javadoc inherited
    public void logStandardHeader(String key) {

        StringBuffer jvmProperties = new StringBuffer(5000);
        Properties properties = System.getProperties();
        for (Enumeration keys = properties.propertyNames();
             keys.hasMoreElements();) {
            String name = (String) keys.nextElement();
            String value = properties.getProperty(name);
            jvmProperties.append(name)
                .append('=')
                .append(value)
                .append(System.getProperty("line.separator"));
        }
        Object[] args = new Object[]{new Date(), jvmProperties.toString()};
        log4jLogger.log(FQCN,
                        Priority.INFO,
                        messageLocalizer.format(Category.INFO, key, args),
                        null);
        // we don't log this to the DIContext logger as it is not normally
        // active when this is output
    }    
}



/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	354/1	doug	VBM:2004120202 Fixed a number of minor logging issues

 29-Nov-04	343/19	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/17	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/15	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/13	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/11	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/9	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	343/5	doug	VBM:2004111702 Refactoring of logging framework

 18-Nov-04	343/3	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
