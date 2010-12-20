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

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class Editor extends JFrame {
	private static final long serialVersionUID = 1L;
	public final LevelCanvas canvas;
	
	public Editor() {
		setResizable(false);
		getContentPane().setLayout(new GridLayout(1, 1));
		canvas = new LevelCanvas(this);
		getContentPane().add(canvas);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
					canvas.save();
					canvas.roomX -= 1;
					canvas.subOpen();
					updateTitle();
				}
				if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
					canvas.save();
					canvas.roomX += 1;
					canvas.subOpen();
					updateTitle();
				}
				if (arg0.getKeyCode() == KeyEvent.VK_UP) {
					canvas.save();
					canvas.roomY -= 1;
					canvas.subOpen();
					updateTitle();
				}
				if (arg0.getKeyCode() == KeyEvent.VK_DOWN) {
					canvas.save();
					canvas.roomY += 1;
					canvas.subOpen();
					updateTitle();
				}
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				if (arg0.getKeyChar() == 'o') {
					canvas.open();
					updateTitle();
				}
				if (arg0.getKeyChar() == 's') {
					canvas.save();
				}
				if (arg0.getKeyChar() == 'r') {
					canvas.reset();
				}
				if (arg0.getKeyChar() == 'm') {
					canvas.generateWorldMap();
				}
			}
			
		});
		pack();
		setLocationRelativeTo(null);
		canvas.open();
		updateTitle();
	}
	
	public static void main(String[] args) {
		new Editor().setVisible(true);
	}
	
	public void updateTitle() {
		String title = "LD19edit: " + canvas.roomX + "x" + canvas.roomY + " "
				+ "Tile " + canvas.currentTile;
		setTitle(title);
	}
}
