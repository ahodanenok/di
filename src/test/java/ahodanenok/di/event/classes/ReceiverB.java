package ahodanenok.di.event.classes;

import ahodanenok.di.event.EventListener;

import javax.inject.Singleton;

@Singleton
public class ReceiverB {

    private int msg;

    @EventListener
    public void receiveMsg(EventB event) {
        this.msg = event.getMsg();
    }

    public int getMsg() {
        return msg;
    }
}
