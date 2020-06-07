package com.dmtrdev.monsters.consts;

public final class ConstGame {

    public enum State {PLAY, PAUSE}
    public static State GAME_STATE = State.PLAY;

    public static final String FIRST_LOADING = "first_loading";
    public static final String SECOND_LOADING = "second_loading";
    public static final String STORY_MODE = "_story";
    public static final String SURVIVE_MODE = "_survive";
    public static final String CHALLENGE_MODE = "_challenge";
    public static final float SURVIVE_ARMOR_TIME = 3.7f;

    public static final short GROUND_BIT = 1;
    public static final short TREE_LOW_BIT = 2;
    public static final short TREE_BIT = 4;
    public static final short ENEMY_BIT = 8;
    public static final short ARMOR_BIT = 16;
    public static final short PLAYER_BIT = 32;
    public static final short COIN_BIT = 64;
    public static final short FIRE_BIT = 128;
    public static final short DISABLE_BIT = 256;
    public static final short ENEMY_DOWN_BIT = 512;
    public static final short ARMOR_ENEMY_BIT = 1024;
    public static final short ENEMY_SHOTS_BIT = 2048;
    public static final short SKY_BIT = 4096;
    public static final short SKY_ARMOR_BIT = 8192;

    public static final byte X = 28;
    public static final byte Y = 21;
    public static final int LEVELS_SIZE = 10;
    public static final int ARMORS_SIZE = 28;
    public static final byte PLAYER_POSITION_Y = Y - 3;

    //Loading screen elements const
    public static final float LOADING_PROGRESS_POS__X = 0.5f;
    public static final float LOADING_PROGRESS_POS__Y = 3.5f;
    public static final byte LOADING_FRAME_WIDTH = 24;
    public static final float LOADING_FRAME_HEIGHT = 4.2f;
    public static final float LOADING_PROGRESS_WIDTH = 22.7f;
    public static final float LOADING_PROGRESS_HEIGHT = 1.1f;
    public static final float LOADING_ELEMENT_POS__Y = 2.4f;

    //Book screen elements const
    public static final float BUTTONS_SIZES = 2.4f;
    public static final byte FRAME_WIDTH = 27;
    public static final byte FRAME_HEIGHT = 20;
    public static final float PANELS_WIDTH = 12.5f;
    public static final float PANELS_HEIGHT = 9.2f;
    public static final float PANELS_SPEED = 0.3f;

    //Menu screen elements const
    public static final float MENU_PANELS_SPEED = 0.3f;
    public static final float MENU_PLAY_BUTTON = 6.6f;
    public static final float MENU_MIDDLE_BUTTON = 5.5f;
    public static final float MENU_LOW_BUTTON = 3.5f;
    public static final byte MENU_PANEL_WIDTH = 21;
    public static final byte MENU_PANEL_HEIGHT = 20;
    public static final float SETTINGS_BOX_BUTTON = 2.5f;
    public static final byte SETTINGS_BOX_BUTTON_LOW = 2;
    public static final float TEXT_LOW_POS_Y = 5.75f;
    public static final float TEXT_HIGH_POS_Y = 11.88f;
    public static final float TEXT_LOW_POS_X = 18.3f;
    public static final float TEXT_HIGH_POS_X = 10.7f;

    //Game mode screen elements const
    public static final byte MODE_PANEL = 12;
    public static final byte MODE_LEVEL = 4;

    //Armor screen elements const
    public static final float PLAYER_INPUT_PANEL_HEIGHT = 4.5f;
    public static final byte ARMOR_PANEL_WIDTH = 21;
    public static final byte ARMOR_PANEL_HEIGHT = 15;
    public static final int ARMOR_TABLE_WIDTH = 15;
    public static final int ARMOR_TABLE_HEIGHT = 5;
    public static final int ARMOR_BUTTON = 3;

    //Game screen elements const
    public static final byte PANEL_WIDTH = 12;
    public static final byte PANEL_HEIGHT = 20;
    public static final float PANEL_BUTTON_WIDTH = 5.8f;
    public static final float PANEL_BUTTON = 3.4f;
    public static final float PANEL_BOX_BUTTON_LITTLE = 3.5f;
    public static final byte PANEL_BOX_BUTTON_FRAME = 2;
    public static final float BAR_HP_FRAME_WIDTH = 7.54f;
    public static final float BAR_HP_FRAME_HEIGHT = 3;
    public static final byte BAR_HP_WIDTH = 5;
    public static final float BAR_HP_HEIGHT = 1.08f;

    //Background elements const
    public static final byte FRAME_WORLD_MARGIN = 5;
    public static final byte WORLD_SIDES = 18;
    public static final float SPAWN_HEIGHT = 5.8f;
    public static final float CLOUDS_SPEED = 0.0065f;
}