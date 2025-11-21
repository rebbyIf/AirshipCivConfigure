package com.rebby.airship_civ_configure.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.valkyrienskies.clockwork.content.physicalities.wing.WingBlock;

@Mixin(WingBlock.class)
public class MixinWingBlock {
    @ModifyVariable(
            method = "getWing",
            at = @At("LOAD"),
            name = "wingDrag",
            remap = false
    )
    private double modifyDrag(double orig) {
        return 0.0;
    }
}
