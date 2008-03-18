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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.log;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Level;

/**
 * Type safe enumeration defining our log levels. This maps directly to Log4J
 * log levels. If you wish to add support for another Loggingg mechanism then
 * modify the constructor to take additional objects that correspond to Log
 * Level in that logging mechanism.
 */
public class LogLevel implements Serializable {

    /**
     * Map the names of the created enums to thier instances. Note that the
     * position of the map is important. It must be initialized before the enum
     * instances are created
     */
    private static final Map enums = new HashMap();

    /**
     * An ALL instance
     */
    public static final LogLevel ALL = new LogLevel("ALL", Integer.MIN_VALUE, Level.ALL);

    /**
     * An TRACE instance (Trace does not exist in log4j 1.2.8 so we map it to ALL)
     */
    public static final LogLevel TRACE = new LogLevel("TRACE", 1000, Level.ALL);

    /**
     * An DEBUG instance
     */
    public static final LogLevel DEBUG = new LogLevel("DEBUG", 10000, Level.DEBUG);

    /**
     * An INFO instance
     */
    public static final LogLevel INFO = new LogLevel("INFO", 100000, Level.INFO);

    /**
     * An WARN instance
     */
    public static final LogLevel WARN = new LogLevel("WARN", 1000000, Level.WARN);

    /**
     * An ERROR instance
     */
    public static final LogLevel ERROR = new LogLevel("ERROR", 10000000, Level.ERROR);

    /**
     * An FATAL instance
     */
    public static final LogLevel FATAL = new LogLevel("FATAL", 100000000, Level.FATAL);

    /**
     * An OFF instance
     */
    public static final LogLevel OFF = new LogLevel("OFF", Integer.MAX_VALUE, Level.OFF);

    /**
     * The name of the enumeration entry
     */
    private final String myName;

    /**
     * The rank of the log level. This allows the log levels to be ordered.
     */
    private final transient int rank;

    /**
     * The corresponding Log4j level
     */
    private final transient Level log4jLevel;

    /**
     * Construct a type safe enum instance.
     *
     * @param name the name of the enumeration entry.
     */
    private LogLevel(String name, int rank, Level log4jLevel) {
        myName = name;
        this.rank = rank;
        this.log4jLevel = log4jLevel;
        if (enums.containsKey(name)) {
            throw new IllegalArgumentException(
                "Enum '" + name + "' already exists");
        }
        enums.put(name, this);
    }

    /**
     * Return the name of the enumeration entry
     */
    public String toString() {
        return myName;
    }

    /**
     * Return the rank of this log level
     * @return the rank of this log level
     */
    private int getRank() {
        return rank;
    }

    /**
     * Returns true if this LogLevel is enabled when compared to a threshold
     * log level provided
     *
     * @param logLevel the threshold log level
     * @return true if this LogLevel will be enabled when compared to the
     * provided threshold level
     */
    public boolean isLevelEnabled(LogLevel logLevel) {
        return getRank() >= logLevel.getRank();
    }

    /**
     * Return the LogLevel instance corresponding to the specified name.
     *
     * @param name the name of the instance to return.
     * @throws IllegalArgumentException if an enum does not exist for the
     *                                  specified name.
     */
    public static LogLevel literal(String name) {
        LogLevel result = null;
        if (enums.containsKey(name)) {
            result = (LogLevel) enums.get(name);
        } else {
            throw new IllegalArgumentException(
                "Enum '" + name + "' does not exist");
        }
        return result;
    }

    /**
     * Return the Log4J log level for this LogLevel. This must not be made public.
     *
     * @return the Log4J log level corresponding to this LogLevel.
     */
    Level getLog4jLevel() {
        return log4jLevel;
    }

    /**
     * This stops sub classes from breaking the equals contract
     */
    // Rest of Javadoc inherited
    public final boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * This stops sub classes from breaking the hashcode/equals contract
     */
    // Rest of Javadoc inherited
    public final int hashCode() {
        return super.hashCode();
    }

    // handle deserialization
    Object readResolve() throws ObjectStreamException {
        return literal(myName);
    }

}
