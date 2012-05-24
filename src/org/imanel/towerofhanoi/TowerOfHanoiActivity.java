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
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

public class TowerOfHanoiActivity extends SimpleBaseGameActivity {
    private static int CAMERA_WIDTH = 800;
    private static int CAMERA_HEIGHT = 480;
    private ITextureRegion backgroundTextureRegion, towerTextureRegion, ring1TextureRegion, ring2TextureRegion, ring3TextureRegion;
    private Sprite tower1, tower2, tower3;
    private Stack stack1, stack2, stack3;

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
        } catch (IOException e) {
            Debug.e(e);
        }
    }

    @Override
    protected Scene onCreateScene() {
        final Scene scene = new Scene();

        Sprite backgroundSprite = new Sprite(0, 0, backgroundTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(backgroundSprite);

        tower1 = new Sprite(192, 63, towerTextureRegion, getVertexBufferObjectManager());
        tower2 = new Sprite(400, 63, towerTextureRegion, getVertexBufferObjectManager());
        tower3 = new Sprite(604, 63, towerTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(tower1);
        scene.attachChild(tower2);
        scene.attachChild(tower3);

        Ring ring1 = new Ring(1, 139, 174, ring1TextureRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (((Ring) this.getStack().peek()).getWeight() != this.getWeight())
                    return false;
                this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
                    pSceneTouchEvent.getY() - this.getHeight() / 2);
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    checkForCollisionsWithTowers(this);
                }
                return true;
            }
        };
        Ring ring2 = new Ring(2, 118, 212, ring2TextureRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (((Ring) this.getStack().peek()).getWeight() != this.getWeight())
                    return false;
                this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
                    pSceneTouchEvent.getY() - this.getHeight() / 2);
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    checkForCollisionsWithTowers(this);
                }
                return true;
            }
        };
        Ring ring3 = new Ring(3, 97, 255, ring3TextureRegion, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (((Ring) this.getStack().peek()).getWeight() != this.getWeight())
                    return false;
                this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
                    pSceneTouchEvent.getY() - this.getHeight() / 2);
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    checkForCollisionsWithTowers(this);
                }
                return true;
            }
        };
        scene.attachChild(ring1);
        scene.attachChild(ring2);
        scene.attachChild(ring3);

        stack1 = new Stack();
        stack2 = new Stack();
        stack3 = new Stack();
        stack1.add(ring3);
        stack1.add(ring2);
        stack1.add(ring1);

        ring1.setStack(stack1);
        ring2.setStack(stack1);
        ring3.setStack(stack1);
        ring1.setTower(tower1);
        ring2.setTower(tower1);
        ring3.setTower(tower1);

        scene.registerTouchArea(ring1);
        scene.registerTouchArea(ring2);
        scene.registerTouchArea(ring3);
        scene.setTouchAreaBindingOnActionDownEnabled(true);

        return scene;
    }

    private void checkForCollisionsWithTowers(Ring ring) {
        Stack stack = null;
        Sprite tower = null;
        if (ring.collidesWith(tower1) && ring.canBeAddedToStack(stack1)) {
            stack = stack1;
            tower = tower1;
        } else if (ring.collidesWith(tower2) && ring.canBeAddedToStack(stack2)) {
            stack = stack2;
            tower = tower2;
        } else if (ring.collidesWith(tower3) && ring.canBeAddedToStack(stack3)) {
            stack = stack3;
            tower = tower3;
        } else {
            stack = ring.getStack();
            tower = ring.getTower();
        }
//        ring.getStack().remove(ring);
//        ring.setPositionFor(tower, stack);
//        stack.add(ring);
//        ring.setStack(stack);
//        ring.setTower(tower);
        ring.addTo(tower, stack);
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
}
