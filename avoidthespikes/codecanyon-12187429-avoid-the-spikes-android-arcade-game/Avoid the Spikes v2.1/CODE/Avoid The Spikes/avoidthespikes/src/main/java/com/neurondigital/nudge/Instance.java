package com.neurondigital.nudge;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Instance {
    public float x, y, speedx = 0, speedy = 0, accelerationx = 0, accelerationy = 0;
    public Sprite sprite;
    Screen screen;
    Physics physics = new Physics();
    boolean world = true;
    public int tag = 0;

    //animation variables
    public float scale = 100, TargetScale = 100;
    float animationSpeed;
    public boolean animationReady = true, animationChanged = false, animationKeepRotation = false;
    AnimationReadyListener AnimationReadyListener;
    int animation_id;

    public int collision_offset_x = 0, collision_offset_y = 0, collision_offset_height = 0, collision_offset_width = 0;

    //Constructors_____________________________________________________________________________________________

    /**
     * Create new instance
     *
     * @param sprite sprite to draw on screen
     * @param x      x-coordinate to draw instance
     * @param y      y-coordinate to draw instance
     * @param screen A reference to the main nudge engine screen instance
     * @param world  true if you wish to draw the instance relative to the camera or false if you wish to draw it relative to screen
     */
    public Instance(Sprite sprite, float x, float y, Screen screen, boolean world) {
        this.sprite = sprite;
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.world = world;
    }

    public Instance(Sprite sprite, float x, float y, Screen screen, boolean world, int tag) {
        this.sprite = sprite;
        this.screen = screen;
        this.x = x;
        this.y = y;
        this.world = world;
        this.tag = tag;
    }

    //General____________________________________________________________________________________________________
    //update the Object
    public void Update() {
        x += speedx;
        y += speedy;
        speedx += accelerationx;
        speedy += accelerationy;

        if (!animationReady) {
            //scale animation
            animationChanged = false;

            if (scale > TargetScale) {
                scale -= animationSpeed;
                if (animationKeepRotation)
                    sprite.setScale_keeprotation(scale);
                else
                    sprite.setScale(scale);

                animationChanged = true;
            } else if (scale < TargetScale) {
                scale += animationSpeed;
                if (animationKeepRotation)
                    sprite.setScale_keeprotation(scale);
                else
                    sprite.setScale(scale);
                animationChanged = true;
            }
            if (Math.abs(scale - TargetScale) <= animationSpeed) {
                scale = TargetScale;
                if (animationKeepRotation)
                    sprite.setScale_keeprotation(scale);
                else
                    sprite.setScale(scale);
            }

            if (!animationChanged) {
                animationReady = true;
                if (AnimationReadyListener != null)
                    AnimationReadyListener.onAnimationReady(animation_id);
            }

        }
    }

    public void rotate(float direction) {
        sprite.rotate(direction);
    }

    public float getDirection() {
        return sprite.getDirection();
    }

    public int getHeight() {
        return sprite.getHeight();
    }

    public int getWidth() {
        return sprite.getWidth();
    }

    public Instance Clone() {
        Instance clone = new Instance(this.sprite, this.accelerationx, this.accelerationy, screen, world);
        clone.speedx = this.speedx;
        clone.speedy = this.speedy;
        clone.x = this.x;
        clone.y = this.y;
        clone.tag = this.tag;
        clone.scale = 100;
        clone.TargetScale = 100;
        return clone;
    }

    //Draw_______________________________________________________________________________________________________
    //draw the sprite to screen
    public void draw(Canvas canvas) {
        //draw image
        if (world)
            sprite.draw(canvas, screen.ScreenX((int) x), screen.ScreenY((int) y));
        else
            sprite.draw(canvas, x, y);

        if (screen.debug_mode)
            physics.drawDebug(canvas);
    }

    //draw the sprite to screen
    public void draw(Canvas canvas, Paint paint) {
        //draw image
        if (world)
            sprite.draw(canvas, screen.ScreenX((int) x), screen.ScreenY((int) y), paint);
        else
            sprite.draw(canvas, x, y, paint);

        if (screen.debug_mode)
            physics.drawDebug(canvas);
    }

    //touch and collisions________________________________________________________________________________________
    public void setCollision_dimensions(int offset_x, int offset_y, int offset_width, int offset_height) {
        collision_offset_x = offset_x;
        collision_offset_y = offset_y;
        this.collision_offset_height = offset_height;
        this.collision_offset_width = offset_width;
    }

    public boolean isTouched(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        if (world)
            return physics.intersect(screen.ScreenX((int) x), screen.ScreenY((int) y), sprite.getWidth(), (int) sprite.getHeight(), (int) event.getX(pointerIndex), (int) event.getY(pointerIndex));
        else
            return physics.intersect((int) x, (int) y, sprite.getWidth(), (int) sprite.getHeight(), (int) event.getX(pointerIndex), (int) event.getY(pointerIndex));
    }

    public boolean CollidedWith(Instance b) {

        return CollidedWith((int) b.x + b.collision_offset_x, (int) b.y - b.collision_offset_y, b.getWidth() - b.collision_offset_width, b.getHeight() - b.collision_offset_height, b.world);

    }

    /**
     * Test for collision to a rectangle
     *
     * @param x                x-coordinate of first point
     * @param y                y-coordinate of first point
     * @param width            Rectangle width
     * @param height           rectangle height
     * @param worldCoordinates true if the rectangle is relative to the camera or false if it's relative to screen coordinates.
     */
    public boolean CollidedWith(int x, int y, int width, int height, boolean worldCoordinates) {
        if (world) {
            if (worldCoordinates)
                return physics.intersect(screen.ScreenX((int) this.x + collision_offset_x), screen.ScreenY((int) this.y + collision_offset_y), sprite.getWidth() - collision_offset_width, (int) sprite.getHeight() - collision_offset_height, screen.ScreenX(x), screen.ScreenY(y), width, height);
            else
                return physics.intersect(screen.ScreenX((int) this.x + collision_offset_x), screen.ScreenY((int) this.y + collision_offset_y), sprite.getWidth() - collision_offset_width, (int) sprite.getHeight() - collision_offset_height, x, y, width, height);

        } else {
            if (worldCoordinates)
                return physics.intersect((int) this.x + collision_offset_x, (int) this.y + collision_offset_y, sprite.getWidth() - collision_offset_width, (int) sprite.getHeight() - collision_offset_height, screen.ScreenX(x), screen.ScreenY(y), width, height);
            else
                return physics.intersect((int) this.x + collision_offset_x, (int) this.y + collision_offset_y, sprite.getWidth() - collision_offset_width, (int) sprite.getHeight() - collision_offset_height, x, y, width, height);

        }
    }

    /**
     * Test if instance is within screen
     */
    public boolean inScreen() {
        if (world)
            return physics.intersect(screen.ScreenX((int) x), screen.ScreenY((int) y), sprite.getWidth(), (int) sprite.getHeight(), 0, 0, screen.ScreenWidth(), screen.ScreenHeight());
        else
            return physics.intersect((int) x, (int) y, sprite.getWidth(), (int) sprite.getHeight(), 0, 0, screen.ScreenWidth(), screen.ScreenHeight());
    }

    //transform_____________________________________________________________________________________________________

    public void Highlight(int color) {
        sprite.Highlight(color);
    }

    public void unHighlight() {
        sprite.unHighlight();
    }

    //animation_____________________________________________________________________________________________________
    public void animateScale(float TargetScale, float animationSpeed, int id) {
        if (TargetScale != scale) {
            this.animationSpeed = animationSpeed;
            this.TargetScale = TargetScale;
            animationReady = false;
            this.animation_id = id;
            this.animationKeepRotation = false;
        }
    }

    public void animateScale(float TargetScale, float animationSpeed, int id, Boolean keepRotation) {
        if (TargetScale != scale) {
            this.animationSpeed = animationSpeed;
            this.TargetScale = TargetScale;
            animationReady = false;
            this.animation_id = id;
            this.animationKeepRotation = keepRotation;
        }
    }

    //animation ready interface
    public interface AnimationReadyListener {
        public void onAnimationReady(int id);
    }

    public synchronized void setAnimationReadyListener(AnimationReadyListener AnimationReadyListener) {
        this.AnimationReadyListener = AnimationReadyListener;
    }

}
