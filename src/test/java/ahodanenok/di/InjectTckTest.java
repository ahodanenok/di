package ahodanenok.di;

import ahodanenok.di.value.InstantiatingValue;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.*;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.RoundThing;
import org.atinject.tck.auto.accessories.SpareTire;

import javax.inject.Named;

@Drivers
@Named("spare")
public class InjectTckTest {

    public static boolean skip = true;

    public static Test suite() {
        // workaround for a bug in vintage engine, suite method is called twice
        if (skip) {
            skip = false;
            return new TestSuite();
        }

        InstantiatingValue<Seat> driverSeat = new InstantiatingValue<>(Seat.class, DriversSeat.class);
        driverSeat.metadata().setQualifiers(InjectTckTest.class.getAnnotation(Drivers.class));

        InstantiatingValue<Tire> spareTire = new InstantiatingValue<>(Tire.class, SpareTire.class);
        spareTire.metadata().setName("spare");
        spareTire.metadata().setQualifiers(InjectTckTest.class.getAnnotation(Named.class));

        DIContainer container = DIContainer.builder()
                .addValue(new InstantiatingValue<>(Car.class, Convertible.class))
                .addValue(driverSeat)
                .addValue(new InstantiatingValue<>(FuelTank.class))
                .addValue(new InstantiatingValue<>(Seat.class))
                .addValue(new InstantiatingValue<>(Seatbelt.class))
                .addValue(new InstantiatingValue<>(Tire.class))
                .addValue(new InstantiatingValue<>(Engine.class, V8Engine.class))
                .addValue(new InstantiatingValue<>(Cupholder.class))
                .addValue(new InstantiatingValue<>(RoundThing.class))
                .addValue(new InstantiatingValue<>(SpareTire.class))
                .addValue(spareTire)
                .allowInjectStatic(Tire.class)
                .allowInjectStatic(SpareTire.class)
                .allowInjectStatic(Convertible.class)
                .build();

        return Tck.testsFor(container.instance(Car.class), true, true);
    }
}
