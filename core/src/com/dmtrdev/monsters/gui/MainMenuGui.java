package com.dmtrdev.monsters.gui;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.GameModeScreen;
import com.dmtrdev.monsters.screens.SplashScreen;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.utils.MusicManager;
import com.dmtrdev.monsters.utils.Options;
import com.dmtrdev.monsters.utils.PlayServices;
import com.dmtrdev.monsters.utils.ShopButton;
import com.dmtrdev.monsters.utils.TextGame;


public class MainMenuGui extends Stage {

    private final Game mGame;
    private byte mPages;
    private final Skin mSkin;
    private Group[] mGroups;
    private TextGame mCoinsText;
    private final Options mOptions;
    private final Button mBackButton;
    private Button mMusicButton, mSoundButton, mEffectButton, mPreviousButton;
    private TextGame mWaveTrophy, mScoreTrophy, mEnemiesTrophy, mChallengesTrophy;
    private Group mMainMenuGui, mSettingsGui, mShopGui, mTrophyGui;
    private Button.ButtonStyle mSoundButtonStyleOff, mSoundButtonStyleOn, mMusicButtonStyleOff,
            mMusicButtonStyleOn, mEffectButtonStyleOff, mEffectButtonStyleOn;
    private PlayServices playServices;

    public MainMenuGui(final Game pGame) {
        super(new StretchViewport(ConstGame.X, ConstGame.Y));
        playServices = ((DefenderOfNature) pGame).getPlayServices();

        mGame = pGame;
        mSkin = new Skin();
        mOptions = new Options();
        DefenderOfNature.musicManager = new MusicManager(mOptions);
        DefenderOfNature.musicManager.play();
        mSkin.addRegions(DefenderOfNature.getGuiAtlas());
        mSkin.add("atlases/gui/google_btn_nrm.png", Texture.class);
        mSkin.add("atlases/gui/google_btn_prs.png", Texture.class);
        final Image image = new Image(mSkin.getDrawable("gui_background"));
        image.setBounds(0, 0, ConstGame.X, ConstGame.Y);
        addActor(image);

        final Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.down = mSkin.getDrawable("button_goleft_2");
        backButtonStyle.up = mSkin.getDrawable("button_goleft_1");
        mBackButton = new Button(backButtonStyle);
        mBackButton.setSize(ConstGame.MENU_LOW_BUTTON, ConstGame.MENU_LOW_BUTTON + 1.5f);
        mBackButton.setPosition(0.6f, ConstGame.Y);
        mBackButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                if (mMainMenuGui.getY() == -ConstGame.Y) {
                    mSettingsGui.addAction(Actions.moveTo(0, ConstGame.Y, ConstGame.MENU_PANELS_SPEED));
                    mShopGui.addAction(Actions.moveTo(0, ConstGame.Y, ConstGame.MENU_PANELS_SPEED));
                    mTrophyGui.addAction(Actions.moveTo(0, ConstGame.Y, ConstGame.MENU_PANELS_SPEED));
                    mMainMenuGui.setPosition(0, 0);
                }
            }
        });

        createShopGui();
        createSettingsGui();
        createTrophyGui();
        createMainMenu();
        addActor(mBackButton);
    }

    private void createMainMenu() {
        mMainMenuGui = new Group();
        mMainMenuGui.setBounds(0, 0, ConstGame.X, ConstGame.Y);

        final Button.ButtonStyle shopButtonStyle = new Button.ButtonStyle();
        shopButtonStyle.down = mSkin.getDrawable("button_shop_2");
        shopButtonStyle.up = mSkin.getDrawable("button_shop_1");
        final Button shopButton = new Button(shopButtonStyle);
        shopButton.setSize(ConstGame.MENU_MIDDLE_BUTTON, ConstGame.MENU_MIDDLE_BUTTON + 2.8f);
        shopButton.setPosition(ConstGame.X / 2 - ConstGame.MENU_MIDDLE_BUTTON / 2 - ConstGame.MENU_PLAY_BUTTON + 0.6f, 0.8f);
        shopButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mMainMenuGui.setPosition(0, -ConstGame.Y);
                mShopGui.addAction(Actions.moveTo(0, 0, ConstGame.MENU_PANELS_SPEED));
            }
        });

        final Button.ButtonStyle scoreButtonStyle = new Button.ButtonStyle();
        scoreButtonStyle.down = mSkin.getDrawable("button_score_2");
        scoreButtonStyle.up = mSkin.getDrawable("button_score_1");
        final Button scoreButton = new Button(scoreButtonStyle);
        scoreButton.setSize(ConstGame.MENU_MIDDLE_BUTTON, ConstGame.MENU_MIDDLE_BUTTON + 2.8f);
        scoreButton.setPosition(ConstGame.X / 2 + ConstGame.MENU_PLAY_BUTTON - ConstGame.MENU_MIDDLE_BUTTON / 2 - 0.6f, 0.8f);
        scoreButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                if (playServices.isOnline()) {
                    if (!playServices.checkSignIn()) {
                        playServices.setIndex();
                        playServices.startSignInIntent();
                    } else {
                        playServices.submitLeaderboard(mOptions.getScore());
                        playServices.getLeaderboard();
                    }
                }
            }
        });

        final Button.ButtonStyle playButtonStyle = new Button.ButtonStyle();
        playButtonStyle.down = mSkin.getDrawable("button_play_2");
        playButtonStyle.up = mSkin.getDrawable("button_play_1");
        final Button playButton = new Button(playButtonStyle);
        playButton.setSize(ConstGame.MENU_PLAY_BUTTON, ConstGame.MENU_PLAY_BUTTON + 3);
        playButton.setPosition(ConstGame.X / 2 - ConstGame.MENU_PLAY_BUTTON / 2, 0.8f);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mGame.setScreen(new GameModeScreen(mGame));
            }
        });

        final Button.ButtonStyle settingsButtonStyle = new Button.ButtonStyle();
        settingsButtonStyle.down = mSkin.getDrawable("button_settings_2");
        settingsButtonStyle.up = mSkin.getDrawable("button_settings_1");
        final Button settingsButton = new Button(settingsButtonStyle);
        settingsButton.setSize(ConstGame.MENU_LOW_BUTTON, ConstGame.MENU_LOW_BUTTON + 1.5f);
        settingsButton.setPosition(0.6f, ConstGame.Y - 5.4f);
        settingsButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mMainMenuGui.setPosition(0, -ConstGame.Y);
                mSettingsGui.addAction(Actions.moveTo(0, 0, ConstGame.MENU_PANELS_SPEED));
            }
        });

        final Button.ButtonStyle trophyButtonStyle = new Button.ButtonStyle();
        trophyButtonStyle.down = mSkin.getDrawable("button_trophy_2");
        trophyButtonStyle.up = mSkin.getDrawable("button_trophy_1");
        final Button trophyButton = new Button(trophyButtonStyle);
        trophyButton.setSize(ConstGame.MENU_LOW_BUTTON, ConstGame.MENU_LOW_BUTTON + 1.5f);
        trophyButton.setPosition(0.6f, ConstGame.Y - 9.9f);
        trophyButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mMainMenuGui.setPosition(0, -ConstGame.Y);
                mTrophyGui.addAction(Actions.moveTo(0, 0, ConstGame.MENU_PANELS_SPEED));
            }
        });

        final Button.ButtonStyle exitButtonStyle = new Button.ButtonStyle();
        exitButtonStyle.down = mSkin.getDrawable("button_close_2");
        exitButtonStyle.up = mSkin.getDrawable("button_close_1");
        final Button exitButton = new Button(exitButtonStyle);
        exitButton.setSize(ConstGame.MENU_LOW_BUTTON, ConstGame.MENU_LOW_BUTTON + 1.5f);
        exitButton.setPosition(ConstGame.X - ConstGame.MENU_LOW_BUTTON - 0.6f, ConstGame.Y - 5.4f);
        exitButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                Gdx.app.exit();
                DefenderOfNature.manager.dispose();
            }
        });

        mMainMenuGui.addActor(exitButton);
        mMainMenuGui.addActor(scoreButton);
        mMainMenuGui.addActor(shopButton);
        mMainMenuGui.addActor(playButton);
        mMainMenuGui.addActor(settingsButton);
        mMainMenuGui.addActor(trophyButton);
        addActor(mMainMenuGui);
    }

    private void createSettingsGui() {
        mSettingsGui = new Group();
        mSettingsGui.setBounds(0, ConstGame.Y, ConstGame.X, ConstGame.Y);

        final Image panel = new Image(mSkin.getDrawable("settings_frame"));
        panel.setBounds(ConstGame.X / 2 - ConstGame.MENU_PANEL_WIDTH / 2, ConstGame.Y - ConstGame.MENU_PANEL_HEIGHT, ConstGame.MENU_PANEL_WIDTH, ConstGame.MENU_PANEL_HEIGHT);
        mSettingsGui.addActor(panel);

        mMusicButtonStyleOn = new Button.ButtonStyle();
        mMusicButtonStyleOn.down = mSkin.getDrawable("button_music_2");
        mMusicButtonStyleOn.up = mSkin.getDrawable("button_music_1");
        mMusicButtonStyleOff = new Button.ButtonStyle();
        mMusicButtonStyleOff.down = mSkin.getDrawable("button_music_2");
        mMusicButtonStyleOff.up = mSkin.getDrawable("button_music_3");
        mMusicButton = new Button();
        mMusicButton.setBounds(ConstGame.X / 2 - ConstGame.SETTINGS_BOX_BUTTON + 0.3f, ConstGame.Y / 2 + 2.75f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
        mMusicButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getMusicCheck()) {
                    mOptions.setMusicCheck(false);
                    DefenderOfNature.musicManager.pause();
                } else {
                    mOptions.setMusicCheck(true);
                    DefenderOfNature.musicManager.play();
                }
                if (mOptions.getSoundCheck() && mOptions.getMusicCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                } else if (mOptions.getSoundCheck() && !mOptions.getMusicCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
            }
        });
        mSettingsGui.addActor(mMusicButton);

        mSoundButtonStyleOn = new Button.ButtonStyle();
        mSoundButtonStyleOn.down = mSkin.getDrawable("button_sound_2");
        mSoundButtonStyleOn.up = mSkin.getDrawable("button_sound_1");
        mSoundButtonStyleOff = new Button.ButtonStyle();
        mSoundButtonStyleOff.down = mSkin.getDrawable("button_sound_2");
        mSoundButtonStyleOff.up = mSkin.getDrawable("button_sound_3");
        mSoundButton = new Button();
        mSoundButton.setBounds(ConstGame.X / 2 + 0.3f, ConstGame.Y / 2 + 2.75f, ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 0.8f);
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
        mSettingsGui.addActor(mSoundButton);

        final Table musicVolumes = new Table();
        musicVolumes.setBounds(ConstGame.X / 2 + 0.4f, ConstGame.Y / 2 + 0.3f, 5, 2);
        musicVolumes.left();
        for (int i = 0; i < 5; i++) {
            musicVolumes.add().width(1).height(2);
        }
        for (int i = 0; i < mOptions.getMusicVolume() * 10 / 2; i++) {
            musicVolumes.getCells().get(i).setActor(new Image(mSkin.getDrawable("volume_indecator"))).width(1).height(2);
        }
        mSettingsGui.addActor(musicVolumes);

        final Button.ButtonStyle plusMusicStyle = new Button.ButtonStyle();
        plusMusicStyle.down = mSkin.getDrawable("button_plus_2");
        plusMusicStyle.up = mSkin.getDrawable("button_plus_1");
        final Button plusMusicVolume = new Button(plusMusicStyle);
        plusMusicVolume.setBounds(ConstGame.X / 2 + 5.65f, ConstGame.Y / 2 - 0.1f, ConstGame.SETTINGS_BOX_BUTTON_LOW, ConstGame.SETTINGS_BOX_BUTTON_LOW + 1);
        plusMusicVolume.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                if (musicVolumes.getChildren().size < 5) {
                    musicVolumes.getCells().get(musicVolumes.getChildren().size).setActor(new Image(mSkin.getDrawable("volume_indecator")));
                }
                mOptions.setMusicVolume((float) musicVolumes.getChildren().size * 2 / 10);
                DefenderOfNature.musicManager.setVolume((float) musicVolumes.getChildren().size * 2 / 10);
            }
        });
        mSettingsGui.addActor(plusMusicVolume);

        final Button.ButtonStyle minusMusicStyle = new Button.ButtonStyle();
        minusMusicStyle.down = mSkin.getDrawable("button_minus_2");
        minusMusicStyle.up = mSkin.getDrawable("button_minus_1");
        final Button minusMusicVolume = new Button(minusMusicStyle);
        minusMusicVolume.setBounds(ConstGame.X / 2 - 2, ConstGame.Y / 2 - 0.1f, ConstGame.SETTINGS_BOX_BUTTON_LOW, ConstGame.SETTINGS_BOX_BUTTON_LOW + 1);
        minusMusicVolume.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                if (musicVolumes.getChildren().size > 1) {
                    musicVolumes.getChildren().removeIndex(musicVolumes.getChildren().size - 1);
                }
                mOptions.setMusicVolume((float) musicVolumes.getChildren().size * 2 / 10);
                DefenderOfNature.musicManager.setVolume((float) musicVolumes.getChildren().size * 2 / 10);
            }
        });
        mSettingsGui.addActor(minusMusicVolume);

        final Table soundVolumes = new Table();
        soundVolumes.setBounds(ConstGame.X / 2 + 0.4f, ConstGame.Y / 2 - 2.8f, 5, 2);
        soundVolumes.left();
        for (int i = 0; i < 5; i++) {
            soundVolumes.add().width(1).height(2);
        }
        for (int i = 0; i < mOptions.getSoundVolume() * 10 / 2; i++) {
            soundVolumes.getCells().get(i).setActor(new Image(mSkin.getDrawable("volume_indecator"))).width(1).height(2);
        }
        mSettingsGui.addActor(soundVolumes);

        final Button.ButtonStyle plusSoundStyle = new Button.ButtonStyle();
        plusSoundStyle.down = mSkin.getDrawable("button_plus_2");
        plusSoundStyle.up = mSkin.getDrawable("button_plus_1");
        final Button plusSoundVolume = new Button(plusSoundStyle);
        plusSoundVolume.setBounds(ConstGame.X / 2 + 5.65f, ConstGame.Y / 2 - 3.2f, ConstGame.SETTINGS_BOX_BUTTON_LOW, ConstGame.SETTINGS_BOX_BUTTON_LOW + 0.8f);
        plusSoundVolume.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (soundVolumes.getChildren().size < 5) {
                    soundVolumes.getCells().get(soundVolumes.getChildren().size).setActor(new Image(mSkin.getDrawable("volume_indecator")));
                }
                mOptions.setSoundVolume((float) soundVolumes.getChildren().size * 2 / 10);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
            }
        });
        mSettingsGui.addActor(plusSoundVolume);

        final Button.ButtonStyle minusSoundStyle = new Button.ButtonStyle();
        minusSoundStyle.down = mSkin.getDrawable("button_minus_2");
        minusSoundStyle.up = mSkin.getDrawable("button_minus_1");
        final Button minusSoundVolume = new Button(minusSoundStyle);
        minusSoundVolume.setBounds(ConstGame.X / 2 - 2, ConstGame.Y / 2 - 3.2f, ConstGame.SETTINGS_BOX_BUTTON_LOW, ConstGame.SETTINGS_BOX_BUTTON_LOW + 0.8f);
        minusSoundVolume.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (soundVolumes.getChildren().size > 1) {
                    soundVolumes.getChildren().removeIndex(soundVolumes.getChildren().size - 1);
                }
                mOptions.setSoundVolume((float) soundVolumes.getChildren().size * 2 / 10);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
            }
        });
        mSettingsGui.addActor(minusSoundVolume);

        mEffectButtonStyleOn = new Button.ButtonStyle();
        mEffectButtonStyleOn.down = mSkin.getDrawable("button_check_2");
        mEffectButtonStyleOn.up = mSkin.getDrawable("button_check_1");
        mEffectButtonStyleOff = new Button.ButtonStyle();
        mEffectButtonStyleOff.down = mSkin.getDrawable("button_check_2");
        mEffectButtonStyleOff.up = mSkin.getDrawable("button_check_3");
        mEffectButton = new Button();
        mEffectButton.setBounds(ConstGame.X / 2 - 2, ConstGame.Y / 2 - 6.4f, ConstGame.SETTINGS_BOX_BUTTON_LOW, ConstGame.SETTINGS_BOX_BUTTON_LOW + 0.8f);
        mEffectButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getEffectCheck()) {
                    mOptions.setEffectCheck(false);
                } else {
                    mOptions.setEffectCheck(true);
                }
                if (mOptions.getSoundCheck() && !mOptions.getEffectCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                } else if (mOptions.getSoundCheck() && mOptions.getEffectCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
            }
        });
        mSettingsGui.addActor(mEffectButton);

        final Button.ButtonStyle signInButtontyle = new Button.ButtonStyle();
        signInButtontyle.down = new TextureRegionDrawable(new TextureRegion(DefenderOfNature.manager.get("atlases/gui/google_brn_prs.png", Texture.class)));
        signInButtontyle.up = new TextureRegionDrawable(new TextureRegion(DefenderOfNature.manager.get("atlases/gui/google_btn_nrm.png", Texture.class)));
        Button signInButton = new Button(signInButtontyle);
        signInButton.setSize(ConstGame.SETTINGS_BOX_BUTTON_LOW - 0.4f, ConstGame.SETTINGS_BOX_BUTTON_LOW);
        signInButton.setPosition(ConstGame.X / 2 + 5.9f, ConstGame.Y / 2 - 6);
        signInButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                if (!playServices.checkSignIn()) {
                    playServices.setIndex();
                    playServices.startSignInIntent();
                } else {
                    playServices.signOut();
                }

            }
        });
        mSettingsGui.addActor(signInButton);

        addActor(mSettingsGui);
    }

    private void createShopGui() {
        mShopGui = new Group();
        mShopGui.setBounds(0, ConstGame.Y, ConstGame.X, ConstGame.Y);

        final Image panel = new Image(mSkin.getDrawable("shop_frame"));
        panel.setBounds(ConstGame.X / 2 - ConstGame.MENU_PANEL_WIDTH / 2, ConstGame.Y - ConstGame.MENU_PANEL_HEIGHT, ConstGame.MENU_PANEL_WIDTH, ConstGame.MENU_PANEL_HEIGHT);
        mShopGui.addActor(panel);

        mGroups = new Group[6];
        int id = 0;
        float positionX;
        for (int i = 0; i < 6; i++) {
            mGroups[i] = new Group();
            mGroups[i].setBounds(ConstGame.X / 2 - 7, ConstGame.Y / 2 - 5.9f, 9, 4);
            mGroups[i].setVisible(false);
            positionX = 0;

            for (int j = 0; j < 2; j++) {
                final ShopButton shopButton = new ShopButton(playServices, id, mOptions, mSkin);
                shopButton.setBounds(positionX, 0, 7.5f, 11);
                mGroups[i].addActor(shopButton);
                positionX += 7.5f;
                id++;
            }
            mShopGui.addActor(mGroups[i]);
        }
        mGroups[0].setVisible(true);

        final Image coinsImage = new Image(mSkin.getDrawable("coins"));
        coinsImage.setBounds(ConstGame.X / 2 - 0.15f, ConstGame.Y / 2 - 5.8f, 1.3f, 2.5f);
        mShopGui.addActor(coinsImage);

        mCoinsText = new TextGame(mOptions, ConstGame.X / 2 + 0.6f, ConstGame.Y / 2 - 6);
        mShopGui.addActor(mCoinsText);

        mPages = 0;
        final Button.ButtonStyle nextButtonStyle = new Button.ButtonStyle();
        nextButtonStyle.down = mSkin.getDrawable("button_play_2");
        nextButtonStyle.up = mSkin.getDrawable("button_play_1");
        final Button nextButton = new Button(nextButtonStyle);
        nextButton.setSize(3, 4.7f);
        nextButton.setPosition(ConstGame.X / 2 + 8.68f, ConstGame.Y / 2 - 3);
        nextButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                switch (mPages) {
                    case 0:
                        mGroups[0].setVisible(false);
                        mGroups[1].setVisible(true);
                        mPreviousButton.setDisabled(false);
                        mPreviousButton.setVisible(true);
                        break;
                    case 1:
                        mGroups[1].setVisible(false);
                        mGroups[2].setVisible(true);
                        break;
                    case 2:
                        mGroups[2].setVisible(false);
                        mGroups[3].setVisible(true);
                        break;
                    case 3:
                        mGroups[3].setVisible(false);
                        mGroups[4].setVisible(true);
                        break;
                    case 4:
                        mGroups[4].setVisible(false);
                        mGroups[5].setVisible(true);
                        nextButton.setDisabled(true);
                        nextButton.setVisible(false);
                        break;
                }
                mPages++;
            }
        });
        mShopGui.addActor(nextButton);

        final Button.ButtonStyle previousButtonStyle = new Button.ButtonStyle();
        previousButtonStyle.down = mSkin.getDrawable("button_goleft_2");
        previousButtonStyle.up = mSkin.getDrawable("button_goleft_1");
        mPreviousButton = new Button(previousButtonStyle);
        mPreviousButton.setSize(3, 4.7f);
        mPreviousButton.setPosition(ConstGame.X / 2 - 11, ConstGame.Y / 2 - 3);
        mPreviousButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                switch (mPages) {
                    case 5:
                        mGroups[5].setVisible(false);
                        mGroups[4].setVisible(true);
                        nextButton.setDisabled(false);
                        nextButton.setVisible(true);
                        break;
                    case 4:
                        mGroups[4].setVisible(false);
                        mGroups[3].setVisible(true);
                        break;
                    case 3:
                        mGroups[3].setVisible(false);
                        mGroups[2].setVisible(true);
                        break;
                    case 2:
                        mGroups[2].setVisible(false);
                        mGroups[1].setVisible(true);
                        break;
                    case 1:
                        mPreviousButton.setDisabled(true);
                        mPreviousButton.setVisible(false);
                        mGroups[1].setVisible(false);
                        mGroups[0].setVisible(true);
                        break;
                }
                mPages--;
            }
        });
        mShopGui.addActor(mPreviousButton);
        mPreviousButton.setVisible(false);
        addActor(mShopGui);
    }

    private void createTrophyGui() {
        mTrophyGui = new Group();
        mTrophyGui.setBounds(0, ConstGame.Y, ConstGame.X, ConstGame.Y);

        final Image panel = new Image(mSkin.getDrawable("achievements_frame"));
        panel.setBounds(ConstGame.X / 2 - ConstGame.MENU_PANEL_WIDTH / 2, ConstGame.Y - ConstGame.MENU_PANEL_HEIGHT, ConstGame.MENU_PANEL_WIDTH, ConstGame.MENU_PANEL_HEIGHT);
        mTrophyGui.addActor(panel);

        mScoreTrophy = new TextGame(String.valueOf(mOptions.getScore()), ConstGame.TEXT_HIGH_POS_X, ConstGame.TEXT_HIGH_POS_Y);
        mTrophyGui.addActor(mScoreTrophy);
        mChallengesTrophy = new TextGame(String.valueOf(mOptions.getChallengesCount()), ConstGame.TEXT_LOW_POS_X, ConstGame.TEXT_LOW_POS_Y);
        mTrophyGui.addActor(mChallengesTrophy);
        mWaveTrophy = new TextGame(String.valueOf(mOptions.getWavesCount()), ConstGame.TEXT_LOW_POS_X, ConstGame.TEXT_HIGH_POS_Y);
        mTrophyGui.addActor(mWaveTrophy);
        mEnemiesTrophy = new TextGame(String.valueOf(mOptions.getEnemiesCount()), ConstGame.TEXT_HIGH_POS_X, ConstGame.TEXT_LOW_POS_Y);
        mTrophyGui.addActor(mEnemiesTrophy);

        final Button.ButtonStyle bookButtonStyle = new Button.ButtonStyle();
        bookButtonStyle.down = mSkin.getDrawable("book_2");
        bookButtonStyle.up = mSkin.getDrawable("book_1");
        final Button bookButton = new Button(bookButtonStyle);
        bookButton.setSize(ConstGame.SETTINGS_BOX_BUTTON, ConstGame.SETTINGS_BOX_BUTTON + 1);
        bookButton.setPosition(ConstGame.X / 2 - 0.7f, ConstGame.Y / 2 - 2.8f);
        bookButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mGame.setScreen(new SplashScreen(mGame, ConstGame.SECOND_LOADING));
            }
        });
        mTrophyGui.addActor(bookButton);
        addActor(mTrophyGui);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        mCoinsText.act(delta);
        if (mMainMenuGui.getY() == -ConstGame.Y) {
            mBackButton.setPosition(0.6f, ConstGame.Y - 5.4f);
        } else if (mMainMenuGui.getY() == 0) {
            mBackButton.setPosition(0.6f, ConstGame.Y);
        }
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
        if (mOptions.getEffectCheck()) {
            mEffectButton.setStyle(mEffectButtonStyleOn);
        } else {
            mEffectButton.setStyle(mEffectButtonStyleOff);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        mScoreTrophy.dispose();
        mWaveTrophy.dispose();
        mEnemiesTrophy.dispose();
        mChallengesTrophy.dispose();
        mSkin.dispose();
    }
}