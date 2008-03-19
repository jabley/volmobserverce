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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.shared.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NotifyingList
        implements List {

    private final ListNotificationListener listener;
    private final List backing;

    public NotifyingList(ListNotificationListener listener, List backing) {
        this.listener = listener;
        this.backing = backing;
    }

    public int size() {
        return backing.size();
    }

    public void clear() {
        for (int i = 0; i < backing.size(); i++) {
            Object object = backing.get(i);
            listener.removingObject(this, object);
        }
        backing.clear();
    }

    public boolean equals(Object o) {
        return backing.equals(o);
    }

    public int hashCode() {
        return backing.hashCode();
    }

    public boolean isEmpty() {
        return backing.isEmpty();
    }

    public Object[] toArray() {
        return backing.toArray();
    }

    public Object get(int index) {
        return backing.get(index);
    }

    public Object remove(int index) {
        Object object = backing.remove(index);
        if (object != null) {
            listener.removingObject(this, object);
        }

        return object;
    }

    public void add(int index, Object element) {
        listener.addingObject(this, element);
        backing.add(index, element);
    }

    public int indexOf(Object o) {
        return backing.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return backing.lastIndexOf(o);
    }

    public boolean add(Object o) {
        listener.addingObject(this, o);
        return backing.add(o);
    }

    public boolean contains(Object o) {
        return backing.contains(o);
    }

    public boolean remove(Object o) {
        listener.removingObject(this, o);
        return backing.remove(o);
    }

    public boolean addAll(int index, Collection c) {
        addingCollection(c);
        return backing.addAll(index, c);
    }

    private void addingCollection(Collection c) {
        for (Iterator i = c.iterator(); i.hasNext();) {
            Object object = i.next();
            listener.addingObject(this, object);
        }
    }

    public boolean addAll(Collection c) {
        addingCollection(c);
        return backing.addAll(c);
    }

    public boolean containsAll(Collection c) {
        return backing.containsAll(c);
    }

    public boolean removeAll(Collection c) {
        for (Iterator i = c.iterator(); i.hasNext();) {
            Object object = i.next();
            listener.removingObject(this, object);
        }
        return backing.removeAll(c);
    }

    public boolean retainAll(Collection c) {
        boolean modified = false;
        for (Iterator iterator = this.iterator(); iterator.hasNext();) {
            Object object = iterator.next();
            if (!c.contains(object)) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }

    public Iterator iterator() {
        return new NotifyingIterator(backing.iterator());
    }

    public List subList(int fromIndex, int toIndex) {
        return new NotifyingList(listener, backing.subList(fromIndex, toIndex));
    }

    public ListIterator listIterator() {
        return new NotifyingListIterator(backing.listIterator());
    }

    public ListIterator listIterator(int index) {
        return  new NotifyingListIterator(backing.listIterator(index));
    }

    public Object set(int index, Object element) {
        Object old = backing.get(index);
        listener.replacingObject(this, old, element);
        return backing.set(index, element);
    }

    public Object[] toArray(Object a[]) {
        return backing.toArray(a);
    }

    private class NotifyingIterator implements Iterator {

        private final Iterator backing;
        private Object last;

        public NotifyingIterator(Iterator backing) {
            this.backing = backing;
        }

        public void remove() {
            listener.removingObject(NotifyingList.this, last);
            backing.remove();
        }

        public boolean hasNext() {
            return backing.hasNext();
        }

        public Object next() {
            last = backing.next();
            return last;
        }
    }

    private class NotifyingListIterator implements ListIterator {

        private final ListIterator backing;
        private Object last;

        public NotifyingListIterator(ListIterator backing) {
            this.backing = backing;
        }

        public int nextIndex() {
            return backing.nextIndex();
        }

        public int previousIndex() {
            return backing.previousIndex();
        }

        public void remove() {
            backing.remove();
        }

        public boolean hasNext() {
            return backing.hasNext();
        }

        public boolean hasPrevious() {
            return backing.hasPrevious();
        }

        public Object next() {
            last = backing.next();
            return last;
        }

        public Object previous() {
            last = backing.previous();
            return last;
        }

        public void add(Object o) {
            listener.addingObject(NotifyingList.this, o);
            backing.add(o);
        }

        public void set(Object o) {
            listener.replacingObject(NotifyingList.this, last, o);
            backing.set(o);
        }
    }
}
