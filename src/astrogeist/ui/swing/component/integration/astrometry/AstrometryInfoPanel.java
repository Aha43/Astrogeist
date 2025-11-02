package astrogeist.ui.swing.component.integration.astrometry;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import aha.common.DecimalFormats;
import aha.common.Guards;
import aha.common.Safe;
import astrogeist.engine.integration.api.astrometry.DefaultAstrometryClient;
import astrogeist.engine.integration.api.astrometry.abstraction.AstrometryClient;
import astrogeist.engine.integration.api.astrometry.model.Annotations;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.ui.swing.dialog.message.MessageDialogs;
import astrogeist.ui.swing.panel.CollapsibleSection;

public final class AstrometryInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	private final AstrometryClient client;
	
	private final ChangeEvent changeEvent = new ChangeEvent(this);
	
	private final CopyOnWriteArrayList<ChangeListener> changeListener = new CopyOnWriteArrayList<>();
	
	public void addChangeListener(ChangeListener l) {
		Objects.requireNonNull(l, "l");
		this.changeListener.add(l);
	}
	
	public void removeChangeListener(ChangeListener l) {
		Objects.requireNonNull(l, "l");
		this.changeListener.remove(l);
	}
	
	private void fireChangeEvent() {
		for (var l : this.changeListener) l.stateChanged(this.changeEvent); }

    // UI widgets
    private final JLabel lblJobId        = new JLabel("Job Id: ");
    private final JTextField jobIdField  = new JTextField();
	private final JLabel lblStatus       = new JLabel("Status: ");
	private final JTextField statusField = new JTextField();
    private final JLabel lblFile         = new JLabel("File: ");
    private final JTextField fileField   = new JTextField();
    
    private final JProgressBar busy = new JProgressBar();

    private final DefaultTableModel calibModel =
        new DefaultTableModel(new Object[]{"Property", "Value"}, 0) {
            private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
        };
    private final JTable calibTable = new JTable(calibModel);

    private final DefaultTableModel tagsModel =
        new DefaultTableModel(new Object[]{"Tag", "Type"}, 0) {
            private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
        };
    private final JTable tagsTable = new JTable(tagsModel);
    
    private final DefaultTableModel annoModel = 
    	new DefaultTableModel(new Object[]{"Type", "Names", "pixelx", "pixely", "radius"}, 0) {
			private static final long serialVersionUID = 1L;
			@Override public boolean isCellEditable(int r, int c) { return false; }
    	};
    private final JTable annoTable = new JTable(annoModel);

    private volatile Long currentJobId;

    public AstrometryInfoPanel(AstrometryClient client) {
        this.client = Objects.requireNonNull(client);
        
        this.annoTable.getColumnModel().getColumn(1).setPreferredWidth(300);

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        this.statusField.setEditable(false);
        this.fileField.setEditable(false);
        
        this.jobIdField.addActionListener(e -> {
        	this.setJobId(this.jobIdField.getText());
        });

        // Top: status + file
        var top = new JPanel(new GridLayout(0,2,2,2));
        top.add(lblJobId);
        top.add(this.jobIdField);
        top.add(lblStatus);
        top.add(this.statusField);
        top.add(lblFile);
        top.add(this.fileField);

        // Busy bar
        busy.setIndeterminate(true);
        busy.setVisible(false);

        // Center: two sections
        var center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(new CollapsibleSection("Calibration", new JScrollPane(calibTable)));
        center.add(new CollapsibleSection("Tags", new JScrollPane(tagsTable)));
        center.add(new CollapsibleSection("Annotations", new JScrollPane(annoTable)));

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(busy, BorderLayout.SOUTH);

        // Table niceties
        calibTable.setFillsViewportHeight(true);
        tagsTable.setFillsViewportHeight(true);
        calibTable.setRowHeight(20);
        tagsTable.setRowHeight(20);
    }
    
    public final void setJobId(String jobId) {
    	try {
    		var id = Long.parseLong(jobId);
    		setJobId(id);
    	} catch (Exception x) {
    		MessageDialogs.showError(this, "Not valid job id : '" + jobId + "'", x);
    	}
    }

    /** 
     * <p>
     *   Load and display info for a astrometry job id 
     * </p>
     */
    public final void setJobId(long jobId) {
    	Guards.requireNonNegative(jobId, "Job Id");
    	
        this.currentJobId = jobId;
        // clear UI
        this.jobIdField.setText("" + jobId);
        this.statusField.setText("loading…");
        this.fileField.setText("loading…");
        calibModel.setRowCount(0);
        tagsModel.setRowCount(0);
        busy.setVisible(true);

        this.loadFromAstrometry(jobId);
        
        this.fireChangeEvent();
    }
    
    public final long getJobId() { return this.currentJobId; }
    
    private final void loadFromAstrometry(long jobId) {
    	client.getInfoAsync(jobId).whenComplete((info, err) -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    if (err != null) throw unwrap(err);
                    if (!Objects.equals(currentJobId, jobId)) return; // user changed quickly
                    renderInfo(info);
                } catch (Exception ex) {
                    showError(ex);
                } finally {
                    busy.setVisible(false);
                }
            });
        });
        
        client.getAnnotationsAsync(jobId).whenComplete((annos, err) -> {
            SwingUtilities.invokeLater(() -> {
                try {
                    if (err != null) throw unwrap(err);
                    if (!Objects.equals(currentJobId, jobId)) return; // user changed quickly
                    renderAnnos(annos);
                } catch (Exception ex) {
                    showError(ex);
                } finally {
                    busy.setVisible(false);
                }
            });
        });
    }

    private final void renderInfo(Info info) {
        this.statusField.setText(Safe.string(info.status()));
        this.fileField.setText(Safe.string(info.originalFileName()));

        this.calibModel.setRowCount(0);
        var c = info.calibration();
        if (c != null) {
        	this.addCalib("ra", c.ra());
        	this.addCalib("dec", c.dec());
        	this.addCalib("radius", c.radius());
        	this.addCalib("pixscale", c.pixscale());
        	this.addCalib("orientation", c.orientation());
        	this.addCalib("parity", c.parity());
        }

        // Merge tags/machine_tags/objects_in_field
        this.tagsModel.setRowCount(0);
        var rows = mergeTags(info);
        for (var r : rows) this.tagsModel.addRow(new Object[]{r.tag, r.typeString()});
    }
    
    private void addCalib(String name, double val) {
        calibModel.addRow(new Object[]{name, name.equals("pixscale") ? 
        	DecimalFormats.DF3.format(val) : DecimalFormats.DF6.format(val)});
    }
    
    private final void addCalib(String name, int val) {
        calibModel.addRow(new Object[]{name, String.valueOf(val)}); }

    private final void showError(Exception ex) {
        lblStatus.setText("Status: error");
        JOptionPane.showMessageDialog(this,
            ex.getMessage(), "Astrometry error", JOptionPane.ERROR_MESSAGE);
    }
    
    private final void renderAnnos(Annotations annotations) {
    	this.annoModel.setRowCount(0);
    	for (var ann : annotations.annotations()) {
    		this.annoModel.addRow(new Object[] {
    			ann.type(),
    			String.join(",", ann.names()),
    			ann.pixelx(),
    			ann.pixely(),
    			ann.radius()
    		});
    	}
    }

    private final static Exception unwrap(Throwable t) {
        if (t instanceof CompletionException ce && ce.getCause() != null) return (Exception)ce.getCause();
        if (t instanceof Exception e) return e;
        return new RuntimeException(t);
    }

    // --- Tag merging ---

    private static final class TagRow {
        final String tag;
        boolean isTag, isMachine, isObject;
        TagRow(String tag) { this.tag = tag; }
        String typeString() {
            var list = new ArrayList<String>(3);
            if (isTag) list.add("tag");
            if (isMachine) list.add("machine");
            if (isObject) list.add("object");
            return String.join(" ", list);
        }
    }

    private static List<TagRow> mergeTags(Info info) {
        Map<String, TagRow> map = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (String s : Safe.array(info.tags())) {
            map.computeIfAbsent(s, TagRow::new).isTag = true; }
        for (String s : Safe.array(info.machineTags())) {
            map.computeIfAbsent(s, TagRow::new).isMachine = true; }
        for (String s : Safe.array(info.objectsInField())) { // your method was pluralized; adjust to your getter
            map.computeIfAbsent(s, TagRow::new).isObject = true; }
        return map.values().stream().collect(Collectors.toList());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Hardcoded test job ID — pick one that’s public / yours
                long jobId = 14260279L;  // <-- replace with a real one you know works

                // Set up client and panel
                var client = new DefaultAstrometryClient();
                var panel = new AstrometryInfoPanel(client);

                // Create dialog frame
                var dialog = new JDialog((Frame) null, "Astrometry Job Test", false);
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.getContentPane().add(panel);
                dialog.setSize(700, 500);
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);

                // Kick off loading
                panel.setJobId(jobId);

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.toString(),
                    "Startup Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
}
