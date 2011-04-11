package Bomberman;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import javax.swing.*;
import javax.swing.text.JTextComponent;

//Klasse BomberConfigDialog heeft een Actionlistener en erft van JDialog., in menu ControlSetup 
public class BomberConfigDialog extends JDialog
    implements ActionListener
{
    private class GetKeyDialog extends JDialog
    {
        private JDialog me;
//getKeyDialog houd bij welke keys gebruikt worden op heden. 
        public GetKeyDialog(JDialog jdialog, String s, boolean flag)
        {
            me = null;
            setTitle(s);
            setModal(flag);
            me = this;
            addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent keyevent)
                {
                    if(waitingForKey)
                    {
                        int k = keysBeingSet[0];
                        int l = keysBeingSet[1];
                        int i1 = keyevent.getKeyCode();
                        boolean flag1 = false;
                        for(int j1 = 0; j1 < 4; j1++)
                        {
                            for(int k1 = 0; k1 < 5; k1++)
                            {
                                if(keys[j1][k1] == i1 && (j1 != k || l != k1))
                                {
                                    flag1 = true;
                                }
                                if(flag1)
                                {
                                    break;
                                }
                            }

                            if(flag1)
                            {
                                break;
                            }
                        }

                        if(!flag1)
                        {
                            keys[k][l] = i1;
                            keyFields[k][l].setText(KeyEvent.getKeyText(keys[k][l]));
                            waitingForKey = false;
                            dispose();
                        } else
                        {
                            JOptionPane joptionpane = new JOptionPane("Key: [" + KeyEvent.getKeyText(i1) + "] is al in gebruik, gebruik een andere toets.");
                            joptionpane.setOptionType(-1);
                            joptionpane.setMessageType(0);
                            JDialog jdialog1 = joptionpane.createDialog(me, "Error");
                            jdialog1.setResizable(false);
                            jdialog1.show();
                        }
                    }
                }

            });
            setResizable(false);
            setSize(300, 0);
            int i = jdialog.getLocation().x + (jdialog.getSize().width - getSize().width) / 2;
            int j = jdialog.getLocation().y + (jdialog.getSize().width - getSize().height) / 2;
            setLocation(i, j);
            show();
        }
    }


    private int keys[][];
    private int keysBeingSet[] = {
        -1, -1
    };
    private boolean waitingForKey;
    private JButton buttons[][];
    private JTextField keyFields[][];
    
//JFrame Keys
    public BomberConfigDialog(JFrame jframe)
    {
        super(jframe, "Bomberman Toetsen", true);
        keys = null;
        waitingForKey = false;
        buttons = null;
        keyFields = null;
        keys = new int[4][5];
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                keys[i][j] = BomberKeyConfig.toetsen[i][j];
            }

        }

        JPanel jpanel = new JPanel(new GridLayout(2, 2));
        JPanel ajpanel[] = new JPanel[4];
        keyFields = new JTextField[4][];
        buttons = new JButton[4][5];
        for(int k = 0; k < 4; k++)
        {
            keyFields[k] = new JTextField[5];
            setupPanel(k, jpanel, ajpanel[k], keyFields[k]);
        }

        JPanel jpanel1 = new JPanel(new FlowLayout(1));
        jpanel1.setBorder(BorderFactory.createEtchedBorder());
        jpanel1.add(new JLabel("Klik op button voor een toetsverandering.", 0));
        getContentPane().add(jpanel1, "North");
        getContentPane().add(jpanel, "Center");
        JPanel jpanel2 = new JPanel(new FlowLayout(1));
        jpanel2.setBorder(BorderFactory.createEtchedBorder());
        JButton jbutton = new JButton("Save");
        jbutton.addActionListener(this);
        jpanel2.add(jbutton);
        JButton jbutton1 = new JButton("Close");
        jbutton1.addActionListener(this);
        jpanel2.add(jbutton1);
        getContentPane().add(jpanel2, "South");
        setResizable(false);
        pack();
        int l = jframe.getLocation().x + (jframe.getSize().width - getSize().width) / 2;
        int i1 = jframe.getLocation().y + (jframe.getSize().height - getSize().height) / 2;
        setLocation(l, i1);
        show();
    }
//SetupPanel
    private void setupPanel(int i, JPanel jpanel, JPanel jpanel1, JTextField ajtextfield[])
    {
        JPanel jpanel2 = new JPanel(new GridLayout(5, 1));
        JPanel jpanel3 = new JPanel(new GridLayout(5, 1));
        for(int j = 0; j < 5; j++)
        {
            buttons[i][j] = new JButton();
            ajtextfield[j] = new JTextField(10);
            switch(j)
            {
            case 0: // '\0'
                buttons[i][j].setText("Omhoog");
                break;

            case 1: // '\001'
                buttons[i][j].setText("Beneden");
                break;

            case 2: // '\002'
                buttons[i][j].setText("Links");
                break;

            case 3: // '\003'
                buttons[i][j].setText("Rechts");
                break;

            case 4: // '\004'
                buttons[i][j].setText("BOM");
                break;
            }
            buttons[i][j].addActionListener(this);
            ajtextfield[j].setText(KeyEvent.getKeyText(keys[i][j]));
            ajtextfield[j].setEditable(false);
            jpanel2.add(buttons[i][j]);
            jpanel3.add(ajtextfield[j]);
        }

        jpanel1 = new JPanel(new GridLayout(1, 2));
        jpanel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Player " + (i + 1) + " ToetsCombinatie"));
        jpanel1.add(jpanel2);
        jpanel1.add(jpanel3);
        jpanel.add(jpanel1);
    }
//ActionEvent om gelijk te krijgen wat je wilt. beschikking van de gegevens die je wilt. 
    public void actionPerformed(ActionEvent actionevent)
    {
        if(actionevent.getActionCommand().equals("Save"))
        {
            for(int i = 0; i < 4; i++)
            {
                for(int k = 0; k < 5; k++)
                {
                    BomberKeyConfig.toetsen[i][k] = keys[i][k];
                }

            }

            BomberKeyConfig.schrijfBestand();
            dispose();
        } else
        if(actionevent.getActionCommand().equals("Close"))
        {
            dispose();
        } else
        {
            int j = 0;
            int l = 0;
            boolean flag = false;
            for(j = 0; j < 4; j++)
            {
                for(l = 0; l < 5; l++)
                {
                    if(actionevent.getSource().equals(buttons[j][l]))
                    {
                        flag = true;
                    }
                    if(flag)
                    {
                        break;
                    }
                }

                if(flag)
                {
                    break;
                }
            }

            keysBeingSet[0] = j;
            keysBeingSet[1] = l;
            waitingForKey = true;
            new GetKeyDialog(this, "Druk je gewenste toets in.....", true);
        }
    }





}
