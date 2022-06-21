package gui;
import mummymaze.MummyMazeEvent;
import mummymaze.MummyMazeListener;
import mummymaze.MummyMazeState;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;

import javax.swing.JPanel;

import static gui.Properties.*;

public class GameArea extends JPanel implements MummyMazeListener {
	
	private Image trap;
	private Image key;
	private Image stairsDown;
	private Image stairsUp;
	private Image stairsRight;
	private Image stairsLeft;
	private Image scorpion;
	private Image hero;
	private Image beackground;
	private Image mummyWhite;
	private Image mummyRed;
	private Image wallHorizontal;
	private Image wallVertical;
	private Image doorHorizontalOpen;
	private Image doorHorizontalClosed;
	private Image doorVerticalOpen;
	private Image doorVerticalClosed;
	
	private int xStart = 63;
	private int yStart = 79;
	
	private MummyMazeState state = null;
	private boolean showSolutionCost;
	private double solutionCost;
	
	public GameArea(){
		super();
		setPreferredSize(new Dimension(486,474));
		loadImages();
		showSolutionCost = true;
	}

	private void loadImages(){
		trap = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/armadilha.png"));
		key = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/chave.png"));
		stairsDown = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/escadaBaixo.png"));
		stairsUp = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/escadaCima.png"));
		stairsRight = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/escadaDireita.png"));
		stairsLeft = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/escadaEsquerda.png"));
		scorpion = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/escorpiao.png"));
		hero = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/explorador.png"));
		beackground = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/fundo.png"));
		mummyWhite = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/mumiaBranca.png"));
		mummyRed = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/mumiaVermelha.png"));
		wallHorizontal = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/paredeHorizontal.png"));
		wallVertical = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/paredeVertical.png"));
		doorHorizontalOpen = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/portaHorizontalAberta.png"));
		doorHorizontalClosed = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/portaHorizontalFechada.png"));
		doorVerticalOpen = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/portaVerticalAberta.png"));
		doorVerticalClosed = Toolkit.getDefaultToolkit().getImage(getClass().getResource("./sprites/portaVerticalFechada.png"));
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		
		g.drawImage(beackground,0,0,this);
		
		if(state == null){
			return;
		}

		char[][] matrix = state.getMatrix();

		for(int i = 0; i < MATRIX_LINE_COLUMN_SIZE; i++) {
			for(int j = 0; j < MATRIX_LINE_COLUMN_SIZE; j++) {
				switch(matrix[i][j]) {
					//WALL
					case WALL_HORIZONTAL_CHAR: g.drawImage(wallHorizontal,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					//HORIZONTAL DOOR CLOSED
					case HORIZONTAL_DOOR_CLOSED_CHAR:g.drawImage(doorHorizontalClosed,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					//HORIZONTAL DOOR OPEN
					case HORIZONTAL_DOOR_OPEN_CHAR: g.drawImage(doorHorizontalOpen,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					//WALL
					case WALL_VERTICAL_CHAR: g.drawImage(wallVertical,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					//VERTICAL DOOR CLOSED
					case VERTICAL_DOOR_CLOSED_CHAR: g.drawImage(doorVerticalClosed,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					//VERTICAL DOOR OPEN
					case VERTICAL_DOOR_OPEN_CHAR: g.drawImage(doorVerticalOpen,xStart + j/2 * 60,yStart + i/2 * 60 - 6,this); break;
					//WHITE MUMMY
					case WHITE_MUMMY_CHAR: g.drawImage(mummyWhite,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					//HERO
					case HERO_CHAR: g.drawImage(hero,j == 0 ? xStart + (j-2)/2 * 60 : xStart + j/2 * 60, i ==0 ? yStart + (i-2)/2 * 60 -6 : yStart + i/2 * 60,this); break;
					//RED MUMMY
					case RED_MUMMY_CHAR: g.drawImage(mummyRed,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					//TRAP
					case TRAP_CHAR: g.drawImage(trap,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					//SCORPION
					case SCORPION_CHAR: g.drawImage(scorpion,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					//KEY
					case KEY_CHAR: g.drawImage(key,xStart + j/2 * 60,yStart + i/2 * 60,this); break;
					//EXIT
					case EXIT_CHAR: g.drawImage(i == 0 ? stairsUp : i == 12 ? stairsDown : j == 0 ? stairsLeft : stairsRight,j == 0 ? xStart + (j-2)/2 * 60 : xStart + j/2 * 60, i ==0 ? yStart + (i-2)/2 * 60 -6 : yStart + i/2 * 60,this); break;
				}
			}
		}

		if(showSolutionCost) {
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setColor(Color.LIGHT_GRAY);
			g.drawString("Solution cost: " + solutionCost, 10, 20);
		}
		
	}
	
	public void setState(MummyMazeState state){
		if(state == null){
			throw new NullPointerException("Puzzle cannot be null");
		}

		if(this.state != null)
			this.state.removeListener(this);
		this.state = state;
		state.addListener(this);
		repaint();		
	}

	public void setShowSolutionCost(boolean showSolutionCost) {
		this.showSolutionCost = showSolutionCost;
	}

	public void setSolutionCost(double solutionCost){
		this.solutionCost = solutionCost;
	}

	@Override
	public void puzzleChanged(MummyMazeEvent pe) {
		repaint();
		try{
			//USAR ISTO PARA OS TURNOS TAMBEM
			Thread.sleep(500);
		}catch(InterruptedException ignore){
		}
	}
}
