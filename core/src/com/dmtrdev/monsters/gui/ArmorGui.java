package com.dmtrdev.monsters.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.GameModeScreen;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.utils.ArmorButton;
import com.dmtrdev.monsters.utils.ArmorTable;

public class ArmorGui extends Stage {

    private final PlayScreen mPlayScreen;
    private ArmorTable mArmorTable;
    private Group mArmorGroup;
    private Group mPlayerGroup;
    private boolean mFlag, mFlagMode;
    private byte mPlayerId;

    public ArmorGui(final Game pGame, final PlayScreen pPlayScreen, final boolean pFlagMode) {
        super(new StretchViewport(ConstGame.X, ConstGame.Y));
        mPlayerId = 0;
        mPlayScreen = pPlayScreen;
        mFlag = false;
        mFlagMode = pFlagMode;
        final Image image = new Image(pPlayScreen.getSkin().getDrawable("book_frame_1"));
        image.setBounds(-3, -3, ConstGame.X + 6, ConstGame.Y + 6);
        addActor(image);
        createPlayerAndTreeFrame();
        if (!mFlagMode) {
            createArmorFrame();
            mArmorTable = new ArmorTable(pPlayScreen, mPlayScreen.getOptions().getAvailableArmors());
            mArmorTable.setBounds(ConstGame.X / 2 - 7.4f, 0.4f, ConstGame.ARMOR_TABLE_WIDTH, ConstGame.ARMOR_TABLE_HEIGHT);
            mArmorTable.left();
            mArmorGroup.addActor(mArmorTable);
        }

        final Button.ButtonStyle leftButtonStyle = new Button.ButtonStyle();
        leftButtonStyle.down = pPlayScreen.getSkin().getDrawable("button_goleft_2");
        leftButtonStyle.up = pPlayScreen.getSkin().getDrawable("button_goleft_1");
        final Button leftButton = new Button(leftButtonStyle);
        leftButton.setBounds(1.35f, 0.2f, 4, 5.7f);
        leftButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mPlayScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getButtonClickOff().play(mPlayScreen.getOptions().getSoundVolume());
                }
                if (!mFlag) {
                    pGame.setScreen(new GameModeScreen(pGame));
                } else if (!mFlagMode && mFlag) {
                    mArmorGroup.addAction(Actions.moveTo(ConstGame.X + 2, 0, ConstGame.PANELS_SPEED));
                    mPlayerGroup.addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                    mFlag = false;
                }
            }
        });
        addActor(leftButton);

        final Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.down = pPlayScreen.getSkin().getDrawable("button_goright_2");
        buttonStyle.up = pPlayScreen.getSkin().getDrawable("button_goright_1");
        final Button playButton = new Button(buttonStyle);
        playButton.setBounds(ConstGame.X - 5.5f, 0.2f, 4, 5.7f);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (mPlayScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mPlayScreen.getOptions().getSoundVolume());
                }
                if (mFlagMode) {
                    pPlayScreen.setGameStart(true);
                    pPlayScreen.createPlayer();
                    if (pPlayScreen.getType().equals(ConstGame.SURVIVE_MODE)) {
                        pPlayScreen.createGameGui(pGame, new ArmorTable(pPlayScreen, pPlayScreen.getOptions().getAvailableArmors()));
                    } else {
                        pPlayScreen.createGameGui(pGame, new ArmorTable(pPlayScreen, initArmorChallenge()));
                    }
                    DefenderOfNature.musicManager.changeMusic(true);
                } else if (mFlag && !mFlagMode) {
                    for (int i = 0; i < mArmorTable.getChildren().size; i++) {
                        ((ArmorButton) mArmorTable.getChildren().get(i)).flag = true;
                    }
                    pPlayScreen.setGameStart(true);
                    pPlayScreen.createPlayer();
                    pPlayScreen.createGameGui(pGame, mArmorTable);
                    DefenderOfNature.musicManager.changeMusic(true);
                } else if (!mFlagMode) {
                    mPlayerGroup.addAction(Actions.moveTo(-ConstGame.X - 1, 0, ConstGame.PANELS_SPEED));
                    mArmorGroup.addAction(Actions.moveTo(0, 0, ConstGame.PANELS_SPEED));
                    mFlag = true;
                }
            }
        });
        addActor(playButton);
    }

    private int[] initArmorChallenge() {
        int[] array;
        if(mPlayScreen.getType().contains(ConstGame.STORY_MODE)){
            array = new int[]{1, 3, 4};
        } else {
            switch (mPlayScreen.getType().charAt(0)) {
                default:
                    array = new int[]{15, 3, 13, 4, 9};
                    break;
                case '1':
                    array = new int[]{8, 107, 8};
                    break;
                case '2':
                    array = new int[]{15, 22, 15};
                    break;
                case '3':
                    array = new int[]{13, 101, 20, 17};
                    break;
                case '4':
                    array = new int[]{1, 28};
                    break;
            }
        }
        return array;
    }

    private void createPlayerAndTreeFrame() {
        mPlayerGroup = new Group();
        mPlayerGroup.setBounds(0, 0, ConstGame.X, ConstGame.Y);

        final Image image = new Image(mPlayScreen.getSkin().getDrawable("book_frame_2"));
        image.setBounds(-1, -1, ConstGame.X + 2, ConstGame.Y + 2);
        mPlayerGroup.addActor(image);

        final Button.ButtonStyle lockStyle = new Button.ButtonStyle();
        lockStyle.up = mPlayScreen.getSkin().getDrawable("default");
        final Button.ButtonStyle defStyle = new Button.ButtonStyle();
        defStyle.checked = mPlayScreen.getSkin().getDrawable("100_100");
        defStyle.up = mPlayScreen.getSkin().getDrawable("100");
        final Button defButton = new Button(defStyle);
        defButton.setChecked(true);
        final Button wallyButton = new Button();
        final Button mortyButton = new Button();

        defButton.addListener(new ClickListener() {

            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                super.clicked(event, x, y);
                if (mPlayScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getButtonClickOn().play(mPlayScreen.getOptions().getSoundVolume());
                }
                mPlayerId = 0;
                defButton.setChecked(true);
                wallyButton.setChecked(false);
                mortyButton.setChecked(false);
                mortyButton.setSize(7, 8);
                wallyButton.setSize(7, 8);
                defButton.setSize(7.5f, 8.5f);
            }
        });
        defButton.setBounds(1.4f, 7.5f, 7.5f, 8.5f);
        mPlayerGroup.addActor(defButton);

        final Button.ButtonStyle wallyStyle = new Button.ButtonStyle();
        wallyStyle.checked = mPlayScreen.getSkin().getDrawable("103_103");
        wallyStyle.up = mPlayScreen.getSkin().getDrawable("103");
        if (mPlayScreen.getOptions().getShopItems().contains("3")) {
            wallyButton.setStyle(wallyStyle);
            wallyButton.addListener(new ClickListener() {

                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    super.clicked(event, x, y);
                    if (mPlayScreen.getOptions().getSoundCheck()) {
                        DefenderOfNature.getButtonClickOn().play(mPlayScreen.getOptions().getSoundVolume());
                    }
                    mPlayerId = 1;
                    defButton.setChecked(false);
                    wallyButton.setChecked(true);
                    mortyButton.setChecked(false);
                    mortyButton.setSize(7, 8);
                    wallyButton.setSize(7.5f, 8.5f);
                    defButton.setSize(7, 8);
                }
            });
        } else {
            wallyButton.setStyle(lockStyle);
        }
        wallyButton.setBounds(10.4f, 7.5f, 7, 8);
        mPlayerGroup.addActor(wallyButton);

        final Button.ButtonStyle mortyStyle = new Button.ButtonStyle();
        mortyStyle.checked = mPlayScreen.getSkin().getDrawable("106_106");
        mortyStyle.up = mPlayScreen.getSkin().getDrawable("106");
        if (mPlayScreen.getOptions().getShopItems().contains("6")) {
            mortyButton.setStyle(mortyStyle);
            mortyButton.addListener(new ClickListener() {

                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    super.clicked(event, x, y);
                    if (mPlayScreen.getOptions().getSoundCheck()) {
                        DefenderOfNature.getButtonClickOn().play(mPlayScreen.getOptions().getSoundVolume());
                    }
                    mPlayerId = 2;
                    defButton.setChecked(false);
                    wallyButton.setChecked(false);
                    mortyButton.setChecked(true);
                    mortyButton.setSize(7.5f, 8.5f);
                    wallyButton.setSize(7, 8);
                    defButton.setSize(7, 8);
                }
            });
        } else {
            mortyButton.setStyle(lockStyle);
        }
        mortyButton.setBounds(19.4f, 7.5f, 7, 8);
        mPlayerGroup.addActor(mortyButton);
        addActor(mPlayerGroup);
    }

    private void createArmorFrame() {
        mArmorGroup = new Group();
        mArmorGroup.setBounds(ConstGame.X + 2, 0, ConstGame.X, ConstGame.Y);

        final Image image = new Image(mPlayScreen.getSkin().getDrawable("book_frame_2"));
        image.setBounds(-1, -1, ConstGame.X + 2, ConstGame.Y + 2);
        mArmorGroup.addActor(image);

        final Table table = new Table();
        table.setBounds(0.5f, ConstGame.Y / 2 - 5, ConstGame.ARMOR_PANEL_WIDTH, ConstGame.ARMOR_PANEL_HEIGHT);
        table.left().top();

        byte count = 0;
        final ArmorButton[] buttons = new ArmorButton[ConstGame.ARMORS_SIZE + 10];
        for (int i = 1; i <= ConstGame.ARMORS_SIZE; i++) {
            if (count == 9) {
                table.row();
                count = 0;
            }
            buttons[i - 1] = new ArmorButton(String.valueOf(i), mPlayScreen, i, i <= mPlayScreen.getOptions().getAvailableArmors());
            final int j = i;
            if (buttons[j - 1].available) {
                buttons[i - 1].addListener(new ClickListener() {

                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        super.clicked(event, x, y);
                        if (!buttons[j - 1].flag && buttons[j - 1].getParent() == table && mArmorTable.getChildren().size < 5) {
                            table.removeActor(buttons[j - 1]);
                            mArmorTable.add(buttons[j - 1]).height(ConstGame.ARMOR_BUTTON).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON).expand();
                            buttons[j - 1].playSound(true);
                            buttons[j - 1].setTable(mArmorTable);
                        } else if (!buttons[j - 1].flag && buttons[j - 1].getParent() == mArmorTable) {
                            mArmorTable.getCell(buttons[j - 1]).reset();
                            buttons[j - 1].playSound(false);
                            table.getCells().get(j - 1).setActor(buttons[j - 1]).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON);
                        }
                    }
                });
            }
            table.add(buttons[i - 1]).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON);
            count++;
        }
        final int[] shopArmors = {101, 102, 104, 105, 107, 108, 109, 110, 111};
        for (int i = 1; i <= 9; i++) {
            if (i == 9) {
                table.row();
                for (int j = 0; j < 4; j++) {
                    table.add();
                }
            }
            buttons[ConstGame.ARMORS_SIZE + i] = new ArmorButton(String.valueOf(shopArmors[i - 1]), mPlayScreen, shopArmors[i - 1], mPlayScreen.getOptions().getShopItems().contains(String.valueOf(shopArmors[i - 1])));
            final int j = ConstGame.ARMORS_SIZE + i;
            if (buttons[ConstGame.ARMORS_SIZE + i].available) {
                buttons[ConstGame.ARMORS_SIZE + i].addListener(new ClickListener() {

                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        super.clicked(event, x, y);
                        if (!buttons[j].flag && buttons[j].getParent() == table && mArmorTable.getChildren().size < 5) {
                            table.removeActor(buttons[j]);
                            mArmorTable.add(buttons[j]).height(ConstGame.ARMOR_BUTTON).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON).expand();
                            buttons[j].playSound(true);
                            buttons[j].setTable(mArmorTable);
                        } else if (!buttons[j].flag && buttons[j].getParent() == mArmorTable) {
                            mArmorTable.getCell(buttons[j]).reset();
                            buttons[j].playSound(false);
                            if (j == 37) {
                                table.getCells().get(j + 3).setActor(buttons[j]).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON);
                            } else {
                                table.getCells().get(j - 1).setActor(buttons[j]).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON);
                            }
                        }
                    }
                });
            }
            table.add(buttons[ConstGame.ARMORS_SIZE + i]).width(ConstGame.ARMOR_BUTTON).height(ConstGame.ARMOR_BUTTON);
        }
        mArmorGroup.addActor(table);
        addActor(mArmorGroup);
    }

    public byte getPlayerId() {
        return mPlayerId;
    }

    @Override
    public void dispose() {
        super.dispose();
        mPlayScreen.getSkin().dispose();
    }
}