package astrogeist.ui.swing.component.observatory;

import static javax.swing.SwingUtilities.invokeLater;

import astrogeist.engine.observatory.AhaObservatory;

public final class SelectConfigurationDialogDemo {

	public static void main(String[] args) {
		invokeLater(new Runnable() {
			public void run() {
				var config = SelectConfigurationOptions.create(
					new AhaObservatory()).show();
				
				if (config != null) {
					System.out.println("Selected:");
					System.out.println(config.toString());
					return;
				}
				System.out.println("None selected");
			}
		});

	}

}
