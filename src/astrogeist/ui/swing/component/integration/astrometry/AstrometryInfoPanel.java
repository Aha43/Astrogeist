package astrogeist.ui.swing.component.integration.astrometry;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import astrogeist.common.Safe;
import astrogeist.engine.integration.api.astrometry.DefaultAstrometryClient;
import astrogeist.engine.integration.api.astrometry.abstraction.AstrometryClient;
import astrogeist.engine.integration.api.astrometry.model.Info;
import astrogeist.ui.swing.panel.CollapsibleSection;

public final class AstrometryInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

	private final AstrometryClient client;

    // UI widgets
    private final JLabel lblJobId   = new JLabel("Job Id: ");
	private final JLabel lblStatus  = new JLabel("Status: ");
    private final JLabel lblFile    = new JLabel("File: ");
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

    private volatile Long currentJobId;

    public AstrometryInfoPanel(AstrometryClient client) {
        this.client = Objects.requireNonNull(client);

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Top: status + file
        var top = new JPanel(new GridLayout(0,1,2,2));
        top.add(lblJobId);
        top.add(lblStatus);
        top.add(lblFile);

        // Busy bar
        busy.setIndeterminate(true);
        busy.setVisible(false);

        // Center: two sections
        var center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

        center.add(section("Calibration", new JScrollPane(calibTable)));
        center.add(Box.createVerticalStrut(8));
        center.add(section("Tags", new JScrollPane(tagsTable)));

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(busy, BorderLayout.SOUTH);

        // Table niceties
        calibTable.setFillsViewportHeight(true);
        tagsTable.setFillsViewportHeight(true);
        calibTable.setRowHeight(20);
        tagsTable.setRowHeight(20);
    }

    private JComponent section(String title, JComponent content) {
    	
      var panel = new JPanel(new BorderLayout());
      panel.add(content, BorderLayout.CENTER);
      panel.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder(""),
          BorderFactory.createEmptyBorder(4,4,4,4)
      ));
      var retVal = new CollapsibleSection(title, panel, true);
      return retVal;
//        var panel = new JPanel(new BorderLayout());
//        var header = new JLabel(title);
//        header.setFont(header.getFont().deriveFont(Font.BOLD));
//        panel.add(header, BorderLayout.NORTH);
//        panel.add(content, BorderLayout.CENTER);
//        panel.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createTitledBorder(""),
//            BorderFactory.createEmptyBorder(4,4,4,4)
//        ));
//        return panel;
    }

    /** Public API: load and display info for a job id */
    public void setJobId(long jobId) {
        this.currentJobId = jobId;
        // clear UI
        lblJobId.setText("Job Id: " + jobId);
        lblStatus.setText("Status: loading…");
        lblFile.setText("File: –");
        calibModel.setRowCount(0);
        tagsModel.setRowCount(0);
        busy.setVisible(true);

        // fetch async
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
    }

    private void renderInfo(Info info) {
    	
        lblStatus.setText("Status: " + Safe.string(info.status()));
        lblFile.setText("File: " + Safe.string(info.originalFileName()));

        calibModel.setRowCount(0);
        var c = info.calibration();
        if (c != null) {
            addCalib("ra", c.ra());
            addCalib("dec", c.dec());
            addCalib("radius", c.radius());
            addCalib("pixscale", c.pixscale());
            addCalib("orientation", c.orientation());
            addCalib("parity", c.parity());
        }

        // Merge tags/machine_tags/objects_in_field
        tagsModel.setRowCount(0);
        var rows = mergeTags(info);
        for (var r : rows) tagsModel.addRow(new Object[]{r.tag, r.typeString()});
    }

    private void addCalib(String name, double val) {
        calibModel.addRow(new Object[]{name, String.valueOf(val)});
    }
    private void addCalib(String name, int val) {
        calibModel.addRow(new Object[]{name, String.valueOf(val)});
    }

    private void showError(Exception ex) {
        lblStatus.setText("Status: error");
        JOptionPane.showMessageDialog(this,
            ex.getMessage(), "Astrometry error", JOptionPane.ERROR_MESSAGE);
    }

    private static Exception unwrap(Throwable t) {
        if (t instanceof java.util.concurrent.CompletionException ce && ce.getCause()!=null) return (Exception) ce.getCause();
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

