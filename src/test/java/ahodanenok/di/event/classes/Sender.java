package ahodanenok.di.event.classes;

import ahodanenok.di.event.Events;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Sender {

    private Events events;

    @Inject
    public void setEvents(Events events) {
        this.events = events;
    }

    public void send(int msg) {
        events.fire(new EventA(msg));
    }
}
