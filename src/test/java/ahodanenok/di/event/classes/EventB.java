package ahodanenok.di.event.classes;

import ahodanenok.di.event.Event;

public class EventB implements Event {

    private int msg;

    public EventB(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }
}
