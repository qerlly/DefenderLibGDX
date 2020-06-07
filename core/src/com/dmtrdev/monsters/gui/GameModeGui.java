package com.dmtrdev.monsters.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.MenuScreen;
import com.dmtrdev.monsters.screens.SplashScreen;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.utils.Options;
import com.dmtrdev.monsters.utils.TextGame;

public class GameModeGui extends Stage {

    private final Game mGame;
    private final Skin mSkin;
    private final Options mOptions;
    private final Button mPreviousButton;
    private Group mModeGroup, mLevelsGroup, mChallengeGroup;
    private TextGame mStoryText, mSurviveText, mChallengeText;

    public GameModeGui(final Game pGame) {
        super(new StretchViewport(ConstGame.X, ConstGame.Y));
        mGame = pGame;
        mOptions = new Options();
        mSkin = new Skin();
        mSkin.addRegions(DefenderOfNature.getGuiAtlas());

        final Image image = new Image(mSkin.getDrawable("book_frame_1"));
        image.setBounds(-3, -3, ConstGame.X + 6, ConstGame.Y + 6);
        addActor(image);

        createModeFrame();
        createLevelsGroup();
        createChallengeGroup();

        final Button.ButtonStyle closeButtonButtonStyle = new Button.ButtonStyle();
        closeButtonButtonStyle.down = mSkin.getDrawable("button_close_4");
        closeButtonButtonStyle.up = mSkin.getDrawable("button_close_3");
        final Button closeButton = new Button(closeButtonButtonStyle);
        closeButton.setBounds(ConstGame.X / 2 - ConstGame.BUTTONS_SIZES / 2, 1.5f, ConstGame.BUTTONS_SIZES, ConstGame.BUTTONS_SIZES + 0.6f);
        closeButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                mGame.setScreen(new MenuScreen(mGame));
            }
        });
        addActor(closeButton);

        final Button.ButtonStyle previousButtonStyle = new Button.ButtonStyle();
        previousButtonStyle.down = mSkin.getDrawable("button_left_2");
        previousButtonStyle.up = mSkin.getDrawable("button_left_1");
        mPreviousButton = new Button(previousButtonStyle);
        mPreviousButton.setBounds(ConstGame.X / 2 - (ConstGame.BUTTONS_SIZES * 3) / 2 - 0.3f, 1.5f, ConstGame.BUTTONS_SIZES, ConstGame.BUTTONS_SIZES + 0.6f);
        mPreviousButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mOptions.getSoundVolume());
                }
                mLevelsGroup.addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                mChallengeGroup.addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                mModeGroup.addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                mPreviousButton.setVisible(false);

            }
        });
        addActor(mPreviousButton);
        mPreviousButton.setVisible(false);
    }

    private void createModeFrame() {
        mModeGroup = new Group();
        mModeGroup.setBounds(0, 0, ConstGame.X, ConstGame.Y);

        final Image image = new Image(mSkin.getDrawable("book_frame_2"));
        image.setBounds(0.5f, 0.5f, ConstGame.FRAME_WIDTH, ConstGame.FRAME_HEIGHT);
        mModeGroup.addActor(image);

        final Button.ButtonStyle storyButtonStyle = new Button.ButtonStyle();
        storyButtonStyle.down = mSkin.getDrawable("story_mode_2");
        storyButtonStyle.up = mSkin.getDrawable("story_mode_1");
        final Button storyButton = new Button(storyButtonStyle);
        storyButton.setBounds(ConstGame.X / 2 - ConstGame.PANELS_WIDTH / 2, ConstGame.Y - ConstGame.PANELS_HEIGHT, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
        storyButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mModeGroup.addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                mLevelsGroup.addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                mPreviousButton.setVisible(true);
            }
        });
        mModeGroup.addActor(storyButton);
        mStoryText = new TextGame(mOptions.getStoryLevel() + " / " + ConstGame.LEVELS_SIZE, ConstGame.X / 2 + 1.51f, ConstGame.Y - 3.83f);
        mModeGroup.addActor(mStoryText);

        final Button.ButtonStyle survivalButtonStyle = new Button.ButtonStyle();
        survivalButtonStyle.down = mSkin.getDrawable("survive_mode_2");
        survivalButtonStyle.up = mSkin.getDrawable("survive_mode_1");
        final Button survivalButton = new Button(survivalButtonStyle);
        survivalButton.setBounds(ConstGame.X - ConstGame.PANELS_WIDTH - 2.7f, 4.1f, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
        survivalButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mGame.setScreen(new SplashScreen(mGame, ConstGame.SURVIVE_MODE));
            }
        });
        mModeGroup.addActor(survivalButton);
        mSurviveText = new TextGame(String.valueOf(mOptions.getWavesCount()), ConstGame.X - 7.49f, 9.48f);
        mModeGroup.addActor(mSurviveText);

        final Button.ButtonStyle challengeButtonStyle = new Button.ButtonStyle();
        challengeButtonStyle.down = mSkin.getDrawable("challenge_mode_2");
        challengeButtonStyle.up = mSkin.getDrawable("challenge_mode_1");
        final Button challengeButton = new Button(challengeButtonStyle);
        challengeButton.setBounds(2.6f, 4.1f, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
        challengeButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                mModeGroup.addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                mChallengeGroup.addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                mPreviousButton.setVisible(true);

            }
        });
        mModeGroup.addActor(challengeButton);
        mChallengeText = new TextGame(mOptions.getChallengesCount() + " / " + 5, 10.34f, 9.48f);
        mModeGroup.addActor(mChallengeText);
        addActor(mModeGroup);
    }

    private void createLevelsGroup() {
        mLevelsGroup = new Group();
        mLevelsGroup.setBounds(ConstGame.X, 0, ConstGame.X, ConstGame.Y);

        final Image image = new Image(mSkin.getDrawable("book_frame_2"));
        image.setBounds(0.5f, 0.5f, ConstGame.FRAME_WIDTH, ConstGame.FRAME_HEIGHT);
        mLevelsGroup.addActor(image);

        final Table table = new Table();
        table.setBounds(ConstGame.X / 2 - ConstGame.MODE_PANEL / 2 - 2.1f, ConstGame.Y / 2 - 3, ConstGame.MODE_PANEL, ConstGame.MODE_PANEL);
        table.left().top();
        byte count = 0;
        final Button[] buttons = new Button[ConstGame.LEVELS_SIZE];
        for (int i = 0; i < ConstGame.LEVELS_SIZE; i++) {
            if (count == 4) {
                table.row();
                count = 0;
            }
            if(i == 8){
                table.add();
            }
            final Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttons[i] = new Button();
            if (mOptions.getStoryLevel() > i) {
                buttonStyle.down = mSkin.getDrawable("complete_level_2");
                buttonStyle.up = mSkin.getDrawable("complete_level_1");
            } else if (mOptions.getStoryLevel() == i) {
                buttonStyle.down = mSkin.getDrawable("no_level_2");
                buttonStyle.up = mSkin.getDrawable("no_level_1");
            } else {
                buttonStyle.up = mSkin.getDrawable("level_locked");
                buttons[i].setName("locked");
            }
            buttons[i].setStyle(buttonStyle);
            if (!"locked".equals(buttons[i].getName())) {
                final int j = i;
                buttons[i].addListener(new ClickListener() {

                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        super.clicked(event, x, y);
                        if (mOptions.getSoundCheck()) {
                            DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                        }
                        mGame.setScreen(new SplashScreen(mGame, j + ConstGame.STORY_MODE));
                    }
                });
            }
            table.add(buttons[i]).width(ConstGame.MODE_LEVEL).height(ConstGame.MODE_LEVEL);
            count++;
        }
        mLevelsGroup.addActor(table);
        addActor(mLevelsGroup);
    }

    private void createChallengeGroup() {
        mChallengeGroup = new Group();
        mChallengeGroup.setBounds(ConstGame.X, 0, ConstGame.X, ConstGame.Y);

        final Image image = new Image(mSkin.getDrawable("book_frame_2"));
        image.setBounds(0.5f, 0.5f, ConstGame.FRAME_WIDTH, ConstGame.FRAME_HEIGHT);
        mChallengeGroup.addActor(image);

        final Table table = new Table();
        table.setBounds(ConstGame.X / 2 - 10, ConstGame.Y / 2 - 9, ConstGame.MODE_PANEL, ConstGame.MODE_PANEL);
        table.left().top();

        final Button[] buttons = new Button[5];
        for (int i = 0; i < 5; i++) {
            final Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
            buttons[i] = new Button();
            if (mOptions.getChallengesCount() > i) {
                buttonStyle.down = mSkin.getDrawable("complete_level_2");
                buttonStyle.up = mSkin.getDrawable("complete_level_1");
            } else if (mOptions.getChallengesCount() == i) {
                buttonStyle.down = mSkin.getDrawable("no_level_2");
                buttonStyle.up = mSkin.getDrawable("no_level_1");
            } else {
                buttonStyle.up = mSkin.getDrawable("level_locked");
                buttons[i].setName("locked");
            }
            buttons[i].setStyle(buttonStyle);
            if (!"locked".equals(buttons[i].getName())) {
                final int j = i;
                buttons[i].addListener(new ClickListener() {

                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        super.clicked(event, x, y);
                        if (mOptions.getSoundCheck()) {
                            DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                        }
                        mGame.setScreen(new SplashScreen(mGame, j + ConstGame.CHALLENGE_MODE));
                    }
                });
            }
            table.add(buttons[i]).width(ConstGame.MODE_LEVEL).height(ConstGame.MODE_LEVEL);
        }
        mChallengeGroup.addActor(table);
        addActor(mChallengeGroup);
    }

    @Override
    public void dispose() {
        super.dispose();
        mStoryText.dispose();
        mSurviveText.dispose();
        mChallengeText.dispose();
        mSkin.dispose();
    }
}