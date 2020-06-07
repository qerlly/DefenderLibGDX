package com.dmtrdev.monsters.sprites.armors.trees;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class AxeShot extends Armor {

    private boolean mFlag;
    private Vector2 mPos;
    private int mTakenDamage, mHp;
    private float destroyTime;

    public AxeShot(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(4.3f, 5.8f);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("swinging_spike_high"));
        setOriginCenter();
        mTakenDamage = 0;
        mHp = ConstArmor.AXE_SHOT_HP;
        mFlag = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("star_explode"), j * 225, i * 213, 225, 213));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();
        body.setLinearVelocity(0, -10);
        destroyTime = 0;
    }

    @Override
    public void collisionEnemy() {
    }

    @Override
    public void collisionEnemy(final int pDamage, final boolean pCollision) {
        super.collisionEnemy(pDamage, pCollision);
        if (pCollision) {
            mTakenDamage += pDamage;
        } else {
            mTakenDamage -= pDamage;
        }
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.AXE_SHOT_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (body.getPosition().y < 12) {
            setToDestroy = true;
        }
        if (body.getLinearVelocity().y == 0 && !mFlag) {
            mPos = new Vector2(body.getPosition().x, body.getPosition().y);
            world.destroyBody(body);
            mFlag = true;
            defineArmor();
        }
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBarrelSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2 + 1, body.getPosition().y - getHeight() - 2.5f);
                setSize(ConstArmor.EFFECT_BIG_SIZE - 1.6f, ConstArmor.EFFECT_BIG_SIZE - 1);                world.destroyBody(body);
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                destroyed = true;
            }
            setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
            time += delta;
        } else if (setToDestroy) {
            if (playScreen.getOptions().getSoundCheck()) {
                DefenderOfNature.getBarrelSound().play(playScreen.getOptions().getSoundVolume());
            }
            world.destroyBody(body);
            destroyed = true;
        } else {
            body.setLinearVelocity(0, -10);
            if (collisions != 0) {
                if (mHp <= 0) {
                    setToDestroy = true;
                } else if (time >= 0.8f) {
                    mHp -= mTakenDamage;
                    time = 0;
                }
                time += delta;
            }
            if (body.getPosition().x > ConstGame.X / 2 + 4.4f) {
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() - 3);
            } else if (body.getPosition().x > ConstGame.X / 2) {
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() - 2);
            } else {
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() - 1);
            }
            if (destroyTime > 10f) {
                setToDestroy = true;
            }
            destroyTime += delta;
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        if (!mFlag) {
            bodyDef.position.set(getX(), getY());
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        } else {
            bodyDef.position.set(mPos.x, mPos.y - 0.05f);
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape chain = new PolygonShape();
        chain.setAsBox(0.05f, 0.01f);
        fixtureDef.shape = chain;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        if (getX() > 21.7f) {
            fixtureDef.filter.categoryBits = ConstGame.SKY_ARMOR_BIT;
            fixtureDef.filter.maskBits = ConstGame.SKY_ARMOR_BIT;
        } else {
            fixtureDef.filter.categoryBits = ConstGame.TREE_BIT;
            fixtureDef.filter.maskBits = ConstGame.TREE_BIT;
        }
        body.createFixture(fixtureDef).setUserData(this);

        if (mFlag) {
            final CircleShape shapes = new CircleShape();
            shapes.setRadius(1.2f);
            if (body.getPosition().x > ConstGame.X / 2 + 4.4f) {
                shapes.setPosition(new Vector2(0, -8.3f));
            } else if (body.getPosition().x > ConstGame.X / 2) {
                shapes.setPosition(new Vector2(0, -6.7f));
            } else {
                shapes.setPosition(new Vector2(0, -5.7f));
            }
            fixtureDef.shape = shapes;
            fixtureDef.filter.categoryBits = ConstGame.SKY_ARMOR_BIT;
            fixtureDef.filter.maskBits = ConstGame.ENEMY_SHOTS_BIT;
            fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
            body.createFixture(fixtureDef).setUserData(this);
        }
    }
}