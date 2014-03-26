/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.BaseLabel;



public class MenuDemo implements ActionListener, ItemListener {
	static JFrame frame = new JFrame("MenuDemo");
	JPanel contentPane;
	JTextArea output;
    JScrollPane scrollPane;
    String newline = "\n";
    
    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the first menu.
        menu = new JMenu("Menu");
        menu.setMnemonic(KeyEvent.VK_A);
        menu.getAccessibleContext().setAccessibleDescription(
                "The only menu let user choose between Transactions and Order book");
        menuBar.add(menu);

        //a group of JMenuItems
        menuItem = new JMenuItem("Transactions",
                                 KeyEvent.VK_T);
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
                "This choose Transactions graph");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menuItem = new JMenuItem("Order Book",
                KeyEvent.VK_T);
		//menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"This choose Order Book graph");
		menuItem.addActionListener(this);
		menu.add(menuItem);


        //Build second menu in the menu bar.
        menu = new JMenu("Actions");
        menu.setMnemonic(KeyEvent.VK_N);
        menu.getAccessibleContext().setAccessibleDescription(
                "This menu does refresh");
        menuBar.add(menu);

        //add a MenuItem to second menu
        menuItem = new JMenuItem("Refresh",
                KeyEvent.VK_T);
        //menuItem.setMnemonic(KeyEvent.VK_T); //used constructor instead
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
        		KeyEvent.VK_3, ActionEvent.ALT_MASK));
        menuItem.getAccessibleContext().setAccessibleDescription(
        		"This choose refresh graph");
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        return menuBar;
    }

    public Container createContentPane() {
        //Create the content-pane-to-be.
        contentPane = new JPanel(new BorderLayout());
        contentPane.setOpaque(true);

        //Create a scrolled text area.
        output = new JTextArea(1, 10);
        output.setEditable(false);
        scrollPane = new JScrollPane(output);

        //Add the text area to the content pane.
        contentPane.add(scrollPane, BorderLayout.EAST);

        return contentPane;
    }

    public void actionPerformed(ActionEvent e) {
    	
        JMenuItem source = (JMenuItem)(e.getSource());
        String selectedItem = source.getText();
        if(selectedItem == "Transactions"){
        	output.append(selectedItem + newline);
        	try {
				Vector<Vector<Double>> array = TransactionAPI.HttpGetTransactions();
				int n = array.get(0).size();
                double[] x = new double[n];
                double[] y = new double[n];
                for(int i=0; i<n; i++){
                	x[i]  = array.get(0).get(i);
                	y[i]  = array.get(1).get(i);;
                }
                
				Plot2DPanel plot = new Plot2DPanel();
                plot.addLinePlot("my plot", x, y);
                // add a title
                BaseLabel title = new BaseLabel("Transactions", Color.RED, 0.5, 1.1);
                title.setFont(new Font("Courier", Font.BOLD, 20));
                plot.addPlotable(title);
                // set axis label
                plot.setAxisLabel(0, "time");
                plot.setAxisLabel(1, "price");
                
                
                // clear previews display, update with new display
                int componentCount = contentPane.getComponentCount();
                if(componentCount > 1){
                	contentPane.remove(1);
                }
                contentPane.add(plot, BorderLayout.CENTER);
                contentPane.revalidate(); 	
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
        }
        else if(selectedItem == "Order Book"){
        	output.append(selectedItem + newline);
        	try {
				Vector<Vector<Double>> array = OrderBookAPI.HttpGetOrderBook();
				int n = array.get(0).size();
                double[] x = new double[n];
                double[] y = new double[n];
                for(int i=0; i<n; i++){
                	x[i]  = array.get(0).get(i);
                	y[i]  = array.get(1).get(i);;
                }
                
				Plot2DPanel plot = new Plot2DPanel();
                plot.addLinePlot("my plot", x, y);
                // add a title
                BaseLabel title = new BaseLabel("Order Book", Color.RED, 0.5, 1.1);
                title.setFont(new Font("Courier", Font.BOLD, 20));
                plot.addPlotable(title);
                // set axis label
                plot.setAxisLabel(0, "price");
                plot.setAxisLabel(1, "total buy");
                
                // clear previews display, update with new display
                int componentCount = contentPane.getComponentCount();
                if(componentCount > 1){
                	contentPane.remove(1);
                }
                contentPane.add(plot, BorderLayout.CENTER);
                contentPane.revalidate();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        }
        else if(selectedItem == "Refresh"){
        	output.append(selectedItem + newline);
        }
        else{
        	output.append("selection exception?" + newline);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        JMenuItem source = (JMenuItem)(e.getSource());
        String s = "Item event detected."
                   + newline
                   + "    Event source: " + source.getText()
                   + " (an instance of " + getClassName(source) + ")"
                   + newline
                   + "    New state: "
                   + ((e.getStateChange() == ItemEvent.SELECTED) ?
                     "selected":"unselected");
        output.append(s + newline);
        output.setCaretPosition(output.getDocument().getLength());
    }
    
    // Returns just the class name -- no package info.
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex+1);
    }


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        //frame = ;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        MenuDemo demo = new MenuDemo();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());

        //Display the window.
        frame.setSize(750, 460);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}