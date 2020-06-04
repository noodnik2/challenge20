package noodnik.thousandeyes;

import static java.util.Arrays.asList;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

/*
     First we will implement a scaffold for our network tests: it can be called BaseTest or something similar.
     **Note:** This is a network test, and not any sort of unit test
    
    A Test should have these properties:
    - Name: A string containing the test name. ie: "My super test"
    - Target: A String containing the target server url. ie: "www.google.com"
    
    And these methods:
    - String getTestDescription(); -> Returns the test name and target with " - " in the middle.
    - Integer computeValue(List<Long> points); -> This will use metrics to aggregate the data and return some value
       It can, by default, just return the number of points in a given series.
    
    Let's implement some metrics. We will start with 2 metrics: 
    - Errors
    - Packet Loss
    
    These metrics are constants with an immutable description.
    Additionally, each metric must implement a computed aggregation from a list of points received as an argument:
    - Errors: Number of -1's in the list
    - Packet Loss: Number of zeros in the list
    
    The signature of the method can be: <br>
    ```Long computeAggregation(List<Long> points)```
    
    **add new test: NetworkTest**

    Write an implementation of computeValue:
    - Double computeValue(List<Long> points); -> Returns Errors / PacketLoss 

 */

public class NetworkTesterTests {
    
    @Test
    public void firstTestCase() {
        NetworkTest networkTest = new NetworkTest("firstTestCase", "internal");
        Double computedValue = networkTest.computeValue(asList(1L, 2L, 3L, 0L, -1L, 5L, 0L, 0L, 99L));
        log(String.format("test(%s) produced result(%.0f%%%%)", networkTest.getTestDescription(), 100d * computedValue));
        assertTrue(computedValue.equals(1d/3));
    }

    interface INetworkTest<T extends Number> {

        // -> Returns the test name and target with " - " in the middle.
        String getTestDescription();

        // -> This will use metrics to aggregate the data and return
        T computeValue(List<Long> points);

    }

    static abstract class BaseNetworkTest<T extends Number> implements INetworkTest<T> {

        private String name;
        private String target;

        public BaseNetworkTest(String name, String target) {
            this.name = name;
            this.target = target;          
        }
        
        public String getTestDescription() {
            return name + "-" + target;
        }

    }

    static class NetworkTest extends BaseNetworkTest<Double> {

        private static Errors errors = new Errors();
        private static PacketLoss packetLoss = new PacketLoss();

        public NetworkTest(String name, String target) {
            super(name, target);          
        }
        
        public Double computeValue(List<Long> points) {
            long packetLossCount = packetLoss.computeAggregation(points);
            if (packetLossCount == 0L) {
                return Double.MAX_VALUE;
            }
            long errorCount = errors.computeAggregation(points);
            return ((double) errorCount / (double) packetLossCount);
        }

    };

    interface NetworkMetric {
        Long computeAggregation(List<Long> points);
    }

    static class Errors implements NetworkMetric {
        public Long computeAggregation(List<Long> points) {
            return (points.stream().filter(p -> p != null && p == -1L).count());
        }
    }

    static class PacketLoss implements NetworkMetric {
        public Long computeAggregation(List<Long> points) {
            return (points.stream().filter(p -> p != null && p == 0L).count());
        }
    }

}
