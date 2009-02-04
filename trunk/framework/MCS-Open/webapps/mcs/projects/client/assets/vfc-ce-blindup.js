/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * BlidnUp strategy - only height is modified.
 */
CustomEffects.BlindUpEffectStrategy = {

  affectedStyles : ['height','overflow'],

  calculatedStyles : ['height'],

  initInternalValues : function(element){
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.height= stylesStruct.height.value*(1-pos)+''+stylesStruct.height.unit;
    return newStruct;
  },

  modifiedStyles : { overflow :'hidden'}
};


CustomEffects.BlindUpEffect = CustomEffects.createEffect(CustomEffects.BlindUpEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    this.hideInputElements();
  },

  internalFinish: function(element){
    element.hide();
    this.showInputElements();
  }
});
