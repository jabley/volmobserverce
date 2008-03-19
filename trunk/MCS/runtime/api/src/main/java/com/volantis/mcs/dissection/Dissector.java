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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-May-03    Paul            VBM:2003052901 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dissection;

import com.volantis.mcs.dissection.annotation.Annotator;
import com.volantis.mcs.dissection.annotation.DissectableArea;
import com.volantis.mcs.dissection.annotation.DissectedDocumentImpl;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.mcs.dissection.impl.AvailableShardsImpl;
import com.volantis.mcs.dissection.impl.DissectedDocumentSerializer;
import com.volantis.mcs.dissection.impl.SelectedShards;
import com.volantis.mcs.dissection.impl.Shard;
import com.volantis.mcs.localization.LocalizationFactory;

import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Entry point to dissection package.
 * <p>
 * A single instance of this can be used for all documents as it does not
 * maintain any document specific state.
 * <h3>Example Usage</h3>
 * The first thing to do is create a dissected document from
 *
 */
public class Dissector {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(Dissector.class);

    /**
     * Create a dissected document from a DissectableDocument.
     * @param document The DissectableDocument that represents the document to be
     * dissected.
     * any other information needed by the dissection process. This object can
     * be cached and reused to generate subsequent pages.
     */
    public
        DissectedDocument createDissectedDocument(DissectionContext dissectionContext,
                                                  DissectionCharacteristics characteristics,
                                                  DissectableDocument document,
                                                  DissectionURLManager urlManager,
                                                  DocumentInformation docInfo)
        throws DissectionException {

        // Annotate the dissectable document and in the process create a
        // DissectedDocument.
        Annotator annotator = new Annotator();
        DissectedDocumentImpl annotation
            = annotator.annotateDocument(dissectionContext, document, characteristics,
                                         urlManager, docInfo);

        // Get the DissectedDocument from the annotator.
        return annotation;
    }

    /**
     * Select the requested shards.
     * @param requestedShards This contains the set of shards that should
     * be generated, one for each dissectable area.
     * @throws DissectionException If there was a problem dissecting.
     */
    public void selectRequestedShards(DissectionContext dissectionContext,
                                      DissectedDocument dissectedDocument,
                                      RequestedShards requestedShards,
                                      AvailableShards availableShards)
        throws DissectionException {

        DissectedDocumentImpl document
            = (DissectedDocumentImpl) dissectedDocument;

        // Select the shards from each area.
        SelectedShards selectedShards = getSelectedShards(dissectionContext,
                                                          document,
                                                          requestedShards,
                                                          availableShards);

    }

    public void selectShard(DissectionContext dissectionContext,
                            DissectedDocument dissectedDocument,
                            RequestedShards requestedShards,
                            AvailableShards availableShards)
        throws DissectionException {

        DissectedDocumentImpl document
            = (DissectedDocumentImpl) dissectedDocument;

        // Select the shards from each area.
        SelectedShards selectedShards = getSelectedShards(dissectionContext, document,
                                                          requestedShards,
                                                          availableShards);

        DissectableArea area = document.getDissectableArea(1);

        int shardIndex = requestedShards.getShard(area.getIndex());

        // Retrieve the appropriate shard from the area. If the shard index
        // is invalid for some reason then we may not get back exactly the
        // one that we asked for but we will get one back.
        Shard shard
            = area.retrieveShard(dissectionContext, shardIndex,
                                 (AvailableShardsImpl) availableShards);
        if (shard == null) {
            throw new DissectionException(
                        exceptionLocalizer.format("shard-not-found"));
        }

    }

    /**
     * Serialize the contents of this DissectedDocument to the specified
     * contentHandler as a stream of events.
     * @param requestedShards This contains the set of shards that should
     * be generated, one for each dissectable area.
     * @param contentHandler The target for the events generated by this method.
     * @throws DissectionException If there was a problem dissecting or an
     * exception was thrown by the contentHandler.
     * todo: Maybe the functionality in this should be moved into a separate
     * todo: class ala XMLReader.
     */
    public void serialize(DissectionContext dissectionContext,
                          DissectedDocument dissectedDocument,
                          RequestedShards requestedShards,
                          DissectedContentHandler contentHandler)
        throws DissectionException {

        DissectedDocumentImpl document
            = (DissectedDocumentImpl) dissectedDocument;

        // Select the shards from each area.
        SelectedShards selectedShards = getSelectedShards(dissectionContext, document,
                                                          requestedShards,
                                                          null);

        DissectedDocumentSerializer serializer
            = new DissectedDocumentSerializer();

        serializer.visitDocument(document, selectedShards, contentHandler);
    }

    /**
     * Create and initialise a SelectedShards object from the RequestedShards.
     * <p>
     * The main reason that this method has been separated out is in order to
     * @param requestedShards
     * @param document
     * @return the selected shards
     * @throws DissectionException
     */
    static SelectedShards getSelectedShards(DissectionContext dissectionContext,
                                            DissectedDocumentImpl document,
                                            RequestedShards requestedShards,
                                            AvailableShards availableShards)
        throws DissectionException {

        // Create a SelectedShards object to hold the shards that have been
        // selected for each dissectable area.
        SelectedShards selectedShards = document.createSelectedShards();

        // Get the shard that has been selected for each dissectable area. This
        // is done by iterating over the dissectable areas looking to see if a
        // shard has been specified for them in the fragmentation state. If no
        // shard has been explicitly set then the first shard is used.
        int count = selectedShards.getCount();
        for (int i = 0; i < count; i += 1) {
            DissectableArea area = document.getDissectableArea(i);

            int shardIndex = requestedShards.getShard(area.getIndex());

            // Retrieve the appropriate shard from the area. If the shard index
            // is invalid for some reason then we may not get back exactly the
            // one that we asked for but we will get one back.
            Shard shard
                = area.retrieveShard(dissectionContext, shardIndex,
                                     (AvailableShardsImpl) availableShards);
            if (shard == null) {
                throw new DissectionException(
                        exceptionLocalizer.format("shard-not-found"));
            }

            // todo: We need to check to see whether there are any discrepancies
            // todo: between the requested shards and the selected ones as if
            // todo: there are it means that there is a problem with the
            // todo: source of the information such as fragmentation state.

            // Set the shard on the specified area.
            selectedShards.setShard(area, shard);
        }

        return selectedShards;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
