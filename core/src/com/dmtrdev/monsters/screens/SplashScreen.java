package com.dmtrdev.monsters.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.dmtrdev.monsters.gui.LoadingGui;

public class SplashScreen implements Screen {

    private final LoadingGui mLoadingGui;

    public SplashScreen(final Game pGame, final String pType) {
        mLoadingGui = new LoadingGui(pGame, pType);
        Gdx.input.setInputProcessor(mLoadingGui);
    }

    @Override
    public void show() {}

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mLoadingGui.act();
        mLoadingGui.draw();
    }

    @Override
    public void resize(final int width, final int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        mLoadingGui.dispose();
    }
}