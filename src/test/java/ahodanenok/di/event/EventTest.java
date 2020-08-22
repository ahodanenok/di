package ahodanenok.di.event;

import ahodanenok.di.DIContainer;
import ahodanenok.di.event.classes.ReceiverA;
import ahodanenok.di.event.classes.ReceiverB;
import ahodanenok.di.event.classes.Sender;
import ahodanenok.di.value.InstantiatingValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    @Test
    public void testEventReceived() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(Sender.class))
                .addValue(new InstantiatingValue<>(ReceiverA.class))
                .build();

        Sender sender = container.instance(Sender.class);
        sender.send(11);

        ReceiverA receiver = container.instance(ReceiverA.class);
        assertEquals(11, receiver.getMsg());
    }

    @Test
    public void testEventNotReceived() {
        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(Sender.class))
                .addValue(new InstantiatingValue<>(ReceiverB.class))
                .build();

        Sender sender = container.instance(Sender.class);
        sender.send(11);

        ReceiverB receiver = container.instance(ReceiverB.class);
        assertEquals(0, receiver.getMsg());
    }
}
