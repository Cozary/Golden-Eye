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

package com.cozary.golden_eye.config;

import com.cozary.golden_eye.GoldenEye;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.*;

public class ConfigHandler {

    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
            .create();
    public static ConfigHandler CONFIG;
    @Expose
    public int durability;
    @Expose
    public int cooldown;
    protected String root = GoldenEye.CONFIG_DIR_PATH;
    protected String extension = ".json";

    public static void register() {
        CONFIG = new ConfigHandler().readConfig();
        GoldenEye.LOGGER.info("Configs loaded!");
    }

    public void generateConfig() {
        this.reset();

        try {
            this.writeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getConfigFile() {
        return new File(this.root + this.getName() + this.extension);
    }

    public ConfigHandler readConfig() {
        try {
            return GSON.fromJson(new FileReader(this.getConfigFile()), this.getClass());
        } catch (FileNotFoundException e) {
            this.generateConfig();
        }

        return this;
    }

    public void writeConfig() throws IOException {
        File dir = new File(this.root);
        if (!dir.exists() && !dir.mkdirs())
            return;
        if (!this.getConfigFile().exists() && !this.getConfigFile().createNewFile())
            return;
        FileWriter writer = new FileWriter(this.getConfigFile());
        GSON.toJson(this, writer);
        writer.flush();
        writer.close();
    }

    protected void reset() {
        this.durability = 100;
        this.cooldown = 120;
    }

    public String getName() {
        return "golden_eye_config";
    }
}
