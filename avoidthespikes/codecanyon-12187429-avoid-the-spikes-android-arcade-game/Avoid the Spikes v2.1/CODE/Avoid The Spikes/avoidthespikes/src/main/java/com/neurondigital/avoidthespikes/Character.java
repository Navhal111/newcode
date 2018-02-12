package com.neurondigital.avoidthespikes;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.neurondigital.nudge.Instance;
import com.neurondigital.nudge.Screen;
import com.neurondigital.nudge.Sprite;

import java.util.ArrayList;

public class Character extends Instance {
    Screen screen;
    float INITIAL_X;
    private boolean collided = false;
    private int angle = 0;
    ArrayList<Integer> tail_x = new ArrayList<Integer>();
    ArrayList<Integer> tail_y = new ArrayList<Integer>();

    Paint tail_Paint = new Paint();

    public Character(Screen screen) {
        super(new Sprite(BitmapFactory.decodeResource(screen.getResources(), R.drawable.newcharecter), screen.ScreenWidth() * 0.12f), 0, 0, screen, true);
        this.screen = screen;
        reset();

        //tail paint
        tail_Paint.setAntiAlias(true);
        tail_Paint.setColor(screen.getResources().getColor(R.color.color_character_tail));

    }

    public void reset() {
        this.x = INITIAL_X = (screen.ScreenWidth() * Configuration.CHARACTER_INITIAL_X) - (this.sprite.getWidth() / 2);
        this.y = this.sprite.getHeight() + (screen.ScreenHeight() * 0.1f);
        setCollision_dimensions((int) (sprite.getWidth() * 0.125f), 0, (int) (sprite.getWidth() * 0.25f), (int) (sprite.getHeight() * 0.3f));

        speedx = screen.ScreenHeight() * 0.001f * Configuration.CHARACTER_SPEED_SIDE;
        speedy = screen.ScreenHeight() * 0.001f * Configuration.CHARACTER_SPEED_UP;
        accelerationy = 0;
        accelerationx = 0;

        //reset Collision
        collided = false;
        angle = 0;
        rotate(angle);

        //refresh tail
        tail_x.clear();
        tail_y.clear();
        for (int i = 0; i < Configuration.CHARACTER_TAIL_LENGTH * Configuration.CHARACTER_TAIL_SEPARATION; i++) {
            tail_x.add(-screen.ScreenWidth());//out of screen
            tail_y.add(-screen.ScreenHeight());//out of screen
        }
    }

    public void toggleDirection() {
        speedx = -speedx;
        sprite.flip(Sprite.HORIZONTAL);
    }

    public void fall() {
        accelerationy = -screen.ScreenHeight() * 0.0001f * Configuration.CHARACTER_FALL_ACCELERATION;
        collided = true;
    }


    @Override
    public void Update() {
        super.Update();

        if (collided) {
            angle += Configuration.CHARACTER_ROTATION_SPEED;
            rotate(angle);
        }

        for (int i = tail_x.size() - 1; i > 0; i--) {
            tail_x.set(i, tail_x.get(i - 1));
            tail_y.set(i, tail_y.get(i - 1));
        }
        tail_x.set(0, (int) this.x + (getWidth() / 2));
        tail_y.set(0, (int) this.y - (getHeight() / 2));

    }


    public void draw(Canvas canvas) {

        for (int i = Configuration.CHARACTER_TAIL_SEPARATION; i < tail_x.size(); i += Configuration.CHARACTER_TAIL_SEPARATION) {
            canvas.drawCircle(screen.ScreenX(tail_x.get(i)), screen.ScreenY(tail_y.get(i)), Configuration.CHARACTER_TAIL_SIZE * (Configuration.CHARACTER_TAIL_LENGTH - (i / Configuration.CHARACTER_TAIL_SEPARATION)), tail_Paint);
        }
        super.draw(canvas);
    }
}
