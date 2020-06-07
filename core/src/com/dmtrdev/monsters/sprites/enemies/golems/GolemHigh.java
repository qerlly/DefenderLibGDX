package com.dmtrdev.monsters.sprites.enemies.golems;

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
import com.dmtrdev.monsters.sprites.armors.bombs.Bomb;
import com.dmtrdev.monsters.sprites.armors.bombs.Grenade;
import com.dmtrdev.monsters.sprites.armors.bombs.SpikeBomb;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class GolemHigh extends Enemy {

    public GolemHigh(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.GOLEM_HIGH_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 50);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.GOLEM_HIGH_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 50);
            } else {
                hp = ConstEnemies.GOLEM_HIGH_HP / 2;
            }
        } else {
            hp = ConstEnemies.GOLEM_HIGH_HP;
        }

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getGolemsAtlas().findRegion("tantal_run"), 0, i * 265, 300, 265));
            runAnimation = new Animation<>(ConstEnemies.GOLEM_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                if (j == 2 && i == 1) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getGolemsAtlas().findRegion("tantal_attack"), i * 300, j * 300, 300, 300));
                attackAnimation = new Animation<>(ConstEnemies.GOLEM_ANIM, frames, Animation.PlayMode.LOOP);
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
        switch (getState()) {
            case DIE:
                if (effects) {
                    if (!destroy) {
                        if (screen.getOptions().getSoundCheck()) {
                            DefenderOfNature.getDeathBigSound().play(screen.getOptions().getSoundVolume());
                        }
                        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y + 0.5f);
                        world.destroyBody(body);
                        setSize(4.3f, 5);
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
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - ConstEnemies.GOLEM_HEAD_DIFF_SIZE);
                } else {
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - ConstEnemies.GOLEM_HEAD_DIFF_SIZE);
                }
                break;
            case ATTACK:
                setSize(ConstEnemies.GOLEM_SIZE - 0.8f, ConstEnemies.GOLEM_SIZE + 1.5f);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.GOLEM_HIGH_DAMAGE);
                    if (tree.getPlayer().getBody().getLinearVelocity().y == 0) {
                        tree.getPlayer().collision(direction, new Vector2(2, 4));
                    }
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.4f, body.getPosition().y - ConstEnemies.GOLEM_HEAD_DIFF_SIZE);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.4f, body.getPosition().y - ConstEnemies.GOLEM_HEAD_DIFF_SIZE);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.GOLEM_SIZE - 1, ConstEnemies.GOLEM_SIZE + 0.4f);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.GOLEM_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - ConstEnemies.GOLEM_HEAD_DIFF_SIZE);
                } else {
                    body.setLinearVelocity(-ConstEnemies.GOLEM_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - ConstEnemies.GOLEM_HEAD_DIFF_SIZE);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.GOLEM_HIGH_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.GOLEM_HIGH_SCORE;
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
        fixtureDef.filter.maskBits = ConstGame.DISABLE_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.GROUND_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.GOLEM_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.GOLEM_BODY_SIZE, ConstEnemies.GOLEM_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.GOLEM_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.GOLEM_HEAD_SIZE, ConstEnemies.GOLEM_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.GOLEM_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}