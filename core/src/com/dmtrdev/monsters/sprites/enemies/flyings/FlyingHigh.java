package com.dmtrdev.monsters.sprites.enemies.flyings;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class FlyingHigh  extends Enemy {


    public FlyingHigh(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        hp = ConstEnemies.FLYING_HIGH_HP;

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 5; i++) {
                frames.add(new TextureRegion(DefenderOfNature.getFlyinglAtlas().findRegion("flying_high_attack"), 638 - (i * 159), j * 184, 159, 184));
                runAnimation = new Animation<>(ConstEnemies.FLYING_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("skeleton_explode"), j * 233, i * 213, 233, 213));
                deathAnimation = new Animation<>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();
        setSize(ConstEnemies.FLYING_HIGH_SIZE - 0.7f, ConstEnemies.FLYING_HIGH_SIZE);
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        hp -= pDamage;
        if (hp <= 0) {
            setToDestroy = true;
        }
    }

    @Override
    public void update(final float delta) {
        super.update(delta);
        switch (getState()) {
            case DIE:
                if (effects) {
                    if (!destroy) {
                        if (screen.getOptions().getSoundCheck()) {
                            DefenderOfNature.getDeathSound().play(screen.getOptions().getSoundVolume());
                        }
                        if (direction) {
                            setPosition(body.getPosition().x - getWidth() / 2 + 1.7f, body.getPosition().y - 1.2f);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2 + 0.8f, body.getPosition().y - 1.2f);
                        }
                        world.destroyBody(body);
                        setSize(ConstArmor.EFFECT_LOW_SIZE, ConstArmor.EFFECT_LOW_SIZE + 0.4f);
                        destroy = true;
                        time = 0;
                        coinSpawn = true;
                    } else if (deathAnimation.isAnimationFinished(time)) {
                        destroyed = true;
                    }
                    setRegion(getFrame());
                    time += delta;
                } else {
                    world.destroyBody(body);
                    destroyed = true;
                    coinSpawn = true;
                }
                break;
            default:
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.FLYING_VELOCITY_X + 1, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y  - getHeight() / 2);
                } else {
                    body.setLinearVelocity(-ConstEnemies.FLYING_VELOCITY_X - 1, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y  - getHeight() / 2);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return 0;
    }

    @Override
    public int getScore() {
        return ConstEnemies.FLYING_HIGH_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY() + 1);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape legs = new PolygonShape();
        legs.setAsBox(ConstEnemies.FLYING_HIGH_BODY_SIZE, ConstEnemies.FLYING_HIGH_BODY_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.PLAYER_BIT | ConstGame.GROUND_BIT | ConstGame.SKY_BIT | ConstGame.ARMOR_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.FLYING_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}


