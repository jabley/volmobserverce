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

package com.volantis.xml.pipeline.sax.impl.operations.tryop;

import com.volantis.xml.pipeline.sax.impl.validation.Element;
import com.volantis.xml.pipeline.sax.impl.validation.Event;
import com.volantis.xml.pipeline.sax.impl.validation.State;

/**
 * Defines the events, elements and states that define the try schema.
 *
 * @todo should use facilities provided by xml-validation subsystem.
 */
public class TrySchema {

    // ========================================================================
    //   Model Events
    // ========================================================================

    /**
     * startElement for pipeline:try
     */
    public static final Event TRY_START = new Event("TRY_START");
    /**
     * endElement for pipeline:try
     */
    public static final Event TRY_END = new Event("TRY_END");
    /**
     * startElement for pipeline:preferred
     */
    public static final Event PREFERRED_START = new Event("PREFERRED_START");
    /**
     * endElement for pipeline:preferred
     */
    public static final Event PREFERRED_END = new Event("PREFERRED_END");
    /**
     * startElement for pipeline:alternative
     */
    public static final Event ALTERNATIVE_START = new Event(
            "ALTERNATIVE_START");
    /**
     * endElement for pipeline:alternative
     */
    public static final Event ALTERNATIVE_END = new Event("ALTERNATIVE_END");

    // ========================================================================
    //   Model Elements
    // ========================================================================

    /**
     * The pipeline:try element.
     */
    public static final Element TRY = new Element("try",
            TRY_START, TRY_END, false);
    /**
     * The pipeline:preferred element.
     */
    public static final Element PREFERRED = new Element("preferred",
            PREFERRED_START, PREFERRED_END, true);
    /**
     * The pipeline:alternative element.
     */
    public static final Element ALTERNATIVE = new Element("alternative",
            ALTERNATIVE_START, ALTERNATIVE_END, true);

    // ========================================================================
    //   Model States
    // ========================================================================

    /**
     * The starting state that expects &lt;pipeline:try&gt;.
     */
    public static final State EXPECT_TRY_START =
            new State("EXPECT_TRY_START") {
                public State transition(Event event) {
                    if (event == TRY_START) {
                        return EXPECT_PREFERRED_START;
                    } else {
                        return null;
                    }
                }
            };
    /**
     * Expects &lt;pipeline:preferred&gt;.
     */
    static final State EXPECT_PREFERRED_START =
            new State("EXPECT_PREFERRED_START") {
                public State transition(Event event) {
                    if (event == PREFERRED_START) {
                        return EXPECT_PREFERRED_END;
                    } else {
                        return null;
                    }
                }
            };
    /**
     * Expects &lt;/pipeline:preferred&gt;
     */
    static final State EXPECT_PREFERRED_END =
            new State("EXPECT_PREFERRED_END") {

                public State transition(Event event) {
                    if (event == PREFERRED_END) {
                        return EXPECT_ALTERNATIVE_START_OR_TRY_END;
                    } else {
                        return null;
                    }
                }
            };
    /**
     * Expects &lt;pipeline:alternative&gt; or &lt;/pipeline:try&gt;.
     */
    static final State EXPECT_ALTERNATIVE_START_OR_TRY_END =
            new State("EXPECT_ALTERNATIVE_START_OR_TRY_END") {
                public State transition(Event event) {
                    if (event == ALTERNATIVE_START) {
                        return EXPECT_ALTERNATIVE_END;
                    } else if (event == TRY_END) {
                        return DONE;
                    } else {
                        return null;
                    }
                }
            };
    /**
     * Expects &lt;/pipeline:alternative&gt;.
     */
    static final State EXPECT_ALTERNATIVE_END =
            new State("EXPECT_ALTERNATIVE_END") {
                public State transition(Event event) {
                    if (event == ALTERNATIVE_END) {
                        return EXPECT_ALTERNATIVE_START_OR_TRY_END;
                    } else {
                        return null;
                    }
                }
            };
    /**
     * Expects &lt;/pipeline:try&gt;.
     */
    static final State EXPECT_TRY_END =
            new State("EXPECT_TRY_END") {
                public State transition(Event event) {
                    if (event == TRY_END) {
                        return DONE;
                    } else {
                        return null;
                    }
                }
            };

}
