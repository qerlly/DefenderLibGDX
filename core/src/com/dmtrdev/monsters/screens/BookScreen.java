package com.dmtrdev.monsters.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.dmtrdev.monsters.gui.BookGui;

public class BookScreen implements Screen {

    private final BookGui mBookGui;

    public BookScreen(final Game pGame) {
        mBookGui = new BookGui(pGame);
        Gdx.input.setInputProcessor(mBookGui);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mBookGui.act();
        mBookGui.draw();
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
        mBookGui.dispose();
    }
}