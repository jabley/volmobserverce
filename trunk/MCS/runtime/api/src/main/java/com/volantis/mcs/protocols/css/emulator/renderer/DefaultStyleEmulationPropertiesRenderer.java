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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Default implementation of {@link StyleEmulationPropertiesRenderer}.
 * <p>
 * NOTE: defining the order in which we use the renderers is not as trivial
 * as it seems. Renderers fall into the following categories: those that
 * add attributes only, those that add attributes and/or elements, and those
 * that add elements only. We <b>MUST</b> process renderers in that order.
 * <p>
 * To enforce this ordering this class only allows renderers that are wrapped
 * using the following classes to be registered:
 * <ul>
 *      <li>{@link AttributeOnlyStyleEmulationPropertyRenderer}</li>
 *      <li>{@link AttributeAndOrElementStyleEmulationPropertyRenderer}</li>
 *      <li>{@link ElementOnlyStyleEmulationPropertyRenderer}</li>
 * </ul>
 *
 * <p>
 * We also need to be very careful about those that add both elements and/or
 * elements, as they can very easily tread on each others toes and prevent one
 * another from finding the element they need to add attributes to.
 */
public final class DefaultStyleEmulationPropertiesRenderer
        implements StyleEmulationPropertiesRenderer {

    /**
     * Stores mappings between style properties and style emulation renderers
     * that only use attributes to perform the emulation.
     */
    private final Map attributeOnlyStyleEmulationRenders = new HashMap();

    /**
     * Stores mappings between style properties and style emulation renderers
     * that attributes and/or elements to perform the emulation.
     */
    private final Map attributeAndOrElementRenderers = new HashMap();

    /**
     * Stores mappings between style properties and style emulation renderers
     * that only use elements to perform the emulation.
     */
    private final Map elementOnlyRenderers = new HashMap();

    /**
     * Stores a list of {@link com.volantis.mcs.themes.StylePropertyDetails}
     * that have attribute only
     * renderers associated with them.  Defines the order
     * in which associated emulation renderers should be have
     * {@link StyleEmulationPropertyRenderer#apply} called.
     */
    private final List attributeOnlyRenderersOpeningOrder = new ArrayList();

    /**
     * Stores a list of {@link com.volantis.mcs.themes.StylePropertyDetails}
     * that have attribute and/or
     * element renderers associated with them.  Defines the order
     * in which associated emulation renderers should be have
     * {@link StyleEmulationPropertyRenderer#apply} called.
     */
    private final List attributeAndOrElementsRenderersOpeningOrder = new ArrayList();

    /**
     * Stores a list of {@link com.volantis.mcs.themes.StylePropertyDetails}
     * that have element only
     * renderers associated with them.  Defines the order
     * in which associated emulation renderers should be have
     * {@link StyleEmulationPropertyRenderer#apply} called.
     */
    private final List elementOnlyRenderersOpeningOrder = new ArrayList();


    /**
     * Registers the supplied style emulation renderer with the supplied
     * style property details.
     *
     * @param styleDetails the style property that
     * <code>attributeOnlyRenderer</code> is to be associated with.
     * @param attributeOnlyRenderer the style emulation renderer to be registered.
     */
    public void register(StyleProperty styleDetails,
            AttributeOnlyStyleEmulationPropertyRenderer attributeOnlyRenderer) {

        doRegisterRendererToStylePropertyDetail(attributeOnlyRenderer,
                                                styleDetails,
                                                attributeOnlyRenderersOpeningOrder,
                                                attributeOnlyStyleEmulationRenders);
    }

    /**
     * Registers the supplied style emulation renderer with the supplied
     * style property details.
     *
     * @param styleDetails the style property that
     * <code>attributeOnlyRenderer</code> is to be associated with.
     * @param attributeAndOrElementRenderer the style emulation renderer to be registered.
     */
    public void register(
            StyleProperty styleDetails,
            AttributeAndOrElementStyleEmulationPropertyRenderer attributeAndOrElementRenderer) {

        doRegisterRendererToStylePropertyDetail(attributeAndOrElementRenderer,
                                                styleDetails,
                                                attributeAndOrElementsRenderersOpeningOrder,
                                                attributeAndOrElementRenderers);
    }

    /**
     * Registers the supplied style emulation renderer with the supplied
     * style property details.
     *
     * @param styleDetails the style property that
     * <code>attributeOnlyRenderer</code> is to be associated with.
     * @param elementOnlyRenderer the style emulation renderer to be registered.
     */
    public void register(StyleProperty styleDetails,
                         ElementOnlyStyleEmulationPropertyRenderer elementOnlyRenderer) {

        doRegisterRendererToStylePropertyDetail(elementOnlyRenderer,
                                                styleDetails,
                                                elementOnlyRenderersOpeningOrder,
                                                elementOnlyRenderers);
    }

    // Javadoc inherited.
    public void applyProperties(Element element,
                                MutablePropertyValues propertyValues) {

        // Remember that rederers must be processed in the following order
        // order: (see class comment for reasons why)

        // attributes only
        // attributes and/or elements, and those
        // elements only.
        invokeApplyOnAttributeOnlyRenderers(propertyValues, element);
        invokeApplyOnAttributeAndOrElementRenderers(propertyValues, element);
        invokeApplyOnElementOnlyRenderers(propertyValues, element);
    }

    /**
     * Helper method to register a style emulation renderer.
     *
     * @param renderToRegister the renderer to be stored in
     * <code>rendererMap</code>.
     * @param stylePropertyDetails the style property to be associated with
     * <code>renderer</code>
     * @param propertyDetailsInRegistrationOrder a list of style properties in
     * the order that renderers have been assciated with them.
     * @param rendererMap the map in which <code>renderToRegister</code>
     * will be stored in.
     */
    private void doRegisterRendererToStylePropertyDetail(
            AbstractIdentifiableStyleEmulationPropertyRenderer renderToRegister,
            StyleProperty stylePropertyDetails,
            List propertyDetailsInRegistrationOrder,
            Map rendererMap) {

        // Remember that we can associate many renderers with a single style
        // property detail and we dont want to store multiple style property
        // details in the list, otherwise style will be emulated twice!
        if (!previouslyRegisteredRendererWithStyleProperty(
                stylePropertyDetails, rendererMap)) {
            propertyDetailsInRegistrationOrder.add(stylePropertyDetails);
        }
        mapStylePropertyDetailToRenderer(rendererMap,
                   stylePropertyDetails, renderToRegister);
    }


    /**
     * Returns true if we have previously mapped a style emulation renderer
     * to the supplied <code>styleDetails</code> in the supplied
     * <code>map</code>.
     *
     * @param styleDetails the style property
     * @param map the map to inspect for having a key eqaul to
     * <code> styleDetails </code>
     *
     * @return true if we have previously mapped a style emulation renderer
     * to the supplied <code>styleDetails</code> in the supplied
     * <code>map</code>; otherwise false.
     */
    private boolean previouslyRegisteredRendererWithStyleProperty(
            StyleProperty styleDetails,
            Map map) {
        return map.containsKey(styleDetails);
    }

    /**
     * Associates a style property with the supplied emulation renderer.
     *
     * @param map the map iun which the renderer will be stored.
     * @param styleProperties the style property the renderer will
     * be associated with.
     * @param renderer the renderer to be stored.
     */
    private void mapStylePropertyDetailToRenderer(Map map,
                            StyleProperty styleProperties,
                            AbstractIdentifiableStyleEmulationPropertyRenderer
                            renderer) {

        if (map.containsKey(styleProperties)) {

            // There is a renderer associated with the current style property
            // Is it a Composite renderer?
            StyleEmulationPropertyRenderer retrievedRenderer =
                    (StyleEmulationPropertyRenderer)map.get(styleProperties);

            if (retrievedRenderer instanceof
                    CompositeStyleEmulationPropertyRenderer) {
                // If so, add the supplied renderer to the existing composite
                CompositeStyleEmulationPropertyRenderer compositeRenderer =
                        (CompositeStyleEmulationPropertyRenderer)retrievedRenderer;

                compositeRenderer.add(renderer);

            } else {

                // Add the retrieved non-composite renderer to a composite
                // renderer.
                CompositeStyleEmulationPropertyRenderer compositeRenderer =
                        new CompositeStyleEmulationPropertyRenderer();
                compositeRenderer.add(retrievedRenderer);
                // Add the supplied renderer to the composite.
                compositeRenderer.add(renderer);

                // Register the composite renderer with the current style
                // property.
                map.put(styleProperties, compositeRenderer);
            }

        } else {
            // A renderer has not been previously registered with the supplied
            // style property.  Register the supplied renderer.
            map.put(styleProperties, renderer);
        }
    }

    private Iterator processingOrderForStyleDetails(List styleDetailsInOrderAdded) {
        return styleDetailsInOrderAdded.iterator();
    }

   /**
     * Calls {@link StyleEmulationPropertyRenderer#apply} on each
     * renderer stored in the supplied map.
     *
     * @param stylePropertiesIterator iteration of style properties.
     * @param map a map containing style emulation renderers.
     * @param propertyValues collection of style property values.
     * @param element the element to write to.
     */
    private void invokeApplyOnRenderers(Iterator stylePropertiesIterator,
                                        Map map,
                                        MutablePropertyValues propertyValues,
                                        Element element) {
        while (stylePropertiesIterator.hasNext()) {
            // Get the current style property
            StyleProperty property =
                    (StyleProperty)stylePropertiesIterator.next();

            // If the style properties collection contains a value for the
            // current property and there is a rule associated then
            // render the emulated style.
            StyleValue value = propertyValues.getComputedValue(property);

            if (value != null) {
                // We have a value to emulate.

                // Get the render associated with this style property
                StyleEmulationPropertyRenderer renderer =
                    (StyleEmulationPropertyRenderer)map.
                    get(property);

                if (renderer != null) {
                    renderer.apply(element, value);
                }
            }
        }
    }

    /**
     * Returns an iteration of
     * {@link AttributeOnlyStyleEmulationPropertyRenderer}'s in the order
     * in which they will be processed.
     *
     * @return a iteration of {@link AttributeOnlyStyleEmulationPropertyRenderer}'s
     * that have been stored.
     */
    private Iterator processingOrderForAttributeOnlyRenderers() {
        return processingOrderForStyleDetails(attributeOnlyRenderersOpeningOrder);
    }

    /**
     * Returns an iteration of
     * {@link AttributeAndOrElementStyleEmulationPropertyRenderer}'s in
     * the order in which they will be processed.
     *
     * @return a iteration of
     * {@link AttributeAndOrElementStyleEmulationPropertyRenderer}'s that have
     * been stored.
     */
    private Iterator processingOrderForAttributeAndOrElementRenderers() {
        return processingOrderForStyleDetails(attributeAndOrElementsRenderersOpeningOrder);
    }

    /**
     * Returns an iteration of
     * {@link ElementOnlyStyleEmulationPropertyRenderer}'s in
     * the order in which they will be processed.
     *
     * @return a iteration of
     * {@link ElementOnlyStyleEmulationPropertyRenderer}'s that have
     * been stored.
     */
    private Iterator processingOrderForElementOnlyRenderers() {
        return processingOrderForStyleDetails(elementOnlyRenderersOpeningOrder);
    }

    /**
     * Invokes {@link AbstractIdentifiableStyleEmulationPropertyRenderer#apply}
     * on each {@link AttributeOnlyStyleEmulationPropertyRenderer} that has
     * been stored.
     *
     * @param styleProperties the style property to be emulated.
     * @param element the element to have attributes added.
     */
    private void invokeApplyOnAttributeOnlyRenderers(
            MutablePropertyValues styleProperties,
            Element element) {

        // Get the opening order for attribute only renderers.
        Iterator openingOrderForAttributeOnlyRenderers =
                processingOrderForAttributeOnlyRenderers();
        invokeApplyOnRenderers(openingOrderForAttributeOnlyRenderers,
                              attributeOnlyStyleEmulationRenders,
                              styleProperties,
                              element);
    }

    /**
     * Invokes {@link AbstractIdentifiableStyleEmulationPropertyRenderer#apply}
     * on each {@link AttributeAndOrElementStyleEmulationPropertyRenderer}
     * that has been stored.
     *
     * @param styleProperties the style property to be emulated.
     * @param element the element to have attributes added.
     */
    private void invokeApplyOnAttributeAndOrElementRenderers(
            MutablePropertyValues styleProperties,
            Element element) {

        // Get the opening order for attribute and/or element renderers
        Iterator openeningOrderForAttributeAndOrElementRenderers =
               processingOrderForAttributeAndOrElementRenderers();

        invokeApplyOnRenderers(openeningOrderForAttributeAndOrElementRenderers,
                              attributeAndOrElementRenderers,
                              styleProperties,
                              element);
    }

    /**
     * Invokes {@link AbstractIdentifiableStyleEmulationPropertyRenderer#apply}
     * on each {@link ElementOnlyStyleEmulationPropertyRenderer} that has
     * been stored.
     *
     * @param styleProperties the style property to be emulated.
     * @param element the element to have attributes added.
     */
    private void invokeApplyOnElementOnlyRenderers(
            MutablePropertyValues styleProperties,
            Element element) {

        Iterator openingOrderForElementOnlyRenderers =
                this.processingOrderForElementOnlyRenderers();

        invokeApplyOnRenderers(openingOrderForElementOnlyRenderers,
                              elementOnlyRenderers,
                              styleProperties,
                              element);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/4	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 23-Nov-05	10402/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/4	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 23-Nov-05	10381/4	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 22-Aug-05	9184/3	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 20-Jul-05	8668/4	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 19-Jul-05	8668/1	geoff	VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 14-Jul-05	9039/1	emma	VBM:2005071401 Adding get/setSynthesizedValues to PropertyValues

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 21-Nov-05	10377/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 17-Nov-05	10251/1	rgreenall	VBM:2005110710 Parameterize HTML 3.2 protocol to output border styling if supported by the requesting device.

 20-Jul-04	4897/6	geoff	VBM:2004071407 Implementation of theme style options: finalise rule processing

 15-Jul-04	4869/1	geoff	VBM:2004062303 Implementation of theme style options: HTMLVersion 3.2 Family

 29-Jun-04	4720/3	byron	VBM:2004061604 Core Emulation Facilities - rename and move classes

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
