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

/**
 * Class that allows clients to perform logging.
 */
public interface LogDispatcher {

    /**
     * A constant used for adding session information to the logging context
     */
    public static final String SESSION = "com.volantis.log.context.session";

    /**
     * A constant used for adding product information to the logging context
     */
    public static final String PRODUCT = "com.volantis.log.context.product";

    /**
     * A constant used for adding service information to the logging context
     */
    public static final String SERVICE = "com.volantis.log.context.service";

    /**
     * A constant used for adding thread information to the logging context
     */
    public static final String THREAD = "com.volantis.log.context.thread";

    /**
     * Push a Developer Information context. DI contexts allow logging to be
     * enabled at a specific level for just the specified context. This will
     * not affect normal logging. It is only used by the secondary logging
     * system. Calls to this method should be matched by calls to popDIContext.
     *
     * @param contextName a name to associate with this context. This MUST NOT
     * contain spaces.
     * @param context a string description of the context being pushed.
     * @param level the logging level you want this context to use.
     */
    public void pushDIContext(String contextName,
                              String context, LogLevel level);

    /**
     * Pop the current Developer Information Context from the "stack". This
     * method should be paired with calls to pushDIContext. This method should
     * be called from within a finally block.
     */
    public void popDIContext();

    /**
     * Push a diagnostic context onto stack associated with the current thread.
     * Diagnostic contexts allow information such as thread ID or user to be
     * added to standard logging output.
     *
     * @param context the context to add
     */
    public void pushDiagnosticContext(String context);

    /**
     * Pop the last diagnostic context from the stack associated with the
     * current thread.
     */
    public void popDiagnosticContext();

    /**
     * Logs this given {@link Throwable} at the debug level.
     *
     * @param throwable the Throwable to log
     */
    public void debug(Throwable throwable);

    /**
     * Log a message at the DEBUG level.
     *
     * @param message the Object to log. Implementations of this method are
     *                free to format the Object as they wish.
     */
    public void debug(Object message);

    /**
     * Log a message at the DEBUG level including the stack trace of the {@link
     * Throwable} parameter.
     *
     * @param message   the message to log
     * @param throwable the Throwable to log
     */
    public void debug(String message, Throwable throwable);

    /**
     * Logs this given {@link Throwable} at the info level.
     *
     * @param throwable the Throwable to log
     */
    public void info(Throwable throwable);

    /**
     * Log a localized message at the INFO level.
     *
     * @param key the key that will be used to retrieve the localized message
     *            from the associated ResourceBundle
     */
    public void info(String key);

    /**
     * Log a localized message at the INFO level including the stack trace of
     * the {@link Throwable} parameter.
     *
     * @param key       the key that will be used to retrieve the localized
     *                  message from the associated ResourceBundle
     * @param throwable the Throwable to log
     */
    public void info(String key, Throwable throwable);

    /**
     * Log a localized message at the INFO level using the substitution
     * argument when formatting the message.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     */
    public void info(String key, Object substitutionArg);

    /**
     * Log a localized message at the INFO level using the substitution
     * argument when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     * @param throwable       the Throwable to log
     */
    public void info(String key,
                     Object substitutionArg,
                     Throwable throwable);

    /**
     * Log a localized message at the INFO level using the substitution
     * arguments when formatting the message.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     */
    public void info(String key, Object[] substitutionArgs);

    /**
     * Log a localized message at the INFO level using the substitution
     * arguments when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     * @param throwable        the Throwable to log
     */
    public void info(String key,
                     Object[] substitutionArgs,
                     Throwable throwable);

    /**
     * Logs this given {@link Throwable} at the warn level.
     *
     * @param throwable the Throwable to log
     */
    public void warn(Throwable throwable);

    /**
     * Log a localized message at the WARN level.
     *
     * @param key the key that will be used to retrieve the localized message
     *            from the associated ResourceBundle
     */
    public void warn(String key);

    /**
     * Log a localized message at the WARN level including the stack trace of
     * the {@link Throwable} parameter.
     *
     * @param key       the key that will be used to retrieve the localized
     *                  message from the associated ResourceBundle
     * @param throwable the Throwable to log
     */
    public void warn(String key, Throwable throwable);

    /**
     * Log a localized message at the WARN level using the substitution
     * argument when formatting the message.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     */
    public void warn(String key, Object substitutionArg);

    /**
     * Log a localized message at the WARN level using the substitution
     * argument when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     * @param throwable       the Throwable to log
     */
    public void warn(String key,
                     Object substitutionArg,
                     Throwable throwable);

    /**
     * Log a localized message at the WARN level using the substitution
     * arguments when formatting the message.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     */
    public void warn(String key, Object[] substitutionArgs);

    /**
     * Log a localized message at the WARN level using the substitution
     * arguments when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     * @param throwable        the Throwable to log
     */
    public void warn(String key,
                     Object[] substitutionArgs,
                     Throwable throwable);

    /**
     * Logs this given {@link Throwable} at the error level.
     *
     * @param throwable the Throwable to log
     */
    public void error(Throwable throwable);

    /**
     * Log a localized message at the ERROR level.
     *
     * @param key the key that will be used to retrieve the localized message
     *            from the associated ResourceBundle
     */
    public void error(String key);

    /**
     * Log a localized message at the ERROR level including the stack trace of
     * the {@link Throwable} parameter.
     *
     * @param key       the key that will be used to retrieve the localized
     *                  message from the associated ResourceBundle
     * @param throwable the Throwable to log
     */
    public void error(String key, Throwable throwable);

    /**
     * Log a localized message at the ERROR level using the substitution
     * argument when formatting the message.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     */
    public void error(String key, Object substitutionArg);

    /**
     * Log a localized message at the ERROR level using the substitution
     * argument when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     * @param throwable       the Throwable to log
     */
    public void error(String key,
                      Object substitutionArg,
                      Throwable throwable);

    /**
     * Log a localized message at the ERROR level using the substitution
     * arguments when formatting the message.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     */
    public void error(String key, Object[] substitutionArgs);

    /**
     * Log a localized message at the ERROR level using the substitution
     * arguments when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     * @param throwable        the Throwable to log
     */
    public void error(String key,
                      Object[] substitutionArgs,
                      Throwable throwable);

    /**
     * Logs this given {@link Throwable} at the fatal level.
     *
     * @param throwable the Throwable to log
     */
    public void fatal(Throwable throwable);

    /**
     * Log a localized message at the FATAL level.
     *
     * @param key the key that will be used to retrieve the localized message
     *            from the associated ResourceBundle
     */
    public void fatal(String key);

    /**
     * Log a localized message at the FATAL level including the stack trace of
     * the {@link Throwable} parameter.
     *
     * @param key       the key that will be used to retrieve the localized
     *                  message from the associated ResourceBundle
     * @param throwable the Throwable to log
     */
    public void fatal(String key, Throwable throwable);

    /**
     * Log a localized message at the FATAL level using the substitution
     * argument when formatting the message.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     */
    public void fatal(String key, Object substitutionArg);

    /**
     * Log a localized message at the FATAL level using the substitution
     * argument when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key             the key that will be used to retrieve the
     *                        localized message from an associated
     *                        ResourceBundle
     * @param substitutionArg will be used as a substitution parameter when
     *                        formatting the localized message
     * @param throwable       the Throwable to log
     */
    public void fatal(String key,
                      Object substitutionArg,
                      Throwable throwable);

    /**
     * Log a localized message at the FATAL level using the substitution
     * arguments when formatting the message.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     */
    public void fatal(String key, Object[] substitutionArgs);

    /**
     * Log a localized message at the FATAL level using the substitution
     * arguments when formatting the message. Includes the stack trace of the
     * {@link Throwable} parameter in the message that is logged.
     *
     * @param key              the key that will be used to retrieve the
     *                         localized message from an associated
     *                         ResourceBundle
     * @param substitutionArgs will be used as the substitution parameters when
     *                         formatting the localized message
     * @param throwable        the Throwable to log
     */
    public void fatal(String key,
                      Object[] substitutionArgs,
                      Throwable throwable);

    /**
     * Returns true if and only if the DEBUG logging level is enabled.
     *
     * @return true if and only if the DEBUG logging level is enabled.
     */
    public boolean isDebugEnabled();

    /**
     * Returns true if and only if the INFO logging level is enabled.
     *
     * @return true if and only if the INFO logging level is enabled.
     */
    public boolean isInfoEnabled();

    /**
     * Returns true if and only if the WARN logging level is enabled.
     *
     * @return true if and only if the WARN logging level is enabled.
     */
    public boolean isWarnEnabled();

    /**
     * Returns true if and only if the ERROR logging level is enabled.
     *
     * @return true if and only if the ERROR logging level is enabled.
     */
    public boolean isErrorEnabled();

    /**
     * Returns true if and only if the FATAL logging level is enabled.
     *
     * @return true if and only if the FATAL logging level is enabled.
     */
    public boolean isFatalEnabled();

    /**
     * Helper method that will write out a standard log file header. The key
     * parameter will be used to retrieve the header message from the
     * associated message bundle.
     *
     * @param key the key to the header message.
     */
    public void logStandardHeader(String key);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	354/1	doug	VBM:2004120202 Fixed a number of minor logging issues

 29-Nov-04	343/15	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/13	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/11	doug	VBM:2004111702 Refactored Logging framework

 24-Nov-04	343/9	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	343/7	doug	VBM:2004111702 Refactoring of logging framework

 18-Nov-04	343/5	doug	VBM:2004111702 Refactoring of logging framework

 18-Nov-04	343/3	doug	VBM:2004111702 Refactoring of logging framework

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
