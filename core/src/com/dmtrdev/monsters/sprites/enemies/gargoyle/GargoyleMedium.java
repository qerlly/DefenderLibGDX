package com.dmtrdev.monsters.sprites.enemies.gargoyle;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.medieval.IronSpear;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveSpear;
import com.dmtrdev.monsters.sprites.armors.ninja.ModernShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.PrimitiveShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.StandardShuriken;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class GargoyleMedium extends Enemy {

    public GargoyleMedium(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.GARGOYLE_MEDIUM_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 15);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.GARGOYLE_MEDIUM_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 15);
            } else {
                hp = ConstEnemies.GARGOYLE_MEDIUM_HP / 2;
            }
        } else {
            hp = ConstEnemies.GARGOYLE_MEDIUM_HP;
        }

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getGargoyleAtlas().findRegion("gargoyle_medium_run"), 0, i * 295, 319, 295));
            runAnimation = new Animation<>(ConstEnemies.GARGOYLE_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getGargoyleAtlas().findRegion("gargoyle_medium_attack"), 0, i * 296, 342, 296));
            attackAnimation = new Animation<>(ConstEnemies.GARGOYLE_ANIM, frames, Animation.PlayMode.LOOP);
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
        if (!pData.equals(Arrow.class.getSimpleName()) && !pData.equals(IronSpear.class.getSimpleName()) && !pData.equals(PrimitiveSpear.class.getSimpleName())) {
            hp -= pDamage;
        }
        if (hp <= 0 || pData.equals(PrimitiveShuriken.class.getSimpleName()) || pData.equals(ModernShuriken.class.getSimpleName())
                || pData.equals(StandardShuriken.class.getSimpleName())) {
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
                            setPosition(body.getPosition().x - 1.5f, body.getPosition().y - 1.9f);
                        } else {
                            setPosition(body.getPosition().x - 1.5f, body.getPosition().y - 1.9f);
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
                setSize(ConstEnemies.GARGOYLE_SIZE, ConstEnemies.GARGOYLE_SIZE + 0.7f);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.GARGOYLE_MEDIUM_DAMAGE);
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.5f, body.getPosition().y - getHeight() / 2);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.5f, body.getPosition().y - getHeight() / 2);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.GARGOYLE_SIZE, ConstEnemies.GARGOYLE_SIZE + 1.3f);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.GARGOYLE_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.5f, body.getPosition().y - getHeight() / 2 + 0.3f);
                } else {
                    body.setLinearVelocity(-ConstEnemies.GARGOYLE_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.5f, body.getPosition().y - getHeight() / 2+ 0.3f);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.GARGOYLE_MEDIUM_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.GARGOYLE_MEDIUM_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY() + 6);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.GARGOYLE_BODY_SIZE, ConstEnemies.GARGOYLE_BODY_DIFF_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.TREE_LOW_BIT | ConstGame.SKY_BIT | ConstGame.GROUND_BIT | ConstGame.SKY_ARMOR_BIT ;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.GARGOYLE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}

