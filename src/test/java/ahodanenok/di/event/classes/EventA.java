package ahodanenok.di.event.classes;

import ahodanenok.di.event.Event;

public class EventA implements Event {

    private int msg;

    public EventA(int msg) {
        this.msg = msg;
    }

    public int getMsg() {
        return msg;
    }
}
