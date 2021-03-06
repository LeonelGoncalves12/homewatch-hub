package homewatch.things.services.thermostat;

import homewatch.exceptions.InvalidSubTypeException;
import homewatch.things.ThingService;
import homewatch.things.discovery.DiscoveryService;
import homewatch.things.discovery.NetworkThingDiscoveryService;
import org.junit.Test;

import java.net.UnknownHostException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TestThermostatServiceFactory {
  private static final ThermostatServiceFactory serviceFactory = new ThermostatServiceFactory();

  @Test
  public void testRestCreate() throws UnknownHostException, InvalidSubTypeException {
    String addr = "192.168.1.50";
    ThingService<Thermostat> thermostatService = serviceFactory.createThingService("rest");
    DiscoveryService<Thermostat> discoveryService = serviceFactory.createDiscoveryService("rest");

    assertTrue(thermostatService instanceof RestThermostatService);
    assertTrue(discoveryService instanceof NetworkThingDiscoveryService);
    assertThat(thermostatService.getType(), is("Things::Thermostat"));
    assertThat(thermostatService.getSubtype(), is("rest"));
  }

  @Test
  public void testRestCreateEmpty() throws UnknownHostException, InvalidSubTypeException {
    ThingService<Thermostat> thermostatService = serviceFactory.createThingService("rest");

    assertTrue(thermostatService instanceof RestThermostatService);
  }

  @Test
  public void testIsSubType() {
    assertTrue(serviceFactory.isSubType("rest"));
  }
}
