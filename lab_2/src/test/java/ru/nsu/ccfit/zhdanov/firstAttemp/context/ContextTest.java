package ru.nsu.ccfit.zhdanov.firstAttemp.context;

import org.junit.Assert;
import org.junit.Test;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.IncorrectVariable;
import ru.nsu.ccfit.zhdanov.firstAttemp.context.exception.VariableRedefinition;

import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextTest {
  @Test
  public void testOccupancy() {
    final Context context = new Context(new OutputStreamWriter(System.out));

    List<Double> args = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5);
    for (int i = 0; i < args.size(); ++i) {
      context.push(args.get(i));
      Assert.assertEquals(context.occupancy(), i + 1);
    }

    for (int i = args.size(); i > 0; --i) {
      context.peek();
      Assert.assertEquals(context.occupancy(), i);
      Assert.assertEquals(0.0, context.pop(), args.get(i - 1));
      Assert.assertEquals(context.occupancy(), i - 1);
    }
  }

  @Test
  public void contextStackDecodeTest() {
    final Context context = new Context(new OutputStreamWriter(System.out));

    AtomicInteger curVal = new AtomicInteger(0);
    List<String> args = Arrays.asList("0.1", "2", "-1.2", "lol", null, "0.a");
    Assert.assertEquals(0, Double.compare(context.decode(args.get(curVal.get())), Double.parseDouble(args.get(curVal.getAndIncrement()))));
    Assert.assertEquals(0, Double.compare(context.decode(args.get(curVal.get())), Double.parseDouble(args.get(curVal.getAndIncrement()))));
    Assert.assertEquals(0, Double.compare(context.decode(args.get(curVal.get())), Double.parseDouble(args.get(curVal.getAndIncrement()))));
    Assert.assertThrows(IncorrectVariable.class, () -> context.decode(args.get(curVal.getAndIncrement())));
    Assert.assertThrows(IncorrectVariable.class, () -> context.decode(args.get(curVal.getAndIncrement())));
    Assert.assertThrows(IncorrectVariable.class, () -> context.decode(args.get(curVal.getAndIncrement())));
  }

  @Test
  public void contextStackDefineTest() {
    final Context context = new Context(new OutputStreamWriter(System.out));

    List<String> defKeys = Arrays.asList("lol", "0.a");
    List<String> defValues = Arrays.asList("1", "2");
    for (int i = 0; i < defKeys.size(); i++) {
      context.define(defKeys.get(i), defValues.get(i));
      Assert.assertEquals(0, Double.compare(context.decode(defKeys.get(i)), Double.parseDouble(defValues.get(i))));
    }

    Assert.assertEquals(0, Double.compare(context.decode("0.1"), Double.parseDouble("0.1")));
    for (AtomicInteger curVal = new AtomicInteger(0); curVal.get() < defKeys.size(); curVal.incrementAndGet()) {
      Assert.assertThrows(VariableRedefinition.class, () -> context.define(defKeys.get(curVal.get()), "0.1"));
      Assert.assertEquals(
              0,
              Double.compare(context.decode(defKeys.get(curVal.get())), Double.parseDouble(defValues.get(curVal.get())))
      );
    }
  }
}
