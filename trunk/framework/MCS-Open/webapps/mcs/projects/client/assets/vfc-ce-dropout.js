/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * DropOUt effect strategy
 * @param {Object} pos
 * @param {Object} stylesStruct
 */
CustomEffects.DropOutEffectStrategy = {

  affectedStyles : ['top','position', 'opacity'],

  calculatedStyles : ['height'],

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.height.value*pos+stylesStruct.height.unit;
    newStruct.opacity = 1-pos;
    return newStruct;
  },

  modifiedStyles : { position : ['relative','absolute','fixed']}

};

CustomEffects.DropOutEffect = CustomEffects.createEffect(CustomEffects.DropOutEffectStrategy,CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  internalFinish: function(element){
    element.hide();
  }
});
