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

package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.xml.pipeline.sax.impl.validation.Event;
import com.volantis.xml.pipeline.sax.impl.validation.Element;
import com.volantis.xml.pipeline.sax.impl.validation.State;

/**
 * Defines the events, elements and states that define the template schema.
 *
 * @todo should use facilities provided by xml-validation subsystem.
 */
public class TemplateSchema {

    // ========================================================================
    //   Model Events
    // ========================================================================

    /**
     * startElement for template:apply
     */
    public static final Event APPLY_START = new Event("APPLY_START");

    /**
     * endElement for template:apply
     */
    public static final Event APPLY_END = new Event("APPLY_END");

    /**
     * startElement for template:bindings
     */
    public static final Event BINDINGS_START = new Event("BINDINGS_START");

    /**
     * endElement for template:bindings
     */
    public static final Event BINDINGS_END = new Event("BINDINGS_END");

    /**
     * startElement for template:binding
     */
    public static final Event BINDING_START = new Event("BINDING_START");

    /**
     * endElement for template:binding
     */
    public static final Event BINDING_END = new Event("BINDING_END");

    /**
     * startElement for template:simpleValue, template:complexValue and for
     * a couple of other places where this event is faked up.
     */
    public static final Event VALUE_START = new Event("VALUE_START");

    /**
     * endElement for template::simpleValue, template:complexValue and for
     * a couple of other places where this event is faked up.
     */
    public static final Event VALUE_END = new Event("VALUE_END");

    /**
     * startElement for template:definition
     */
    public static final Event DEFINITION_START = new Event("DEFINITION_START");

    /**
     * endElement for template:definition
     */
    public static final Event DEFINITION_END = new Event("DEFINITION_END");

    /**
     * startElement for template:declarations
     */
    public static final Event DECLARATIONS_START = new Event("DECLARATIONS_START");

    /**
     * endElement for template:declarations
     */
    public static final Event DECLARATIONS_END = new Event("DECLARATIONS_END");

    /**
     * startElement for template:parameter
     */
    public static final Event PARAMETER_START = new Event("PARAMETER_START");

    /**
     * endElement for template:parameter
     */
    public static final Event PARAMETER_END = new Event("PARAMETER_END");

    /**
     * startElement for template:body
     */
    public static final Event BODY_START = new Event("BODY_START");

    /**
     * endElement for template:body
     */
    public static final Event BODY_END = new Event("BODY_END");

    // ========================================================================
    //   Model Elements
    // ========================================================================

    /**
     * The template:apply element.
     */
    public static final Element APPLY = new Element("apply",
            APPLY_START, APPLY_END, false);

    /**
     * The template:bindings element.
     */
    public static final Element BINDINGS = new Element(
            "bindings", BINDINGS_START, BINDINGS_END, false);

    /**
     * The template:binding element.
     */
    public static final Element BINDING = new Element(
            "binding", BINDING_START, BINDING_END, false);

    /**
     * The template:complexValue element.
     */
    public static final Element COMPLEX_VALUE = new Element(
            "complexValue", VALUE_START, VALUE_END, true);

    /**
     * The template:simpleValue element.
     */
    public static final Element SIMPLE_VALUE = new Element(
            "simpleValue", VALUE_START, VALUE_END, true);

    /**
     * The template:definition element.
     */
    public static final Element DEFINITION = new Element(
            "definition", DEFINITION_START, DEFINITION_END, false);

    /**
     * The template:declarations element.
     */
    public static final Element DECLARATIONS = new Element(
            "declarations", DECLARATIONS_START, DECLARATIONS_END,
            false);

    /**
     * The template:parameter element.
     */
    public static final Element PARAMETER = new Element(
            "parameter", PARAMETER_START, PARAMETER_END, false);

    /**
     * The template:body element.
     */
    public static final Element BODY = new Element("body",
            BODY_START, BODY_END, true);

    // ========================================================================
    //   Model States
    // ========================================================================

    /**
     * The starting state that expects &lt;template:apply&gt;.
     */
    public static final State EXPECT_APPLY_START =
            new State("EXPECT_APPLY_START") {
                public State transition(Event event) {
                    if (event == APPLY_START) {
                        return EXPECT_BINDINGS_START;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;template:bindings&gt;.
     */
    static final State EXPECT_BINDINGS_START =
            new State("EXPECT_BINDINGS_START") {
                public State transition(Event event) {
                    if (event == BINDINGS_START) {
                        return EXPECT_BINDING_START_OR_BINDINGS_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects either &lt;template:binding&gt;, or &lt;/template:bindings&gt;
     */
    static final State EXPECT_BINDING_START_OR_BINDINGS_END =
            new State("EXPECT_BINDING_START_OR_BINDINGS_END") {

                public State transition(Event event) {
                    if (event == BINDING_START) {
                        return EXPECT_BINDING_VALUE_START;
                    } else if (event == BINDINGS_END) {
                        return EXPECT_DEFINITION_START;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects either &lt;template:simpleValue&gt;, or
     * &lt;template:complexValue&gt; within a template:binding element.
     */
    static final State EXPECT_BINDING_VALUE_START =
            new State("EXPECT_BINDING_VALUE_START") {
                public State transition(Event event) {
                    if (event == VALUE_START) {
                        return EXPECT_BINDING_VALUE_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects either &lt;/template:simpleValue&gt;, or
     * &lt;/template:complexValue&gt; within a template:binding element.
     */
    static final State EXPECT_BINDING_VALUE_END =
            new State("EXPECT_BINDING_VALUE_END") {
                public State transition(Event event) {
                    if (event == VALUE_END) {
                        return EXPECT_BINDING_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;/template:binding&gt;.
     */
    static final State EXPECT_BINDING_END =
            new State("EXPECT_BINDING_END") {
                public State transition(Event event) {
                    if (event == BINDING_END) {
                        return EXPECT_BINDING_START_OR_BINDINGS_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;template:definition&gt;.
     */
    static final State EXPECT_DEFINITION_START =
            new State("EXPECT_DEFINITION_START") {
                public State transition(Event event) {
                    if (event == DEFINITION_START) {
                        return EXPECT_DECLARATIONS_OR_BODY_START;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;template:declarations&gt; or &lt;template:body&gt;.
     */
    static final State EXPECT_DECLARATIONS_OR_BODY_START =
            new State("EXPECT_DECLARATIONS_OR_BODY_START") {
                public State transition(Event event) {
                    if (event == DECLARATIONS_START) {
                        return EXPECT_PARAMETER_START_OR_DECLARATIONS_END;
                    } else if (event == BODY_START) {
                        return EXPECT_BODY_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;template:parameter&gt; or &lt;/template:declarations&gt;.
     */
    static final State EXPECT_PARAMETER_START_OR_DECLARATIONS_END =
            new State("EXPECT_PARAMETER_START_OR_DECLARATIONS_END") {
                public State transition(Event event) {
                    if (event == PARAMETER_START) {
                        return EXPECT_PARAMETER_VALUE_START;
                    } else if (event == DECLARATIONS_END) {
                        return EXPECT_BODY_START;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects either &lt;template:simpleValue&gt;, or
     * &lt;template:complexValue&gt; within a template:parameter element.
     */
    static final State EXPECT_PARAMETER_VALUE_START =
            new State("EXPECT_PARAMETER_VALUE_START") {
                public State transition(Event event) {
                    if (event == VALUE_START) {
                        return EXPECT_PARAMETER_VALUE_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects either &lt;/template:simpleValue&gt;, or
     * &lt;/template:complexValue&gt; within a template:parameter element.
     */
    static final State EXPECT_PARAMETER_VALUE_END =
            new State("EXPECT_PARAMETER_VALUE_END") {
                public State transition(Event event) {
                    if (event == VALUE_END) {
                        return EXPECT_PARAMETER_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;/template:parameter&gt;.
     */
    static final State EXPECT_PARAMETER_END =
            new State("EXPECT_PARAMETER_END") {
                public State transition(Event event) {
                    if (event == PARAMETER_END) {
                        return EXPECT_PARAMETER_START_OR_DECLARATIONS_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;template:body&gt;.
     */
    static final State EXPECT_BODY_START =
            new State("EXPECT_BODY_START") {
                public State transition(Event event) {
                    if (event == BODY_START) {
                        return EXPECT_BODY_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;/template:body&gt;.
     */
    static final State EXPECT_BODY_END =
            new State("EXPECT_BODY_END") {
                public State transition(Event event) {
                    if (event == BODY_END) {
                        return EXPECT_DEFINITION_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;/template:definition&gt;.
     */
    static final State EXPECT_DEFINITION_END =
            new State("EXPECT_DEFINITION_END") {
                public State transition(Event event) {
                    if (event == DEFINITION_END) {
                        return EXPECT_APPLY_END;
                    } else {
                        return null;
                    }
                }
            };

    /**
     * Expects &lt;/template:apply&gt;.
     */
    static final State EXPECT_APPLY_END =
            new State("EXPECT_APPLY_END") {
                public State transition(Event event) {
                    if (event == APPLY_END) {
                        return DONE;
                    } else {
                        return null;
                    }
                }
            };

}
