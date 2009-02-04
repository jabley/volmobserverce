/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * PuffEffect strategy
 */
CustomEffects.PuffEffectStrategy = {

  affectedStyles : ['top','left','width','height','lineHeight','fontSize','position','opacity'],

  calculatedStyles : ['top','left','fontSize','lineHeight','opacity'],

  initInternalValues : function(element){
    // only for puff we need different way of height/width calculation
    var dims = Element.getDimensions(element);
    this._internalValues.height = dims.height;
    this._internalValues.width = dims.width;
  },

  transition : function(pos, stylesStruct){
    var newStruct = {};
    newStruct.top = stylesStruct.top.value-pos*this._internalValues.height/2+"px";
    newStruct.left = stylesStruct.left.value-pos*this._internalValues.width/2+"px";
    newStruct.height = this._internalValues.height*(1+pos)+"px";
    newStruct.width= this._internalValues.width*(1+pos)+"px";
    newStruct.lineHeight = stylesStruct.lineHeight.value * (1+pos)+stylesStruct.lineHeight.unit;
    newStruct.fontSize = stylesStruct.fontSize.value  *(1+pos)+stylesStruct.fontSize.unit;
    newStruct.opacity = 1-pos;
    return newStruct;
  },

  modifiedStyles : { position : 'absolute', height : 'auto', width : 'auto'}

};

CustomEffects.PuffEffect = CustomEffects.createEffect(CustomEffects.PuffEffectStrategy, CustomEffects.EffectExecutor,
{
  internalStart: function(element){
    element.setOpacity(0.9999999);
  },

  internalFinish: function(element){
    element.hide();
  }
});
