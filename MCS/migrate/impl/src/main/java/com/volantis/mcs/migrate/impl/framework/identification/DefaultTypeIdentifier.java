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
package com.volantis.mcs.migrate.impl.framework.identification;

import com.volantis.mcs.migrate.api.framework.InputMetadata;
import com.volantis.mcs.migrate.api.framework.PathRecogniser;
import com.volantis.mcs.migrate.api.framework.ResourceMigrationException;
import com.volantis.mcs.migrate.api.framework.Version;
import com.volantis.mcs.migrate.impl.framework.graph.Graph;
import com.volantis.mcs.migrate.impl.framework.io.RestartInputStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Default implementation of {@link TypeIdentifier}.
 * <p>
 * This is implemented by checking the path recogniser and if recognised, then
 * iterating through the content identifiers looking for a match. If we get
 * more than one match this is an error.
 */
public class DefaultTypeIdentifier implements TypeIdentifier {

    /**
     * Factory for creating identification objects.
     * <p>
     * Used to create {@link Match} objects.
     */
    private IdentificationFactory factory;

    /**
     * The name of the type that this object identifies.
     */
    private String name;

    /**
     * The path recogniser.
     */
    private PathRecogniser pathRecogniser;

    /**
     * The collection of content identifiers.
     */
    private Collection contentIdentifiers = new ArrayList();

    /**
     * The migration step graph.
     * <p>
     * Note that if the graph does not include all the versions recognised
     * buy the content identifiers than we will generate an exception when
     * we try and extract the step sequence for the version.
     */
    private Graph graph;

    /**
     * Initialise.
     *
     * @param factory factory for creating identification objects.
     * @param name
     */
    public DefaultTypeIdentifier(IdentificationFactory factory, String name) {

        this.factory = factory;
        this.name = name;
    }

    // Javadoc inherited.
    public String getName() {

        return name;
    }

    /**
     * Set the path recogniser used by this object.
     *
     * @param pathRecogniser the path recogniser used by this object.
     */
    public void setPathRecogniser(PathRecogniser pathRecogniser) {

        this.pathRecogniser = pathRecogniser;
    }

    /**
     * Add a content recogniser to be used by this object.
     * <p>
     * Each type identifier should have an content identifier added for each
     * version supported by the graph (and vice versa).
     *
     * @param contentRecogniser a content recogniser to be used by this object.
     */
    public void addContentIdentifier(
            ContentIdentifier contentRecogniser) {

        contentIdentifiers.add(contentRecogniser);
    }

    /**
     * Add the graph of migration steps used by this object.
     * <p>
     * Each type identifier should have a graph with a step for each version
     * recognised by it's content recognisers (and vice versa).
     *
     * @param graph the graph of migration steps for this object.
     */
    public void setGraph(Graph graph) {

        this.graph = graph;
    }

    // Javadoc inherited.
    public Match identifyResource(InputMetadata meta, RestartInputStream input)
            throws ResourceMigrationException {

        if (contentIdentifiers.size() == 0) {
            throw new IllegalStateException(
                    "Must provide at least one content recogniser");
        }
        if (graph == null) {
            throw new IllegalStateException("Must provide graph");
        }

        Match match = null;

        boolean applyPathRecognition = meta.applyPathRecognition();
        boolean recognisedPath = false;
        if (applyPathRecognition) {
            if (pathRecogniser != null) {
                try {
                    recognisedPath = pathRecogniser.recognisePath(meta.getURI());
                } catch (ResourceMigrationException e) {
                    throw new ResourceMigrationException("Error recognising path " +
                            "for type " + getName(), e);
                }
            }
            // else, user has asked for path recognition but this type does not
            // support it, so we skip it. This just means that things run a bit
            // slower.
            // todo: add a debug log here? does this use notification?
        }

        if (recognisedPath || !applyPathRecognition) {

            Version identifiedVersion = null;
            Iterator contentRecognisersIterator = contentIdentifiers.iterator();
            while (contentRecognisersIterator.hasNext()) {
                input.restart();
                ContentIdentifier contentRecogniser =
                        (ContentIdentifier) contentRecognisersIterator.next();

                // todo: later: potentially a recognition exception should not
                // always be fatal. It may be that there are multiple
                // recognisers for a path and that one of the ones that throws
                // an exception is not the one that actually recognises the
                // content. In this case we could log the error, continue on,
                // and only fail if there is no match in the end.
                boolean identifiedContent;
                try {
                    identifiedContent = contentRecogniser.identifyContent(input);
                } catch (Exception e) {
                    throw new ResourceMigrationException("Error identifying " +
                            "content for type " + getName() + " version " +
                            contentRecogniser.getVersion().getName(), e);
                }

                if (identifiedContent) {
                    if (identifiedVersion == null) {
                        identifiedVersion = contentRecogniser.getVersion();
                    } else {
                        throw new IllegalStateException("Duplicate version " +
                                "match: " + contentRecogniser.getVersion().
                                getName() + " and " + identifiedVersion +
                                " both match the content");
                    }
                }
            }

            if (identifiedVersion != null &&
                    !identifiedVersion.equals(graph.getTargetVersion())) {
                // Extract the sequence of steps that starts with the
                // identified version. This might generate an exception if
                // we didn't do much validation at build time and the
                // identified version does not have a matching step.
                Iterator sequence = graph.getSequence(identifiedVersion);

                match = factory.createMatch(getName(),
                        identifiedVersion.getName(), sequence);
            }
        }

        return match;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 19-May-05	8036/15	geoff	VBM:2005050505 XDIMECP: Migration Framework

 18-May-05	8036/13	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/10	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/7	geoff	VBM:2005050505 XDIMECP: Migration Framework

 16-May-05	8036/5	geoff	VBM:2005050505 XDIMECP: Migration Framework

 11-May-05	8036/1	geoff	VBM:2005050505 XDIMECP: Migration Framework

 ===========================================================================
*/
