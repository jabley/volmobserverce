/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * BlindDown effect - only height is calculated
 */
CustomEffects.BlindDownEffectStrategy = {

  affectedStyles : ['height','overflow'],

  calculatedStyles : ['height'],

  initInternalValues : function(element){
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.height= stylesStruct.height.value*pos+stylesStruct.height.unit;
    return newStruct;
  },

  modifiedStyles : { overflow :'hidden'}
};

CustomEffects.BlindDownEffect =     CustomEffects.createEffect(CustomEffects.BlindDownEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.show();
    this.hideInputElements();
  },

  internalFinish: function(element){
    this.showInputElements();
  }
});
