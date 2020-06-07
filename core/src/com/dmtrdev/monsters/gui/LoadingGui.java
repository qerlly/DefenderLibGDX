package com.dmtrdev.monsters.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.BookScreen;
import com.dmtrdev.monsters.screens.MenuScreen;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.utils.PlayServices;

public class LoadingGui extends Stage {

    private final Game mGame;
    private final Skin mSkin;
    private float mProgressSize;
    private final String mType;
    private final Image mProgress;

    public LoadingGui(final Game pGame, final String pType) {
        super(new StretchViewport(ConstGame.X, ConstGame.Y));
        mGame = pGame;
        mType = pType;
        DefenderOfNature.manager.load("atlases/gui/loading_atlas.txt", TextureAtlas.class);
        DefenderOfNature.manager.finishLoading();
        mSkin = new Skin(DefenderOfNature.manager.get("atlases/gui/loading_atlas.txt", TextureAtlas.class));
        mProgressSize = 15;

        final Image background = new Image(mSkin.getDrawable("loadscreen_bacground"));
        background.setBounds(0, 0, ConstGame.X, ConstGame.Y);
        addActor(background);

        final Image barFrame = new Image(mSkin.getDrawable("progress_bar"));
        barFrame.setSize(ConstGame.LOADING_FRAME_WIDTH, ConstGame.LOADING_FRAME_HEIGHT);
        barFrame.setPosition(ConstGame.X / 2 - barFrame.getWidth() / 2, ConstGame.LOADING_ELEMENT_POS__Y);
        addActor(barFrame);

        mProgress = new Image(mSkin.getDrawable("progress"));
        mProgress.setSize(mProgressSize, ConstGame.LOADING_PROGRESS_HEIGHT);
        mProgress.setPosition(barFrame.getX() + ConstGame.LOADING_PROGRESS_POS__X, ConstGame.LOADING_PROGRESS_POS__Y);
        addActor(mProgress);

        if (ConstGame.FIRST_LOADING.equals(mType)) {
            DefenderOfNature.manager.load("atlases/gui/gui_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("font/game_font.fnt", BitmapFont.class);
            DefenderOfNature.manager.load("sounds/click_on.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/click_off.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/buy.wav", Sound.class);
            DefenderOfNature.manager.load("music/game_menu_music.mp3", Music.class);
            DefenderOfNature.manager.load("music/game_music.mp3", Music.class);
            DefenderOfNature.manager.load("atlases/world/world_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("atlases/world/items_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("atlases/gui/game_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("sounds/error_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/coin_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/moose_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/enemy_death1__sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/enemy_death2__sound.wav", Sound.class);
            DefenderOfNature.manager.load("atlases/gui/google_btn_nrm.png", Texture.class);
            DefenderOfNature.manager.load("atlases/gui/google_brn_prs.png", Texture.class);
        } else if (ConstGame.SECOND_LOADING.equals(mType)) {
            DefenderOfNature.manager.load("atlases/book/book_atlas.txt", TextureAtlas.class);
        } else {
            DefenderOfNature.manager.load("atlases/player/player_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("atlases/armors/armor_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("atlases/effects/effects_atlas.txt", TextureAtlas.class);
            DefenderOfNature.manager.load("sounds/game_over_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/tomato_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/pumpkin_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/stone_axe_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/shuriken_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/trap_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/barrel_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/rex_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/punch_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/birds_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/bird_death_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/molotov_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/iron_axe_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/mini_bomb_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/big_stone_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/grenade_sound.mp3", Sound.class);
            DefenderOfNature.manager.load("sounds/eat_plant_sound.wav", Sound.class);
            DefenderOfNature.manager.load("sounds/bomb_sound.wav", Sound.class);

            if(mType.contains(ConstGame.STORY_MODE)) {
                DefenderOfNature.manager.load("sounds/win_sound.mp3", Sound.class);
                switch (pType) {
                    case "0" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                        break;
                    case "1" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        break;
                    case "2" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        break;
                    case "3" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
                        break;
                    case "4" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                        break;
                    case "5" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/cyclops_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
                        break;
                    case "6" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/cyclops_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
                        break;
                    case "7" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/cyclops_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/dragons_atlas.txt", TextureAtlas.class);
                        break;
                    case "8" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/dragons_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/golems_atlas.txt", TextureAtlas.class);
                        break;
                    case "9" + ConstGame.STORY_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/dragons_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/golems_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/king_of_death_atlas.txt", TextureAtlas.class);
                        break;
                }
            } else if(mType.contains(ConstGame.CHALLENGE_MODE)){
                DefenderOfNature.manager.load("sounds/win_sound.mp3", Sound.class);
                switch (pType) {
                    case "0" + ConstGame.CHALLENGE_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        break;
                    case "1" + ConstGame.CHALLENGE_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                        break;
                    case "2" + ConstGame.CHALLENGE_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/dragons_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                        break;
                    case "3" + ConstGame.CHALLENGE_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/cyclops_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                        DefenderOfNature.manager.load("atlases/enemies/golems_atlas.txt", TextureAtlas.class);
                        break;
                    case "4" + ConstGame.CHALLENGE_MODE:
                        DefenderOfNature.manager.load("atlases/enemies/king_of_death_atlas.txt", TextureAtlas.class);
                        break;
                }
            } else {
                DefenderOfNature.manager.load("atlases/enemies/goblins_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/orcs_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/zombie_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/flyings_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/trolls_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/animals_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/elementals_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/gargoyle_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/minotaurs_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/cyclops_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/yetsi_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/dragons_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/golems_atlas.txt", TextureAtlas.class);
                DefenderOfNature.manager.load("atlases/enemies/king_of_death_atlas.txt", TextureAtlas.class);
            }
        }
    }

    @Override
    public void act() {
        super.act();
        if (DefenderOfNature.manager.update() && mProgress.getWidth() == ConstGame.LOADING_PROGRESS_WIDTH) {
            if (ConstGame.FIRST_LOADING.equals(mType)) {
                mGame.setScreen(new MenuScreen(mGame));
                DefenderOfNature.manager.unload("atlases/gui/loading_atlas.txt");
            } else if (ConstGame.SECOND_LOADING.equals(mType)) {
                mGame.setScreen(new BookScreen(mGame));
                DefenderOfNature.manager.unload("atlases/gui/loading_atlas.txt");
            } else {
                mGame.setScreen(new PlayScreen(mGame, mType));
                DefenderOfNature.manager.unload("atlases/gui/loading_atlas.txt");
            }
        } else if (DefenderOfNature.manager.getProgress() == 1) {
            mProgress.setWidth(ConstGame.LOADING_PROGRESS_WIDTH);
        } else if (mProgress.getWidth() < ConstGame.LOADING_PROGRESS_WIDTH) {
            mProgressSize += DefenderOfNature.manager.getProgress() / 10;
            mProgress.setWidth(mProgressSize);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        mSkin.dispose();
    }
}