package org.andresoviedo.app.modelviewer3D.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.andresoviedo.app.modelviewer3D.Main;


public class SceneControl extends JPanel
		implements
			ActionListener,
			MouseListener {

	JLabel cameraInfo;

	private JCheckBox drawSolidCB;
	private JCheckBox drawSmoothCB;
	private JLabel enableLightsLbl;
	private JCheckBox enableLightsCB;
	private JCheckBox enableTexturesCB;
	private JCheckBox drawNormalsCB;
	private JCheckBox drawBoundingBoxCB;
	private JCheckBox drawWithPerspectiveBoxCB;

	public SceneControl() {
		super();
		init();
	}

	private void init() {
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		int gridY = 0;
		JLabel title = new JLabel("Scene control");
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title, new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));

		Box cameraBox = Box.createHorizontalBox();
		JLabel camera = new JLabel("Camera (x,y,z): ");
		cameraBox.add(camera);
		cameraInfo = new JLabel("-,-,-");
		cameraBox.add(cameraInfo);
		add(cameraBox, new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
				GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(1,
						1, 1, 1), 0, 0));

		drawSolidCB = new JCheckBox("Draw solid",
				Main.getInstance().enableDrawSolid);
		drawSolidCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		drawSolidCB.addActionListener(this);
		add(drawSolidCB, new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));
		{
			drawBoundingBoxCB = new JCheckBox("Draw Bounding Box", Main
					.getInstance().drawNormals);
			drawBoundingBoxCB.setAlignmentX(Component.LEFT_ALIGNMENT);
			drawBoundingBoxCB.addActionListener(this);
			add(drawBoundingBoxCB,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1,
									1), 0, 0));
		}
		{
			drawNormalsCB = new JCheckBox("Draw Normals", Main
					.getInstance().drawNormals);
			drawNormalsCB.setAlignmentX(Component.LEFT_ALIGNMENT);
			drawNormalsCB.addActionListener(this);
			add(drawNormalsCB,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1,
									1), 0, 0));
		}
		{
			Box enableLightsBox = Box.createHorizontalBox();
			enableLightsCB = new JCheckBox("",
					Main.getInstance().enableLighting);
			enableLightsCB.setAlignmentX(Component.LEFT_ALIGNMENT);
			enableLightsCB.addActionListener(this);
			enableLightsBox.add(enableLightsCB);
			enableLightsLbl = new JLabel("<html><u>Enable lights</u></html>");
			enableLightsLbl.setForeground(Color.BLUE);
			enableLightsLbl.setCursor(Cursor
					.getPredefinedCursor(Cursor.HAND_CURSOR));
			enableLightsLbl.addMouseListener(this);
			enableLightsBox.add(enableLightsLbl);

			add(enableLightsBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1,
									1), 0, 0));
		}
		{
			drawSmoothCB = new JCheckBox("Draw smooth",
					Main.getInstance().drawSmooth);
			drawSmoothCB.setAlignmentX(Component.LEFT_ALIGNMENT);
			drawSmoothCB.setEnabled(Main.getInstance().enableLighting);
			drawSmoothCB.addActionListener(this);
			add(drawSmoothCB,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1,
									1), 0, 0));
		}
		{
			drawWithPerspectiveBoxCB = new JCheckBox("Enable perspective",
					Main.getInstance().enablePerspective);
			drawWithPerspectiveBoxCB.setAlignmentX(Component.LEFT_ALIGNMENT);
			drawWithPerspectiveBoxCB.addActionListener(this);
			add(drawWithPerspectiveBoxCB,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1,
									1), 0, 0));
		}
		enableTexturesCB = new JCheckBox("Enable textures", Main
				.getInstance().enableTextures);
		enableTexturesCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		enableTexturesCB.addActionListener(this);
		add(enableTexturesCB, new GridBagConstraints(0, gridY++, 1, 1, 1.0D,
				0D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(1, 1, 1, 1), 0, 0));

		// itemsBox.setAlignmentX(Component.LEFT_ALIGNMENT);

		add(Box.createVerticalGlue());
	}

	public void update() {
		cameraInfo.setText(Main.getInstance().getCamera()
				.intLocationToString());
		revalidate();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == drawSolidCB) {
			Main.getInstance().enableDrawSolid = drawSolidCB.isSelected();
		} else if (e.getSource() == drawSmoothCB) {
			Main.getInstance().drawSmooth = drawSmoothCB.isSelected();
		} else if (e.getSource() == drawBoundingBoxCB) {
			Main.getInstance().drawBoundingBox = drawBoundingBoxCB
					.isSelected();
		} else if (e.getSource() == drawNormalsCB) {
			Main.getInstance().drawNormals = drawNormalsCB.isSelected();
		} else if (e.getSource() == enableLightsCB) {
			drawSmoothCB.setEnabled(enableLightsCB.isSelected());
			Main.getInstance().enableLighting = enableLightsCB
					.isSelected();
		} else if (e.getSource() == enableTexturesCB) {
			Main.getInstance().enableTextures = enableTexturesCB
					.isSelected();
		} else if (e.getSource() == drawWithPerspectiveBoxCB) {
			Main.getInstance().enablePerspective = drawWithPerspectiveBoxCB
					.isSelected();
		}
		Main.getInstance().forceRedrawAll = true;
		Main.getInstance().reshape();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == enableLightsLbl) {
			JDialogLighting lightingDlg = new JDialogLighting();
			lightingDlg.setVisible(true);
		}
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
