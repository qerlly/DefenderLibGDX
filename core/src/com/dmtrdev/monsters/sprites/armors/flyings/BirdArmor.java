package com.dmtrdev.monsters.sprites.armors.flyings;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class BirdArmor extends Armor {

    public BirdArmor(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("dropping_drop"));
        setSize(ConstArmor.CORN_SIZE, ConstArmor.CORN_SIZE + 0.6f);
        setOriginCenter();
        textureRegion = DefenderOfNature.getArmorsAtlas().findRegion("dropping_splat");
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.SPLAT_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getTomatoSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() + 0.3f);
                world.destroyBody(body);
                destroy = true;
                setSize(1.5f, 1.2f);
            } else if (time >= 0.35f) {
                destroyed = true;
            }
            setRegion(textureRegion);
            time += delta;
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.CORN_BODY_SIZE + 0.1f);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.CORN_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}