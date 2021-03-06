package homewatch.things.services.motionsensors;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import homewatch.exceptions.NetworkException;
import homewatch.stubs.MotionSensorStubs;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.net.ssl.*")
public class TestRestMotionSensor {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(options().port(8080).bindAddress("0.0.0.0"));

  private RestMotionSensorService restMotionSensorService;
  private final MotionSensor originalMotionSensor = new MotionSensor(new Random().nextBoolean());

  @Test
  public void testMotionSensor() throws NetworkException, UnknownHostException {
    restMotionSensorService = new RestMotionSensorService(InetAddress.getLocalHost().getHostName(), 8080);
    MotionSensorStubs.stubGetStatus(wireMockRule, originalMotionSensor.hasMovement());

    MotionSensor returnedMotionSensor = restMotionSensorService.get();

    assertEquals(originalMotionSensor, returnedMotionSensor);
  }

  @Test
  public void goodPing() throws UnknownHostException {
    restMotionSensorService = new RestMotionSensorService(InetAddress.getLocalHost().getHostName(), 8080);
    MotionSensorStubs.stubGetStatus(wireMockRule, originalMotionSensor.hasMovement());

    assertTrue(restMotionSensorService.ping());
  }


  @Test
  public void badPing() throws UnknownHostException {
    restMotionSensorService = new RestMotionSensorService(InetAddress.getLocalHost().getHostName(), 77);

    assertFalse(restMotionSensorService.ping());
  }
}
