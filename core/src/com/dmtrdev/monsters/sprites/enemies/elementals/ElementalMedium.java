package com.dmtrdev.monsters.sprites.enemies.elementals;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.bombs.Bomb;
import com.dmtrdev.monsters.sprites.armors.bombs.Grenade;
import com.dmtrdev.monsters.sprites.armors.bombs.Molotov;
import com.dmtrdev.monsters.sprites.armors.bombs.SpikeBomb;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class ElementalMedium extends Enemy {

    public ElementalMedium(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.ELEMENTAL_MEDIUM_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 15);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.ELEMENTAL_MEDIUM_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 15);
            } else {
                hp = ConstEnemies.ELEMENTAL_MEDIUM_HP / 2;
            }
        } else {
            hp = ConstEnemies.ELEMENTAL_MEDIUM_HP;
        }

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getElementalAtlas().findRegion("elemental_medium_run"), i * 205, 0, 205, 241));
            runAnimation = new Animation<>(ConstEnemies.ELEMENTAL_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                if (j == 2 && i == 1) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getElementalAtlas().findRegion("elemental_medium_attack"), i * 408, j * 248, 408, 248));
                attackAnimation = new Animation<>(ConstEnemies.ELEMENTAL_ANIM, frames, Animation.PlayMode.LOOP);
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
        if(burn) {
            setToDestroy = true;
        }
        switch (getState()) {
            case DIE:
                if (effects) {
                    if (!destroy) {
                        if (screen.getOptions().getSoundCheck()) {
                            DefenderOfNature.getDeathSound().play(screen.getOptions().getSoundVolume());
                        }
                        if (attack && !direction) {
                            setPosition(body.getPosition().x - getWidth() / 2 + 2, body.getPosition().y + 0.2f);
                        } else if (attack) {
                            setPosition(body.getPosition().x - getWidth() / 2 + 1.6f, body.getPosition().y + 0.2f);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y + 0.2f);
                        }
                        world.destroyBody(body);
                        setSize(ConstArmor.EFFECT_MEDIUM_SIZE, ConstArmor.EFFECT_MEDIUM_SIZE + 0.6f);
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
            case ATTACK:
                setSize(ConstEnemies.ELEMENTAL_DEFAULT_SIZE + 2.2f, ConstEnemies.ELEMENTAL_DEFAULT_SIZE + 0.6f);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.ELEMENTAL_MEDIUM_DAMAGE);
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 1.8f, body.getPosition().y);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 1.8f, body.getPosition().y);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.ELEMENTAL_DEFAULT_SIZE - 1, ConstEnemies.ELEMENTAL_DEFAULT_SIZE + 0.4f);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.ELEMENTAL_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y);
                } else {
                    body.setLinearVelocity(-ConstEnemies.ELEMENTAL_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public void setDirection() {
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.ELEMENTAL_MEDIUM_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.ELEMENTAL_MEDIUM_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY() + 1);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDeff = new FixtureDef();
        final PolygonShape legss = new PolygonShape();
        legss.setAsBox(0.3F, 0.01f);
        fixtureDeff.filter.categoryBits = ConstGame.ENEMY_DOWN_BIT ;
        fixtureDeff.filter.maskBits = ConstGame.FIRE_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDeff.shape = legss;
        fixtureDeff.density = ConstEnemies.ELEMENTAL_DENSITY;
        body.createFixture(fixtureDeff).setUserData(this);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape legs = new PolygonShape();
        legs.setAsBox(4, 0.01f);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.TREE_LOW_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.ELEMENTAL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.ELEMENTAL_BODY_SIZE, ConstEnemies.ELEMENTAL_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.ELEMENTAL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.ELEMENTAL_HEAD_SIZE, ConstEnemies.ELEMENTAL_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.ELEMENTAL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}