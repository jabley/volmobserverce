

/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * Shrink effect strategy.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.ShrinkEffectStrategy = {
  affectedStyles : ['top','left','width','height','lineHeight','fontSize','position', 'overflow'],

  calculatedStyles : ['top','left','width','height','fontSize','lineHeight'],

  initInternalValues : function(element){
    // _internalValues.x and _internalValues.y from [0,0.5,1]
    // direction determines starting point of transition
    // and step for each direction
    // step = 0 means that no transition is given direction
    // step = 0.5 menas that transition from border to center or from center to border
    // step = 1 transition from border to opposite border
    switch (this.direction) {
      case 'top-left':
        this._internalValues.x = this._internalValues.y = 0;
        break;
      case 'top-right':
        this._internalValues.x = 0;
        this._internalValues.y = 1;
        break;
      case 'bottom-left':
        this._internalValues.x = 1;
        this._internalValues.y = 0;
        break;
      case 'bottom-right':
        this._internalValues.x = this._internalValues.y =  1;
        break;
      case 'center':
        this._internalValues.x = this._internalValues.y = 0.5;
        break;
    }

    this._internalValues._topStep = this._internalValues.x*2;
    this._internalValues._leftStep =this._internalValues.y*2;
    //this._internalValues._startTop = this._internalValues.x*100;
    //this._internalValues._startLeft = this._internalValues.y*100;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value+stylesStruct.height.value/2*pos*this._internalValues._topStep+stylesStruct.top.unit;
    newStruct.left = stylesStruct.left.value+stylesStruct.width.value/2*pos*this._internalValues._leftStep+stylesStruct.left.unit;
    newStruct.width = stylesStruct.width.value * (1-pos)+stylesStruct.width.unit;
    newStruct.height = stylesStruct.height.value * (1-pos)+stylesStruct.height.unit;

    newStruct.fontSize = stylesStruct.fontSize.value  * (1-pos)+stylesStruct.fontSize.unit;
    newStruct.lineHeight = stylesStruct.lineHeight.value  * (1-pos)+stylesStruct.lineHeight.unit;

    return newStruct;
  },

  modifiedStyles : { position : ['relative','absolute','fixed'], overflow : 'hidden'}

};

CustomEffects.ShrinkEffect = CustomEffects.createEffect(CustomEffects.ShrinkEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    this.hideInputElements();
  },

  internalFinish: function(element){
    this.showInputElements();
    element.hide();
  }
});
