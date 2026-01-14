package com.teamsimplyrs.prismaarcanum.api.casting.spell_events;

import com.teamsimplyrs.prismaarcanum.PrismaArcanum;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonConfiguration;
import com.zigythebird.playeranimcore.api.firstPerson.FirstPersonMode;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PlayAnimationSpellEvent extends AbstractSpellTimedEvent{

    ResourceLocation animationLocation;

    public PlayAnimationSpellEvent(ResourceLocation animationLocation) {
        this.animationLocation = animationLocation;
    }

    @Override
    public void run(Player player) {
        Level level = player.level();
        //Animation player
        if (level.isClientSide() && animationLocation != null) {
            PlayerAnimationController controller = (PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer(
                    (AbstractClientPlayer) player, ResourceLocation.fromNamespaceAndPath(PrismaArcanum.MOD_ID, "spell_caster"));
            assert controller != null;
            controller.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
            FirstPersonConfiguration config = new FirstPersonConfiguration(true, true, true, true, true);
            controller.setFirstPersonConfiguration(config);
            controller.triggerAnimation(animationLocation);
        }
    }
}
