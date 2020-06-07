package com.dmtrdev.monsters.sprites.armors.ground;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class StoneTrap extends Armor {

    private final Stone mStone;

    public StoneTrap(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(6.5f, 9);
        setOriginCenter();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("star_explode"), j * 225, i * 213, 225, 213));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();

        mStone = new Stone(pPlayScreen, body.getPosition().x, body.getPosition().y + 3, pDirection);
        textureRegion = new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("gibbet"), 499, 0, 499, 425);
        if (direction) {
            textureRegion.flip(true, false);
        }
        setRegion(textureRegion);
    }

    @Override
    public void draw(final Batch pBatch) {
        super.draw(pBatch);
        mStone.draw(pBatch);
    }

    @Override
    public void collisionEnemy() {
    }

    @Override
    public void collisionEnemy(final int pDamage, final boolean pCollision) {
    }

    @Override
    public void collisionGround() {
    }

    @Override
    public int getArmorDamage() {
        return 1;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y);
            mStone.update(delta);
            if(mStone.getDestroyed()){
                setToDestroy = true;
            }
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), ConstEnemies.SPAWN_HEIGHT);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.PLANT_BODY_WIDTH, ConstArmor.PLANT_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.PLANT_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
