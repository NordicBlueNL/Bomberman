package Bomberman;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import javax.swing.*;

//Errordialog als er een fout in het spel optreed. 
public class ErrorDialog
{

	@SuppressWarnings("deprecation")
	public ErrorDialog(Exception exception)
	{
		CharArrayWriter chararraywriter = new CharArrayWriter();
		exception.printStackTrace(new PrintWriter(chararraywriter, true));
		String s = new String(" " + chararraywriter.toString() + "\n" + " CLICK OK TO TERMINATE THE PROGRAM.");
		JTextArea jtextarea = new JTextArea(s);
		jtextarea.setEditable(false);
		JScrollPane jscrollpane = new JScrollPane(jtextarea);
		JOptionPane joptionpane = new JOptionPane(jscrollpane);
		joptionpane.setOptionType(-1);
		joptionpane.setMessageType(0);
		javax.swing.JDialog jdialog = joptionpane.createDialog(null, "Exception Caught");
		jdialog.setResizable(false);
		jdialog.show();
		System.exit(-1);
	}


	public ErrorDialog(Exception exception, boolean flag)
	{
		CharArrayWriter chararraywriter = new CharArrayWriter();
		exception.printStackTrace(new PrintWriter(chararraywriter, true));
		String s = new String(" " + chararraywriter.toString());
		JTextArea jtextarea = new JTextArea(s);
		jtextarea.setEditable(false);
		JScrollPane jscrollpane = new JScrollPane(jtextarea);
		JOptionPane joptionpane = new JOptionPane(jscrollpane);
		joptionpane.setOptionType(-1);
		joptionpane.setMessageType(0);
		javax.swing.JDialog jdialog = joptionpane.createDialog(null, "Exception Caught");
		jdialog.setResizable(false);
		jdialog.show();
		if(flag)
		{
			System.exit(-1);
		}
	}
}
