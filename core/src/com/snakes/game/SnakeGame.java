package com.snakes.game;

import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.sqrt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputProcessor;

import java.util.Arrays;

public class SnakeGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture playerTexture,wall,grass,collectibleTexture,playertailTexture,playerbodyTexture;
	Sprite player,collectible;
	//String[] playerBody={"www","www","www","www"};
	Array<String> playerBody=new Array<>();
	float timeElapsed,playerX=300,playerY=300,playerSpeed=0.5f,mouseX,mouseY,angleRadians,angleDegrees,directionX,directionY,playerBodyDelayCount=2f;
	double distance;

	@Override
	public void create () {
		Gdx.graphics.setWindowedMode(640, 480);
		Gdx.graphics.setResizable(false);
		inputhandler inputprocessor = new inputhandler();
		Gdx.input.setInputProcessor(inputprocessor);
		batch = new SpriteBatch();



		//texture initialization
		playerTexture = new Texture("./player/head.png");
		playerbodyTexture = new Texture("./player/body.png");
		playertailTexture = new Texture("./player/tail.png");
		collectibleTexture = new Texture("./collectible/flower.png");
		grass = new Texture("./level_1/background.png");
		wall = new Texture("./level_1/maze.png");

		//Sprites initialization
		player= new Sprite(playerTexture);
		player.setSize(30,25);
		player.setOrigin(15,12);
		player.setPosition(playerX,playerY);
		collectible= new Sprite(collectibleTexture);
		collectible.setSize(30,70);
		collectible.setPosition(200f,200f);
		collectible.setOrigin(15,35);



	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		float delta = Gdx.graphics.getDeltaTime();
		mouseX = Gdx.input.getX();
		mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
		angleRadians = MathUtils.atan2(mouseY - player.getY(), mouseX - player.getX());
		angleDegrees = MathUtils.radiansToDegrees * angleRadians;
		directionX = MathUtils.cosDeg(player.getRotation());
		directionY = MathUtils.sinDeg(player.getRotation());

		player.setRotation(angleDegrees);
		player.translate(directionX*0.5f, directionY*0.5f );

		timeElapsed += delta;
		if (timeElapsed >= 0.6f) {
			playerBody.add(player.getX() + "," + player.getY() + "," + player.getRotation());
			for (int i = 0; i < playerBody.size - 1; i++) {
				playerBody.add(playerBody.get(i+1));
				playerBody.pop();
				if(playerBody.size>5) playerBody.removeIndex(0);
			}
			Gdx.app.log("playerbody is: ", playerBody.size+"");
			timeElapsed = 0;
		}




		batch.begin();
		batch.draw(grass, 0, 0);
		batch.draw(wall,0,0);
		player.draw(batch);
		/*for (int i = 0; i < playerBody.length - 1; i++) {
			String[] props = playerBody[i].split(",");
			Sprite body = new Sprite(playerbodyTexture);
			body.setSize(30,25);
			body.setOrigin(15,12);
			body.setPosition(Float.parseFloat(props[0]),Float.parseFloat(props[1]));
			body.setRotation(Float.parseFloat(props[2]));
			body.draw(batch);
		}*/
		for (int i = 0; i < playerBody.size - 1; i++) {
			String[] props = playerBody.get(i).split(",");
			if (props.length != 3) {
				System.out.println("Invalid number of properties in playerBody[" + i + "]: " + playerBody.get(i));
				continue; // Skip this iteration if the properties are invalid.
			}
			try {
				float x = Float.parseFloat(props[0]);
				float y = Float.parseFloat(props[1]);
				float rotation = Float.parseFloat(props[2]);
				Sprite body = new Sprite((i!=0)?playerbodyTexture:playertailTexture);
				body.setSize(30, 25);
				body.setOrigin(15, 12);
				body.setPosition(x, y);
				body.setRotation(rotation);
				body.draw(batch);
			} catch (NumberFormatException e) {
				System.out.println("Invalid float value in playerBody[" + i + "]: " + playerBody.get(i));
			}
		}
		collectible.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
		playerbodyTexture.dispose();
		playertailTexture.dispose();
		grass.dispose();
	}

	public class inputhandler implements InputProcessor{
		@Override
		public boolean keyDown(int keycode) {

			//Gdx.app.log("rotationInfo",initialRotation+" , "+targetRotation);
			if(keycode==Input.Keys.SPACE){
				playerSpeed=0.7f;
			}

			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			if(keycode==Input.Keys.SPACE){
				playerSpeed=0.5f;
			}
			return false;
		}

		@Override
		public boolean keyTyped(char character) {

			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {

			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}
	}
}
