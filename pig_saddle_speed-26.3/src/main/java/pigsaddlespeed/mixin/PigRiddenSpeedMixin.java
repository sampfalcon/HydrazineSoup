package pigsaddlespeed.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.animal.pig.Pig;
import pigsaddlespeed.PigSaddleSpeedMod.MountType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pigsaddlespeed.PigSaddleSpeedMod;

@Mixin(Pig.class)
public abstract class PigRiddenSpeedMixin {
	@Inject(method = "getRiddenSpeed", at = @At("RETURN"), cancellable = true)
	private void pig_saddle_speed$boostRiddenSpeed(
			Player player,
			CallbackInfoReturnable<Float> callbackInfo
	) {
		callbackInfo.setReturnValue(
				callbackInfo.getReturnValue()
						* (float) PigSaddleSpeedMod.getMovementSpeedMultiplier(MountType.PIG)
		);
	}
}
