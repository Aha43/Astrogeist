package astrogeist.engine.runconfig;

import java.util.Map;

import aha.common.Guards;

public final class Resolver {
	private Resolver() { Guards.throwStaticClassInstantiateError(); }
	
    // Supports ${KEY} and ${ENV.KEY:-default}
    public final static String resolve(String template, 
    	Map<String,String> vars) {
        
    	if (template == null) return null;
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < template.length(); ) {
            int p = template.indexOf("${", i);
            if (p < 0) { out.append(template, i, template.length()); break; }
            out.append(template, i, p);
            int q = template.indexOf('}', p + 2);
            if (q < 0)
            	throw new IllegalArgumentException("Unclosed ${ at: " +
            		template.substring(p));
            String expr = template.substring(p + 2, q);
            String val = resolveExpr(expr, vars);
            if (val == null)
            	throw new IllegalArgumentException("Unknown variable: " + expr);
            out.append(val);
            i = q + 1;
        }
        return out.toString();
    }
    
    private final static String resolveExpr(String expr,
    	Map<String,String> vars) {
        
    	String key = expr;
        String def = null;
        int idx = expr.indexOf(":-");
        if (idx >= 0) { 
        	key = expr.substring(0, idx); def = expr.substring(idx + 2); }
        String v = vars.get(key);
        return v != null ? v : def;
    }
    
}