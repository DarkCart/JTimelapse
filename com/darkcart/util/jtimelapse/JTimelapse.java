package com.darkcart.util.jtimelapse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class JTimelapse {

	static String directory = ".";
	static int count = 0;

	public static void main(String[] args) {
		JFrame frame = new JFrame("JTimelapse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 200);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		JLabel l = new JLabel();
		l.setText("JTimelapse");
		l.setFont(new Font("Arial", Font.PLAIN, 20));

		JButton b = new JButton("Start");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					start(Integer.parseInt(JOptionPane.showInputDialog(null,
							"Delay between captures (in seconds)?",
							"JTimelapse", JOptionPane.PLAIN_MESSAGE)));
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "You put in a string.",
							"Wow.", JOptionPane.ERROR_MESSAGE);
				}
			}

		});

		JLabel outputDir = new JLabel();
		outputDir.setText("Current output directory: " + directory);

		JButton loc = new JButton("Change output directory");
		loc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					directory = selectedFile.getAbsolutePath();
					outputDir.setText("Current output directory: " + directory);
				}
			}

		});

		l.setAlignmentX(Component.CENTER_ALIGNMENT);
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
		loc.setAlignmentX(Component.CENTER_ALIGNMENT);
		outputDir.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(l);
		panel.add(b);
		panel.add(loc);
		panel.add(outputDir);
		panel.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
		panel.setFocusable(true);

		panel.setBackground(new Color(220, 220, 220));

		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.revalidate();
	}

	public static void start(int delayBetweenInSeconds) {

		JOptionPane.showMessageDialog(null, "Capture has begun.", "JTimelapse",
				JOptionPane.PLAIN_MESSAGE);
		Timer t = new Timer(delayBetweenInSeconds * 1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				count += 1;
				try {
					BufferedImage img = new Robot()
							.createScreenCapture(new Rectangle(Toolkit
									.getDefaultToolkit().getScreenSize()));
					ImageIO.write(img, "png", new File(
							directory + "/screenshot" + count + ".png"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		});
		t.start();
	}
}
