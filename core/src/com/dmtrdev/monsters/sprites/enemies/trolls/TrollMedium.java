package com.dmtrdev.monsters.sprites.enemies.trolls;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.bombs.Bomb;
import com.dmtrdev.monsters.sprites.armors.bombs.Grenade;
import com.dmtrdev.monsters.sprites.armors.bombs.SpikeBomb;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;

public class TrollMedium extends Enemy {

    public TrollMedium(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.TROLL_MEDIUM_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 35);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.TROLL_MEDIUM_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 35);
            } else {
                hp = ConstEnemies.TROLL_MEDIUM_HP / 2;
            }
        } else {
            hp = ConstEnemies.TROLL_MEDIUM_HP;
        }

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getTrollsAtlas().findRegion("troll_medium_run"), i * 333, 0, 333, 248));
            runAnimation = new Animation<>(ConstEnemies.TROLL_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 2; i++) {
                if (j == 3 && i == 1) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getTrollsAtlas().findRegion("troll_medium_attack"), i * 402, j * 339, 402, 339));
                attackAnimation = new Animation<>(ConstEnemies.TROLL_ANIM, frames, Animation.PlayMode.LOOP);
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
        if (hp <= 0 || pData.equals(Bomb.class.getSimpleName()) || pData.equals(Grenade.class.getSimpleName())
                || pData.equals(SpikeBomb.class.getSimpleName())) {
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
                            setPosition(body.getPosition().x - 1.5f, body.getPosition().y);
                        } else {
                            setPosition(body.getPosition().x - 1.5f, body.getPosition().y);
                        }
                        world.destroyBody(body);
                        setSize(ConstArmor.EFFECT_BIG_SIZE, ConstArmor.EFFECT_BIG_SIZE + 0.6f);
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
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.3f, body.getPosition().y - ConstEnemies.TROLL_HEAD_DIFF_SIZE - 0.1f);
                } else {
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.2f, body.getPosition().y - ConstEnemies.TROLL_HEAD_DIFF_SIZE - 0.1f);
                }
                break;
            case ATTACK:
                setSize(ConstEnemies.TROLL_WIDTH + 0.8f, ConstEnemies.TROLL_HEIGHT + 1.8f);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.TROLL_MEDIUM_DAMAGE);
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.3f, body.getPosition().y - ConstEnemies.TROLL_HEAD_DIFF_SIZE + 0.2f);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.3f, body.getPosition().y - ConstEnemies.TROLL_HEAD_DIFF_SIZE + 0.2f);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.TROLL_WIDTH, ConstEnemies.TROLL_HEIGHT);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.TROLL_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.3f, body.getPosition().y - ConstEnemies.TROLL_HEAD_DIFF_SIZE - 0.1f);
                } else {
                    body.setLinearVelocity(-ConstEnemies.TROLL_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.2f, body.getPosition().y - ConstEnemies.TROLL_HEAD_DIFF_SIZE - 0.1f);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.TROLL_MEDIUM_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.TROLL_MEDIUM_SCORE;
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
        fixtureDef.density = ConstEnemies.TROLL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.TROLL_BODY_SIZE, ConstEnemies.TROLL_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.TROLL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.TROLL_HEAD_SIZE, ConstEnemies.TROLL_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.TROLL_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}

