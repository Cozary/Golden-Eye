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

package com.cozary.golden_eye;

import com.cozary.golden_eye.config.ConfigHandler;
import com.cozary.golden_eye.init.ModEntityTypes;
import com.cozary.golden_eye.init.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

@Mod("golden_eye")
public class GoldenEye {

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "golden_eye";
    public static final CreativeModeTab TAB = new CreativeModeTab("golden_eyeTab") {
        public @NotNull
        ItemStack makeIcon() {
            return new ItemStack(ModItems.GOLDEN_EYE.get());
        }
    };
    public static final String CONFIG_DIR_PATH = "config/" + MOD_ID + "/";

    public GoldenEye() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::constructMod);

        ModItems.ITEMS.register(eventBus);
        ModEntityTypes.ENTITY_TYPES.register(eventBus);


        MinecraftForge.EVENT_BUS.register(this);
    }

    public void constructMod(final FMLConstructModEvent event) {
        ConfigHandler.register();
    }

}