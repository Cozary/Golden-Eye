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

package com.cozary.golden_eye.init;


import com.cozary.golden_eye.GoldenEye;
import com.cozary.golden_eye.entities.GoldenEyeEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = GoldenEye.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, GoldenEye.MOD_ID);

    public static final RegistryObject<EntityType<GoldenEyeEntity>> GOLDEN_EYE = ENTITY_TYPES.register("golden_eye", () -> EntityType.Builder.<GoldenEyeEntity>of(GoldenEyeEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(4)
            .build(new ResourceLocation(GoldenEye.MOD_ID, "golden_eye").toString()));

}
