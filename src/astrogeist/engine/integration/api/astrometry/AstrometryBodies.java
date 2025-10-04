package astrogeist.engine.integration.api.astrometry;

public final class AstrometryBodies {
	public String login(String apiKey) { return "request-json={\"apikey\":\"" + apiKey + "\"}"; }
	public String withSession(String session) { return "request-json={\"session\":\"" + session + "\"}"; }
}
