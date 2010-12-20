/* discovery . Palpable Heroic Perception
 * Copyright (c) 2010, Furyhunter <furyhunter600@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *   
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *   
 * * Neither the name of Furyhunter nor the names of his contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package info.furyhunter.ld19.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class LevelCanvas extends JPanel {
	private static final long serialVersionUID = 1L;
	public final int[] level;
	public int currentTile;
	public int mouseX, mouseY;
	public static final int SPACE = 0;
	public static final int WALL = 1;
	public static final int LATCHMOB_LEFT = 2;
	public static final int LATCHMOB_RIGHT = 3;
	public static final int LATCHMOB_UP = 4;
	public static final int LATCHMOB_DOWN = 5;
	public static final int LATCHMOB_FLOATING = 6;
	public static final int LIFE_PICKUP = 7;
	public static final int OSCILLATINGBALL_LEFT_RIGHT = 8;
	public static final int OSCILLATINGBALL_UP_DOWN = 9;
	public static final int OSCILLATINGBALL_TOPLEFT_BOTTOMRIGHT = 10;
	public static final int OSCILLATINGBALL_TOPRIGHT_BOTTOMLEFT = 11;
	public static final int OSCILLATINGBALL_NONE = 12;
	public static final int ARTIFACT_A = 13;
	public static final int ARTIFACT_B = 14;
	public static final int ARTIFACT_C = 15;
	public static final int ARTIFACT_D = 16;
	public static final int BREAKABLE_WALL = 17;
	public static final int BADENDGAMESWITCH = 18;
	public static final int GOODENDGAMESWITCH = 19;
	public static final int ENDGAMEWALL = 20;
	public static final int CHECKPOINT = 21;
	public static final int FINALBOSS = 22;
	public static final int ARTIFACTDOOR = 23;
	public final JFileChooser chooser;
	public File folder;
	public int roomX = 10;
	public int roomY = 5;
	public Editor owner;
	
	public LevelCanvas(Editor owner) {
		this(new int[300]);
		this.owner = owner;
	}
	
	public void setTile(int x, int y, int tile) {
		level[(y*20)+x] = tile;
	}
	
	public LevelCanvas(final int[] level) {
		this.level = level;
		setSize(320, 240);
		chooser = new JFileChooser();
		
		setPreferredSize(new Dimension(320, 240));
		setVisible(true);
		MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Get the tile position
				int tileX = e.getX()/16;
				int tileY = e.getY()/16;
				
				setTile(tileX, tileY, currentTile);
				e.getComponent().repaint();
			}
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				currentTile -= e.getWheelRotation();
				e.getComponent().repaint();
				owner.updateTitle();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				repaint();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				int tileX = e.getX()/16;
				int tileY = e.getY()/16;
				
				setTile(tileX, tileY, currentTile);
				e.getComponent().repaint();
				repaint();
			}
		};
		
		addMouseListener(adapter);
		addMouseWheelListener(adapter);
		addMouseMotionListener(adapter);
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int ix, iy;
		for (iy=0; iy<15; iy++) {
			for (ix=0; ix<20; ix++) {
				drawTile(level[(iy*20)+ix], ix*16, iy*16, g);
			}
		}
		g.setColor(Color.DARK_GRAY);
		for (iy=0; iy<15; iy++) {
			g.drawLine(0, iy*16, 320, iy*16);
		}
		for (ix=0; ix<20; ix++) {
			g.drawLine(ix*16, 0, ix*16, 240);
		}
		drawTile(currentTile, mouseX-8, mouseY-8, g);
	}
	
	public void drawTile(int tile, int x, int y, Graphics g) {
		int[] xp = {x+8, x+16, x};
		int[] yp = {y, y+16, y+16};
		Polygon p;
		switch (tile) {
		case SPACE:
			break;
		case WALL:
			g.setColor(new Color(61, 49, 19));
			g.fillRect(x, y, 16, 16);
			break;
		case LATCHMOB_LEFT:
			g.setColor(new Color(60, 160, 60));
			g.fillRect(x+4, y, 12, 16);
			break;
		case LATCHMOB_RIGHT:
			g.setColor(new Color(60, 160, 60));
			g.fillRect(x, y, 12, 16);
			break;
		case LATCHMOB_UP:
			g.setColor(new Color(60, 160, 60));
			g.fillRect(x, y+4, 16, 12);
			break;
		case LATCHMOB_DOWN:
			g.setColor(new Color(60, 160, 60));
			g.fillRect(x, y, 16, 12);
			break;
		case LATCHMOB_FLOATING:
			g.setColor(new Color(60, 160, 60));
			g.fillRect(x+4, y+4, 8, 8);
			break;
		case LIFE_PICKUP:
			g.setColor(Color.WHITE);
			g.fillOval(x+2, y+2, 12, 12);
			break;
		case OSCILLATINGBALL_LEFT_RIGHT:
			g.setColor(Color.GRAY);
			g.fillOval(x, y, 16, 16);
			g.setColor(Color.BLACK);
			g.drawLine(x, y+8, x+16, y+8);
			break;
		case OSCILLATINGBALL_UP_DOWN:
			g.setColor(Color.GRAY);
			g.fillOval(x, y, 16, 16);
			g.setColor(Color.BLACK);
			g.drawLine(x+8, y, x+8, y+16);
			break;
		case OSCILLATINGBALL_TOPLEFT_BOTTOMRIGHT:
			g.setColor(Color.GRAY);
			g.fillOval(x, y, 16, 16);
			g.setColor(Color.BLACK);
			g.drawLine(x, y, x+16, y+16);
			break;
		case OSCILLATINGBALL_TOPRIGHT_BOTTOMLEFT:
			g.setColor(Color.GRAY);
			g.fillOval(x, y, 16, 16);
			g.setColor(Color.BLACK);
			g.drawLine(x+16, y, x, y+16);
			break;
		case OSCILLATINGBALL_NONE:
			g.setColor(Color.GRAY);
			g.fillOval(x, y, 16, 16);
			break;
		case ARTIFACT_A:
			g.setColor(Color.CYAN);
			p = new Polygon(xp, yp, 3);
			g.fillPolygon(p);
			g.setColor(Color.RED);
			g.drawString("A", x+5, y+14);
			break;
		case ARTIFACT_B:
			g.setColor(Color.CYAN);
			p = new Polygon(xp, yp, 3);
			g.fillPolygon(p);
			g.setColor(Color.RED);
			g.drawString("B", x+5, y+14);
			break;
		case ARTIFACT_C:
			g.setColor(Color.CYAN);
			p = new Polygon(xp, yp, 3);
			g.fillPolygon(p);
			g.setColor(Color.RED);
			g.drawString("C", x+5, y+14);
			break;
		case ARTIFACT_D:
			g.setColor(Color.CYAN);
			p = new Polygon(xp, yp, 3);
			g.fillPolygon(p);
			g.setColor(Color.RED);
			g.drawString("D", x+5, y+14);
			break;
		case BREAKABLE_WALL:
			g.setColor(new Color(40, 20, 0));
			g.fillRect(x, y, 16, 16);
			break;
		case BADENDGAMESWITCH:
			g.setColor(Color.RED);
			g.fillOval(x, y, 16, 16);
			break;
		case GOODENDGAMESWITCH:
			g.setColor(Color.GREEN);
			g.fillOval(x, y, 16, 16);
			break;
		case ENDGAMEWALL:
			g.setColor(Color.GRAY);
			g.fillRect(x, y, 16, 16);
			break;
		case CHECKPOINT:
			g.setColor(Color.YELLOW);
			g.drawOval(x, y, 16, 16);
			break;
		case FINALBOSS:
			g.setColor(Color.MAGENTA);
			g.fillRect(x, y, 16, 16);
			break;
		case ARTIFACTDOOR:
			g.setColor(Color.BLUE);
			g.fillRect(x, y, 16, 16);
			break;
		default:
			g.setColor(Color.RED);
			g.drawString("E",x+4,y+8);
			break;
		}
	}
	
	public void open() {
		/*chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this.getParent())
				== JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				Scanner scanner = new Scanner(file);
				int i = 0;
				while(scanner.hasNextInt()) {
					level[i] = scanner.nextInt();
					i++;
				}
				scanner.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		
		// Select a directory, then load the 10x5.txt map
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if (chooser.showOpenDialog(this.getParent())
				== JFileChooser.APPROVE_OPTION) {
			folder = chooser.getSelectedFile();
			roomX = 10;
			roomY = 5;
			File file = new File(
					folder.getAbsolutePath()+"/"+roomX+"x"+roomY+".txt");
			try {
				Scanner scanner = new Scanner(file);
				int i = 0;
				while(scanner.hasNextInt()) {
					level[i] = scanner.nextInt();
					i++;
				}
				scanner.close();
				repaint();
			} catch (FileNotFoundException e) {
				reset();
			}
		}
	}
	
	public void subOpen() {
		File file = new File(
				folder.getAbsolutePath()+"/"+roomX+"x"+roomY+".txt");
		try {
			Scanner scanner = new Scanner(file);
			int i = 0;
			while(scanner.hasNextInt()) {
				level[i] = scanner.nextInt();
				i++;
			}
			scanner.close();
			repaint();
		} catch (FileNotFoundException e) {
			reset();
		}
	}
	
	public void save() {
		/*if (chooser.showSaveDialog(this.getParent())
				== JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			try {
				PrintWriter write = new PrintWriter(file);
				int i = 0;
				for (i=0; i<level.length; i++) {
					write.print(level[i]+" ");
				}
				write.flush();
				write.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}*/
		
		// Save based on current room
		File file = new File(
				folder.getAbsolutePath()+"/"+roomX+"x"+roomY+".txt");
		try {
			PrintWriter write = new PrintWriter(file);
			int i = 0;
			for (i=0; i<level.length; i++) {
				write.print(level[i]+" ");
			}
			write.flush();
			write.close();
		} catch (Exception e) {
			System.err.println("The file "+file.getName()+" was not saved.");
			e.printStackTrace(System.err);
		}
	}
	
	public void reset() {
		int i = 0;
		for (i=0; i<300; i++) {
			level[i] = 0;
		}
		repaint();
	}
	
	public void generateWorldMap() {
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.showSaveDialog(getParent());
		File out = chooser.getSelectedFile();
		try {
			BufferedImage image = new BufferedImage(20*20, 15*15,
					BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			
			int originalX = roomX;
			int originalY = roomY;
			
			int ix, iy, im;
			for (iy=0; iy<15; iy++) {
				System.out.println("Horizontal Line "+iy);
				roomY = iy;
				for (ix=0; ix<20; ix++) {
					System.out.println("Vertical Line "+ix);
					roomX = ix;
					subOpen();
					for (im=0; im<300; im++) {
						switch (level[im]) {
						case SPACE:
							g.setColor(Color.BLACK); break;
						case WALL:
							g.setColor(new Color(61, 49, 19)); break;
						case LATCHMOB_LEFT:
							g.setColor(new Color(60, 160, 60)); break;
						case LATCHMOB_RIGHT:
							g.setColor(new Color(60, 160, 60)); break;
						case LATCHMOB_UP:
							g.setColor(new Color(60, 160, 60)); break;
						case LATCHMOB_DOWN:
							g.setColor(new Color(60, 160, 60)); break;
						case LIFE_PICKUP:
							g.setColor(Color.WHITE); break;
						case OSCILLATINGBALL_LEFT_RIGHT:
							g.setColor(Color.DARK_GRAY); break;
						case OSCILLATINGBALL_UP_DOWN:
							g.setColor(Color.DARK_GRAY); break;
						case OSCILLATINGBALL_TOPLEFT_BOTTOMRIGHT:
							g.setColor(Color.DARK_GRAY); break;
						case OSCILLATINGBALL_TOPRIGHT_BOTTOMLEFT:
							g.setColor(Color.DARK_GRAY); break;
						case ARTIFACT_A:
							g.setColor(Color.CYAN); break;
						case ARTIFACT_B:
							g.setColor(Color.CYAN); break;
						case ARTIFACT_C:
							g.setColor(Color.CYAN); break;
						case ARTIFACT_D:
							g.setColor(Color.CYAN); break;
						case BREAKABLE_WALL:
							g.setColor(new Color(40, 20, 0)); break;
						case BADENDGAMESWITCH:
							g.setColor(Color.RED); break;
						case GOODENDGAMESWITCH:
							g.setColor(Color.GREEN); break;
						case ENDGAMEWALL:
							g.setColor(Color.GRAY); break;
						case CHECKPOINT:
							g.setColor(Color.YELLOW); break;
						case FINALBOSS:
							g.setColor(Color.MAGENTA); break;
						case ARTIFACTDOOR:
							g.setColor(Color.BLUE); break;
						}
						g.drawRect((im%20)+(ix*20), (int)(im/20)+(iy*15), 1, 1);
					}
				}
			}
			g.dispose();
			
			Iterator<ImageWriter> writers =
				ImageIO.getImageWritersByFormatName("png");
			ImageWriter writer = (ImageWriter)writers.next();
			ImageOutputStream ios = ImageIO.createImageOutputStream(out);
			writer.setOutput(ios);
			writer.write(image);
			ios.flush();
			ios.close();
			
			roomX = originalX;
			roomY = originalY;
			subOpen();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
