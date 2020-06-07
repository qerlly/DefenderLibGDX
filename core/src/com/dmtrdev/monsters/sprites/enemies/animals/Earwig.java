package com.dmtrdev.monsters.sprites.enemies.animals;

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
import com.dmtrdev.monsters.sprites.armors.bombs.Bomb;
import com.dmtrdev.monsters.sprites.armors.bombs.Grenade;
import com.dmtrdev.monsters.sprites.armors.bombs.Molotov;
import com.dmtrdev.monsters.sprites.armors.bombs.SpikeBomb;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.armors.medieval.IronSpear;
import com.dmtrdev.monsters.sprites.armors.medieval.PrimitiveSpear;
import com.dmtrdev.monsters.sprites.armors.ninja.ModernShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.PrimitiveShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.StandardShuriken;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class Earwig extends Enemy {

    public Earwig(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.EARWIG_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 20);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.EARWIG_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 25);
            } else {
                hp = ConstEnemies.EARWIG_HP / 2;
            }
        } else {
            hp = ConstEnemies.EARWIG_HP;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getAnimalsAtlas().findRegion("earwing_run"), j * 201, i * 143, 201, 143));
                runAnimation = new Animation<>(ConstEnemies.ANIMALS_ANIM - 0.03f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getAnimalsAtlas().findRegion("earwing_attack"), j * 201, i * 143, 201, 143));
                attackAnimation = new Animation<>(ConstEnemies.ANIMALS_ANIM, frames, Animation.PlayMode.LOOP);
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
        setSize(ConstEnemies.EARWIG_SIZE_X, ConstEnemies.EARWIG_SIZE_Y);
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        hp -= pDamage;
        if (hp <= 0 || pData.equals(Molotov.class.getSimpleName())) {
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
                            DefenderOfNature.getDeathBigSound().play(screen.getOptions().getSoundVolume());
                        }
                        if (direction) {
                            setPosition(body.getPosition().x - getWidth() / 2 + 1, body.getPosition().y - 0.3f);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2 + 0.6f, body.getPosition().y - 0.3f);
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
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.EARWIG_DAMAGE);
                    attackTime = 0;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 , body.getPosition().y - 0.7f);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.7f);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.EARWIG_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.7f);
                } else {
                    body.setLinearVelocity(-ConstEnemies.EARWIG_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    setPosition(body.getPosition().x - getWidth() / 2 , body.getPosition().y - 0.7f);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.EARWIG_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.EARWIG_SCORE;
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
        vertice[0] = new Vector2(-1.7f, ConstEnemies.LEGS_SIZE);
        if(direction) {
            vertice[1] = new Vector2(-1.3f, ConstEnemies.LEGS_SIZE + 0.3f);
            vertice[2] = new Vector2(1.3f, ConstEnemies.LEGS_SIZE + 1.6f);
        } else {
            vertice[1] = new Vector2(-1.3f, ConstEnemies.LEGS_SIZE + 1.6f);
            vertice[2] = new Vector2(1.3f, ConstEnemies.LEGS_SIZE + 0.3f);
        }
        vertice[3] = new Vector2(1.7f, ConstEnemies.LEGS_SIZE);
        corpus.set(vertice);
        fixture.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixture.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixture.shape = corpus;
        fixture.density = ConstEnemies.ANIMAL_DENSITY;
        body.createFixture(fixture).setUserData(this);
        body.setFixedRotation(true);
    }
}
