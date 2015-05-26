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
import javax.swing.BoxLayout;
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
import org.andresoviedo.app.modelviewer3D.objects.Object3D;


public class JDialogObject extends JDialog implements ActionListener {

	private Object3D obj;
	private JCheckBox isVisibleCB;
	private JTextField locationInput;
	private JTextField scaleInput;
	private JTextField rotationInput;

	private JTextField ambient_input;
	private JTextField diffuse_input;
	private JTextField specular_input;
	private JTextField shininess_input;
	private JTextField emission_input;

	private JCheckBox drawSolidCB;
	private JCheckBox enableLightsCB;
	private JCheckBox enableTexturesCB;

	private JCheckBox ani_enabledCB;
	private JTextField ani_rotation_input;
	private JTextField ani_translation_input;

	public JDialogObject(Object3D obj) {
		super(Main.getInstance(), "Propiedades del objecto");
		this.obj = obj;
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
		JLabel desc = new JLabel("Object: " + obj.toString());
		contentPane.add(desc, new GridBagConstraints(0, gridY++, 1, 1, 1.0D,
				0D, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		BoundingBox boundingBox = obj.getBoundingBox();
		if (boundingBox != null) {
			{
				Box paramBox = Box.createHorizontalBox();
				JLabel paramLabel = new JLabel("Object center: ");
				paramBox.add(paramLabel);

				JTextField infoField = new JTextField(boundingBox
						.centerToString());
				infoField.setEditable(false);
				infoField.setPreferredSize(new Dimension(100, 23));
				paramBox.add(infoField);
				contentPane.add(paramBox, new GridBagConstraints(0, gridY++, 1,
						1, 1.0D, 0D, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
			}
			{
				Box paramBox = Box.createHorizontalBox();
				JLabel paramLabel = new JLabel("Limits size (x,y,z): ");
				paramBox.add(paramLabel);

				JTextField infoField = new JTextField(boundingBox
						.limitsToString());
				infoField.setEditable(false);
				infoField.setPreferredSize(new Dimension(100, 23));
				paramBox.add(infoField);
				contentPane.add(paramBox, new GridBagConstraints(0, gridY++, 1,
						1, 1.0D, 0D, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
			}
			{
				Box paramBox = Box.createHorizontalBox();
				JLabel paramLabel = new JLabel("Current size (x,y,z): ");
				paramBox.add(paramLabel);

				JTextField infoField = new JTextField(boundingBox
						.sizeToString());
				infoField.setEditable(false);
				infoField.setPreferredSize(new Dimension(100, 23));
				paramBox.add(infoField);
				contentPane.add(paramBox, new GridBagConstraints(0, gridY++, 1,
						1, 1.0D, 0D, GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5),
						0, 0));
			}
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Location (x,y,z): ");
			paramBox.add(paramLabel);

			locationInput = new JTextField(obj.transX + "," + obj.transY + ","
					+ obj.transZ);
			locationInput.setPreferredSize(new Dimension(100, 23));
			paramBox.add(locationInput);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Scale (x,y,z): ");
			paramBox.add(paramLabel);

			scaleInput = new JTextField(obj.getScale()[0] + ","
					+ obj.getScale()[1] + "," + obj.getScale()[2]);
			scaleInput.setPreferredSize(new Dimension(100, 23));
			paramBox.add(scaleInput);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Rotation (x,y,z): ");
			paramBox.add(paramLabel);

			rotationInput = new JTextField(obj.rotX + "," + obj.rotY + ","
					+ obj.rotZ);
			rotationInput.setPreferredSize(new Dimension(100, 23));
			paramBox.add(rotationInput);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Auto-rotation (x,y,z): ");
			paramBox.add(paramLabel);

			ani_rotation_input = new JTextField(obj.ani_rotation[0] + ","
					+ obj.ani_rotation[1] + "," + obj.ani_rotation[2]);
			ani_rotation_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(ani_rotation_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel(
					"Auto-translation (relative to 0,0) (x,y,z): ");
			paramBox.add(paramLabel);

			ani_translation_input = new JTextField(obj.ani_translation[0] + ","
					+ obj.ani_translation[1] + "," + obj.ani_translation[2]);
			ani_translation_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(ani_translation_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Material ambient (r,g,b,a): ");
			paramBox.add(paramLabel);

			ambient_input = new JTextField(obj.mat_ambient[0] + ","
					+ obj.mat_ambient[1] + "," + obj.mat_ambient[2] + ","
					+ obj.mat_ambient[3]);
			ambient_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(ambient_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Material diffuse (r,g,b,a): ");
			paramBox.add(paramLabel);

			diffuse_input = new JTextField(obj.mat_diffuse[0] + ","
					+ obj.mat_diffuse[1] + "," + obj.mat_diffuse[2] + ","
					+ obj.mat_diffuse[3]);
			diffuse_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(diffuse_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Material specular (r,g,b,a): ");
			paramBox.add(paramLabel);

			specular_input = new JTextField(obj.mat_specular[0] + ","
					+ obj.mat_specular[1] + "," + obj.mat_specular[2] + ","
					+ obj.mat_specular[3]);
			specular_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(specular_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Material shininess (0.0-128.0): ");
			paramBox.add(paramLabel);

			shininess_input = new JTextField(String
					.valueOf(obj.mat_shininess[0]));
			shininess_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(shininess_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}
		{
			Box paramBox = Box.createHorizontalBox();
			JLabel paramLabel = new JLabel("Material emission (r,g,b,a): ");
			paramBox.add(paramLabel);

			emission_input = new JTextField(obj.mat_emission[0] + ","
					+ obj.mat_emission[1] + "," + obj.mat_emission[2] + ","
					+ obj.mat_emission[3]);
			emission_input.setPreferredSize(new Dimension(100, 23));
			paramBox.add(emission_input);

			contentPane.add(paramBox,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}

		drawSolidCB = new JCheckBox("Draw solid", obj.drawSolid);
		drawSolidCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(drawSolidCB, new GridBagConstraints(0, gridY++, 1, 1,
				1.0D, 0D, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		enableLightsCB = new JCheckBox("Enable lights", obj.enableLighting);
		enableLightsCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(enableLightsCB, new GridBagConstraints(0, gridY++, 1,
				1, 1.0D, 0D, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		enableTexturesCB = new JCheckBox("Enable textures", obj.enableTextures);
		enableTexturesCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(enableTexturesCB, new GridBagConstraints(0, gridY++, 1,
				1, 1.0D, 0D, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		isVisibleCB = new JCheckBox("Visible", obj.isVisible);
		isVisibleCB.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(isVisibleCB, new GridBagConstraints(0, gridY++, 1, 1,
				1.0D, 0D, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

		{
			// Animation
			ani_enabledCB = new JCheckBox("Enable auto-transformations",
					obj.ani_enabled);
			ani_enabledCB.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPane.add(ani_enabledCB,
					new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
							GridBagConstraints.CENTER,
							GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5,
									5), 0, 0));
		}

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
		contentPane.add(buttonsBox, new GridBagConstraints(0, gridY++, 1, 1,
				1.0D, 0D, GridBagConstraints.CENTER,
				GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			try {
				String[] locationInputArray = locationInput.getText()
						.split(",");
				float locX = Float.parseFloat(locationInputArray[0]);
				float locY = Float.parseFloat(locationInputArray[1]);
				float locZ = Float.parseFloat(locationInputArray[2]);

				String[] scaleInputArray = scaleInput.getText().split(",");
				float scaleX = Float.parseFloat(scaleInputArray[0]);
				float scaleY = Float.parseFloat(scaleInputArray[1]);
				float scaleZ = Float.parseFloat(scaleInputArray[2]);

				String[] rotationInputArray = rotationInput.getText()
						.split(",");
				float rotX = Float.parseFloat(rotationInputArray[0]);
				float rotY = Float.parseFloat(rotationInputArray[1]);
				float rotZ = Float.parseFloat(rotationInputArray[2]);

				String[] ambient_Array = ambient_input.getText().split(",");
				float[] mat_ambient = new float[4];
				mat_ambient[0] = Float.parseFloat(ambient_Array[0]);
				mat_ambient[1] = Float.parseFloat(ambient_Array[1]);
				mat_ambient[2] = Float.parseFloat(ambient_Array[2]);
				mat_ambient[3] = Float.parseFloat(ambient_Array[3]);

				String[] diffuse_Array = diffuse_input.getText().split(",");
				float[] mat_diffuse = new float[4];
				mat_diffuse[0] = Float.parseFloat(diffuse_Array[0]);
				mat_diffuse[1] = Float.parseFloat(diffuse_Array[1]);
				mat_diffuse[2] = Float.parseFloat(diffuse_Array[2]);
				mat_diffuse[3] = Float.parseFloat(diffuse_Array[3]);

				String[] specular_Array = specular_input.getText().split(",");
				float[] mat_specular = new float[4];
				mat_specular[0] = Float.parseFloat(specular_Array[0]);
				mat_specular[1] = Float.parseFloat(specular_Array[1]);
				mat_specular[2] = Float.parseFloat(specular_Array[2]);
				mat_specular[3] = Float.parseFloat(specular_Array[3]);

				String[] ani_rotationInputArray = ani_rotation_input.getText()
						.split(",");
				float[] ani_rotation = new float[3];
				ani_rotation[0] = Float.parseFloat(ani_rotationInputArray[0]);
				ani_rotation[1] = Float.parseFloat(ani_rotationInputArray[1]);
				ani_rotation[2] = Float.parseFloat(ani_rotationInputArray[2]);

				String[] ani_translationInputArray = ani_translation_input
						.getText().split(",");
				float[] ani_translation = new float[3];
				ani_translation[0] = Float
						.parseFloat(ani_translationInputArray[0]);
				ani_translation[1] = Float
						.parseFloat(ani_translationInputArray[1]);
				ani_translation[2] = Float
						.parseFloat(ani_translationInputArray[2]);

				float[] snininess = new float[]{Float
						.parseFloat(shininess_input.getText())};

				String[] emission_Array = emission_input.getText().split(",");
				float[] mat_emission = new float[4];
				mat_emission[0] = Float.parseFloat(emission_Array[0]);
				mat_emission[1] = Float.parseFloat(emission_Array[1]);
				mat_emission[2] = Float.parseFloat(emission_Array[2]);
				mat_emission[3] = Float.parseFloat(emission_Array[3]);

				obj.transX = locX;
				obj.transY = locY;
				obj.transZ = locZ;

				obj.setScale(new float[]{scaleX, scaleY, scaleZ});

				obj.rotX = rotX;
				obj.rotY = rotY;
				obj.rotZ = rotZ;

				obj.ani_rotation = ani_rotation;
				obj.ani_translation = ani_translation;

				obj.mat_ambient = mat_ambient;
				obj.mat_diffuse = mat_diffuse;
				obj.mat_specular = mat_specular;
				obj.mat_shininess = snininess;
				obj.mat_emission = mat_emission;

				obj.drawSolid = drawSolidCB.isSelected();
				obj.enableLighting = enableLightsCB.isSelected();
				obj.enableTextures = enableTexturesCB.isSelected();
				obj.isVisible = isVisibleCB.isSelected();

				obj.ani_enabled = ani_enabledCB.isSelected();

				// JOptionPane.showMessageDialog(this,
				// "Object update succesfully!", "Object properties",
				// JOptionPane.INFORMATION_MESSAGE, null);

				Main.getInstance().objectUpdated(obj);

				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Wrong values!",
						"Object properties", JOptionPane.INFORMATION_MESSAGE,
						null);
			}
		} else if (e.getActionCommand().equals("cancel")) {
			dispose();
		}
	}
}
