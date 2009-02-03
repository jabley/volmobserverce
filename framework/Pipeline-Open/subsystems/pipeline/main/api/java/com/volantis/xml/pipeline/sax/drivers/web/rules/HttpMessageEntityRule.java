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

package com.volantis.xml.pipeline.sax.drivers.web.rules;

import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.xml.namespace.ExpandedName;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.pipeline.Namespace;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLProcess;
import com.volantis.xml.pipeline.sax.drivers.web.DerivableHTTPMessageEntity;
import com.volantis.xml.pipeline.sax.drivers.web.WebDriverElements;
import com.volantis.xml.pipeline.sax.drivers.web.WebRequestEntityFactory;
import com.volantis.xml.pipeline.sax.drivers.web.simple.Setter;
import com.volantis.xml.pipeline.sax.drivers.web.simple.SetterFinder;
import com.volantis.xml.pipeline.sax.drivers.web.simple.Setters;
import com.volantis.xml.pipeline.sax.drivers.web.simple.SettersBuilder;
import com.volantis.xml.pipeline.sax.drivers.web.simple.SimpleElementProcess;
import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.dynamic.rules.AbstractAddProcessRule;
import com.volantis.pipeline.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Base rule for webd:header, webd:parameter and webd:cookie element.
 */
public abstract class HttpMessageEntityRule
        extends AbstractAddProcessRule {

    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(HttpMessageEntityRule.class);

    /**
     * The setters that are common across all
     * {@link DerivableHTTPMessageEntity}
     */
    protected static final Setters COMMON_SETTERS;
    private static final String WEB_DRIVER_NAMESPACE = Namespace.WEB_DRIVER.getURI();

    static {
        SettersBuilder builder = new SettersBuilder();

        addCommonSetters(builder);

        COMMON_SETTERS = builder.buildSetters();
    }

    /**
     * Add the setters that are common across all
     * {@link DerivableHTTPMessageEntity} to the builder.
     *
     * @param builder The builder to update.
     */
    protected static void addCommonSetters(SettersBuilder builder) {
        builder.addAttributeSetter(WebDriverElements.FROM.getLocalName(), new Setter() {
            public void setPropertyAsString(Object object, String string) {
                ((DerivableHTTPMessageEntity) object).setFrom(string);
            }
        });

        builder.addCombinedSetter(WebDriverElements.NAME, new Setter() {
            public void setPropertyAsString(Object object, String string) {
                ((DerivableHTTPMessageEntity) object).setName(string);
            }
        });

        builder.addCombinedSetter(WebDriverElements.VALUE, new Setter() {
            public void setPropertyAsString(Object object, String string) {
                ((DerivableHTTPMessageEntity) object).setValue(string);
            }
        });
    }

    /**
     * Create an expanded name in the web driver namespace.
     *
     * @param localName The expanded name.
     * @return The name.
     */
    protected static ImmutableExpandedName createExpandedName(
            final String localName) {
        String namespaceURI = WEB_DRIVER_NAMESPACE;
        return new ImmutableExpandedName(namespaceURI, localName);
    }

    /**
     * The factory to use to create entities.
     */
    protected final WebRequestEntityFactory factory;

    /**
     * The setters to use to initialise the entity.
     */
    private final Setters setters;

    /**
     * The key for the appropriate entity collection in the context.
     */
    private final Object entityCollectionKey;

    /**
     * Initialise.
     *
     * @param factory             The factory to use to create entities.
     * @param setters             The setters to use to initialise the entity.
     * @param entityCollectionKey The key for the appropriate entity collection
     *                            in the context.
     */
    protected HttpMessageEntityRule(
            WebRequestEntityFactory factory,
            Setters setters,
            Object entityCollectionKey) {
        this.factory = factory;
        this.setters = setters;
        this.entityCollectionKey = entityCollectionKey;
    }

    // Javadoc inherited.
    protected XMLProcess createProcess(
            DynamicProcess dynamicProcess, ExpandedName elementName,
            Attributes attributes) throws SAXException {

        DerivableHTTPMessageEntity entity = createEntity();

        // Initialise the entity from the attributes.
        SetterFinder finder = new SetterFinder(setters);
        int length = attributes.getLength();

        for (int i = 0; i < length; i += 1) {
            String namespaceURI = attributes.getURI(i);

            if (namespaceURI.equals("")) {
                String localName = attributes.getLocalName(i);
                Setter setter = finder.findAttributeSetter(localName);
                if (setter == null) {
                    forwardError(dynamicProcess,
                            "Invalid attribute '" + localName + "'");
                }

                String value = attributes.getValue(i);
                if (value != null) {
                    setter.setPropertyAsString(entity, value);
                }

            } else {
                // Ignore global attributes.
            }
        }

        // Create a process to collect values set by elements which will
        // override the ones set by attribute.
        SimpleElementProcess operation = new SimpleElementProcess(
                elementName, setters, entity, WEB_DRIVER_NAMESPACE);

        return operation;
    }

    protected void preRemoveProcess(
            DynamicProcess dynamicProcess, ExpandedName elementName,
            XMLProcess process) throws SAXException {

        SimpleElementProcess simple = (SimpleElementProcess) process;

        DerivableHTTPMessageEntity entity = (DerivableHTTPMessageEntity)
                simple.getObject();

        String name = entity.getName();
        if (name == null) {
            String from = entity.getFrom();
            if (from == null) {
                forwardError(dynamicProcess, "No name specified for '" +
                        elementName + "'");
            } else {
                entity.setName(from);
            }
        }

        // Add the entity to the collection of entities.
        XMLPipelineContext context = dynamicProcess.getPipelineContext();
        HTTPMessageEntities entities = getEntities(context);
        addProtocolMessageEntity(entities, entity);
    }

    /**
     * Create the entity to initialise.
     *
     * @return The entity.
     */
    protected abstract DerivableHTTPMessageEntity createEntity();

    /**
     * Add the entity to the list of entities and update the list of entities
     * in the pipeline context (keyed on the key parameter).
     */
    protected void addProtocolMessageEntity(
            HTTPMessageEntities entities,
            DerivableHTTPMessageEntity entity) {
        entities.add(entity);
    }

    /**
     * Get the appropriate HTTPMessageEntities from the context creating
     * if necessary.
     *
     * @return The HTTPMessageEntities from the XMLPipelineContext for the
     *         entity.
     */
    protected HTTPMessageEntities getEntities(XMLPipelineContext context) {
        HTTPMessageEntities entities = (HTTPMessageEntities)
                context.getProperty(entityCollectionKey);
        if (entities == null) {
            entities = factory.createHTTPMessageEntities();
            context.setProperty(entityCollectionKey, entities, false);
        }
        return entities;
    }
}
