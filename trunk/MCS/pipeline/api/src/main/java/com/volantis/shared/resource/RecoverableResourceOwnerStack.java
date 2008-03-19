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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.resource;

import com.volantis.shared.recovery.AbstractRecoverableTransaction;
import com.volantis.shared.recovery.RecoverableTransactionStack;
import com.volantis.xml.pipeline.sax.ReleasableResourceOwner;
import com.volantis.xml.pipeline.sax.ResourceOwner;

import java.util.Stack;

/**
 * This specialisation of RecoverableTransactionStack allows us encapsulate the
 * recoverable stack behaviour and call {@link ResourceOwner#release} on any
 * ResourceOwner Objects popped from the Stack or 'unadded' as a result of
 * reverting the state.
 *
 * {@see RecoverableTransactionStack} for more details of recoverable stack
 * behaviour.
 *
 * todo Transaction only needs to support popping entries that have been pushed
 * todo within that transaction. At least I think so.
 */
public class RecoverableResourceOwnerStack
        extends RecoverableTransactionStack implements ResourceOwner {

    /**
     * This java.util.Stack is used to track the {@link ResourceOwner}s that
     * have been pushed (and have been requested to be released) onto the main
     * Stack since the start of a transaction.
     */
    protected Stack pushedResourceOwners;

    /**
     * This java.util.Stack is used to track the {@link ResourceOwner}s that
     * have been popped (and have been requested to be released) from the main
     * Stack since the start of a transaction.
     */
    protected Stack poppedResourceOwners;

    /**
     * Create a new instance of RecoverableResourceOwnerStack.
     */
    public RecoverableResourceOwnerStack() {
        pushedResourceOwners = new Stack();
        poppedResourceOwners = new Stack();
    }

    // Javadoc inherited from Cloneable interface.
    protected Object clone() throws CloneNotSupportedException {
        RecoverableResourceOwnerStack clone =
                (RecoverableResourceOwnerStack)super.clone();
        clone.pushedResourceOwners = (Stack)pushedResourceOwners.clone();
        clone.poppedResourceOwners = (Stack)poppedResourceOwners.clone();
        return clone;
    }

    // Javadoc inherited from superclass
    public Object push(Object item) {
        return push(item, true);
    }

    /**
     * Pushes an item onto the top of this stack.
     * @param item - the item to be pushed onto this stack.
     * @param release if true and the specified Object is a
     * {@link ResourceOwner} then call the {@link ResourceOwner#release} method
     * when the item is popped from the stack
     * @return the <code>item</code> argument.
     */
    public Object push(Object item, boolean release) {
        Object toPush = item;
        boolean releasable = false;
        if (release && toPush instanceof ResourceOwner) {
            ResourceOwner resourceOwner = (ResourceOwner)toPush;
            if (!existsInStacks(resourceOwner)) {
                toPush = new ReleasableResourceOwner((ResourceOwner)toPush);
                releasable = true;
            }
        }

        Object pushed = super.push(toPush);
        if (inTransaction() && releasable) {
            pushedResourceOwners.push(pushed);
        }
        return pushed;
    }

    /**
     * This helper method is used to determine whether a specified
     * ResourceOwner exists within the stacks maintained by this object
     * @param resourceOwner The ResourceOwner to find in this object.
     * @return true if the ResourceOwner was found in the Stacks maintained
     * by this object.
     */
    private boolean existsInStacks(ResourceOwner resourceOwner) {
        boolean result = false;

        result = contains(stack, resourceOwner);

        if (inTransaction()) {
            // if it is not in the stack then it won't be in the pushedDelta
            // unless it is also in the poppedResourceOwners.  So we only need
            // also check the poppedResourceOwners
            if (!result) {
                result = contains(poppedResourceOwners, resourceOwner);
            }

            // The only other place it could be is in a parent transaction
            // poppedResourceOwner stack...
            for (int i = clones.size() - 1; i >= 0 && !result; i--) {
                RecoverableResourceOwnerStack clone =
                        (RecoverableResourceOwnerStack)clones.get(i);
                result = contains(clone.poppedResourceOwners, resourceOwner);
            }
        }

        return result;
    }

    // javadoc inherited from superclass
    public Object peek() {
        if (stack.empty()) {
            throw new IllegalStateException("Cannot peek as stack is empty");
        }
        Object peek = super.peek();
        if (peek instanceof ReleasableResourceOwner) {
            peek = ((ReleasableResourceOwner)peek).getResourceOwner();
        }
        return peek;
    }

    /**
     * Removes the Object at the top of the Stack. If this Object is an
     * instance of {@link ResourceOwner} then:
     * <ul>
     * <li> if we are in a transaction we keep a reference to the object to
     *      later invoke {@link ResourceOwner#release} if and only if the
     *      transaction completes successfully.
     * <li> if we are not in a transaction we call the
     *      {@link ResourceOwner#release} method immediately.
     * </ul>
     * @return the Object at the top of the stack
     */
    public Object pop() {
        Object popped = super.pop();
        if (popped instanceof ReleasableResourceOwner) {
            if (inTransaction()) {
                poppedResourceOwners.push(popped);
                popped = ((ReleasableResourceOwner)popped).getResourceOwner();
            } else {
                popped = ((ReleasableResourceOwner)popped).release();
            }
        }
        return popped;
    }

    // Javadoc inherited from superclass
    protected void startTransactionImpl() {
        super.startTransactionImpl();
        pushedResourceOwners.clear();
        poppedResourceOwners.clear();
    }

    // Javadoc inherited from superclass
    protected void commitTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        if (poppedState.inTransaction()) {
            RecoverableResourceOwnerStack popped =
                    (RecoverableResourceOwnerStack)poppedState;
            addStack(popped.pushedResourceOwners, pushedResourceOwners);
            pushedResourceOwners = popped.pushedResourceOwners;
            addStack(popped.poppedResourceOwners, poppedResourceOwners);
            poppedResourceOwners = popped.poppedResourceOwners;
        } else {
            // if the clone is not in a transaction state then we must commit
            // everything.  we clear the pushedDelta for completeness.
            releaseStack(poppedResourceOwners);
            pushedResourceOwners.clear();
        }
        super.commitTransactionImpl(poppedState);
    }

    // Javadoc inherited from superclass
    protected void rollbackTransactionImpl(
            AbstractRecoverableTransaction poppedState) {
        RecoverableResourceOwnerStack popped =
                (RecoverableResourceOwnerStack)poppedState;

        // release anything we added during the course of the transaction.
        releaseStack(pushedResourceOwners);

        // restore the delta stacks
        pushedResourceOwners = popped.pushedResourceOwners;
        poppedResourceOwners = popped.poppedResourceOwners;

        super.rollbackTransactionImpl(poppedState);
    }

    // Javadoc inherited from ResourceOwner interface
    public void release() {
        if (inTransaction()) {
            RecoverableResourceOwnerStack clone =
                    (RecoverableResourceOwnerStack)clones.pop();
            clone.release();
        }
        releaseStack(stack);
        releaseStack(pushedResourceOwners);
        releaseStack(poppedResourceOwners);
    }


    /**
     * This helper method pops the content of a specified Stack and pushes
     * the popped Objects onto a specified target Stack.
     * @param target The Stack to populate with the sum of both Stacks.
     * @param stack The Stack to remove the contents from.
     */
    protected static void addStack(Stack target, Stack stack) {
        for (int i = 0; i < stack.size(); i++) {
            target.push(stack.get(i));
        }
        stack.clear();
    }

    /**
     * This method iterates over the contents of the specified Stack and calls
     * {@link ResourceOwner#release} on any ResourceOwner Objects encountered
     * before finally clearing the content of the Stack.
     * @param stack The Stack to release and clear.
     */
    protected static void releaseStack(Stack stack) {
        for (int i = stack.size() - 1; i >= 0; i--) {
            Object object = stack.get(i);
            if (object instanceof ReleasableResourceOwner) {
                ResourceOwner resourceOwner =
                        ((ReleasableResourceOwner)object).getResourceOwner();
                resourceOwner.release();
            }
        }
        stack.clear();
    }

    /**
     * This helper method is used to determine whether a specified
     * {@link ResourceOwner} exists in the specified Stack.  It may be either
     * contained directly or wrapped within a {@link ReleasableResourceOwner}
     * @param stack The Stack to search for the specified ResourceOwner.
     * @param resourceOwner The ResourceOwner to search for in the specified
     *                      Stack
     * @return true if the ResourceOwner exists in the Stack.
     */
    protected static boolean contains(Stack stack, ResourceOwner resourceOwner) {
        boolean result = false;
        for (int i = 0; i < stack.size() && !result; i++) {
            Object object = stack.get(i);
            if (object instanceof ResourceOwner && object == resourceOwner) {
                result = true;
            } else if (object instanceof ReleasableResourceOwner) {
                ResourceOwner contained =
                        ((ReleasableResourceOwner)object).getResourceOwner();
                if (contained == resourceOwner) {
                    result = true;
                }
            }
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 ===========================================================================
*/
