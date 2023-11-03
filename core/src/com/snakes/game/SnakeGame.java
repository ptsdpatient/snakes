package com.snakes.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.InputProcessor;

public class SnakeGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture playerTexture,wall,grass;
	Sprite player;
	Array<String> playerBody=new Array<>();
	float playerX=30,playerY=30,rotation=0f,playerSpeed=0.2f,targetRotation=0f,initialRotation=0f;


	public void handlePlayerDirection(float rotation,float speed){
		switch((int) rotation){
			case 0:
			case 360:
				playerX+=speed;break;
			case 90: playerY+=speed;break;
			case 180:
			case -180:
				playerX-=speed;break;
			case -90:
			case 270:
				playerY-=speed;break;
			default:break;
		}
	}
	@Override
	public void create () {
		inputhandler inputprocessor = new inputhandler();
		Gdx.input.setInputProcessor(inputprocessor);
		batch = new SpriteBatch();
		playerTexture = new Texture("./player/head.png");
		grass = new Texture("./level_1/background.png");
		wall = new Texture("./level_1/maze.png");
		player= new Sprite(playerTexture);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		handlePlayerDirection(rotation,playerSpeed);
		float delta = Gdx.graphics.getDeltaTime();


		if(rotation!=targetRotation) {
			Gdx.app.log("updateInfo", rotation + " , " + targetRotation);
			if(targetRotation==90.0f &&(rotation>=0f&&rotation<=90f)){
				rotation+=2;
			}else if(targetRotation==90f &&(rotation<=180f&&rotation>=90f)){
				rotation-=2;
			}else if(targetRotation==180f &&(rotation>=90f&&rotation<=180f)){
				rotation+=2;
			}else if(targetRotation==180f &&(rotation<=270f&&rotation>=180f)){
				rotation-=2;
			}else if(targetRotation==270f &&(rotation<=270f&&rotation>=180f)){
				rotation+=2;
			}else if(targetRotation==270f &&((rotation>=270f&&rotation<=360f))){
				Gdx.app.log("this is : ","no nigga!");
			}else if(targetRotation==360f &&(rotation<=360f&&rotation>=270f)){
				rotation+=2;
			}else if(targetRotation==0f &&(rotation>=0f&&rotation<=90f)){
				rotation-=2;
			}else if(rotation<=0 && rotation>=-90){if(rotation==-90){rotation=targetRotation;}rotation-=2;}
		}
		batch.begin();
		batch.draw(grass, 0, 0);
		batch.draw(wall,0,0);
		batch.draw(player,playerX,playerY,5,10,30,20,1,1,rotation);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
		grass.dispose();
	}

	public class inputhandler implements InputProcessor{
		@Override
		public boolean keyDown(int keycode) {
			if(targetRotation==rotation){
			switch(keycode){
				case Input.Keys.UP: if(rotation!=270f){if(targetRotation==360){rotation=0;} targetRotation=90f;}break;
				case Input.Keys.DOWN: if(rotation!=90f){if(targetRotation==360){rotation=0;} targetRotation=270f;}break;
				case Input.Keys.LEFT: if(rotation!=0f){targetRotation=180f;}break;
				case Input.Keys.RIGHT: if(rotation!=180f && rotation!=360){if(rotation==-90f||rotation==270f){targetRotation=360f;}else targetRotation=0f;} break;
				default : break;
			}}
			Gdx.app.log("rotationInfo",initialRotation+" , "+targetRotation);
			if(keycode==Input.Keys.SPACE){
				playerSpeed=0.5f;
			}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			if(keycode==Input.Keys.SPACE){
				playerSpeed=0.2f;
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
