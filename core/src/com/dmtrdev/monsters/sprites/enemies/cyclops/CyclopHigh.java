package com.dmtrdev.monsters.sprites.enemies.cyclops;

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
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.medieval.IronSpear;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveSpear;
import com.dmtrdev.monsters.sprites.armors.ninja.NinjaKnife;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class CyclopHigh extends Enemy {

    public CyclopHigh(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.CYCLOP_HIGH_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 40);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.CYCLOP_HIGH_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 40);
            } else {
                hp = ConstEnemies.CYCLOP_HIGH_HP / 2;
            }
        } else {
            hp = ConstEnemies.CYCLOP_HIGH_HP;
        }

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                if (j == 1 && i == 2) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getCyclopsAtlas().findRegion("cyclop_high_run"), i * 207, j * 149, 207, 149));
                runAnimation = new Animation<>(ConstEnemies.CYCLOP_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                if (j == 1 && i == 2) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getCyclopsAtlas().findRegion("cyclop_high_attack"), i * 205, j * 194, 205, 194));
                attackAnimation = new Animation<>(ConstEnemies.CYCLOP_ANIM, frames, Animation.PlayMode.LOOP);
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
        if(pData.equals(NinjaKnife.class.getSimpleName()) || pData.equals(PrimitiveSpear.class.getSimpleName())
                || pData.equals(IronSpear.class.getSimpleName())) {
            hp -= 40;
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
                            setPosition(body.getPosition().x - getWidth() / 2 + 1.55f, body.getPosition().y + 0.4f);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2 + 1.35f, body.getPosition().y + 0.4f);
                        }
                        world.destroyBody(body);
                        destroy = true;
                        time = 0;
                        setSize(3.7f, 4.4f);
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
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.4f, body.getPosition().y - 1.2f);
                } else {
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.4f, body.getPosition().y - 1.2f);
                }
                break;
            case ATTACK:
                setSize(ConstEnemies.CYCLOP_SIZE, ConstEnemies.CYCLOP_SIZE + 2);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    if(takenDamage != ConstArmor.CARNIVORE_PLANT_DAMAGE) {
                        collisionPlant(takenDamage);
                    }                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.CYCLOP_HIGH_DAMAGE);
                    if(tree.getPlayer().getBody().getLinearVelocity().y == 0) {
                        tree.getPlayer().collision(direction, new Vector2(1, 3));
                    }
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1.5f);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1.5f);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.CYCLOP_SIZE - 0.3f, ConstEnemies.CYCLOP_SIZE);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.CYCLOP_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 + 0.4f, body.getPosition().y - 1.2f);
                } else {
                    body.setLinearVelocity(-ConstEnemies.CYCLOP_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 - 0.4f, body.getPosition().y - 1.2f);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.CYCLOP_HIGH_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.CYCLOP_HIGH_SCORE;
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
        fixtureDef.density = ConstEnemies.CYCLOP_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.CYCLOP_BODY_SIZE, ConstEnemies.CYCLOP_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.CYCLOP_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.CYCLOP_HEAD_SIZE, ConstEnemies.CYCLOP_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.CYCLOP_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}
