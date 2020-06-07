package com.dmtrdev.monsters.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.dmtrdev.monsters.gui.GameModeGui;

public class GameModeScreen implements Screen {

    private final GameModeGui mGameModeGui;

    public GameModeScreen(final Game pGame){
        mGameModeGui = new GameModeGui(pGame);
        Gdx.input.setInputProcessor(mGameModeGui);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mGameModeGui.act();
        mGameModeGui.draw();
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
        mGameModeGui.dispose();
    }
}