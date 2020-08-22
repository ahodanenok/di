package ahodanenok.di.event.classes;

import ahodanenok.di.event.EventListener;

import javax.inject.Singleton;

@Singleton
public class ReceiverA {

    private int msg;

    @EventListener
    public void receiveMsg(EventA event) {
        this.msg = event.getMsg();
    }

    public int getMsg() {
        return msg;
    }
}
