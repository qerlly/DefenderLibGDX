package com.dmtrdev.monsters.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.dmtrdev.monsters.gui.MainMenuGui;

public class MenuScreen implements Screen {

    private final MainMenuGui mMenuGui;

    public MenuScreen(final Game pGame) {
        mMenuGui = new MainMenuGui(pGame);
        Gdx.input.setInputProcessor(mMenuGui);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mMenuGui.act(delta);
        mMenuGui.draw();
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
        mMenuGui.dispose();
    }
}