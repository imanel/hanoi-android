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
    
    public boolean canBeAddedToStack(Stack s) {
    	return (s.size() == 0 || getWeight() < ((Ring) s.peek()).getWeight());
    }
    
    public float getXPositionFor(Sprite t, Stack s) {
    	return t.getX() + t.getWidth()/2 - getWidth()/2;
    }
    
    public float getYPositionFor(Sprite t, Stack s) {
    	if (s.size() == 0) {
            return t.getY() + t.getHeight() - getHeight();
        } else {
            return ((Ring) s.peek()).getY() - getHeight();
        }
    }
    
    public void setPositionFor(Sprite t, Stack s) {
    	setPosition( getXPositionFor(t, s), getYPositionFor(t, s) );
    }
    
    public void addTo(Sprite t, Stack s) {
    	if (getStack() != null)
    		getStack().remove(this);
    	setPositionFor(t, s);
    	s.add(this);
    	setStack(s);
    	setTower(t);
    }
}
