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
            ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/background.png");
                }
            });
            ITexture towerTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/tower.png");
                }
            });
            ITexture ring1Texture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/ring1.png");
                }
            });
            ITexture ring2Texture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/ring2.png");
                }
            });
            ITexture ring3Texture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/ring3.png");
                }
            });

            backgroundTexture.load();
            towerTexture.load();
            ring1Texture.load();
            ring2Texture.load();
            ring3Texture.load();

            this.backgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);
            this.towerTextureRegion = TextureRegionFactory.extractFromTexture(towerTexture);
            this.ring1TextureRegion = TextureRegionFactory.extractFromTexture(ring1Texture);
            this.ring2TextureRegion = TextureRegionFactory.extractFromTexture(ring2Texture);
            this.ring3TextureRegion = TextureRegionFactory.extractFromTexture(ring3Texture);
        } catch (IOException e) {
            Debug.e(e);
        }
    }

    @Override
    protected Scene onCreateScene() {
        final Scene scene = new Scene();

        Sprite backgroundSprite = new Sprite(0, 0, this.backgroundTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(backgroundSprite);

        tower1 = new Sprite(192, 63, this.towerTextureRegion, getVertexBufferObjectManager());
        tower2 = new Sprite(400, 63, this.towerTextureRegion, getVertexBufferObjectManager());
        tower3 = new Sprite(604, 63, this.towerTextureRegion, getVertexBufferObjectManager());
        scene.attachChild(tower1);
        scene.attachChild(tower2);
        scene.attachChild(tower3);

        Ring ring1 = new Ring(1, 139, 174, this.ring1TextureRegion, getVertexBufferObjectManager()) {
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
        Ring ring2 = new Ring(2, 118, 212, this.ring2TextureRegion, getVertexBufferObjectManager()) {
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
        Ring ring3 = new Ring(3, 97, 255, this.ring3TextureRegion, getVertexBufferObjectManager()) {
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

        this.stack1 = new Stack();
        this.stack2 = new Stack();
        this.stack3 = new Stack();
        this.stack1.add(ring3);
        this.stack1.add(ring2);
        this.stack1.add(ring1);

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
        if (ring.collidesWith(tower1) && (stack1.size() == 0 || ring.getHeight() < ((Ring) stack1.peek()).getHeight())) {
            stack = stack1;
            tower = tower1;
        } else if (ring.collidesWith(tower2) && (stack2.size() == 0 || ring.getHeight() < ((Ring) stack2.peek()).getHeight())) {
            stack = stack2;
            tower = tower2;
        } else if (ring.collidesWith(tower3) && (stack3.size() == 0 || ring.getHeight() < ((Ring) stack3.peek()).getHeight())) {
            stack = stack3;
            tower = tower3;
        } else {
            stack = ring.getStack();
            tower = ring.getTower();
        }
        ring.getStack().remove(ring);
        if (stack != null && tower !=null && stack.size() == 0) {
            ring.setPosition(tower.getX() + tower.getWidth()/2 - ring.getWidth()/2, tower.getY() + tower.getHeight() - ring.getHeight());
        } else if (stack != null && tower !=null && stack.size() > 0) {
            ring.setPosition(tower.getX() + tower.getWidth()/2 - ring.getWidth()/2, ((Ring) stack.peek()).getY() - ring.getHeight());
        }
        stack.add(ring);
        ring.setStack(stack);
        ring.setTower(tower);
    }
}
