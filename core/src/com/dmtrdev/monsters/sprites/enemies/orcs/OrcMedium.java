package com.dmtrdev.monsters.sprites.enemies.orcs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.medieval.BoneAxe;
import com.dmtrdev.monsters.sprites.armors.medieval.IronAxe;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveAxe;
import com.dmtrdev.monsters.sprites.armors.medieval.StoneAxe;
import com.dmtrdev.monsters.sprites.enemies.Enemy;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;

public class OrcMedium extends Enemy {

    public OrcMedium(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.ORC_MEDIUM_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 10);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.ORC_MEDIUM_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 10);
            } else {
                hp = ConstEnemies.ORC_MEDIUM_HP / 2;
            }
        } else {
            hp = ConstEnemies.ORC_MEDIUM_HP;
        }

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 2; i++) {
                frames.add(new TextureRegion(DefenderOfNature.getOrcsAtlas().findRegion("orc_medium_run"), i * 345, j * 345, 345, 345));
                runAnimation = new Animation<>(ConstEnemies.ORC_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 2; i++) {
                if (j == 3 && i == 1) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getOrcsAtlas().findRegion("orc_medium_attack"), i * 345, j * 345, 345, 345));
                attackAnimation = new Animation<>(ConstEnemies.ORC_ANIM, frames, Animation.PlayMode.LOOP);
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
        setSize(ConstEnemies.ORC_SIZE_WIDTH, ConstEnemies.ORC_SIZE_HEIGHT);
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        hp -= pDamage;
        if (hp <= 0 || pData.equals(StoneAxe.class.getSimpleName()) || pData.equals(BoneAxe.class.getSimpleName())
                || pData.equals(IronAxe.class.getSimpleName()) || pData.equals(PrimitiveAxe.class.getSimpleName())) {
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
                        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y + 0.3f);
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
                    coinSpawn = true;
                    destroyed = true;
                }
                break;
            case DISABLE:
                body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                if (disableTime <= 0) {
                    disable = false;
                }
                disableTime -= delta;
                if (direction) {
                    setPosition(body.getPosition().x - getWidth() / 2 + ConstEnemies.ORC_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.ORC_HEAD_DIFF_SIZE);
                } else {
                    setPosition(body.getPosition().x - getWidth() / 2 - ConstEnemies.ORC_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.ORC_HEAD_DIFF_SIZE);
                }
                break;
            case ATTACK:
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.ORC_MEDIUM_DAMAGE);
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + ConstEnemies.ORC_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.ORC_HEAD_DIFF_SIZE);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - ConstEnemies.ORC_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.ORC_HEAD_DIFF_SIZE);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.ORC_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + ConstEnemies.ORC_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.ORC_HEAD_DIFF_SIZE);
                } else {
                    body.setLinearVelocity(-ConstEnemies.ORC_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - ConstEnemies.ORC_HEAD_DIFF_SIZE / 2, body.getPosition().y - ConstEnemies.ORC_HEAD_DIFF_SIZE);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.ORC_MEDIUM_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.ORC_MEDIUM_SCORE;
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
        fixtureDef.density = ConstEnemies.ORC_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.ORC_BODY_SIZE, ConstEnemies.ORC_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.ORC_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.ORC_HEAD_SIZE, ConstEnemies.ORC_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.ORC_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}
