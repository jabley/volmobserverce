
/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * Base strategy for all direction of Slide. Effect is configurable by _offset and direction
 * attributes which determines parameters used to calculation.
 */
CustomEffects.SlideEffectStrategy = {

  affectedStyles : ['top','left','position'],

  calculatedStyles : ['top','left'],

  /**
   * Specify if effect is appear or disappear
   * appear: _offset = 1; disappear: _offset = -1;
   */
  _offset : 1,

  initInternalValues : function(element){
    // step from [-1,1]
    this._internalValues._leftStep = 0;
    this._internalValues._topStep = 0;
    // direction is second parameter that determines transition calculations.
    switch (this.direction) {
      case 'left':
        this._internalValues._leftStep = 1;
        break;
      case 'right':
        this._internalValues._leftStep = -1;
        break;
      case 'bottom':
        this._internalValues._topStep = -1;
        break;
      case 'top':
    this._internalValues._topStep = 1;
        break;
    }

    var dims = Element.getDimensions(element);
    this._internalValues.height = dims.height;
    this._internalValues.width = dims.width;

    if(this._offset == -1){
      // reverse direction
      this._internalValues._offsetLeft = this._internalValues._offsetTop = 0;
    } else {
      this._internalValues._offsetLeft = -1*this._offset*this._internalValues.width*this._internalValues._leftStep;
      this._internalValues._offsetTop = -1*this._offset*this._internalValues.height*this._internalValues._topStep;
    }
    this._internalValues._topStep = this._internalValues._topStep * this._offset;
    this._internalValues._leftStep = this._internalValues._leftStep * this._offset;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value+this._internalValues._offsetTop+this._internalValues._topStep*this._internalValues.height*pos+stylesStruct.top.unit;
    newStruct.left = stylesStruct.left.value+this._internalValues._offsetLeft+this._internalValues._leftStep*this._internalValues.width*pos+stylesStruct.left.unit;
    return newStruct;
  },

  modifiedStyles : { position : ['relative','absolute','fixed']}

};

CustomEffects.SlideAppearEffect     = CustomEffects.createEffect(CustomEffects.SlideEffectStrategy, CustomEffects.EffectExecutor,
{
  _offset  : 1,

  _hidenDiv : null,

  internalStart: function(element){
    var parent = element.parentNode;

    if(Prototype.operaMobile()){
      // add transparent div at effect start to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      this._hidenDiv = document.createElement('div');
      this._hidenDiv = $(this._hidenDiv);
      Element.setStyle(this._hidenDiv, {height : '200%', width : '200%',
        backgroundColor : 'transparent',position : 'absolute', top : '0', left : '0'});
      parent.insertBefore(this._hidenDiv,element);
    }
    var newDiv = CustomEffects._createOuterDiv(element);
    parent.insertBefore(newDiv,element);
    newDiv.appendChild(element);
  },

  internalFinish: function(element){
    CustomEffects._removeOuterDiv(element);
    if(Prototype.operaMobile()){
      // remove transparent div at effect finish to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      var mainElement = element.parentNode;
      mainElement.removeChild(this._hidenDiv);
    }
  }
});CustomEffects.SlideDisppearEffect = CustomEffects.createEffect(CustomEffects.SlideEffectStrategy, CustomEffects.EffectExecutor,
{
  _offset  : -1,

  _hidenDiv : null,

  internalStart: function(element){
    var parent = element.parentNode;

    if(Prototype.operaMobile()){
      // add transparent div at effect start to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      this._hidenDiv = document.createElement('div');
      this._hidenDiv = $(this._hidenDiv);
      Element.setStyle(this._hidenDiv, {height : '200%', width : '200%',
         backgroundColor : 'transparent',position : 'absolute', top : '0', left : '0'});
       parent.insertBefore(this._hidenDiv,element);
    }else if(Prototype.operaPC()){
      // add transparent div at effect start to force
      // refresh  view after effect finish
      // to fix Opera PC refresh bug
      this._hidenDiv = document.createElement('div');
      this._hidenDiv = $(this._hidenDiv);
      Element.setStyle(this._hidenDiv, {height : '110%', width : '10%',
        backgroundColor : 'transparent',position : 'fixed', top : '0', left : '0'});
      parent.insertBefore(this._hidenDiv,element);
    }

    var newDiv = CustomEffects._createOuterDiv(element);
    newDiv = $(newDiv);
  // workaround for IE - if static position is set for outer DIV then overflow:hidden not working good in IE
  // so static is change to relative with top = 0px and left = 0px
  if(Prototype.msieBrowser() && Element.getStyle(element, 'position') == 'static') {
    Element.setStyle(newDiv, { position : 'relative', left: '0px', top: '0px'});
  } else {
        Element.setStyle(newDiv, { position : Element.getStyle(element, 'position')});
  }
    parent.insertBefore(newDiv,element);
    newDiv.appendChild(element);
    Element.setStyle(newDiv, {overflow : 'hidden'});
  },

  internalFinish: function(element){
    element.hide();
    CustomEffects._removeOuterDiv(element);
    if(Prototype.operaMobile() || Prototype.operaPC()){
      // remove transparent div at effect finish to force
      // refresh  view after effect finish
      // to fix Opera refresh bug
      var mainElement = element.parentNode;
      mainElement.removeChild(this._hidenDiv);
    }
  }
});


