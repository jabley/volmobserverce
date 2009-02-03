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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.log;

import junit.framework.TestCase;

import java.util.Arrays;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class StackTestCase extends TestCase {

    public void testGeneral() throws Exception {

        Stack s = new Stack();

        assertTrue(s.isEmpty());
        // empty so throw exception
        try {
            s.pop();
            fail("exception should have been thrown");
        } catch(IllegalStateException e) {
            // success
        }
        assertTrue(s.isEmpty());
        try {
            s.peek();
            fail("exception should have been thrown");
        } catch (IllegalStateException e) {
            // success
        }

        assertTrue(s.isEmpty());

        try {
            s.push(null);
            fail("an exception should be thrown");
        } catch(Exception e) {
            // success
        }

        {
            String contextName = "bob";
            String contextValue = "b";
            LogLevel level = LogLevel.INFO;
            Stack.Context context = new Stack.Context(contextName, contextValue, level);
            s.push(context);
            assertFalse(s.isEmpty());
            assertEquals(context, s.peek());
            assertEquals(contextValue, s.getFullContext());
        }

        {
            String contextName = "jane";
            String contextValue = "j";
            LogLevel level = LogLevel.WARN;
            Stack.Context context =
                new Stack.Context(contextName, contextValue, level);

            s.push(context);
            assertFalse(s.isEmpty());
            assertSame(context, s.peek());
            // should have two contexts here
            assertEquals("b "+contextValue, s.getFullContext());
            assertEquals(context, s.pop());
            // should still have a context
            assertFalse(s.isEmpty());
        }

        assertNotNull(s.pop());
        assertTrue(s.isEmpty());
        try {
            s.peek();
            fail("An exception should have been thrown");
        } catch(IllegalStateException e) {
            // success
        }
                
    }

    /**
     * Ensure the asArray method works.
     * @throws Exception
     */
    public void testAsArray() throws Exception {
        Stack s = new Stack();

        String [] empty = s.asArray();
        assertTrue(Arrays.equals(new String[0], empty));

        String[] names = {"rita", "sue", "and bob too"};
        String[] values = {"r", "s", "b"};
        LogLevel[] levels = {LogLevel.INFO, LogLevel.ERROR, LogLevel.WARN};

        for (int i=0; i<names.length; i++) {
            s.push(new Stack.Context(names[i], values[i], levels[i]));
        }

        String[] contexts = s.asArray();

        assertTrue(Arrays.equals(names, contexts));
    }

    /**
     * Test equals method
     * @throws Exception
     */
    public void testEquals() throws Exception {
        Stack s1 = new Stack();
        Stack s2 = new Stack();


        String[] names = {"rod", "jane", "freddy"};
        String[] values = {"r", "j", "f"};
        LogLevel[] levels = {LogLevel.INFO, LogLevel.ERROR, LogLevel.WARN};

        {
            for (int i = 0; i < names.length; i++) {
                s1.push(new Stack.Context(names[i], values[i], levels[i]));
            }
        }
        {
            for (int i = 0; i < names.length; i++) {
                s2.push(new Stack.Context(names[i], values[i], levels[i]));
            }
        }

        assertEquals(s1, s2);
    }


    public void testSerializable() throws Exception {

        Stack s = new Stack();

        String[] empty = s.asArray();
        assertTrue(Arrays.equals(new String[0], empty));

        String[] names = {"rod", "jane", "freddy"};
        String[] values = {"r", "j", "f"};
        LogLevel[] levels = {LogLevel.INFO, LogLevel.ERROR, LogLevel.WARN};

        for (int i = 0; i < names.length; i++) {
            s.push(new Stack.Context(names[i], values[i], levels[i]));
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(s);
        oos.close();
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        Stack s2 = (Stack) ois.readObject();
        assertEquals(s, s2);

    }

}
