package com.dmtrdev.monsters.sprites.world;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.Player;

public class Tree extends Sprite {

    private final PlayScreen mPlayScreen;
    private int mHp, mPrevHp;

    public Tree(final PlayScreen pPlayScreen) {
        mHp = mPrevHp = ConstEnemies.TREE_HP;
        mPlayScreen = pPlayScreen;
        setRegion(DefenderOfNature.getWorldAtlas().findRegion("tree"));
        setSize(20.5f,14);
        setPosition(ConstGame.X / 2 - 11.28f, ConstGame.SPAWN_HEIGHT - 1.02f);
        defineTree();
    }

    public void update(){
        if (mHp <= 0) {
            mPlayScreen.getPlayer().setDestroy(true);
        }
    }

    public int getPrevHp() {
        return mPrevHp;
    }

    public void setHp(final int pDamage){
        mHp -= pDamage;
    }

    public void restoreHp(int pWave){
        if (pWave % 5 == 0) {
            mPrevHp += 150;
        }
        mHp = mPrevHp;
    }

    public float getHP() {
        return mHp;
    }

    public Player getPlayer(){
        return mPlayScreen.getPlayer();
    }

    private void defineTree() {
        final BodyDef bodyDef = new BodyDef();
        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape polygonShape = new PolygonShape();

        bodyDef.position.set(ConstGame.X / 2, ConstGame.PLAYER_INPUT_PANEL_HEIGHT + 2.7f);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = mPlayScreen.getWorld().createBody(bodyDef);
        polygonShape.setAsBox(1f, 3);
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = ConstGame.TREE_LOW_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        bodyDef.position.set(5.25f, 15.35f);
        body = mPlayScreen.getWorld().createBody(bodyDef);
        polygonShape.setAsBox(2.4f, 0.4f);
        fixtureDef.filter.categoryBits = ConstGame.TREE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.PLAYER_BIT | ConstGame.TREE_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        bodyDef.position.set(11.2f, 14.9f);
        body = mPlayScreen.getWorld().createBody(bodyDef);
        polygonShape.setAsBox(2.55f, 0.4f);
        fixtureDef.filter.categoryBits = ConstGame.TREE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.PLAYER_BIT | ConstGame.TREE_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        bodyDef.position.set(16.6f, 16.2f);
        body = mPlayScreen.getWorld().createBody(bodyDef);
        polygonShape.setAsBox(2.1f, 0.4f);
        fixtureDef.filter.categoryBits = ConstGame.TREE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.PLAYER_BIT | ConstGame.TREE_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        bodyDef.position.set(20.8f, 17.9f);
        body = mPlayScreen.getWorld().createBody(bodyDef);
        polygonShape.setAsBox(2.3f, 0.4f);
        fixtureDef.filter.categoryBits = ConstGame.TREE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.PLAYER_BIT | ConstGame.TREE_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }
}