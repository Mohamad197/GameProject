package com.mygdx.game;
/**
 * Created by tmp-sda-1124 on 12/4/17.
 */
        import com.badlogic.gdx.ApplicationListener;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.graphics.g2d.TextureRegion;
        import com.badlogic.gdx.scenes.scene2d.InputEvent;
        import com.badlogic.gdx.scenes.scene2d.Stage;
        import com.badlogic.gdx.scenes.scene2d.ui.Skin;
        import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
        import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;




    public class MenuScreen implements ApplicationListener {
    private SpriteBatch batch;
    private Skin skin;
    private Stage stage;

    //private Texture backgroundTexture;
    //private Sprite sprite;
    //private TextureRegion region;

    @Override
    public void create() {
        //backgroundTexture = new Texture(Gdx.files.internal("bakgroundmenu.png"));
        //sprite =new Sprite(backgroundTexture,20,20,50,50);
        //region=new TextureRegion(backgroundTexture,0,0,800,420);
        //sprite=new Sprite(region);
        //sprite.setSize(1,1);

        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal(Constants.skin));
        stage = new Stage();

        final TextButton playButton = new TextButton("PLAY", skin, "default");
        playButton.setSize(Constants.colWidth ,Constants.rowHeight);
        playButton.setPosition(Constants.centerX,Constants.centerY+200);
        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                playButton.setText("You clicked the button");
            }
        });
        final TextButton settingsButton = new TextButton("SETTINGS", skin, "default");
        settingsButton.setSize(Constants.colWidth ,Constants.rowHeight);
        settingsButton.setPosition(Constants.centerX ,Constants.centerY+50);
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                settingsButton.setText("You clicked the button");
            }
        });

        final TextButton scoreButton = new TextButton("SCORE", skin, "default");
        scoreButton.setSize(Constants.colWidth ,Constants.rowHeight);
        scoreButton.setPosition(Constants.centerX ,Constants.centerY-100);
        scoreButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                scoreButton.setText("You clicked the button");
            }
        });
        final TextButton exitButton = new TextButton("EXIT", skin, "default");
        exitButton.setSize(Constants.colWidth ,Constants.rowHeight);
        exitButton.setPosition(Constants.centerX ,Constants.centerY-250);
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                exitButton.setText("You clicked the button");
            }
        });
        stage.addActor(playButton);
        stage.addActor(settingsButton);
        stage.addActor(scoreButton);
        stage.addActor(exitButton);
        Gdx.input.setInputProcessor(stage);
    }

    /*public void renderBackground()
    {
        sprite.draw(batch);
    }*/
    @Override
    public void dispose() {
        batch.dispose();
    }
    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        //batch.disableBlending();
       // batch.draw(backgroundTexture,100,100);
        //renderBackground();
        stage.draw();
       // batch.enableBlending();
        batch.end();
    }
    @Override
    public void resize(int width, int height) {
    }
    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }


}

