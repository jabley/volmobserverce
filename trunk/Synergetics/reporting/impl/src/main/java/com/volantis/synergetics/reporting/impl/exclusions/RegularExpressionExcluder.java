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
 * (c) Copyright Volantis Systems Ltd. 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.reporting.impl.exclusions;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.reporting.config.MetricExclusion;

/**
 * Exclude report event when metric value matches regular expression
 *
 */
public class RegularExpressionExcluder implements EventExcluder {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(RegularExpressionExcluder.class);

    /**
     * metric name
     */
    private String metric;

    /**
     * regular expression string
     */
    private String expression;

    /**
     * ignore case matching
     */
    private boolean ignoreCase;

    /**
     * Precompiled pattern
     */
    private Pattern pattern;

    /**
     * set to true if regular expression pattern compiled successfully
     */
    private boolean initialized;

    /**
     * Constructor
     * @param metric String
     * @param value String
     * @param ignoreCase boolean
     */
    public RegularExpressionExcluder(String metric, String expression, boolean ignoreCase) {
        this.metric = metric;
        this.initialized = false;
        this.expression = expression;
        this.ignoreCase = ignoreCase;
        try {
            if(ignoreCase) {
                this.pattern = Pattern.compile(expression,
                        Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE );
            } else {
                this.pattern = Pattern.compile(expression);
            }
            this.initialized = true;
        } catch(PatternSyntaxException ex) {
            this.initialized = false;
            LOGGER.error("exclusion-regexp-error", expression);
        }
    }

    /**
     * Constructor
     * @param configuration MetricExclusion
     */
    public RegularExpressionExcluder(MetricExclusion configuration) {
        this(configuration.getMetricName(),
             configuration.getValue(),
             configuration.getIgnoreCase().booleanValue());
    }

    // javadoc inherited
    public boolean isExcluded(Map metrics) {
        boolean excluded = false;
        Object obj = metrics.get(metric);
        if(obj != null) {
            if(initialized) {
                Matcher matcher = pattern.matcher(obj.toString());
                excluded = matcher.matches();
            }
        }
        return excluded;
    }

    // javadoc inherited
    public boolean isEqual(Object object) {
        if ( this == object ) {
            return true;
        }

        if ( !(object instanceof RegularExpressionExcluder) ) {
            return false;
        }
        RegularExpressionExcluder excluder = (RegularExpressionExcluder)object;

        return
          this.metric.equals(excluder.metric) &&
          this.expression.equals(excluder.expression) &&
          (this.ignoreCase == excluder.ignoreCase) ;
      }
}
