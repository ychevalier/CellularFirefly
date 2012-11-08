package com.bham.naturalcomp.cellularfirefly.view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.bham.naturalcomp.cellularfirefly.app.Config;
import com.bham.naturalcomp.cellularfirefly.model.Firefly;

public class AppWindow extends JFrame {

	private static final long serialVersionUID = 2012110611;

	public AppWindow(Firefly[][] swarm) {
		
		setTitle("Firefly Coordination " + Config.NUMBER_OF_COLUMNS + " x " + Config.NUMBER_OF_ROWS);
		addWindowListener(new WindowAdapter()
	      {
			public void windowClosing(WindowEvent e)
	         {
	           dispose();
	           System.exit(0); //calling the method is a must
	         }
	      });
		
		JPanel pan = new JPanel();
		GridLayout layout = new GridLayout(Config.NUMBER_OF_ROWS, Config.NUMBER_OF_COLUMNS);
		pan.setLayout(layout);
		add(pan);
		for(int i = 0; i < Config.NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < Config.NUMBER_OF_ROWS; j++) {
				pan.add(new FireflyView(swarm[i][j]));
			}
		}
		
		final Dimension panSize = new Dimension(Config.NUMBER_OF_COLUMNS*10, Config.NUMBER_OF_ROWS*10);
		pan.setMinimumSize(panSize);
		pan.setMaximumSize(panSize);
		pan.setPreferredSize(panSize);
		pack();
		setMinimumSize(new Dimension(200, 200));
		setResizable(false);
		setVisible(true);
	}	
	
}
