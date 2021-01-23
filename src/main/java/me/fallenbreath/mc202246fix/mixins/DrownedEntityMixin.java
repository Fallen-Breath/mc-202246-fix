package me.fallenbreath.mc202246fix.mixins;

import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity
{
	public DrownedEntityMixin(World world)
	{
		super(world);
	}

	private boolean shouldSyncNavigation()
	{
		return !this.world.isClient && this.world instanceof ServerWorld;
	}

	@Inject(method = "updateSwimming", at = @At("HEAD"))
	void removeCurrentNavigation(CallbackInfo ci)
	{
		if (this.shouldSyncNavigation())
		{
			((ServerWorldAccessor)this.world).getEntityNavigations().remove(this.getNavigation());
		}
	}

	@Inject(method = "updateSwimming", at = @At("RETURN"))
	void addedBackCurrentNavigation(CallbackInfo ci)
	{
		if (this.shouldSyncNavigation())
		{
			((ServerWorldAccessor)this.world).getEntityNavigations().add(this.getNavigation());
		}
	}
}
