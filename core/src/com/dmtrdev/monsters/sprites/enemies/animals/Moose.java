package com.dmtrdev.monsters.sprites.enemies.animals;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.flyings.BirdArmor;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class Moose extends Enemy {

    private Sound sound;

    public Moose(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        hp = ConstEnemies.MOOSE_HP;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getAnimalsAtlas().findRegion("moose_charge"), 526 - (j * 132), i * 124, 132, 124));
                runAnimation = new Animation<>(ConstEnemies.ANIMALS_ANIM - 0.04f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getAnimalsAtlas().findRegion("moose_die"), 527 - (i * 132), 0, 132, 124));
            attackAnimation = new Animation<>(ConstEnemies.ANIMALS_ANIM, frames);
        }
        frames.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("skeleton_explode"), j * 233, i * 213, 233, 213));
                deathAnimation = new Animation<>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();
        setSize(ConstEnemies.MOOSE_SIZE, ConstEnemies.MOOSE_SIZE);
        sound = DefenderOfNature.getMooseSound();
        if (pScreen.getOptions().getSoundCheck()) {
            sound.loop();
            sound.play(pScreen.getOptions().getSoundVolume());
        }
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
        if(ConstGame.GAME_STATE == ConstGame.State.PAUSE){
            sound.pause();
        } else if(screen.getOptions().getSoundCheck() && !destroy) {
            sound.resume();
        }
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
                            DefenderOfNature.getDeathBigSound().play(screen.getOptions().getSoundVolume());
                        }
                        if (direction) {
                            setPosition(body.getPosition().x - getWidth() / 2 + 1, body.getPosition().y - 0.2f);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2 + 1.1f, body.getPosition().y - 0.2f);
                        }
                        world.destroyBody(body);
                        destroy = true;
                        setSize(ConstArmor.EFFECT_MEDIUM_SIZE, ConstArmor.EFFECT_MEDIUM_SIZE + 0.6f);
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
            case DISABLE:
                body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                if (disableTime <= 0) {
                    disable = false;
                }
                disableTime -= delta;
                setPosition(getX(), getY());
                break;
            case ATTACK:
                sound.stop();
                if(attack && !plantAttack){
                    tree.getPlayer().collision(direction, new Vector2(7, 8));
                    plantAttack = true;
                }
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    setToDestroy = true;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.MOOSE_DAMAGE);
                    setToDestroy = true;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.6f);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.6f);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.MOOSE_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.6f);
                } else {
                    body.setLinearVelocity(-ConstEnemies.MOOSE_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.6f);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.MOOSE_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.MOOSE_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY() + 3);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape legs = new PolygonShape();
        legs.setAsBox(ConstEnemies.LEGS_SIZE + 1, ConstEnemies.LEGS_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.filter.maskBits = ConstGame.DISABLE_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.GROUND_BIT | ConstGame.FIRE_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.ANIMAL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final FixtureDef fixture = new FixtureDef();
        final PolygonShape corpus = new PolygonShape();
        final Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-1.5f, ConstEnemies.LEGS_SIZE);
        vertice[1] = new Vector2(-1.5f, ConstEnemies.LEGS_SIZE + 2);
        vertice[2] = new Vector2(1.5f, ConstEnemies.LEGS_SIZE + 2);
        vertice[3] = new Vector2(1.5f, ConstEnemies.LEGS_SIZE);
        corpus.set(vertice);
        fixture.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixture.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixture.shape = corpus;
        fixture.density = ConstEnemies.ANIMAL_DENSITY;
        body.createFixture(fixture).setUserData(this);
        body.setFixedRotation(true);
    }
}
