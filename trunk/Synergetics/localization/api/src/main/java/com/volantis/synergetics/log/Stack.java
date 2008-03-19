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

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A typed class used to manage thread local DI Context information. This
 * class cannot hold null values. It uses null returns to signal that you did
 * something stupid
 */
class Stack implements Serializable {

    /**
     * The empty string array
     */
    private static final String[] EMPTY = new String[0];

    /**
     * The actual stack (use list as it is not synchronized)
     */
    private final ArrayList list = new ArrayList();

    /**
     * The maximum size of this stack. This limit is imposed so that even if
     * you mess up your push and pops you cannot consume enough memory to
     * take out your app server
     */
    private static final int MAX_DEPTH_SIZE = 30;

    /**
     * Push the context onto the stack
     *
     * @param context the context to push
     */
    public void push(Context context) {
        if (null == context) {
            throw new NullPointerException();
        }

        if (list.size() <= MAX_DEPTH_SIZE) {
            // set up the full context message
            if (isEmpty()) {
                context.setFullContext(context.getContext());
            } else {
                context.setFullContext(getFullContext() +
                    ' ' + context.getContext());
            }
            list.add(context);
        } else {
            throw new IllegalStateException("stack-full");
        }
    }

    /**
     * Pop the context from the stack.
     *
     * @return null if the stack is empty. The Context from the top of the
     * stack otherwise.
     */
    public Context pop() {
        Context result = null;
        if (!isEmpty()) {
            result = (Context) list.remove(list.size() - 1);
        } else {
            throw new IllegalStateException("stack-empty");
        }
        return result;
    }

    /**
     * Peek at the value of the value on the top of the stack
     *
     * @return the value on the top of the stack or null if the stack is empty
     */
    public Context peek() {
        Context result = null;
        if (!isEmpty()) {
            result = (Context) list.get(list.size() - 1);
        } else {
            throw new IllegalStateException("stack-empty");
        }
        return result;
    }

    /**
     * Return true if the stack is empty, false otherwise
     * 
     * @return true if the stack is empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Convienience method to return the Full context of the current stack
     *
     * @return the Full context of the current stack
     */
    public String getFullContext() {
        String result = "";
        Context context = peek();
        if (null != context) {
            result = context.getFullContext();
        }
        return result;
    }

    /**
     * Return an array of Strings containing the context names from the stack
     * This is a copy. If the stack is empty this returns an empty String[]
     *
     * @return an array of Strings containing the context names
     */
    public String[] asArray() {
        final String[] result;
        if (isEmpty()) {
            result = EMPTY;
        } else {
            result = new String[list.size()];
            for (int i=0; i<list.size(); i++) {
                result[i] = ((Context) list.get(i)).getContextName();
            }
        }
        return result;
    }

    /**
     * Compare the parameter o with this.
     * Two stacks are equal if they have the same number of entries and each
     * entry is equal to its corresponding entry in the other stack.
     */
    // Rest of Javadoc inherited
    public boolean equals(Object o) {
        boolean result = false;
        if (null != o && getClass() == o.getClass()) {
            Stack other = (Stack) o;
            result = list.equals(other.list);
        }
        return result;
    }

    // javadoc inherited
    public int hashCode() {
        return list.hashCode();
    }

    /**
     * Return the size of the stack.
     *
     * @return the size of the stack
     */
    public int size() {
        return list.size();
    }

    /**
     * Return the name of the entry at element i
     *
     * @param i the index of the entry whose name you want
     * @return the name of the entry at the specified element or null if the
     * index is invalid
     */
    public String getContextName(int i) {
       if (i<0 || i>=size()) {
           return null;
       } else {
           Context c = (Context)list.get(i);
           return c.getContextName();
       }
    }

    /**
     * Return the value of the context at element i
     *
     * @param i the index of the entry whose value you want
     * @return the value of the entry at the specified element or null if the
     * index is invalid
     */
    public String getContextValue(int i) {
        if (i < 0 || i >= size()) {
            return null;
        } else {
            Context c = (Context) list.get(i);
            return c.getContext();
        }
    }
    /**
     * Helper class used to store information in the thread local stack
     */
    static class Context implements Serializable {

        /**
         * the name of the context
         */
        private final String contextName;

        /**
         * The context
         */
        private final String context;

        /**
         * The full context message (i.e. the concatenated stack messages).  We
         * build this as entries are added to the stack as this occurs far less
         * often then requests for the message.
         */
        private String fullContext;

        /**
         * The logging level for this context. This must be set by calling
         * {@link #setFullContext(String)} prior to use.
         */
        private final LogLevel level;

        /**
         * Constructor
         *
         * @param contextName the name of the context
         * @param context the logging context
         * @param level   the logging level for this context
         */
        Context(String contextName, String context, LogLevel level) {
            this.contextName = contextName;
            this.context = context;
            this.level = level;
        }

        /**
         * Return the name of the context.
         *
         * @return the name of the context.
         */
        public String getContextName() {
            return this.contextName;
        }

        /**
         * Return the context
         *
         * @return the context
         */
        public String getContext() {
            return context;
        }

        /**
         * Return the log level.
         *
         * @return the log level
         */
        public LogLevel getLevel() {
            return level;
        }

        /**
         * Return the full context message.
         *
         * @return the full context message
         */
        public String getFullContext() {
            return fullContext;
        }

        /**
         * Private method used by Stack to set the full context message
         *
         * @param fullContext the full context message
         */
        private void setFullContext(String fullContext) {
            this.fullContext = fullContext;
        }

        // javadoc inherited
        public boolean equals(Object o) {
            boolean result = false;
            if (getClass() == o.getClass()) {
                Context other = (Context) o;
                if (getContextName().equals(other.getContextName()) &&
                    getContext().equals(other.getContext()) &&
                    getLevel().equals(other.getLevel())) {
                    result = true;
                }
            }
            return result;
        }

        // Javadoc inherited
        public int hashCode() {
            int result = 31;
            result += result * 17 + getContextName().hashCode();
            result += result * 17 + getContext().hashCode();
            result += result * 17 + getLevel().hashCode();
            return result;
        }

    }
}
