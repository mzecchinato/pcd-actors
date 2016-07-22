package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;
import it.unipd.math.pcd.actors.exceptions.UnsupportedMessageException;

/**
 * Created by Mattia on 22/07/16.
 */
public class ActorRefImpl<T extends Message> implements ActorRef<T> {

    /**
     * Manages sending of a {@code message}
     *
     * @param message The message to send
     * @param to The actor to which sending the message
     */
    @Override
    public void send(T message, ActorRef to) {
        if(AbsActorSystem.getActors().get(this) != null) {
            AbsActor<T> actor = (AbsActor) AbsActorSystem.getActors().get(to);
            if (!(message instanceof Message)) {
                throw new UnsupportedMessageException(message);
            }
            if (actor != null) {
                if (actor.isActive()) {
                    actor.getMailbox().add(message);
                    actor.manageMessage(this, message);
                } else {
                    throw new NoSuchActorException("Receiver actor is stopped");
                }
            } else {
                throw new NoSuchActorException("Actor doesn't exist");
            }
        } else {
            throw new NoSuchActorException("Actor doesn't exist anymore");
        }
    }

    /**
     * Compares two {@code ActorRef}
     *
     * @param o Reference to compare
     * @return 1 if they are equal, 0 otherwise
     */
    @Override
    public int compareTo(ActorRef o) {
        if(this.equals(o)) {
            return 0;
        }
        return 1;
    }
}
