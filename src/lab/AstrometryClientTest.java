package lab;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import astrogeist.engine.integration.api.astrometry.DefaultAstrometricClient;




public class AstrometryClientTest {
    private final HttpClient http = HttpClient.newHttpClient();

    public CompletableFuture<HttpResponse<String>> fetchCalibrationAsync(long jobId) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://nova.astrometry.net/api/jobs/" + jobId + "/info/"))
                .GET()
                .build();

        // sendAsync returns a CompletableFuture
        return http.sendAsync(req, HttpResponse.BodyHandlers.ofString());
    }

    public static void main(String[] args) {
    	
    	try {
    		var cl = new DefaultAstrometricClient();
    		
    		System.out.println("Calibration");
    		var cal = cl.getCalibration(14207155);
    		System.out.println(cal);
    		System.out.println("-------");
    		System.out.println("Info");
    		var info = cl.getInfo(14207155);
    		System.out.println(info);
    		System.out.println("-------");
    		System.out.println("Annotations");
    		var anno = cl.getAnnotations(14207155);
    		System.out.println(anno);
    		System.out.println("-------");
    	} catch (Exception x) {
    		x.printStackTrace();
    	}
    	
    	
//        AstrometryClientTest client = new AstrometryClientTest();
//        client.fetchCalibrationAsync(14207155)
//              .thenApply(HttpResponse::body)
//              .thenAccept(body -> {
//                  // parse JSON here
//                  System.out.println("Calibration JSON: " + body);
//                  jsontest(body);
//              })
//              .exceptionally(ex -> {
//                  ex.printStackTrace();
//                  return null;
//              });
//
//        // keep JVM alive long enough for async call to complete
//        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
    }
    
    private static void jsontest(String json) {
    	try {
    		ObjectMapper mapper = new ObjectMapper();
    		JsonNode root = mapper.readTree(json);

    		// array:
    		JsonNode objects = root.get("objects_in_field");
    		for (JsonNode obj : objects) {
    			System.out.println("Object: " + obj.asText());
    		}

    		// nested object:
    		JsonNode calib = root.get("calibration");
    		double ra  = calib.get("ra").asDouble();
    		double dec = calib.get("dec").asDouble();
    		double pixscale = calib.get("pixscale").asDouble();

    		System.out.printf("RA=%.4f Dec=%.4f PixScale=%.3f\"/px%n", ra, dec, pixscale);
    	} catch (Exception x) {
    		x.printStackTrace();
    	}
    	
    }
    
//    private static void jsontest(String json) {
//    	Gson gson = new Gson();
//    	JsonObject root = gson.fromJson(json, JsonObject.class);
//    	JsonArray objects = root.getAsJsonArray("objects_in_field");
//    	for (JsonElement e : objects) {
//    	    System.out.println(e.getAsString());
//    	}
//
//    	JsonObject calib = root.getAsJsonObject("calibration");
//    	double ra = calib.get("ra").getAsDouble();
//    	double dec = calib.get("dec").getAsDouble();
//    	System.out.println("RA = " + ra + " Dec = " + dec);
//    }
}

