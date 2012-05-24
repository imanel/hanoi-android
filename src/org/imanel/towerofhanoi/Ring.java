package org.imanel.towerofhanoi;

import java.util.Stack;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class Ring extends Sprite {
    private int weight;
    private Stack stack;
    private Sprite tower;

    public Ring(int w, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
        weight = w;
    }

    public int getWeight() {
        return weight;
    }

    public Stack getStack() {
        return stack;
    }

    public void setStack(Stack s) {
        stack = s;
    }

    public Sprite getTower() {
        return tower;
    }

    public void setTower(Sprite t) {
        tower = t;
    }
}
