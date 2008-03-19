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
 * $Header: /src/voyager/com/volantis/mcs/papi/PAPIInternals.java,v 1.15 2003/03/26 11:43:44 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Doug            VBM:2001121701 - Created.
 * 31-Jan-02    Paul            VBM:2001122105 - Renamed from PAPIClassFactory.
 * 20-Feb-02    Steve           VBM:2002021404 - Creation of a FormFragment
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 06-Aug-02    Paul            VBM:2002080509 - Added method to initialise
 *                              canvas specific event attributes.
 * 15-Aug-02    Paul            VBM:2002081421 - Form fields now all support
 *                              the general events so made sure that they
 *                              are initialised propertly.
 * 02-Sep-02    Sumit           VBM:2002030703 Added createPaneFormat() to
 *                              create a PaneFormat from a PaneInstance.
 * 24-Jan-03    Sumit           VBM:2003011613 - In initFocusEventAttr() added
 *                              conditional to add accesskey attr 
 * 25-Feb-03    Byron           VBM:2003022105 - Added associateEvent(...) and
 *                              replaced same code with call to this method
 *                              throughout the source. Modified
 *                              initialiseFieldEventAttributes. Cleaned up
 *                              javadocs and unused variables.
 * 11-Mar-03    Steve           VBM:2003022403 - Added public api doclet tags
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 21-Mar-03    Byron           VBM:2003031907 - Added createDissectingPane
 *                              method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.papi.impl;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.papi.CanvasAttributes;
import com.volantis.mcs.papi.EventAttributes;
import com.volantis.mcs.papi.FocusAttributes;
import com.volantis.mcs.papi.FormFragment;
import com.volantis.mcs.papi.Fragment;
import com.volantis.mcs.papi.PageAttributes;
import com.volantis.mcs.papi.PaneFormat;
import com.volantis.mcs.papi.XFFormAttributes;
import com.volantis.mcs.papi.XFFormFieldAttributes;
import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.MCSAttributes;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.protocols.layouts.FormFragmentInstance;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.FragmentInstance;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Class that is used to access the internals of PAPI from other volantis
 * classes, some of which could be outside this package. It also contains
 * some helper methods.
 */
public class PAPIInternals {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(PAPIInternals.class);

    /**
     * Factory method to create a com.volantis.mcs.papi.Fragment object
     * <p>
     * The Fragment constructor has an access level of package because we
     * cannot expose the FragmentInstance class that the constructor requires
     * to the Public API so we need this method for the MarinerRequestContext
     * to create Fragments.
     * </p>
     *
     * @param fragmentInstance the corresponding FragmentInstance object.
     * @return the Fragment object.
     */
    static public Fragment createFragment(FragmentInstance fragmentInstance) {
        return new FragmentImpl(fragmentInstance);
    }

    /**
     * Factory method to create a com.volantis.mcs.papi.Fragment object
     * <p>
     * The Fragment constructor has an access level of package because we
     * cannot expose the FragmentInstance class that the constructor requires
     * to the Public API so we need this method for the MarinerRequestContext
     * to create Fragments.
     * </p>
     *
     * @param fragmentInstance the corresponding FragmentInstance object.
     * @return the Fragment object.
     */
    static public FormFragment createFormFragment(
            FormFragmentInstance fragmentInstance) {
        return new FormFragmentImpl(fragmentInstance);
    }

    /**
     * This method creates a DissectingPane object (which may not be created
     * from outside its package due to the fact that the constructor is package
     * protected). In other words, without this method, the DissectingPane
     * object cannot be created from within the public API.
     *
     * @param instance the DissectingPaneInstance
     * @return the newly created DissectingPane object (always creates
     *         a new object).
     * @see DissectingPaneImpl
     */
    static public DissectingPaneImpl createDissectingPane(
            DissectingPaneInstance instance) {
        return new DissectingPaneImpl(instance);
    }

    /**
     * Factory method to create a com.volantis.mcs.papi.PaneFormat object
     *
     * @param formatInstance the corresponding FormatInstance object.
     * @return the PaneFormat object.
     */
    static public PaneFormat createPaneFormat(FormatInstance formatInstance) {
        return new PaneFormatImpl(formatInstance);
    }


    /**
     * Initialise the general event attributes in the given protocol
     * attributes.
     *
     * @param pageContext    The context within which the mariner expressions
     *                       are resolved.
     * @param papiAttributes The PAPI attributes which are to be used to
     *                       initialise the protocol attributes.
     * @param attributes     The protocol attributes object to initialise.
     */
    static public void initialiseGeneralEventAttributes(
            MarinerPageContext pageContext,
            EventAttributes papiAttributes,
            MCSAttributes attributes) {

        com.volantis.mcs.protocols.EventAttributes pattributes = null;

        String value;

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising general event attributes");
        }

        if ((value = papiAttributes.getOnClick()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_CLICK, value);
        }

        if ((value = papiAttributes.getOnDoubleClick()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_DOUBLE_CLICK, value);
        }

        if ((value = papiAttributes.getOnKeyDown()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_KEY_DOWN, value);
        }

        if ((value = papiAttributes.getOnKeyPress()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_KEY_PRESS, value);
        }

        if ((value = papiAttributes.getOnKeyUp()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_KEY_UP, value);
        }

        if ((value = papiAttributes.getOnMouseDown()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_MOUSE_DOWN, value);
        }

        if ((value = papiAttributes.getOnMouseMove()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_MOUSE_MOVE, value);
        }

        if ((value = papiAttributes.getOnMouseOut()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_MOUSE_OUT, value);
        }

        if ((value = papiAttributes.getOnMouseOver()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_MOUSE_OVER, value);
        }

        if ((value = papiAttributes.getOnMouseUp()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_MOUSE_UP, value);
        }
    }

    /**
     * Initialise the field event attributes in the given protocol attributes.
     *
     * @param pageContext    The context within which the mariner expressions
     *                       are resolved.
     * @param papiAttributes The PAPI attributes which are to be used to
     *                       initialise the protocol attributes.
     * @param attributes     The protocol attributes object to initialise.
     */
    static public void initialiseFieldEventAttributes(
            MarinerPageContext pageContext,
            XFFormFieldAttributes papiAttributes,
            MCSAttributes attributes) {

        com.volantis.mcs.protocols.EventAttributes pattributes = null;

        String value;

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising field event attributes");

            // All fields have general event attributes so initialise them first.

        }
        initialiseGeneralEventAttributes(pageContext, papiAttributes,
                attributes);

        // All fields have focus event attributes so initialise them next.
        initialiseFocusEventAttributes(pageContext, papiAttributes, attributes);

        if ((value = papiAttributes.getOnChange()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_CHANGE, value);
        }

        if ((value = papiAttributes.getOnSelect()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_SELECT, value);
        }
    }


    /**
     * Initialise the focus event attributes in the given protocol attributes.
     *
     * @param pageContext    The context within which the mariner expressions
     *                       are resolved.
     * @param papiAttributes The PAPI attributes which are to be used to
     *                       initialise the protocol attributes.
     * @param attributes     The protocol attributes object to initialise.
     */
    static public void initialiseFocusEventAttributes(
            MarinerPageContext pageContext,
            FocusAttributes papiAttributes,
            MCSAttributes attributes) {

        com.volantis.mcs.protocols.EventAttributes pattributes = null;

        String value;

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising focus event attributes");
        }

        if ((value = papiAttributes.getOnBlur()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_BLUR, value);
        }

        if ((value = papiAttributes.getOnFocus()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_FOCUS, value);
        }
    }

    /**
     * Initialise the form event attributes in the given protocol attributes.
     *
     * @param pageContext    The context within which the mariner expressions
     *                       are resolved.
     * @param papiAttributes The PAPI attributes which are to be used to
     *                       initialise the protocol attributes.
     * @param attributes     The protocol attributes object to initialise.
     */
    static public void initialiseFormEventAttributes(
            MarinerPageContext pageContext,
            XFFormAttributes papiAttributes,
            MCSAttributes attributes) {

        com.volantis.mcs.protocols.EventAttributes pattributes = null;

        String value;

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising form event attributes");
        }

        if ((value = papiAttributes.getOnSubmit()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_SUBMIT, value);
        }

        if ((value = papiAttributes.getOnReset()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_RESET, value);
        }
    }

    /**
     * Initialise the page event attributes in the given protocol attributes.
     *
     * @param pageContext    The context within which the mariner expressions
     *                       are resolved.
     * @param papiAttributes The PAPI attributes which are to be used to
     *                       initialise the protocol attributes.
     * @param attributes     The protocol attributes object to initialise.
     */
    static public void initialisePageEventAttributes(
            MarinerPageContext pageContext,
            PageAttributes papiAttributes,
            MCSAttributes attributes) {

        com.volantis.mcs.protocols.EventAttributes pattributes = null;
        String value;

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising page event attributes");
        }

        if ((value = papiAttributes.getOnLoad()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_LOAD, value);
        }

        if ((value = papiAttributes.getOnUnload()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_UNLOAD, value);
        }
    }

    /**
     * Initialise the canvas event attributes in the given protocol attributes.
     *
     * @param pageContext    The context within which the mariner expressions
     *                       are resolved.
     * @param papiAttributes The PAPI attributes which are to be used to
     *                       initialise the protocol attributes.
     * @param attributes     The protocol attributes object to initialise.
     */
    static public void initialiseCanvasEventAttributes(
            MarinerPageContext pageContext,
            CanvasAttributes papiAttributes,
            MCSAttributes attributes) {

        com.volantis.mcs.protocols.EventAttributes pattributes = null;
        String value;

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising canvas event attributes");
        }

        if ((value = papiAttributes.getOnTimer()) != null) {
            pattributes = associateEvent(pattributes, attributes,
                    pageContext, EventConstants.ON_TIMER, value);
        }
        // If onEnterForward is null then use the of OnEnter.
        if (papiAttributes.getOnEnterForward() == null) {
            papiAttributes.setOnEnterForward(papiAttributes.getOnEnter());
        }
        // If onEnterBackward is null then use the of OnEnter.
        if (papiAttributes.getOnEnterBackward() == null) {
            papiAttributes.setOnEnterBackward(papiAttributes.getOnEnter());
        }

        if ((value = papiAttributes.getOnEnterForward()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_ENTER_FORWARD, value);
        }

        if ((value = papiAttributes.getOnEnterBackward()) != null) {
            pattributes = associateEvent(pattributes, attributes, pageContext,
                    EventConstants.ON_ENTER_BACKWARD, value);
        }
    }

    /**
     * This method associates an event with an expression for the protocol
     * event attribute.
     *
     * @param pattributes   the protocol attributes
     * @param attributes    the Volantis attributes
     * @param pageContext   the page context
     * @param eventConstant the event constant
     * @param value         the value of the item to be used to resolve.
     * @return the modified event attributes
     */
    protected static com.volantis.mcs.protocols.EventAttributes associateEvent(
            com.volantis.mcs.protocols.EventAttributes pattributes,
            MCSAttributes attributes,
            MarinerPageContext pageContext, int eventConstant, String value) {
        // Only get the EventAttributes if we need to.
        if (pattributes == null) {
            pattributes = attributes.getEventAttributes(true);
        }

        PolicyReferenceResolver resolver =
                pageContext.getPolicyReferenceResolver();

        // Resolve the value event string to a mariner expression
        ScriptAssetReference expression =
                resolver.resolveQuotedScriptExpression(value);
        pattributes.setEvent(eventConstant, expression);
        return pattributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-May-05	8196/2	ianw	VBM:2005051203 Refactored PAPI to seperate out implementation

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
