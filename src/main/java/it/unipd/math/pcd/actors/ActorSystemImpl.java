package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

import java.util.Iterator;

/**
 * Created by Mattia on 21/07/2016.
 */
public final class ActorSystemImpl extends AbsActorSystem {

    @Override
    public synchronized void stop(ActorRef<?> actorRef) {
        AbsActor<?> actor = (AbsActor<?>) getActors().get(actorRef);
        if (actor == null) {
            throw new NoSuchActorException("Actor didn't find");
        }
        actor.setActiveState(false);
        while(!actor.getMailbox().isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        getActors().remove(actorRef);
    }

    @Override
    public synchronized void stop() {
        Iterator actors = getActors().entrySet().iterator();
        while(actors.hasNext()) {
            AbsActor actor = (AbsActor) actors.next();
            actor.setActiveState(false);
            stop(actor.getSelf());
        }
    }

    @Override
    protected ActorRef createActorReference(ActorMode mode) {
        if (mode == ActorMode.REMOTE) {
            throw new IllegalArgumentException("Actor mode not supported");
        }
        else {
            ActorRef actorRef = new ActorRefImpl();
            return actorRef;
        }
    }

    public Actor<? extends Message> findActor(ActorRef<?> actorRef) {
        Actor<? extends Message> actor = getActors().get(actorRef);
        if(actor != null) {
            return actor;
        }
        return null;
    }
}
