package it.unipd.math.pcd.actors;

import it.unipd.math.pcd.actors.exceptions.NoSuchActorException;
import it.unipd.math.pcd.actors.exceptions.UnsupportedMessageException;

/**
 * Created by gruppo on 22/07/16.
 */
public class ActorRefImpl<T extends Message> implements ActorRef<T> {

    @Override
    public void send(T message, ActorRef to) {
        AbsActor<T> actor = (AbsActor) AbsActorSystem.getActors().get(to);
        if(!(message instanceof Message)) {
            throw new UnsupportedMessageException(message);
        }
        if(actor != null) {
            if(actor.isActive()) {
                actor.getMailbox().add(message);
                actor.manageMessage(this, message);
            } else {
                throw new NoSuchActorException("Actor is stopped");
            }
        } else {
            throw new NoSuchActorException("Actor doesn't exist");
        }
    }

    @Override
    public int compareTo(ActorRef o) {
        if(this.equals(o)) {
            return 0;
        }
        return 1;
    }
}
