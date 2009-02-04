/**
 * (c) Volantis Systems Ltd 2008. 
 */

var Prototype = {
  netFrontMobile: function() {
/*    if(/NetFront\/3.4/.test(navigator.userAgent)) {
      return true
    }*/
    return true;
  },

  operaMobile: function() {
    return false;
  },

  firefoxBrowser: function() {
    return false;
  },
  
  msieBrowser: function() {
    return false;
  },

  nokiaOSSBrowser: function() {
    return false;
  },

  konquerorBrowser: function() {
    return false;
  },

  operaPC: function() {
    return false;
  },
  
  // for NetFront Windows Emulator
  netFront: function() {
    return false;
  },

  // specific
  hiddenAttr: function(attr) {
    if (attr == "constructor") return true;
    if (attr == "toString") return true;
    if (attr == "toLocaleString") return true;
    if (attr == "valueOf") return true;
    if (attr == "hasOwnProperty") return true;
    if (attr == "isPrototypeOf") return true;
    if (attr == "propertyIsEnumerable") return true;
    if (attr == "__defineGetter__") return true;
    if (attr == "__defineSetter__") return true;
    return false;
  },

  // Temporary method for NetFront3.4 devices
  // Returns true if UserAgent match to list of devices which has mouse cursor
  // TODO - should be rendered depending on device capabilities
  mouseCursorSupported: function() {
    return  /SonyEricssonZ770i/.test(navigator.userAgent) ||
            /SonyEricssonW890i/.test(navigator.userAgent) ||
            /SonyEricssonK660i/.test(navigator.userAgent) ||
            /SonyEricssonTM506/.test(navigator.userAgent);

  },

  // This browser sometimes has mouse cursor - 
  // TODO - should be rendered depending on device capabilities
  // Events like: 
  // onBlur should be changed to onMouseOut
  // onFocus should be changed to onMouseOver
  useMouseAsSelect: function() {
    return this.mouseCursorSupported();
  },

  useEmulatedOpacity: function() {
    return true;
  },

  disableCache: function() {
    return true;
  },

  /**
  * Change input type. 
  * element - a reference to the input element
  * elType - value of the type property: 'text', 'password', 'button', etc.
  */
  changeInputType: function(element, elType) {
    element.type=elType;
  }

}

Object.extend = function(destination, source) {
  for (var property in source) {
    if (!Prototype.hiddenAttr(property)) {
      destination[property] = source[property];
    }
  }
  return destination;
}

