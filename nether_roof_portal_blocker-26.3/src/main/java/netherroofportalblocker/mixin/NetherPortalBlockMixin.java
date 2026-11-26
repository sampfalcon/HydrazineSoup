package netherroofportalblocker.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetherPortalBlock.class)
public abstract class NetherPortalBlockMixin {
	private static final int MIN_ROOF_PORTAL_Y = 128;

	@Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
	private void nether_roof_portal_blocker$preventRoofPortalEntry(
			BlockState state,
			Level level,
			BlockPos pos,
			Entity entity,
			InsideBlockEffectApplier effectApplier,
			boolean isMoving,
			CallbackInfo callbackInfo
	) {
		if (!level.isClientSide() && level.dimension() == Level.NETHER && pos.getY() >= MIN_ROOF_PORTAL_Y) {
			callbackInfo.cancel();
		}
	}
}
