package ca.lakeeffect.eventreader;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

public class Selection extends JFrame implements MouseListener, ActionListener, KeyListener {

	private static final long serialVersionUID = 2L;

	private JPanel contentPane;
	
	Choice choice;
	
	JTextArea robotSearch;
	private JSplitPane inputSplitPane;
	
	Button selectRobot;
	DirectoryChooser dirChooser;
	private Button selectDir;
	String directory = "C:\\Users\\Ajay\\git\\Scouting-Event-Reader\\src\\ca\\lakeeffect\\eventreader\\EventData";
	private JSplitPane splitPane;
	private JLabel dirLabel;
	
	ArrayList<String> robotNumbers = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Selection frame = new Selection();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Selection() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setSize(260, 150);
		setResizable(false);
		setTitle("Event Viewer");
		
		
		inputSplitPane = new JSplitPane();
		inputSplitPane.setDividerSize(0);
		inputSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		inputSplitPane.setDividerLocation(25);
		contentPane.add(inputSplitPane, BorderLayout.CENTER);
		
		robotSearch = new JTextArea();
		robotSearch.addKeyListener(this);
		
		choice = new Choice();
		
		inputSplitPane.setTopComponent(robotSearch);
		inputSplitPane.setBottomComponent(choice);
		
		selectRobot = new Button("Select");
		contentPane.add(selectRobot, BorderLayout.SOUTH);
		selectRobot.addMouseListener(this);
		
		splitPane = new JSplitPane();
		splitPane.setDividerSize(1);
		splitPane.setDividerLocation(200);//(int) (contentPane.getWidth()/4*3));
		contentPane.add(splitPane, BorderLayout.NORTH);
		
		selectDir = new Button("CD");
		splitPane.setRightComponent(selectDir);
		
		dirLabel = new JLabel(directory);
		changeDir(directory);
		splitPane.setLeftComponent(dirLabel);
		selectDir.addMouseListener(this);
		

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource() == selectDir){
			System.out.println("selectDir");
			DirectoryChooser frame = new DirectoryChooser(this, directory);
			frame.setVisible(true);
		}
		if(e.getSource() == selectRobot){
			selectRobot();
		}
	}
	
	public void selectRobot() {
		String file = directory+"\\"+choice.getSelectedItem()+".csv";
		System.out.println(file);		
		new Field(choice.getSelectedItem(), 640, 480, file);
	}

	public void changeDir(String directory){
		this.directory = directory;
		System.out.println(directory);
		dirLabel.setText(directory);
		File dir = new File(directory);
		String[] children = dir.list();
		choice.removeAll();
		for(String c : children){
			if(c.endsWith(".csv")){
				choice.add(c.replace(".csv", ""));
				robotNumbers.add(c.replace(".csv", ""));
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			robotSearch.setText(robotSearch.getText().substring(0, robotSearch.getText().length() - 1));
			
			selectRobot();

		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		String fullSearch = robotSearch.getText();
		
		if(isNumber(e.getKeyChar() + "")) {
			fullSearch += e.getKeyChar();
		} else if(e.getKeyChar() == '\n') {
			return;
		}
		
		choice.removeAll();
		for(String robotNumber : robotNumbers){
			if(robotNumber.contains(fullSearch)) {
				choice.add(robotNumber);
			}
		}
	}
	
	public boolean isNumber(String string) {
		return "1234567890".contains(string);
	}

}
