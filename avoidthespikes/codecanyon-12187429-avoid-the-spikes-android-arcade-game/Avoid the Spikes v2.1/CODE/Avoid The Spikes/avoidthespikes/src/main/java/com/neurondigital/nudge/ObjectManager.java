package com.neurondigital.nudge;

import java.util.ArrayList;

import android.graphics.Canvas;

public class ObjectManager {
	public int time = 0;
	Screen screen;
	Instance DifferentObjects[];
	Boolean DestroyOutOfScreen = true;
	int Direction = 0;

	public static final int TOP = 0, LEFT = 1, RIGHT = 2, BOTTOM = 3;

	ArrayList<Integer> starttime = new ArrayList<Integer>();
	ArrayList<Integer> endtime = new ArrayList<Integer>();
	ArrayList<Integer[]> objecttypes = new ArrayList<Integer[]>();
	ArrayList<Integer> generation_frequency = new ArrayList<Integer>();
	public ArrayList<Instance> Objects_live = new ArrayList<Instance>();

	public ObjectManager(Instance DifferentObjects[], Screen screen, Boolean DestroyOutOfScreen, int Direction) {
		this.DifferentObjects = DifferentObjects;
		time = 0;
		this.screen = screen;
		this.DestroyOutOfScreen = DestroyOutOfScreen;
		this.Direction = Direction;
	}

	public void restart() {
		time = 0;
	}

	public void add_timeperiod(int starttime, int endtime, Integer[] obstacletypes, int generation_frequency) {
		this.starttime.add(starttime);
		this.endtime.add(endtime);
		this.objecttypes.add(obstacletypes);
		this.generation_frequency.add(generation_frequency);
	}

	public void update() {
		time++;
		//generate objects
		for (int i = 0; i < starttime.size(); i++)
			generate_at_timeperiod(starttime.get(i), endtime.get(i), objecttypes.get(i), generation_frequency.get(i));

		//update all onscreen objects
		for (int i = 0; i < Objects_live.size(); i++) {
			Objects_live.get(i).Update();
			if (DestroyOutOfScreen && !Objects_live.get(i).inScreen()) {
				//destroy it
				if (Direction == LEFT && Objects_live.get(i).x > screen.ScreenWidth())
					Objects_live.remove(i);
				if (Direction == RIGHT && Objects_live.get(i).x < 0)
					Objects_live.remove(i);
				if (Direction == TOP && Objects_live.get(i).y < 0)
					Objects_live.remove(i);
				if (Direction == BOTTOM && Objects_live.get(i).y > screen.ScreenHeight())
					Objects_live.remove(i);
			}
		}

	}

	public void drawObjects(Canvas canvas) {
		//draw all onscreen objects
		for (int i = 0; i < Objects_live.size(); i++)
			Objects_live.get(i).draw(canvas);
	}

	private void generate_at_timeperiod(int starttime, int endtime, Integer[] obstacletypes, int generation_frequency) {
		if (((time >= starttime) && (endtime == -1)) || ((time >= starttime) && (time < endtime))) {
			if (time % (100 - generation_frequency) == 0) {
				int type = (int) (Math.random() * obstacletypes.length) + 1;
				Objects_live.add(DifferentObjects[obstacletypes[type - 1]].Clone());
				//clone sprite to rotate images
				Sprite temp = DifferentObjects[obstacletypes[type - 1]].sprite.Clone();
				temp.rotate((float) (Math.random() * 360));
				Objects_live.get(Objects_live.size() - 1).sprite = temp;

				if (Direction == RIGHT) {
					Objects_live.get(Objects_live.size() - 1).x = screen.ScreenWidth() + Objects_live.get(Objects_live.size() - 1).getWidth();
					Objects_live.get(Objects_live.size() - 1).y = (float) ((screen.ScreenHeight() * 0.8f * Math.random()) + (screen.ScreenHeight() * 0.2f));
				}
				if (Direction == LEFT) {
					Objects_live.get(Objects_live.size() - 1).x = -Objects_live.get(Objects_live.size() - 1).getWidth();
					Objects_live.get(Objects_live.size() - 1).y = (float) ((screen.ScreenHeight() * 0.8f * Math.random()) + (screen.ScreenHeight() * 0.2f));
				}
				if (Direction == TOP) {
					Objects_live.get(Objects_live.size() - 1).y = screen.ScreenHeight() + Objects_live.get(Objects_live.size() - 1).getHeight();
					Objects_live.get(Objects_live.size() - 1).x = (float) ((screen.ScreenWidth() * (0.8f - (float) ((float) Objects_live.get(Objects_live.size() - 1).getWidth() / (float) screen.ScreenWidth())) * Math.random()) + (screen.ScreenWidth() * 0.1f));
				}
				if (Direction == BOTTOM) {
					Objects_live.get(Objects_live.size() - 1).y = -Objects_live.get(Objects_live.size() - 1).getHeight();
					Objects_live.get(Objects_live.size() - 1).x = (float) ((screen.ScreenWidth() * 0.8f * Math.random()) + (screen.ScreenWidth() * 0.2f));
				}

			}
		}
	}
}
