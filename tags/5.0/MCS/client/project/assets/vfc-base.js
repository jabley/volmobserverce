/**
 * (c) Volantis Systems Ltd 2006. 
 */

/**
 * A package.
 */
var Package = {
  /**
   * Defines and returns new package. If no content is specified (null),
   * it defines empty package. Otherwise it defines a package with specified
   * content.
   *
   * Example: To define empty package, do the following:
   *
   *  MyPackage = Package.define()
   *
   * Example: To define package with some content in it (package fields or methods),
   * do the following:
   *
   *  MyPackage = Package.define({
   *    myField1: null,
   *    myField2: null,
   *
   *    myMethod1: function() {...},
   *    myMethod2: function() {...}
   *  })
   */
  define: function(content) {
    if (content == null) {
      return {};
    } else {
      return content;
    }
  }
};

Object.copyFields = function (destination, source) {
  for (var property in source) {
    if (destination[property] !== undefined
        && typeof(destination[property]) != 'function') {
        destination[property] = source[property];
    }
  }
  return destination;
};

// Define additional method on Element class.
Element.addMethods({
  /*
   * Replaces styles on an Element with specified styles,
   * remembering the original ones.
   * Styles may be reverted to originals by calling a
   * revertStyle() method.
   * Note, that if this method is called twice in a row,
   * styles are always reverted to originals first.
   *
   * Example: Following example illustrates how to replace styles
   * and then revert them to originals:
   *
   *  vfcReplaceStyle(element, {'background-color': 'red', 'font-weight': 'bold'})
   *  vfcRevertStyle(element)
   *
   * @param element The element to replace styles for
   * @param style The styles to replace (style names can not be camelized, as in setStyle())
   */
  vfcReplaceStyle: function(element, style) {
    element = $(element);
    element.vfcRevertStyle();
    element.vfcReplacedStyle = {};
    for (var name in style) {
      cname = name.camelize();
      element.vfcReplacedStyle[name] = element.style[cname];
      element.style[cname] = style[name];
    }
  },

  /*
   * Revert styles before last replaceStyle() invokation.
   * If replaceStyle() has not been called, it does nothing.
   */
  vfcRevertStyle: function(element) {
    element = $(element);
    style = element.vfcReplacedStyle;
    if (style) {
      for(var name in style) {
        cname = name.camelize();
        element.style[cname] = style[name];
      }
      element.replacedStyle = null;
    }
  },

  vfcGetDimensions: function(element) {
    element = $(element);
    if (Element.getStyle(element, 'display') != 'none')
      return {width: element.offsetWidth, height: element.offsetHeight};

    // All *Width and *Height properties give 0 on elements with display none,
    // so enable the element temporarily
    var els = element.style;
    var originalVisibility = els.visibility;
    var originalPosition = els.position;
    els.visibility = 'hidden';
    els.position = 'absolute';
    els.display = '';
    var originalWidth = element.offsetWidth;
    var originalHeight = element.offsetHeight;
    els.display = 'none';
    els.position = originalPosition;
    els.visibility = originalVisibility;
    return {width: originalWidth, height: originalHeight};
  },

  /* calculate real dimensions in PX units
  * return object with all needed dimensions .i.e. with borders, padding and content dimensions
  * results.width -
  * result.height
  * result.borderLeftWidth
  * result.borderRightWidth
  * result.borderTopWidth
  * result.borderBottomWidth
  * result.paddingLeft
  * result.paddingRight
  * result.paddingTop
  * result.paddingBottom
  * result.contentWidth
  * result.contentHeight -
  */
  vfcGetRealDimensions: function(element) {
    var result = {};
    //if element has display value none or not none
    var isInDom = true;
    element = $(element);

    // All *Width and *Height properties give 0 on elements with display none,
    // enable the element temporarily
    if (Element.getStyle(element, 'display') == 'none') {
      isInDom = false;

      // display:block is used instead display:'' because in some cases display is set by inline style and additional by CSS class (date-picker).
      // if we change only inline style to display:'' element get display:none from CSS class and it is still impossible to read offsetWidth.
      // this method is used only for reading dimensions and should works good also for inline element, but it is not tested for it
      element.vfcReplaceStyle({visibility: 'hidden', position: 'absolute', display:'block'})
    }

    result.width = element.offsetWidth;
    result.height = element.offsetHeight;

    //get others dimensions

    var borderLeftWidthOld = 0;
    var borderRightWidthOld = 0;
    var borderTopWidthOld = 0;
    var borderBottomWidthOld = 0;

    var paddingLeftOld = 0;
    var paddingRightOld = 0;
    var paddingTopOld = 0;
    var paddingBottomOld = 0;

    //get  borders dimensions
    if(Element.getStyle(element, 'border-left-width') != undefined) {
      borderLeftWidthOld = Element.getStyle(element, 'border-left-width');
      Element.setStyle(element, {borderLeftWidth: '0px'});
    }

    result.borderLeftWidth = result.width - element.offsetWidth;

    if(Element.getStyle(element, 'border-right-width') != undefined) {
      borderRightWidthOld = Element.getStyle(element, 'border-right-width');
      Element.setStyle(element, {borderRightWidth: '0px'});
    }

    result.borderRightWidth = result.width - element.offsetWidth - result.borderLeftWidth;

    if(Element.getStyle(element, 'border-top-width') != undefined) {
      borderTopWidthOld = Element.getStyle(element, 'border-top-width');
      Element.setStyle(element, {borderTopWidth: '0px'});
    }

    result.borderTopWidth = result.height - element.offsetHeight;

    if(Element.getStyle(element, 'border-bottom-width') != undefined) {
      borderBottomWidthOld = Element.getStyle(element, 'border-bottom-width');
      Element.setStyle(element, {borderBottomWidth: '0px'});
    }

    result.borderBottomWidth = result.height - element.offsetHeight - result.borderTopWidth;

    //get  paddings dimensions
    if(Element.getStyle(element, 'padding-left') != undefined) {
      paddingLeftOld = Element.getStyle(element, 'padding-left');
      Element.setStyle(element, {paddingLeft: '0px'});
    }

    result.paddingLeft = result.width - element.offsetWidth - result.borderLeftWidth -  result.borderRightWidth;

    if(Element.getStyle(element, 'padding-right') != undefined) {
      paddingRightOld = Element.getStyle(element, 'padding-right');
      Element.setStyle(element, {paddingRight: '0px'});
    }

    result.paddingRight = result.width - element.offsetWidth - result.borderLeftWidth -  result.borderRightWidth - result.paddingLeft;

    if(Element.getStyle(element, 'padding-top') != undefined) {
      paddingTopOld = Element.getStyle(element, 'padding-top');
      Element.setStyle(element, {'padding-top': '0px'});
    }

    result.paddingTop = result.height - element.offsetHeight - result.borderTopWidth - result.borderBottomWidth;

    if(Element.getStyle(element, 'padding-bottom') != undefined) {
      paddingBottomOld = Element.getStyle(element, 'padding-bottom');
      Element.setStyle(element, {paddingBottom: '0px'});
    }

    result.paddingBottom = result.height - element.offsetHeight - result.borderTopWidth - result.borderBottomWidth - result.paddingTop;

    result.contentWidth = element.offsetWidth;
    result.contentHeight = element.offsetHeight;

    //revert oryginal values

    Element.setStyle(element, {borderLeftWidth: borderLeftWidthOld});
    Element.setStyle(element, {borderRightWidth: borderRightWidthOld});
    Element.setStyle(element, {borderTopWidth: borderTopWidthOld});
    Element.setStyle(element, {borderBottomWidth: borderBottomWidthOld});

    Element.setStyle(element, {paddingLeft: paddingLeftOld});
    Element.setStyle(element, {paddingRight: paddingRightOld});
    Element.setStyle(element, {paddingTop: paddingTopOld});
    Element.setStyle(element, {paddingBottom: paddingBottomOld});

    if(! isInDom) {
      element.vfcRevertStyle();
    }
    return result;
  },

  isVisible : function(element){
    var visible = true;
    if(Element.isParentVisible == undefined){
        // special method because element could not be full element that extends prototype Element
        // so we need static method Element.isParentVisible
       Element.isParentVisible = function(element){
          if (document.defaultView && document.defaultView.getComputedStyle) {
            var css = document.defaultView.getComputedStyle(element, null);
            if((null == css) && (Prototype.nokiaOSSBrowser())){
                // bug in Nokia OSS - if element has display:block then
                // result of getcomputedStyle for element is null
                return false;
            }
            var display = css ? css.getPropertyValue('display') : null;
            if(display == 'none'){
              return false;
            }
          } else if(element.currentStyle['display'] == 'none'){
            return false;
          }
          if(element.parentNode == null || element.parentNode.style == undefined) {
            return true;
          } else {
            return Element.isParentVisible(element.parentNode);
          }
        }
    }
    visible = Element.isParentVisible(element);
    if(visible){
      if (document.defaultView && document.defaultView.getComputedStyle) {
        var css = document.defaultView.getComputedStyle(element, null);
        var visibility = css ? css.getPropertyValue('visibility') : null;

        if(visibility == 'hidden'){
            visible = false;
        }
      } else {
        if(element.currentStyle['visibility'] == 'hidden'){
          visible = false;
        }
      }
    }
    return visible;
  },

  /**
   * return real value of style, not replaced by null when real value is auto.
   */
  getNotNullStyle: function(element, style) {
    element = $(element);
    var value = element.style[style.camelize()];
    var isStyleChanged = false;
    if((Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser() || Prototype.konquerorBrowser() || Prototype.operaPC()) && element.style.display=='none') {
      element.style.visibility='hidden';
      element.style.display='block';
      isStyleChanged = true;
    }

    if (!value) {
      // this workaround for opera PC older than 9.02 version.
      // The issue is if element change display:none to display:block
      // it is required some time of delay for getting property value which are computed
      // after putting element into DOM (by set display:block)
      // The problem is only with computed values like height, width. It seems that these browsers
      // needs some time about 200 miliseconds to resize document's layout ade refresh its computed style object.
      // There is additional condition with time because getComputedStyle always return 0px
      // in case of really value of property is 0px, before and after time for refresh (about 200ms) in that case it is
      // not possible to recognize whether returned value is correct
      if (document.defaultView && document.defaultView.getComputedStyle) {
        var css = document.defaultView.getComputedStyle(element, null);
        if(/Opera\/9.00|Opera\/9.01|Opera\/9.02/.test(navigator.userAgent)) {
          if (!value) {
            var now = new Date();
            var exitTime = now.getTime() + 300;
            while(true) {
        value = css ? css.getPropertyValue(style) : null;
              now = new Date();
              if(value != '0px' || now.getTime() > exitTime) {
                  break;
              }
            }
          }
        } else {
          value = css ? css.getPropertyValue(style) : null;
        }
      } else if (element.currentStyle) {
        value = element.currentStyle[style.camelize()];
      }
    }

    if (window.opera && ['left', 'top', 'right', 'bottom'].include(style))
      if (Element.getStyle(element, 'position') == 'static') value = 'auto';

    if(isStyleChanged) {
      element.style.visibility='visible';
      element.style.display='none';
    }
    // always return  not null value
    return value;
  }
});

// Define additional method on Position class.
Object.extend(Position, {
  /*
  * Override Position.clone method from prototype
  */
  vfcClone: function(source, target) {
    source = $(source);
    target = $(target);
    target.style.position = 'absolute';
    var offsets = this.cumulativeOffset(source);
    target.style.top    = offsets[1] + source.offsetHeight + 'px';
    target.style.left   = offsets[0] + 'px';
    // if is not set by user, set default value - width from source element
    if(! target.style.width) {
      target.style.width  = source.offsetWidth + 'px';
    }
  },

  /**
   * Method similar to positionOffset but including fixed elements
   * in calculation alghoritms.
   */
  fixedOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;
      element = element.offsetParent;
      if (element) {
        p = Element.getStyle(element, 'position');
        if (p == 'relative' || p == 'absolute' || p == 'fixed') break;
      }
    } while (element);
    return [valueL, valueT];
  }
});

var Widget = Package.define({

  init: function() {
    if (!this.internalRegister && !this.internalGetInstance) {

      // Private registry of widgets
      var registry = {};

      this.internalRegister = function (id, widget) {
        registry[id] = widget;

        // Store the ID, under which the widget was registered.
        // This is used by properties to serialize widget to a string.
        widget._widgetId = id
      };

      this.internalGetInstance = function (id) {
		return registry[id];
      };
    }
  },

  /**
   * Registers widget under the specified id,
   * and returns registered widget.
   *
   * @volantis-api-include-in PublicAPI
   */
  register: function(id, widget) {
    this.init();
    this.internalRegister(id, widget);
    return widget
  },

  /**
   * Returns a widget registered under the specified id
   *
   * @volantis-api-include-in PublicAPI
   */
  getInstance: function(id) {
    this.init();
    return this.internalGetInstance(id);
  },

  /**
   * Creates and returns a string, which will equal to the
   * string representation of the HTML element stored
   * in the document under specified ID.
   */
  createString: function(id) {
    var element = $(id)

    var nodes = element.childNodes;

    var string = "";

    for(var i=0; i<nodes.length; i++)
      string += nodes[i].nodeValue

    return string
  },

  /**
   * Checks if AJAX updates are enabled.
   * Returns true, if polling is enabled.
   *
   * @volantis-api-include-in PublicAPI
   */
  isPollingEnabled: function() {
    // Note: polling is enabled by default, if the field value
    // is uninitialised.
    return (this.internalPollingDisabled === undefined) ||
            !this.internalPollingDisabled
  },


  /**
   * Enables/disables AJAX updates for all widgets.
   *
   * @volantis-api-include-in PublicAPI
   */
  setPollingEnabled: function(enabled) {
    var old = this.internalPollingDisabled
    this.internalPollingDisabled = !enabled

    if (old != this.internalPollingDisabled) {
      if (this.pollingEnabledCallbackHandler != null) {
        this.pollingEnabledCallbackHandler.invoke(enabled)
      }
    }
  },

  /**
   * Adds callback function, which will be invoked when polling
   * enabled status is changed.
   */
  addPollingEnabledCallback: function(callback) {
    if (this.pollingEnabledCallbackHandler == null) {
      this.pollingEnabledCallbackHandler = new Widget.CallbackHandler()
    }

    this.pollingEnabledCallbackHandler.add(callback)
  },

  /*
   * Adds an item (function), which will be invoked after the page is loaded.
   * All functions added using this method will be invoked in the order
   * of addition.
   */
  addStartupItem: function(func) {
    // Create an array of startup items, if not already created.
    if (!this.internalStartupItems) {
      this.internalStartupItems = new Array()
    }

    // Add startup items to the array.
    this.internalStartupItems.push(func)
  },

  /*
   * Widget startup.
   *
   * Invokes all startup items (functions), and performs all
   * nessecarry startup actions.
   *
   * Important! This method MUST be invoked as a response
   * to the 'load' event of the 'body' element, like in
   * the following example:
   *
   * <body onload="Widget.startup()">
   */
  startup: function() {
    if (this.internalStartupItems) {
      for (var index = 0; index < this.internalStartupItems.length; index++) {
        this.internalStartupItems[index]()
      }
    }
  },

  // add given obsever func to each focusable element inside tree
  addObserversToFocusableElements: function(element,event,func){
    var focusableElementsCounter = 0;
    var focusableElementsList = ['a','area','button','input','object', 'select',
            'textarea', 'label'];
    var nodes;
    for(var i = 0;i<focusableElementsList.length;i++){
      nodes = element.getElementsByTagName(focusableElementsList[i]);
      for (var j=0;j<nodes.length;j++) {
        focusableElementsCounter++;
        Widget.addElementObserver(nodes[j], event, func);
      }
    }
    return focusableElementsCounter;
  },

  addElementObserver: function(element,event,func){
    if(Prototype.useMouseAsSelect()){
      var new_event = event;
      if(Prototype.nokiaOSSBrowser()){
        if(event == Widget.BLUR){
          new_event = Widget.MOUSEOUT;
        } else if(event == Widget.FOCUS){
          new_event = Widget.MOUSEOVER;
        }
      } else if(Prototype.operaMobile()){
        if((element.nodeName.toLowerCase() == 'input') &&
          (element.type.toLowerCase() == 'text')){
          if(event == Widget.BLUR){
            new_event = Widget.MOUSEOUT;
          } else if(event == Widget.FOCUS){
            new_event = Widget.MOUSEOVER;
          } else if(event == Widget.CLICK) {
            new_event = Widget.FOCUS;
          }
        }
      }
      Event.observe(element,new_event,func);
    } else {
      Event.observe(element,event,func);
    }
  },

  /*
   * @param element DOM element
   * @param eventName event name, should be used only "focus" or "blur"
   * @param funct function binded as event listener
   */
  observeFocusableElement: function(element, event, func) {
    if (Prototype.netFront()) {
      //on NetFront there is a problem with propagation of event listeners
      //according to: http://www.w3.org/TR/1998/REC-html40-19980424/interact/forms.html#h-17.11
      //following elements are focusable: A, AREA, BUTTON, INPUT, OBJECT, SELECT, and TEXTAREA.
      this.addObserversToFocusableElements(element,event,func);
    } else {
      Widget.addElementObserver(element, event, func);
  }
  },

  removeElementObserver: function(element,event,func){
    if(Prototype.useMouseAsSelect()){
      var new_event = event;
      if(Prototype.nokiaOSSBrowser()){
        if(event == Widget.BLUR){
          new_event = Widget.MOUSEOUT;
        } else if(event == Widget.FOCUS){
          new_event = Widget.MOUSEOVER;
        }
      } else if(Prototype.operaMobile()){
        if((element.nodeName.toLowerCase() == 'input') &&
          (element.type.toLowerCase() == 'text')){
          if(event == Widget.BLUR){
            new_event = Widget.MOUSEOUT;
          } else if(event == Widget.FOCUS){
            new_event = Widget.MOUSEOVER;
          } else if(event == Widget.CLICK) {
            new_event = Widget.FOCUS;
          }
        }
      }
      Event.stopObserving(element,new_event,func);
    } else {
      Event.stopObserving(element,event,func);
    }
  },

  removeObserversFromFocusableElements: function(element,event,func){
    var focusableElementsCounter = 0;
    var focusableElementsList = ['a','area','button','input','object', 'select',
            'textarea', 'label'];
    var nodes;
    for(var i = 0;i<focusableElementsList.length;i++){
      nodes = element.getElementsByTagName(focusableElementsList[i]);
      for (var j=0;j<nodes.length;j++) {
        focusableElementsCounter++;
        Widget.removeElementObserver(nodes[j], event, func);
      }
    }
    return focusableElementsCounter;
  },

  stopObservingFocusableElement: function(element, event, func){
    if (Prototype.netFront()) {
      this.removeObserversFromFocusableElements(element,event,func);
    } else {
      Widget.removeElementObserver(element, event, func);
    }
  },

  stopEventPropagation: function(evt) {
    if (Prototype.msieBrowser()) {
      evt.cancelBubble=true;
    } else {
      evt.stopPropagation();
    }
  },

 /*
  * Make element focusable
  */
  makeFocusable: function(el) {
    var imp;
    if(Prototype.netFront()) {
    	// On NetFront browser, a new 1px width, focusable element is inserted,
    	// which will catch the focus.
        imp = document.createElement("input");
        imp.type="button";
        imp.style.width='1px';
        imp.style.height='1px';
        imp.style.backgroundColor='transparent';
        imp.style.border='0px';
        el.appendChild(imp);
    } else if (Prototype.firefoxBrowser() ||  Prototype.msieBrowser()) {
        // On Mozilla and IE, the tabIndex property of the element has special meaning.
        // For more info see: http://developer.mozilla.org/en/docs/Key-navigable_custom_DHTML_widgets
        el.tabIndex = "0"
        imp = el;
    }
    if(Prototype.operaMobile()) {
      //bind Click handler only in order to make element focusable. DIV and SPAN is focusable on Opera mobile if has any handler bind to click event
      Widget.addElementObserver(el, Widget.CLICK, void(0));
      imp = el;
    }
    return imp;
  },

 /*
  * Returns dimension in px unit from dimension set by others units -  otherUnitSize
  */
  getPXDimension: function(otherUnitSize) {
    // Width of a single character will be calculated by
    // temporarily creating div element, set size width
    // and measuring its actual width in pixels.
    var resultPX = 0;
    // If width is not already cached, calculate it now.
    // First, create an invisible span element containing 'M' contents.
    this.mElement = document.createElement("div")
    // Make an element invisible.
    this.mElement.style.visibility = "hidden";
    this.mElement.style.display = "block";
    this.mElement.style.position = "absolute";
    this.mElement.style.padding ="0px";
    this.mElement.style.border ="0px";
    this.mElement.style.margin ="0px";
    if(otherUnitSize !== undefined) {
      this.mElement.style.width = otherUnitSize;
    } else {
      this.mElement.style.width = "0px";
    }
    this.mElement.appendChild(document.createTextNode(""));

    // Now, append it to the document body
    document.body.appendChild(this.mElement);
    // Now, measure its width in pixels.
    resultPX = this.mElement.scrollWidth;
    this.mElement.style.display = "none";
    document.body.removeChild(this.mElement)
    return resultPX;
  },

  // Implemented in vfc-debug.js
  log: function() {},

  // Implemented in vfc-debug.js
  isLogEnabled: function() {return false},

  /**
   * Chceck if browser supports position fixed. IE6.0 and
   * opera mobile don't.
   */
  supportsFixed : function(){
    return !(Prototype.msieBrowser() || Prototype.operaMobile());
  },

  FOCUS: 'focus',
  BLUR: 'blur',
  MOUSEOVER: 'mouseover',
  MOUSEOUT: 'mouseout',
  CLICK: 'click',
  KEYDOWN: 'keydown',
  KEYUP: 'keyup',
  KEYPRESS: 'keypress',
  CHANGE: 'change'
});

/**
 * An module with response widgets/objects
 */
Widget.Response = Package.define()

/**
 * An module with internal building blocks
 */
Widget.Internal = Package.define()

/**
 * shortcut for Widget.getInstance()
 */
$W = function(id) {
  return Widget.getInstance(id)
}

/**
 * shortcut for Widget.register()
 */
$RW = function(id, widget) {
  return Widget.register(id, widget)
}

/**
 * A generic mixin for all objects.
 */
Class.ObjectMixin = {
  __object: true,

  /**
   * Returns string representation of an object.
   * May be used for debuggin purposes.
   */
  toString: function() {
    var s = ""

    for (var name in this) {
      if (name != "__object") {
        var value = this[name]

        if (typeof(value) == "string" || typeof(value) == "boolean" || typeof(value) == "number") {
          s += name + "=" + value + ", "
        }
      }
    }

	return s
  }
}

/*
 * Defines and returns new class.
 *
 * Following examples explains usage of this method.
 *
 *  Example 1: A class without a superclass
 * -----------------------------------------
 *
 * MyClass = Class.define(
 * {
 *     attribute: null,
 *
 *     initialize: function(value) {
 *         this.attribute = value
 *     },
 *
 *     getAttribute: function() {
 *         return this.attribute
 *     },
 *
 *     setAttribute: function(value) {
 *         this.attribute = value
 *     }
 * })
 *
 *  Example 2: A class with one superclass
 * ----------------------------------------
 *
 * AnotherClass = Class.define(MyClass,
 * {
 *     // ...attributes and methods...
 * })
 *
 *  Example 3: A class with many superclasses
 * -------------------------------------------
 *
 * YetAnotherClass = Class.define(MyClass, AnotherClass,
 * {
 *     // ...attributes and methods...
 * })
 *
 */
Class.define = function() {
    // Create a new class.
    var clazz = function() {
      // Invoke initialization function, if it's specified.
      if (this.initialize != null) {
        this.initialize.apply(this, arguments)
      }
    }

    // Generic function used to extend a class.
    var extend = function(clazz, superClazz) {
        if (!superClazz) {
            // No superclass - do nothing

        } else if (typeof(superClazz) == "object") {
            // Superclass is an object
            Object.extend(clazz.prototype, superClazz)

        } else if (typeof(superClazz) == "function") {
            // Superclass is a class
            Object.extend(clazz.prototype, superClazz.prototype)
        }
    }

    // Extend defined class with all subclasses
    // passed in arguments.
    for (var i = 0; i < arguments.length; i++) {
        extend(clazz, arguments[i])
    }

    // Define standard getMethod() method, which would access a method
    // on this instance.
    extend(clazz, Class.ObjectMixin)

    // Return defined class
    return clazz
}

/**
 * Mix-in which defines a method to store input fields list options.
 */
Widget.InputContainer = Class.define(
{
    /**
     * Storage for object's options. Internal use only.
     */
    inputFields: {},

    /**
     * Add new input to container
     *
     * Container hold map where input name is key and input id is value.
     */
    registerInputId: function(fieldName,fieldId) {
		this.inputFields[fieldName] = fieldId
    },

    /**
     * Get field named 'name'.
     */
    getInputId: function (fieldName) {
        return this.inputFields[fieldName]
    },

	/**
	 * Return all input fields in hash map.
	 */
	getAllInputs : function(){
		return this.inputFields
	}
})

/*
 * Mix-in which defines a method to install options, which
 * can be used during object initialization (inside initialize() method).
 */
Widget.OptionsContainer = Class.define(
{
    /*
     * Storage for object's options. Internal use only.
     */
    options: null,

    /*
     * Installs given options on this object.
     *
     * Once options are installed, they can be accessed
     * using getOption() method.
     */
    installOptions: function(options) {
        this.options = options

        if (options) {
            for (var option in options) {
                if (!(this[option] === undefined) && typeof(this[option]) != 'function') {
                    this[option] = options[option];
                }
            }
        }
    },

    /*
     * Get option named 'name'.
     * If option is not defined, it returns 'def' by default.
     */
    getOption: function (name, def) {
        return this.options ? (this.options[name] || def) : def
    }
})


/*------------------ Widget.Focusable ----------------------------*/
// Creates a Focusable element out of any HTML element.
// It will catch focus upon entering and/or leaving the element.
Widget.Focusable = Class.create()

Widget.Focusable.ACCEPT = 'accept'
Widget.Focusable.IGNORE = 'ignore'
// Creates new Focusable Widget from any HTML element
// within the document.
//
// Style controls the moment when the focus is taken,
// and may be one of:
//  - "pre" (default)
//  - "post"
//  - "pre-and-post"
Object.extend(Widget.Focusable.prototype, {
  focusable: Widget.Focusable.ACCEPT,

  setFocus: function(element, funcFocus, funcBlur){
    var focusableElementsCount = Widget.addObserversToFocusableElements(
      element,Widget.FOCUS,funcFocus);
    if(this.focusable.toLowerCase() == Widget.Focusable.ACCEPT){
      // focusable flag is set to 'accept'
      // so there is need to create fakeFocusElement
      if(Prototype.operaMobile()){
        this.setFocusOnOpera(element,funcFocus,funcBlur);
      }else if(Prototype.nokiaOSSBrowser()){
        this.setFocusOnNokia(element,funcFocus,funcBlur);
      } else if(Prototype.netFront()){
        this.setFocusOnNetFront(element,funcFocus,funcBlur);
      } else {
        this.createFocusableElement(element, funcFocus);
      }
      }
      Widget.addObserversToFocusableElements(element,Widget.BLUR,funcBlur);
  },

  setFocusOnNokia: function(element,funcFocus,funcBlur){
    Widget.addElementObserver(element,Widget.FOCUS,funcFocus);
    Widget.addElementObserver(element,Widget.BLUR,funcBlur);
  },

  setFocusOnOpera: function(element,funcFocus,funcBlur){
    this.createFocusableElement(element, funcFocus);
  },

  setFocusOnNetFront: function(element,funcFocus,funcBlur){
    this.createFocusableElement(element, funcFocus);
  },

  unsetFocus: function(element, funcFocus, funcBlur){
    if(Prototype.operaMobile()){
      this.unsetFocusOnOpera(element,funcFocus,funcBlur);
    }else if(Prototype.nokiaOSSBrowser()){
      this.unsetFocusOnNokia(element,funcFocus,funcBlur);
    } else if(Prototype.netFront()){
      this.unsetFocusOnNetFront(element,funcFocus,funcBlur);
    } else {
      this.unsetFocusOnFocusableElement(element,funcFocus);
    }
    Widget.removeObserversFromFocusableElements(element,Widget.FOCUS,funcFocus);
    Widget.removeObserversFromFocusableElements(element,Widget.BLUR,funcBlur);
  },

  unsetFocusOnNetFront: function(element,funcFocus,funcBlur){
    this.unsetFocusOnFocusableElement(element,funcFocus);
  },

  unsetFocusOnOpera: function(element,funcFocus,funcBlur){
    this.unsetFocusOnFocusableElement(element,funcFocus);
  },

  unsetFocusOnNokia: function(element,funcFocus,funcBlur){
    Widget.removeElementObserver(element,Widget.FOCUS,funcFocus);
    Widget.removeElementObserver(element,Widget.BLUR,funcBlur);
  },

  createFocusableElement: function(element, funcFocus) {
    var fakeFocusID = element.getAttribute('id')+'_f';

    var fakeFocusDIV =document.createElement('div');
    fakeFocusDIV.setAttribute('id',fakeFocusID);
    Element.setStyle(fakeFocusDIV,{ 'white-space': 'nowrap', 'width': '1px',
      'height': '1px', 'background-color': 'transparent', 'line-height': '1px',
      'margin' : '0px', 'padding': '0px', 'border': '0px'});
    element.appendChild(fakeFocusDIV);

    var fakeFocusElement = document.createElement("input");
    Element.setStyle(fakeFocusElement,{'width': '1px', 'height': '1px',
      'overflow' : 'hidden', 'background-color': 'transparent', 'border': '0px'});
    fakeFocusElement.type="button";

    fakeFocusDIV.appendChild(fakeFocusElement);
    if (typeof(funcFocus) == 'function') {
    Widget.addElementObserver(fakeFocusElement, Widget.FOCUS, funcFocus);
    }
    return fakeFocusElement;
  },

  unsetFocusOnFocusableElement: function(element,funcFocus,funcBlur){
    var fakeFocusID = element.getAttribute('id')+'_f';
    var fakeFocusElement = $(fakeFocusID);
    if(fakeFocusElement != null){
      if (typeof(funcFocus) == 'function') {
      Widget.removeElementObserver(fakeFocusElement, Widget.FOCUS, funcFocus);
      }
      if (typeof(funcBlur) == 'function') {
      Widget.removeElementObserver(fakeFocusElement, Widget.BLUR, funcBlur);
      }
      element.removeChild(fakeFocusElement);
    }
  }

})

/*
 * Creates and returns a function, which will
 * invoke this function first, and then the
 * function specified in the parameters.
 *
 * Example: To create a function, which will invoke
 * function A, and then function B, do the following:
 *
 *  result = A.andThen(B)
 *
 * @param anotherFunction The function to invoke
 *           after this function
 */
Function.prototype.andThen = function(anotherFunction) {
    var thisFunction = this;

    return function() {
        thisFunction.apply(this, arguments)
        anotherFunction.apply(this, arguments)
  }
};

/*
 * This class is currently equivalent to Ajax.Request (from prototype.js).
 *
 * In case some Widget-specific action or attribute will need to be
 * added, it'll be done in this class, without a need to modify
 * the prototype.js file.
 */
Widget.AjaxRequest = Class.define(
{
  /*
   * Initialization method.
   */
  initialize: function(url, options) {
    new Ajax.Request(url, options)
  }
})

/*
 * This class is equivalent to Ajax.Updater (from prototype.js).
 *
 * In case some Widget-specific action or attribute will need to be
 * added, it'll be done in this class, without a need to modify
 * the prototype.js file.
 */
Widget.AjaxUpdater = Class.define(
{
  /*
   * Initialization method
   */
  initialize: function(container, url, options) {
    new Ajax.Updater(container, url, options)
  }
})

/*
 * Callback Handler provides an API to add callbacks,
 * and invoke added callbacks.
 *
 * It can be used to implement callback facilities
 * for your classes.
 *
 * Example:
 * MyClass = Class.create({
 *     // Constructor
 *     initialize: function() {
 *         dismissCallbackHandler = new Widget.CallbackHandler()
 *     },
 *
 *     // Adds callback on dismiss
 *     addDismissCallback: function(callback) {
 *         dismissCallbackHandler.add(callback)
 *     },
 *
 *     // Fictional function which serves as an example
 *     // on how to invoke callbacks.
 *     someFunctionProducingDismissCallback: function(event) {
 *         ...
 *         dismissCallbackHandler.invoke()
 *         ...
 *     }
 * })
 */
Widget.CallbackHandler = Class.define(
{
    /*
     * Adds a callback.
     *
     * @param callback The callback function to add.
     * @returns The callback added.
     */
    add: function(callback) {
        if (!this.callbacks) {
            // Case 1: Adding first callback.
            // Store the callback directly.
            this.callbacks = callback
            this.singleCallback = true

        } else {
            // Case 2: Adding subsequent callback.
            if (this.singleCallback) {
                // Case 2a: This is the second callback to add.
                // Create an array to store both callbacks
                this.callbacks = new Array(this.callbacks, callback)
                this.singleCallback = false
            } else {
                // Case 2b: This is a subsequent callback
                // to push into the array
                this.callbacks.push(callback)
            }
        }

        return callback
    },

    /*
     * Removes a callback.
     *
     * @param callback The callback function to remove.
     * @returns The success flag (true if callback has been removed)
     */
    remove: function(callback) {
        if (this.callbacks) {
            if (this.singleCallback) {
                if (this.callbacks === callback) {
                    delete this.callbacks
                    return true
                }
            } else {
                for (var i = 0; i < this.callbacks.length; i++) {
                    if (this.callbacks[i] === callback) {
                        this.callbacks.splice (i, 1)
                        return true
                    }
			    }
            }
        }

        return false
    },

    /*
     * Invokes all added callbacks. All parameters are passed
     * to all of the invoked callback functions.
     */
    invoke: function() {
        if (this.callbacks) {
            if (this.singleCallback) {
                this.invokeCallback(this.callbacks, arguments)
            } else {
                for (var i = 0; i < this.callbacks.length; i++) {
                    this.invokeCallback(this.callbacks[i], arguments)
                }
            }
        }
    },

    /*
     * Invoke single callback in safe environment (catching exceptions)
     */
    invokeCallback: function(callback, args) {
        try {
            //Widget.log("CallbackHandler", "Invoking callback...")

            callback.apply(this, args)

            //Widget.log("CallbackHandler", "Callback finished without problems")
        } catch (error) {
            //Widget.log("CallbackHandler", "Exception catched in callback: " + error)
        }
    }
})

/*
 * A delaying callback handler. Guarantees, that callbacks will
 * invoked at most once during given time interval (delay).
 *
 * This class may be used as a proxy between API object
 * and corresponding UI control, to make sure that it's not
 * refreshed too often (which may be very costly).
 *
 * Following classes makes use of this Callback Handler class:
 *  - Ticker.TickerTapeController
 *  - Ticker.CarouselController
 */
Widget.DelayCallbackHandler = Class.define(
{
  /*
   * Initialization.
   *
   * @param interval The interval in milliseconds (default: 1000)
   */
  initialize: function(interval) {
    this.baseCallbackHandler = new Widget.CallbackHandler()
    this.interval = interval ? interval : 1000; // Default: 1 second.
  },

  // See: Widget.CallbackHandler.add()
  add: function(callback) {
    this.baseCallbackHandler.add(callback)
  },

  // See: Widget.CallbackHandler.remove()
  remove: function(callback) {
    this.baseCallbackHandler.remove(callback)
  },

  // See: Widget.CallbackHandler.invoke()
  invoke: function() {
    if (this.timer == null) {
      this.timer = setTimeout(this.invokeNow.bind(this), this.interval)
    }
  },

  invokeNow: function() {
    this.timer = null
    this.baseCallbackHandler.invoke()
  }
})

/*
 * Mixin to be used for objects which contains callback handling
 *
 * This mixin requires the property this.callbackHandler
 * to contain an instance of Widget.CallbackHandler.
 */
Widget.CallbackMixin = Class.define(
{
    /**
     * Adds callback function.
     *
     * @param callback The callback function added.
     */
    addCallback: function(callback) {
        this.callbackHandler.add(callback)
    },

    /**
     * Removes callback function, if exists.
     *
     * @param callback The callback function to remove.
     */
    removeCallback: function(callback) {
        this.callbackHandler.remove(callback)
    }
})

/**
 * An internal class maintaining a set of observers,
 * and sending notifications to them.
 */
Widget.Internal.ObserverHandler = Class.define(
{
  /**
   * Adds an observer for given notification name.
   */
  addObserver: function(notificationName, observer, handlerName) {
    // Get a map of observers, keyed by notification name.
    // Create one, if not yet created
    var observersArray = this._getObserversArray(notificationName, true)

    // Check, if the observer exists.
    var index = this._getObserverIndex(observersArray, observer, handlerName)

    // If not, add it.
    if (index == null) {
      observersArray.push(observer)
      observersArray.push(handlerName)

      if (arguments.length <= 3) {
        observersArray.push(null)
      } else {
        observersArray.push($A(arguments).slice(3))
      }
    }
  },

  /**
   * Removes an observer for given notification name.
   */
  removeObserver: function(notificationName, observer, handlerName) {
    // Get a map of observers, keyed by notification name.
    // In not exist, return immediately.
    var observersArray = this._getObserversArray(notificationName)

    // Find an index of the observer.
    var index = this._getObserverIndex(observersArray, observer, handlerName)

    // If found, remove it from array.
    if (index != null) {
      observersArray.splice (i, 3)
    }
  },

  /**
   * Invokes handlers for all observers on given notification name.
   */
  notifyObservers: function(notificationName) {
    // Notify observers for specified notification name
    var observersArray = this._getObserversArray(notificationName)

    if (observersArray != null) {
      this.notifyObserversArray(observersArray, arguments)
    }

    // Notify observers for all notifications
    var observersArray = this._getObserversArray(null)

    if (observersArray != null) {
      this.notifyAllObserversArray(observersArray, arguments)
    }
  },

  notifyObserversArray: function(observersArray, args) {
      for (var index = 0; index < observersArray.length; index = index + 3) {
        var observer = observersArray[index]
        var handlerName = observersArray[index+1]
        var additionalArguments = observersArray[index+2]

        // Invoke a handler method on the observer object.
        if (additionalArguments == null) {
        if (args.length == 1) {
            // If there are no additional arguments apart from eventName,
            // invoke the handler with no arguments.
            observer[handlerName]()

          } else {
            // Otherwise, pass all arguments except first one (eventName).
            observer[handlerName].apply(observer, $A(args).slice(1))
          }
        } else {
          if (arguments.length == 1) {
            // If there are no additional arguments apart from eventName,
            // invoke the handler with additional arguments only.
            observer[handlerName].apply(observer, additionalArguments)

          } else {
            // Otherwise, concatenate additional arguments with notification arguments
            // except first one (eventName).
          observer[handlerName].apply(observer, additionalArguments.concat($A(args).slice(1)))
          }
        }
      }
  },

  notifyAllObserversArray: function(observersArray, args) {
    for (var index = 0; index < observersArray.length; index = index + 3) {
      var observer = observersArray[index]
      var handlerName = observersArray[index+1]
      var additionalArguments = observersArray[index+2]

      // Invoke a handler method on the observer object.
      if (additionalArguments == null) {
        // If there are no additional arguments, invoke the handler
        // with all notification arguments, including notification name.
        observer[handlerName].apply(observer, args)

      } else {
        // Otherwise, concatenate additional arguments with notification
        // arguments except that notification name goes first.
        var argsArray = $A(args)

        observer[handlerName].apply(observer, [argsArray[0]].concat(additionalArguments).concat(argsArray.slice(1)))
      }
    }
  },

  _getObserversArray: function(notificationName, create) {
    // Null notofication name means: all notifications.
    notificationName = (notificationName == null) ? "" : notificationName

    // Get a map of observers, keyed by notification name.
    // Create one, if not yet created
    var observers = this.observers

    if (observers == null && create == true) {
      observers = {}

      this.observers = observers
    }

    // Get an array of event observers, creating it if not yet created.
    if (observers != null) {
      var notificationObservers = observers[notificationName]

      if (notificationObservers == null && create) {
        notificationObservers = []

        observers[notificationName] = notificationObservers
      }
    }

    return notificationObservers
  },

  _getObserverIndex: function(observersArray, observer, handlerName) {
    for (var index = 0; index < observersArray; index = index + 3) {
      if (observersArray[index] === observer && observersArray[index+1] == handlerName) {
        return index
      }
    }
  }
})

/**
 * A mixin for all observable objects.
 * Provides a method to notify observers on given event name.
 */
Widget.Observable = Class.define(
{
  /**
   * Internal method. Returns and instance of Widget.ObserverHandler
   * used by this object, creating it if nessecarry.
   */
  _getObserverHandler: function() {
    if (this.observerHandler == null) {
      this.observerHandler = new Widget.Internal.ObserverHandler()
    }

    return this.observerHandler
  },

  /**
   * Notifies all observers on given notification name.
   *
   * Examples:
   *  this.notifyObservers("pressed")
   *  this.notifyObservers("valueChanged", 12)
   */
  notifyObservers: function(notificationName) {
    var observerHandler = this._getObserverHandler()

    observerHandler.notifyObservers.apply(observerHandler, arguments)
  }
})

/**
 * A mixin for all objects, which has the ability to observe
 * other observable objects for events.
 */
Widget.Observer = Class.define(
{
  /**
   * Observes given object on given notification name.
   * If a notification is sent, a given handler is invoked on object.
   * If more that three arguments are specified, the additional ones
   * will be passed to the handler method, followed by notification arguments.
   *
   * Example:
   *  myGallery.observe(playButton, "pressed", "play")
   */
  observe: function(object, notificationName, handlerName) {
    var handler = object._getObserverHandler()

    if (handler != null) {
      if (arguments.length <= 3) {
        handler.addObserver(notificationName, this, handlerName)
      } else {
        handler.addObserver.apply(handler, [notificationName, this, handlerName].concat($A(arguments).slice(3)))
      }
    }
  },

  /**
   * Stops observing given object on given notification using given handler.
   *
   * Example:
   *  myGallery.stopObserving(playButton, "pressed", "play")
   */
  stopObserving: function(object, notificationName, handlerName) {
    var handler = object._getObserverHandler()

    if (handler != null) {
      handler.removeObserver(notificationName, this, handlerName)
    }
  }
})

/**
 * An object containing a set of named actions.
 */
Widget.ActionOwner = Class.define(
{
  /**
   * Returns action with specified name, if it exists.
   */
  getAction: function(name) {
    if (this.actions != null) {
      return this.actions[name]
    }
  },

  /**
   * Adds named action. To be called upon initialization.
   */
  addAction: function(name, options) {
    if (this.actions == null) {
      this.actions = {}
    }

    this.actions[name] = new Widget.Action(name, this, options)
  }
})

/**
 * Object containing a set of named properties.
 */
Widget.PropertyOwner = Class.define(
{
  /**
   * Returns property with specified name, is exists.
   */
  getProperty: function(name) {
    if (this.properties != null) {
      return this.properties[name]
    }
  },

  /**
   * Adds named property. To be called upon initialization.
   */
  addProperty: function(name, options) {
    if (this.properties == null) {
      this.properties = {}
    }

    this.properties[name] = new Widget.Property(name, this, options)
  }
})

/**
 * An object containing a set of named events.
 */
Widget.EventOwner = Class.define(
{
  /**
   * Returns event with specified name, if it exists.
   */
  getEvent: function(name) {
    if (name != "") {
      // Standard event
    if (this.events != null) {
	  return this.events[name]
	}

  	} else {
  	  // Meta event
  	  if (this.metaEvent == null) {
  	    this.metaEvent = new Widget.Event("", this)
  	  }

  	  return this.metaEvent
  	}
  },

  /**
   * Adds named event. To be called upon initialization.
   */
  addEvent: function(name, options) {
    // Create event
    var event = new Widget.Event(name, this, options)

    // Add event
    if (this.events == null) {
      this.events = {}
    }

    this.events[name] = event

    // Store entry in mapping between notification name and event name.
    if (event.notificationName != null) {
      if (this.notification2EventNameMap == null) {
        this.notification2EventNameMap = {}
      }

      this.notification2EventNameMap[event.notificationName] = name
    }
  },

  getEventNameForMetaEvent: function(notificationName) {
    if (this.notification2EventNameMap != null) {
      return this.notification2EventNameMap[notificationName]
    }
  }
})

/**
 * Member owner provides single method to access Actions, Properties and Events.
 */
Widget.MemberOwner = Class.define(Widget.ActionOwner, Widget.PropertyOwner, Widget.EventOwner,
{
  getMember: function(name) {
    var member = this.getAction(name)

    if (member == null) {
      member = this.getProperty(name)

      if (member == null) {
        member = this.getEvent(name)
      }
    }

    return member
  }
})

/**
 * An internal class performing member type conversion.
 */
Widget.Internal.MemberTypeConverter = Class.define(
{
  /**
   * Validates the string value before it's set.
   */
  validate: function(type, value) {
    if (type == "boolean") {
      return (value == "yes") || (value == "no")
    } else if (type == "int") {
      return !isNaN(parseInt(value))
    } else if (type == "number") {
      return !isNaN(parseFloat(value))
    } else if (type == "string") {
      return true
    } else if (type == "widget") {
      return (Widget.getInstance(value) != null)
    } else if (type == "list") {
      return true;
    } else {
      throw "Invalid type: " + type;
    }
  },

  /**
   * Convert non-null JavaScript value into string value
   */
  convertForGet: function(type, value) {
    if (type == "boolean") {
      return value ? "yes" : "no"
    } else if (type == "int") {
      return value.toString()
    } else if (type == "number") {
      return value.toString()
    } else if (type == "string") {
      return value
    } else if (type == "widget") {
      return (value._widgetId == null) ? "" : value._widgetId
    } else if (type == "list") {
      return value.toString()
    } else {
      throw "Invalid type: " + type;
    }
  },

  /**
   * Converts already validated string value into JavaScript value.
   */
  convertForSet: function(type, value) {
    if (type == "boolean") {
      return (value == "yes")
    } else if (type == "int") {
      return parseInt(value)
    } else if (type == "number") {
      return parseFloat(value)
    } else if (type == "string") {
      return value
    } else if (type == "widget") {
      return Widget.getInstance(value)
    } else if (type == "list") {
      return value.split(',');
    } else {
      throw "Invalid type: " + type;
    }
  }
})

/**
 * Returns a singleton instance of MemberTypeConverter.
 */
Widget.Internal.getMemberTypeConverter = function() {
  if (this._memberTypeConverter == null) {
    this._memberTypeConverter = new Widget.Internal.MemberTypeConverter()
  }

  return this._memberTypeConverter
}

/**
 * Action is something, which may be invoked.
 *
 * An action may be in enabled, or disabled state.
 * In disabled state, invoking the action does nothing.
 */
Widget.Action = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer, Widget.MemberOwner,
{
  methodName: null,
  canMethodName: null,
  canChangedNotificationName: null,

  /**
   * Initialization.
   */
  initialize: function(name, owner, options) {
    this.installOptions(options)

    this.name = name
    this.owner = owner

    // Generate default method/callback names based on action name.
    if (this.methodName == null) {
      this.methodName = this.name.camelize()
    }

    if (this.canMethodName == null) {
      this.canMethodName = ("can-" + this.name).camelize()
    }

    if (this.canChangedNotificationName == null) {
      this.canChangedNotificationName = ("can-" + this.name + "-changed").camelize()
    }

    // Generate invoker and enabler getter/callback handler.
    this.observe(this.owner, this.canChangedNotificationName, "isEnabledChanged")

    this.addProperty("is-enabled")

    if (Widget.isLogEnabled()) {
      Widget.log("Action", "Created action: " + this.toString())
    }
  },

  /**
   * Invokes this action with specified arguments, if it's enabled.
   */
  invoke: function() {
    if (this.isEnabled()) {
      this.owner[this.methodName].apply(this.owner, arguments)
    }
  },

  /**
   * Returns true, if this action is enabled, false otherewise.
   */
  isEnabled: function() {
    var isEnabled = true

    var canFunction = this.owner[this.canMethodName]

    if (canFunction != null) {
      isEnabled = canFunction.apply(this.owner)
    }

    return isEnabled
  },

  isEnabledChanged: function() {
    this.notifyObservers('isEnabledChanged')
  }
})

/**
 * A property holding a value as a string.
 * It can work in read-only or writable mode.
 */
Widget.Property = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer, Widget.MemberOwner,
{
  type: null,
  getMethodName: null,
  setMethodName: null,
  changedNotificationName: null,
  defaultValue: "",
  writable: null,
  readable: null,
  validator: null,

  /**
   * Initialization
   */
  initialize: function(name, owner, options) {
    this.installOptions(options)

    this.name = name
    this.owner = owner

    // Get implementation of a super method, so it'll be possible to
    // call it from overwritten one.
    this.superGetMember = Widget.MemberOwner.prototype.getMember.bind(this)

    // Generate default method/callback names.
    if (this.getMethodName == null) {
      if (this.name.substring(0, 3) == "is-" ||
          this.name.substring(0, 4) == "can-" ||
          this.name.substring(0, 4) == "has-" ||
          this.name.substring(0, 5) == "does-") {
        this.getMethodName = this.name.camelize()
      } else {
        this.getMethodName = ("get-" + this.name).camelize()
      }
    }

    if (this.setMethodName == null) {
      if (this.name.substring(0, 3) == "is-") {
        this.setMethodName = ("set-" + this.name.slice(3)).camelize()
      } else if (this.name.substring(0, 4) == "can-") {
        this.setMethodName = ("set-" + this.name.slice(4)).camelize()
      } else if (this.name.substring(0, 4) == "has-") {
        this.setMethodName = ("set-" + this.name.slice(4)).camelize()
      } else if (this.name.substring(0, 5) == "does-") {
        this.setMethodName = ("set-" + this.name.slice(5)).camelize()
      } else {
        this.setMethodName = ("set-" + this.name).camelize()
      }
    }

    if (this.changedNotificationName == null) {
      this.changedNotificationName = (this.name + "-changed").camelize()
    }

    // Figure the type from property name.
	if (this.type == null) {
      if (this.name.substring(0, 3) == "is-" ||
          this.name.substring(0, 4) == "can-" ||
          this.name.substring(0, 4) == "has-" ||
          this.name.substring(0, 5) == "does-") {
   		this.type = "boolean"
      } else if (this.name.substring(this.name.length-6) == "-count" ||
                 this.name.substring(this.name.length-7) == "-number") {
   	    this.type = "int"
   	  } else {
   	    this.type = "string"
      }
   	}

    // Determine, if a property is readable and writable
    if (this.readable == null) {
      this.readable = (this.owner[this.getMethodName] != null)
    }

    if (this.writable == null) {
      this.writable = (this.owner[this.setMethodName] != null)
    }

    // Observe property owner on value change.
    this.observe(this.owner, this.changedNotificationName, "ownerChanged")

    this.addEvent("value-changed")

    if (Widget.isLogEnabled()) {
      Widget.log("Property", "Created property: " + this.toString())
    }
  },

  /**
   * Returns a value of the property as a string.
   * The value is never null, but it may be an empty string.
   */
  getValue: function() {
    // If the property is not readable, return immediately with default value.
    if (!this.readable) {
      return this.defaultValue
    }

    // Get the value from get method.
    var value = this.owner[this.getMethodName]()

    // Convert it to string
    if (value == null) {
      // Nulls should be converted to default value
      value = this.defaultValue

    } else {
      // Non-null values should be converted appropriately by its type.
      value = Widget.Internal.getMemberTypeConverter().convertForGet(this.type, value)
    }

    return value
  },

  /**
   * Sets the value of this property, unless it's read-only.
   */
  setValue: function(value) {
    // If this property is not writable, return immediately.
    if (!this.writable) {
      return
    }

    var valueToSet = null

    // Non-default values should be validated, and converted appropriately by its type.
    if (value != this.defaultValue) {
      var converter = Widget.Internal.getMemberTypeConverter()

      if (!converter.validate(this.type, value)) {
        return
      }

      // Convert value through converter.
      valueToSet = converter.convertForSet(this.type, value)
    }

    // Set the value using setter method.
    this.owner[this.setMethodName](valueToSet)

    // A 'valueChanged' notification should be sent now,
    // and it'll done at the moment the owner sends change notification.
    // in the ownerChanged() method.
  },

  /**
   * Returns true, if this property is writable.
   */
  isWritable: function() {
    return this.writable
  },

  /**
   * Returns true, if this property is readable.
   */
  isReadable: function() {
    return this.readable
  },

  /**
   * Indicates, that the value of this property has changed.
   */
  valueChanged: function() {
    this.notifyObservers('valueChanged')
  },

  /**
   * A callback for widget event.
   */
  ownerChanged: function() {
    this.valueChanged()
  },

  /**
   * When widget-type property is asked for a member,
   * return member of a widget held in the property value.
   */
  getMember: function(name) {
    // Call method on super class.
    var member = this.superGetMember(name)

    if (member == null && this.type == "widget" && this.readable) {
      var widget = this.owner[this.getMethodName]()

      if (widget != null) {
        member = widget.getMember(name)
      }
    }

    return member
  }
})

/**
 * An event.
 *
 * Invokes callbacks, when sent.
 */
Widget.Event = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer, Widget.MemberOwner,
{
  notificationName: null,

  initialize: function(name, owner, options) {
    this.installOptions(options)

    this.name = name
    this.owner = owner

    // Figure out a notification name
    if (this.notificationName == null) {
      if (this.name != "") {
     	this.notificationName = this.name.camelize()
      } else {
        // Notification name is null, means listen to all notifications.
      }
    }

    if (this.notificationName != null) {
      this.observe(this.owner, this.notificationName, "handleOwnerEvent")
    } else {
      this.observe(this.owner, null, "handleOwnerMetaEvent")
    }
  },

  /**
   * Handles owner notification.
   */
  handleOwnerEvent: function() {
    // Send this event.
    if (arguments.length == 0) {
      // This is a optimisation for parameter-less notifications.
      this.notifyObservers("sent")

    } else {
      // Send an event passing all notification arguments.
      this.notifyObservers.apply(this, ["sent"].concat($A(arguments)))
    }
  },

  /**
   * Handles owner notification.
   */
  handleOwnerMetaEvent: function() {
    var eventName = this.owner.getEventNameForMetaEvent(arguments[0])

    if (eventName != null) {
      var args = $A(arguments)

      args[0] = eventName

      this.notifyObservers.apply(this, ["sent"].concat(args))
    }
  }
})

/**
 * A super class for all widgets.
 *
 * To define a widget subclass, write:
 *
 * Widget.MyWidget = Class.define(Widget.Widget,
 * {
 *   initialize: function(id, options) {
 *     // Initialize the superclass.
 *     this.initializeWidget(id, options)
 *
 *     // ... subclass initialization goes here ...
 *   }
 * })
 */
Widget.Widget = Class.define(Widget.MemberOwner, Widget.OptionsContainer, Widget.Observable, Widget.Observer,
{
  /**
   * Initializes this widget with ID of the element (may be null),
   * initial property values and options.
   */
  initializeWidget: function(id, options) {
    // Install possible options
    this.installOptions(options)

    this.id = id

    this.element = $(this.id)

    // Create callback handler (deprecated, replaced with Widget.Observable).
    this.callbackHandler = new Widget.CallbackHandler()
  },

  /**
   * Returns element for this widget.
   */
  getElement: function() {
    return this.element
  }
})

/**
 * A super class for all internal widgets.
 *
 * To define an internal widget subclass, write:
 *
 * Widget.Internal.MyWidget = Class.define(Widget.Internal.Widget,
 * {
 *   initialize: function(id, options) {
 *     // Initialize the superclass.
 *     this.initializeWidget(id, options)
 *
 *     // ... subclass initialization goes here ...
 *   }
 * })
 */
Widget.Internal.Widget = Class.define(Widget.OptionsContainer, Widget.Observable, Widget.Observer,
{
  /**
   * Initializes this widget with ID of the element (may be null),
   * initial property values and options.
   */
  initializeWidget: function(id, options) {
    // Install possible options
    this.installOptions(options)

    this.id = id

    this.element = $(this.id)

    // Create callback handler (deprecated, replaced with Widget.Observable).
    this.callbackHandler = new Widget.CallbackHandler()
  },

  /**
   * Returns element for this widget.
   */
  getElement: function() {
    return this.element
  }
})

/**
 * Defines and returns new widget class.
 *
 * This is equivalent to Class.define(Widget.Widget, ...)
 */
Widget.define = function(a1, a2) {
  return Class.define.apply(Class, [Widget.Widget].concat($A(arguments)))
}

/**
 * Defines and returns new internal widget class.
 *
 * This is equivalent to Class.define(Widget.Internal.Widget, ...)
 */
Widget.Internal.define = function(a1, a2) {
  return Class.define.apply(Class, [Widget.Internal.Widget].concat($A(arguments)))
}

/**
 * A dismiss widget.
 */
Widget.Dismiss = Widget.define({

  initialize: function(id, type, button, dismissable) {
    this.initializeWidget(id);
    this.type = type;
    this.button = button;
    this.action = dismissable.getAction("dismiss");

    this.observe(button, "pressed", "_buttonPressed");
    if (this.action != null) {
      this.observe(this.action, "isEnabledChanged", "_updateButtonWithAction")
      // Set button's initial state
      this._updateButtonWithAction()
    }
  },

  _buttonPressed: function() {
    if (this.action != null) {
      this.action.invoke(this.type);
    }
  },

  _updateButtonWithAction: function() {
    this.button.setEnabled(this.action.isEnabled());
  }
});

/**
 * A button widget, for internal use only.
 *
 * The public button is represented by Widget.ActionButton.
 */
Widget.Internal.Button = Class.define(Widget.OptionsContainer, Widget.Observable,
{
  enabled: true,
  disabledStyle: {},

  /**
   * Initialization.
   */
  initialize: function(id, options) {
    this.installOptions(options)

    this.id = id

    var element = this.getElement()

    // TODO: Maybe listen to DOMActivate event?
    // DOMActivate do not work on Opera browser
    if (this.isNative() && (Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser())) {
      // Native HTML buttons sends "DOMActivate" event on press.
      Widget.addElementObserver(element, "DOMActivate", this.press.bindAsEventListener(this));
    } else {
      // Emulated buttons needs to be handled differently.
    Widget.addElementObserver(element, Widget.CLICK, this.press.bindAsEventListener(this));
    Widget.addElementObserver(element, Widget.KEYPRESS, this.keyPressed.bindAsEventListener(this));
    Widget.makeFocusable(element);
    }

    this.update()
  },

  /**
   * Returns an element this button is based on.
   */
  getElement: function() {
    return $(this.id)
  },

  /**
   * Presses the button programatically.
   */
  press: function() {
    if (this.enabled) {
      this.notifyObservers('pressed')
    }
  },

  /**
   * Equivalent to press (backward compability method).
   */
  click: function() {
    this.press()
  },

  /**
   * Handles key press
   */
  keyPressed: function(ev) {
    // Enter activates the button (but only the non-native one)
    if (!this.isNative()) {
      if (ev.keyCode == 13) {
        this.press()
      }
    }
  },

  /**
   * Enables or disables the button.
   *
   * @param enabled The enabled flag to set.
   */
  setEnabled: function(enabled) {
    this.enabled = enabled

    this.update()

    this.notifyObservers('isEnabledChanged')
  },

  /**
   * Returns true if this button is enabled, false otherwise.
   */
  isEnabled: function() {
    return this.enabled
  },

  /**
   * Updates button state
   */
  update: function() {
    if (this.enabled) {
      if (this.isNative()) {
        this.getElement().disabled = false
      }

      this.getElement().vfcRevertStyle()
    } else {
      if (this.isNative()) {
        this.getElement().disabled = true
      }

      this.getElement().vfcReplaceStyle(this.disabledStyle)
    }
  },

  isNative: function() {
    return this.getElement().tagName.toUpperCase() == "BUTTON"
  }
})

/**
 * A button widget.
 */
Widget.Button = Widget.define(
{
  action: null,

  /**
   * Intialization
   */
  initialize: function(id, options) {
    // Initalize superclass
    this.initializeWidget(id, options)

    // Create internal button
    this.button = new Widget.Internal.Button(id, options)

    // Handle internal button events
    this.observe(this.button, "pressed", "internalButtonPressed")

    this.observe(this.button, "isEnabledChanged", "internalButtonEnabledChanged")

    // If action is specified, synchronize this button with action.
    if (this.action != null) {
      this.observe(this.action, "isEnabledChanged", "updateButtonWithAction")

      this.updateButtonWithAction()
    }

    this.addAction("press")
    this.addAction("enable")
    this.addAction("disable")
    this.addProperty("is-enabled")
    this.addEvent("pressed")
  },

  /**
   * Presses the button programatically.
   * If a button is enabled, a callback and the action are invoked.
   */
  press: function() {
    // Simulate the click by pressing an internal simple button.
    this.button.press()
  },

  canPress: function() {
    return this.isEnabled()
  },

  /**
   * Invoked, when the internal button is pressed
   */
  internalButtonPressed: function() {
    this.notifyObservers("pressed")

    if (this.action != null) {
      this.action.invoke()
    }
  },

  /**
   * Invoked, when the internal button is pressed
   */
  internalButtonEnabledChanged: function() {
    this.notifyObservers("isEnabledChanged")
    this.notifyObservers("canPressChanged")
    this.notifyObservers("canEnableChanged")
    this.notifyObservers("canDisableChanged")
  },

  /**
   * Returns true, if this button is enabled.
   */
  isEnabled: function() {
    return this.button.isEnabled()
  },

  /**
   * Enabled/disables this button, unless button is associated with an action.
   */
  setEnabled: function(enabled) {
    if (this.action == null) {
      this.button.setEnabled(enabled)
    }
  },

  enable: function() {
    this.setEnabled(true)
  },

  canEnable: function() {
    return !this.isEnabled()
  },

  disable: function() {
    this.setEnabled(false)
  },

  canDisable: function() {
    return this.isEnabled()
  },

  updateButtonWithAction: function() {
    this.button.setEnabled(this.action.isEnabled())
  }
})

/**
 * A widget displaying a string value.
 * It's also possible to display rich content, as a fragment of HTML.
 */
Widget.Internal.Display = Class.define(Widget.OptionsContainer, Widget.Observable,
{
  content: "",

  initialize: function(id, options) {
    this.installOptions(options)

    this.id = id

    this.updateHTML()
  },

  getElement: function() {
    return $(this.id)
  },

  setContent: function(content) {
    if (this.content != content) {
      this.content = content

      this.updateHTML()

      this.notifyObservers("contentChanged")
      this.notifyObservers("canClearContentChanged")
    }
  },

  getContent: function() {
    return this.content
  },

  clearContent: function() {
    this.setContent("")
  },

  canClearContent: function() {
    return this.content != ""
  },

  updateHTML: function() {
    this.getElement().innerHTML = this.content.escapeHTML()
  }
})

/**
 * A property display widget.
 *
 * It can display any string value.
 * Additionally, it may be associated with a widget property
 */
Widget.Display = Widget.define(
{
  property: null,

  initialize: function(id, options) {
    this.initializeWidget(id, options)

    // Create a Display widget.
    this.display = new Widget.Internal.Display(id, options)

    this.observe(this.display, "contentChanged", "contentChanged")
    this.observe(this.display, "canClearContentChanged", "canClearContentChanged")

    if (this.property != null) {
      this.observe(this.property, "valueChanged", "updateWithProperty")

      this.updateWithProperty()
    }

    this.addAction("clear-content")
    this.addProperty("content")
  },

  /**
   * Sets displayed content.
   */
  setContent: function(string) {
    if (this.property == null) {
      this.display.setContent(string)
    }
  },

  getContent: function() {
    return this.display.getContent()
  },

  clearContent: function() {
    this.display.clearContent()
  },

  canClearContent: function() {
    return this.display.canClearContent()
  },

  canClearContentChanged: function() {
    this.notifyObservers("canClearContentChanged")
  },

  contentChanged: function() {
    this.notifyObservers("contentChanged")
  },

  updateWithProperty: function() {
    this.display.setContent(this.property.getValue())
  }
})

Widget.Internal.Control = Class.define(Widget.OptionsContainer, Widget.Observable,
{
  id: null,

  initializeControl: function(id, options) {
    this.installOptions(options);

    this.id = id;

    var element = this.getElement();

    this.value = this.getValueFromControl();

    Widget.addElementObserver(element, Widget.CHANGE, this.controlValueChanged.bindAsEventListener(this));
  },

  getValueFromControl: function() {
      return this.getElement().value
  },

  getValue: function() {
	  return this.value;
  },

  setValue: function(string) {
    this.getElement().value = string;
    this.checkValueChanged();
  },

  checkValueChanged: function() {
    if (this.value != this.getElement().value) {
      this.value = this.getElement().value;
      this.valueChanged();
    }
  },

  valueChanged: function() {
    this.notifyObservers('valueChanged')
  },

  controlValueChanged: function() {
    this.checkValueChanged()
  },

  getElement: function() {
    return $(this.id)
    }
});

Widget.Internal.Input = Class.define(Widget.Internal.Control, {
  initialize: function(id, options) {
    this.initializeControl(id, options);

    // Check every 0.1 seconds, if the value has changed.
    // Send 'valueChanged' callback, if it did.
    // TODO: Maybe there's another way to check for incremental value updates.
    // TODO: If this solution remains, don't spawn timer if there are no obervers
    // on partial value change. This requires to extends obervers architecture.
    this.lastPartialValue = this.getPartialValue();

    setInterval(this.checkPartialValueChanged.bind(this), 100);

  },

  getPartialValue: function() {
    return this.getElement().value;
  },

  partialValueChanged: function() {
    this.lastPartialValue = this.getPartialValue()

    this.notifyObservers('partialValueChanged')
  },

  checkPartialValueChanged: function() {
    if (this.getPartialValue() != this.lastPartialValue) {
      this.partialValueChanged()
    }
  }
});

Widget.Internal.Select = Class.define(Widget.Internal.Control, {

  internalValue: [],
  initialize: function(id, options) {
    this.initializeControl(id, options);
  },

  getValueFromControl: function() {
    if(this.multiple == false) {
      return this.getElement().value
    } else {
      this.value = $A();
      var options = this.getElement().options;
      for(var i = 0; i < options.length; i++) {
        this.value[i] = options[i].selected;
      }
      return this.value;
    }
  },

  // for single select type - value is string,
  // for multiple type - value is array of selected values
  setValue: function(value) {
    if(this.multiple == false) {
      this.getElement().value = value;
    } else {
      var options = this.getElement().options;
      for(var i = 0; i < options.length; i++) {
        if(value.indexOf(options[i].value) != -1) {
          options[i].selected = true;
        } else {
          options[i].selected = false;
        }
      }
    }
    this.checkValueChanged();
  },

  getValue: function() {
    if(this.multiple == false) {
      return this.value;
    } else {
      var array = $A();
      var options = this.getElement().options;
      for(var i = 0; i < this.value.length; i++) {
        if(this.value[i]) {
          array.push(options[i].value);
        }
      }
      return array;
    }
  },

  checkValueChanged: function() {
    if(this.multiple == false) {
      if (this.value != this.getElement().value) {
        this.value = this.getElement().value;
        this.valueChanged();
      }
    } else {
      var options = this.getElement().options;
      var changed = false;
      for(var i = 0; i < options.length; i++) {
        if(options[i].selected != this.value[i]) {
          changed = true;
          this.value[i] = options[i].selected;
        }
      }
      if(changed) {
        this.valueChanged();
      }
    }
  }
});

Widget.Control = Widget.define(
{
  property: null,

  initializeControl: function(internalControl, valueType, options) {
    this.initializeWidget(null, options)

    this.internalControl = internalControl

    // Those fields are set to true while changing property value
    // and/or activating, to prevent callback recursion.
    this.isUpdatingFromProperty = false

    if (this.property != null) {
      this.observe(this.property, "valueChanged", "propertyValueChanged")

      this.updateFromProperty()
    }

    this.observe(this.internalControl, "valueChanged", "internalControlValueChanged")
    this.addProperty("value", {type: valueType})
  },

  getValue: function() {
    return this.internalControl.getValue()
  },

  setValue: function(string) {
    this.internalControl.setValue(string)
  },

  internalControlValueChanged: function() {
    if (this.property != null && !this.isUpdatingFromProperty) {
      this.updateProperty()
    }

    this.valueChanged()
  },

  propertyValueChanged: function() {
    this.updateFromProperty()
  },

  updateFromProperty: function() {
    this.isUpdatingFromProperty = true

    this.setValue(this.property.getValue())

    this.isUpdatingFromProperty = false
  },

  updateProperty: function() {
    this.property.setValue(this.getValue())
  },

  valueChanged: function() {
    this.notifyObservers("valueChanged")
  }
});

/**
 * An Input widget
 */
Widget.Input = Class.define(Widget.Control,
{
  initialize: function(id, options) {
    this.initializeControl(new Widget.Internal.Input(id, options), "string", options);

    this.observe(this.internalControl, "partialValueChanged", "partialValueChanged");
    this.addProperty("partial-value")
  },

  getPartialValue: function() {
    return this.internalControl.getPartialValue()
  },

  partialValueChanged: function() {
    this.notifyObservers("partialValueChanged")
  }

});

/**
 * An Select widget
 */
Widget.Select = Class.define(Widget.Control,
{
  initialize: function(id, options) {
    if($(id).getAttribute('multiple') == 'multiple') {
      this.initializeControl(new Widget.Internal.Select(id, options), "list", options)
    } else {
      this.initializeControl(new Widget.Internal.Select(id, options), "string", options)
    }
  }
});

/**
 * A Script widget
 */
Widget.Script = Widget.define(
{
  initialize: function(content, options) {
    this.initializeWidget(null, options)

    eval("this.func = function(){" + content + "}")

    this.addAction("invoke")
  },

  invoke: function() {
    this.func.apply(this, arguments)
  }
})

/**
 * A handler widget, connects event with action.
 */
Widget.Handler = Widget.define(
{
  initialize: function(event, action, options) {
    this.initializeWidget(null, options)

    this.enabled = true
    this.event = event
    this.action = action

    if (this.getOption("isEnabled") != null) {
      this.setEnabled(this.getOption("isEnabled"))
    }

    this.observe(event, "sent", "eventSent")

    this.addProperty("is-enabled")
  },

  isEnabled: function() {
    return this.enabled
  },

  setEnabled: function(enabled) {
    if (this.enabled != enabled) {
      this.enabled = enabled

      this.notifyObservers("isEnabledChanged")
    }
  },

  eventSent: function() {
    if (this.enabled) {
      this.action.invoke.apply(this.action, arguments)
    }
  }
})

/**
 * A generic base class for Content widgets.
 */
Widget.Internal.Content = Widget.define(
{
  /**
   * Initialization.
   *
   * The 'id' and 'html' arguments are exclusive.
   *
   * If 'id' is specified, it means that the HTML content
   * of this element is already rendered on the page, inside
   * the parent container. The 'parent' option is required
   * in that case.
   *
   * If 'html' is specified, it means that the content
   * is held as a HTML string. The optional 'script' option
   * contains a script to execute, when the content is attached
   * to the page.
   *
   * A HTML parameter may be a string, or DOM element.
   */
  initializeContent: function(id, html, parent, script, options) {
    this.initializeWidget(id, options)

    this.html = html
    this.parent = parent
    this.script = script

    if (id == null) {
      if (typeof(html) == "string") {
        // Case 1: A html parameter is a HTML string
        var tempElement = document.createElement("div")

        tempElement.innerHTML = html.stripScripts()

        var scripts = html.extractScripts()

        var script = ""

        for (var i = 0; i < scripts.length; i++) {
          script += scripts[i] + ";"
        }

        this.script = script

        this.element = tempElement.firstChild

      } else {
        // Case 2: A html parameter is DOM element.
        this.element = html
      }
    } else {
      this.parent.children.push(this)
    }
  },

  getContainer: function() {
    return this.parent
  },

  runScript: function() {
    if (this.script != null) {
      eval(this.script)
    }
  },

  adding: function() {
    this.notifyObservers("adding")
  },

  added: function() {
    this.runScript()

    this.notifyObservers("added")

    this.notifyObservers("containerChanged")
  },

  removing: function() {
    this.notifyObservers("removing")
  },

  removed: function() {
    this.notifyObservers("removed")
  }
})

/**
 * BlockContent encapsulated any MixedFlow content.
 * It can be placed in a Block widget.
 */
Widget.Internal.BlockContent = Class.define(Widget.Internal.Content,
{
  initialize: function(id, html, parent, script, options) {
    this.initializeContent(id, html, parent, script, options)
  },

  clone: function() {
    return new Widget.Internal.BlockContent(null, this.getElement().cloneNode(true), null, null, this)
  }
})

/**
 * InlineContent encapsulated any MixedText content.
 * It can be placed in a Block and Inline widgets.
 */
Widget.Internal.InlineContent = Class.define(Widget.Internal.Content,
{
  initialize: function(id, html, parent, script, options) {
    this.initializeContent(id, html, parent, script, options)
  },

  clone: function() {
    return new Widget.Internal.InlineContent(null, this.getElement().cloneNode(true), null, null, this)
  }
})

/**
 * PCDATAContent encapsulated a String content.
 * It can be placed in a Block and Inline widgets.
 */
Widget.Internal.PCDATAContent = Class.define(Widget.Internal.Content,
{
  initialize: function(id, string, parent, options) {
    if (id != null) {
      this.initializeContent(id, null, parent, options)
    } else {
      this.initializeContent(null, "<span>" + string.escapeHTML() + "</span>", parent, options)
    }
  },

  clone: function() {
    return new Widget.Internal.PCDATAContent(null, this.getElement().innerHTML.unescapeHTML(), null, this)
  }
})

/**
 * A generic base class for Block and Inline widgets.
 */
Widget.Internal.Container = Widget.define(
{
  initializeContainer: function(id, options) {
    this.initializeWidget(id, options)

    this.children = []

    // Set initial content, of specified.
    if (this.getOption("content") != null) {
      this.setContent(this.getOption("content"))
    }
  },

  getAt: function(index) {
    if (index >= 1 && index <= this.children.length) {
      return this.children[index - 1]
    }
  },

  addLast: function(content) {
    this.addAt(content, this.children.length + 1)
  },

  addFirst: function(content) {
    this.addAt(content, 1)
  },

  addAt: function(content, index) {
    if (index < 1 || index > this.children.length + 1) {
      return
    }

    // If widget is already attached, do nothing.
    if (content.parent != null) {
      return
    }

    // Inform content, that it's going to be attached.
    content.adding()

    // Attach the widget.
    this.children.splice(index - 1, 0, content)

    content.parent = this

    // Attach the markup.
    var parentElement = this.getElement()

    var addedElement = content.getElement()

    if (index < this.children.length) {
      var existingElement = this.children[index].getElement()

      parentElement.insertBefore(addedElement, existingElement)
    } else {
      parentElement.appendChild(addedElement)
    }

    this.notifyObservers("sizeChanged")

    // Inform content, that it has been attached.
    content.added()
  },

  remove: function(content) {
    for (var i = 0; i < this.children.length; i++) {
      if (this.children[i] === content) {
        this.removeAt(i + 1)

        return
      }
    }
  },

  removeFirst: function() {
    this.removeAt(1)
  },

  removeLast: function() {
    this.removeAt(this.children.length)
  },

  removeAt: function(index) {
    // If index is out of bound, return immediately
    if (index < 1 || index > this.children.length) {
      return
    }

    var content = this.children[index - 1]

    // If widget is already detached or is not a child of
    // this widget, do nothing.
    if (content.parent !== this) {
      return
    }

    // Inform content, that it's goind to be detached.
    content.removing()

    // Detach the widget.
	this.children.splice(index - 1, 1)

	content.parent = null

    // Detach the markup.
    var parentElement = this.getElement()

    var childElement = content.getElement()

    parentElement.removeChild(childElement)

    this.notifyObservers("sizeChanged")

    // Inform content, that it has been detached.
    content.removed()
  },

  getSize: function() {
    return this.children.length
  },

  setContent: function(content) {
    if (content == null || content.parent == null) {
      while (this.getSize() > 0) {
        this.removeLast()
      }

      if (content != null) {
        this.addLast(content)
      }

      this.content = content

      this.notifyObservers("contentChanged")
    }
  },

  getContent: function() {
    return this.content
  }
})

/**
 * Block Container widget displays a live list of
 * BlockContent, InlineContent and PCDATAContent widgets.
 * The list of Content widgets may be updated live.
 */
Widget.Internal.BlockContainer = Class.define(Widget.Internal.Container,
{
  initialize: function(id, options) {
    this.initializeContainer(id, options)

    if (this.getOption("content") != null) {
      this.setContent(this.getOption("content"))
    }
  }
})

/**
 * Inline Container widget displays a live list of
 * InlineContent and PCDATAContent widgets.
 * The list of Content widgets may be updated live.
 */
Widget.Internal.InlineContainer = Class.define(Widget.Internal.Container,
{
  initialize: function(id, options) {
    this.initializeContainer(id, options)

    if (this.getOption("content") != null) {
      this.setContent(this.getOption("content"))
    }
  }
})

/* MOVED FROM  prototype.patch */

// TODO: Move all the below code to a separate 'capabilities' class
Prototype.isEmulatedOpacity = null;

// TODO: we test for non-existance of HTMLElement to detect
// NetFront 3.4. Should be replaced with Navigator.userAgent check
// when its value is known.
//  /NetFront/.test(navigator.userAgent)
Prototype.netFront = function() {
  // previous method of checking if browser is mobile failed for newer version of NetFront
  // standard method by userAgent is used instead
  return (/NetFront/.test(navigator.userAgent));
};

Prototype.nokiaOSSBrowser = function(){
  // most suitable so far
  return (/AppleWebKit/.test(navigator.userAgent) && /SymbianOS/.test(navigator.userAgent));
};

Prototype.firefoxBrowser = function(){
  return (/Firefox/.test(navigator.userAgent));
};

Prototype.konquerorBrowser = function() {
  return (/Konqueror/.test(navigator.userAgent));
};

Prototype.operaPC = function() {
  return (/Opera\/9./.test(navigator.userAgent));
};


// TODO: could someone please explain why do we set isEmulatedOpacity
// on each call to this function?
// And why there is no 'else' after each 'if'?
Prototype.useEmulatedOpacity = function(){
  if(this.netFront()){
    this.isEmulatedOpacity = true;
  }
  if(this.nokiaOSSBrowser()){
    this.isEmulatedOpacity = true;
  }
  if(this.msieBrowser()){
    this.isEmulatedOpacity = false;
  }
  if(this.isEmulatedOpacity == null){
    var element = document.documentElement;
    if(element.getStyle('opacity') != 1){
      this.isEmulatedOpacity = true;
    } else {
      this.isEmulatedOpacity = false;
    }
  }
  return (this.isEmulatedOpacity);
};

Prototype.useMouseAsSelect = function(){
  return this.nokiaOSSBrowser() || this.operaMobile();
};

// End of code to be moved to "capabilities" class

// Workarounds for NetFront 3.4 pre-release [Volantis]
//
// Fixed problems:
//
// 1) Class Abstract.Insertion and its derived classes Insertion.Before,
// Insertion.After, Insertion.Top, Insertion.Bottom, make use of the following
// methods to insert HTML content:
// - insertAdjacentHTML of HTMLElement
// or
// - createRange of Document
// Both are unavailable on Netfront 3.4, so we use the third approach
// for NetFront. See insertHTML() implementations
//
// 2) Setting of innerHTML does not handle tables properly. This problem affects:
// - all Abstract.Insertion dericed classes
// - Element.update and Element.replace
// The workaround is implemented in Table object and used in our implementations
// of the affected methods

var Table = {
  createRowChildren: function(content) {
    var div = document.createElement('div');
    div.innerHTML = '<table><tbody><tr>' + content + '</tr></tbody></table>';
    return $A(div.childNodes[0].childNodes[0].childNodes[0].childNodes);
  },

  createTbodyChildren: function(content) {
    var div = document.createElement('div');
    div.innerHTML = '<table><tbody>' + content + '</tbody></table>';
    return $A(div.childNodes[0].childNodes[0].childNodes);
  },

  createColGroupChildren: function(content) {
    var div = document.createElement('div');
    div.innerHTML = '<table><colgroup>' + content + '</colgroup></table>';
    return $A(div.childNodes[0].childNodes[0].childNodes);
  },

  createTableChildren: function(content) {
    var div = document.createElement('div');
    div.innerHTML = '<table>' + content + '</table>';
    return $A(div.childNodes[0].childNodes);
  },

  isParentOfTableElements: function(tagName) {
    return tagName == "table"
    || tagName == "tbody"
    || tagName == "tfoot"
    || tagName == "thead"
    || tagName == "tr"
    || tagName == "colgroup";
  },

  createChildren: function(tagName, content) {
    var subnodes;
    if(tagName == 'table') {
      subnodes = this.createTableChildren(content)
    } else if(tagName == 'tbody' || tagName == 'tfoot'|| tagName == 'thead') {
      subnodes = this.createTbodyChildren(content)
    } else if(tagName == 'tr') {
      subnodes = this.createRowChildren(content)
    } else if(tagName == 'colgroup') {
      subnodes = this.createColGroupChildren(content)
    }
    return subnodes
  }
}

Abstract.Insertion.prototype.originalInitialize = Abstract.Insertion.prototype.initialize

Object.extend(Abstract.Insertion.prototype, {
  initialize: function(element, content) {
    this.element = $(element);
    this.content = content.stripScripts();
    if(this.element.insertAdjacentHTML || this.element.ownerDocument.createRange) {
      this.originalInitialize(element, content);
    } else {
      this.insertHTML();
      setTimeout(function() {content.evalScripts()}, 10);
    }
  },

  getFirstTag: function(parentNode, startIdx) {
    if(startIdx == parentNode.innerHTML.length-1) {
      return "";
    }
    var singleQuotationOpened = false;
    var doubleQuotationOpened = false;
    var htmlStr = parentNode.innerHTML;
    var currIdx = startIdx + 1;
    var currentChar;
    do {
      currentChar = htmlStr.charAt(currIdx);
      if(currentChar == ">") {
        if(!(singleQuotationOpened || doubleQuotationOpened)) {
          break;
        }
      }
      if(currentChar == "'" && !doubleQuotationOpened) {
        singleQuotationOpened = !singleQuotationOpened;
      }
      if(currentChar == '"' && !singleQuotationOpened) {
        doubleQuotationOpened = !doubleQuotationOpened;
      }
      currIdx++;
    } while(currIdx < htmlStr.length)
    return htmlStr.substring(startIdx, currIdx+1);
  },

  getElementAsString: function(parentNode, childNode, currIdx) {
    var ret;
    if(childNode.innerHTML) {
      ret = this.getFirstTag(parentNode, currIdx);
      ret = ret + childNode.innerHTML;
      var firstClosingTagOccur = parentNode.innerHTML.indexOf("</", currIdx+ret.length);
      if(firstClosingTagOccur == currIdx+ret.length) {
        var endOfClosingTag = parentNode.innerHTML.indexOf(">", currIdx+ret.length);
        ret = ret+parentNode.innerHTML.substring(firstClosingTagOccur, endOfClosingTag+1);
      }
    } else if(childNode.data) {
      ret = childNode.data;
    } else {
      ret = this.getFirstTag(parentNode, currIdx);
      var firstClosingTagOccur = parentNode.innerHTML.indexOf("</", currIdx+ret.length);
      if(firstClosingTagOccur == currIdx+ret.length) {
        var endOfClosingTag = parentNode.innerHTML.indexOf(">", currIdx+ret.length);
        ret = ret + parentNode.innerHTML.substring(firstClosingTagOccur, endOfClosingTag + 1);
      }
    }
    return ret
  }
});

Object.extend(Insertion.Before.prototype, {
  insertHTML:  function () {
    var parentNode = this.element.parentNode;
    var tagName = parentNode.tagName.toLowerCase();
    if (Table.isParentOfTableElements(tagName)) {
      var subnodes = Table.createChildren(tagName, this.content);
      subnodes.reverse(false).each(function(sn) {
        parentNode.insertBefore(sn, this.element);
      }.bind(this));
    } else {
      var str = "";
      var currIdx = 0;
      var currentChild = parentNode.firstChild;
      while(currentChild && currentChild != this.element) {
        var elemAsStr = this.getElementAsString(parentNode, currentChild, currIdx);
        str = str + elemAsStr;
        currIdx = currIdx + elemAsStr.length;
        currentChild = currentChild.nextSibling;
      }
      str = str + this.content;
      str = str + parentNode.innerHTML.substring(currIdx);
      parentNode.innerHTML =  str;
    }
  }
});

Object.extend(Insertion.After.prototype, {
  insertHTML:  function () {
    var parentNode = this.element.parentNode;
    var tagName = parentNode.tagName.toLowerCase();
    if(Table.isParentOfTableElements(tagName)) {
      var subnodes = Table.createChildren(tagName, this.content);
      if(this.element.nextSibling) {
        subnodes.each(function(sn){parentNode.insertBefore(sn, this.element.nextSibling)}.bind(this));
      } else {
        subnodes.each(function(sn){parentNode.appendChild(sn)});
      }
    } else {
      var str = "";
      var currIdx = 0;
      var currentChild = parentNode.firstChild;
      do {
        var elemAsStr = this.getElementAsString(parentNode, currentChild, currIdx);
        str = str + elemAsStr;
        currIdx = currIdx + elemAsStr.length;
        currentChild = currentChild.nextSibling;
      } while(currentChild && currentChild.previousSibling != this.element)
      str = str + this.content;
      str = str + parentNode.innerHTML.substring(currIdx);
      parentNode.innerHTML =  str;
    }
  }
});

Object.extend(Insertion.Top.prototype, {
  insertHTML:  function() {
    var tagName = this.element.tagName.toLowerCase();
    if(Table.isParentOfTableElements(tagName)) {
      var subnodes = Table.createChildren(tagName, this.content);
      this.insertContent(subnodes);
    } else {
      var str = this.element.innerHTML;
      this.element.innerHTML = this.content + str;
    }
  }
});

Object.extend(Insertion.Bottom.prototype, {
  insertHTML:  function() {
    var tagName = this.element.tagName.toLowerCase();
    if (Table.isParentOfTableElements(tagName)) {
      var subnodes = Table.createChildren(tagName, this.content);
      this.insertContent(subnodes);
    } else {
      var str = this.element.innerHTML;
      this.element.innerHTML = str + this.content;
    }
  }
});

Element.originalUpdate = Element.update;
Element.originalReplace = Element.replace;

Object.extend(Element, {
  update: function(element, html) {
    if (Prototype.netFront()) {
      var wholeElement = $(element);
      var tagName = wholeElement.tagName.toLowerCase();
      if (Table.isParentOfTableElements(tagName)) {
        var subnodes = Table.createChildren(tagName, html.stripScripts());
        wholeElement.innerHTML = "";
        subnodes.each(function(s) { wholeElement.appendChild(s); });
        setTimeout(function() { html.evalScripts() }, 10);
      } else {
        this.originalUpdate(element, html);
      }
    } else {
      this.originalUpdate(element, html);
    }
  },

  replace: function(element, html) {
    if (Prototype.netFront()) {
      var wholeElement = $(element);
      var parentNode = wholeElement.parentNode;
      var tagName = parentNode.tagName.toLowerCase();
      if (Table.isParentOfTableElements(tagName)) {
        var subnodes = Table.createChildren(tagName, html.stripScripts());
        if(subnodes.length == 0) {
          parentNode.removeChild(wholeElement);
        } else {
          var lastSubnode = subnodes[subnodes.length-1];
          parentNode.replaceChild(lastSubnode, wholeElement);
          for (var i = 0; i <= subnodes.length-2; i++) {
            parentNode.insertBefore(lastSubnode);
          }
        }
        setTimeout(function() {html.evalScripts()}, 10);
      } else {
        this.originalReplace(element, html);
      }
    } else {
      this.originalReplace(element, html);
    }
  }
});


/* END: MOVED FROM  prototype.patch */

/* MOVED FROM  effects.patch */

/*--------------------------------------------------------------------------*/
// support for faking opacity - Dave Raggett June 2006
// see also http://www.phpied.com/rgb-color-parser-in-javascript/

// rgbString must be of form "rgb(100,200,0)" or "transparent"
// however this is guaranteed when using computed colors
// but not with NetFront, sigh!!!
function Color(rgbString)
{
  this._rgba = false;
  if (rgbString == "transparent")
  {
    this.toString = function () { return "transparent" };
    this.transparent = true;
    return this;
  }

  // full color table is quite large ...
  var color = namedColors[rgbString];

  if (color)
    rgbString = color;

  if (rgbString[0] == '#')
  {
    var rex = /^#(\w{2})(\w{2})(\w{2})$/;
    var nums = rex.exec(rgbString);

    if (!nums)
    {
      // use of parse color to convert from #xxx to #xxxxxx
      nums = rex.exec(rgbString.parseColor());
    }

    this.red = parseInt(nums[1], 16);
    this.green = parseInt(nums[2], 16);
    this.blue = parseInt(nums[3], 16);
    this.transparent = false;
  } else if (rgbString.indexOf('rgba')!= -1){
    var rex = /^rgba\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;
    var nums = rex.exec(rgbString);

    if (!nums)
      throw "invalid color: " + rgbString;

    this.red = parseInt(nums[1]);
    this.green = parseInt(nums[2]);
    this.blue = parseInt(nums[3]);
    this.alpha = parseInt(nums[4]);
    this.transparent = false;
    this._rgba = true;

    this.toString = function () {
      return "rgba(" + this.red + "," + this.green + "," + this.blue + ","+this.alpha+")";
    };
  } else if (rgbString[0] == 'r'){
    var rex = /^rgb\((\d{1,3}),\s*(\d{1,3}),\s*(\d{1,3})\)$/;
    var nums = rex.exec(rgbString);

    if (!nums)
      throw "invalid color: " + rgbString;

    this.red = parseInt(nums[1]);
    this.green = parseInt(nums[2]);
    this.blue = parseInt(nums[3]);
    this.transparent = false;

    this.toString = function () {
      return "rgb(" + this.red + "," + this.green + "," + this.blue + ")";
    };
  }
  return this;
}

// return blended color from foreground color object
// background color object and the opacity (0..1)
// assumes background is itself opaque
function BlendColor(fg, bg, opacity)
{

  if (typeof opacity != "number" || opacity < 0 || opacity > 1){
    // if opacity undefined or ou of bound it wil be set to 1
    // then background is invisible
    // no exception must be thrown because it results unexpected errors
    // on opera platform
    opacity = 1.0;
  }

  if (!fg || opacity < 0.001){
    return bg;
  }
  // when opacity == 1 we still calculate it, because
 // we still need to handle two casess rgb or rgba
  if(fg.transparent){
      this.red = Math.floor( (1 - opacity)* bg.red);
      this.green = Math.floor( (1 - opacity)* bg.green);
      this.blue = Math.floor((1 - opacity)* bg.blue);
  } else if(bg.transparent){
      this.red = Math.floor(opacity * fg.red);
      this.green = Math.floor(opacity * fg.green);
      this.blue = Math.floor(opacity * fg.blue);
  } else {
      this.red = Math.floor(opacity * fg.red + (1 - opacity)* bg.red);
      this.green = Math.floor(opacity * fg.green + (1 - opacity)* bg.green);
      this.blue = Math.floor(opacity * fg.blue + (1 - opacity)* bg.blue);
  }
  if(fg._rgba) {
    this.toString = function () {
      return "rgba(" + this.red + "," + this.green + "," + this.blue + ","+fg.alpha+")";
    }
  } else {
    this.toString = function () {
      return "rgb(" + this.red + "," + this.green + "," + this.blue + ")";
    }
  }
  return this;
}


Element.saveStylesFakeOpacity = function(element){
  element= $(element);
  if(!element._madeFakeOpacity) {
    element._madeFakeOpacity = true;

    element._fo_foreground = Element.getStyle(element,"background-color");
    element._fo_fg = new Color(element._fo_foreground);
    element._fo_text = Element.getStyle(element,"color");
    element._fo_tx = new Color(element._fo_text);
    element._fo_border_left = Element.getStyle(element,"border-left-color");
    element._fo_bo = new Color(element._fo_border_left);
    element._fo_border_right = Element.getStyle(element,"border-right-color");
    element._fo_border_top = Element.getStyle(element,"border-top-color");
    element._fo_border_bottom = Element.getStyle(element,"border-bottom-color");
    element._fo_backgroud = Element.getNonTransparentBackground(element.parentNode);
    element._fo_bg = new Color(element._fo_backgroud);
  }
}


Element.getNonTransparentBackground = function(element){
  var node = element;
  var background = Element.getStyle(node, "background-color");
  // on OperaMobile, NokiaOSS, Netfront (all don't support opacity) when outer element is fixed
  // there were issue with end loop condition, therefore calculation must be end when HTML is reached.
  while(((background == "transparent") || (background=="rgba(0, 0, 0, 0)")) && (node.parentNode != null)
	&& (node.nodeName !== "HTML")){
    node = node.parentNode;
    background = Element.getStyle(node, "background-color");
  }
  if((background === "transparent") || (node.nodeName === "HTML")){
    background = "rgb(255,255,255)";
  }
  // when we wound rgba also rgba must be set
  if(background === "rgba(0, 0, 0, 0)"){
    background = "rgba(255,255,255,0)";
  }
  return background;
}

Element.restoreStylesFakeOpacity = function(element){
  element= $(element);
  if(element._madeFakeOpacity) {
    element._madeFakeOpacity = undefined;
    element.style.backgroundColor = element._fo_foreground;
    element.style.color = element._fo_text;
    element.style.borderLeftColor = element._fo_border;
    element.style.borderLeftColor = element._fo_border_left;
    element.style.borderRightColor = element._fo_border_right;
    element.style.borderTopColor = element._fo_border_top;
    element.style.borderBottomColor = element._fo_border_bottom;
  }
}

Element.fakeOpacity = function (element, opacity) {
    element= $(element);
    var i = 0;
    // a markup needs special treatment - this is workaround
    // permanent fix needs reimplementation of fakeOpacity
    var childList = element.getElementsByTagName("a");
    for(i = 0;i<childList.length;i++){
      Element.aFakeOpacity(childList[i],opacity);
    }
    if(opacity){
      Element.normalFakeOpacity(element,opacity);
    }
};

Element.aFakeOpacity = function (element, opacity) {
    element= $(element);
    var faked = new BlendColor(element._fo_tx, element._fo_bg, opacity);
    element.style.color = faked.toString();
    element.style.opacity = opacity.toString();
};


Element.normalFakeOpacity = function (element, opacity) {
    element= $(element);
    Element.saveStylesFakeOpacity(element);

    var faked = new BlendColor(element._fo_fg, element._fo_bg, opacity);
    element.style.backgroundColor = faked.toString();
    faked = new BlendColor(element._fo_tx, element._fo_bg, opacity);
    element.setStyle({ color :faked.toString()});
    faked = new BlendColor(element._fo_bo, element._fo_bg, opacity);
    element.style.borderColor = faked.toString();
    element.style.opacity = opacity.toString();
  };



function SetFakeOpacity(id, opacity)
{
  var el = $(id);

  if (!el)
    throw "unknown element id: " + id;

  if (!el.getStyle)
  {
    el.getStyle = function (style) {
      return computedStyle(this, style);
    };

    el.parentNode.getStyle = function (style) {
      return computedStyle(this, style);
    };
  }

  if (opacity < 0.99)
  {
    Element.fakeOpacity(el, opacity);
  }
}

var namedColors = {
  aliceblue: '#f0f8ff',
  antiquewhite: '#faebd7',
  aqua: '#00ffff',
  aquamarine: '#7fffd4',
  azure: '#f0ffff',
  beige: '#f5f5dc',
  bisque: '#ffe4c4',
  black: '#000000',
  blanchedalmond: '#ffebcd',
  blue: '#0000ff',
  blueviolet: '#8a2be2',
  brown: '#a52a2a',
  burlywood: '#deb887',
  cadetblue: '#5f9ea0',
  chartreuse: '#7fff00',
  chocolate: '#d2691e',
  coral: '#ff7f50',
  cornflowerblue: '#6495ed',
  cornsilk: '#fff8dc',
  crimson: '#dc143c',
  cyan: '#00ffff',
  darkblue: '#00008b',
  darkcyan: '#008b8b',
  darkgoldenrod: '#b8860b',
  darkgray: '#a9a9a9',
  darkgreen: '#006400',
  darkkhaki: '#bdb76b',
  darkmagenta: '#8b008b',
  darkolivegreen: '#556b2f',
  darkorange: '#ff8c00',
  darkorchid: '#9932cc',
  darkred: '#8b0000',
  darksalmon: '#e9967a',
  darkseagreen: '#8fbc8f',
  darkslateblue: '#483d8b',
  darkslategray: '#2f4f4f',
  darkturquoise: '#00ced1',
  darkviolet: '#9400d3',
  deeppink: '#ff1493',
  deepskyblue: '#00bfff',
  dimgray: '#696969',
  dodgerblue: '#1e90ff',
  feldspar: '#d19275',
  firebrick: '#b22222',
  floralwhite: '#fffaf0',
  forestgreen: '#228b22',
  fuchsia: '#ff00ff',
  gainsboro: '#dcdcdc',
  ghostwhite: '#f8f8ff',
  gold: '#ffd700',
  goldenrod: '#daa520',
  gray: '#808080',
  green: '#008000',
  greenyellow: '#adff2f',
  honeydew: '#f0fff0',
  hotpink: '#ff69b4',
  indianred : '#cd5c5c',
  indigo : '#4b0082',
  ivory: '#fffff0',
  khaki: '#f0e68c',
  lavender: '#e6e6fa',
  lavenderblush: '#fff0f5',
  lawngreen: '#7cfc00',
  lemonchiffon: '#fffacd',
  lightblue: '#add8e6',
  lightcoral: '#f08080',
  lightcyan: '#e0ffff',
  lightgoldenrodyellow: '#fafad2',
  lightgrey: '#d3d3d3',
  lightgreen: '#90ee90',
  lightpink: '#ffb6c1',
  lightsalmon: '#ffa07a',
  lightseagreen: '#20b2aa',
  lightskyblue: '#87cefa',
  lightslateblue: '#8470ff',
  lightslategray: '#778899',
  lightsteelblue: '#b0c4de',
  lightyellow: '#ffffe0',
  lime: '#00ff00',
  limegreen: '#32cd32',
  linen: '#faf0e6',
  magenta: '#ff00ff',
  maroon: '#800000',
  mediumaquamarine: '#66cdaa',
  mediumblue: '#0000cd',
  mediumorchid: '#ba55d3',
  mediumpurple: '#9370d8',
  mediumseagreen: '#3cb371',
  mediumslateblue: '#7b68ee',
  mediumspringgreen: '#00fa9a',
  mediumturquoise: '#48d1cc',
  mediumvioletred: '#c71585',
  midnightblue: '#191970',
  mintcream: '#f5fffa',
  mistyrose: '#ffe4e1',
  moccasin: '#ffe4b5',
  navajowhite: '#ffdead',
  navy: '#000080',
  oldlace: '#fdf5e6',
  olive: '#808000',
  olivedrab: '#6b8e23',
  orange: '#ffa500',
  orangered: '#ff4500',
  orchid: '#da70d6',
  palegoldenrod: '#eee8aa',
  palegreen: '#98fb98',
  paleturquoise: '#afeeee',
  palevioletred: '#d87093',
  papayawhip: '#ffefd5',
  peachpuff: '#ffdab9',
  peru: '#cd853f',
  pink: '#ffc0cb',
  plum: '#dda0dd',
  powderblue: '#b0e0e6',
  purple: '#800080',
  red: '#ff0000',
  rosybrown: '#bc8f8f',
  royalblue: '#4169e1',
  saddlebrown: '#8b4513',
  salmon: '#fa8072',
  sandybrown: '#f4a460',
  seagreen: '#2e8b57',
  seashell: '#fff5ee',
  sienna: '#a0522d',
  silver: '#c0c0c0',
  skyblue: '#87ceeb',
  slateblue: '#6a5acd',
  slategray: '#708090',
  snow: '#fffafa',
  springgreen: '#00ff7f',
  steelblue: '#4682b4',
  tan: '#d2b48c',
  teal: '#008080',
  thistle: '#d8bfd8',
  tomato: '#ff6347',
  turquoise: '#40e0d0',
  violet: '#ee82ee',
  violetred: '#d02090',
  wheat: '#f5deb3',
  white: '#ffffff',
  whitesmoke: '#f5f5f5',
  yellow: '#ffff00',
  yellowgreen: '#9acd32'
};


// If effects.js isn't included, Effect is an undefined variable
// so we need to check for that situation
if (typeof(Effect) != 'undefined') {

  Object.extend(Effect.Scale.prototype, {
    vfcSetup: function() {
      if(! (/^content/.test(this.options.scaleMode))) {
        this.borderLeftWidthOld = '0px';
        this.borderRightWidthOld = '0px';
        this.borderTopWidthOld = '0px';
        this.borderBottomWidthOld = '0px';

        this.paddingLeftOld = '0px';
        this.paddingRightOld = '0px';
        this.paddingTopOld = '0px';
        this.paddingBottomOld = '0px';

        this.contentWidth = 0;
        this.contentHeight = 0;

        var divHeight = this.element.vfcGetDimensions().height;
        var divWidth = this.element.vfcGetDimensions().width;

        // get dimensions of borders

        // temporary set top and bottom borders to 0 px
        var border = Element.getStyle(this.element, 'border-top-width');
        if(parseInt(border) != 0 && this.options.scaleY) {
          this.borderTopWidthOld = border;
          Element.setStyle(this.element, {borderTopWidth: '0px'});
        }

        border = Element.getStyle(this.element, 'border-bottom-width');
        if(parseInt(border) != 0 && this.options.scaleY) {
          this.borderBottomWidthOld = border;
          Element.setStyle(this.element, {borderBottomWidth: '0px'});
        }
        // offsetHeight from vfcGetDimensions().height  working good
        this.bordersHeight = divHeight - this.element.vfcGetDimensions().height;
        this.bordersWidth = 0;

        // get left and right border width in another way -
        // strange bahaviour with offsetWidth - only offsetWidth return the same value before and after changing border-left-width and border-right-width values
        // workaround is read direct values from this.borderLeftWidthOld and this.borderRightWidthOld because for all browsers return style value in PX independent of unit set by user (except IE)
        // for IE may use difference between offsetWidth and clientWidth = bordersWidth
        if(Prototype.msieBrowser()) {
          this.bordersWidth = this.element.offsetWidth - this.element.clientWidth;
        } else {
          border = Element.getStyle(this.element, 'border-left-width');
          if(parseInt(border) != 0 && this.options.scaleX) {
            this.borderLeftWidthOld = border;
            this.borderRightWidthOld = Element.getStyle(this.element, 'border-right-width');
            this.bordersWidth = parseInt(this.borderLeftWidthOld);
          }

          border = Element.getStyle(this.element, 'border-right-width');
          if(parseInt(border) != 0 && this.options.scaleX) {
            this.borderRightWidthOld = border;
            this.bordersWidth += parseInt(this.borderRightWidthOld);
          }
        }

        // temporary set paddings to 0 px
        var divHeightBefore = this.element.vfcGetDimensions().height;
        this.paddingsWidth = 0;
        var padding = Element.getStyle(this.element, 'padding-left');
        if(parseInt(padding) != 0 && this.options.scaleX) {
          this.paddingLeftOld = padding;
          if(Prototype.msieBrowser()) {
            this.paddingsWidth = Widget.getPXDimension(this.paddingLeftOld);
          } else {
            this.paddingsWidth = parseInt(this.paddingLeftOld);
          }
        }

        padding = Element.getStyle(this.element, 'padding-right');
        if(parseInt(padding) != 0 && this.options.scaleX) {
          if(Prototype.msieBrowser()) {
            this.paddingsWidth += Widget.getPXDimension(this.paddingRightOld);
          } else {
            this.paddingsWidth += parseInt(this.paddingRightOld);
          }
        }

        padding = Element.getStyle(this.element, 'padding-top');
        if(parseInt(padding) != 0 && this.options.scaleY) {
          this.paddingTopOld = padding;
          Element.setStyle(this.element, {paddingTop: '0px'});
        }

        padding = Element.getStyle(this.element, 'padding-bottom');
        if(parseInt(padding) != 0 && this.options.scaleY) {
          this.paddingBottomOld = padding;
          Element.setStyle(this.element, {paddingBottom: '0px'});
        }

        var divHeightAfter = this.element.vfcGetDimensions().height;
        this.paddingsHeight = divHeightBefore - divHeightAfter;

        this.paddingsHeight = divHeightBefore - divHeightAfter;
        this.contentWidth = divWidth - this.paddingsWidth - this.bordersWidth;
        this.contentHeight = divHeight - this.paddingsHeight - this.bordersHeight;

      }
    },

    recursiveCollectOriginalStyle : function (childNodes){
      var i = 0;
      for(i = 0;i<childNodes.length;i++){
        var child = childNodes[i];
        if(child.hasChildNodes()){
          this.recursiveCollectOriginalStyle(child.childNodes);
        }
        // 'width',
        var styleArray = ['top','left','height','fontSize', 'width'];
        child._originalStyle = {};
        var l = 0;
        for(l = 0;l < styleArray.length;l++){
          try {
            child._originalStyle[styleArray[l].camelize()] = child.style[styleArray[l].camelize()];
          } catch(e){
          }
        }

        if(child['getDimensions'] != undefined){
          var dim = child.getDimensions();
          child._height = dim.height;
          child._width = dim.width;
        }
        child._originalTop  = child.offsetTop;
        child._originalLeft = child.offsetLeft;

        // line height
        try {
            var lineHeight = child.getStyle('line-height') || '100%';
            child._lineHeightValue = lineHeight;
            if(lineHeight == 'normal') {
              lineHeight = '100%';
            }
            ['em','px','%'].each( function(lineHeightType) {
              if(lineHeight.indexOf(lineHeightType)>0) {
                child._lineHeight = parseInt(lineHeight);
                child._lineHeightUnit = lineHeightType;
              }
            });
        } catch (e){
        }
      }
    },

    vfcFinish : function (position) {
      if(! (/^content/.test(this.options.scaleMode))) {
        if(this.options.scaleX) {
          if(this.bordersWidth > 0) {
            Element.setStyle(this.element, {borderLeftWidth: this.borderLeftWidthOld + 'px'});
            Element.setStyle(this.element, {borderRightWidth: this.borderRightWidthOld + 'px'});
          }

          if(this.paddingsWidth > 0) {
            Element.setStyle(this.element, {paddingLeft: this.paddingLeftOld + 'px'});
            Element.setStyle(this.element, {paddingRight: this.paddingRightOld + 'px'});
          }
        }

        if(this.options.scaleY) {
          if(this.bordersHeight > 0) {
            Element.setStyle(this.element, {borderTopWidth: this.borderTopWidthOld + 'px'});
            Element.setStyle(this.element, {borderBottomWidth: this.borderBottomWidthOld + 'px'});
          }

          if(this.paddingsHeight > 0) {
            Element.setStyle(this.element, {paddingTop: this.paddingTopOld + 'px'});
            Element.setStyle(this.element, {paddingBottom: this.paddingBottomOld + 'px'});
          }
        }
      }

      if (this.restoreAfterFinish){
        if( Prototype.operaMobile() && this.element.hasChildNodes()) {
          this.recursiveRestoreOriginalStyle(this.element.childNodes)
        } else {
          this.element.setStyle(this.originalStyle);
        }
      }

      if (Prototype.netFront()) {
        var netFrontBadBehaveElementsList = ['area','button','input', 'select','textarea'];
        var nodes;
        for(var i = 0;i<netFrontBadBehaveElementsList.length;i++){
          nodes = this.element.getElementsByTagName(netFrontBadBehaveElementsList[i]);
          for (var j=0;j<nodes.length;j++) {
            nodes[j].style.visibility = 'visible';
          }
        }
      }
    },

    recursiveSetSize: function(childNodes, currentScale){
      var i = 0;
      var child;
      for(i = 0;i<childNodes.length;i++){
        child = childNodes[i];
        //ignore text node
        if(child.nodeType == 3) {
          continue;
        }
        if(child.hasChildNodes()){
          this.recursiveSetSize(childNodes[i].childNodes,currentScale);
        }

        if(child._lineHeight && child._lineHeightUnit){
          child.setStyle({lineHeight: parseInt(child._lineHeight*currentScale)+child._lineHeightUnit});
        }
      }
      return;
    },

    recursiveRestoreOriginalStyle : function(childNodes){
      var i = 0;
      for(i = 0;i<childNodes.length;i++){
        if(childNodes[i].hasChildNodes()){
          this.recursiveRestoreOriginalStyle(childNodes[i].childNodes);
        }
        if(childNodes[i]['setStyle']!= undefined){
          childNodes[i].setStyle(childNodes[i]._originalStyle);
        }
        if(childNodes[i]._lineHeightValue != undefined){
          childNodes[i].setStyle({ 'line-height' : childNodes[i]._lineHeightValue});
        }
      }
    },

    vfcSetDimensions : function(height, width) {
      var d = {};
      height = parseInt(height);
      width = parseInt(width);

      var currentScale = height / this.dims[0];

      if(this.options.scaleX) {
        d.width = width + 'px';
        if(! (/^content/.test(this.options.scaleMode))) {
          if(this.bordersWidth > 0) {
            Element.setStyle(this.element, {borderLeftWidth: parseInt((this.bordersWidth / 2) * currentScale) + 'px'});
            Element.setStyle(this.element, {borderRightWidth: parseInt((this.bordersWidth / 2) * currentScale) + 'px'});
          }
          if(this.paddingsWidth > 0) {
            Element.setStyle(this.element, {paddingLeft: parseInt((this.paddingsWidth / 2) * currentScale) + 'px'});
            Element.setStyle(this.element, {paddingRight: parseInt((this.paddingsWidth / 2) * currentScale) + 'px'});
          }
        }
      }

      if(this.options.scaleY) {
        d.height = height + 'px';
        if(! (/^content/.test(this.options.scaleMode))) {
          if(this.bordersHeight > 0) {
            Element.setStyle(this.element, {borderTopWidth: parseInt((this.bordersHeight / 2) * currentScale) + 'px'});
            Element.setStyle(this.element, {borderBottomWidth: parseInt((this.bordersHeight / 2) * currentScale) + 'px'});
          }
          if(this.paddingsHeight > 0) {
            Element.setStyle(this.element, {paddingTop: parseInt((this.paddingsHeight / 2) * currentScale) + 'px'});
            Element.setStyle(this.element, {paddingBottom: parseInt((this.paddingsHeight / 2) * currentScale) + 'px'});
          }
        }
      }

      return d;
    }

  });

  Effect.Revert = function(element) {
    element = $(element);
    var options = Object.extend({
    from: (element.getStyle('display') == 'none' ? 0.0 : element.getOpacity() || 0.0),
    to: element.initialOpacity || 1.0, //element.getInlineOpacity() || 1.0,
    // force Safari to render floated elements properly
    afterFinishInternal: function(effect) {
      effect.element.forceRerendering();
    },
    beforeSetup: function(effect) {
      effect.element.setOpacity(effect.options.from);
      effect.element.show();
    }}, arguments[1] || {});
    return new Effect.Opacity(element,options);
  }


}


/* END: MOVED FROM  effects.patch */


/* Base class for load widget */

Widget.Load = Widget.define(
{
  src: null,
  when: null,
  owner: null,

  initialize: function(options) {
    this.initializeLoad(options)
  },

  initializeLoad: function(options) {
    this.initializeWidget(null, options)

    this.addAction("execute")

    this.addProperty("src")

    this.addEvent("succeeded")
    this.addEvent("failed")
  },

  setOwner: function(owner) {
    this.owner = owner
  },

  getSrc: function() {
    return this.src
  },

  setSrc: function(src) {
    if (this.src != src) {
      this.src = src

      this.notifyObservers("srcChanged")

      this.notifyObservers("canExecuteChanged")
    }
  },

  getWhen: function() {
    return this.when
  },

  execute: function() {
    if (this.canExecute()) {
      this.doExecute()
    }
  },

  doExecute: function() {
    throw "Method doExecute() is not implemented. Overwrite it in subclass.";
  },

  canExecute: function() {
    return this.src != null
  }
})

/* Base class for fetch widget */

Widget.Fetch = Widget.define(
{
  src: null,
  when: null,
  transformation: null,
  //localization of installed MCS
  pageBase: null,
  // path to fetch service with dafault value
  service: "/services/fetch",
  transformCompile: null,
  transformCache: null,

  owner: null,

  initializeFetch: function(options) {
    this.initializeWidget(null, options)

    this.addAction("execute")

    this.addProperty("src")
    this.addProperty("transformation")

    this.addEvent("succeeded")
    this.addEvent("failed")
  },

  setOwner: function(owner) {
    this.owner = owner;
  },

  isTransformCompiled: function() {
    return this.transformCompile
  },

  isTransformCached: function() {
    return this.transformCache
  },

  getTransformation: function() {
    return this.transformation
  },

  getSrc: function() {
    return this.src
  },

  setSrc: function(src) {
    if (this.src != src) {
      this.src = src
      this.notifyObservers("srcChanged")
      this.notifyObservers("canExecuteChanged")
    }
  },

  getWhen: function() {
    return this.when
  },

  execute: function() {
    if (this.canExecute()) {
      this.doExecute()
    }
  },

  doExecute: function() {
    throw "Method doExecute() is not implemented. Overwrite it in subclass.";
  },

  canExecute: function() {
    return this.src != null
  }
})

/* Base class for refresh widget */
Widget.Refresh = Widget.define(
{
  src: null,
  interval: null,

  // parent widget reference
  owner: null,

  initializeRefresh: function(options) {
    this.initializeWidget(null, options)

    this.addAction("execute")

    this.addProperty("src")
    this.addProperty("interval")
  },

  setOwner: function(owner) {
    this.owner = owner;
  },

  getInterval: function() {
    return this.interval
  },

  _setInterval: function(interval) {
    this.transformation = interval
    this.notifyObservers("intervalChanged")
  },

  getSrc: function() {
    return this.src
  },

  setSrc: function(src) {
    if (this.src != src) {
      this.src = src
      this.notifyObservers("srcChanged")
      this.notifyObservers("canExecuteChanged")
    }
  },

  execute: function() {
    if (this.canExecute()) {
      this.doExecute()
    }
  },

  doExecute: function() {
    throw "Method doExecute() is not implemented. Overwrite it in subclass.";
  },

  canExecute: function() {
    return this.src != null
  }
})

Widget.Internal.Client = Widget.define(
{
  url: null,
  parameters: null,
  json: false,

  initialize: function(options) {
    this.initializeWidget(null, options)
  },

  getURL: function() {
    return this.url
  },

  setURL: function(url) {
    Widget.log("Client", "Changing URL to: " + url)

    this.url = url

    this.notifyObservers("urlChanged")
    this.notifyObservers("canSendRequestChanged")
  },

  getParameters: function() {
    return this.parameters
  },

  setParameters: function(parameters) {
    Widget.log("Client", "Changing parameters to: " + parameters)

	this.parameters = parameters

    this.notifyObservers("parametersChanged")
  },

  expectsJSONResponse: function() {
    return this.json
  },

  setExpectsJSONResponse: function(expectsJSONResponse) {
    this.json = expectsJSONResponse
  },

  isBusy: function() {
    return this.pendingAJAXRequest != null
  },

  /**
   * Sends request using specified URL and parameters.
   * Before sending request, parameter values are appended to the query
   * component of the URL, using HTTP query syntax.
   *
   * This method returns immediately after request is sent.
   *
   * When response is received, a 'requestSucceeded' notification is sent,
   * with response content passed in the first argument.
   *
   * When request fails, a 'requestFailed' notification is sent,
   * with failure message string passed in the first argument,
   *
   * Together with 'requestSucceeded', one of two notifications is sent,
   * which may be used to handle user-errors in simplier way:
   *  - 'requestSucceededWithUserError'
   *  - 'requestSucceededWithoutUserError'
   *
   * Sending a request implicitely interrupts currently pending request.
   */
  sendRequest: function() {
    if (this.canSendRequest()) {
	  this.interruptRequest()

	  Widget.log("Client", "Sending AJAX request")

	  this.setPendingAJAXRequest(
	    new Ajax.Request(this.url, {
	              parameters: $H(this.parameters || {}).toQueryString(),
	              method: "get",
	              onSuccess: this.ajaxRequestSucceeded.bind(this),
	              onFailure: this.ajaxRequestFailed.bind(this),
	              onException: this.ajaxRequestThrownException.bind(this)}))
	}
  },

  canSendRequest: function() {
    return this.url != null
  },

  /**
   * Interrupts currently pending request, if any.
   */
  interruptRequest: function() {
    if (this.canInterruptRequest()) {
      Widget.log("Client", "Interrupting AJAX request")

      // Rather than interrupting AJAX request,
      // we just stop listening to its results.
      this.pendingAJAXRequest.options['onSuccess'] = null
      this.pendingAJAXRequest.options['onFailure'] = null
      this.pendingAJAXRequest.options['onException'] = null

      this.setPendingAJAXRequest(null)

      this.notifyObservers("requestInterrupted")
    }
  },

  canInterruptRequest: function() {
    return this.pendingAJAXRequest != null
  },

  /**
   * Invoked on AJAX request success.
   */
  ajaxRequestSucceeded: function(transport) {
    Widget.log("Client", "AJAX request succeeded")

    setTimeout(this.processAjaxRequestSuccess.bind(this, transport), 0)
  },

  processAjaxRequestSuccess: function(transport) {
    this.setPendingAJAXRequest(null)

    if (this.json) {
      // If the response is JSON string, simply evaluate it
      // and store on this.responseContent field.
      try {
        this.responseContent = eval(transport.responseText)

      } catch(e) {
        this.notifyObservers("requestFailed", "Error evaluating JSON. " + e)

        return
      }

    } else {
      // Otherwise, if response is a fragment of HTML containing
      // script parts, do the following:

      // Get response text out of the request.
      var html = transport.responseText

      // Insert response content into HTML, so that widgets included
      // within the scripts may access it by ID.
      this.getResponseArea().innerHTML = html.stripScripts()

      // Evaluate the JavaScript from the response content.
      // An instance of ServiceResponse object should be stored
      // on this.response field.
      this.responseContent = null

      try {
        var scripts = html.extractScripts()

        for (var i = 0; i < scripts.length; i++) {
          eval(scripts[i])
        }
      } catch(e) {
        this.notifyObservers("requestFailed", "Error evaluating response scripts. " + e)
        return
      }

      // After scripts are invoked, clear the response area,
      // since it's no more used.
      this.getResponseArea().innerHTML = ""
    }

    // Notify observers with success
    this.notifyObservers("requestSucceeded", this.responseContent)

    // Send alternative notification, with information whether
    // response contains Widget.Response.Error or other content.
    // This simplifies task of handling user-errors.
    if (this.responseContent != null && this.responseContent._widgetResponseErrorToken == true) {
      // Case 1: Response contains Widget.Response.Error.
      this.notifyObservers("requestSucceededWithError", this.responseContent)

    } else {
      // Case 2: Response does not contain Widget.Response.Error.
      this.notifyObservers("requestSucceededWithoutError", this.responseContent)
    }
  },

  /**
   * Invoked on AJAX request failure.
   */
  ajaxRequestFailed: function(transport) {
    Widget.log("Client", "AJAX request failed")

    setTimeout(this.processAjaxRequestFailure.bind(this, transport), 0)
  },

  processAjaxRequestFailure: function(transport) {
    this.setPendingAJAXRequest(null)

    this.notifyObservers("requestFailed", "AJAX Request failed. HTTP Status: " + transport.status)
  },

  /**
   * Invoked on AJAX request exception.
   */
  ajaxRequestThrownException: function(e) {
    Widget.log("Client", "AJAX request exception")

    setTimeout(this.processAjaxRequestException.bind(this, e), 0)
  },

  processAjaxRequestException: function(e) {
    this.setPendingAJAXRequest(null)

    this.notifyObservers("requestFailed", "AJAX Request thrown exception: " + e)
  },

  /*
   * Creates and returns new HTML element, which will act
   * as a placeholder for response content.
   */
  createResponseArea: function() {
    var responseArea = document.createElement("div")

    var responseAreaElement = Element.extend(responseArea)

    responseAreaElement.setStyle({display: 'none'})

    responseAreaElement.setStyle({visibility: 'hidden'})

    document.body.appendChild(responseArea)

    return responseAreaElement
  },

  /*
   * Returns a HTML element, which acts as a placeholder
   * for response content.
   */
  getResponseArea: function() {
    if (!this.responseArea) {
      this.responseArea = this.createResponseArea()
    }

    return this.responseArea
  },

  setPendingAJAXRequest: function(request) {
    this.pendingAJAXRequest = request

    this.notifyObservers("isBusyChanged")
    this.notifyObservers("canInterruptRequestChanged")
  }
})

/**
 * Response User Error.
 */
Widget.Response.Error = new Class.define(
{
  /**
   * Initializes this error with message.
   */
  initialize: function(message) {
    // Because there's no keyword similar to Java 'instanceof',
    // this special token is used to distinguish between
    // Error and other types of responses.
    this._widgetResponseErrorToken = true

    this.message = message
  },

  /**
   * Returns error message
   */
  getMessage: function() {
    return this.message
  }
})
