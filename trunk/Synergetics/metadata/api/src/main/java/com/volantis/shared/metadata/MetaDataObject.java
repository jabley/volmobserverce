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

package com.volantis.shared.metadata;

import com.volantis.shared.metadata.immutable.ImmutableMetaDataObject;
import com.volantis.shared.metadata.mutable.MutableMetaDataObject;
import com.volantis.shared.inhibitor.Inhibitor;

import java.util.Collections;
import java.util.Set;


/**
 * Base interface for all meta data interfaces.
 *
 * <p>This interface defines behaviour common to all interfaces used within the
 * meta data package.</p>
 *
 * <h2>Mutable / Immutable</h2>
 *
 * <h3>Overview</h3>
 *
 * <p>It is often much better for an API to accept and return immutable
 * objects, i.e. those whose externally visible state can never change,
 * rather than mutable objects, i.e. those whose externally visible state can
 * be changed. The advantages are:</p>
 *
 * <dl>
 *     <dt><b>Safety</b></dt>
 *     <dd><p>It is much safer for both the API and the user of the API that
 * object state cannot be changed. They can store and share the objects at will
 * without having to remember to protect themselves by making defensive copies.
 * Immutable objects are also by definition thread safe so there will be none
 * of those hard to find problems where one thread modifies an object that
 * another thread relies upon.</p></dd>
 *
 *     <dt><b>Efficiency</b></dt>
 *     <dd><p>The fact that code does not have to make defensive copies of the
 * objects when they store and share them reduces the amount of garbage
 * generated.</p></dd>
 * </dl>
 *
 * <p>The disadvantages are:</p>
 *
 * <dl>
 *     <dt><b>Initialisation Complexity</b></dt>
 *     <dd><p>As objects are immutable it is harder to initialise them.
 * Simple immutable objects, e.g. {@link Integer} are easy to initialise
 * using a constructor. However, more complex objects, cannot be initialised
 * through the constructor and need some intermediate builder object that can
 * be populated with the state from which the immutable object can be
 * constructed.</p></dd>
 *
 *     <dt><b>Change Complexity</b></dt>
 *     <dd><p>As immutable objects are impossible to modify if a referencing
 * object needs to change an immutable object it needs to do it indirectly. It
 * must first copy its state into a mutable builder object, modify that state,
 * create a new immutable object and finally change its reference to point to
 * that.</p></dd>
 *
 *     <dt><b>Efficiency</b></dt>
 *     <dd><p>Modifying and initialising immutable objects creates garbage that
 * is not created when modifiying generally mutable objects. This needs to be
 * balanced against the amount of defensive copying that is needed when passing
 * around mutable objects to see whether the benefits outweight the
 * costs.</p></dd>
 * </dl>
 *
 * <h3>Design</h3>
 *
 * <p>Unfortunately, Java does not have any direct support for this sort of
 * pattern and there are a number of different patterns in common use, all with
 * advantages and disadvantages.</p>
 *
 * <p>The pattern that is used in the meta data API is to have three interfaces
 * for each basic object type that needs to be immutable. There is one
 * 'abstract' interface that is the base for the other two and represents all
 * objects of that type and may be either mutable, or immutable. It provides
 * read only accessors to the object state as that is common across both
 * mutable and immutable objects. It also provides methods to create both
 * mutable and immutable instances of the object, which are represented by the
 * two other interfaces.</p>
 *
 * <p>The base interface is abstract in the sense that there are no
 * implementations of it that are not also either implementations of the
 * mutable or immutable interfaces.</p>
 *
 * <p>The behaviour of the method to create an immutable instance is dependent
 * on whether the object it is called on is immutable or mutable. If it is
 * immutable then it will simply return a reference to itself. If it is mutable
 * then it will either construct a new immutable instance, or if it had
 * previously constructed (or been initialised by) an immutable instance and
 * its state had not changed then it may return a previously constructed
 * immutable instance.</p>
 *
 * <p>The method to create a mutable instance will always create a new mutable
 * instance even if it is called on a mutable instance.</p>
 *
 * <h4>Immutable Interface</h4>
 *
 * <p>As the read only accessors are defined on the base interface the
 * immutable interface is a marker interface. It defines an implicit contract
 * that instances that implement that interface cannot be modified. An
 * additional aspect of this contract is that immutable implementations must
 * be thread safe. That means that if they lazily create representations of
 * their external state they must ensure that the creation code is suitably
 * synchronized.</p>
 *
 * <h4>Mutable Interface</h4>
 *
 * <p>The mutable interface defines any mutating accessors that are needed to
 * update the state of the object. It can be used as the builder for immutable
 * objects. Mutable implementations are not thread safe and are not recommended
 * for use in situations where they may be accessed by multiple threads, that
 * is part of the reason for supporting immutable objects in the first place.
 * If they have to be accessed by multiple threads then it is the
 * responsibility of the accessing code to synchronize access to it.</p>
 *
 * <h4>Example</h4>
 *
 * <p>The following diagram shows an interface hierarchy for two classes,
 * <code>Foo</code> and <code>Bar</code>, showing the base interfaces and the
 * immutable and mutable interfaces.</p>
 *
 * <pre>
 *                          +-----+
 *                      +-> | Foo | &lt;-+
 *  +--------------+   /    +-----+    \   +------------+
 *  | ImmutableFoo | -+        ^        +- | MutableFoo |
 *  +--------------+           |           +------------+
 *         ^                +-----+              ^
 *         |            +-> | Bar | &lt;-+          |
 *  +--------------+   /    +-----+    \   +------------+
 *  | ImmutableBar | -+                 +- | MutableBar |
 *  +--------------+                       +------------+
 * </pre>
 *
 * <p>The convertion is for the base interface to have an appropriate name for
 * what it is representing and for the mutable and immutable interfaces to be
 * prefixed by <code>Mutable</code> and <code>Immutable</code> and also to be
 * within the <code>mutable</code> and <code>immutable</code> sub packages
 * respectively.</p>
 *
 * <h4>Containers</h4>
 *
 * <p>Container objects (i.e. any object that references another object that
 * can be immutable) typically hold references to immutable objects. Adding
 * a mutable object to the container will result in an immutable object being
 * retrieved and stored instead. This ensures that the container object is
 * always in a consistent state and simplifies the API as there is no need to
 * provide separate methods for retrieving mutable and immutable instances of
 * objects stored within the container.</p>
 *
 * <p>This has a number of consequences on the design and usage of the API:</p>
 * <ul>
 * <li><p>Object hierarchies have to be constructed from the bottom up. i.e.
 * contained objects need to be initialised before they are added to their
 * container as it is not possible to modify them once they are
 * attached.</p></li>
 *
 * <li><p>Mutable instances of immutable objects are typically constructed as
 * shallow copies, i.e. objects referenced from a mutable instance are not
 * copied (as they have to be immutable).</p></li>
 *
 * </ul>
 *
 * <p>The only exception to this are those objects that form part of a
 * reference cycle as they need a different approach as they cannot be
 * constructed from the bottom up as required by this approach. Fortunately
 * this structure is not required for the meta data API so will not be
 * discussed further.</p>
 *
 * <h4>Referencing Other Objects</h4>
 *
 * <p>Not all objects referenced from within an immutable object are going to
 * be of this same pattern, some are going to be completely immutable, e.g.
 * {@link String} and some are going to normally be mutable, e.g. {@link Set}.
 * The completely immutable objects can be referenced without any problems but
 * care needs to be taken with the mutable objects as exposing them from within
 * an immutable object would break its immutability.</p>
 *
 * <p>Ideally an immutable version of the mutable object should be created
 * using the pattern described here. However, if that is not possible then
 * there are a number of approaches that can be taken. The correct one to use
 * depends on the situation.</p>
 * <ul>
 * <li>Implement (or extend) the mutable interface (or class) but make the
 * mutators unusable by causing them to throw an exception when called.</li>
 * <li>If the mutable object is an instance of a final class then a wrapper
 * class could be created (using the pattern here) that provides an immutable
 * view on the original object.</li>
 * <li>Defensive copies could be made when storing and returning instances of
 * the mutable object.</li>
 * </ul>
 *
 * <p>In all cases it is the responsibility of the containing class to manage
 * the state of any mutable objects that it references. Either by ensuring it
 * is suitably wrapped, or performing the defensive copying.</p>
 *
 * <p>Java collections and maps are very common examples of mutable objects
 * that may be referenced and exposed from within an immutable object.
 * Immutable objects should use the <code>umodifiable...</code> methods within
 * {@link Collections} class to create an umodifiable wrapper that can be
 * returned from immutable objects without violating their immutability.</p>
 *
 * <h3>Usage</h3>
 *
 * <p>The three interfaces have very specific contracts and the appropriate one
 * should be used wherever there is a reference to the object.</p>
 *
 * <ul>
 * <li>Methods that always modify a parameter should use the mutable
 * interface.</li>
 * <li>Methods that never need to modify the parameter should use the immutable
 * interface.</li>
 * <li>Methods that may need to modify the paramater should either use the
 * mutable interface, or use a wrapper that contains a reference to any of
 * the interfaces, or use any of the interfaces and return the potentially
 * modified object.</li>
 * <li>Methods that do not care whether an object is mutable, or immutable
 * should use the base interface. If they need to store an instance of it then
 * they can call the method to create an immutable object.</li>
 * </ul>
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with future releases of the product at both
 * binary and source levels. </strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface MetaDataObject extends Inhibitor {

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 14-Jan-05	6560/5	tom	VBM:2004122401 Added Inhibitor base class

 10-Jan-05	6560/3	tom	VBM:2004122401 Began implementation of the new Metadata API

 ===========================================================================
*/
