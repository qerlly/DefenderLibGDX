package com.dmtrdev.monsters.sprites.enemies.bosses;

import com.badlogic.gdx.graphics.g2d.Animation;
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
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.medieval.IronSpear;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveSpear;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class Storm extends Enemy {

    private boolean fly;
    private int count;

    public Storm(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.STORM_HIGH_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 80);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.STORM_HIGH_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 80);
            } else {
                hp = ConstEnemies.STORM_HIGH_HP / 2;
            }
        } else {
            hp = ConstEnemies.STORM_HIGH_HP;
        }

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getDragonsAtlas().findRegion("dragon_high_run"), i * 328, 0, 328, 240));
            runAnimation = new Animation<>(ConstEnemies.DRAGON_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getDragonsAtlas().findRegion("dragon_high_attack"), 0, i * 235, 511, 235));
            attackAnimation = new Animation<>(ConstEnemies.DRAGON_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("skeleton_explode"), j * 233, i * 213, 233, 213));
                deathAnimation = new Animation<>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();
        fly = false;
        count = 0;
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        if (pData.equals(Arrow.class.getSimpleName()) || pData.equals(PrimitiveSpear.class.getSimpleName())
                || pData.equals(IronSpear.class.getSimpleName())) {
            hp -= pDamage * 3;
        } else {
            hp -= pDamage;
        }
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
                            setPosition(body.getPosition().x - getWidth() / 2 + 2, body.getPosition().y - 1.4f);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2 + 2, body.getPosition().y - 1.4f);
                        }
                        world.destroyBody(body);
                        destroy = true;
                        time = 0;
                        setSize(5, 5.8f);
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
                setSize(ConstEnemies.STORM_SIZE + 3.7f, ConstEnemies.STORM_SIZE - 0.2f);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.STORM_HIGH_DAMAGE);
                    attackTime = 0;
                    count++;
                    if (count >= 2) {
                        attack = false;
                        count = 0;
                        direction = !direction;
                    }
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - 4.4f, body.getPosition().y - 2.1f);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - 6.3f, body.getPosition().y - 2.1f);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.STORM_SIZE - 0.2f, ConstEnemies.STORM_SIZE);
                if (fly) {
                    if (direction) {
                        body.setLinearVelocity(ConstEnemies.DRAGON_VELOCITY_X + 13, 10);
                        setPosition(body.getPosition().x - getWidth() / 2 - 0.8f, body.getPosition().y - 2);
                    } else {
                        body.setLinearVelocity(-ConstEnemies.DRAGON_VELOCITY_X - 13, 10);
                        setPosition(body.getPosition().x - getWidth() / 2 + 0.8f, body.getPosition().y - 2);
                    }
                } else {
                    if (direction) {
                        body.setLinearVelocity(ConstEnemies.DRAGON_VELOCITY_X + 4, ConstEnemies.VELOCITY_Y);
                        setPosition(body.getPosition().x - getWidth() / 2 - 0.8f, body.getPosition().y - 2);
                    } else {
                        body.setLinearVelocity(-ConstEnemies.DRAGON_VELOCITY_X - 4, ConstEnemies.VELOCITY_Y);
                        setPosition(body.getPosition().x - getWidth() / 2 + 0.8f, body.getPosition().y - 2);
                    }

                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public void setDirection() {
        direction = !direction;
        fly = !fly;
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.STORM_HIGH_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.STORM_HIGH_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY() + 6);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape legs = new PolygonShape();
        legs.setAsBox(7, 0.01f);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.SKY_BIT | ConstGame.TREE_LOW_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.DRAGON_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final PolygonShape corpus = new PolygonShape();
        final Vector2[] vertice = new Vector2[6];
        vertice[0] = new Vector2(-2.7f, ConstEnemies.LEGS_SIZE);
        vertice[1] = new Vector2(-2.5f, ConstEnemies.LEGS_SIZE + 1.5f);
        vertice[2] = new Vector2(-1, ConstEnemies.LEGS_SIZE + 2.5f);
        vertice[3] = new Vector2(1, ConstEnemies.LEGS_SIZE + 2.5f);
        vertice[4] = new Vector2(2.5f, ConstEnemies.LEGS_SIZE + 1.5f);
        vertice[5] = new Vector2(2.7f, ConstEnemies.LEGS_SIZE);
        corpus.set(vertice);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.PLAYER_BIT | ConstGame.GROUND_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.DRAGON_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
