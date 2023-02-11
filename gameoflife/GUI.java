package gameoflife;

import java.awt.*;          // needed to implement Color
import java.awt.event.*;    // needed to implement ActionListener
import javax.swing.*;       // needed to implement JFrame, JPanel, JLabel, JButton, Timer

class GUI implements ActionListener {   // this class must have actionPerformed method because of implement
    //initialization
    //objects in window
    JFrame frame;
    JPanel contentPane;

    JLabel label1;
    JLabel label2;
    JButton button1;
    JButton button2;
    JButton exitbutton;

    //declare game variables
    Color border = new Color (202, 202, 202);
    public static int gridsize = 75;    // needs to be accessed in Main class
    JButton board[] [] = new JButton [gridsize] [gridsize];     //2d array, 8x8, board shown as buttons
    int tilevalue[] [] = new int [gridsize] [gridsize];     //is cell "dead or alive"
    int ntilevalue[] [] = new int [gridsize] [gridsize];        //to store values for next day

    boolean rungame = false;
    int gen = 0, life = 0;
    int neighbours = 0;

    int movefromy, movefromx;
    int movetoy, movetox;
    
    Timer timer1 = new Timer (1, this);      //1000ms = 1s

    public void setup() {
        //create GUI layout
        frame = new JFrame ("Game of Life");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

        /* Create a content pane */
        contentPane = new JPanel ();

        /* Add content pane to frame */
        frame.setContentPane (contentPane);

        //no format, use absolute positioning to set bounds
        contentPane.setLayout (null);

        //output grid
        int xpos = 0, ypos = 0, square = 9;

        for (int i = 0 ; i < gridsize ; i++)
        {
            for (int j = 0 ; j < gridsize ; j++)
            {
            board [i] [j] = new JButton (); //'y', 'x' axis
            board [i] [j].addActionListener (this);
            board [i] [j].setBounds (xpos, ypos, square, square);
            board [i] [j].setBackground (Color.gray);
            board [i] [j].setBorder (BorderFactory.createLineBorder (border));
            contentPane.add (board [i] [j]);
            tilevalue [i] [j] = 0; //"dead" cell
            xpos += 9;
            }

            xpos = 0;
            ypos += 9;
        }

        // remember , to format text within a JLABEL use HTML coding
        //create output label
        /*output = new JLabel (); //"<html><font face=Arial size=9 color = blue >Poker!</font>"
        output.setHorizontalTextPosition (JLabel.CENTER);
        output.setBounds (240, 400, 110, 50); //xpos, ypos, xlen, ylen
        contentPane.add (output); */

        // create button1 (Toggle Run button)
        button1 = new JButton ("Toggle Run");
        button1.addActionListener (this);
        button1.setBounds (290, 675, 100, 25);
        button1.setBackground (Color.blue);
        contentPane.add (button1);

        // create label1 (day counter)
        label1 = new JLabel ("Gen: " + gen);
        label1.setBounds (620, 675, 60, 25);
        contentPane.add (label1);
        
        // create label2 (life counter)
        label2 = new JLabel ("Live Cells: " + life);
        label2.setBounds (64, 675, 120, 25);
        contentPane.add (label2);

        // create exitbutton
        exitbutton = new JButton ("Quit");
        exitbutton.addActionListener (this);
        exitbutton.setBounds (0, 675, 60, 25);
        contentPane.add (exitbutton);

        // Size and then display the window
        frame.setSize (692, 750); 
        frame.setLocation (600, -5);
        frame.setVisible (true);

    }

    public void actionPerformed (ActionEvent event) {
	//Check which button is clicked
	    //String eventName = event.getActionCommand ();     // using source instead

        if (event.getSource () == button1)      //Toggle run by using timer
        {
            if (rungame == false) 
            {
                timer1.start();
                rungame = true;
            } 
            else
            {
                rungame = false;
                timer1.stop();
            }
        }
        
        //Run 1 interation of game every timer fire
        if (event.getSource () == timer1) 
        {
            Main.updateGame ();
        }

        //if (event.getSource () == button2)
        //{
        //Main.reset()
        //}

        //For manual clicks to change life status
        for (int i = 0 ; i < gridsize ; i++)
        {
            for (int j = 0 ; j < gridsize ; j++)
            {
                if (event.getSource () == board [i] [j])
                {
                    if (tilevalue [i] [j] == 0)     //if "dead" make alive on click
                    {
                        board [i] [j].setBackground (Color.yellow);
                        tilevalue [i] [j] = 1;
                        life++;
                    }
                    else    //if "alive " make dead on click
                    {
                        board [i] [j].setBackground (Color.gray);
                        tilevalue [i] [j] = 0;
                        life--;
                    }

                    label2.setText ("Live Cells: " + life);
                }
            }
        }

        if (event.getSource () == exitbutton)
        {
            System.exit (0);
        } 
    }


    public int numNeighbours(int i, int j) {
        neighbours = 0;
        
        for (int k = (i - 1) ; k <= (i + 1) ; k++)      //y pos, +/- 1
        {
            for (int l = (j - 1) ; l <= (j + 1) ; l++)      //x pos, +/- 1
            {
                if ((k >= 0 && l >= 0) && (k < gridsize && l < gridsize))       //deal with out of index errors
                {
                    if ((k != i) || (l != j))       //don't compare to itself
                    {
                        //System.out.println (l + " " + k);
                        if (tilevalue [k] [l] == 1)     //if neighbouring cell is alive
                            {
                                neighbours++;
                            }
                    }
                }
            }
        }
        return neighbours;
    } 

	public void lifeUpdate(int i, int j, int neighbours) {
        if (tilevalue [i] [j] == 1)     //if cell was alive
        {
            if (neighbours == 2 || neighbours == 3)
            {
                ntilevalue [i] [j] = 1;     //stay alive
            }
            else
            {
                ntilevalue [i] [j] = 0;     //dies
            }
        }
        else    //if cell was dead
        {
            if (neighbours == 3)    //become alive
            {
                ntilevalue [i] [j] = 1;
            }
            else    //stay dead
            {
                ntilevalue [i] [j] = 0;
            }
        }
    }
    
    public void newGenOut() {
        //output new generation based on 2nd array
        life = 0;
        
        for (int i = 0 ; i < gridsize ; i++)
        {
            for (int j = 0 ; j < gridsize ; j++)
            {
                if (ntilevalue [i] [j] == 1) //if cell should be alive
                {
                    tilevalue [i] [j] = 1;  //update current cell value, "ntilevalue" will be overwritten so no need to reset
                    board [i] [j].setBackground (Color.yellow);
                    life++;
                }
                else //if cell should be dead
                {
                    tilevalue [i] [j] = 0;
                    board [i] [j].setBackground (Color.gray);
                }
            }
        }

        gen++;
        label1.setText ("Gen: " + gen);
        label2.setText ("Live Cells: " + life);
    }

}   //end of class GUI body
