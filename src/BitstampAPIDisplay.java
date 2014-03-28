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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.Timer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class BitstampAPIDisplay implements ActionListener, ItemListener {
	static JFrame frame = new JFrame("Bitstamp Data");
	static JPanel contentPane;
	JTextArea output;
    JScrollPane scrollPane;
    String newline = "\n";
    static Timer t;
   //add action listener
    static ActionListener l1 = new ActionListener(){
    	public void actionPerformed(ActionEvent e) {
    		callTransaction();
    	}
	};
    static ActionListener l2 = new ActionListener(){
    	public void actionPerformed(ActionEvent e) {
    		callOrderBook();
    	}
	};
	static int delay = 5000;
    
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

        	if(t.isRunning()){
        		t.stop();
        	}
        	ActionListener[] test = t.getActionListeners();
            if(test!=null){
            	System.out.println("In Transaction, found actionListener!");
            	t.removeActionListener(test[0]);
            }
            t.addActionListener(l1);
        	t.setInitialDelay(0);
        	t.setDelay(delay);
    		t.start();
        }
        else if(selectedItem == "Order Book"){
        	output.append(selectedItem + newline);
        	
        	if(t.isRunning()){
        		t.stop();	
        	}
        	ActionListener[] test = t.getActionListeners();
            if(test!=null){
            	System.out.println("In Order Book, found actionListener!");
            	t.removeActionListener(test[0]);
            }
            t.addActionListener(l2);
    		t.setInitialDelay(0);
    		t.setDelay(delay);
    		t.start();
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

    
    private static void callTransaction(){
    	Vector<Vector<Double>> array;
		try {
			array = TransactionAPI.HttpGetTransactions();
			int n = array.get(0).size();
			TimeSeries s1 = new TimeSeries("Transactions");
			for(int i=0; i<n; i++){
				long dateLong = (long) (array.get(0).get(i)*1000);
		    	Date date = new Date(dateLong);
		    	Millisecond temp = new Millisecond(date);
		    	s1.addOrUpdate(temp, array.get(1).get(i));
	        }
			
			
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(s1);
			
			JFreeChart chart = ChartFactory.createTimeSeriesChart("Transactions", "time", "price", dataset);
			chart.setBackgroundPaint(Color.white);
			
			XYPlot plot = (XYPlot) chart.getPlot();
			plot.setBackgroundPaint(Color.lightGray);
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
			plot.setDomainCrosshairVisible(true);
			plot.setRangeCrosshairVisible(true);
			
			XYItemRenderer r = plot.getRenderer();
			if (r instanceof XYLineAndShapeRenderer) {
			    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
			    renderer.setBaseShapesVisible(true);
			    renderer.setBaseShapesFilled(true);
			    renderer.setDrawSeriesLineAsPath(true);
			}
			
			DateAxis axis = (DateAxis) plot.getDomainAxis();
			axis.setDateFormatOverride(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));

			
			ChartPanel panel = new ChartPanel(chart);
			panel.setFillZoomRectangle(true);
			panel.setMouseWheelEnabled(true);
	        // clear previews display, update with new display
	        int componentCount = contentPane.getComponentCount();
	        if(componentCount > 1){
	        	contentPane.remove(1);
	        }
	        contentPane.add(panel, BorderLayout.CENTER);
	        contentPane.revalidate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private static void callOrderBook(){
    	try {
			Vector<Vector<Double>> array = OrderBookAPI.HttpGetOrderBook();
			int n = array.get(0).size();
			XYSeriesCollection dataset = new XYSeriesCollection();
	        XYSeries data = new XYSeries("Bids");
			
            for(int i=0; i<n; i++){
            	data.add(array.get(0).get(i),array.get(1).get(i));
            }
            
            //
            int n1 = array.get(2).size();
	        XYSeries data1 = new XYSeries("Asks");
            for(int i=0; i<n1; i++){
            	data1.add(array.get(2).get(i),array.get(3).get(i));
            }
            //
            
            dataset.addSeries(data);
            dataset.addSeries(data1);
            JFreeChart chart = ChartFactory.createScatterPlot(
                    "Order Book",                  // chart title
                    "Price",                      // x axis label
                    "Value",                      // y axis label
                    dataset,                  // data
                    PlotOrientation.VERTICAL,
                    true,                     // include legend
                    true,                     // tooltips
                    false                     // urls
                );
            
            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
            renderer.setSeriesLinesVisible(0, true);
            plot.setRenderer(renderer);
            plot.setDomainCrosshairVisible(true);
			plot.setRangeCrosshairVisible(true);
            
			ChartPanel chartPanel = new ChartPanel(chart);
            // clear previews display, update with new display
            int componentCount = contentPane.getComponentCount();
            if(componentCount > 1){
            	contentPane.remove(1);
            }
            contentPane.add(chartPanel, BorderLayout.CENTER);
            contentPane.revalidate();
    	} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        BitstampAPIDisplay demo = new BitstampAPIDisplay();
        frame.setJMenuBar(demo.createMenuBar());
        frame.setContentPane(demo.createContentPane());

        //Display the window.
        frame.setSize(950, 560);
        frame.setVisible(true);
        
        t = new Timer(delay, l1);
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