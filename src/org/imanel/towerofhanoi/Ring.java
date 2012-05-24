package org.imanel.towerofhanoi;

import java.util.Stack;

import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;

@SuppressWarnings("rawtypes")
public class Ring extends Sprite {
    private int weight;
    private Stack stack;
    private Sprite tower;
    private TowerOfHanoiActivity parent;

    public Ring(int w, Sprite t, Stack s, ITextureRegion pTextureRegion, TowerOfHanoiActivity p) {
        super(0, 0, pTextureRegion, p.getVertexBufferObjectManager());
        parent = p;
        weight = w;
        addTo(t, s);
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

    @SuppressWarnings("unchecked")
    public void addTo(Sprite t, Stack s) {
        if (getStack() != null)
            getStack().remove(this);
        setPositionFor(t, s);
        s.add(this);
        setStack(s);
        setTower(t);
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (((Ring) this.getStack().peek()).getWeight() != this.getWeight())
            return false;
        this.setPosition(pSceneTouchEvent.getX() - this.getWidth() / 2,
            pSceneTouchEvent.getY() - this.getHeight() / 2);
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
            checkForCollisionsWithTowers();
        }
        return true;
    }

    private void checkForCollisionsWithTowers() {
        Stack s = null;
        Sprite t = null;
        parent.points++;
        if (collidesWith(parent.tower1) && canBeAddedToStack(parent.stack1)) {
            s = parent.stack1;
            t = parent.tower1;
        } else if (collidesWith(parent.tower2) && canBeAddedToStack(parent.stack2)) {
            s = parent.stack2;
            t = parent.tower2;
        } else if (collidesWith(parent.tower3) && canBeAddedToStack(parent.stack3)) {
            s = parent.stack3;
            t = parent.tower3;
        } else {
            parent.points--;
            s = getStack();
            t = getTower();
        }
        addTo(t, s);
        parent.checkResult();
    }
}
