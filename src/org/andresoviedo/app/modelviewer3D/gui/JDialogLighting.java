package org.andresoviedo.app.modelviewer3D.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.andresoviedo.app.modelviewer3D.Main;
import org.andresoviedo.app.modelviewer3D.objects.BoundingBox;
import org.andresoviedo.app.modelviewer3D.objects.Light;
import org.andresoviedo.app.modelviewer3D.objects.Object3D;


public class JDialogLighting extends JDialog
		implements
			ActionListener,
			MouseListener {

	private JCheckBox shadeModelSmooth;
	private Map<JCheckBox, Light> cbs = new Hashtable<JCheckBox, Light>();
	private Map<JLabel, Light> lbls = new Hashtable<JLabel, Light>();

	public JDialogLighting() {
		super(Main.getInstance(), "Propiedades de la luz", true);
		init();
		pack();
		setLocationRelativeTo(Main.getInstance());
	}

	private void init() {
		// Set layout
		Container contentPane = getContentPane();
		GridBagLayout gbl = new GridBagLayout();
		contentPane.setLayout(gbl);

		int gridY = 0;

		// Title
		List<Light> lights = Main.getInstance().lights;
		for (Light light : lights) {
			// Box lightBox = Box.createHorizontalBox();
			// JLabel label = new JLabel(light.getName());
			// lightBox.add(label);

			Box lightBox = Box.createHorizontalBox();
			JCheckBox lightCB = new JCheckBox("", light.enabled);
			lightCB.addActionListener(this);
			lightBox.add(lightCB);
			JLabel lightLbl = new JLabel("<html><u>" + light.getName()
					+ "</u></html>");
			lightLbl.setForeground(Color.BLUE);
			lightLbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lightLbl.addMouseListener(this);
			lightBox.add(lightLbl);

			contentPane.add(lightBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.WEST,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));

			cbs.put(lightCB, light);
			lbls.put(lightLbl, light);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBox) {
			Light light = cbs.get(e.getSource());
			light.enabled = ((JCheckBox) e.getSource()).isSelected();
			Main.getInstance().forceRedrawAll = true;
			dispose();
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JLabel) {
			Light light = lbls.get(e.getSource());
			JDialogLight dialog = new JDialogLight(light);
			dialog.setVisible(true);
			dispose();
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
