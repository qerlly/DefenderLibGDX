package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.dmtrdev.monsters.DefenderOfNature;

public class ShopButton extends Button {

    private float coast;
    private boolean flag;
    private final ButtonStyle style;
    private final Skin mSkin;
    private final int mId;
    private PlayServices mPlayServices;
    private Options mOptions;

    public ShopButton(final PlayServices playServices, final int pId, final Options pOptions, final Skin pSkin) {
        setCoast(pId);
        flag = false;
        mId = pId;
        mSkin = pSkin;
        style = new ButtonStyle();
        mPlayServices = playServices;
        mOptions = pOptions;
        final String[] shopStr = pOptions.getShopItems().split(" ");
        final int[] array = new int[shopStr.length];
        boolean exist = false;
        for (int i = 0; i < shopStr.length; i++) {
            array[i] = Integer.parseInt(shopStr[i]);
            if (array[i] == mId + 100) {
                exist = true;
            }
        }
        if (pId != 0 && exist) {
            style.up = mSkin.getDrawable(mId + "_" + (mId + 1));
            setDisabled(true);
        } else {
            style.down = mSkin.getDrawable(mId + "_" + mId);
            style.up = mSkin.getDrawable(String.valueOf(mId));
            addListener(new ClickListener() {

                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    super.clicked(event, x, y);
                    if (pOptions.getCoins() >= getCoast() && pId != 0) {
                        pOptions.setShopItems(pId + 100);
                        pOptions.setCoins(-getCoast());
                        if (pOptions.getSoundCheck()) {
                            DefenderOfNature.getBuySound().play(pOptions.getSoundVolume());
                        }
                        flag = true;
                    } else if (pId == 0) {
                        if (pOptions.getSoundCheck()) {
                            DefenderOfNature.getCoinSound().play(pOptions.getSoundVolume());
                        }
                        playServices.showReward();
                    } else {
                        if (pOptions.getSoundCheck()) {
                            DefenderOfNature.getErrorSound().play(pOptions.getSoundVolume());
                        }
                    }
                }
            });
        }
        setStyle(style);
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if (mPlayServices.getReward()) {
            mPlayServices.setReward();
            mOptions.setCoins(1);
        }
        if (flag) {
            getListeners().clear();
            setDisabled(true);
            style.up = mSkin.getDrawable(mId + "_" + (mId + 1));
            setStyle(style);
            flag = false;
        }
    }

    private void setCoast(final int pId) {
        switch (pId) {
            case 1:
                coast = 2.86f;
                break;
            case 2:
                coast = 5.40f;
                break;
            case 3:
                coast = 13.50f;
                break;
            case 4:
                coast = 20.00f;
                break;
            case 5:
                coast = 25.90f;
                break;
            case 6:
                coast = 26.25f;
                break;
            case 7:
                coast = 28.90f;
                break;
            case 8:
                coast = 35.00f;
                break;
            case 9:
                coast = 32.10f;
                break;
            case 10:
                coast = 45.00f;
                break;
            case 11:
                coast = 50.00f;
                break;
            default:
                coast = 0;
                break;
        }
    }

    private float getCoast() {
        return coast;
    }
}
