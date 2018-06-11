package org.eye.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class WindowUtil {
	public static Color NICE_COLOR = new Color(211, 211, 211);// new Color(181,
																// 229, 181);
	public static void setWindowPos(Container c, boolean isMain) {

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		if (isMain) {
			Dimension MainSize = new Dimension(dim.width * 2 / 3,
					dim.height * 2 / 3);
			c.setSize(MainSize);
		}
		int w = c.getSize().width;
		int h = c.getSize().height;

		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;

		c.setBounds(x, y, w, h);
	}

	public static void setWindowIcon(Container c,String file){
		if( c instanceof JFrame ){
			JFrame frame=(JFrame )c;
			String icon = "/images/eyelogo.png";
	        if( file!=null )
	        	icon=file;
	        frame.setIconImage(Toolkit.getDefaultToolkit()
	                .getImage(ClassLoader.class.getResource(icon)));
		}

	}

}
