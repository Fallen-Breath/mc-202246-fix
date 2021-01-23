package me.fallenbreath.mc202246fix.mixins;

import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(DrownedEntity.class)
public abstract class DrownedEntityMixin extends ZombieEntity
{
	public DrownedEntityMixin(World world)
	{
		super(world);
	}

	private EntityNavigation previousNavigation;

	private boolean shouldSyncNavigation()
	{
		return !this.world.isClient && this.world instanceof ServerWorld;
	}

	@Inject(method = "updateSwimming", at = @At("HEAD"))
	void recordPreviousNavigation(CallbackInfo ci)
	{
		if (this.shouldSyncNavigation())
		{
			this.previousNavigation = this.getNavigation();
		}
	}

	@Inject(method = "updateSwimming", at = @At("RETURN"))
	void synchronizeNavigation(CallbackInfo ci)
	{
		if (this.shouldSyncNavigation())
		{
			EntityNavigation currentNavigation = this.getNavigation();
			if (currentNavigation != this.previousNavigation)
			{
				Set<EntityNavigation> navigationSet = ((ServerWorldAccessor) this.world).getEntityNavigations();
				navigationSet.remove(this.previousNavigation);
				navigationSet.add(currentNavigation);
			}
		}
	}
}
