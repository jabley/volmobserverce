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

package com.volantis.cache.notification;

import com.volantis.cache.CacheEntryMock;
import com.volantis.cache.CacheEntry;
import com.volantis.cache.impl.notification.RemovalListenerList;
import com.volantis.testtools.mock.expectations.OrderedExpectations;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test cases for {@link RemovalListenerList}.
 */
public class RemovalListenerListTestCase
        extends TestCaseAbstract {

    private CacheEntryMock entryMock;
    private RemovalListenerMock listenerMock1;
    private RemovalListenerMock listenerMock2;
    private RemovalListenerMock listenerMock3;

    protected void setUp() throws Exception {
        super.setUp();

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        entryMock = new CacheEntryMock("entryMock", expectations);

        listenerMock1 = new RemovalListenerMock("listenerMock1", expectations);

        listenerMock2 = new RemovalListenerMock("listenerMock2", expectations);

        listenerMock3 = new RemovalListenerMock("listenerMock3", expectations);
    }

    /**
     * Ensure that adding listeners works.
     */
    public void testAdd() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                listenerMock1.expects.entryRemoved(entryMock);
                listenerMock2.expects.entryRemoved(entryMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemovalListenerList list = new RemovalListenerList();
        list.addListener(listenerMock1);
        list.addListener(listenerMock2);

        list.dispatchRemovedEntryEvent(entryMock);
    }

    /**
     * Ensure that adding the same listener twice fails.
     */
    public void testAddTwice() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemovalListenerList list = new RemovalListenerList();
        list.addListener(listenerMock1);
        try {
            list.addListener(listenerMock1);
            fail("Did not detect attempt to add same listener twice");
        } catch (IllegalStateException expected) {
            assertEquals("Listener " + listenerMock1 +
                    " has already been added", expected.getMessage());
        }
    }

    /**
     * Ensure that removing listeners works.
     */
    public void testRemove() throws Exception {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                listenerMock1.expects.entryRemoved(entryMock);
                listenerMock2.expects.entryRemoved(entryMock);
                listenerMock3.expects.entryRemoved(entryMock);
            }
        });

        expectations.add(new OrderedExpectations() {
            public void add() {
                listenerMock1.expects.entryRemoved(entryMock);
                listenerMock3.expects.entryRemoved(entryMock);
            }
        });

        expectations.add(new OrderedExpectations() {
            public void add() {
                listenerMock1.expects.entryRemoved(entryMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemovalListenerList list = new RemovalListenerList();
        list.addListener(listenerMock1);
        list.addListener(listenerMock2);
        list.addListener(listenerMock3);

        // Make sure that they have been added properly.
        list.dispatchRemovedEntryEvent(entryMock);

        // Remove the middle one and make sure that the content are correct.
        list.removeListener(listenerMock2);
        list.dispatchRemovedEntryEvent(entryMock);

        // Remove the last one and make sure that the content are correct.
        list.removeListener(listenerMock3);
        list.dispatchRemovedEntryEvent(entryMock);

        // Remove the remaining one and make sure that the content are correct.
        list.removeListener(listenerMock1);
        list.dispatchRemovedEntryEvent(entryMock);
    }

    /**
     * Ensure that removing a listener that has not been added fails.
     */
    public void testRemoveUnknown() throws Exception {

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        RemovalListenerList list = new RemovalListenerList();
        try {
            list.removeListener(listenerMock1);
            fail("Did not detect attempt to remove unknow listener");
        } catch (IllegalStateException expected) {
            assertEquals("Listener " + listenerMock1 +
                    " is not in the list", expected.getMessage());
        }
    }

    /**
     * Ensure that add and remove can be called while in the middle of
     * processing a {@link RemovalListenerList#dispatchRemovedEntryEvent(CacheEntry)}.
     *
     * @throws Exception
     */
    public void testReentrant() throws Exception {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        expectations.add(new OrderedExpectations() {
            public void add() {
                listenerMock1.expects.entryRemoved(entryMock);
                listenerMock2.expects.entryRemoved(entryMock);
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================
        final RemovalListenerList list = new RemovalListenerList();
        RemovalListener listener = new RemovalListener() {
            public void entryRemoved(CacheEntry entry) {
                list.removeListener(listenerMock1);
                list.removeListener(this);
                list.removeListener(listenerMock2);
            }
        };
        list.addListener(listenerMock1);
        list.addListener(listener);
        list.addListener(listenerMock2);

        list.dispatchRemovedEntryEvent(entryMock);
    }
}
