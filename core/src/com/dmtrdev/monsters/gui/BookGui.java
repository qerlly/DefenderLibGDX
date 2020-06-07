package com.dmtrdev.monsters.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.MenuScreen;
import com.dmtrdev.monsters.utils.Options;

public class BookGui extends Stage {

    private byte mPages;
    private Group[] groups;
    private final Game mGame;
    private final Skin mSkin;
    private final Options mOptions;
    private final Button mPreviousButton;

    public BookGui(final Game pGame) {
        super(new StretchViewport(ConstGame.X, ConstGame.Y));
        mPages = 0;
        mGame = pGame;
        mSkin = new Skin();
        mOptions = new Options();
        mSkin.addRegions(DefenderOfNature.getGuiAtlas());
        mSkin.addRegions(DefenderOfNature.manager.get("atlases/book/book_atlas.txt", TextureAtlas.class));
        final Image image = new Image(mSkin.getDrawable("book_frame_1"));
        image.setBounds(-3, -3, ConstGame.X + 6, ConstGame.Y + 6);
        addActor(image);
        createBookCards();
        groups[0].setPosition(0, 0);

        final Button.ButtonStyle playButtonStyle = new Button.ButtonStyle();
        playButtonStyle.down = mSkin.getDrawable("button_right_2");
        playButtonStyle.up = mSkin.getDrawable("button_right_1");
        final Button playButton = new Button(playButtonStyle);
        playButton.setBounds(ConstGame.X / 2 + ConstGame.BUTTONS_SIZES / 2 + 0.3f, 1.5f, ConstGame.BUTTONS_SIZES, ConstGame.BUTTONS_SIZES + 0.6f);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mOptions.getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mOptions.getSoundVolume());
                }
                switch (mPages) {
                    case 0:
                        groups[0].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[1].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        mPreviousButton.setDisabled(false);
                        mPreviousButton.setVisible(true);
                        break;
                    case 1:
                        groups[1].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[2].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 2:
                        groups[2].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[3].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 3:
                        groups[3].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[4].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 4:
                        groups[4].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[5].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 5:
                        groups[5].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[6].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 6:
                        groups[6].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[7].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 7:
                        groups[7].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[8].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 8:
                        groups[8].addAction(Actions.moveTo(-ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[9].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        playButton.setDisabled(true);
                        playButton.setVisible(false);
                        break;
                }
                mPages++;
            }
        });
        addActor(playButton);

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
                switch (mPages) {
                    case 9:
                        groups[9].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[8].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        playButton.setDisabled(false);
                        playButton.setVisible(true);
                        break;
                    case 8:
                        groups[8].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[7].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 7:
                        groups[7].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[6].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 6:
                        groups[6].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[5].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 5:
                        groups[5].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[4].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 4:
                        groups[4].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[3].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 3:
                        groups[3].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[2].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 2:
                        groups[2].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[1].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                    case 1:
                        mPreviousButton.setDisabled(true);
                        mPreviousButton.setVisible(false);
                        groups[1].addAction(Actions.moveTo(ConstGame.X, 0, ConstGame.PANELS_SPEED));
                        groups[0].addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                        break;
                }
                mPages--;
            }
        });
        addActor(mPreviousButton);
        mPreviousButton.setVisible(false);

        final Button.ButtonStyle closeButtonStyle = new Button.ButtonStyle();
        closeButtonStyle.down = mSkin.getDrawable("button_close_4");
        closeButtonStyle.up = mSkin.getDrawable("button_close_3");
        final Button closeButton = new Button(closeButtonStyle);
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
    }

    private void createBookCards() {
        byte j = 0;
        groups = new Group[10];
        for (int i = 0; i < 10; i++) {
            groups[i] = new Group();
            groups[i].setBounds(ConstGame.X, 0, ConstGame.X, ConstGame.Y);

            final Image frame = new Image(mSkin.getDrawable("book_frame_2"));
            frame.setBounds(0.5f, 0.5f, ConstGame.FRAME_WIDTH, ConstGame.FRAME_HEIGHT);
            groups[i].addActor(frame);

            final Image image = new Image();
            if ((mOptions.getStoryLevel() + 1) * 4 > j) {
                image.setDrawable(mSkin.getDrawable(String.valueOf(j)));
            } else {
                image.setDrawable(mSkin.getDrawable("frame_lock"));
            }
            image.setBounds(2.6f, ConstGame.Y - ConstGame.PANELS_HEIGHT - 0.1f, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
            groups[i].addActor(image);
            j++;

            final Image image1 = new Image();
            if ((mOptions.getStoryLevel() + 1) * 4 > j) {
                image1.setDrawable(mSkin.getDrawable(String.valueOf(j)));
            } else {
                image1.setDrawable(mSkin.getDrawable("frame_lock"));
            }
            image1.setBounds(ConstGame.X - ConstGame.PANELS_WIDTH - 2.7f, ConstGame.Y - ConstGame.PANELS_HEIGHT - 0.1f, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
            groups[i].addActor(image1);
            j++;

            if (j <= 36) {
                final Image image2 = new Image();
                if ((mOptions.getStoryLevel() + 1) * 4 > j) {
                    image2.setDrawable(mSkin.getDrawable(String.valueOf(j)));
                } else {
                    image2.setDrawable(mSkin.getDrawable("frame_lock"));
                }
                image2.setBounds(2.6f, 4.1f, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
                groups[i].addActor(image2);
                j++;

                final Image image3 = new Image();
                if ((mOptions.getStoryLevel() + 1) * 4 > j) {
                    image3.setDrawable(mSkin.getDrawable(String.valueOf(j)));
                } else {
                    image3.setDrawable(mSkin.getDrawable("frame_lock"));
                }
                image3.setBounds(ConstGame.X - ConstGame.PANELS_WIDTH - 2.7f, 4.1f, ConstGame.PANELS_WIDTH, ConstGame.PANELS_HEIGHT);
                groups[i].addActor(image3);
                j++;
            }
            addActor(groups[i]);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        mSkin.dispose();
        groups = null;
        DefenderOfNature.manager.unload("atlases/book/book_atlas.txt");
    }
}