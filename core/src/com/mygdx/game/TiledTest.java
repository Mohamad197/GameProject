package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import static com.mygdx.game.Constants.LEVEL_ONE;
import static com.mygdx.game.Constants.LEVEL_TWO;
import static com.mygdx.game.Constants.MONSTER1;
import static com.mygdx.game.Constants.SOCKS;
import static com.mygdx.game.Constants.TSHIRT;
import static com.mygdx.game.Constants.UNDERWEAR;

/**
 * This class renders the tile map made with Tiled and shows it on the screen
 * Event handling is done using the observer pattern. InputProcessor, a listener interface, is implemented
 */

public class TiledTest extends ApplicationAdapter implements InputProcessor{
    public static final  int tileSize = 128; //tile in pixel
    private static int tileCountW = 15; //numbers of tiles in width
    private static int tileCountH = 8; //numbers of tiles in height
    private int animatioPlayerYpos;
    private int animatioPlayerXpos;
    //calculate the game world dimensions
    int tileWidth = 128;
    int tileHeight = 128;
    float oldX , oldY;

    public final static int mapWidth = tileSize * tileCountW;
    public final static int mapHeight = tileSize * tileCountH;
    private int NumberOfMovedTiles=2;

    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMapTileLayer Blockedlayer;
    private TiledMapTileLayer terrain;
    private InputMultiplexer multiplexer;


    private Item underwear,socks,tshirt;
    private Player girl; //animated player

    private Monster gazeti;
    private Monster yeti;

    private HUD hud ;
    SpriteBatch sp;



    int oneStepHorizontaly ;
    int twoStepsHorizontally;
    int oneStepVertically ;
    int twoStepsvertically ;

    private TiledMapTileLayer.Cell ground;
    private TiledMapTileLayer.Cell obstacles;

    int marginTop = 55; //parameterize as: screen height -1 -mapHeight
    int screenHeight = 1080;

    @Override
    public void create () {

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        sp=new SpriteBatch (  );
        hud = new HUD ( sp );
        screenHeight = Gdx.graphics.getHeight(); //this is here, since it seems it cannot be done at init time
        marginTop = screenHeight-1-mapHeight; //this depends on screenHeight so it needs to be done after that

        //set up an OrthographicCamera, set it to the dimensions of the screen and update() it.
        camera = new OrthographicCamera();
        camera.setToOrtho(false,width,screenHeight);
        camera.translate ( 128 ,128 );
        camera.update();
        //load map and create a renderer passing in our tiled map

        tiledMap = new TmxMapLoader().load(LEVEL_TWO);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        tiledMapRenderer.setView(camera);
        //Gdx.input.setInputProcessor(this);




        //player
        girl = new Player();
        girl.create();

        SoundEffect.newSoundEffect.create(new AssetManager()); //load audio
        GameSetting.newSetting.load(); //load audio settings
        SoundManager.newSoundManager.play(SoundEffect.newSoundEffect.backgroundMusic.musicSnowMap); //play background music


        //items
        underwear = new Item("underwear", UNDERWEAR, 256,256);
        socks=new Item("socks", SOCKS,768, 768);
        tshirt=new Item("tshirt", TSHIRT,1280, 384);


       //Monster Gazeti
      gazeti = new Monster(MONSTER1, 4, 3, 1, 1);
       yeti = new Monster();

    }
    // Initial render
    public void initialRender()
    {
        //set the background color to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //pass it in to the TiledMapRenderer with setView() and finally render() the map.
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        sp.setProjectionMatrix ( hud.stage.getCamera ().combined);
        hud.stage.draw ();
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(hud.stage);
        Gdx.input.setInputProcessor(multiplexer);
    }

    //Initial Item Render
    public void initialItemRender()
    {
        girl.render();
        girl.updateSpriteBatch(underwear);

        underwear.render();
        socks.render();
        tshirt.render();


        gazeti.render(782, 512); //spawn gazeti at the given position in the map
        yeti.render(128, 252); //spawn yeti at the given position in the map

        }
        //Gdx.input.setInputProcessor(hud.stage);


    //Player collide with Item
    private void playerCollideWithItem(Item item){
        item.setCollected(true);
        initialItemRender();

    }
    @Override
    public void dispose() {
        //free allocated memory by disposing the instance
        SoundEffect.newSoundEffect.backgroundMusic.musicSnowMap.stop();
    }
    @Override
    public void render () {
        //Calling initial render
        initialRender();
        initialItemRender();
        //Grab Item
        if(girl.getOldX ()>704 && girl.getOldX ()<832  && girl.getOldY()>704&& girl.getOldY()<832) {
            playerCollideWithItem(socks);
            }
        if(girl.getOldX ()>1216 && girl.getOldX ()<1344  && girl.getOldY()>320&& girl.getOldY()<448) {
            playerCollideWithItem(tshirt);
            }
            if(girl.getOldX ()>192 && girl.getOldX ()<320  && girl.getOldY()>192&& girl.getOldY()<320) {
                playerCollideWithItem(underwear);
            }
    }

    @Override
    public boolean keyDown(int keycode) {return false;}
    /**
     * Navigating around the map is simply a matter of moving around the camera. Move in 128px per tile size.
     * move left/right/up/down by one tile each time an arrow key is pressed. A/D/W/S for moving two tiles(no collision tho)
     * @param keycode The key pressed on the keyboard
     * @return true if a key is pressed.
     */
     public boolean keyUp(int keycode) {
            if (keycode == Input.Keys.LEFT){// one step left
             //   collisionL(differenceInPositionX * tileWidth, differenceInPositionY * tileHeight);
                }
            if (keycode == Input.Keys.A)    {  // 2 steps left
                girl.setCurrentAnimation(girl.getWalkAnimationLEFT());
                girl.move(-twoStepsHorizontally, 0);
                }
            if (keycode == Input.Keys.RIGHT)   {// one step right
                collisionR ();
            }
            if (keycode == Input.Keys.D)  {       // two steps step right
                girl.setCurrentAnimation(girl.getWalkAnimationRIGHT());
                girl.move(twoStepsHorizontally, 0);}
            if (keycode == Input.Keys.UP)    {        // one step up
                collisionU ();            }
            if (keycode == Input.Keys.W)  {          // 2 steps up
                girl.setCurrentAnimation(girl.getWalkAnimationUP());
                girl.move(0, twoStepsvertically); }
            if (keycode == Input.Keys.DOWN)    {     // one step down
                collisionD ();               }
            if (keycode == Input.Keys.S)    {      // 2 steps down
                girl.setCurrentAnimation(girl.getWalkAnimationDOWN());
                girl.move(0, -twoStepsvertically);}
            return false;
    }


    public void debugMe(){
        //Gdx.app.log("movement","ground: " + checkFirstLayer(ground) + " obstacles:" + checkSecondLayer(obstacles) );
        Gdx.app.log("movement","oldX: " + (oldX / tileWidth) + " oldY: " + (oldY / tileHeight) );
    }

    /**
      *check the collision on the left side. if the Properties is blocked the character will stay on the old x, y
     *
     */

    public void collisionL() {
        getProperties();
        girl.resetTimeTillIdle();
        ground = Blockedlayer.getCell((int) (oldX / tileWidth), (int) (oldY / tileHeight) + 1);

        debugMe();
        obstacles = terrain.getCell((int) (oldX / tileWidth), (int) (oldY / tileHeight) + 1);
        if (checkFirstLayer(ground) || checkSecondLayer(obstacles)) {
            girl.move(0, 0);
        } else {
            girl.setCurrentAnimation(girl.getWalkAnimationLEFT());
            girl.move(-oneStepHorizontaly, 0);
        }
    }

    /**
     *  collision for the right side
     */
    public void collisionR(){
        getProperties();
        girl.resetTimeTillIdle();
        ground = Blockedlayer.getCell((int) (oldX / tileWidth)+2 , (int) (oldY / tileHeight)+1);
        obstacles = terrain.getCell((int) (oldX / tileWidth)+2, (int) (oldY / tileHeight) + 1);

        debugMe();
        if (checkFirstLayer(ground) || checkSecondLayer(obstacles) ){
            girl.move ( 0,0 );}
        else
            {girl.setCurrentAnimation(girl.getWalkAnimationRIGHT ());
            girl.move(+oneStepHorizontaly, 0);}
    }


    public void collisionU(){
        getProperties();
        girl.resetTimeTillIdle(); //go back to idle state
        ground = Blockedlayer.getCell((int) (oldX / tileWidth)+1 , (int) (oldY / tileHeight)+2);
        obstacles = terrain.getCell((int) (oldX / tileWidth)+1, (int) (oldY / tileHeight) +2);

        debugMe();
        if(checkFirstLayer(ground) || checkSecondLayer(obstacles) ){
            girl.move ( 0,0 );}
        else
            {girl.setCurrentAnimation(girl.getWalkAnimationUP ());
            girl.move(0, +oneStepVertically);}
    }
    /**
     *  collision for the downward
     */
    public void collisionD(){
        getProperties();
        girl.resetTimeTillIdle();
        ground = Blockedlayer.getCell((int) (oldX / tileWidth)+1 , (int) (oldY / tileHeight));

        debugMe();
        obstacles = terrain.getCell((int) (oldX / tileWidth)+1, (int) (oldY / tileHeight));
        if ( checkFirstLayer(ground) || checkSecondLayer(obstacles) ){
            girl.move ( 0,0 );}
        else {
            girl.setCurrentAnimation(girl.getWalkAnimationDOWN());
            girl.move(0, -oneStepVertically);}
    }

    public boolean collisionCheck(int stepsX, int stepsY){
        getProperties();
        girl.resetTimeTillIdle(); //go back to idle state
        //Gdx.app.log("movement","ground: " + checkFirstLayer(ground) + " obstacles:" + checkSecondLayer(obstacles) );
        debugMe();
        boolean blocked = false;
        int posX = (int) (girl.getOldX () / tileWidth) + 1;
        int posY = (int) (girl.getOldY () / tileHeight) + 1;
        Gdx.app.log("movement","before move: X: " + posX + " Y: " + posY  );
        if(stepsY == 0 ){//horizontal movement
            int directionSign = Integer.signum(stepsX); //-1 for left, otherwise 1
            int limit = Math.abs( stepsX );
                ground = Blockedlayer.getCell(posX + directionSign , posY );
                obstacles = terrain.getCell(posX + directionSign , posY );
                blocked = checkFirstLayer(ground) || checkSecondLayer(obstacles) || blocked;
            if(limit == 2){
                //Gdx.app.log("movement","horizontal: oldX: " + (posX + directionSign) + " oldY: " +  posY  );
                ground = Blockedlayer.getCell(posX + directionSign*2 , posY );
                obstacles = terrain.getCell(posX + directionSign*2 , posY );
                blocked = checkFirstLayer(ground) || checkSecondLayer(obstacles) || blocked;
            }
        }
        else if(stepsX == 0){//vertical movement
            int directionSign = Integer.signum(stepsY); //-1 for left, otherwise 1
            int limit = Math.abs( stepsY );
                //Gdx.app.log("movement","directionSign: " + directionSign + " limit: " +  limit  );
                ground = Blockedlayer.getCell(posX , posY + directionSign );
                obstacles = terrain.getCell(posX , posY +  directionSign );
                blocked = checkFirstLayer(ground) || checkSecondLayer(obstacles) || blocked;
            if(limit==2){
                //Gdx.app.log("movement","vertical: oldX: " + posX + " oldY: " +  (posY + directionSign) );
                ground = Blockedlayer.getCell(posX , posY + directionSign*2 );
                obstacles = terrain.getCell(posX , posY +  directionSign*2 );
                blocked = checkFirstLayer(ground) || checkSecondLayer(obstacles) || blocked;
            }
        }
        else{ blocked = true;}
        //Gdx.app.log("movement","horizontal: oldX: " + (posX + directionSign*i) + " oldY: " +  posY  );
        return !blocked;
    }

    /**
     * assign the values of the tiles Properties
     */
    public void getProperties(){
         Blockedlayer = (TiledMapTileLayer)tiledMap.getLayers().get("background");
         terrain = (TiledMapTileLayer)tiledMap.getLayers().get("terrain");

         oldX = girl.getOldX () ;
         oldY = girl.getOldY ();
         tileWidth= (int) Blockedlayer.getTileWidth ();
         tileHeight= (int) Blockedlayer.getTileHeight ();

         oneStepHorizontaly = mapWidth / tileCountW;
         twoStepsHorizontally = mapWidth / tileCountW * NumberOfMovedTiles;
         oneStepVertically = mapHeight / tileCountH;
         twoStepsvertically = mapHeight / tileCountH * NumberOfMovedTiles;
    }

    /**
     *  This method checks whether a tile for the second layer contains the property "blocked"
     */
    public boolean checkSecondLayer(TiledMapTileLayer.Cell obstacle){
        if(obstacle != null) { //if it is not an empty cell
            return obstacle.getTile().getProperties().containsKey("blocked");}
        return false;     //else do nothing
    }

    /**
     * This method checks whether a tile from the first layer contains the property "blocked"
     * @param ground the first tile layer
     * @return false if it is an empty cell
     */

    public boolean checkFirstLayer(TiledMapTileLayer.Cell ground){
        if(ground != null) { //if it is not an empty cell
            return ground.getTile().getProperties().containsKey("blocked");}
        return false;     //else do nothing
    }

    @Override
    public boolean keyTyped(char character) {return false;}

    /**
     * Called when the user touches the screen
     *
     * */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
       return false;
    }


    /** This method converts screen Y position to simplified Y
    **/
    public int ScreenPosYtoSimplified(float PositionY){
        float temporary = (PositionY-(float) marginTop)/(float) tileSize;
       // Gdx.app.log("move","marginTop: " + marginTop + " tilesize: " + tileSize + "result" + temporary  );
        return (int) Math.floor( Math.max(0.0,temporary));
        //return (int) Math.floor( Math.max(0,(PositionY-56)/128.0));
    }




    /**
     *  This method checks whether a tile form the second layer contains property "exit"
     */

    public boolean isExitSecondLayer(TiledMapTileLayer.Cell obstacle){

        if(obstacle != null) { // not an empty cell

            return obstacle.getTile().getProperties().containsKey("exit");}

        return false;
    }

    /**
     * This method checks whether a tile from the first layer contains the property "exit"
     * @param ground the first tile layer
     * @return false if it is an empty cell
     */

    public boolean isExitFirstLayer(TiledMapTileLayer.Cell ground){

        if(ground != null) { // not an empty cell

            return ground.getTile().getProperties().containsKey("exit");}

        return false;
    }


    /**
     * This method converts screen X position to simplified X
     */
    public int ScreenPosXtoSimplified(float PositionX){ //convert screen X position to simplified X
        return (int) Math.floor( Math.max(0,PositionX/(float) tileSize));
    }

    public int simplifiedXtoScreenPos(int PositionX){ //convert simplified X to screen X position
        return PositionX*tileWidth;
    }

    public int simplifiedYtoScreenPos(int PositionY){ //convert simplified Y to screen Y position
        return PositionY*tileHeight+marginTop+tileHeight-1;
    }

    public int invertScreenPos(int PositionY){ //convert sprite position to screenPosition which in turn can be used in ScreenPosYtoSimplified()
        return screenHeight-marginTop-PositionY; //probably slightly wrong in the offset (+-1 or something like that), but works to convert sprite position
    }

    public int invertSimplifiedHeight(int simplified){
        return tileCountH-1-simplified;
    }

    /**
     *Called when the user lifts their finger from the screen.
     * We use touchUp instead of touchDown to avoid actions triggered by double clicks
     *
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int differenceInPositionX; //difference between simplified player position and simplified touch position in X
        int differenceInPositionY; //difference between simplified player position and simplified touch position in Y
        int touchPositionX = ScreenPosXtoSimplified(screenX); //simplified touch position X
        int touchPositionY = ScreenPosYtoSimplified(screenY); //simplified touch position Y
        //Gdx.app.log("move", "Clicked pos X: " + touchPositionX + " Set pos X:" + simplifiedXtoScreenPos(touchPositionX) );
        //Gdx.app.log("move", "screenY: " + screenY + " Simplified pos Y: " + touchPositionY + " Set pos Y:" + simplifiedYtoScreenPos(touchPositionY) );

        int playerPositionY = invertScreenPos((int) girl.getOldY()); //we need to invert the Y because the sprite is in a different coordinate system
        int playerPositionX = (int) girl.getOldX(); //(see playerPositionY comment) the different coordinate systems have identical X, so we don't manipulate oldX
        //Gdx.app.log("move", "girl.oldY: " + girl.getOldY() + " inverted: " + playerPositionY + " Simplified:" + ScreenPosYtoSimplified(playerPositionY));
        //Gdx.app.log("move", "girl.oldX: " + girl.getOldX() + " Simplified:" + ScreenPosXtoSimplified(playerPositionX));

        playerPositionX = ScreenPosXtoSimplified(playerPositionX);
        playerPositionY = ScreenPosYtoSimplified(playerPositionY);

        differenceInPositionX = touchPositionX - playerPositionX;
        differenceInPositionY = playerPositionY - touchPositionY;

        //We cannot use a switch statement, because it is not the right way to use that. We have to use an "else if" chains
        if( differenceInPositionX == 0 && differenceInPositionY==0 ) {
            //probably best to give some kind of feedback. Probably best to draw where the player can go.
        }
        else if( ( Math.abs(differenceInPositionX)<3 && differenceInPositionY==0 ) ) {
            //attempt at horizontal movement - may be still blocked by collision, so let's check for that
            if( collisionCheck(differenceInPositionX , differenceInPositionY) ){
                girl.move(differenceInPositionX*tileWidth,0);
            }
        }
        else if( ( Math.abs(differenceInPositionY)<3 && differenceInPositionX==0 ) ) {
            //attempt at vertical movement - may be still blocked by collision, so let's check for that
            if( collisionCheck(differenceInPositionX , differenceInPositionY) ){
                girl.move(0,differenceInPositionY*tileHeight);
            }
        }
        //additional interaction types go in between here.
        else{
            //fail-press nothing happens. Maybe we give some feedback, like a sound, that a press occured.
        }
       // Gdx.app.log("move", "playerPositionY: " + playerPositionY + " playerPositionX:" + playerPositionX);
       // Gdx.app.log("move", "differenceInPositionX: " + differenceInPositionX + " differenceInPositionY:" + differenceInPositionY);

        //move should then use the various simplified position methods to check the simplified positions of items and monsters against simplified position of player
        //all items and monsters should express their position in a simplified way
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
    public boolean scrolled(int amount) {
        return false;
    }

}
