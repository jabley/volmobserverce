/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import java.io.IOException;

/**
 * An interface that extends an interface that has methods with throws and
 * changes the throws list.
 *
 * @mock.generate base="InterfaceWithMethodThrows"
 */
public interface InterfaceExtendingInterfaceWithMethodThrows
        extends InterfaceWithMethodThrows {

    void foo(int a) throws IOException;

    int[] bar();
}
