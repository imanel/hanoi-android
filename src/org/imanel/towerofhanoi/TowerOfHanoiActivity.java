package org.imanel.towerofhanoi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.Texture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

@SuppressWarnings("rawtypes")
public class TowerOfHanoiActivity extends SimpleBaseGameActivity {
    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private ITextureRegion backgroundTextureRegion, towerTextureRegion, ring1TextureRegion, ring2TextureRegion, ring3TextureRegion;
    private Texture fontTexture;
    private Font font;
    private Text pointsText;
    private Scene scene;
    private Ring ring1, ring2, ring3;
    public Sprite tower1, tower2, tower3;
    public Stack stack1, stack2, stack3;
    public int points = 0;

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
    }

    @Override
    protected void onCreateResources() {
        try {
            ITexture backgroundTexture = preloadTexture("gfx/background.png");
            ITexture towerTexture = preloadTexture("gfx/tower.png");
            ITexture ring1Texture = preloadTexture("gfx/ring1.png");
            ITexture ring2Texture = preloadTexture("gfx/ring2.png");
            ITexture ring3Texture = preloadTexture("gfx/ring3.png");

            backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
            towerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
            ring1TextureRegion = TextureRegionFactory.extractFromTexture(ring1Texture);
            ring2TextureRegion = TextureRegionFactory.extractFromTexture(ring2Texture);
            ring3TextureRegion = TextureRegionFactory.extractFromTexture(ring3Texture);

            fontTexture = new BitmapTextureAtlas(null, 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            font = new Font(null, fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, false, Color.WHITE);
            this.mEngine.getTextureManager().loadTexture(fontTexture);
            this.getFontManager().loadFont(font);
        } catch (IOException e) {
            Debug.e(e);
        }
    }

    @Override
    protected Scene onCreateScene() {
        scene = new Scene();

        Sprite backgroundSprite = new Sprite(0, 0, backgroundTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(backgroundSprite);

        pointsText = new Text(20, 20, font, "" + points, 100, getVertexBufferObjectManager());
        scene.attachChild(pointsText);

        stack1 = new Stack();
        stack2 = new Stack();
        stack3 = new Stack();

        tower1 = new Sprite(192, 63, towerTextureRegion, getVertexBufferObjectManager());
        tower2 = new Sprite(400, 63, towerTextureRegion, getVertexBufferObjectManager());
        tower3 = new Sprite(604, 63, towerTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(tower1);
        scene.attachChild(tower2);
        scene.attachChild(tower3);

        ring3 = new Ring(3, tower1, stack1, ring3TextureRegion, this);
        ring2 = new Ring(2, tower1, stack1, ring2TextureRegion, this);
        ring1 = new Ring(1, tower1, stack1, ring1TextureRegion, this);
        scene.attachChild(ring1);
        scene.attachChild(ring2);
        scene.attachChild(ring3);

        scene.registerTouchArea(ring1);
        scene.registerTouchArea(ring2);
        scene.registerTouchArea(ring3);
        scene.setTouchAreaBindingOnActionDownEnabled(true);

        return scene;
    }

    private ITexture preloadTexture(final String url) throws IOException {
        ITexture texture = new BitmapTexture(getTextureManager(), new IInputStreamOpener() {
            @Override
            public InputStream open() throws IOException {
                return getAssets().open(url);
            }
        });
        texture.load();
        return texture;
    }

    public void checkResult() {
        pointsText.setText("" + points);
        if (stack3.size() == 3) {
            scene.setTouchAreaBindingOnActionDownEnabled(false);
            scene.unregisterTouchArea(ring1);
            scene.unregisterTouchArea(ring2);
            scene.unregisterTouchArea(ring3);
        }
    }
}
