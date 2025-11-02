package astrogeist.engine.runconfig;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import astrogeist.engine.runconfig.model.RunConfig;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class RunConfigIO {
    private final ObjectMapper mapper;

    public RunConfigIO(ObjectMapper mapper) {
        this.mapper = mapper.copy()
        	.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
        		false);
    }

    public final RunConfig read(Path path) throws Exception {
        try (Reader r = Files.newBufferedReader(path)) {
            RunConfig cfg = mapper.readValue(r, RunConfig.class);
            List<String> errors = cfg.validate();
            if (!errors.isEmpty()) {
                throw new IllegalArgumentException("Invalid run-config " + 
                	path + ":\n - " + String.join("\n - ", errors));
            }
            return cfg;
        }
    }
    
}
