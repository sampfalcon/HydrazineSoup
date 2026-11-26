package goldtoolsbuff.modid.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CraftingMenu.class, InventoryMenu.class})
public abstract class GoldToolDurabilityMixin {
	@Inject(method = "slotsChanged", at = @At("TAIL"))
	private void gold_tools_buff$setCraftedToolDurability(CallbackInfo info) {
		ItemStack result = ((AbstractContainerMenu) (Object) this).getSlot(0).getItem();

		if (result.is(Items.GOLDEN_SWORD)
				|| result.is(Items.GOLDEN_PICKAXE)
				|| result.is(Items.GOLDEN_AXE)
				|| result.is(Items.GOLDEN_SHOVEL)
				|| result.is(Items.GOLDEN_HOE)) {
			result.set(DataComponents.MAX_DAMAGE, 300);
		}
	}
}
