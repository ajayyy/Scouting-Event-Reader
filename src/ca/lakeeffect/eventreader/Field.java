package ca.lakeeffect.eventreader;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Field extends JComponent implements MouseMotionListener, ActionListener, KeyListener{
	
	private static final long serialVersionUID = 1L;
	private String title;
	private int height;
	private int width;
	
	private JFrame window;
	
	Reader[] readers;
	
	BufferedImage field;
	
	Path[][] paths;
	
	float scale;
	
	InfoPanel info = new InfoPanel();
	
	Point[] locations = {
			new Point(500,800),
			new Point(105,65),
			new Point(120,360),
			new Point(105,839),
			new Point(380,450),
			new Point(495,315),
			new Point(495,598),
			new Point(890,265),
			new Point(890,639),
			new Point(1283,315),
			new Point(1283,598),
			new Point(1398,450),
			new Point(1673,65),
			new Point(1658,544),
			new Point(1673,839)
	};
//	Point[] locations = {
//			new Point(100,100),
//			new Point(100, 200),
//	};
	
	Color[] colors = {
			new Color(0, 255, 0, 120),
			new Color(255, 0, 0, 120),
			new Color(0, 0, 255, 120)
	};
	
	Color[] nonAlphaColors = {
			new Color(0, 255, 0),
			new Color(255, 0, 0),
			new Color(0, 0, 255)
	};
	
	String[] locationNames = {
			"Ground",
			"Top Red Portal",
			"Red Vault",
			"Bottom Red Portal",
			"Red Pickup Location",
			"Top Red Switch",
			"Bottom Red Switch",
			"Top Scale",
			"Bottom Scale",
			"Top Blue Switch",
			"Bottom Blue Switch",
			"Blue Pickup Location",
			"Top Blue Portal",
			"Blue Vault",
			"Bottom Blue Portal"
	};

	BufferedImage hover = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

	boolean hovering;
	int mousex,mousey;
	
	int[] robotNumbers;
	
	public Field(int[] robotNumbers, int width, int height, String[] files){
		setBounds(0, 0, 5000, 5000);
		
		if(robotNumbers.length == 1) {
			colors[0] = new Color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), 255);
		}
		
		this.robotNumbers = robotNumbers;
		this.title = "";
		for (int i=0;i<robotNumbers.length;i++) {
			if(i != 0) {
				this.title += ", ";
			}
			this.title += robotNumbers[i];
		}
		this.width = width;
		this.height = height;
		init();
		window.setVisible(true);
		readers = new Reader[files.length];
		for(int i=0; i < files.length; i++) {
			readers[i] = new Reader();
		}
		changeRobot(files);
	}
	
	public void changeRobot(String[] paths){
		this.paths = new Path[paths.length][];
		for(int i=0; i < paths.length; i++) {
			this.paths[i] = readers[i].read(paths[i]);
		}
	}

	private void init() {
		window = new JFrame(title); 
		window.setLayout(null);
		window.add(this);
		window.pack();
		window.setSize(new Dimension(640, 480));
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addMouseMotionListener(this);
		window.addKeyListener(this);
		addKeyListener(this);
		
		try {
			System.out.println("Loading");
			field = ImageIO.read(Field.class.getResourceAsStream("/ca/lakeeffect/eventreader/field.PNG"));
			System.out.println("Loaded");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed");
		}
	}
	
	@Override
	protected void paintComponent(Graphics g){
		
		Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.white);
		
		g.fillRect(0, 0, window.getWidth(), window.getHeight());
		
		scale = (float) (window.getWidth()/(field.getWidth()*1.00));
		//I know it's redundant, but its there for the future
		g.drawImage(field, 0, 0, (int) (field.getWidth()*scale), (int) (field.getHeight()*scale), null);
		g.setColor(Color.white);
		for(Point p : locations){
			g.fillRect((int) (p.x*scale)-5, (int) (p.y*scale)-5, 10, 10);
		}
		
		g.setColor(Color.white);
		g.setFont(g.getFont().deriveFont(Font.PLAIN, ((int) (field.getHeight()*scale)/4)));
		
		
		if(robotNumbers.length > 0) {
			
			int fontSize = ((int) (field.getHeight()*scale)/16);
			
			g.setFont(g.getFont().deriveFont(Font.PLAIN, fontSize));
			
//			{
//				
//				String fullMessage = robotNumbers[0] + "";
//				
//				g.setColor(nonAlphaColors[0]);
//				
//				g.drawString(fullMessage+"", window.getWidth()/2 - g.getFontMetrics().stringWidth(fullMessage)/2, ((int) (field.getHeight()*scale / 16 * 15)));
//			
//			}
			
			for(int i=0; i < robotNumbers.length; i++) {
				
				String fullMessage = robotNumbers[i] + "";
				
				g.setColor(nonAlphaColors[i]);
				
				g.drawString(fullMessage+"", window.getWidth()/2 - g.getFontMetrics().stringWidth(fullMessage)/2, ((int) (field.getHeight()*scale / 16 * 15)) + fontSize * (i-2) );
			}
			
			
		} else {
			g.drawString(window.getTitle()+"", window.getWidth()/2 - g.getFontMetrics().stringWidth(window.getTitle())/2, ((int) (field.getHeight()*scale / 16 * 15)));
		}
		
		Graphics2D g2d = (Graphics2D) g;
		for(int i = 0; i < paths.length; i++){
			
			g.setColor(colors[i]);
			
			for(Path p : paths[i]){
				g2d.setStroke(new BasicStroke((int) (p.count/2)+1));
				g2d.drawLine((int)(locations[p.startLocation].x*scale), (int)(locations[p.startLocation].y*scale), (int)( locations[p.endLocation].x*scale), (int)( locations[p.endLocation].y*scale));
			}
		}
		
		if(hovering){
			
			BufferedImage infoPanelImage = info.render();
			
			int x = mousex;
			if(x + info.width > window.getWidth()) {
				x = window.getWidth() - info.width;
			}
			
			int y = mousey;
			if(y + info.height > window.getHeight()) {
				y = window.getHeight() - info.height;
			}
			
			g.drawImage(infoPanelImage, x, y, null);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	long time = -1;
	
	@Override
	public void mouseMoved(MouseEvent e) {
		
		if(time == -1) time = System.currentTimeMillis();
//		if(System.currentTimeMillis()-time<25){
//			return;
//		}
		time = System.currentTimeMillis();
		info.clearData();
		Point mouse = new Point((int)(e.getX()/scale), (int)(e.getY()/scale));
		boolean hovering = false;
		if(robotNumbers.length == 1) {
			for(Path p : paths[0]){
//				System.out.println(Math.abs(locations[p.startLocation].distance(locations[p.endLocation]) - (locations[p.startLocation].distance(mouse)+locations[p.endLocation].distance(mouse))));
				if(p.startLocation == -1 || p.endLocation==-1) continue;
				if(Math.abs(locations[p.startLocation].distance(locations[p.endLocation]) - (locations[p.startLocation].distance(mouse)+locations[p.endLocation].distance(mouse)))<15){
//					System.out.println(locationNames[p.startLocation]+"=>"+locationNames[p.endLocation]);
					hovering = true;
					info.addData(new String[] {"Count","Avg. Time","Avg/Match"}, new double[] {p.count, p.averageTime, p.count/1}, new String[] {"","s",""}, locationNames[p.startLocation]+"=>"+locationNames[p.endLocation]);
				}
			}
		}
		for(int i = 0; i < locations.length; i++){
			Point l = new Point((int)(locations[i].x*scale),(int) (locations[i].y*scale));
			if(l.distance(e.getX(), e.getY())<15){
				hovering=true;
				int scoreSuccess = 0, scoreFail = 0, pickupSuccess = 0, pickupFail = 0;
				if(robotNumbers.length == 1) {
					for(Path p : paths[0]){
						if(p.endLocation == i){
							scoreSuccess+=p.scoreSuccess;
							scoreFail+=p.scoreFail;
							pickupSuccess+=p.pickupSuccess;
							pickupFail+=p.pickupFail;
						}
					}
				}
				double scorePercent = 0, pickupPercent = 0;
				if(scoreSuccess+scoreFail > 0) scorePercent = ((double) scoreSuccess/(scoreSuccess+scoreFail))*100;
				if(pickupSuccess+pickupFail > 0) pickupPercent = ((double) pickupSuccess/(pickupSuccess+pickupFail))*100;
				info.addData(new String[] {"Score Count","Score Success","Pickup Count","Pickup Success"}, new double[] {scoreSuccess+scoreFail, scorePercent, pickupSuccess+pickupFail, pickupPercent}, new String[] {"","%","","%"}, locationNames[i]+"");
			}
		}
		this.hovering = hovering;
		mousex = e.getX();
		mousey = e.getY();
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		float[] times = new float[15*15];
		
		for(int i=0;i<times.length;i++) {
			times[i] = 0;
		}
		
		System.out.print("Robot Number,");
		for(int i=0;i<times.length;i++) {
			System.out.print(i/15 + " => " + (i % 15) + ",");
		}
		System.out.print("\n" + window.getTitle() + ",");
		for(int i=0;i<times.length;i++) {
			System.out.print(times[i] + ",");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}

class InfoPanel{
	
	public int width = 250, height = 100;
	private ArrayList<String> labels = new ArrayList<String>();
	private ArrayList<Double> data = new ArrayList<Double>();
	private ArrayList<String> suffix = new ArrayList<String>();
	
	NumberFormat formatter = new DecimalFormat("#0.00");     
	
	Color background = new Color(220, 220, 220, 255);
	
	public BufferedImage render(){
		height = labels.size()*15+5;
		BufferedImage panel = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = panel.createGraphics();
		g.setColor(background);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.black);
		for(int i = 0; i < labels.size(); i++){
			String out;
			if(data.get(i)==-1) out = labels.get(i);
			else out = labels.get(i) + ": " + formatter.format(data.get(i)) + suffix.get(i);
			g.drawString(out, 15, i*15+15);
		}
		g.dispose();
		return panel;
	}
	
	public void addData(String[] labels, double[] data, String[] suffix, String head){
		this.labels.add("----"+head+"----");
		this.data.add(-1d);
		this.suffix.add("");
		for(int i = 0; i < labels.length; i++){
			this.labels.add(labels[i]);
			this.data.add(data[i]);
			this.suffix.add(suffix[i]);
		}
	}
	
	public void clearData(){
		labels.clear();
		data.clear();
		suffix.clear();
	}
	
}
