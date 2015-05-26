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
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import org.andresoviedo.app.modelviewer3D.Main;
import org.andresoviedo.app.modelviewer3D.objects.Object3D;


public class JPanelObjects extends JPanel
		implements
			ActionListener,
			MouseListener {

	private Box itemsBox = Box.createVerticalBox();

	// private List<Item> items = new Vector<Item>();
	private List<JCheckBox> cbs = new Vector<JCheckBox>();
	private List<Object3D> items = new Vector<Object3D>();

	// private PopupListener popupListener = new PopupListener();
	// private JPopupMenu popup;

	public JPanelObjects() {
		super();
		setLayout(new GridBagLayout());
		// setBorder(BorderFactory.createLineBorder(Color.BLACK));

		// Box title = Box.createHorizontalBox();
		// title.setAlignmentX(Box.CENTER_ALIGNMENT);
		// JLabel lab = new JLabel("Objetos 3D");
		// lab.setBackground(Color.BLUE);
		// lab.setHorizontalAlignment(SwingConstants.CENTER);
		// title.add(lab);

		int gridY = 0;
		JLabel title = new JLabel("Objetos 3D");
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(title, new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 0D,
				GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
				new Insets(5, 5, 5, 5), 0, 0));

		// itemsBox.setAlignmentX(Component.LEFT_ALIGNMENT);
		JScrollPane scrollPane = new JScrollPane(itemsBox,

		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPane, new GridBagConstraints(0, gridY++, 1, 1, 1.0D, 1D,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(
						0, 0, 0, 0), 0, 0));

		// add(new JLabel("end"));
	}
	public void reload() {
		List<Object3D> objs = Main.getInstance().getObjects3D();
		items.clear();
		cbs.clear();
		itemsBox.removeAll();
		for (Iterator<Object3D> it = objs.iterator(); it.hasNext();) {
			itemsBox.add(createItemRenderer(it.next()));
		}
		itemsBox.revalidate();
	}

	public void update() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < cbs.size(); i++) {
					JCheckBox cb = cbs.get(i);
					cb.setSelected(items.get(i).isVisible);
				}
			}
		});
	}
	private JComponent createItemRenderer(Object3D obj) {
		JCheckBox checkbox = new JCheckBox();
		checkbox.setSelected(obj.isVisible);
		checkbox.setActionCommand(String.valueOf(items.size()));
		checkbox.addActionListener(this);

		JLabel description = new JLabel("<html><u>" + obj.toString()
				+ "</u></html>");
		description.setForeground(Color.BLUE);
		description.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		description.setLabelFor(checkbox);
		description.addMouseListener(this);

		Box box = Box.createHorizontalBox();
		box.add(checkbox);
		box.add(description);

		// Item item = new Item(obj, checkbox);
		items.add(obj);
		cbs.add(checkbox);
		return box;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JCheckBox) {
			JCheckBox checkbox = (JCheckBox) e.getSource();
			int index = Integer.parseInt(e.getActionCommand());
			items.get(index).isVisible = checkbox.isSelected();
			Main.getInstance().objectUpdated(items.get(index));
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getSource() instanceof JLabel) {
			JCheckBox checkbox = ((JCheckBox) ((JLabel) e.getSource())
					.getLabelFor());
			int index = Integer.parseInt(checkbox.getActionCommand());
			System.out.println(checkbox.isSelected());
			JDialogObject object = new JDialogObject(items.get(index));
			object.setVisible(true);
		}

	}
	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	// class PopupListener extends MouseAdapter {
	//
	// private PopupObjectListener popupObjectListener = new
	// PopupObjectListener();
	//
	// private JPopupMenu getPopupMenu() {
	// if (popup == null) {
	// // Create the popup menu.
	// popup = new JPopupMenu();
	// JMenuItem menuItem = new JMenuItem("A popup menu item");
	// menuItem.addActionListener(popupObjectListener);
	// popup.add(menuItem);
	// menuItem = new JMenuItem("Another popup menu item");
	// menuItem.addActionListener(popupObjectListener);
	// popup.add(menuItem);
	// }
	// return popup;
	// }
	//
	// public void mousePressed(MouseEvent e) {
	// maybeShowPopup(e);
	// }
	//
	// public void mouseReleased(MouseEvent e) {
	// maybeShowPopup(e);
	// }
	//
	// private void maybeShowPopup(MouseEvent e) {
	// if (e.isPopupTrigger()) {
	// getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
	// }
	// }
	// }
	//
	// class PopupObjectListener implements ActionListener {
	// public void actionPerformed(ActionEvent e) {
	// }
	// }
}

// class Item {
// Item(Tegr3dObjectLoader obj, JCheckBox checkbox) {
//
// }
//
// }
