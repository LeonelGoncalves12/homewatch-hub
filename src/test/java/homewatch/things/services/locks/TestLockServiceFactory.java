package homewatch.things.services.locks;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestLockServiceFactory {
  private static final LockServiceFactory serviceFactory = new LockServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    ThingService<Lock> lockService = serviceFactory.createThingService("rest");
    DiscoveryService<Lock> discoveryService = serviceFactory.createDiscoveryService("rest");

    assertTrue(lockService instanceof RestLockService);
    assertTrue(discoveryService instanceof NetworkThingDiscoveryService);
    assertThat(lockService.getType(), is("Things::Lock"));
    assertThat(lockService.getSubtype(), is("rest"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Lock> lockService = serviceFactory.createThingService("rest");

    assertTrue(lockService instanceof RestLockService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
