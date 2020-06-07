package com.dmtrdev.monsters.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.GameModeScreen;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.spawns.Generates;
import com.dmtrdev.monsters.sprites.Player;
import com.dmtrdev.monsters.sprites.world.Background;
import com.dmtrdev.monsters.sprites.world.Tree;
import com.dmtrdev.monsters.utils.ArmorTable;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.utils.Options;
import com.dmtrdev.monsters.utils.PlayServices;
import com.dmtrdev.monsters.utils.TextGame;

public class GameGui extends Stage {

    private final Game mGame;
    private final Skin mSkin;
    private final World mWorld;
    private final SpriteBatch mBatch;
    private final Player mPlayer;
    private final Tree mTree;
    private final Background mBackground;
    private final OrthographicCamera mCamera;
    private final Options mOptions;
    private final Generates mGenerates;
    private final Image mTreeHP;
    private Group mMenuGroup;
    private boolean mFlagLeft, mFlagRight;
    private final ArmorTable mTable;
    private final Button mPauseButton;
    private Button mMusicButton, mSoundButton;
    private boolean mFlag;
    private Button.ButtonStyle mSoundButtonStyleOff, mSoundButtonStyleOn, mMusicButtonStyleOff,
            mMusicButtonStyleOn;
    private final PlayScreen mPlayScreen;
    private PlayServices mPlaySevices;

    public GameGui(final Game pGame, final PlayScreen pPlayScreen, final ArmorTable pArmorTable) {
        super(new StretchViewport(ConstGame.X, ConstGame.Y));
        mCamera = pPlayScreen.getCamera();
        mCamera.position.set(new Vector3(ConstGame.X / 2, ConstGame.Y / 2, 0));
        mPlayScreen = pPlayScreen;
        mFlag = false;
        mGame = pGame;
        mTable = pArmorTable;
        mSkin = pPlayScreen.getSkin();
        mWorld = pPlayScreen.getWorld();
        mOptions = pPlayScreen.getOptions();
        mPlayer = pPlayScreen.getPlayer();
        mTree = pPlayScreen.getTree();
        mBackground = new Background(mWorld);
        mGenerates = new Generates(pPlayScreen);
        mBatch = new SpriteBatch();
        mPlaySevices = ((DefenderOfNature)pGame).getPlayServices();

        final Image frameBarHP = new Image(mSkin.getDrawable("hp_bar_frame"));
        frameBarHP.setBounds(0.3f, ConstGame.Y - ConstGame.BAR_HP_FRAME_HEIGHT - 0.3f, ConstGame.BAR_HP_FRAME_WIDTH, ConstGame.BAR_HP_FRAME_HEIGHT);
        addActor(frameBarHP);
        mTreeHP = new Image(mSkin.getDrawable("hp_bar"));
        mTreeHP.setBounds(2.435f, ConstGame.Y - ConstGame.BAR_HP_FRAME_HEIGHT + 0.605f, ConstGame.BAR_HP_WIDTH, ConstGame.BAR_HP_HEIGHT);
        addActor(mTreeHP);

        createPlayerInput();
        createMenu();

        final Button.ButtonStyle pauseButtonStyle = new Button.ButtonStyle();
        pauseButtonStyle.down = mSkin.getDrawable("button_pause_2");
        pauseButtonStyle.up = mSkin.getDrawable("button_pause_1");
        mPauseButton = new Button(pauseButtonStyle);
        mPauseButton.setBounds(ConstGame.X - 3.1f, ConstGame.Y - 4.3f, ConstGame.MENU_LOW_BUTTON - 0.75f, ConstGame.MENU_LOW_BUTTON + 0.5f);
        mPauseButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (ConstGame.GAME_STATE == ConstGame.State.PLAY) {
                    mMenuGroup.addAction(Actions.moveTo(0, 0, ConstGame.MENU_PANELS_SPEED));
                    if (mOptions.getSoundCheck()) {
                        DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                    }
                    ConstGame.GAME_STATE = ConstGame.State.PAUSE;
                    DefenderOfNature.musicManager.pause();
                } else {
                    ConstGame.GAME_STATE = ConstGame.State.PLAY;
                    mMenuGroup.addAction(Actions.moveTo(0, ConstGame.Y, ConstGame.MENU_PANELS_SPEED));
                    if (mOptions.getSoundCheck()) {
                        DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                    }
                    DefenderOfNature.musicManager.play();
                }
            }
        });
        addActor(mPauseButton);
    }

    private void createVictoryScreen() {
        final Group victoryGroup = new Group();
        victoryGroup.setBounds(0, ConstGame.Y, ConstGame.X, ConstGame.Y);

        final Button.ButtonStyle panelStyle = new Button.ButtonStyle();
        panelStyle.up = mSkin.getDrawable("defeat_frame");
        final Button panel = new Button(panelStyle);
        panel.setBounds(ConstGame.X / 2 - ConstGame.PANEL_WIDTH / 2, ConstGame.Y - ConstGame.PANEL_HEIGHT, ConstGame.PANEL_WIDTH, ConstGame.PANEL_HEIGHT);
        victoryGroup.addActor(panel);

        final Image star = new Image(mSkin.getDrawable("star_2"));
        star.setBounds(ConstGame.X / 2 + 0.51f, ConstGame.Y / 2 + 2.875f, 1.2f, 1.2f);
        victoryGroup.addActor(star);

        final TextGame enemy = new TextGame(String.valueOf(mGenerates.getDeadEnemies()), ConstGame.X / 2 + 1.075f, ConstGame.Y / 2 + 1.2f);
        victoryGroup.addActor(enemy);

        final TextGame score = new TextGame(String.valueOf(mGenerates.getScore()), ConstGame.X / 2 + 1.075f, ConstGame.Y / 2 - 1.44f);
        victoryGroup.addActor(score);

        final Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        exitButtonStyle.down = mSkin.getDrawable("button_check_2");
        exitButtonStyle.up = mSkin.getDrawable("button_check_1");
        final Button exitButton = new Button(exitButtonStyle);
        exitButton.setBounds(ConstGame.X / 2 - ConstGame.SETTINGS_BOX_BUTTON / 2, ConstGame.Y / 2 - 6.7f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
        exitButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                mPlayScreen.setStats(mGenerates.getScore(), mGenerates.getWaves(), mGenerates.getDeadEnemies());
                mGame.setScreen(new GameModeScreen(mGame));
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                ConstGame.GAME_STATE = ConstGame.State.PLAY;
                DefenderOfNature.musicManager.changeMusic(false);
                mPlaySevices.showInterstitial();
            }
        });

        victoryGroup.addActor(exitButton);
        addActor(victoryGroup);
        victoryGroup.addAction(Actions.moveTo(0, 0, ConstGame.MENU_PANELS_SPEED));
    }

    private void createGameOverScreen() {
        final Group gameOverGroup = new Group();
        gameOverGroup.setBounds(0, ConstGame.Y, ConstGame.X, ConstGame.Y);

        final Button.ButtonStyle panelStyle = new Button.ButtonStyle();
        panelStyle.up = mSkin.getDrawable("defeat_survive_frame");
        final Button panel = new Button(panelStyle);
        panel.setBounds(ConstGame.X / 2 - ConstGame.PANEL_WIDTH / 2, ConstGame.Y - ConstGame.PANEL_HEIGHT, ConstGame.PANEL_WIDTH, ConstGame.PANEL_HEIGHT);
        gameOverGroup.addActor(panel);

        if (mGenerates.getGameModeHelper().isSurviveMode()) {
            final TextGame waves = new TextGame(String.valueOf(mGenerates.getWaves()), ConstGame.X / 2 + 1.075f, ConstGame.Y / 2 + 3.85f);
            gameOverGroup.addActor(waves);
        } else {
            final Image star = new Image(mSkin.getDrawable("star_1"));
            star.setBounds(ConstGame.X / 2 + 0.51f, ConstGame.Y / 2 + 2.875f, 1.2f, 1.2f);
            gameOverGroup.addActor(star);
        }

        final TextGame enemy = new TextGame(String.valueOf(mGenerates.getDeadEnemies()), ConstGame.X / 2 + 1.075f, ConstGame.Y / 2 + 1.2f);
        gameOverGroup.addActor(enemy);

        final TextGame score = new TextGame(String.valueOf(mGenerates.getScore()), ConstGame.X / 2 + 1.075f, ConstGame.Y / 2 - 1.44f);
        gameOverGroup.addActor(score);

        final Button.ButtonStyle restartButtonStyle = new Button.ButtonStyle();
        restartButtonStyle.down = mSkin.getDrawable("button_replay_2");
        restartButtonStyle.up = mSkin.getDrawable("button_replay_1");
        final Button restartButton = new Button(restartButtonStyle);
        restartButton.setBounds(ConstGame.X / 2 - ConstGame.SETTINGS_BOX_BUTTON - 0.1f, ConstGame.Y / 2 - 6.7f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
        restartButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                mPlayScreen.setStats(mGenerates.getScore(), mGenerates.getWaves(), mGenerates.getDeadEnemies());
                mPlayScreen.setRestart(true);
                mGame.setScreen(new PlayScreen(mGame, mPlayScreen.getType()));
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                ConstGame.GAME_STATE = ConstGame.State.PLAY;
                DefenderOfNature.musicManager.changeMusic(false);
            }
        });

        final Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        exitButtonStyle.down = mSkin.getDrawable("button_check_2");
        exitButtonStyle.up = mSkin.getDrawable("button_check_1");
        final Button exitButton = new Button(exitButtonStyle);
        exitButton.setBounds(ConstGame.X / 2 - 0.1f, ConstGame.Y / 2 - 6.7f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
        exitButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                mPlayScreen.setStats(mGenerates.getScore(), mGenerates.getWaves(), mGenerates.getDeadEnemies());
                mGame.setScreen(new GameModeScreen(mGame));
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                ConstGame.GAME_STATE = ConstGame.State.PLAY;
                DefenderOfNature.musicManager.changeMusic(false);
            }
        });

        gameOverGroup.addActor(restartButton);
        gameOverGroup.addActor(exitButton);
        addActor(gameOverGroup);
        gameOverGroup.addAction(Actions.moveTo(0, 0, ConstGame.MENU_PANELS_SPEED));
    }

    public Game getGame(){
        return mGame;
    }

    private void createMenu() {
        mMenuGroup = new Group();
        mMenuGroup.setBounds(0, ConstGame.Y, ConstGame.X, ConstGame.Y);

        final Button.ButtonStyle panelStyle = new Button.ButtonStyle();
        panelStyle.up = mSkin.getDrawable("paused_frame");
        final Button panel = new Button(panelStyle);
        panel.setBounds(ConstGame.X / 2 - ConstGame.PANEL_WIDTH / 2, ConstGame.Y - ConstGame.PANEL_HEIGHT, ConstGame.PANEL_WIDTH, ConstGame.PANEL_HEIGHT);
        mMenuGroup.addActor(panel);

        mMusicButtonStyleOn = new Button.ButtonStyle();
        mMusicButtonStyleOn.down = mSkin.getDrawable("button_music_2");
        mMusicButtonStyleOn.up = mSkin.getDrawable("button_music_1");
        mMusicButtonStyleOff = new Button.ButtonStyle();
        mMusicButtonStyleOff.down = mSkin.getDrawable("button_music_2");
        mMusicButtonStyleOff.up = mSkin.getDrawable("button_music_3");
        mMusicButton = new Button();
        mMusicButton.setBounds(ConstGame.X / 2 - ConstGame.SETTINGS_BOX_BUTTON - 0.1f, ConstGame.Y / 2 + 2.2f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
        mMusicButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getMusicCheck()) {
                    mOptions.setMusicCheck(false);
                } else {
                    mOptions.setMusicCheck(true);
                }
                if (mOptions.getSoundCheck() && mOptions.getMusicCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                } else if (mOptions.getSoundCheck() && !mOptions.getMusicCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
            }
        });
        mMenuGroup.addActor(mMusicButton);

        mSoundButtonStyleOn = new Button.ButtonStyle();
        mSoundButtonStyleOn.down = mSkin.getDrawable("button_sound_2");
        mSoundButtonStyleOn.up = mSkin.getDrawable("button_sound_1");
        mSoundButtonStyleOff = new Button.ButtonStyle();
        mSoundButtonStyleOff.down = mSkin.getDrawable("button_sound_2");
        mSoundButtonStyleOff.up = mSkin.getDrawable("button_sound_3");
        mSoundButton = new Button();
        mSoundButton.setBounds(ConstGame.X / 2 - 0.1f, ConstGame.Y / 2 + 2.2f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
        mSoundButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                    mOptions.setSoundCheck(false);
                } else {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                    mOptions.setSoundCheck(true);
                }
            }
        });
        mMenuGroup.addActor(mSoundButton);

        final Button.ButtonStyle resumeButtonStyle = new Button.ButtonStyle();
        resumeButtonStyle.down = mSkin.getDrawable("resume_title_2");
        resumeButtonStyle.up = mSkin.getDrawable("resume_title_1");
        final Button resumeButton = new Button(resumeButtonStyle);
        resumeButton.setBounds(ConstGame.X / 2 - ConstGame.PANEL_BUTTON_WIDTH / 2 - 0.125f, ConstGame.Y / 2 - 0.925f, ConstGame.PANEL_BUTTON_WIDTH, ConstGame.PANEL_BUTTON);
        resumeButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                ConstGame.GAME_STATE = ConstGame.State.PLAY;
                mMenuGroup.addAction(Actions.moveTo(0, ConstGame.Y, ConstGame.MENU_PANELS_SPEED));
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                DefenderOfNature.musicManager.play();
            }
        });
        mMenuGroup.addActor(resumeButton);

        final Button.ButtonStyle restartButtonStyle = new Button.ButtonStyle();
        restartButtonStyle.down = mSkin.getDrawable("restart_title_2");
        restartButtonStyle.up = mSkin.getDrawable("restart_title_1");
        final Button restartButton = new Button(restartButtonStyle);
        restartButton.setBounds(ConstGame.X / 2 - ConstGame.PANEL_BUTTON_WIDTH / 2 - 0.125f, ConstGame.Y / 2 - ConstGame.PANEL_BOX_BUTTON_FRAME - 1.9f, ConstGame.PANEL_BUTTON_WIDTH, ConstGame.PANEL_BUTTON);
        restartButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                mPlayScreen.setStats(mGenerates.getScore(), mGenerates.getWaves(), mGenerates.getDeadEnemies());
                mPlayScreen.setRestart(true);
                mGame.setScreen(new PlayScreen(mGame, mPlayScreen.getType()));
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                ConstGame.GAME_STATE = ConstGame.State.PLAY;
                DefenderOfNature.musicManager.changeMusic(false);
            }
        });
        mMenuGroup.addActor(restartButton);

        final Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        exitButtonStyle.down = mSkin.getDrawable("exit_title_2");
        exitButtonStyle.up = mSkin.getDrawable("exit_title_1");
        final Button exitButton = new Button(exitButtonStyle);
        exitButton.setBounds(ConstGame.X / 2 - ConstGame.PANEL_BUTTON_WIDTH / 2 - 0.125f, ConstGame.Y / 2 - ConstGame.PANEL_BOX_BUTTON_FRAME - 4.8f, ConstGame.PANEL_BUTTON_WIDTH, ConstGame.PANEL_BUTTON);
        exitButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                mPlayScreen.setStats(mGenerates.getScore(), mGenerates.getWaves(), mGenerates.getDeadEnemies());
                mGame.setScreen(new GameModeScreen(mGame));
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                ConstGame.GAME_STATE = ConstGame.State.PLAY;
                DefenderOfNature.musicManager.changeMusic(false);
            }
        });
        mMenuGroup.addActor(exitButton);
        addActor(mMenuGroup);
    }

    private void createPlayerInput() {
        final Group playerPanel = new Group();
        playerPanel.setBounds(0, 0, ConstGame.X, ConstGame.FRAME_WORLD_MARGIN);

        final Image image = new Image(mSkin.getDrawable("input_frame"));
        image.setBounds(0, 0, ConstGame.X, ConstGame.FRAME_WORLD_MARGIN - 0.1f);
        playerPanel.addActor(image);

        final Button.ButtonStyle leftButtonStyle = new Button.ButtonStyle();
        leftButtonStyle.down = mSkin.getDrawable("button_goleft_2");
        leftButtonStyle.up = mSkin.getDrawable("button_goleft_1");
        final Button leftButton = new Button(leftButtonStyle);
        leftButton.setBounds(0.05f, 0.1f, ConstGame.PANEL_BUTTON, ConstGame.PANEL_BUTTON + 1.5f);
        leftButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                mFlagLeft = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                mPlayer.getBody().setLinearVelocity(mPlayer.mBody.getLinearVelocity().x / 5, mPlayer.mBody.getLinearVelocity().y);
                mFlagLeft = false;
            }
        });
        playerPanel.addActor(leftButton);

        final Button.ButtonStyle rightButtonStyle = new Button.ButtonStyle();
        rightButtonStyle.down = mSkin.getDrawable("button_goright_2");
        rightButtonStyle.up = mSkin.getDrawable("button_goright_1");
        final Button rightButton = new Button(rightButtonStyle);
        rightButton.setBounds(ConstGame.PANEL_BOX_BUTTON_LITTLE + 0.35f, 0.1f, ConstGame.PANEL_BUTTON, ConstGame.PANEL_BUTTON + 1.5f);
        rightButton.addListener(new ClickListener() {

            @Override
            public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                mFlagRight = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(final InputEvent event, final float x, final float y, final int pointer, final int button) {
                super.touchUp(event, x, y, pointer, button);
                mPlayer.getBody().setLinearVelocity(mPlayer.mBody.getLinearVelocity().x / 5, mPlayer.mBody.getLinearVelocity().y);
                mFlagRight = false;
            }
        });
        playerPanel.addActor(rightButton);

        final Button.ButtonStyle jumpButtonStyle = new Button.ButtonStyle();
        jumpButtonStyle.down = mSkin.getDrawable("button_jump_2");
        jumpButtonStyle.up = mSkin.getDrawable("button_jump_1");
        final Button jumpButton = new Button(jumpButtonStyle);
        jumpButton.setBounds(ConstGame.X - ConstGame.MODE_LEVEL , -0.1f, ConstGame.PANEL_BUTTON + 0.4f, ConstGame.PANEL_BUTTON + 2);
        jumpButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mPlayer.mBody.getLinearVelocity().y == 0) {
                    mPlayer.mBody.applyLinearImpulse(new Vector2(0, 7), mPlayer.mBody.getWorldCenter(), true);
                }
            }
        });
        playerPanel.addActor(jumpButton);

        mTable.setBounds(ConstGame.X / 2 - 5.84f, -0.05f, ConstGame.ARMOR_TABLE_WIDTH, ConstGame.ARMOR_TABLE_HEIGHT);
        playerPanel.addActor(mTable);
        addActor(playerPanel);
    }

    public Generates getGenerates() {
        return mGenerates;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        mCamera.update();
        updateGui();
        if (!mPlayScreen.getLevelComplete() && !mPlayer.getDestroy() && ConstGame.GAME_STATE == ConstGame.State.PLAY) {
            mWorld.step((float) 1 / Gdx.graphics.getFramesPerSecond(), 4, 4);
            mTreeHP.setWidth(mTree.getHP() * 5 / mTree.getPrevHp());
            mTree.update();
            if (mFlagLeft  && mPlayer.mBody.getLinearVelocity().x > -5.4f) {
                mPlayer.mBody.applyLinearImpulse(new Vector2(-0.24f, 0), mPlayer.mBody.getWorldCenter(), true);
            } else if (mFlagRight  && mPlayer.mBody.getLinearVelocity().x < 5.4f) {
                mPlayer.mBody.applyLinearImpulse(new Vector2(0.24f, 0), mPlayer.mBody.getWorldCenter(), true);
            }
            mBackground.update();
            mPlayer.update(delta);
            mTable.update(delta);
            mGenerates.updateGenerations(delta);
        } else if (mPlayScreen.getLevelComplete() && !mFlag) {
            ConstGame.GAME_STATE = ConstGame.State.PAUSE;
            mPauseButton.setPosition(ConstGame.X, ConstGame.Y);
            createVictoryScreen();
            if (mOptions.getSoundCheck()) {
                DefenderOfNature.getVictorySound().play(mOptions.getSoundVolume());
            }
            mFlag = true;
        } else if (mPlayer.getDestroy() && !mFlag) {
            ConstGame.GAME_STATE = ConstGame.State.PAUSE;
            mPauseButton.setPosition(ConstGame.X, ConstGame.Y);
            mTreeHP.setWidth(0);
            createGameOverScreen();
            if (mOptions.getSoundCheck()) {
                DefenderOfNature.getLoseSound().play(mOptions.getSoundVolume());
            }
            DefenderOfNature.musicManager.pause();
            mFlag = true;
        }
        mBatch.setProjectionMatrix(mCamera.combined);
        mBatch.begin();
        mBackground.draw(mBatch);
        mTree.draw(mBatch);
        mPlayer.draw(mBatch);
        mTable.drawArmors(mBatch);
        mGenerates.drawEnemies(mBatch);
        mBatch.end();
    }

    private void updateGui() {
        if (mOptions.getSoundCheck()) {
            mSoundButton.setStyle(mSoundButtonStyleOn);
        } else {
            mSoundButton.setStyle(mSoundButtonStyleOff);
        }
        if (mOptions.getMusicCheck()) {
            mMusicButton.setStyle(mMusicButtonStyleOn);
        } else {
            mMusicButton.setStyle(mMusicButtonStyleOff);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        mSkin.dispose();
        mTable.dispose();
    }
}