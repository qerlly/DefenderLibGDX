package com.dmtrdev.monsters.sprites.enemies.goblins;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.ground.CarnivorePlant;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.vegetables.Pumpkin;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class GoblinMedium extends Enemy {

    public GoblinMedium(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.GOBLINS_MEDIUM_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 10);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.GOBLINS_MEDIUM_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 10);
            } else {
                hp = ConstEnemies.GOBLINS_MEDIUM_HP / 2;
            }
        } else {
            hp = ConstEnemies.GOBLINS_MEDIUM_HP;
        }

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getGoblinsAtlas().findRegion("goblin_medium_run"), i * 136, 0, 136, 174));
            runAnimation = new Animation<>(ConstEnemies.GOBLINS_ANIM - 0.02f, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getGoblinsAtlas().findRegion("goblin_medium_attack"), i * 141, 0, 141, 154));
            attackAnimation = new Animation<>(ConstEnemies.GOBLINS_ANIM, frames, Animation.PlayMode.LOOP);
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
        if (hp <= 0 || pData.equals(CarnivorePlant.class.getSimpleName()) || pData.equals(Pumpkin.class.getSimpleName())) {
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
                            setPosition(body.getPosition().x - 1, body.getPosition().y - 0.05f);
                        } else {
                            setPosition(body.getPosition().x - 0.8f, body.getPosition().y - 0.05f);
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
            case DISABLE:
                body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                if (disableTime <= 0) {
                    disable = false;
                }
                disableTime -= delta;
                if (direction) {
                    setPosition(body.getPosition().x - getWidth() / 2 + ConstEnemies.GOBLINS_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE * 2);
                } else {
                    setPosition(body.getPosition().x - getWidth() / 2 - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE * 2);
                }
                break;
            case ATTACK:
                setSize(ConstEnemies.GOBLINS_WIDTH, ConstEnemies.GOBLINS_HEIGHT);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.GOBLINS_MEDIUM_DAMAGE);
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + ConstEnemies.GOBLINS_HEAD_DIFF_SIZE, body.getPosition().y - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE, body.getPosition().y - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.GOBLINS_WIDTH, ConstEnemies.GOBLINS_HEIGHT + 0.4f);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.GOBLINS_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + ConstEnemies.GOBLINS_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE * 2);
                } else {
                    body.setLinearVelocity(-ConstEnemies.GOBLINS_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.GOBLINS_HEAD_DIFF_SIZE * 2);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.GOBLINS_MEDIUM_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.GOBLINS_MEDIUM_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape legs = new CircleShape();
        legs.setRadius(ConstEnemies.LEGS_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.filter.maskBits = ConstGame.DISABLE_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.GROUND_BIT | ConstGame.FIRE_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.GOBLINS_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.GOBLINS_BODY_SIZE, ConstEnemies.GOBLINS_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.GOBLINS_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.GOBLINS_HEAD_SIZE, ConstEnemies.GOBLINS_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.GOBLINS_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}
