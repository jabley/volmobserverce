/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2005. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.impl;


class OrderedExpectationBuilder
        extends ExpectationBuilderImpl {

    OrderedExpectationBuilder(String description) {
        super(description == null ? null : "OrderedBuilder: " + description);
        
        beginSequence();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-May-05	8277/1	pduffin	VBM:2005051704 Added expectation builder to make it easier to use combinations of sequences and sets

 ===========================================================================
*/
