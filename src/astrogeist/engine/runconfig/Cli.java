package astrogeist.engine.runconfig;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import aha.common.util.Guards;
import astrogeist.engine.runconfig.model.Command;
import astrogeist.engine.runconfig.model.CopyRule;
import astrogeist.engine.runconfig.model.RunConfig;

public final class Cli {
	private Cli() { Guards.throwStaticClassInstantiateError(); }
	
	public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("""
                Usage:
                  java RunConfigTester <config.json>
                      --raw "/path/to/snapshot/raw"
                      --work "/path/to/work/base"
                      --target "Seestar"
                      --stamp "2025-10-24T21:05:00Z"
                      [--env SIRIL_BIN=siril-cli]
                      [--dry-run]
                """);
            System.exit(2);
        }

        Path configPath = Paths.get(args[0]);
        Map<String, String> cli = 
        	parseCli(Arrays.copyOfRange(args, 1, args.length));

        // Required inputs
        String rawDir   = need(cli, "--raw");
        String workBase = need(cli, "--work");
        String target   = cli.getOrDefault("--target", "target");
        String stampIso = cli.getOrDefault("--stamp", Instant.now().toString());

        // Build a minimal Snapshot context
        SnapshotContext snap = SnapshotContext.of(rawDir, target, stampIso);

        // Env overrides (e.g., --env SIRIL_BIN=siril-cli)
        Map<String, String> env = new HashMap<>(System.getenv());
        cli.entrySet().stream()
                .filter(e -> e.getKey().equals("--env"))
                .map(Map.Entry::getValue)
                .forEach(kv -> {
                    int p = kv.indexOf('=');
                    if (p > 0) env.put(kv.substring(0, p), kv.substring(p + 1));
                });

        boolean dryRun = cli.containsKey("--dry-run");

        // Load config
        RunConfigIO io = new RunConfigIO(new com.fasterxml.jackson.databind.ObjectMapper());
        RunConfig cfg = io.read(configPath);

        // Build variable bag
        VariableBag bag = VariableBag.from(snap, workBase, env);

        // Resolve top-level derived variables
        String runFolder = Resolver.resolve(cfg.runFolder(), bag);
        bag.put("RUN_FOLDER", runFolder);

        // Resolve inputs (optional)
        Map<String, String> inputs = new LinkedHashMap<>();
        if (cfg.inputs() != null) {
            for (var e : cfg.inputs().entrySet()) {
                String v = Resolver.resolve(e.getValue(), bag);
                inputs.put(e.getKey(), v);
                bag.put("INPUT." + e.getKey(), v);
            }
        }

        // Print overview
        System.out.println("== RunConfig ==");
        System.out.println("Name       : " + cfg.name());
        System.out.println("Kind       : " + cfg.kind());
        System.out.println("Run Folder : " + runFolder);
        System.out.println("Target     : " + target + " (slug=" +
        	bag.get("TARGET_SLUG") + ")");
        System.out.println("Stamp      : " + bag.get("STAMP"));
        System.out.println("Work Base  : " + workBase);
        System.out.println();

        if (inputs.isEmpty()) {
            System.out.println("Inputs     : (none)");
        } else {
            System.out.println("Inputs:");
            inputs.forEach((k, v) -> System.out.println("  " + k + " = " + v));
        }
        System.out.println();

        // Plan copy rules
        System.out.println("== Copy Plan ==");
        for (int i = 0; i < cfg.copy().size(); i++) {
            CopyRule r = cfg.copy().get(i);
            String from = Resolver.resolve(r.from(), bag);
            String toRel = Resolver.resolve(r.to(), bag);
            Path dest = Paths.get(runFolder).resolve(toRel);

            List<String> include = 
            	Optional.ofNullable(r.include()).orElse(List.of("*"));
            List<String> exclude =
            	Optional.ofNullable(r.exclude()).orElse(List.of());

            // Dry list: up to 10 samples
            CopyPlanner.Result preview =
            	CopyPlanner.preview(from, include, exclude, 10);

            System.out.println("Rule #" + (i + 1));
            System.out.println("  from     : " + from);
            System.out.println("  to       : " + dest);
            System.out.println("  include  : " + include);
            System.out.println("  exclude  : " + exclude);
            System.out.println("  behavior : " +
            	(r.behavior() == null ? "overwrite (default)" : r.behavior()));
            System.out.println("  matches  : " + preview.totalMatches +
            	" file(s)");
            for (String s : preview.samples) {
                System.out.println("    - " + s);
            }
        }
        System.out.println();

        // Command preview
        System.out.println("== Command Preview ==");
        Command cmd = cfg.command();
        String exec = Resolver.resolve(cmd.exec(), bag);
        List<String> argsResolved = new ArrayList<>();
        if (cmd.args() != null) {
            for (String a : cmd.args())
            	argsResolved.add(Resolver.resolve(a, bag));
        }
        String cwd = Resolver.resolve(cmd.cwd(), bag);
        Integer timeout = Optional.ofNullable(cmd.timeoutSec()).orElse(0);
        var drm = Optional.ofNullable(cmd.dryRun()).orElse(
        	Command.DryRunMode.DESCRIBE);

        System.out.println("CWD    : " + cwd);
        System.out.println("Exec   : " + exec);
        System.out.println("Args   : " + argsResolved);
        System.out.println("DryRun : " + (dryRun ? "true (CLI flag)" : drm));
        System.out.println("Timeout: " + 
        	(timeout > 0 ? timeout + "s" : "(none)"));

        System.out.println(
        	"\nOK ✅  (Nothing executed; this is just a planner.)");
    }

    private static Map<String, String> parseCli(String[] args) {
        Map<String, String> m = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            String k = args[i];
            if (k.equals("--dry-run")) {
                m.put(k, "true");
            } else if (k.equals("--env")) {
                if (i + 1 >= args.length)
                	throw new IllegalArgumentException("--env needs KEY=VALUE");
                m.put("--env", args[++i]);
            } else if (k.startsWith("--")) {
                if (i + 1 >= args.length)
                	throw new IllegalArgumentException(k + " needs a value");
                m.put(k, args[++i]);
            } else {
                throw new IllegalArgumentException("Unknown arg: " + k);
            }
        }
        return m;
    }

    private static String need(Map<String, String> m, String key) {
        String v = m.get(key);
        if (v == null || v.isBlank())
            throw new IllegalArgumentException("Missing required " + key);
        return v;
    }
    
}
