package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.dmtrdev.monsters.DefenderOfNature;

public class TextGame extends Actor implements Disposable {

    private final GlyphLayout mLayout;
    private final BitmapFont mFont;
    private final float mX, mY;
    private final String mString;
    private Options mOptions;
    private final boolean flag;

    public TextGame(final CharSequence pText, final float pX, final float pY) {
        flag = false;
        mLayout = new GlyphLayout();
        mFont = DefenderOfNature.getFont();
        mFont.getData().setScale(0.03f);
        mFont.setUseIntegerPositions(false);
        mString = (String) pText;
        mX = pX;
        mY = pY;
    }

    public TextGame(final Options pOptions, final float pX, final float pY) {
        flag = true;
        mLayout = new GlyphLayout();
        mFont = DefenderOfNature.getFont();
        mFont.getData().setScale(0.03f);
        mFont.setUseIntegerPositions(false);
        mOptions = pOptions;
        mString = String.format("%.2f", mOptions.getCoins());
        mX = pX;
        mY = pY;
    }

    @Override
    public void act(final float delta) {
        super.act(delta);
        if(!flag) {
            mLayout.setText(mFont, mString);
        } else {
            mLayout.setText(mFont, String.format("%.2f", mOptions.getCoins()));
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        super.draw(batch, parentAlpha);
        mFont.draw(batch, mLayout, mX - mLayout.width / 2, mY);
    }

    @Override
    public void dispose() {
        mFont.dispose();
    }
}
