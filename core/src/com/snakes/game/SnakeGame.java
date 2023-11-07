package com.snakes.game;

import static java.lang.StrictMath.atan;
import static java.lang.StrictMath.sqrt;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
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

	Array<GameObject> worldObject = new Array<GameObject>();
	float playerBodyUpdateTime,playerX=300,playerY=300,playerSpeed=0.5f,directionX,directionY;
	boolean hide=false,rotatePlayer=false,rotatePlayerClockwise=true;

	float[] playerVertices,collectibleVertices;

	Polygon playerBounds,collectibleBounds;

public static class GameObject{
	private Sprite sprite;
	private Texture spriteTexture;
	private float x,y,rotation,width,height;

	private Polygon boundingPolygon;
	public GameObject(float x,float y,float rotation,float width,float height,boolean block,String texture){
		this.spriteTexture=new Texture(texture);
		this.sprite = new Sprite(spriteTexture);
		this.x=x;
		this.y=y;
		this.rotation=rotation;
		this.width=width;
		this.height=height;
		float[] vertices= new float[]{
				0, 0,
				width, 0,
				width, height,
				0, height
		};
		sprite.setPosition(x,y);
		sprite.setSize(width,height);
		sprite.setOrigin(width/2,height/2);
		sprite.setRotation(rotation);
		this.boundingPolygon=new Polygon(vertices);
		boundingPolygon.setPosition(x,y);
		boundingPolygon.setOrigin(width/2,height/2);


	}
	public Polygon getBoundingPolygon(){
		boundingPolygon.setPosition(x,y);
		return boundingPolygon;
	}
	public void render(SpriteBatch batch){
		sprite.draw(batch);
	}
	}

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

		GameObject wall1 = new GameObject(50,50,90,50,10,true,"maze-wall.png");
		GameObject wall2 = new GameObject(400,275,0,50,10,true,"maze-block.png");

		worldObject.add(wall1,wall2);

		playerVertices= new float[]{
				0, 0,
				player.getWidth(), 0,
				player.getWidth(), player.getHeight(),
				0, player.getHeight()
		};
		collectibleVertices= new float[]{
				0, 0,
				collectible.getWidth(), 0,
				collectible.getWidth(), collectible.getHeight(),
				0, collectible.getHeight()
		};
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
		collectibleBounds = new Polygon(collectibleVertices);
		playerBounds = new Polygon(playerVertices);
		playerBounds.setPosition(player.getX(),player.getY());

		if(Intersector.overlapConvexPolygons(playerBounds,collectibleBounds)){
		hide =true;
		}

		playerBodyUpdateTime += delta;
		if (playerBodyUpdateTime >= ((playerSpeed==0.5f)?0.55f:0.35f)) {
			playerBody.add(player.getX() + "," + player.getY() + "," + player.getRotation());
			for (int i = 0; i < playerBody.size - 1; i++) {
				playerBody.add(playerBody.get(i+1));
				playerBody.pop();
				if(playerBody.size>5) playerBody.removeIndex(0);
			}
			playerBodyUpdateTime = 0;
		}

		for(GameObject go : worldObject){
			if(Intersector.overlapConvexPolygons(playerBounds,go.getBoundingPolygon())){
				//player.setRotation(player.getRotation()+45f);
				//player.translate(-2.5f*directionX*playerSpeed, -2.5f*directionY*playerSpeed );
				Gdx.app.log("","");
			}
		}

		batch.begin();
		batch.draw(grass, 0, 0);

		//walls
		for(GameObject wall : worldObject){
			wall.render(batch);
		}

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
