/**
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2015 Riccardo Cardin
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */

/**
 * Please, insert description here.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
package it.unipd.math.pcd.actors;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Defines common properties of all actors.
 *
 * @author Riccardo Cardin
 * @version 1.0
 * @since 1.0
 */
public abstract class AbsActor<T extends Message> implements Actor<T> {

    /**
     * {@code mailbox} of the actor
     */
    private final Queue<T> mailbox = new ConcurrentLinkedQueue<>();

    /**
     * State of the actor, if is active or not
     */
    private boolean active = true;

    /**
     * Gets mailbox
     *
     * @return mailbox
     */
    public Queue<T> getMailbox() {
        return mailbox;
    }

    /**
     * Gets actual state of the {@code Actor}
     *
     * @return State of the {@code Actor}
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the new state
     *
     * @param active New state for the {@code Actor}
     */
    public void setActiveState(boolean active) {
        this.active = active;
    }

    /**
     * Self-reference of the actor
     */
    protected ActorRef<T> self;

    /**
     * Sender of the current message
     */
    protected ActorRef<T> sender;

    /**
     * Sets the self-referece.
     *
     * @param self The reference to itself
     * @return The actor.
     */
    protected final Actor<T> setSelf(ActorRef<T> self) {
        this.self = self;
        return this;
    }

    /**
     * Gets the self-reference
     *
     * @return Reference of the actor
     */
    public ActorRef<T> getSelf() {
        return self;
    }

    /**
     * Manages a message received
     *
     * @param sender The reference of the sender {@code Actor}
     * @param message Message received
     */
    public synchronized void manageMessage(ActorRef<T> sender, T message) {
        this.sender = sender;
        receive(message);
        mailbox.remove(message);
        if(mailbox.isEmpty()) {
            notifyAll();
        }
    }

}
