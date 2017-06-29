package waffle.wafflecontrol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import waffle.Config;

public class WaffleGraphic implements ActionListener {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JPanel controlPanel;
    private JPanel logPanel;
    private JLabel listenPortLabel;
    private JLabel peerHostNameLabel;
    private JLabel peerPortLabel;
    private JLabel miningLabel;
    private JLabel waffle;
    private JTextField listenPortField;
    private JTextField peerHostNameField;
    private JTextField peerPortField;
    private JCheckBox miningCheck;
    private JButton executeButton;
    private JTextArea logArea;
    private JScrollPane logScroll;

    private boolean ready = false;

    public WaffleGraphic() {
        frame = new JFrame("Waffle Control");
        frame.setSize(400, 450);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(null);

        controlPanel = new JPanel();
        controlPanel.setPreferredSize(null);
        controlPanel.setLayout(new GridLayout(5, 2, 10, 10));

        listenPortLabel = new JLabel("Listen Port", JLabel.CENTER);
        listenPortLabel.setPreferredSize(null);

        peerHostNameLabel = new JLabel("Peer HostName", JLabel.CENTER);
        peerHostNameLabel.setPreferredSize(null);

        peerPortLabel = new JLabel("Peer Port", JLabel.CENTER);
        peerPortLabel.setPreferredSize(null);

        miningLabel = new JLabel("Enable Mining", JLabel.CENTER);
        miningLabel.setPreferredSize(null);

        waffle = new JLabel("Waffle\u00ae", JLabel.CENTER);
        waffle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        waffle.setPreferredSize(null);

        listenPortField = new JTextField("1189");
        listenPortField.setPreferredSize(null);
        listenPortField.setHorizontalAlignment(JTextField.CENTER);

        peerHostNameField = new JTextField();
        peerHostNameField.setPreferredSize(null);
        peerHostNameField.setHorizontalAlignment(JTextField.CENTER);

        peerPortField = new JTextField("1189");
        peerPortField.setPreferredSize(null);
        peerPortField.setHorizontalAlignment(JTextField.CENTER);

        miningCheck = new JCheckBox();
        miningCheck.setPreferredSize(null);

        executeButton = new JButton("Execute");
        executeButton.setPreferredSize(null);
        executeButton.addActionListener(this);

        controlPanel.add(listenPortLabel);
        controlPanel.add(listenPortField);
        controlPanel.add(peerHostNameLabel);
        controlPanel.add(peerHostNameField);
        controlPanel.add(peerPortLabel);
        controlPanel.add(peerPortField);
        controlPanel.add(miningLabel);
        controlPanel.add(miningCheck);
        controlPanel.add(waffle);
        controlPanel.add(executeButton);

        tabbedPane.addTab("Control", controlPanel);

        logPanel = new JPanel();
        logPanel.setPreferredSize(null);

        logArea = new JTextArea();
        logArea.setPreferredSize(new Dimension(370, 370));
        logArea.setEditable(false);

        logScroll = new JScrollPane(logArea);
        logScroll.setPreferredSize(null);

        logPanel.add(logScroll);

        tabbedPane.addTab("Log", logPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (listenPortField.getText().equals("")) {
            return;
        }

        listenPortField.setEditable(false);
        peerHostNameField.setEditable(false);
        peerPortField.setEditable(false);
        miningCheck.setEnabled(false);

        if (peerHostNameField.getText().equals("")) {
            Config.setPeerHostName("-1");
        } else {
            Config.setPeerHostName(peerHostNameField.getText());
        }

        if (peerPortField.getText().equals("")) {
            Config.setPeerPort(-1);
        } else {
            Config.setPeerPort(Integer.parseInt(peerPortField.getText()));
        }

        Config.setListenPort(Integer.parseInt(listenPortField.getText()));
        Config.setIsMining(miningCheck.isSelected());
        ready = true;
    }

    public boolean isReady() {
        return ready;
    }

    public void addLog(String log) {
        logArea.append(log);
    }
}
