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

package com.cozary.golden_eye.item;

import com.cozary.golden_eye.config.ConfigHandler;
import com.cozary.golden_eye.entities.GoldenEyeEntity;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class GoldenEyeItem extends Item {
    public static final String structure = "structureName";
    public static List<ResourceLocation> allStructuresRL = new ArrayList<>();


    public GoldenEyeItem(Properties properties) {
        super(properties);
    }


    public static void getStructures(Level level) {
        Registry<Structure> configuredRegistryRL = level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
        configuredRegistryRL.keySet().forEach(location -> {
            if (!allStructuresRL.contains(location)) {
                allStructuresRL.add(location);
            }
        });
        Collections.sort(allStructuresRL);
    }

    public static String getStructureNameRL(ResourceLocation structure) {

        if (structure == null) {
            return "";
        }
        String name = structure.toString();
        name = WordUtils.capitalize(StringUtils.contains(name, ":") ? StringUtils.substringAfter(name, ":") + " " : name);
        name = WordUtils.capitalize(name.replace("_", " "));

        if (name.equals(Util.makeDescriptionId("structure", structure))) {
            name = structure.toString();
            name = WordUtils.capitalize(StringUtils.contains(name, ":") ? StringUtils.substringAfter(name, ":") + " " : name);
            name = WordUtils.capitalize(name.replace("_", " "));
        }
        return name;
    }

    private void trySpawnEntity(@Nonnull ItemStack stack, ServerLevel world, ServerPlayer player) {

        if (stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains(structure)) {
            ServerLevel level = (ServerLevel) player.level;
            CompoundTag tag = stack.getTag();

            String boundStructure = tag.getString(structure);
            ResourceLocation structureLocation = ResourceLocation.tryParse(boundStructure);

            if (structureLocation != null) {
                ResourceKey<Structure> structureKey = ResourceKey.create(Registry.STRUCTURE_REGISTRY, structureLocation);
                Registry<Structure> registry = level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY);
                HolderSet<Structure> featureHolderSet = registry.getHolder(structureKey).map((holders) -> {
                    return HolderSet.direct(holders);
                }).orElse(null);
                if (featureHolderSet != null) {
                    int searchRange = 100;

                    boolean findUnexplored = false;

                    Pair<BlockPos, Holder<Structure>> pair =
                            level.getChunkSource().getGenerator().findNearestMapStructure(level, featureHolderSet, player.blockPosition(), searchRange, findUnexplored);
                    BlockPos blockpos = pair != null ? pair.getFirst() : null;


                    if (blockpos != null) {
                        stack.hurtAndBreak(1, player, (p_43076_) -> p_43076_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                        player.getCooldowns().addCooldown(this, ConfigHandler.CONFIG.cooldown); //ConfigHandler.cooldown.get()
                        GoldenEyeEntity eye = new GoldenEyeEntity(world, player.getX(), player.getY(0.5D), player.getZ());
                        eye.setItem(stack);
                        eye.signalTo(blockpos);
                        world.addFreshEntity(eye);
                        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 0.5F, 0.4F / (0.4F + 0.8F));
                    }
                }
            }
        } else
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FLINTANDSTEEL_USE, SoundSource.NEUTRAL, 0.5F, 0.4F / (0.4F + 0.8F));
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        final ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = new CompoundTag();

        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(stack);

        if (level instanceof final ServerLevel betterWorld) {

            if (player.isShiftKeyDown()) {

                if (allStructuresRL.isEmpty())
                    getStructures(level);


                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_CHAIN, SoundSource.NEUTRAL, 0.5F, 0.4F / (0.4F + 0.8F));

                Collections.rotate(allStructuresRL, 1);
                tag.putString(structure, allStructuresRL.get(0).toString());
                stack.setTag(tag);
                player.sendSystemMessage(Component.literal(ChatFormatting.DARK_AQUA + "Structure Selected: " + ChatFormatting.WHITE + getStructureNameRL(allStructuresRL.get(0))));
                return InteractionResultHolder.pass(stack);
            }

            final ServerPlayer betterPlayer = (ServerPlayer) player;

            if (!allStructuresRL.isEmpty()) {

                trySpawnEntity(stack, betterWorld, betterPlayer);

            }

            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (!allStructuresRL.isEmpty()) {
            tooltip.add(Component.translatable(ChatFormatting.YELLOW + "Shift + Right Click" + ChatFormatting.BLUE + " to Change Structure"));
            tooltip.add(Component.translatable(ChatFormatting.DARK_AQUA + "Structure Selected: " + ChatFormatting.GOLD + ChatFormatting.BOLD + getStructureNameRL(allStructuresRL.get(0))));
        } else
            tooltip.add(Component.translatable(ChatFormatting.RED + "Shift Right Click to Search"));

    }

}
