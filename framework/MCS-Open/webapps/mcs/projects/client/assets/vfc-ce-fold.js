/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * Fold effect strategy, it modifies only height and width parameters. Overflow must be
 * set to hidden at the beginning of effect.
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.FoldEffectStrategy = {
  affectedStyles : ['width', 'height', 'overflow'],

  calculatedStyles : ['width','height'],

  /* read only attributes */
  _minHeight : 0.05,

  _turnPoint : 0.5,

  transition : function(pos, stylesStruct){
    var newStruct = {};
    if(pos < this._turnPoint){
      newStruct.height = stylesStruct.height.value * (this._turnPoint-pos)/(this._turnPoint)+
      this._minHeight*pos/this._turnPoint+stylesStruct.height.unit;
    } else {
      newStruct.width = stylesStruct.width.value *(1-(pos-this._turnPoint)/(1-this._turnPoint))+stylesStruct.width.unit;
    }
    return newStruct;
  },
  modifiedStyles : { overflow : 'hidden'}
};

CustomEffects.FoldEffect = CustomEffects.createEffect(CustomEffects.FoldEffectStrategy,CustomEffects.EffectExecutor,
{

  internalStart: function(element){
    this.hideInputElements();
  },

  internalFinish: function(element){
    this.showInputElements();
    element.hide();
  }
});

