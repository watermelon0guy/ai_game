package com.watermelon0guy.ai_game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        increasePointOfContacts(fa,fb);
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;

        decreasePointOfContacts(fa,fb);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean isJointContact(Fixture a, Fixture b)
    {
        return ((a.getUserData() instanceof JointClass && b.getUserData() instanceof Floor) || (a.getUserData() instanceof Floor && b.getUserData() instanceof JointClass));
    }

    private void increasePointOfContacts(Fixture fa, Fixture fb)
    {
        if(isJointContact(fa,fb))
        {
            if(fa.getUserData() instanceof JointClass)
            {
                ((JointClass) fa.getUserData()).parent.pointOfContacts +=1;
                System.out.println(((JointClass) fa.getUserData()).parent.pointOfContacts);
            }
            else
            {
                ((JointClass) fb.getUserData()).parent.pointOfContacts +=1;
                //System.out.println(((JointClass) fb.getUserData()).parent.pointOfContacts);
            }
        }
    }

    private void decreasePointOfContacts(Fixture fa, Fixture fb)
    {
        if(isJointContact(fa,fb))
        {
            if(fa.getUserData() instanceof JointClass)
            {
                ((JointClass) fa.getUserData()).parent.pointOfContacts -=1;
                System.out.println(((JointClass) fa.getUserData()).parent.pointOfContacts);
            }
            else
            {
                ((JointClass) fb.getUserData()).parent.pointOfContacts -=1;
                //System.out.println(((JointClass) fb.getUserData()).parent.pointOfContacts);
            }

        }
    }

}
