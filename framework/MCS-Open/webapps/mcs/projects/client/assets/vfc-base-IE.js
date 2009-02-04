/**
 * (c) Volantis Systems Ltd 2008. 
 */

var Prototype = {
  msieBrowser: function() {
    return true;
  },

  operaMobile: function() {
    return false;
  },

  firefoxBrowser: function() {
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

  netFront: function() {
    return false;
  },

  netFrontMobile: function() {
    return false;
  },

  // needed as it appears in many places in vfc-... files
  hiddenAttr: function(attr) {
    return false;
  },

  useMouseAsSelect: function() {
    return false;
  },

  useEmulatedOpacity: function() {
    return false;
  },

  disableCache: function() {
    return false;
  },

  /**
  * Change input type. Be aware that element will lost event's observers
  * element - a reference to the input element
  * elType - value of the type property: 'text' or 'password'
  */
  changeInputType: function(element, elType) {
    if(!element || !element.parentNode || (elType.length<4)) 
      return;

    var newElm=document.createElement('span');
    newElm.innerHTML='<input type="'+elType+'" name="'+element.name+'">';
    newElm=newElm.firstChild;

    var props=['value','id','className','size','tabIndex','accessKey'];
    for(var i=0,l=props.length;i<l;i++){
      if(element[props[i]]) newElm[props[i]]=element[props[i]];
    }
    element.parentNode.replaceChild(newElm,element);
  }

}

Object.extend = function(destination, source) {
  for (var property in source) {
    destination[property] = source[property];
  }
  return destination;
}
