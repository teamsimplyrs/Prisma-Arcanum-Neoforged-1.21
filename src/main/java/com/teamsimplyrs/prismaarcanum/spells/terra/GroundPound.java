package com.teamsimplyrs.prismaarcanum.spells.terra;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.teamsimplyrs.prismaarcanum.api.states.EntitySpellControlState;
import com.teamsimplyrs.prismaarcanum.api.states.EntitySpellControlStateComponent;
import com.teamsimplyrs.prismaarcanum.api.states.EntitySpellControlStateInstance;
import com.teamsimplyrs.prismaarcanum.api.states.data.HoverStateData;
import com.teamsimplyrs.prismaarcanum.api.states.data.RisingStateData;
import com.teamsimplyrs.prismaarcanum.api.states.data.SlammingStateData;
import com.teamsimplyrs.prismaarcanum.api.utils.Element;
import com.teamsimplyrs.prismaarcanum.api.utils.School;
import com.teamsimplyrs.prismaarcanum.registry.PADataAttachmentsRegistry;
import com.teamsimplyrs.prismaarcanum.spells.common.AbstractSpell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class GroundPound extends AbstractSpell {
    public static final String SPELL_ID = "ground_pound";
    protected static final Element ELEMENT = Element.Terra;
    protected static final School SCHOOL = School.Mortality;
    protected static final int TIER = 1;

    protected static int basicManaCost = 20;
    protected static int basicCooldown = 70;

    protected static boolean hasEvolution = true;

    public GroundPound() {
        super(SPELL_ID, ELEMENT, SCHOOL, TIER, basicManaCost, basicCooldown, 0, hasEvolution);
    }

    @Override
    public void cast(ServerPlayer player, Level world) {
        super.cast(player, world);
        EntitySpellControlStateComponent spellControlState = player.getData(PADataAttachmentsRegistry.SPELL_CONTROL_STATE.get());
        EntitySpellControlStateInstance stateChain = getStateChain(player, world);
        spellControlState.addState(stateChain);
    }

    public EntitySpellControlStateInstance getStateChain(ServerPlayer player, Level world) {
        EntitySpellControlStateInstance slammingState = new EntitySpellControlStateInstance(
                EntitySpellControlState.SLAMMING,
                new SlammingStateData(new Vec3(0f, -10f, 0f)),
                60,
                0,
                null,
                this.getResourceLocation(),
                () -> {
                    groundPoundSlam(player, world);
                }
        );

        EntitySpellControlStateInstance hoveringToSlammingState = new EntitySpellControlStateInstance(
                EntitySpellControlState.HOVERING,
                new HoverStateData(),
                2,
                0,
                slammingState,
                this.getResourceLocation(),
                null
        );
        EntitySpellControlStateInstance risingToHoverState = new EntitySpellControlStateInstance(
                EntitySpellControlState.RISING,
                new RisingStateData(new Vec3(0f, 2f, 0f)),
                3,
                0,
                hoveringToSlammingState,
                this.getResourceLocation(),
                null
        );

        return risingToHoverState;
    }

    @Override
    public ResourceLocation getFXid() {
        return ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "ground_pound_fx");
    }

    private void groundPoundSlam(ServerPlayer player, Level world) {
        if (!player.onGround()) return;

        double radius = 4.0d;
        double height = 0.5d;

        AABB box = player.getBoundingBox().inflate(radius, height, radius);
        List<Entity> entities = world.getEntities(player, box, entity -> entity != player);

        if (entities.isEmpty()) return;

        float pushForceVertical = 4f;
        float pushForceHorizontal = 1.5f;

        for (Entity entity: entities) {
            Vec3 pushForce = player.position().vectorTo(entity.position()).normalize();
            pushForce = pushForce.multiply(pushForceHorizontal, pushForceVertical, pushForceHorizontal);
            entity.hurt(player.damageSources().magic(), 5f);
            entity.push(pushForce);
        }
    }
}
