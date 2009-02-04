/*
This file is part of Volantis Mobility Server.

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server. If not, see <http://www.gnu.org/licenses/>.
*/
/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2008.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime.scriptlibrarymanager;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;


/**
 * class responsible for registering library modules for widgets 
 */
public class WidgetScriptModules {

    public final static ScriptModule DAL = createAndRegisterDAL();
    public final static ScriptModule BASE_COMMON = createAndRegisterBaseCommon();
    public final static ScriptModule BASE_AJAX = createAndRegisterBaseAjax();
    public final static ScriptModule BASE_REFRESHABLE = createAndRegisterBaseRefreshable();
    public final static ScriptModule CONTROLS = createAndRegisterControls();
    public final static ScriptModule TRANSITIONS = createAndRegisterTransitions();
    public final static ScriptModule EFFECT_BASE = createAndRegisterEffectBase();
    public final static ScriptModule NAMED_COLORS = createAndRegisterNamedColors();
    public final static ScriptModule PARSE_COLOR = createAndRegisterParseColor();
    public final static ScriptModule FAKE_OPACITY = createAndRegisterFakeOpacity();    
    public final static ScriptModule CE_COMMON = createAndRegisterCECommon();
    public final static ScriptModule VALIDATE = createAndRegisterValidate();

     //custom effects modules
    public final static ScriptModule CE_PUFF =
            createAndRegisterCustomEffect("/vfc-ce-puff.mscr", new String[]{"puff"}, 1500, true);
    public final static ScriptModule CE_SHAKE =
            createAndRegisterCustomEffect("/vfc-ce-shake.mscr", new String[]{"shake", "fold-top-left"}, 1500, true);
    public final static ScriptModule CE_APPEAR =
            createAndRegisterCustomEffect("/vfc-ce-appear.mscr", new String[]{"appear", "fade"}, 599, true);
    public final static ScriptModule CE_FADE =
            createAndRegisterCustomEffect("/vfc-ce-fade.mscr", new String[]{"fade"}, 652, true);

    // slide module is included for wipe-top or wipe-bottom too. It is needed only for Opera Mobile browsers -
    // look for supportsScale method in vfc-transitions. This conditional require should be moved to capabilities in the future.
    // the same for "grow-center", "grow-...", etc.
    public final static ScriptModule CE_SLIDE = createAndRegisterCustomEffect("/vfc-ce-slide.mscr",
            new String[]{"slide-left", "slide-right", "slide-top","slide-bottom",
                    "wipe-top", "wipe-bottom",
                    "grow-bottom-right", "grow-bottom-left", "grow-center", "grow-top-left","grow-top-right",
                    "shrink-center", "shrink-top-left", "shrink-top-right", "shrink-bottom-left","shrink-bottom-right"}, 5500, true);
    public final static ScriptModule CE_DROPOUT =
            createAndRegisterCustomEffect("/vfc-ce-dropout.mscr", new String[]{"dropout"}, 800, true);

    //Maybe we should merge blinddown and blindup modules into one module as we cannot distinguishe where wipe efect exists
    // e.g. in :mcs-concealed{ mcs-effect-style: wipe-top} or in mcs-effect-style: wipe-top;
    // blinddown - is only appear affect, blindup is only disappear effect
    public final static ScriptModule CE_BLINDDOWN =
            createAndRegisterCustomEffect("/vfc-ce-blinddown.mscr", new String[]{"wipe-top", "wipe-bottom"}, 788, true);
    public final static ScriptModule CE_BLINDUP =
            createAndRegisterCustomEffect("/vfc-ce-blindup.mscr", new String[]{"wipe-top", "wipe-bottom"}, 785, true);
    public final static ScriptModule CE_GROW =
            createAndRegisterCustomEffect("/vfc-ce-grow.mscr",
                    new String[]{"grow-center", "grow-top-left", "grow-top-right",  "grow-bottom-left", "grow-bottom-right"}, 2200, true);
    public final static ScriptModule CE_SHRINK =
            createAndRegisterCustomEffect("/vfc-ce-shrink.mscr",
                    new String[]{"shrink-center", "shrink-top-left", "shrink-top-right", "shrink-bottom-left", "shrink-bottom-right"}, 2600, true);
    public final static ScriptModule CE_FOLD =
            createAndRegisterCustomEffect("/vfc-ce-fold.mscr", new String[]{"fold-top-left"}, 1200, true);    

    public final static ScriptModule CE_PULSATE =
                createAndRegisterCustomEffect("/vfc-ce-pulsate.mscr", new String[]{"pulsate"}, 1400, true);

    // building blocks
    public final static ScriptModule BASE_BB = createAndRegisterBaseBB();
    public final static ScriptModule BASE_BB_BUTTON = createAndRegisterBaseBBButton();
    public final static ScriptModule BASE_BB_DISMISS = createAndRegisterBaseBBDismiss();
    public final static ScriptModule BASE_BB_DISPLAY = createAndRegisterBaseBBDisplay();
    public final static ScriptModule BASE_BB_HANDLER = createAndRegisterBaseBBHandler();
    public final static ScriptModule BASE_BB_SCRIPT = createAndRegisterBaseBBScript();
    public final static ScriptModule BASE_BB_CONTENT = createAndRegisterBaseBBContent();
    public final static ScriptModule BASE_BB_CONTAINER = createAndRegisterBaseBBContainer();
    public final static ScriptModule BASE_BB_EFFECT = createAndRegisterBaseBBEffect();
    public final static ScriptModule BASE_BB_BLOCK = createAndRegisterBaseBBBlock();
    public final static ScriptModule BASE_BB_CONTROLS = createAndRegisterBaseBBControls();
    public final static ScriptModule BASE_BB_CONTROLS_SELECT = createAndRegisterBaseBBControlsSelect();
    public final static ScriptModule BASE_BB_CONTROLS_INPUT = createAndRegisterBaseBBControlsInput();
    public final static ScriptModule BASE_BB_LOAD = createAndRegisterBaseBBLoad();
    public final static ScriptModule BASE_BB_FETCH = createAndRegisterBaseBBFetch();
    public final static ScriptModule BASE_BB_REFRESH = createAndRegisterBaseBBRefresh();
    public final static ScriptModule BASE_BB_CLIENT = createAndRegisterBaseBBClient();
    public final static ScriptModule DEBUG = createAndRegisterDebug();    


    private static ScriptModule createAndRegisterDAL() {
        //size is evalutated as avarage over the script variants
        ScriptModule module = new ScriptModule("/vfc-base-DAL.mscr", Collections.EMPTY_SET,
                1179, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterFakeOpacity() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_COMMON);
        dependencies.add(NAMED_COLORS);
        dependencies.add(PARSE_COLOR);        
        ScriptModule transparency = new CapabilityBasedScriptModule("/vfc-base-utils-fake-opacity.mscr", dependencies,
                7500, true);
        ScriptModulesDefinitionRegistry.register(transparency);
        return transparency;
    }    

    private static ScriptModule createAndRegisterBaseCommon() {
        Set dependencies = new HashSet();
        dependencies.add(DAL);
        ScriptModule module = new ScriptModule("/vfc-base-common.mscr", dependencies,
                54000, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseAjax() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_COMMON);
        // module is required only
        // if widget:load,widget:fetch, widget:refresh, widget:autocomplete, widget-multivalidator exists in XDIME
        ScriptModule module = new ScriptModule("/vfc-base-ajax.mscr", dependencies, 8500, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseRefreshable() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_COMMON);
        // module is currently required by widgets: clock, carousel, progressbar
        ScriptModule module = new ScriptModule("/vfc-base-refreshable.mscr", dependencies, 2400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }
        
    private static ScriptModule createAndRegisterControls() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_COMMON);
        ScriptModule module = new ScriptModule("/controls.mscr", dependencies, 14600, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterTransitions() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_COMMON);
        ScriptModule module = new ScriptModule("/vfc-transitions.mscr", dependencies, 12200, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;        
    }

    private static ScriptModule createAndRegisterEffectBase() {
        Set dependencies = new HashSet();
        dependencies.add(TRANSITIONS);
        ScriptModule module = new ScriptModule("/vfc-effect-base.mscr", dependencies, 4400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterCECommon() {
        Set dependencies = new HashSet();
        dependencies.add(EFFECT_BASE);
        dependencies.add(FAKE_OPACITY);
        // module is required if mcs-effect-style differ from none or widget doesn't need effects like rounded corners
        ScriptModule module = new ScriptModule("/vfc-ce-common.mscr", dependencies, 23500, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterNamedColors() {
        ScriptModule module = new ScriptModule("/vfc-base-utils-named-colors.mscr", Collections.EMPTY_SET,
                3400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;        
    }

    private static ScriptModule createAndRegisterParseColor() {
        ScriptModule module = new ScriptModule("/vfc-base-utils-parse-color.mscr", Collections.EMPTY_SET,
                893, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterValidate() {
        ScriptModule module = new ScriptModule("/validate.mscr", Collections.EMPTY_SET, 9600, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBB() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_COMMON);
        ScriptModule module = new ScriptModule("/vfc-base-bb.mscr", dependencies, 2800, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBButton() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-button.mscr", dependencies,
                4800, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBDismiss() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-dismiss.mscr", dependencies,
                757, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBDisplay() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-display.mscr", dependencies,
                2300, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBHandler() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-handler.mscr", dependencies,
                823, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBScript() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-script.mscr", dependencies,
                344, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBContent() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);

        // conditionals effects modules
        dependencies.add(WidgetScriptModules.CE_APPEAR);
        dependencies.add(WidgetScriptModules.CE_BLINDDOWN);
        dependencies.add(WidgetScriptModules.CE_BLINDUP);
        dependencies.add(WidgetScriptModules.CE_DROPOUT);
        dependencies.add(WidgetScriptModules.CE_FADE);
        dependencies.add(WidgetScriptModules.CE_FOLD);
        dependencies.add(WidgetScriptModules.CE_GROW);
        dependencies.add(WidgetScriptModules.CE_PUFF);
        dependencies.add(WidgetScriptModules.CE_SHAKE);
        dependencies.add(WidgetScriptModules.CE_SHRINK);
        dependencies.add(WidgetScriptModules.CE_SLIDE);
        dependencies.add(WidgetScriptModules.CE_PULSATE);
        
        ScriptModule module = new ScriptModule("/vfc-base-bb-content.mscr", dependencies,
                3400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBContainer() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-container.mscr", dependencies,
                3900, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBEffect() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        dependencies.add(EFFECT_BASE);
        ScriptModule module = new ScriptModule("/vfc-base-bb-effect.mscr", dependencies,
                4600, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBBlock() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB_CONTENT);
        dependencies.add(BASE_BB_CONTAINER);
        dependencies.add(BASE_BB_EFFECT);
        
        // conditionals effects modules
        dependencies.add(WidgetScriptModules.CE_APPEAR);
        dependencies.add(WidgetScriptModules.CE_BLINDDOWN);
        dependencies.add(WidgetScriptModules.CE_BLINDUP);
        dependencies.add(WidgetScriptModules.CE_DROPOUT);
        dependencies.add(WidgetScriptModules.CE_FADE);
        dependencies.add(WidgetScriptModules.CE_FOLD);
        dependencies.add(WidgetScriptModules.CE_GROW);
        dependencies.add(WidgetScriptModules.CE_PUFF);
        dependencies.add(WidgetScriptModules.CE_SHAKE);
        dependencies.add(WidgetScriptModules.CE_SHRINK);
        dependencies.add(WidgetScriptModules.CE_SLIDE);
        dependencies.add(WidgetScriptModules.CE_PULSATE);
        
        ScriptModule module = new ScriptModule("/vfc-base-bb-block.mscr", dependencies,
                7700, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBControls() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-base-bb-controls.mscr", dependencies,
                2400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBControlsSelect() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB_CONTROLS);
        ScriptModule module = new ScriptModule("/vfc-base-bb-controls-select.mscr", dependencies,
                2300, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBControlsInput() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB_CONTROLS);
        ScriptModule module = new ScriptModule("/vfc-base-bb-controls-input.mscr", dependencies,
                1500, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBClient() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        dependencies.add(BASE_AJAX);
        ScriptModule module = new ScriptModule("/vfc-base-bb-client.mscr", dependencies,
                7400, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBLoad() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB_BLOCK);
        dependencies.add(BASE_AJAX);
        ScriptModule module = new ScriptModule("/vfc-base-bb-load.mscr", dependencies,
                2800, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBFetch() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB_BLOCK);
        dependencies.add(BASE_BB_LOAD);
        ScriptModule module = new ScriptModule("/vfc-base-bb-fetch.mscr", dependencies,
                2300, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterBaseBBRefresh() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB_BLOCK);
        dependencies.add(BASE_AJAX);
        ScriptModule module = new ScriptModule("/vfc-base-bb-refresh.mscr", dependencies,
                2500, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

    private static ScriptModule createAndRegisterDebug() {
        Set dependencies = new HashSet();
        dependencies.add(BASE_BB);
        ScriptModule module = new ScriptModule("/vfc-debug.mscr", dependencies, 4200, true);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }    

    // Conditional Custom Effects Modules
    private static ScriptModule createAndRegisterCustomEffect(String assetName, String[] effectNames,
                                                              int size, boolean cacheable) {
        Set dependencies = new HashSet();
        dependencies.add(CE_COMMON);
        ScriptModule module = new EffectScriptModule(assetName, effectNames, dependencies, size, cacheable);
        ScriptModulesDefinitionRegistry.register(module);
        return module;
    }

}
