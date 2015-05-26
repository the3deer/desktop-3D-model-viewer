package org.andresoviedo.app.modelviewer3D.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.andresoviedo.app.modelviewer3D.Main;
import org.andresoviedo.app.modelviewer3D.loaders.obj.OBJLoaderGL.OBJModel;
import org.andresoviedo.app.modelviewer3D.loaders.obj.tegr.Tegr3dObjectLoader;
import org.andresoviedo.app.modelviewer3D.objects.Object3D;


public class Object3DLoadDialog extends JDialog implements ActionListener {

	JTextField fileInput;
	JTextField textureFileInput;
	// JTextField firstVertexInput;
	JCheckBox invertNormalsCB;

	File objectFile;
	File textureFile;

	public Object3DLoadDialog(Frame owner) {
		super(owner, "Object load");
		init();
		pack();
		setLocationRelativeTo(owner);
	}

	private void init() {
		// Set layout
		Container contentPane = getContentPane();
		BoxLayout boxLayout = new BoxLayout(contentPane, BoxLayout.Y_AXIS);
		setLayout(boxLayout);

		{
			Box fileInputBox = Box.createHorizontalBox();
			// Title
			JLabel desc = new JLabel("Object:");
			desc.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPane.add(desc);
			fileInput = new JTextField();
			fileInput.setPreferredSize(new Dimension(200, 23));
			JButton fileButton = new JButton("...");
			fileButton.setActionCommand("showFileDialog");
			fileButton.addActionListener(this);
			fileInputBox.add(desc);
			fileInputBox.add(fileInput);
			fileInputBox.add(fileButton);
			fileInputBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPane.add(fileInputBox);
		}
		{
			Box fileInputBox = Box.createHorizontalBox();
			// Title
			JLabel desc = new JLabel("Texture:");
			desc.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPane.add(desc);
			textureFileInput = new JTextField();
			textureFileInput.setPreferredSize(new Dimension(200, 23));
			JButton fileButton = new JButton("...");
			fileButton.setActionCommand("showTextureFileDialog");
			fileButton.addActionListener(this);
			fileInputBox.add(desc);
			fileInputBox.add(textureFileInput);
			fileInputBox.add(fileButton);
			fileInputBox.setAlignmentX(Component.LEFT_ALIGNMENT);
			contentPane.add(fileInputBox);
		}

		// Box vertexBox = Box.createHorizontalBox();
		// JLabel scaleLabel = new JLabel("First vertex:");
		// firstVertexInput = new JTextField("0");
		// firstVertexInput.setMaximumSize(new Dimension(50, 23));
		// vertexBox.add(scaleLabel);
		// vertexBox.add(firstVertexInput);
		// vertexBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		// contentPane.add(vertexBox);
		{
			JLabel scaleLabel = new JLabel("First vertex:");
			invertNormalsCB = new JCheckBox("Inverted normals");
			contentPane.add(invertNormalsCB);
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
		contentPane.add(buttonsBox);
	}

	private File showObjectFileDialog() {
		JFileChooser fc = new JFileChooser(".\\models");
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".obj");
			}

			public String getDescription() {
				return "Ficheros Object (*.obj)";
			}

		});
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".3d");
			}

			public String getDescription() {
				return "Ficheros 3D (*.3d)";
			}
		});

		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		return null;
	}

	private File showTextureFileDialog() {
		JFileChooser fc = new JFileChooser(".\\textures");
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".gif") || f.getName().toLowerCase().endsWith(".bmp")
						|| f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".png");
			}

			public String getDescription() {
				return "Ficheros de textura (*.bmp, *.gif, *.jpg)";
			}

		});
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return fc.getSelectedFile();
		}
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("showFileDialog")) {
			objectFile = showObjectFileDialog();
			if (objectFile != null) {
				fileInput.setText(objectFile.getAbsolutePath());
			}
		} else if (actionCommand.equals("showTextureFileDialog")) {
			textureFile = showTextureFileDialog();
			if (textureFile != null) {
				textureFileInput.setText(textureFile.getAbsolutePath());
			}
		} else if (actionCommand.equals("ok")) {
			try {
				// int firstVertex =
				// Integer.parseInt(firstVertexInput.getText());
				Object3D obj = null;

				if (objectFile.getName().toLowerCase().endsWith(".3d")) {
					Tegr3dObjectLoader tegrObj = new Tegr3dObjectLoader(objectFile);
					tegrObj.textureFile = textureFile;
					// tegrObj.offset = -firstVertex;
					tegrObj.invertedNormals = invertNormalsCB.isSelected();
					tegrObj.load();
					obj = tegrObj;
				} else if (objectFile.getName().toLowerCase().endsWith(".obj")) {
					OBJModel objModel = new OBJModel(objectFile.getAbsolutePath());
					objModel.loadModel(true);
					obj = objModel;

				} else {
					throw new Exception("Unsupported file format");
				}
				Main.getInstance().loadObject(obj);
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Corrupt file!", "Object load", JOptionPane.INFORMATION_MESSAGE, null);
			}
		} else if (actionCommand.equals("cancel")) {
			dispose();
		}
	}
}
