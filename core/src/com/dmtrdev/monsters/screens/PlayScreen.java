package com.dmtrdev.monsters.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.gui.ArmorGui;
import com.dmtrdev.monsters.gui.GameGui;
import com.dmtrdev.monsters.sprites.Player;
import com.dmtrdev.monsters.sprites.world.Tree;
import com.dmtrdev.monsters.utils.ArmorTable;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.utils.Options;
import com.dmtrdev.monsters.utils.WorldContactListener;

public class PlayScreen implements Screen {

    private final Skin mSkin;
    private final String mType;
    private final OrthographicCamera mCamera;
    private boolean mGameStart, mRestart;
    private GameGui mGameGui;
    private final World mWorld;
    private ArmorGui mArmorGui;
    private Player mPlayer;
    private final Options mOptions;
    private final Tree mTree;
    private boolean mLevelComplete;

    public PlayScreen(final Game pGame, final String pType) {
        mCamera = new OrthographicCamera(ConstGame.X, ConstGame.Y);
        mType = pType;
        mGameStart = mLevelComplete = mRestart = false;
        mOptions = new Options();
        mSkin = new Skin();
        mSkin.addRegions(DefenderOfNature.manager.get("atlases/gui/game_atlas.txt", TextureAtlas.class));
        mSkin.addRegions(DefenderOfNature.getArmorsAtlas());
        mSkin.add("default", DefenderOfNature.getFont());
        final TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = mSkin.getFont("default");
        mSkin.add("default", textButtonStyle);
        mWorld = new World(new Vector2(0, -10), true);
        mWorld.setContactListener(new WorldContactListener());
        mTree = new Tree(this);
        if (mType.contains(ConstGame.STORY_MODE) && !mOptions.firstStart()) {
            mArmorGui = new ArmorGui(pGame, this, false);
        } else {
            mArmorGui = new ArmorGui(pGame, this, true);
        }
        Gdx.input.setInputProcessor(mArmorGui);
    }

    public void createPlayer(){
        mPlayer = new Player(mWorld, mArmorGui.getPlayerId());
    }

    public void createGameGui(final Game pGame, final ArmorTable pArmorTable) {
        mGameGui = new GameGui(pGame, this, pArmorTable);
        Gdx.input.setInputProcessor(mGameGui);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(final float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!mGameStart) {
            mArmorGui.act(delta);
            mArmorGui.draw();
        } else {
            mGameGui.act(delta);
            mGameGui.draw();
        }
    }

    public OrthographicCamera getCamera() {
        return mCamera;
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
        mSkin.dispose();
        if (!mType.equals(ConstGame.SURVIVE_MODE)) {
            mArmorGui.dispose();
        }
        if (mGameStart) {
            mGameGui.dispose();
        }
        if(!mRestart) {
            DefenderOfNature.unloadGamePlayRes(mType);
            mRestart = false;
        }
    }

    public void setLevelChallenge(final boolean pLevelComplete, final int pLevelIndex) {
        if (mOptions.getChallengesCount() < pLevelIndex) {
            mOptions.setChallengesCount(pLevelIndex);
        }
        mLevelComplete = pLevelComplete;
    }

    public void setLevelStory(final boolean pLevelComplete, final int pLevelIndex) {
        if (mOptions.getStoryLevel() < pLevelIndex) {
            mOptions.setStoryLevel(pLevelIndex);
        }
        mLevelComplete = pLevelComplete;
    }

    public void setStats(final int pScore, final int pWaves, final int pEnemies) {
        if (mOptions.getScore() < pScore) {
            mOptions.setScore(pScore);
        }
        if (mOptions.getWavesCount() < pWaves) {
            mOptions.setWavesCount(pWaves);
        }
        if (mOptions.getEnemiesCount() < pEnemies) {
            mOptions.setEnemiesCount(pEnemies);
        }
    }

    public boolean getLevelComplete() {
        return mLevelComplete;
    }

    public void setRestart(final boolean pRestart){
        mRestart = pRestart;
    }

    public void setGameStart(final boolean pGameStart){
        mGameStart = pGameStart;
    }

    public GameGui getGameGui() {
        return mGameGui;
    }

    public Skin getSkin() {
        return mSkin;
    }

    public Tree getTree() {
        return mTree;
    }

    public String getType() {
        return mType;
    }

    public World getWorld() {
        return mWorld;
    }

    public Player getPlayer() {
        return mPlayer;
    }

    public Options getOptions() {
        return mOptions;
    }
}