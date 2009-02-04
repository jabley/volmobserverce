

/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * GrowEffect strategy
 */
CustomEffects.GrowEffectStrategy = {

  affectedStyles : ['top','left','width','height','lineHeight','fontSize','position','overflow'],

  calculatedStyles : ['top','left','width','height','fontSize','lineHeight'],

  initInternalValues : function(element){
    // step from [0,1,2]
    switch (this.direction) {
      case 'top-left':
        this._internalValues.x = this._internalValues.y = 0;
        break;
      case 'top-right':
        this._internalValues.x = 1;
        this._internalValues.y = 0;
        break;
      case 'bottom-left':
        this._internalValues.x = 0;
        this._internalValues.y = 1;
        break;
      case 'bottom-right':
        this._internalValues.x = this._internalValues.y =  1;
        break;
      case 'center':
        this._internalValues.x = this._internalValues.y = 0.5;
       break;
    }
    this._internalValues._topStep = this._internalValues.y*2;
    this._internalValues._leftStep =this._internalValues.x*2;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value+this._internalValues.y*stylesStruct.height.value-stylesStruct.height.value/2*pos*this._internalValues._topStep+stylesStruct.top.unit;
    newStruct.left = stylesStruct.left.value+this._internalValues.x*stylesStruct.width.value-stylesStruct.width.value/2*pos*this._internalValues._leftStep+stylesStruct.left.unit;
    newStruct.width = stylesStruct.width.value * pos+stylesStruct.width.unit;
    newStruct.height = stylesStruct.height.value * pos+stylesStruct.height.unit;
    newStruct.lineHeight = stylesStruct.lineHeight.value * pos+stylesStruct.lineHeight.unit;
    newStruct.fontSize = stylesStruct.fontSize.value  * pos+stylesStruct.fontSize.unit;
    return newStruct;
  },

  modifiedStyles : {  position : ['relative','absolute','fixed'], overflow : 'hidden'}

};

CustomEffects.GrowEffect =  CustomEffects.createEffect(CustomEffects.GrowEffectStrategy, CustomEffects.EffectExecutor,{

  internalStart: function(element){
    this.hideInputElements();
  },

  internalFinish: function(element){
    this.showInputElements();
  }
});
