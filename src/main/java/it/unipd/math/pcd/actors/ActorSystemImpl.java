package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;

import java.util.Iterator;

/**
 * Created by Mattia on 21/07/2016.
 */
public final class ActorSystemImpl extends AbsActorSystem {

    /**
     * Stops a specific {@code actor} and removes it from {@code ActorSystem}
     *
     * @param actorRef Reference of {@code actor} to stop
     */
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

    /**
     * Stops and removes all {@code actor} from the {@code system}
     */
    @Override
    public synchronized void stop() {
        Iterator actors = getActors().entrySet().iterator();
        while(actors.hasNext()) {
            AbsActor actor = (AbsActor) actors.next();
            actor.setActiveState(false);
            stop(actor.getSelf());
        }
    }

    /**
     * Creates a reference to insert a new {@code actor} in the {@code system}
     *
     * @param mode Type of {@code actor}
     * @return Reference of a new {@code actor} in the {@code system}
     */
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

    /**
     * Gets the {@code Actor} with a specific {@code ActorRef}
     *
     * @param actorRef Reference of the {@code Actor} to find
     * @return Actor instance
     */
    public Actor<? extends Message> findActor(ActorRef<?> actorRef) {
        Actor<? extends Message> actor = getActors().get(actorRef);
        if(actor != null) {
            return actor;
        }
        return null;
    }
}
