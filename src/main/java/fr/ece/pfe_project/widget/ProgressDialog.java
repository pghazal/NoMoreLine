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

    private static final int PROGRESS_BAR_WIDTH = 200;

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

    public void setVisible(boolean visible) {
        if (visible) {
            progressBar.setIndeterminate(true);
        } else {
            progressBar.setIndeterminate(false);
        }
        super.setVisible(visible);
    }

    private void init(Runnable runnable, String message) {
        setupControls();
        setupComponent();
        setupEventHandlers();
        setMessage(message);
        setRunnable(runnable);
    }

    private void setupControls() {

        progressBar = new JProgressBar();
        Dimension preferredSize = progressBar.getPreferredSize();
        preferredSize.width = PROGRESS_BAR_WIDTH;
        progressBar.setPreferredSize(preferredSize);
        lblMessage = new JLabel(" ");
    }

    private void setupComponent() {

        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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
        pack();
    }

    private void setupEventHandlers() {

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
