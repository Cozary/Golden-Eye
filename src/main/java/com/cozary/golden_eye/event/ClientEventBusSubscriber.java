/*
 *
 *  * Copyright (c) 2024 Cozary
 *  *
 *  * This file is part of Golden Eye, a mod made for Minecraft.
 *  *
 *  * Golden Eye is free software: you can redistribute it and/or modify it
 *  * under the terms of the GNU General Public License as published
 *  * by the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * Golden Eye is distributed in the hope that it will be useful, but
 *  * WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * License along with Golden Eye.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.cozary.golden_eye.event;

import com.cozary.golden_eye.GoldenEye;
import com.cozary.golden_eye.init.ModEntityTypes;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GoldenEye.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {

    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.GOLDEN_EYE.get(), (p_174088_) -> {
            return new ThrownItemRenderer<>(p_174088_, 1.0F, true);
        });

    }

}
