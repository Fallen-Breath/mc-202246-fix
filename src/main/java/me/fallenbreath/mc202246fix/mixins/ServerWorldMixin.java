package me.fallenbreath.mc202246fix.mixins;

import me.fallenbreath.mc202246fix.IDrownedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.DrownedEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin
{
	@Shadow @Final private Set<EntityNavigation> entityNavigations;

	/**
	 * Remove all possible navigations of drowned, stop memory leak
	 * Drowned might changed its navigation field sometimes, but thankfully its possible navigations are final fields
	 */
	@Inject(method = "unloadEntity", at = @At("TAIL"))
	private void removeAllPossibleDrownedNavigations(Entity entity, CallbackInfo ci)
	{
		if (entity instanceof DrownedEntity)
		{
			this.entityNavigations.remove(((IDrownedEntity)entity).getInitialEntityNavigation());
			this.entityNavigations.remove(((DrownedEntityAccessor)entity).getLandNavigation());
			this.entityNavigations.remove(((DrownedEntityAccessor)entity).getWaterNavigation());
		}
	}
}
