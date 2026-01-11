package astrogeist.engine.observatory;

import static aha.common.guard.StringGuards.requireNonEmpty;

public abstract class Observatory {
	
	private InventoryNode root = InventoryNode.createRoot("Observatory");
	
	private final Configurations configurations = new Configurations(); 
	
	protected Observatory() { this("Observatory"); }
	
	protected Observatory(String name) {
		this.root = InventoryNode.createRoot(requireNonEmpty(name, "name"));
		buildInventory(root);
		createConfigurations(root, configurations);
	}
	
	protected abstract void buildInventory(InventoryNode root);
	
	protected abstract void createConfigurations(InventoryNode root, 
		Configurations configurations);

	public final InventoryNode root() { return this.root; }
	
	public final Configurations configurations() { return this.configurations; }
	
}
