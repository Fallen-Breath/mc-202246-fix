package me.fallenbreath.mc202246fix.mixins;

import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor
{
	@Accessor
	Set<EntityNavigation> getEntityNavigations();
}
