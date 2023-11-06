package com.snakes.game;

import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.sqrt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
	Texture playerTexture,grass,collectibleTexture,playertailTexture,playerbodyTexture;

	Sprite player,collectible;
	Array<String> playerBody=new Array<>();
	float timeElapsed,playerX=300,playerY=300,playerSpeed=0.5f,directionX,directionY;
	boolean hide=false,rotatePlayer=false,rotatePlayerClockwise=true;



	Rectangle playerBounds,collectibleBounds;
	@Override
	public void create () {


		//Gdx.graphics.setWindowedMode(640, 480);
		//Gdx.graphics.setResizable(false);
		inputhandler inputprocessor = new inputhandler();
		Gdx.input.setInputProcessor(inputprocessor);
		batch = new SpriteBatch();


		//texture initialization
		playerTexture = new Texture("head.png");
		playerbodyTexture = new Texture("body.png");
		playertailTexture = new Texture("tail.png");
		collectibleTexture = new Texture("mushroom.png");
		grass = new Texture("background.png");


		//Sprites initialization
		player= new Sprite(playerTexture);
		player.setSize(30,25);
		player.setOrigin(15f,12.5f);
		player.setPosition(playerX,playerY);
		collectible= new Sprite(collectibleTexture);
		collectible.setSize(30,30);
		collectible.setPosition(200f,200f);
		collectible.setOrigin(15,35);



	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		float delta = Gdx.graphics.getDeltaTime();
		if(rotatePlayer){
			float rotation = player.getRotation();
			if ((rotatePlayerClockwise)) {
				player.setRotation(rotation - 2f);
			} else {
				player.setRotation(rotation + 2f);
			}
		}
		directionX = MathUtils.cosDeg(player.getRotation());
		directionY = MathUtils.sinDeg(player.getRotation());
		player.translate(directionX*playerSpeed, directionY*playerSpeed );

		//collider bounds initialization
		collectibleBounds = collectible.getBoundingRectangle();
		playerBounds = player.getBoundingRectangle();

		if(playerBounds.overlaps(collectibleBounds)){
		hide =true;
		}
		timeElapsed += delta;
		if (timeElapsed >= ((playerSpeed==0.5f)?0.55f:0.35f)) {
			playerBody.add(player.getX() + "," + player.getY() + "," + player.getRotation());
			for (int i = 0; i < playerBody.size - 1; i++) {
				playerBody.add(playerBody.get(i+1));
				playerBody.pop();
				if(playerBody.size>5) playerBody.removeIndex(0);
			}
			timeElapsed = 0;
		}

		batch.begin();
		batch.draw(grass, 0, 0);


		if(!hide){
			collectible.draw(batch);
		}

		for (int i = 0; i < playerBody.size - 1; i++) {
			String[] props = playerBody.get(i).split(",");
				float x = Float.parseFloat(props[0]);
				float y = Float.parseFloat(props[1]);
				float rotation = Float.parseFloat(props[2]);
				Sprite body = new Sprite((i!=0)?playerbodyTexture:playertailTexture);
				body.setSize(32f, 22f);
				body.setOrigin(16f, 11f);
				body.setPosition(x, y);
				body.setRotation(rotation);
				body.draw(batch);
		}
		player.draw(batch);
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
			if(keycode==Input.Keys.LEFT){
				rotatePlayer=true;
				rotatePlayerClockwise=false;
			}
			if(keycode==Input.Keys.RIGHT){
				rotatePlayer=true;
				rotatePlayerClockwise=true;
			}

			if(keycode== Input.Keys.UP){
				playerSpeed=0.8f;
			}

			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			if(keycode==Input.Keys.LEFT){
				rotatePlayer=false;
				rotatePlayerClockwise=false;
			}
			if(keycode==Input.Keys.RIGHT){
				rotatePlayer=false;
				rotatePlayerClockwise=true;
			}

			if(keycode== Input.Keys.UP){
				playerSpeed=0.5f;
			}
			return false;
		}

		@Override
		public boolean keyTyped(char character) { return false; }
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
