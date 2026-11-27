package goldtoolsbuff.modid.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(CraftingMenu.class)
public abstract class GoldToolDurabilityMixin {
	@ModifyArg(
			method = "slotChangedCraftingGrid",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"
			),
			index = 1
	)
	private static ItemStack gold_tools_buff$setCraftedToolDurability(ItemStack result) {
		if (result.is(Items.GOLDEN_SWORD)
				|| result.is(Items.GOLDEN_PICKAXE)
				|| result.is(Items.GOLDEN_AXE)
				|| result.is(Items.GOLDEN_SHOVEL)
				|| result.is(Items.GOLDEN_HOE)) {
			result.set(DataComponents.MAX_DAMAGE, 300);
		}
		return result;
	}
}
