package waffle.wafflecontrol;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import waffle.*;

public class WaffleGraphic implements ActionListener {
	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel controlPanel;
	private JPanel logPanel;
	private JLabel listenPortLabel;
	private JLabel peerHostnameLabel;
	private JLabel peerPortLabel;
	private JLabel miningLabel;
	private JLabel waffle;
	private JTextField listenPortField;
	private JTextField peerHostnameField;
	private JTextField peerPortField;
	private JCheckBox miningCheck;
	private JButton executeButton;
	private JTextArea logArea;
	private JScrollPane logScroll;
	
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
		
		peerHostnameLabel = new JLabel("Peer Hostname", JLabel.CENTER);
		peerHostnameLabel.setPreferredSize(null);
		
		peerPortLabel = new JLabel("Peer Port", JLabel.CENTER);
		peerPortLabel.setPreferredSize(null);
		
		miningLabel = new JLabel("Enable Mining", JLabel.CENTER);
		miningLabel.setPreferredSize(null);
		
		waffle = new JLabel("Waffle\u00ae", JLabel.CENTER);
		waffle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
		waffle.setPreferredSize(null);
		
		listenPortField = new JTextField("8080");
		listenPortField.setPreferredSize(null);
		listenPortField.setHorizontalAlignment(JTextField.CENTER);
		
		peerHostnameField = new JTextField();
		peerHostnameField.setPreferredSize(null);
		peerHostnameField.setHorizontalAlignment(JTextField.CENTER);
		
		peerPortField = new JTextField("8080");
		peerPortField.setPreferredSize(null);
		peerPortField.setHorizontalAlignment(JTextField.CENTER);
		
		miningCheck = new JCheckBox();
		miningCheck.setPreferredSize(null);
		
		executeButton = new JButton("Execute");
		executeButton.setPreferredSize(null);
		executeButton.addActionListener(this);
		
		controlPanel.add(listenPortLabel);
		controlPanel.add(listenPortField);
		controlPanel.add(peerHostnameLabel);
		controlPanel.add(peerHostnameField);
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
		if (listenPortField.getText().equals("") || peerHostnameField.getText().equals("") || peerPortField.getText().equals("")) {
			return;
		}
		
		listenPortField.setEditable(false);
		peerHostnameField.setEditable(false);
		peerPortField.setEditable(false);
		
		Config.setConfigData(Integer.parseInt(listenPortField.getText()), peerHostnameField, Integer.parseInt(peerPortField.getText()));
	}
	
	public void addLog(String log) {
		logArea.append(log);
	}
}