/**
 * (c) Volantis Systems Ltd 2008. 
 */

Object.extend(Prototype, {
  Version: '1.5.0_rc0',
  ScriptFragment: '(?:<script.*?>)((\n|\r|.)*?)(?:<\/script>)',

  emptyFunction: function() {},
  K: function(x) {return x}
});

var Class = {
  create: function() {
    return function() {
      this.initialize.apply(this, arguments);
    }
  }
}

Object.inspect = function(object) {
  try {
    if (object == undefined) return 'undefined';
    if (object == null) return 'null';
    return object.inspect ? object.inspect() : object.toString();
  } catch (e) {
    if (e instanceof RangeError) return '...';
    throw e;
  }
}

var $A = Array.from = function(iterable) {
  if (!iterable) return [];
  if (iterable.toArray) {
    return iterable.toArray();
  } else {
    var results = [];
    for (var i = 0; i < iterable.length; i++)
      results.push(iterable[i]);
    return results;
  }
}

Function.prototype.bind = function() {
  var __method = this, args = $A(arguments), object = args.shift();
  return function() {
    return __method.apply(object, args.concat($A(arguments)));
  }
}

Function.prototype.bindAsEventListener = function(object) {
  var __method = this;
  return function(event) {
    return __method.call(object, event || window.event);
  }
}

var Try = {
  these: function() {
    var returnValue;

    for (var i = 0; i < arguments.length; i++) {
      var lambda = arguments[i];
      try {
        returnValue = lambda();
        break;
      } catch (e) {}
    }

    return returnValue;
  }
}

if (!window.Element)
  var Element = new Object();


Element.extend = function(element) {
  if (!element) return;
  if (_nativeExtensions) return element;

  if (!element._extended && element.tagName && element != window) {
    var methods = Element.Methods, cache = Element.extend.cache;
    for (property in methods) {
        var value = methods[property];
        if (typeof value == 'function')
          element[property] = cache.findOrStore(value);
    }
  }
  if (!Prototype.msieBrowser()) {
    element._extended = true;
  }
  return element;
}

Element.extend.cache = {
  findOrStore: function(value) {
    if (Prototype.disableCache()) {
      return function() {
        return value.apply(null, [this].concat($A(arguments)));
      }
    } else {
      return this[value] = this[value] || function() {
        return value.apply(null, [this].concat($A(arguments)));
      }
    }
  }
}


// start -  moved from vfc-base-enumerable
// TODO - seems to me that some of this method below are never used in our widget - to optimailze
var Enumerable = {
  each: function(iterator) {
    var index = 0;
    try {
      this._each(function(value) {
        try {
          iterator(value, index++);
        } catch (e) {
          if (e != $continue) throw e;
        }
      });
    } catch (e) {
      if (e != $break) throw e;
    }
  },

  all: function(iterator) {
    var result = true;
    this.each(function(value, index) {
      result = result && !!(iterator || Prototype.K)(value, index);
      if (!result) throw $break;
    });
    return result;
  },

  any: function(iterator) {
    var result = true;
    this.each(function(value, index) {
      if (result = !!(iterator || Prototype.K)(value, index))
        throw $break;
    });
    return result;
  },

  collect: function(iterator) {
    var results = [];
    this.each(function(value, index) {
      results.push(iterator(value, index));
    });
    return results;
  },

  detect: function (iterator) {
    var result;
    this.each(function(value, index) {
      if (iterator(value, index)) {
        result = value;
        throw $break;
      }
    });
    return result;
  },

  findAll: function(iterator) {
    var results = [];
    this.each(function(value, index) {
      if (iterator(value, index))
        results.push(value);
    });
    return results;
  },

  grep: function(pattern, iterator) {
    var results = [];
    this.each(function(value, index) {
      var stringValue = value.toString();
      if (stringValue.match(pattern))
        results.push((iterator || Prototype.K)(value, index));
    })
    return results;
  },

  include: function(object) {
    var found = false;
    this.each(function(value) {
      if (value == object) {
        found = true;
        throw $break;
      }
    });
    return found;
  },

  inject: function(memo, iterator) {
    this.each(function(value, index) {
      memo = iterator(memo, value, index);
    });
    return memo;
  },

  invoke: function(method) {
    var args = $A(arguments).slice(1);
    return this.collect(function(value) {
      return value[method].apply(value, args);
    });
  },

  max: function(iterator) {
    var result;
    this.each(function(value, index) {
      value = (iterator || Prototype.K)(value, index);
      if (result == undefined || value >= result)
        result = value;
    });
    return result;
  },

  min: function(iterator) {
    var result;
    this.each(function(value, index) {
      value = (iterator || Prototype.K)(value, index);
      if (result == undefined || value < result)
        result = value;
    });
    return result;
  },

  partition: function(iterator) {
    var trues = [], falses = [];
    this.each(function(value, index) {
      ((iterator || Prototype.K)(value, index) ?
        trues : falses).push(value);
    });
    return [trues, falses];
  },

  pluck: function(property) {
    var results = [];
    this.each(function(value, index) {
      results.push(value[property]);
    });
    return results;
  },

  reject: function(iterator) {
    var results = [];
    this.each(function(value, index) {
      if (!iterator(value, index))
        results.push(value);
    });
    return results;
  },

  sortBy: function(iterator) {
    return this.collect(function(value, index) {
      return {value: value, criteria: iterator(value, index)};
    }).sort(function(left, right) {
      var a = left.criteria, b = right.criteria;
      return a < b ? -1 : a > b ? 1 : 0;
    }).pluck('value');
  },

  toArray: function() {
    return this.collect(Prototype.K);
  },

  zip: function() {
    var iterator = Prototype.K, args = $A(arguments);
    if (typeof args.last() == 'function')
      iterator = args.pop();

    var collections = [this].concat(args).map($A);
    return this.map(function(value, index) {
      return iterator(collections.pluck(index));
    });
  },

  inspect: function() {
    return '#<Enumerable:' + this.toArray().inspect() + '>';
  }
}

Object.extend(Enumerable, {
  map:     Enumerable.collect,
  find:    Enumerable.detect,
  select:  Enumerable.findAll,
  member:  Enumerable.include,
  entries: Enumerable.toArray
});


Object.extend(Array.prototype, Enumerable);

if (!Array.prototype._reverse)
  Array.prototype._reverse = Array.prototype.reverse;

Object.extend(Array.prototype, {
  _each: function(iterator) {
    for (var i = 0; i < this.length; i++)
      iterator(this[i]);
  },

  clear: function() {
    this.length = 0;
    return this;
  },

  first: function() {
    return this[0];
  },

  last: function() {
    return this[this.length - 1];
  },

  compact: function() {
    return this.select(function(value) {
      return value != undefined || value != null;
    });
  },

  flatten: function() {
    return this.inject([], function(array, value) {
      return array.concat(value && value.constructor == Array ?
        value.flatten() : [value]);
    });
  },

  without: function() {
    var values = $A(arguments);
    return this.select(function(value) {
      return !values.include(value);
    });
  },

  indexOf: function(object) {
    for (var i = 0; i < this.length; i++)
      if (this[i] == object) return i;
    return -1;
  },

  reverse: function(inline) {
    return (inline !== false ? this : this.toArray())._reverse();
  },

  inspect: function() {
    return '[' + this.map(Object.inspect).join(', ') + ']';
  }
});

var Hash = {
  _each: function(iterator) {
    for (var key in this) {
      var value = this[key];
      if (typeof value == 'function') continue;

      var pair = [key, value];
      pair.key = key;
      pair.value = value;
      iterator(pair);
    }
  },

  keys: function() {
    return this.pluck('key');
  },

  values: function() {
    return this.pluck('value');
  },

  merge: function(hash) {
    return $H(hash).inject($H(this), function(mergedHash, pair) {
      mergedHash[pair.key] = pair.value;
      return mergedHash;
    });
  },

  toQueryString: function() {
    return this.map(function(pair) {
      return pair.map(encodeURIComponent).join('=');
    }).join('&');
  },

  inspect: function() {
    return '#<Hash:{' + this.map(function(pair) {
      return pair.map(Object.inspect).join(': ');
    }).join(', ') + '}>';
  }
}

function $H(object) {
  var hash = Object.extend({}, object || {});
  Object.extend(hash, Enumerable);
  Object.extend(hash, Hash);
  return hash;
}

// end - moved from vfc-base-enumerable


/*--------------------------------------------------------------------------*/
Object.extend(String.prototype, {
  truncate: function(length, truncation) {
    length = length || 30;
    truncation = truncation === undefined ? '...' : truncation;
    return this.length > length ?
      this.slice(0, length - truncation.length) + truncation : this;
  },

  strip: function() {
    return this.replace(/^\s+/, '').replace(/\s+$/, '');
  },

  stripTags: function() {
    return this.replace(/<\/?[^>]+>/gi, '');
  },

  stripScripts: function() {
    if (Prototype.nokiaOSSBrowser()) {
      return this.stripScriptsWithSubstring()
    } else {
      return this.replace(new RegExp(Prototype.ScriptFragment, 'img'), '');
    }
  },

  /*
   * Returns string without scripts.
   * It works like stripScripts, but it does NOT uses RegExp, which in some cases can crash NokiaOSSBrowser
   */
  stripScriptsWithSubstring: function() {
    var tmp = this
    var ret = ''
    var index
    while ((index = tmp.indexOf('<script')) != -1) {
      ret += tmp.substring(0, index)
      var index = tmp.indexOf('</script')
      tmp = tmp.substring(index+9)
    }
    ret += tmp
    return ret;
  },

  extractScripts: function() {
    if (Prototype.nokiaOSSBrowser()) {
      return this.extractScriptsWithSubstring()
    } else {
      var matchAll = new RegExp(Prototype.ScriptFragment, 'img');
      var matchOne = new RegExp(Prototype.ScriptFragment, 'im');
      return (this.match(matchAll) || []).map(function(scriptTag) {
        return (scriptTag.match(matchOne) || ['', ''])[1];
      });
    }
  },

  /*
   * Returns array of scripts.
   * It works like extractScripts, but it does NOT uses RegExp, which in some cases can crash NokiaOSSBrowser
   */
  extractScriptsWithSubstring: function() {
    var tmp = this
    var ret = new Array();
    var indexBeg
    var indexEnd
    while ((indexBeg = tmp.indexOf('<script')) != -1) {
      tmp = tmp.substring(indexBeg)
      indexBeg = tmp.indexOf('>');
      indexEnd = tmp.indexOf('</script')
      if (indexBeg != -1 && indexEnd != -1) {
        ret.push(tmp.substring(indexBeg+1, indexEnd))
      }
      tmp = tmp.substring(indexEnd+9)
    }
    return ret;
  },

  evalScripts: function() {
    return this.extractScripts().map(function(script) { return eval(script) });
  },

  escapeHTML: function() {
    var div = document.createElement('div');
    var text = document.createTextNode(this);
    div.appendChild(text);
    return div.innerHTML;
  },

  unescapeHTML: function() {
    var div = document.createElement('div');
    div.innerHTML = this.stripTags();
    var nodes = div.childNodes;
    var result = "";
    for(i=0; i<nodes.length; i++) {
      result = result + nodes[i].nodeValue;
    }
    return result;
  },

  toQueryParams: function() {
    var pairs = this.match(/^\??(.*)$/)[1].split('&');
    return pairs.inject({}, function(params, pairString) {
      var pair = pairString.split('=');
      params[pair[0]] = pair[1];
      return params;
    });
  },

  toArray: function() {
    return this.split('');
  },

  camelize: function() {
    var oStringList = this.split('-');
    if (oStringList.length == 1) return oStringList[0];

    var camelizedString = this.indexOf('-') == 0
      ? oStringList[0].charAt(0).toUpperCase() + oStringList[0].substring(1)
      : oStringList[0];

    for (var i = 1, len = oStringList.length; i < len; i++) {
      var s = oStringList[i];
      camelizedString += s.charAt(0).toUpperCase() + s.substring(1);
    }

    return camelizedString;
  },

  inspect: function() {
    return "'" + this.replace(/\\/g, '\\\\').replace(/'/g, '\\\'') + "'";
  }
});


String.prototype.parseQuery = String.prototype.toQueryParams;

var $break    = new Object();
var $continue = new Object();



function $() {
  var results = [], element;
  for (var i = 0; i < arguments.length; i++) {
    element = arguments[i];
    if (typeof element == 'string')
      element = document.getElementById(element);
    results.push(Element.extend(element));
  }
  return results.length < 2 ? results[0] : results;
}

/*--------------------------------------------------------------------------*/
Element.Methods = {
  visible: function(element) {
    return $(element).style.display != 'none';
  },

  toggle: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      Element[Element.visible(element) ? 'hide' : 'show'](element);
    }
  },

  hide: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      element.style.display = 'none';
    }
  },

  show: function() {
    for (var i = 0; i < arguments.length; i++) {
      var element = $(arguments[i]);
      if (element.vfcDefaultStyle == null) {
        // Original code goes here.
        element.style.display = ''
      } else {
        // Patched code goes here.
        element.style.display = element.vfcDefaultStyle
      }
    }
  },

  remove: function(element) {
    element = $(element);
    element.parentNode.removeChild(element);
  },

  update: function(element, html) {
    $(element).innerHTML = html.stripScripts();
    setTimeout(function() {html.evalScripts()}, 10);
  },

  replace: function(element, html) {
    element = $(element);
    if (element.outerHTML) {
      element.outerHTML = html.stripScripts();
    } else {
      var range = element.ownerDocument.createRange();
      range.selectNodeContents(element);
      element.parentNode.replaceChild(
        range.createContextualFragment(html.stripScripts()), element);
    }
    setTimeout(function() {html.evalScripts()}, 10);
  },

  getHeight: function(element) {
    element = $(element);
    return element.offsetHeight;
  },

  // removes whitespace-only text node children
  cleanWhitespace: function(element) {
    element = $(element);
    for (var i = 0; i < element.childNodes.length; i++) {
      var node = element.childNodes[i];
      if (node.nodeType == 3 && !/\S/.test(node.nodeValue))
        Element.remove(node);
    }
  },

  empty: function(element) {
    return $(element).innerHTML.match(/^\s*$/);
  },

  childOf: function(element, ancestor) {
    element = $(element), ancestor = $(ancestor);
    while (element = element.parentNode)
      if (element == ancestor) return true;
    return false;
  },

  scrollTo: function(element) {
    element = $(element);
    var x = element.x ? element.x : element.offsetLeft,
        y = element.y ? element.y : element.offsetTop;
    window.scrollTo(x, y);
  },

  setStyle: function(element, style) {
    element = $(element);
    for(var name in style) {
      if (!Prototype.hiddenAttr(name)) {
        element.style[name.camelize(name)]=style[name];
      }
    }
  },

  getStyle: function(element, style) {
    element = $(element);
    var value = element.style[style.camelize()];

    // when style is 'display' or 'visibility'  must be returned withot modify
    // otherwise when display==none then getStyle('visibility') returns 'hidden'
    if((style == 'visibility') || (style == 'display')){
      // code must be repeated
      if(!value){
        if (document.defaultView && document.defaultView.getComputedStyle) {
          var css = document.defaultView.getComputedStyle(element, null);
          value = css ? css.getPropertyValue(style) : null;
        } else if (element.currentStyle) {
          value = element.currentStyle[style.camelize()];
        }
      }
      return value == 'auto' ? null : value;
    }

    var isStyleChanged = false;
    if((Prototype.nokiaOSSBrowser() || Prototype.firefoxBrowser() || Prototype.konquerorBrowser() || Prototype.operaPC()) && element.style.display=='none') {
      element.style.visibility='hidden';
      element.style.display='block';
      
      // this workaround for opera PC older than 9.02 version.
      // The issue is if element change display:none to display:block
      // it is required some time of delay for getting property value which are computed 
      // after putting element into DOM (by set display:block)
      // The problem is only with computed values like height, width. It seems that these browsers
      // needs some time about 200 miliseconds to resize document's layout ade refresh its computed style object.
      // There is additional condition with time because getComputedStyle always return 0px 
      // in case of really value of property is 0px, before and after time for refresh (about 200ms) in that case it is 
      // not possible to recognize whether returned value is correct  
                  
      if(/Opera\/9.00|Opera\/9.01|Opera\/9.02/.test(navigator.userAgent)) {                        
        if (!value) {
          var now = new Date();
          var exitTime = now.getTime() + 300;                  
          if (document.defaultView && document.defaultView.getComputedStyle) {
            var css = document.defaultView.getComputedStyle(element, null);
            while(true) {
              value = css ? css.getPropertyValue(style) : null;
              now = new Date();
              if(value != '0px' || now.getTime() > exitTime) {                                   
                  break;
              }  
            }
          }
        }
      }              
      isStyleChanged = true;
    }

    if (!value) {
      if (document.defaultView && document.defaultView.getComputedStyle) {
        var css = document.defaultView.getComputedStyle(element, null);
        value = css ? css.getPropertyValue(style) : null;
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

    return value == 'auto' ? null : value;
  },

  setStyle: function(element, style) {
    element = $(element);
     for(var name in style) {
      if (!Prototype.hiddenAttr(name)) {
       element.style[name.camelize(name)]=style[name];
      }
     }
   }, 
  
  getDimensions: function(element) {
    element = $(element);
    var display = $(element).getStyle('display');
    if (display != 'none' && display != null) // Safari bug
      return {width: element.offsetWidth, height: element.offsetHeight};

    // All *Width and *Height properties give 0 on elements with display none,
    // so enable the element temporarily
    var els = element.style;
    var originalVisibility = els.visibility;
    var originalPosition = els.position;
    var originalDisplay = els.display;
    els.visibility = 'hidden';
    els.position = 'absolute';
    els.display = 'block';
    var originalWidth = element.clientWidth;
    var originalHeight = element.clientHeight;
    els.display = originalDisplay;
    els.position = originalPosition;
    els.visibility = originalVisibility;
    return {width: originalWidth, height: originalHeight};
  },

  makePositioned: function(element) {
    element = $(element);
    var pos = Element.getStyle(element, 'position');
    if (pos == 'static' || !pos) {
      element._madePositioned = true;
      element.style.position = 'relative';
      // Opera returns the offset relative to the positioning context, when an
      // element is position relative but top and left have not been defined
    }
  },

  undoPositioned: function(element) {
    element = $(element);
    if (element._madePositioned) {
      element._madePositioned = undefined;
      element.style.position =
        element.style.top =
        element.style.left =
        element.style.bottom =
        element.style.right = '';
    }
  },

  makeClipping: function(element) {
    element = $(element);
    if (element._overflow && !Prototype.msieBrowser()) return;
    if (Prototype.msieBrowser()) {
      element._overflow = 'visible';
    } else {
      element._overflow = element.style.overflow;
    }

    if ((Element.getStyle(element, 'overflow') || 'visible') != 'hidden') {
      Element.setStyle(element, {overflow:'hidden'})
    }
  },

  undoClipping: function(element) {
    element = $(element);
    if (element._overflow && !Prototype.msieBrowser()) {
      return;
    } 
    if(Prototype.msieBrowser()) {      
      element.style.overflow = 'visible';
    } else {
      element.style.overflow = '';
    }      
    element._overflow = undefined;
  }
}

Object.extend(Element, Element.Methods);

var _nativeExtensions = false;

if(!HTMLElement && /Konqueror|Safari|KHTML/.test(navigator.userAgent)) {
  var HTMLElement = {}
  HTMLElement.prototype = document.createElement('div').__proto__;
}

Element.addMethods = function(methods) {
  Object.extend(Element.Methods, methods || {});

  if(typeof HTMLElement != 'undefined') {
    var methods = Element.Methods, cache = Element.extend.cache;
    for (property in methods) {
      var value = methods[property];
      if (typeof value == 'function')
        HTMLElement.prototype[property] = cache.findOrStore(value);
    }
    _nativeExtensions = true;
  }
}

Element.addMethods();

var Toggle = new Object();
Toggle.display = Element.toggle;

/*--------------------------------------------------------------------------*/

if (!window.Event) {
  var Event = new Object();
}

Object.extend(Event, {
  KEY_BACKSPACE: 8,
  KEY_TAB:       9,
  KEY_RETURN:   13,
  KEY_ESC:      27,
  KEY_LEFT:     37,
  KEY_UP:       38,
  KEY_RIGHT:    39,
  KEY_DOWN:     40,
  KEY_DELETE:   46,

  element: function(event) {
    return event.target || event.srcElement;
  },

  isLeftClick: function(event) {
    return (((event.which) && (event.which == 1)) ||
            ((event.button) && (event.button == 1)));
  },

  pointerX: function(event) {
    return event.pageX || (event.clientX +
      (document.documentElement.scrollLeft || document.body.scrollLeft));
  },

  pointerY: function(event) {
    return event.pageY || (event.clientY +
      (document.documentElement.scrollTop || document.body.scrollTop));
  },

  stop: function(event) {
    if (event.preventDefault) {
      event.preventDefault();
      event.stopPropagation();
    } else {
      event.returnValue = false;
      event.cancelBubble = true;
    }
  },

  // find the first node with the given tagName, starting from the
  // node the event was triggered on; traverses the DOM upwards
  findElement: function(event, tagName) {
    var element = Event.element(event);
    while (element.parentNode && (!element.tagName ||
        (element.tagName.toUpperCase() != tagName.toUpperCase())))
      element = element.parentNode;
    return element;
  },

  observers: false,

  _observeAndCache: function(element, name, observer, useCapture) {
    if (!this.observers) this.observers = [];
    if (element.addEventListener) {
      //this was causing memory leaks under browsers other than MSIE, 
      //take a look at jira issue VMS-320
      //this.observers.push([element, name, observer, useCapture]);
      element.addEventListener(name, observer, useCapture);
    } else if (element.attachEvent) {
      this.observers.push([element, name, observer, useCapture]);
      element.attachEvent('on' + name, observer);
    }
  },

  unloadCache: function() {
    if (!Event.observers) return;
    for (var i = 0; i < Event.observers.length; i++) {
      Event.stopObserving.apply(this, Event.observers[i]);
      Event.observers[i][0] = null;
    }
    Event.observers = false;
  },

  observe: function(element, name, observer, useCapture) {
    var element = $(element);
    useCapture = useCapture || false;

    if (name == 'keypress' &&
        (navigator.appVersion.match(/Konqueror|Safari|KHTML/)
        || element.attachEvent))
      name = 'keydown';

    this._observeAndCache(element, name, observer, useCapture);
  },

  stopObserving: function(element, name, observer, useCapture) {
    var element = $(element);
    useCapture = useCapture || false;

    if (name == 'keypress' &&
        (navigator.appVersion.match(/Konqueror|Safari|KHTML/)
        || element.detachEvent))
      name = 'keydown';

    if (element.removeEventListener) {
      element.removeEventListener(name, observer, useCapture);
    } else if (element.detachEvent) {
      element.detachEvent('on' + name, observer);
    }
  }
});

/* prevent memory leaks in IE */
if (navigator.appVersion.match(/\bMSIE\b/))
  Event.observe(window, 'unload', Event.unloadCache, false);

var Position = {
  // set to true if needed, warning: firefox performance problems
  // NOT neeeded for page scrolling, only if draggable contained in
  // scrollable elements
  includeScrollOffsets: false,

  // must be called before calling withinIncludingScrolloffset, every time the
  // page is scrolled
  prepare: function() {
    this.deltaX =  window.pageXOffset
                || document.documentElement.scrollLeft
                || document.body.scrollLeft
                || 0;
    this.deltaY =  window.pageYOffset
                || document.documentElement.scrollTop
                || document.body.scrollTop
                || 0;
  },

  realOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.scrollTop  || 0;
      valueL += element.scrollLeft || 0;
      element = element.parentNode;
    } while (element);
    return [valueL, valueT];
  },

  cumulativeOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;
      element = element.offsetParent;
    } while (element);
    return [valueL, valueT];
  },

  positionedOffset: function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;
      element = element.offsetParent;
      if (element) {
        p = Element.getStyle(element, 'position');
        if (p == 'relative' || p == 'absolute') break;
      }
    } while (element);
    return [valueL, valueT];
  },

  offsetParent: function(element) {
    if (element.offsetParent) return element.offsetParent;
    if (element == document.body) return element;

    while ((element = element.parentNode) && element != document.body)
      if (Element.getStyle(element, 'position') != 'static')
        return element;

    return document.body;
  },

  // caches x/y coordinate pair to use with overlap
  within: function(element, x, y) {
    if (this.includeScrollOffsets)
      return this.withinIncludingScrolloffsets(element, x, y);
    this.xcomp = x;
    this.ycomp = y;
    this.offset = this.cumulativeOffset(element);

    return (y >= this.offset[1] &&
            y <  this.offset[1] + element.offsetHeight &&
            x >= this.offset[0] &&
            x <  this.offset[0] + element.offsetWidth);
  },

  withinIncludingScrolloffsets: function(element, x, y) {
    var offsetcache = this.realOffset(element);

    this.xcomp = x + offsetcache[0] - this.deltaX;
    this.ycomp = y + offsetcache[1] - this.deltaY;
    this.offset = this.cumulativeOffset(element);

    return (this.ycomp >= this.offset[1] &&
            this.ycomp <  this.offset[1] + element.offsetHeight &&
            this.xcomp >= this.offset[0] &&
            this.xcomp <  this.offset[0] + element.offsetWidth);
  },

  // within must be called directly before
  overlap: function(mode, element) {
    if (!mode) return 0;
    if (mode == 'vertical')
      return ((this.offset[1] + element.offsetHeight) - this.ycomp) /
        element.offsetHeight;
    if (mode == 'horizontal')
      return ((this.offset[0] + element.offsetWidth) - this.xcomp) /
        element.offsetWidth;
  },

  page: function(forElement) {
    var valueT = 0, valueL = 0;

    var element = forElement;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;

      // Safari fix
      if (element.offsetParent==document.body)
        if (Element.getStyle(element,'position')=='absolute') break;

    } while (element = element.offsetParent);

    element = forElement;
    do {
      valueT -= element.scrollTop  || 0;
      valueL -= element.scrollLeft || 0;
    } while (element = element.parentNode);

    return [valueL, valueT];
  },

  clone: function(source, target) {
    var options = Object.extend({
      setLeft:    true,
      setTop:     true,
      setWidth:   true,
      setHeight:  true,
      offsetTop:  0,
      offsetLeft: 0
    }, arguments[2] || {})

    // find page position of source
    source = $(source);
    var p = Position.page(source);

    // find coordinate system to use
    target = $(target);
    var delta = [0, 0];
    var parent = null;
    // delta [0,0] will do fine with position: fixed elements,
    // position:absolute needs offsetParent deltas
    if (Element.getStyle(target,'position') == 'absolute') {
      parent = Position.offsetParent(target);
      delta = Position.page(parent);
    }

    // correct by body offsets (fixes Safari)
    if (parent == document.body) {
      delta[0] -= document.body.offsetLeft;
      delta[1] -= document.body.offsetTop;
    }

    // set position
    if(options.setLeft)   target.style.left  = (p[0] - delta[0] + options.offsetLeft) + 'px';
    if(options.setTop)    target.style.top   = (p[1] - delta[1] + options.offsetTop) + 'px';
    if(options.setWidth)  target.style.width = source.offsetWidth + 'px';
    if(options.setHeight) target.style.height = source.offsetHeight + 'px';
  },

  absolutize: function(element) {
    element = $(element);
    if (element.style.position == 'absolute') return;
    Position.prepare();

    var offsets = Position.positionedOffset(element);
    var top     = offsets[1];
    var left    = offsets[0];
    var width   = element.clientWidth;
    var height  = element.clientHeight;

    element._originalLeft   = left - parseFloat(element.style.left  || 0);
    element._originalTop    = top  - parseFloat(element.style.top || 0);
    element._originalWidth  = element.style.width;
    element._originalHeight = element.style.height;

    element.style.position = 'absolute';
    element.style.top    = top + 'px';;
    element.style.left   = left + 'px';;
    element.style.width  = width + 'px';;
    element.style.height = height + 'px';;
  },

  relativize: function(element) {
    element = $(element);
    if (element.style.position == 'relative') return;
    Position.prepare();

    element.style.position = 'relative';
    var top  = parseFloat(element.style.top  || 0) - (element._originalTop || 0);
    var left = parseFloat(element.style.left || 0) - (element._originalLeft || 0);

    element.style.top    = top + 'px';
    element.style.left   = left + 'px';
    element.style.height = element._originalHeight;
    element.style.width  = element._originalWidth;
  }
}

// Safari returns margins on body which is incorrect if the child is absolutely
// positioned.  For performance reasons, redefine Position.cumulativeOffset for
// KHTML/WebKit only.
if (/Konqueror|Safari|KHTML/.test(navigator.userAgent)) {
  Position.cumulativeOffset = function(element) {
    var valueT = 0, valueL = 0;
    do {
      valueT += element.offsetTop  || 0;
      valueL += element.offsetLeft || 0;
      if (element.offsetParent == document.body)
        if (Element.getStyle(element, 'position') == 'absolute') break;

      element = element.offsetParent;
    } while (element);

    return [valueL, valueT];
  }
}
/********************************************************/
// above we have mininalized code from prototype.js
//belove code moved from vfc-base.js
/********************************************************/

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
   if (!Prototype.hiddenAttr(property)) {
    if (destination[property] !== undefined
        && typeof(destination[property]) != 'function') {
        destination[property] = source[property];
    }
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
      if (!Prototype.hiddenAttr(name)) {
        cname = name.camelize();
        element.vfcReplacedStyle[name] = element.style[cname];
        element.style[cname] = style[name];
      }
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
        if (!Prototype.hiddenAttr(name)) {
          cname = name.camelize();
          element.style[cname] = style[name];
        }
      }
      element.replacedStyle = null;
    }
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
  }

});

/***** WIDGET base ***/

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
    var new_event = event;
    if(Prototype.useMouseAsSelect()){
      if(event == Widget.BLUR){
        new_event = Widget.MOUSEOUT;
      } else if(event == Widget.FOCUS){
        new_event = Widget.MOUSEOVER;
      }
      // only NetFront3.4 with mouse cursor has bug. onChange occurs only if user "click" outsite edited element
      // not when user left edited controls. 
      if(event == Widget.CHANGE && Prototype.netFrontMobile()){
        new_event = Widget.MOUSEOUT;
      }
    } else {
      // change events for OperaMobile only for <input type="test" ...
      if(Prototype.operaMobile() && element.nodeName.toLowerCase()=='input' && element.type.toLowerCase()=='text') {
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
  },

  /*
   * @param element DOM element
   * @param eventName event name, should be used only "focus" or "blur"
   * @param funct function binded as event listener
   */
  observeFocusableElement: function(element, event, func) {
    if (Prototype.netFront() || Prototype.netFrontMobile()) {
      //on NetFront there is a problem with propagation of event listeners
      //according to: http://www.w3.org/TR/1998/REC-html40-19980424/interact/forms.html#h-17.11
      //following elements are focusable: A, AREA, BUTTON, INPUT, OBJECT, SELECT, and TEXTAREA.
      this.addObserversToFocusableElements(element,event,func);
    } else {
      Widget.addElementObserver(element, event, func);
    }
  },

  removeElementObserver: function(element,event,func){
    var new_event = event;
    if(Prototype.useMouseAsSelect()){
      if(event == Widget.BLUR){
        new_event = Widget.MOUSEOUT;
      } else if(event == Widget.FOCUS){
        new_event = Widget.MOUSEOVER;
      }
    } else {
      if(Prototype.operaMobile() && element.nodeName.toLowerCase() == 'input' && element.type.toLowerCase() == 'text') {
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
    if (Prototype.netFront() || Prototype.netFrontMobile()) {
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
    if(Prototype.netFront() || Prototype.netFrontMobile()) {
        // On NetFront browser, a new 1px width, focusable element is inserted,
        // which will catch the focus.
        imp = document.createElement("input");
        imp.type="button";
        imp.style.width='3px';
        imp.style.height='3px';
        imp.style.fontSize='1px';
        imp.style.backgroundColor='transparent';
        imp.style.border='0px';
        imp.style.padding='0px';
        imp.style.margin='0px';
        el.appendChild(imp);
    } else if (Prototype.firefoxBrowser() ||  Prototype.msieBrowser()) {
        // On Mozilla and IE, the tabIndex property of the element has special meaning.
        // For more info see: http://developer.mozilla.org/en/docs/Key-navigable_custom_DHTML_widgets
        el.tabIndex = "0"
        imp = el;
    }
    if(Prototype.operaMobile() || Prototype.operaPC()) {
      //bind Click handler only in order to make element focusable. DIV and SPAN is focusable on Opera mobile if has any handler bind to click event
      Widget.addElementObserver(el, Widget.CLICK, this.doNothing.bindAsEventListener(this));
      imp = el;
    }
    return imp;
  },

  doNothing:function() {
    // do nothing, only needed to create div focusable in Opera browsers
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
      if (!Prototype.hiddenAttr(name)) {
        if (name != "__object") {
          var value = this[name]
  
          if (typeof(value) == "string" || typeof(value) == "boolean" || typeof(value) == "number") {
            s += name + "=" + value + ", "
          }
        }
      }
    }

    return s
  }
}

// defined in transitions.js but sometimes widgets highlight needn't transitions but need  Widget.TransitionFactory
// as define there his highlight effect
Widget.TransitionFactory = {};

//moved from scriptaculous
var Effect = {};

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
      } else if(Prototype.netFront() || Prototype.netFrontMobile()){
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
    Element.setStyle(fakeFocusDIV,{ 'white-space': 'nowrap', 'width': '3px',
      'height': '3px', 'background-color': 'transparent', 'line-height': '3px',
      'margin' : '0px', 'padding': '0px', 'border': '0px'});
    element.appendChild(fakeFocusDIV);

    var fakeFocusElement = document.createElement("input");
    Element.setStyle(fakeFocusElement,{
      'width': '3px',
      'height': '3px',
      'overflow' : 'hidden',
      'background-color': 'transparent',
      'border': '0px',
      'font-size': '1px',
      'line-height': '1px',
      'margin' : '0px', 
      'padding': '0px'    
    });
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



