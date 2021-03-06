package homewatch.things.services.weather;

import com.fasterxml.jackson.databind.JsonNode;
import homewatch.constants.JsonUtils;
import homewatch.exceptions.NetworkException;
import homewatch.exceptions.ReadOnlyDeviceException;
import homewatch.net.HttpCachingUtils;
import homewatch.things.ThingService;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class OWMWeatherService extends ThingService<Weather> {
  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=3e7e26039e4050a3edaaf374adb887de";
  private static final String REGION_URL = "http://freegeoip.net/json/";

  @Override
  public Weather get() throws NetworkException {
    try {
      JsonNode response = this.getWeatherData();

      return this.jsonToWeather(response);
    } catch (ExecutionException | IOException e) {
      throw new NetworkException(e, 500);
    }
  }

  @Override
  public Weather put(Weather weather) throws NetworkException {
    throw new ReadOnlyDeviceException();
  }

  @Override
  public boolean ping() {
    try {
      get();
      return true;
    } catch (NetworkException e) {
      return false;
    }
  }

  @Override
  public void setAttributes(Map<String, ?> attributes) {
    //no attributes to set...
  }

  private JsonNode getWeatherData() throws ExecutionException, IOException {
    String region = JsonUtils.getOM().readTree(HttpCachingUtils.get(REGION_URL).getPayload()).get("region_name").asText();
    String url = String.format(BASE_URL, region);

    return JsonUtils.getOM().readTree(HttpCachingUtils.get(url).getPayload());
  }

  @Override
  public String getType() {
    return "Things::Weather";
  }

  @Override
  public String getSubtype() {
    return "owm";
  }

  private Weather jsonToWeather(JsonNode json) {
    double temperature = json.get("main").get("temp").asDouble() - 273.15d;
    double windspeed = json.get("wind").get("speed").asDouble();
    boolean rain = getRain(json);
    boolean cloudy = json.get("clouds").get("all").asInt() > 0;

    return new Weather(temperature, windspeed, rain, cloudy);
  }

  private boolean getRain(JsonNode json) {
    boolean rain = false;

    for (JsonNode weatherNode : json.get("weather")) {
      int statusCode = weatherNode.get("id").asInt();
      rain = statusCode >= 300 && statusCode <= 531;
      if (rain)
        break;
    }

    return rain;
  }
}
