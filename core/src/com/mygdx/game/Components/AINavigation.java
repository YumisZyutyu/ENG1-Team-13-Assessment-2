package com.mygdx.game.Components;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.utils.Utilities;

import static com.mygdx.utils.Constants.*;

public class AINavigation extends Component implements Steerable<Vector2> {
    private static class Attributes {
        public float boundingRadius = 64;
        public float maxSpd = 5000;
        public float maxAcc = 500;
        public float maxAngSpd = 0;
        public float maxAngAcc = 0;
        public boolean isTagged = false;
    }

    RigidBody rb;
    Transform t;
    Attributes attributes;
    SteeringBehavior<Vector2> behavior;
    SteeringAcceleration<Vector2> steeringOutput;

    public AINavigation() {
        super();
        attributes = new Attributes();
        setRequirements(ComponentType.RigidBody);
        type = ComponentType.AINavigation;
        steeringOutput = new SteeringAcceleration<>(new Vector2());
    }

    public void setBehavior(SteeringBehavior<Vector2> behavior) {
        this.behavior = behavior;
    }

    @Override
    public void update() {
        super.update();
        if (rb == null){
            rb = parent.getComponent(RigidBody.class);
            t = parent.getComponent(Transform.class);
        }
        if (behavior != null){
            behavior.calculateSteering(steeringOutput);
            applySteering();
        }
    }

    private void applySteering() {
        boolean anyAcc = false;
        if (!steeringOutput.linear.isZero()) {
            Vector2 f = steeringOutput.linear.scl(PHYSICS_TIME_STEP);
            rb.applyForce(f);
            anyAcc = true;
        }

        if (anyAcc) {
            Vector2 vel = rb.getVelocity();
            float speed = vel.len2();
            if(speed > attributes.maxSpd * attributes.maxSpd) {
                rb.setVelocity(vel.scl(attributes.maxSpd / (float) Math.sqrt(speed)));
            }
        }
    }

    @Override
    public Vector2 getLinearVelocity() {
        return rb.getVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return rb.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return attributes.boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return attributes.isTagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        attributes.isTagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.01f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {

    }

    @Override
    public float getMaxLinearSpeed() {
        return attributes.maxSpd;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        attributes.maxSpd = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return attributes.maxAcc;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        attributes.maxAcc = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return attributes.maxAngSpd;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        attributes.maxAngSpd = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return attributes.maxAngAcc;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        attributes.maxAngAcc = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return t.getPosition();
    }

    @Override
    public float getOrientation() {
        return t.getRotation();
    }

    @Override
    public void setOrientation(float orientation) {

    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return Utilities.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Utilities.angleToVector(outVector, angle);
    }

    @Override
    public Location<Vector2> newLocation() {
        return t;
    }
}