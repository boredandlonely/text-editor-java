package main;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class texteditor extends JFrame implements ActionListener {
JTextArea editor;
JScrollPane scrollBar; //scroll bar
JLabel label;
JSpinner fontSet;

JButton fontBtn;

JButton backgroundColor;

JComboBox fontOption;

JMenuBar menuBar;
JMenu menu;

Calendar calendar;
String getTime;
JLabel clock;
SimpleDateFormat time;

private Map<String, JMenuItem> menuItemMap;


texteditor(){
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setTitle("dollar tree text editor");
    this.setSize(720,540);
    this.setLayout(new FlowLayout());
    this.setLocationRelativeTo(null);

    editor = new JTextArea();

    editor.setLineWrap(true);

    //scroll bar feature
    scrollBar = new JScrollPane(editor);
    scrollBar.setPreferredSize(new Dimension(680,500));
    scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


    fontBtn = new JButton("Text Color");
    fontBtn.addActionListener(this);


    backgroundColor = new JButton("Background Color");
    backgroundColor.addActionListener(this);


// sets font size
    label = new JLabel("Font size: ");
    fontSet = new JSpinner();
    fontSet.setPreferredSize(new Dimension(50,25));
    fontSet.setValue(12);
    fontSet.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            editor.setFont(new Font(editor.getFont().getFamily(),Font.PLAIN,(int)fontSet.getValue()));
        }
    });

    // change the font
    String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    fontOption = new JComboBox(fonts);
    fontOption.addActionListener(this);
    fontOption.setSelectedItem("Consolas");

    // this next section is specifically focused on the menu

    menuItemMap = new HashMap<>();

    // menu
    menuBar = new JMenuBar();
    menu = new JMenu("File");


// will add the menu items we need
    String[] menuNames = {"New","Open","Save","Exit",};
    for(String names: menuNames){
        JMenuItem item = new JMenuItem(names);
        item.addActionListener(this);
        menu.add(item);
        menuItemMap.put(names,item);

    }

    menuBar.add(menu);

// clock set up
    time = new SimpleDateFormat("hh:mm a");
    getTime = time.format(Calendar.getInstance().getTime());
    clock = new JLabel( getTime);

    this.setJMenuBar(menuBar);
    this.add(backgroundColor);
    this.add(fontBtn);
    this.add(label);
    this.add(fontSet);
    this.add(fontOption);
    this.add(clock);
    this.add(scrollBar);
    this.setVisible(true);


}
private void exitFile(){
    System.exit(0);
}
private void newFile(){

    editor.setText("");
}
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fontBtn){
            JColorChooser btnColor = new JColorChooser();

            Color color = btnColor.showDialog(null,"Choose font color",Color.black);
            editor.setForeground(color);

        }
        if(e.getSource() == backgroundColor){
            JColorChooser bgColor = new JColorChooser();
            Color background = bgColor.showDialog(null, "Choose background color",Color.white);
            editor.setBackground(background);
        }
        if(e.getSource() == fontOption){

            editor.setFont(new Font((String)fontOption.getSelectedItem(),Font.PLAIN,editor.getFont().getSize()));

        }
        JMenuItem saved = null;
        if (menuItemMap != null) {
            saved = menuItemMap.get("Save");
        }
        if(saved != null && e.getSource() == saved){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            int num = fileChooser.showSaveDialog(null) ;
            if(num == 0){
                File file;
                PrintWriter write = null;
                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    write = new PrintWriter(file);
                    write.println(editor.getText());
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    write.close();
                }
            }

        }

        JMenuItem exit = null;
        if(menuItemMap !=null){
            exit = menuItemMap.get("Exit");
        }
        if(exit != null && e.getSource() ==exit){
            exitFile();
        }


        JMenuItem open = null;
        if (menuItemMap != null) {
            open = menuItemMap.get("Open");
        }
        if(open != null && e.getSource() == open){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("text","txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == 0){

                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner in = null;
                try {
                    in = new Scanner(file);
                    if(file.isFile()){
                        while(in.hasNextLine()){
                            String ln = in.nextLine()+ "\n";
                            editor.append(ln);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                finally {
                    in.close();
                }
            }

        }

        JMenuItem makeNewFile = null;
        if(menuItemMap !=null){
            makeNewFile = menuItemMap.get("New");
        }
        if(makeNewFile != null && e.getSource() == makeNewFile){

            newFile();
        }




    }
}
