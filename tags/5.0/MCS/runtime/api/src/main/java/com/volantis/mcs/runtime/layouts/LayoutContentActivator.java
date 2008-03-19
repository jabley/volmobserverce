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

package com.volantis.mcs.runtime.layouts;

import com.volantis.mcs.layouts.DeviceLayoutReplicator;
import com.volantis.mcs.layouts.DeviceLayoutUpdater;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.activator.FormatActivator;
import com.volantis.mcs.layouts.activator.FormatActivatorFactoryImpl;
import com.volantis.mcs.layouts.activator.RecursingFormatActivatorImpl;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.layout.InternalLayoutContent;
import com.volantis.mcs.runtime.layouts.styling.LayoutStyleSheetBuilder;
import com.volantis.mcs.runtime.layouts.styling.LayoutStyleSheetBuilderImpl;
import com.volantis.mcs.runtime.layouts.styling.LayoutStyleSheetCompilerFactory;
import com.volantis.mcs.runtime.policies.theme.StyleSheetActivator;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LayoutContentActivator {
    /**
     * Used for logging.
     */
     private static final LogDispatcher logger =
             LocalizationFactory.createLogger(LayoutActivator.class);

    private final LayoutStyleSheetBuilder styleSheetBuilder;
    private final StyleSheetCompilerFactory styleSheetCompilerFactory;
    private final DeviceLayoutUpdater deviceLayoutUpdater;

    public LayoutContentActivator() {
        this(new LayoutStyleSheetBuilderImpl(),
                LayoutStyleSheetCompilerFactory.getDefaultInstance(),
                new DeviceLayoutUpdater());
    }

    public LayoutContentActivator(
            LayoutStyleSheetBuilder styleSheetBuilder,
            StyleSheetCompilerFactory stylingFactory,
            DeviceLayoutUpdater deviceLayoutUpdater) {

        this.styleSheetBuilder = styleSheetBuilder;
        this.styleSheetCompilerFactory = stylingFactory;
        this.deviceLayoutUpdater = deviceLayoutUpdater;
    }

    public ActivatedLayoutContent activateLayoutContent(
            StyleSheetActivator styleSheetActivator,
            InternalLayoutContent layoutContent) {

        Layout layout = layoutContent.getLayout();

        deviceLayoutUpdater.update(layout);

        // Make sure that the default fragment is set.
        setDefaultFragment(layout);

        // Activate all the formats.
        FormatActivatorFactoryImpl factory = new FormatActivatorFactoryImpl();
        FormatActivator activator = new RecursingFormatActivatorImpl(factory);
        Format rootFormat = layout.getRootFormat();
        activator.activate(rootFormat);

        // create container name -> {fragment, index} map
        final Map containerNameToFragments =
            buildContainerNameToPositionMap(rootFormat);

        // Handle any replicas.
        DeviceLayoutReplicator replicator = new DeviceLayoutReplicator();
        replicator.replicate(layout);


        // Create a StyleSheet from the layout and compile it.
        StyleSheet styleSheet =
                styleSheetBuilder.build(layout);

        styleSheetActivator.activate(styleSheet);

        StyleSheetCompiler compiler =
                styleSheetCompilerFactory.createStyleSheetCompiler();
        CompiledStyleSheet compiledStyleSheet =
                compiler.compileStyleSheet(styleSheet);

        return new ActivatedLayoutContent(layout, compiledStyleSheet,
                containerNameToFragments);
    }

    private Map buildContainerNameToPositionMap(final Format rootFormat) {

        final Map fragmentToCounter = new HashMap();
        FormatVisitorAdapter visitor = new FormatVisitorAdapter() {
            public boolean visitFormat(final Format format, final Object object) {
                if (format instanceof Pane || format instanceof Region) {
                    final Fragment fragment = format.getEnclosingFragment();
                    if (fragment != null) {
                        final String containerName = format.getName();

                        Counter counter =
                            (Counter) fragmentToCounter.get(fragment);
                        if (counter == null) {
                            counter = new Counter();
                            fragmentToCounter.put(fragment, counter);
                        }

                        final Map map = (Map) object;
                        List fragments = (List) map.get(containerName);
                        if (fragments == null) {
                            fragments = new LinkedList();
                            map.put(containerName, fragments);
                        }
                        fragments.add(new ContainerPosition(fragment,
                            counter.getCount()));

                        counter.increment();
                    }
                }
                return false;
            }
        };
        final Map containerNameToFragments = new HashMap();
        try {
            if (rootFormat != null) {
                visitor.visitFormatChildren(rootFormat, containerNameToFragments);
            }
        } catch (FormatVisitorException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot traverse format hierarchy");
        }
        return containerNameToFragments;
    }

    /**
     * Sets the default fragment to the first fragment in the format tree if
     * the layout has a null value as a default fragment
     *
     * @param layout
     */
    private void setDefaultFragment(Layout layout) {
        if (layout.getDefaultFragmentName() == null) {
            layout.setDefaultFragmentName(getFirstFragment(layout.getRootFormat()));
            if (logger.isDebugEnabled()) {
                logger.debug("Default fragment set to " +
                        layout.getDefaultFragmentName());
            }
        }
    }

    /**
     * Finds the first fragment in a format tree and returns the name
     *
     * @param format
     * @return The first fragment or null if no fragments exist.
     */
    protected String getFirstFragment(Format format) {
        String retVal = null;
        if (null != format) {
            if (!(format instanceof Fragment)) {
                int x = format.getNumChildren();
                for (int y = 0; y < x; y++) {
                    Format childFormat = format.getChildAt(y);
                    retVal = getFirstFragment(childFormat);
                    if (retVal != null) {
                        break;
                    }
                }
            } else {
                retVal = format.getName();
            }
        }
        return retVal;
    }


    /**
     * Class to store a container position in a fragment. It contains the
     * fragment and the index of the container.
     */
    public static class ContainerPosition {
        private final Fragment fragment;
        private final int index;

        public ContainerPosition(final Fragment fragment, final int index) {

            this.fragment = fragment;
            this.index = index;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public int getIndex() {
            return index;
        }
    }

    /**
     * Simple counter class.
     */
    private class Counter {
        int count;

        public void increment() {
            count++;
        }

        public int getCount() {
            return count;
        }
    }
}
