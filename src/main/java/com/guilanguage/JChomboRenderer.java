package com.guilanguage;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

@SuppressWarnings("serial")

public class JChomboRenderer extends JLabel implements ListCellRenderer<Object> {

	private ImageIcon[] items;

	public JChomboRenderer(ImageIcon[] items) {

		setOpaque(true);

		this.items = items;

	}

	@Override

	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {

		int selectedIndex = ((Integer) value).intValue();

		if (isSelected) {

			setBackground(list.getSelectionBackground());

			setForeground(list.getSelectionForeground());

		}

		else {

			setBackground(list.getBackground());

			setForeground(list.getForeground());

		}

		ImageIcon icon = this.items[selectedIndex];

		setIcon(icon);

		return this;

	}

}
