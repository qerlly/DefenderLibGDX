package com.dmtrdev.monsters;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.SplashScreen;
import com.dmtrdev.monsters.utils.MusicManager;
import com.dmtrdev.monsters.utils.PlayServices;

public class DefenderOfNature extends Game implements ApplicationListener {

    private final PlayServices playServices;
    public static AssetManager manager;
    public static MusicManager musicManager = null;

    public DefenderOfNature(PlayServices pPlayServices){
        playServices = pPlayServices;
    }

    public PlayServices getPlayServices(){
        return playServices;
    }

    @Override
    public void create() {
        manager = new AssetManager();
        setScreen(new SplashScreen(this, ConstGame.FIRST_LOADING));
    }

    public static TextureAtlas getGuiAtlas() {
        return manager.get("atlases/gui/gui_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getWorldAtlas() {
        return manager.get("atlases/world/world_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getGoblinsAtlas() {
        return manager.get("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getOrcsAtlas() {
        return manager.get("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getTrollsAtlas() {
        return manager.get("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getAnimalsAtlas() {
        return manager.get("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getZombieAtlas() {
        return manager.get("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getGargoyleAtlas() {
        return manager.get("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getMinotaursAtlas() {
        return manager.get("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getCyclopsAtlas() {
        return manager.get("atlases/enemies/cyclops_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getElementalAtlas() {
        return manager.get("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getFlyinglAtlas() {
        return manager.get("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getGolemsAtlas() {
        return manager.get("atlases/enemies/golems_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getDragonsAtlas() {
        return manager.get("atlases/enemies/dragons_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getYetisAtlas() {
        return manager.get("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getArmorsAtlas() {
        return manager.get("atlases/armors/armor_atlas.txt", TextureAtlas.class);
    }

    public static TextureAtlas getEffectsAtlas() {
        return manager.get("atlases/effects/effects_atlas.txt", TextureAtlas.class);
    }

    public static BitmapFont getFont() {
        return manager.get("font/game_font.fnt", BitmapFont.class);
    }

    //SOUNDS
    public static Sound getButtonClickOn() {
        return manager.get("sounds/click_on.wav", Sound.class);
    }

    public static Sound getButtonClickOff() {
        return manager.get("sounds/click_off.wav", Sound.class);
    }

    public static Sound getBuySound() {
        return manager.get("sounds/buy.wav", Sound.class);
    }

    public static Sound getCoinSound() {
        return manager.get("sounds/coin_sound.mp3", Sound.class);
    }

    public static Sound getErrorSound() {
        return manager.get("sounds/error_sound.mp3", Sound.class);
    }

    public static Sound getVictorySound() {
        return manager.get("sounds/win_sound.mp3", Sound.class);
    }

    public static Sound getLoseSound() {
        return manager.get("sounds/game_over_sound.mp3", Sound.class);
    }

    public static Sound getTomatoSound() {
        return manager.get("sounds/tomato_sound.mp3", Sound.class);
    }

    public static Sound getPumpkinSound() {
        return manager.get("sounds/pumpkin_sound.mp3", Sound.class);
    }

    public static Sound getStoneSound() {
        return manager.get("sounds/stone_axe_sound.wav", Sound.class);
    }

    public static Sound getShurikenSound() {
        return manager.get("sounds/shuriken_sound.mp3", Sound.class);
    }

    public static Sound getTrapSound() {
        return manager.get("sounds/trap_sound.mp3", Sound.class);
    }

    public static Sound getBarrelSound() {
        return manager.get("sounds/barrel_sound.mp3", Sound.class);
    }

    public static Sound getRexSound() {
        return manager.get("sounds/rex_sound.wav", Sound.class);
    }

    public static Sound getPunchSound() {
        return manager.get("sounds/punch_sound.mp3", Sound.class);
    }

    public static Sound getBirdSound() {
        return manager.get("sounds/bird_death_sound.wav", Sound.class);
    }

    public static Sound getMolotovSound() {
        return manager.get("sounds/molotov_sound.wav", Sound.class);
    }

    public static Sound getFlySound() {
        return manager.get("sounds/birds_sound.mp3", Sound.class);
    }

    public static Sound getIronSound() {
        return manager.get("sounds/iron_axe_sound.wav", Sound.class);
    }

    public static Sound getBombikSound() {
        return manager.get("sounds/mini_bomb_sound.wav", Sound.class);
    }

    public static Sound getBigSound() {
        return manager.get("sounds/big_stone_sound.mp3", Sound.class);
    }

    public static Sound getGrenadeSound() {
        return manager.get("sounds/grenade_sound.mp3", Sound.class);
    }

    public static Sound getBombSound() {
        return manager.get("sounds/bomb_sound.wav", Sound.class);
    }

    public static Sound getPlantSound() {
        return manager.get("sounds/eat_plant_sound.wav", Sound.class);
    }

    public static Sound getMooseSound() {
        return manager.get("sounds/moose_sound.mp3", Sound.class);
    }

    public static Sound getDeathSound() {
        return manager.get("sounds/enemy_death1__sound.wav", Sound.class);
    }

    public static Sound getDeathBigSound() {
        return manager.get("sounds/enemy_death2__sound.wav", Sound.class);
    }

    public static void unloadGamePlayRes(final String pType) {
        DefenderOfNature.manager.unload("atlases/player/player_atlas.txt");
        DefenderOfNature.manager.unload("atlases/armors/armor_atlas.txt");
        DefenderOfNature.manager.unload("atlases/effects/effects_atlas.txt");
        DefenderOfNature.manager.unload("sounds/game_over_sound.mp3");
        DefenderOfNature.manager.unload("sounds/tomato_sound.mp3");
        DefenderOfNature.manager.unload("sounds/pumpkin_sound.mp3");
        DefenderOfNature.manager.unload("sounds/stone_axe_sound.wav");
        DefenderOfNature.manager.unload("sounds/shuriken_sound.mp3");
        DefenderOfNature.manager.unload("sounds/trap_sound.mp3");
        DefenderOfNature.manager.unload("sounds/barrel_sound.mp3");
        DefenderOfNature.manager.unload("sounds/rex_sound.wav");
        DefenderOfNature.manager.unload("sounds/punch_sound.mp3");
        DefenderOfNature.manager.unload("sounds/birds_sound.mp3");
        DefenderOfNature.manager.unload("sounds/bird_death_sound.wav");
        DefenderOfNature.manager.unload("sounds/molotov_sound.wav");
        DefenderOfNature.manager.unload("sounds/iron_axe_sound.wav");
        DefenderOfNature.manager.unload("sounds/mini_bomb_sound.wav");
        DefenderOfNature.manager.unload("sounds/big_stone_sound.mp3");
        DefenderOfNature.manager.unload("sounds/grenade_sound.mp3");
        DefenderOfNature.manager.unload("sounds/eat_plant_sound.wav");
        DefenderOfNature.manager.unload("sounds/bomb_sound.wav");

        if (pType.contains(ConstGame.STORY_MODE)) {
            DefenderOfNature.manager.unload("sounds/win_sound.mp3");
            switch (pType) {
                case "0" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/orcs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
                    break;
                case "1" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/orcs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    break;
                case "2" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/orcs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    break;
                case "3" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/orcs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/gargoyle_atlas.txt");
                    break;
                case "4" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/orcs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/gargoyle_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
                    break;
                case "5" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/gargoyle_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/cyclops_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/yetsi_atlas.txt");
                    break;
                case "6" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/gargoyle_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/cyclops_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/yetsi_atlas.txt");
                    break;
                case "7" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/gargoyle_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/cyclops_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/yetsi_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/dragons_atlas.txt");
                    break;
                case "8" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/yetsi_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/dragons_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/golems_atlas.txt");
                    break;
                case "9" + ConstGame.STORY_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/yetsi_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/dragons_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/golems_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/king_of_death_atlas.txt");
                    break;
            }
        } else if (pType.contains(ConstGame.CHALLENGE_MODE)) {
            DefenderOfNature.manager.unload("sounds/win_sound.mp3");
            switch (pType) {
                case "0" + ConstGame.CHALLENGE_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    break;
                case "1" + ConstGame.CHALLENGE_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
                    break;
                case "2" + ConstGame.CHALLENGE_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/dragons_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
                    break;
                case "3" + ConstGame.CHALLENGE_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/cyclops_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
                    DefenderOfNature.manager.unload("atlases/enemies/golems_atlas.txt");
                    break;
                case "4" + ConstGame.CHALLENGE_MODE:
                    DefenderOfNature.manager.unload("atlases/enemies/king_of_death_atlas.txt");
                    break;
            }
        } else {
            DefenderOfNature.manager.unload("atlases/enemies/goblins_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/orcs_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/zombie_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/flyings_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/trolls_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/animals_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/elementals_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/gargoyle_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/minotaurs_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/cyclops_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/yetsi_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/dragons_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/golems_atlas.txt");
            DefenderOfNature.manager.unload("atlases/enemies/king_of_death_atlas.txt");
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        manager.dispose();
        musicManager.dispose();
    }
}

