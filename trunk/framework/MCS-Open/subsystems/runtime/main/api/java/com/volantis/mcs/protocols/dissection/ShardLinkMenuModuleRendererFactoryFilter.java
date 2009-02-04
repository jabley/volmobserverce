package com.volantis.mcs.protocols.dissection;

import com.volantis.mcs.protocols.menu.MenuModuleRendererFactoryFilter;
import com.volantis.mcs.protocols.menu.MenuModuleRendererFactory;

/**
 * Created by IntelliJ IDEA.
 * User: mrybak
 * Date: Aug 13, 2008
 * Time: 2:48:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShardLinkMenuModuleRendererFactoryFilter implements MenuModuleRendererFactoryFilter {

    public MenuModuleRendererFactory decorate(MenuModuleRendererFactory rendererFactory) {
        return new ShardLinkMenuModuleRendererFactory(rendererFactory);
    }
}
