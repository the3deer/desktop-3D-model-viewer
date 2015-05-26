package org.andresoviedo.app.modelviewer3D.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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


public class JDialogLight extends JDialog implements ActionListener {

	private Light light;
	private JTextField light_position_input;
	private JTextField spot_direction_input;
	private JTextField light_ambient_input;
	private JTextField light_diffuse_input;
	private JTextField light_specular_input;

	public JDialogLight(Light light) {
		super(Main.getInstance(), "Light properties", true);
		this.light = light;
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
		JLabel desc = new JLabel("Light: " + light.getName());
		contentPane.add(desc, new GridBagConstraints(0, gridY++, 1, 1, 1.0D,
				0D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		{
			Box lightPosBox = Box.createHorizontalBox();
			JLabel bb = new JLabel("Position (x,y,z,w): ");
			lightPosBox.add(bb);

			light_position_input = new JTextField(light.light_position[0] + ","
					+ light.light_position[1] + "," + light.light_position[2]
					+ "," + light.light_position[3]);
			light_position_input.setPreferredSize(new Dimension(100, 23));
			lightPosBox.add(light_position_input);

			contentPane.add(lightPosBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Spot direction (x,y,z): ");
			paramBox.add(paramLabel);

			spot_direction_input = new JTextField(light.spotDirectionToString());
			spot_direction_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(spot_direction_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Light ambient (r,g,b,a): ");
			paramBox.add(paramLabel);

			light_ambient_input = new JTextField(light.lightAmbientToString());
			light_ambient_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(light_ambient_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Light diffuse (r,g,b,a): ");
			paramBox.add(paramLabel);

			light_diffuse_input = new JTextField(light.lightDiffuseToString());
			light_diffuse_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(light_diffuse_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Light specular (r,g,b,a): ");
			paramBox.add(paramLabel);

			light_specular_input = new JTextField(light.lightSpecularToString());
			light_specular_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(light_specular_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box buttonsBox = Box.createHorizontalBox();
			JButton okButton = new JButton("OK");
			okButton.setActionCommand("ok");
			okButton.addActionListener(this);
			okButton.setPreferredSize(new Dimension(100, 23));
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setActionCommand("cancel");
			cancelButton.addActionListener(this);
			cancelButton.setPreferredSize(new Dimension(100, 23));
			buttonsBox.add(Box.createHorizontalGlue());
			buttonsBox.add(okButton);
			buttonsBox.add(Box.createRigidArea(new Dimension(5, 5)));
			buttonsBox.add(cancelButton);
			buttonsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPane.add(buttonsBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			try {
				String[] light_positionArray = light_position_input.getText()
						.split(",");
				float[] light_position = new float[]{
						Float.parseFloat(light_positionArray[0]),
						Float.parseFloat(light_positionArray[1]),
						Float.parseFloat(light_positionArray[2]),
						Float.parseFloat(light_positionArray[3])};

				String[] light_spotArray = spot_direction_input.getText()
						.split(",");
				float[] spot_direction = new float[]{
						Float.parseFloat(light_spotArray[0]),
						Float.parseFloat(light_spotArray[1]),
						Float.parseFloat(light_spotArray[2])};

				String[] light_ambientArray = light_ambient_input.getText()
						.split(",");
				float[] light_ambient = new float[]{
						Float.parseFloat(light_ambientArray[0]),
						Float.parseFloat(light_ambientArray[1]),
						Float.parseFloat(light_ambientArray[2]),
						Float.parseFloat(light_ambientArray[3])};

				String[] light_diffuseArray = light_diffuse_input.getText()
						.split(",");
				float[] light_diffuse = new float[]{
						Float.parseFloat(light_diffuseArray[0]),
						Float.parseFloat(light_diffuseArray[1]),
						Float.parseFloat(light_diffuseArray[2]),
						Float.parseFloat(light_diffuseArray[3])};

				String[] light_specularArray = light_specular_input.getText()
						.split(",");
				float[] light_specular = new float[]{
						Float.parseFloat(light_specularArray[0]),
						Float.parseFloat(light_specularArray[1]),
						Float.parseFloat(light_specularArray[2]),
						Float.parseFloat(light_specularArray[3])};

				light.light_position = light_position;
				light.spot_direction = spot_direction;
				light.light_ambient = light_ambient;
				light.light_diffuse = light_diffuse;
				light.light_specular = light_specular;

				Main.getInstance().forceRedrawAll = true;

				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Wrong values!",
						"Light properties", JOptionPane.INFORMATION_MESSAGE,
						null);
			}
		} else if (e.getActionCommand().equals("cancel")) {
			dispose();
		}
	}
}
