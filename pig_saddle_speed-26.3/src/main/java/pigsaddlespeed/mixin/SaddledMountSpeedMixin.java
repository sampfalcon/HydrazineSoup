package pigsaddlespeed.mixin;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.equine.Mule;
import net.minecraft.world.entity.animal.happyghast.HappyGhast;
import pigsaddlespeed.PigSaddleSpeedMod.MountType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pigsaddlespeed.PigSaddleSpeedMod;

@Mixin(Mob.class)
public abstract class SaddledMountSpeedMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	private void pig_saddle_speed$updateSaddleSpeed(CallbackInfo callbackInfo) {
		Mob mob = (Mob) (Object) this;
		if (mob.level().isClientSide()) {
			return;
		}

		MountType mountType = getMountType(mob);
		if (mountType == null) {
			return;
		}

		Identifier modifierId = modifierId(mountType);
		Holder<Attribute> movementSpeedAttribute = Attributes.MOVEMENT_SPEED;
		AttributeInstance movementSpeed = mob.getAttribute(movementSpeedAttribute);
		if (movementSpeed == null) {
			throw new IllegalStateException("Saddled mount is missing its movement speed attribute");
		}

		if (mob.isSaddled() && !movementSpeed.hasModifier(modifierId)) {
			double multiplier = PigSaddleSpeedMod.getMovementSpeedMultiplier(mountType);
			movementSpeed.addTransientModifier(new AttributeModifier(
					modifierId,
					multiplier - 1.0,
					AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
			));
		} else if (!mob.isSaddled()) {
			movementSpeed.removeModifier(modifierId);
		}
	}

	private static MountType getMountType(Mob mob) {
		if (mob instanceof Horse) {
			return MountType.HORSE;
		} else if (mob instanceof Donkey) {
			return MountType.DONKEY;
		} else if (mob instanceof Camel) {
			return MountType.CAMEL;
		} else if (mob instanceof HappyGhast) {
			return MountType.HAPPY_GHAST;
		} else if (mob instanceof Mule) {
			return MountType.MULE;
		}
		return null;
	}

	private static Identifier modifierId(MountType mountType) {
		return Identifier.fromNamespaceAndPath(
				PigSaddleSpeedMod.MOD_ID,
				"saddle_speed_" + mountType.getConfigName()
		);
	}
}
