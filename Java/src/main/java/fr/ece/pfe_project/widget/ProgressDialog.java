package fr.ece.pfe_project.widget;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {

    private static final int PROGRESS_BAR_WIDTH = 300;

    private Runnable runnable;

    private JProgressBar progressBar;
    private JLabel lblMessage;

    /**
     * Constructor.
     *
     * @param parent the parent frame.
     * @param runnable the <tt>Runnable</tt> to be started on
     * <tt>setVisible</tt>.
     * @param message the initial status message.
     */
    public ProgressDialog(JFrame parent, Runnable runnable, String message) {
        super(parent);
        init(runnable, message);

        setLocationRelativeTo(null);
    }

    /**
     * Constructor.
     *
     * @param parent the parent dialog.
     * @param runnable the <tt>Runnable</tt> to be started on
     * <tt>setVisible</tt>.
     * @param message the initial status message.
     */
    public ProgressDialog(JDialog parent, Runnable runnable, String message) {
        super(parent);
        init(runnable, message);

        setLocationRelativeTo(null);
    }

    /**
     * Set the current status message.
     *
     * @param message the message.
     */
    public void setMessage(String message) {
        lblMessage.setText(message);
    }

    /**
     * Set the  <tt>Runnable</tt> to be started on <tt>setVisible</tt>.
     *
     * @param runnable the <tt>Runnable</tt>.
     */
    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public void setVisible(boolean visible) {
        if (visible) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
        }
        super.setVisible(visible);
    }

    protected void init(Runnable runnable, String message) {
        setupControls();
        setupComponent();
        setMessage(message);
        setRunnable(runnable);
        setupEventHandlers();
    }

    protected void setupControls() {

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        Dimension preferredSize = progressBar.getPreferredSize();
        preferredSize.width = PROGRESS_BAR_WIDTH;
        progressBar.setPreferredSize(preferredSize);
        lblMessage = new JLabel(" ");
    }

    protected void setupComponent() {

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = GridBagConstraints.RELATIVE;
        gc.anchor = GridBagConstraints.NORTHWEST;
        contentPane.add(lblMessage, gc);
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(progressBar, gc);

        setTitle("");
        setModal(true);
        setResizable(false);
        setUndecorated(true);
        pack();
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public JLabel getLblMessage() {
        return lblMessage;
    }

    public void setLblMessage(JLabel lblMessage) {
        this.lblMessage = lblMessage;
    }

    protected void setupEventHandlers() {

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                final Thread task = new Thread(runnable);
                task.start();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            task.join();
                        } catch (InterruptedException e) {
                        }
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                setVisible(false);
                            }
                        });
                    }
                }.start();
            }
        });
    }

}
