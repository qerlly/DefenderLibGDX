package com.dmtrdev.monsters.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;

public class Player extends Sprite {

    private enum State {STANDING, RUNNING, FIRE, JUMP}

    private State currentState, previousState;
    private final World mWorld;
    public Body mBody;
    private boolean mAttack, mToDestroy, runningRight, flag;
    private float mAttackTime, mStateTimer;
    public TextureRegion mTextureRegion;
    private Animation<TextureRegion> playerStanding, playerRun, playerFire, playerJump;

    public Player(final World pWorld, final byte pPlayerId) {
        mWorld = pWorld;
        definePlayer();
        currentState = previousState = State.STANDING;
        mToDestroy = runningRight = flag = false;
        final Array<TextureRegion> frames = new Array<>();

        switch (pPlayerId) {
            case 1:
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("medium_player_run"), i * 167, j * 161, 167, 161));
                        playerRun = new Animation<>(ConstEnemies.PLAYER_ANIM, frames, Animation.PlayMode.LOOP);
                    }
                }
                frames.clear();

                for (int j = 1; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("medium_player_jump"), i * 167, j * 161, 167, 161));
                        playerJump = new Animation<>(ConstEnemies.PLAYER_ANIM, frames);
                    }
                }
                frames.clear();

                for (int j = 0; j < 5; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("medium_player_idle"), i * 167, j * 161, 167, 161));
                        playerStanding = new Animation<>(ConstEnemies.PLAYER_ANIM, frames, Animation.PlayMode.LOOP);
                    }
                }
                frames.clear();

                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("medium_player_attack"), i * 167, j * 161, 167, 161));
                        playerFire = new Animation<>(ConstEnemies.PLAYER_ANIM - 0.02f, frames);
                    }
                }
                frames.clear();
                setSize(ConstEnemies.PLAYER_SIZE_W, ConstEnemies.PLAYER_SIZE_H);
                break;
            case 2:
                flag = true;
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("high_player_run"), i * 239, j * 182, 239, 182));
                        playerRun = new Animation<>(ConstEnemies.PLAYER_ANIM, frames, Animation.PlayMode.LOOP);
                    }
                }
                frames.clear();

                for (int j = 1; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("high_player_jump"), i * 239, j * 182, 239, 182));
                        playerJump = new Animation<>(ConstEnemies.PLAYER_ANIM, frames);
                    }
                }
                frames.clear();

                for (int j = 0; j < 5; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("high_player_idle"), i * 239, j * 182, 239, 182));
                        playerStanding = new Animation<>(ConstEnemies.PLAYER_ANIM, frames, Animation.PlayMode.LOOP);
                    }
                }
                frames.clear();

                for (int j = 0; j < 2; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("high_player_attack"), i * 239, j * 182, 239, 182));
                        playerFire = new Animation<>(ConstEnemies.PLAYER_ANIM - 0.02f, frames);
                    }
                }
                frames.clear();
                setSize(ConstEnemies.PLAYER_SIZE_W + 1, ConstEnemies.PLAYER_SIZE_H + 1);
                break;
            default:
                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("default_player_run"), i * 167, j * 161, 167, 161));
                        playerRun = new Animation<>(ConstEnemies.PLAYER_ANIM, frames, Animation.PlayMode.LOOP);
                    }
                }
                frames.clear();

                for (int j = 1; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("default_player_jump"), i * 167, j * 161, 167, 161));
                        playerJump = new Animation<>(ConstEnemies.PLAYER_ANIM, frames);
                    }
                }
                frames.clear();

                for (int j = 0; j < 5; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("default_player_idle"), i * 167, j * 161, 167, 161));
                        playerStanding = new Animation<>(ConstEnemies.PLAYER_ANIM, frames, Animation.PlayMode.LOOP);
                    }
                }
                frames.clear();

                for (int j = 0; j < 3; j++) {
                    for (int i = 0; i < 5; i++) {
                        frames.add(new TextureRegion(getPlayerAtlas().findRegion("default_player_attack"), i * 167, j * 161, 167, 161));
                        playerFire = new Animation<>(ConstEnemies.PLAYER_ANIM - 0.02f, frames);
                    }
                }
                frames.clear();
                setSize(ConstEnemies.PLAYER_SIZE_W, ConstEnemies.PLAYER_SIZE_H);
                break;
        }
    }

    private static TextureAtlas getPlayerAtlas() {
        return DefenderOfNature.manager.get("atlases/player/player_atlas.txt", TextureAtlas.class);
    }

    public void update(final float delta) {
        if (!mToDestroy) {
            if (flag) {
                if (!isFlipX()) {
                    setPosition(mBody.getPosition().x - 1.4f, mBody.getPosition().y - 1.4f);
                } else {
                    setPosition(mBody.getPosition().x - 1.7f, mBody.getPosition().y - 1.4f);
                }
            } else {
                if (!isFlipX()) {
                    setPosition(mBody.getPosition().x - getWidth() / 1.6f, mBody.getPosition().y - getHeight() / 2);
                } else {
                    setPosition(mBody.getPosition().x - getWidth() / 2.7f, mBody.getPosition().y - getHeight() / 2);
                }
            }
            setRegion(getFrame(delta));
        } else {
            mBody.setActive(false);
            setRegion(getFrame(delta));
        }
    }

    private TextureRegion getFrame(final float delta) {
        if (mAttack && playerFire.isAnimationFinished(mAttackTime)) {
            mAttack = false;
            mAttackTime = 0;
        } else if (mAttack) {
            mAttackTime += delta;
        }
        currentState = getState();
        switch (currentState) {
            case FIRE:
                mTextureRegion = playerFire.getKeyFrame(mAttackTime);
                break;
            case RUNNING:
                mTextureRegion = playerRun.getKeyFrame(mStateTimer);
                break;
            case JUMP:
                mTextureRegion = playerJump.getKeyFrame(mStateTimer);
                break;
            default:
                mTextureRegion = playerStanding.getKeyFrame(mStateTimer);
                break;
        }
        if ((mBody.getLinearVelocity().x < 0 || !runningRight) && mTextureRegion.isFlipX()) {
            mTextureRegion.flip(true, false);
            runningRight = false;
        } else if ((mBody.getLinearVelocity().x > 0 || runningRight) && !mTextureRegion.isFlipX()) {
            mTextureRegion.flip(true, false);
            runningRight = true;
        }
        mStateTimer = currentState == previousState ? mStateTimer + delta : 0;
        previousState = currentState;
        return mTextureRegion;
    }

    public void setAttack() {
        mAttack = true;
    }

    private State getState() {
        if (mAttack) {
            return State.FIRE;
        } else if (mBody.getLinearVelocity().y != 0) {
            return State.JUMP;
        } else if (mBody.getLinearVelocity().x != 0 && mBody.getLinearVelocity().y == 0 && !mAttack) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public Body getBody() {
        return mBody;
    }

    public void collision(final boolean pDirection, final Vector2 pImpulse) {
        if (pDirection && mBody.getLinearVelocity().y == 0) {
            mBody.applyLinearImpulse(pImpulse.x, pImpulse.y, 0, 0, true);
        } else if (!pDirection && mBody.getLinearVelocity().y == 0) {
            mBody.applyLinearImpulse(-pImpulse.x, pImpulse.y, 0, 0, true);
        } else if (pDirection && pImpulse.x == 12) {
            mBody.applyLinearImpulse(-pImpulse.x, pImpulse.y, 0, 0, true);
        } else if (!pDirection && pImpulse.x == 12) {
            mBody.applyLinearImpulse(-pImpulse.x, pImpulse.y, 0, 0, true);
        }
    }

    public void setDestroy(final boolean pToDestroy) {
        mToDestroy = pToDestroy;
    }

    public boolean getDirection() {
        return runningRight;
    }

    public boolean getDestroy() {
        return mToDestroy;
    }

    private void definePlayer() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(ConstGame.X / 2 + 2, ConstGame.PLAYER_POSITION_Y);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        mBody = mWorld.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstEnemies.PLAYER_BODY_WIDTH, ConstEnemies.PLAYER_BODY_HEIGHT);
        fixtureDef.filter.categoryBits = ConstGame.PLAYER_BIT;
        fixtureDef.filter.maskBits = ConstGame.TREE_BIT | ConstGame.GROUND_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.shape = shape;
        mBody.createFixture(fixtureDef).setUserData(this);
        mBody.setFixedRotation(true);
    }
}