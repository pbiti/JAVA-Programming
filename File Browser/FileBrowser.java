/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package ce326.hw3;

/**
 *
 * @author ksenia
 */
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Files;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.io.File;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class FileBrowser extends JFrame{
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 800;
    JTextField text;
    JPopupMenu popupmn, favespopup;
    JButton searchbutton;
    JPanel filepan, faves, pan1, pan2;
    File CopiedFile, CutFile, SelectedFromFaves;  
    int CutorCopy=0, index, bbsize=0;
    JList<File> listf = new JList<>();
    JList<File> Jfavorites = new JList<>();    
    ArrayList<File> list = new ArrayList<>();
    ArrayList<File> faveslist = new ArrayList<>();    
    File[] listedFiles, listedFaves;
    AttachedFile ClickedItem;
    FileListener mouseListener = new FileListener();
    MenuItemsListener menuListener = new MenuItemsListener();
    SearchButtonListener searchlistener = new SearchButtonListener();
    String path = "";
    String os = System.getProperty("os.name");
    
    public FileBrowser(){
        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("File Browser");
        this.setJMenuBar(createMenu());
        PopUpMenu();
        FavesPopUp();
                
        faves = new JPanel();     //create panel to display files in current directory
        faves.setPreferredSize(new Dimension(500, 550));
        faves.setBackground(Color.PINK);
        faves.setVisible(true);
        
        filepan = new JPanel();  //create panel to display Favorite Directories
        filepan.setPreferredSize(new Dimension(800, 700));
        filepan.setBackground(Color.WHITE);
        filepan.addMouseListener(mouseListener);
        filepan.setVisible(true);
                
        text = new JTextField(40);  //search bar and button
        searchbutton = new JButton("Search");
        searchbutton.addActionListener(searchlistener);
        searchbutton.setActionCommand("search");
        text.setVisible(false);
        searchbutton.setVisible(false);
    
        path = System.getProperty("user.home");

        pan1 = new JPanel(); //search bar panel
        pan2 = new JPanel(); //breadcrumb panel
        pan1.setLayout(new BoxLayout(pan1, BoxLayout.PAGE_AXIS));   
        pan2.setLayout(new FlowLayout());
        pan1.add(text);
        pan1.add(searchbutton);
       
        ChangeBreadcrumb(System.getProperty("user.home"));
                
        faveslist.add(new File(System.getProperty("user.home"))); //faveslist saves all directories added to favorites
        listedFaves = new File[faveslist.size()];   //put all faveslist files in listedFaves array
            for(int k=0; k<faveslist.size(); k++){
                listedFaves[k] = faveslist.get(k);
            }
        Jfavorites = new JList<File>(listedFaves); //put all favorite directories into a Jlist so they can be displayed
        Jfavorites.setVisibleRowCount(20);
        Jfavorites.addMouseListener(new favesListener());
        faves.add(Jfavorites);
        if(os.contains("Windows")){
            if(new File(System.getProperty("user.home") + "\\java-file-browser"+"\\properties.xml").exists()){
                ReadXML(System.getProperty("user.home") + "\\java-file-browser"+"\\properties.xml");
            } //read xml file that holds all the favorite files so they can be displayed 
        }else{
            if(new File(System.getProperty("user.home") + "/java-file-browser"+"/properties.xml").exists()){
                ReadXML(System.getProperty("user.home") + "/java-file-browser"+"/properties.xml");
            } 
        }
        JScrollPane FavesScroll = new JScrollPane(faves);
        FavesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        FavesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JScrollPane FilesScroll = new JScrollPane(filepan);
        FilesScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        FilesScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, FavesScroll, FilesScroll);
        //splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(500);
        
        JScrollPane bcrumbscroll = new JScrollPane(pan2);
        bcrumbscroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        bcrumbscroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        
        
        this.add(pan1, BorderLayout.NORTH);
        this.add(splitPane, BorderLayout.CENTER);
        this.add(bcrumbscroll, BorderLayout.SOUTH);
        this.pack();
        
        listedFiles = listFiles(System.getProperty("user.home")); //list all files in user's home directory        
    }
    
    private void ChangeBreadcrumb(String NewDest){
        for(int i=0; i<NewDest.length(); i++){  //The breadcrumb is created with JButtons placed next to each other
            if(os.contains("Windows")){
                if(NewDest.charAt(i)=='\\'){        //split the path into file names and create a new JButton for each file       
                    JButton button = new JButton(NewDest.substring(bbsize, i) + " >");
                    button.addActionListener(new BreadcrumbListener());
                    pan2.add(button);               
                    bbsize = i+1;
                }  
            }else{
                if(NewDest.charAt(i)=='/'){    //split the path into file names and create a new JButton for each file       
                    JButton button = new JButton(NewDest.substring(bbsize, i) + " >");
                    button.addActionListener(new BreadcrumbListener());
                    pan2.add(button);               
                    bbsize = i+1;
                } 
            }
        }
        JButton button = new JButton(NewDest.substring(bbsize, 
            NewDest.length()));
        pan2.add(button);
    }
    class BreadcrumbListener implements ActionListener{
        public void actionPerformed(ActionEvent ae){
            String fileclicked = ae.getActionCommand().substring(0,ae.getActionCommand().length()-1).replaceAll(" ", "");
            
            if(os.contains("Windows")){
                if(fileclicked.equals("C:")){ 
                    path = "C:\\";                           
                }else{
                    String getfile = "\\"+fileclicked+"\\";  //path holds the current directory            
                    String filedest = path.substring(0, path.indexOf(getfile)); //split path to change to the directory pointed to
                    filedest = filedest + "\\"+ fileclicked;                    //based on the JButton pressed
                    path = filedest;
                }
            }else{
                if(fileclicked.equals("")){path = "/";}
                else{
                    String getfile = "/"+fileclicked+"/";
                    String filedest = path.substring(0, path.indexOf(getfile));
                    filedest = filedest + "/"+ fileclicked;
                    path = filedest;
                }
            }
            filepan.removeAll();
            listedFiles = listFiles(path);
            filepan.revalidate();
            filepan.repaint();
            
            pan2.removeAll();
            bbsize=0;
            ChangeBreadcrumb(path); //add the new bradcrumb based on the directory change
            pan2.revalidate();
            pan2.repaint();            
        }    
    }
    
    public void PopUpMenu(){
        popupmn = new JPopupMenu();
        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(menuListener);
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(menuListener);
        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(menuListener);
        JMenuItem renameItem = new JMenuItem("Rename");
        renameItem.addActionListener(menuListener);
        JMenuItem delItem = new JMenuItem("Delete");
        delItem.addActionListener(menuListener);
        JMenuItem favItem = new JMenuItem("Add To Favourites");
        favItem.addActionListener(menuListener);
        JMenuItem propItem = new JMenuItem("Properties");
        propItem.addActionListener(menuListener);
        popupmn.add(cutItem);
        popupmn.add(copyItem);
        popupmn.add(pasteItem);
        popupmn.add(renameItem);
        popupmn.add(delItem);
        popupmn.add(favItem);
        popupmn.add(propItem);
    }
    
    public void FavesPopUp(){
        favespopup = new JPopupMenu();
        JMenuItem delItem = new JMenuItem("Delete from Favorites");
        delItem.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                faveslist.remove(SelectedFromFaves); //delete selected file from the list
                listedFaves = new File[faveslist.size()]; //so that the favorites panel can be updated 
                for(int k=0; k<faveslist.size(); k++){    //to the new data
                    listedFaves[k] = faveslist.get(k);
                }
                Jfavorites = new JList<File>(listedFaves);
                Jfavorites.setVisibleRowCount(20);
                Jfavorites.addMouseListener(new favesListener());
                faves.removeAll();
                faves.add(Jfavorites); 
                faves.revalidate();
                faves.repaint();
                if(os.contains("Windows"))
                    DelFromXML(System.getProperty("user.home") + "\\java-file-browser"+"\\properties.xml");//remove directory from the xml file
                else{DelFromXML(System.getProperty("user.home") + "/java-file-browser"+"/properties.xml");}
            }           
        });
        favespopup.add(delItem);
    }
    
    JMenuBar createMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenu edit = new JMenu("Edit");
        JMenu search = new JMenu("Search");
        search.addMenuListener(new MenuListener(){
            public void menuSelected(MenuEvent e) {                
                if(text.isVisible()){ //if the Menu is enabled and search is pressed hide or show search bar
                    text.setVisible(false); 
                    searchbutton.setVisible(false);
                    searchbutton.getParent().revalidate();
                    text.getParent().revalidate();
                }else{
                    text.setVisible(true);
                    searchbutton.setVisible(true);
                    text.getParent().revalidate();
                    searchbutton.getParent().revalidate();
                }
            }
            public void menuDeselected(MenuEvent e) {}
            public void menuCanceled(MenuEvent e) {}        
        });

        menubar.add(file);
        menubar.add(edit);
        menubar.add(search);

        JMenuItem newBrowser = new JMenuItem("New Window");
        newBrowser.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                main(null);
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){               
                dispose();
            }
        });
        file.add(newBrowser);
        file.add(exit);
        //Edit
        JMenuItem cut = new JMenuItem("Cut");
        cut.addActionListener(menuListener);
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(menuListener);
        JMenuItem paste = new JMenuItem("Paste");
        paste.addActionListener(menuListener);
        JMenuItem rename = new JMenuItem("Rename");
        rename.addActionListener(menuListener);
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(menuListener);
        JMenuItem faves = new JMenuItem("Add to Favourites");
        faves.addActionListener(menuListener);
        JMenuItem properties = new JMenuItem("Properties");
        properties.addActionListener(menuListener);
        edit.add(cut);
        edit.add(copy);
        edit.add(paste);
        edit.add(rename);
        edit.add(delete);
        edit.add(faves);
        edit.add(properties);
        
        return menubar;
    }
    public File[] FilesContent(File fnode) {
        File file = fnode;
        File[] files = file.listFiles();
        ArrayList<File> list = new ArrayList<>();
        if (files == null) {
          return null;
        }
        for (File tempFile : files) {
          if (!tempFile.isHidden() && !(tempFile.getName().charAt(0) == '.')) {
            list.add(tempFile); //add all files in current directory to a list
          }
        }
        Collections.sort(   //sort them so that directories are shown first and all files are in alphabetical order
            list,
            new Comparator<File>() {
              public int compare(File f1, File f2) {
                if (f1.isDirectory() && !f2.isDirectory()) {
                  return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                  return 1;
                } else {
                  return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
                }
              }
            });
        for (File tempFile : list) {
          createLabel(tempFile); //attach each file to a label/icon
        } 
        return files;
    }
    public void createLabel(File tempFile) {
        ImageIcon icon = createIcon(tempFile);
        AttachedFile label = new AttachedFile(tempFile.getName(), icon, JLabel.CENTER, tempFile); //Object AttachedFile is used to attach every file
        label.addMouseListener(mouseListener);                                                    //to it's icon and label
        label.setVerticalTextPosition(JLabel.BOTTOM);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setPreferredSize(new Dimension(100, 100));
        label.revalidate();
        filepan.add(label);
    }
    
    public ImageIcon createIcon(File tempFile) {
        
        if(tempFile.getName().endsWith(".txt")) {
            if(os.contains("Windows")){return new ImageIcon("./icons/txt.png");}
            else{return new ImageIcon(".\\icons\\txt.png");}
        }
        if(tempFile.getName().endsWith(".pdf")) {
           return new ImageIcon("./icons/pdf.png");
        }
        if(tempFile.getName().endsWith(".jpg")
             || tempFile.getName().endsWith(".bmp")
             || tempFile.getName().endsWith(".png")
             || tempFile.getName().endsWith(".giff")
             || tempFile.getName().endsWith(".jpeg")) {
           return new ImageIcon("./icons/image.png");
        }
        if(tempFile.getName().endsWith(".mp3")
             || tempFile.getName().endsWith(".ogg")
             || tempFile.getName().endsWith(".wav")) {
           return new ImageIcon("./icons/audio.png");
        }
        if(tempFile.getName().endsWith(".html")
             || tempFile.getName().endsWith(".htm")) {
           return new ImageIcon("./icons/html.png");
        }
        if(tempFile.getName().endsWith(".zip")
             || tempFile.getName().endsWith(".tgz")
             || tempFile.getName().endsWith(".tar")
             || tempFile.getName().endsWith(".gz")
             || tempFile.getName().endsWith(".rar")) {
           return new ImageIcon("./icons/zip.png");
        }
        if(tempFile.getName().endsWith(".xml")) {
           return new ImageIcon("./icons/xml.png");
        }
        if(tempFile.getName().endsWith(".xlsx")
             || tempFile.getName().endsWith(".ods")
             || tempFile.getName().endsWith(".xlx")) {
           return new ImageIcon(".icons/xlsx.png");
        }
        if(tempFile.getName().endsWith(".docx")
             || tempFile.getName().endsWith(".doc")
             || tempFile.getName().endsWith(".odt")) {
           return new ImageIcon("./icons/docx.png");
        }
        if(tempFile.getName().endsWith(".mp4")
             || tempFile.getName().endsWith(".avi")
             || tempFile.getName().endsWith(".wmv")){
           return new ImageIcon("./icons/video.png");
        }
        if (tempFile.isDirectory()) {
           return new ImageIcon("./icons/folder.png");
        }
        return new ImageIcon("./icons/question.png");    
    }
       
    File[] listFiles(String path) { //lists all files based on a given path
        File file = new File(path);
        return FilesContent(file);
    }
    
    private class FileListener implements MouseListener {
        public void mouseClicked(MouseEvent me) {
          if (me.getSource() instanceof JLabel) {
            if (me.getButton() == MouseEvent.BUTTON1) {
              int Count = me.getClickCount();
              if (Count == 1) {
                if (ClickedItem != null) { //There is a file already selected
                    ClickedItem.setBorder(BorderFactory.createEmptyBorder()); //get rid of its border
                  }
                  AttachedFile label = (AttachedFile) me.getSource();
                  label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));//put a border around the selected file
                  ClickedItem = label;
              } else if (Count == 2) {
                  AttachedFile label = (AttachedFile) me.getSource();
                  File file = label.file;
                  if(file.isDirectory()){ //change to the selected directory
                      filepan.removeAll();
                      listedFiles = FilesContent(file);
                      path =file.getAbsolutePath();
                      bbsize = 0;
                      pan2.removeAll();                     
                      ChangeBreadcrumb(file.getAbsolutePath());
                      pan2.revalidate();
                      pan2.repaint();
                                                    
                      filepan.revalidate();
                      filepan.repaint();
                  }else{ //execute the file
                      try {
                          Desktop.getDesktop().open( file );
                      }catch (IOException ex) {
                          System.out.println("File Execution Exception");
                          ex.printStackTrace();
                       }                  
                    }
                }
              }
           } 
        }
        public void mousePressed(MouseEvent me) {
          if(me.isPopupTrigger()){              
            if(me.getSource() instanceof JLabel){
                AttachedFile label = (AttachedFile) me.getSource();
                ClickedItem = label;
                popupmn.show(me.getComponent(), me.getX(), me.getY());
            }
          }
        }
        public void mouseReleased(MouseEvent me) {
            if(me.isPopupTrigger()){               
                if(me.getSource() instanceof JLabel){
                    AttachedFile label = (AttachedFile) me.getSource();
                    ClickedItem = label;
                    popupmn.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        }
        public void mouseExited(MouseEvent me) {}

        public void mouseEntered(MouseEvent me) {}
    }
    
    private void deleteDirectory(File file){
      if (file.isDirectory()) {
        for (File temp : file.listFiles()) {
            deleteDirectory(temp);
        }
      }
      file.delete();
    }
    
    private long DirSize(File file){
        long length = 0;
        if(file.listFiles()!=null){
            for(File tempfile : file.listFiles()){
                if(tempfile.isFile()){length += tempfile.length();}
                else{length += DirSize(tempfile);}
            }
        }
        return length;
    }
    
    private void CheckSearchCases(File file, String filetosearch,String filetype){ //adds all files that qualify to a list       
        int lastindex = file.getName().lastIndexOf(".");                           //so they can be displayed
        String fileExtent = file.getName().substring(lastindex+1);
        if(filetype!=null){                     
            if(filetype.toLowerCase().equals("dir") && file.isDirectory() && file.getName().toLowerCase().contains(filetosearch.toLowerCase())){               
                list.add(file);                            
            }
            else if(filetype.toLowerCase().equals(fileExtent) && file.getName().toLowerCase().contains(filetosearch.toLowerCase())){             
                list.add(file);               
            }
        }else{                
            if(file.getName().toLowerCase().contains(filetosearch.toLowerCase())){                 
                list.add(file);
            }
        }
    }
    
   private void CheckSubDirs(File file, String filetosearch, String filetype){
     
       if(file.canRead() && file.length()>0 && !file.isHidden() && file.listFiles()!=null){
           for(File temp : file.listFiles()){
               if(temp.isDirectory()){CheckSearchCases(temp, filetosearch,filetype);
               CheckSubDirs(temp, filetosearch, filetype);}
               else{CheckSearchCases(temp, filetosearch, filetype);}
           }
        }
    }
   
    class ListListener implements MouseListener{//listener for the search results list
        public void mouseClicked(MouseEvent e) {
            if( e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            int index = listf.locationToIndex(e.getPoint());           
            File f = listf.getModel().getElementAt(index);
            if(f.isDirectory()) {
              filepan.removeAll();
              listedFiles = FilesContent(f);
              path = f.getAbsolutePath();
              bbsize = 0;
              pan2.removeAll();             
              ChangeBreadcrumb(f.getAbsolutePath());
              pan2.revalidate();
              pan2.repaint();  
              filepan.revalidate();
              filepan.repaint();             
            }else {
              try {
                Desktop.getDesktop().open(f);
              }
              catch(IOException ex) {
                  ex.printStackTrace();
                return;
             }
           }
         }
       }
        public void mousePressed(MouseEvent me){}
        public void mouseReleased(MouseEvent me){}
        public void mouseExited(MouseEvent me){}
        public void mouseEntered(MouseEvent me){}
    }
    
    class favesListener implements MouseListener{ //listener for the favorites list
        public void mouseClicked(MouseEvent e) {
            if( e.getButton() == MouseEvent.BUTTON1 && (e.getClickCount() == 2 || e.getClickCount()==1)) {
                int index = Jfavorites.locationToIndex(e.getPoint());           
                File f = Jfavorites.getModel().getElementAt(index);
             
                filepan.removeAll();
                listedFiles = FilesContent(f);
                path = f.getAbsolutePath();
                bbsize = 0;
                pan2.removeAll();             
                ChangeBreadcrumb(f.getAbsolutePath());
                pan2.revalidate();
                pan2.repaint();  
                filepan.revalidate();
                filepan.repaint();                         
            }         
        }
        public void mousePressed(MouseEvent me){
            if(me.isPopupTrigger()){              
                if(me.getSource() instanceof JList){
                    index = Jfavorites.locationToIndex(me.getPoint());
                    
                    SelectedFromFaves = Jfavorites.getModel().getElementAt(index);
                    favespopup.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        }
        public void mouseReleased(MouseEvent me){
             if(me.isPopupTrigger()){              
                if(me.getSource() instanceof JList){
                    index = Jfavorites.locationToIndex(me.getPoint());                    
                    SelectedFromFaves = Jfavorites.getModel().getElementAt(index);
                    favespopup.show(me.getComponent(), me.getX(), me.getY());
                }
            }
        }
        public void mouseExited(MouseEvent me){}
        public void mouseEntered(MouseEvent me){}
    }
        
    class SearchButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){ //split input string to get attributes for the file search           
            list.clear();
            /*String[] fileinfo = text.getText().split(" ");
            String filetosearch = fileinfo[0]; */
            int fileinfoindex = text.getText().indexOf(" ");
            System.out.println("index: " + fileinfoindex);
            String[] fileinfo = new String[2];
            if(fileinfoindex!=-1){fileinfo[0] = text.getText().substring(0,fileinfoindex);
                fileinfo[1] = text.getText().substring(fileinfoindex+6,text.getText().length());
                System.out.println("type: " + fileinfo[1]);
                
            }
            else{fileinfo[0] = text.getText();}
            String filetosearch = fileinfo[0];
            System.out.println("file: " + filetosearch);
            //String[] filetype = null;
            String fileExtent = "";
            int i=0;
            
            /*if(fileinfo.length > 1){
               String filetypestr = fileinfo[1];
               filetype = filetypestr.split(":");               
            }*/
                                     
            for(File file : listedFiles){                                
                //if(fileinfo.length >1){CheckSearchCases(file, filetosearch,filetype[1]);}                
                //else{CheckSearchCases(file, filetosearch,null);}
                if(fileinfo[1]!=null){CheckSearchCases(file, filetosearch,fileinfo[1]);}
                else{CheckSearchCases(file, filetosearch,null);}
            }
                          
            for(File tempfile : listedFiles){
                if(tempfile.isDirectory()){
                    //if(fileinfo.length > 1){CheckSubDirs(tempfile, filetosearch, filetype[1]);}
                    //else{CheckSubDirs(tempfile, filetosearch, null);}
                    if(fileinfo[1]!=null){CheckSubDirs(tempfile, filetosearch, fileinfo[1]);}
                    else{CheckSubDirs(tempfile, filetosearch, null);}
                }               
            }
             
            if(!list.isEmpty()){ //display the results found using a Jlist
                filepan.removeAll();
                File[] results = new File[list.size()];
                for(int k=0; k<list.size(); k++){
                    results[k] = list.get(k);
                }
                listf = new JList<File>(results);
                listf.setVisibleRowCount(20);
                listf.addMouseListener(new ListListener());
                filepan.removeAll();
                filepan.add(listf);
                filepan.revalidate();
                filepan.repaint();                
            }
            else{
                JOptionPane.showMessageDialog(null, 
                        "There are no Results for your Search", 
                        "No Results", JOptionPane.ERROR_MESSAGE );
            }                
        }
    }
    
    private void CopyFilesToAnotherDir(File Old, File New, int whichaction, String path) throws IOException{
         if (Old.isDirectory()) 
        {
            if (!New.exists()){New.mkdir();}
         
            for (File file : Old.listFiles()) {
                File srcFile = new File(Old, file.getName());
                File destFile = new File(New, file.getName());
                CopyFilesToAnotherDir(srcFile, destFile, whichaction, path);
            }
        }
         else{pasteItem(Old,New.getAbsolutePath(),whichaction);}    
    }
    
    private void pasteItem(File cutorcopyfile, String dest, int whichaction) throws FileNotFoundException,IOException{
        int confirm=0;        
        if(!cutorcopyfile.isDirectory()){
            InputStream readingf=null;
            OutputStream writingf=null;
                      
            File NewFile = new File(dest);
            if(NewFile.exists()){confirm = JOptionPane.showConfirmDialog(null, "Replace File "+ 
                    NewFile.getName()+ "?");}
            if(confirm==JOptionPane.YES_OPTION){
                readingf = new FileInputStream(cutorcopyfile);
                writingf = new FileOutputStream(NewFile);
                byte[]data = new byte[1024];
                int amount;
                while((amount = readingf.read(data))>0){writingf.write(data,0,amount);}
                readingf.close();
                writingf.close();
                if(whichaction==1){
                    cutorcopyfile.delete();
                    filepan.revalidate();
                    filepan.repaint();
                }            
            }else{return;}
        }
        else{    
            File newDir = new File(dest);
            if(newDir.exists()){confirm = JOptionPane.showConfirmDialog(null, "Replace Directory "+ 
                       newDir.getName()+ "?");}
            if(confirm==JOptionPane.YES_OPTION){
                newDir.mkdir();

                CopyFilesToAnotherDir(cutorcopyfile, newDir, whichaction, dest);
                if(whichaction==1){deleteDirectory(cutorcopyfile);}
            }else{return;}
        }
    }
    
    private void DelFromXML(String xmlpath){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            org.w3c.dom.Document doc = db.parse(new FileInputStream(new File(xmlpath)));
            org.w3c.dom.Element element = (org.w3c.dom.Element) doc.getElementsByTagName("path").item(index-1);

            element.getParentNode().removeChild(element);
            doc.normalize();
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");

            tf.transform(new DOMSource(doc), new StreamResult(new File(xmlpath)));
           
        } catch(TransformerConfigurationException ex){ex.printStackTrace();}
          catch(TransformerException ex){ex.printStackTrace();}
          catch(ParserConfigurationException ex){ex.printStackTrace();}
          catch(FileNotFoundException ex){ex.printStackTrace();}
          catch(SAXException ex){ex.printStackTrace();}
          catch(IOException ex){ex.printStackTrace();}
    }
    
    private void ReadXML(String xmlpath){
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            
            DefaultHandler handler = new DefaultHandler() {
                boolean bpath = false;
                public void startElement(String uri, String localName,String qName, 
                Attributes attributes){
                    if (qName.equalsIgnoreCase("path")) {bpath = true;}
                }
                public void endElement(String uri, String localName,
		String qName){}
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (bpath) {
                        if(!faveslist.contains(new File(new String(ch, start, length)))){
                            faveslist.add(new File(new String(ch, start, length)));            
                        }
			bpath = false;
                    }
                }
            };
            saxParser.parse(xmlpath, handler);
            listedFaves = new File[faveslist.size()];
            for(int k=0; k<faveslist.size(); k++){
                listedFaves[k] = faveslist.get(k);               
            }
            Jfavorites = new JList<File>(listedFaves);
            Jfavorites.addMouseListener(new favesListener());
            faves.removeAll();
            faves.add(Jfavorites);
            faves.revalidate();
            faves.repaint();  
            
        }catch (ParserConfigurationException ex){ex.printStackTrace();}
         catch (SAXException ex){ex.printStackTrace();}
         catch (IOException ex){ex.printStackTrace();}
    }
    
    private void CreateXML(String xmlpath){
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.newDocument();
            org.w3c.dom.Element rootElement = doc.createElement("favorites");
            doc.appendChild(rootElement);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();           
            
            Transformer transformer = transformerFactory.newTransformer();
           
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(xmlpath));
            transformer.transform(source, result);
            System.out.println("File saved!");

        }catch (ParserConfigurationException ex) {
           ex.printStackTrace();
        }catch (TransformerConfigurationException ex) {
           ex.printStackTrace();
        }catch(TransformerException ex){
            ex.printStackTrace();
        }       
    }
    private void addtoXML(String dirtoadd, String xmlpath){
        try{
            File xmlFile = new File(xmlpath);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            org.w3c.dom.Document document = documentBuilder.parse(xmlFile);
            org.w3c.dom.Element documentElement = document.getDocumentElement();
            
            org.w3c.dom.Element path = document.createElement("path");
            path.setTextContent(dirtoadd);
            org.w3c.dom.Element nodeElement = document.createElement("Directories");
            nodeElement.appendChild(path);
            
            documentElement.appendChild(nodeElement);
            document.replaceChild(documentElement, documentElement);
            
            Transformer tFormer = TransformerFactory.newInstance().newTransformer();
            tFormer.setOutputProperty(OutputKeys.METHOD, "xml");
            Source source = new DOMSource(document);
            Result result = new StreamResult(xmlFile);
            tFormer.transform(source, result);
            
            
            faveslist.add(new File(dirtoadd));            
            listedFaves = new File[faveslist.size()];
            for(int k=0; k<faveslist.size(); k++){
                listedFaves[k] = faveslist.get(k);               
            }
            Jfavorites = new JList<File>(listedFaves);
            Jfavorites.addMouseListener(new favesListener());
            faves.removeAll();
            faves.add(Jfavorites);
            faves.revalidate();
            faves.repaint();
                        
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        }catch (TransformerConfigurationException ex){
            ex.printStackTrace();
        }catch (TransformerException ex){
            ex.printStackTrace();
        }
    }
    
    private void CheckifXMLexists(String currentpath, String filepath){
        String folderdest="";
        String filedest="";
        if(os.contains("Windows")){
             folderdest = System.getProperty("user.home") + "\\java-file-browser";
             filedest = folderdest + "\\properties.xml";
        }else{
             folderdest = System.getProperty("user.home") + "/java-file-browser";
             filedest = folderdest + "/properties.xml";
        }
        File xmlfolder = new File(folderdest);
        File newxml = new File(filedest);
                
        if(!xmlfolder.exists() && !newxml.exists()){
            xmlfolder.mkdir();            
            CreateXML(filedest);
            filepan.removeAll();
            listedFiles = listFiles(currentpath);
            filepan.revalidate();
            filepan.repaint();
        }
        else if(xmlfolder.exists() && !newxml.exists()){
            CreateXML(filedest);
            filepan.removeAll();
            listedFiles = listFiles(currentpath);
            filepan.revalidate();
            filepan.repaint();
        }
        if(!faveslist.contains(new File(filepath))){addtoXML(filepath, filedest);}
    }
      
    private class MenuItemsListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if (e.getActionCommand().equals("Cut")){              
                CutFile = ClickedItem.file;
                filepan.remove(ClickedItem);
                filepan.revalidate();
                filepan.repaint();                
                CutorCopy = 1;
            }
            if (e.getActionCommand().equals("Copy")){               
                CopiedFile = ClickedItem.file;
                CutorCopy = 2;
            }
            if (e.getActionCommand().equals("Paste")){
                if(!ClickedItem.file.isDirectory()){JOptionPane.showMessageDialog(null, 
                        "Files can only be pasted inside Directories", 
                        "Invalid Action", JOptionPane.ERROR_MESSAGE );}
                else{
                    if(CutorCopy == 1){
                        try {
                            if(os.contains("Windows"))
                                pasteItem(CutFile, ClickedItem.file.getAbsolutePath()+"\\"+CutFile.getName(), CutorCopy);
                            else{pasteItem(CutFile, ClickedItem.file.getAbsolutePath()+"/"+CutFile.getName(), CutorCopy);}
                        } catch (IOException ex) {
                            System.out.println("I/O exception occured while pasting cut file");
                            ex.printStackTrace();
                        }
                    }
                    else if(CutorCopy==2){                                  
                        try{
                            if(os.contains("Windows"))
                                pasteItem(CopiedFile, ClickedItem.file.getAbsolutePath()+"\\"+CopiedFile.getName(), CutorCopy);
                            else{pasteItem(CopiedFile, ClickedItem.file.getAbsolutePath()+"/"+CopiedFile.getName(), CutorCopy);}
                        } catch(IOException ex){
                            System.out.println("I/O exception occured while pasting copied file");
                            ex.printStackTrace();
                        }
                    }
                    filepan.revalidate();
                    filepan.repaint();
                }
            }
            if (e.getActionCommand().equals("Rename")){
                String newName = JOptionPane.showInputDialog(null, "Insert New Name:", "Rename File");
                String NewPath = ClickedItem.file.getAbsolutePath().substring(0, ClickedItem.file.getAbsolutePath().length()
                    - ClickedItem.file.getName().length());
                NewPath = NewPath + newName;
                
                if(newName!=null){                    
                    File renamefile = new File(NewPath);
                    if(ClickedItem.file.renameTo(renamefile)){
                        ClickedItem.setText(newName);
                        ClickedItem.file = renamefile;
                        filepan.revalidate();
                        filepan.repaint();
                    }
                }
            }
            if (e.getActionCommand().equals("Delete")){
                int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to Delete "+ 
                        ClickedItem.file.getName()+ "?");
                if(confirm == JOptionPane.YES_OPTION){
                    File file = ClickedItem.file;
                    if(file.isDirectory()){deleteDirectory(file);}
                    else{file.delete();}
                    filepan.remove(ClickedItem);
                    filepan.revalidate();
                    filepan.repaint();
                }
            }
            if (e.getActionCommand().equals("Add To Favourites")){
                if(!ClickedItem.file.isDirectory()){
                    JOptionPane.showMessageDialog(null, 
                        "Only Directories can be added to Favorites", 
                        "Invalid Action", JOptionPane.ERROR_MESSAGE );
                }else{
                    if(os.contains("Windows")){
                        CheckifXMLexists(ClickedItem.file.getAbsolutePath().substring(0, 
                            ClickedItem.file.getAbsolutePath().lastIndexOf("\\")),ClickedItem.file.getAbsolutePath());  
                    }else{
                        CheckifXMLexists(ClickedItem.file.getAbsolutePath().substring(0, 
                            ClickedItem.file.getAbsolutePath().lastIndexOf("/")),ClickedItem.file.getAbsolutePath()); 
                    }
                }
            }
            if (e.getActionCommand().equals("Properties")){
                long length = 0;
                String typeoffile = null;
                if(ClickedItem.file.isDirectory()){typeoffile = "Folder"; length = DirSize(ClickedItem.file);}
                else{typeoffile = "File"; length = ClickedItem.file.length();}
                
                String abspath;
                abspath = ClickedItem.file.getAbsolutePath();
                JCheckBox readbutton = new JCheckBox("Readable");
                JCheckBox writebutton = new JCheckBox("Writable");
                JCheckBox execbutton = new JCheckBox("Executable");
                if(ClickedItem.file.canRead()){readbutton.setSelected(true); 
                    if(ClickedItem.file.setReadable(false)){readbutton.setEnabled(true); ClickedItem.file.setReadable(true);}
                    else{readbutton.setEnabled(false);}
                }
                if(ClickedItem.file.canWrite()){writebutton.setSelected(true);
                    if(ClickedItem.file.setWritable(false)){writebutton.setEnabled(true); ClickedItem.file.setWritable(true);}
                    else{writebutton.setEnabled(false);}
                }
                if(ClickedItem.file.canExecute()){execbutton.setSelected(true);
                    if(ClickedItem.file.setExecutable(false)){execbutton.setEnabled(true);ClickedItem.file.setExecutable(true);}
                    else{execbutton.setEnabled(false);}
                }
           
                String msg = "File Name: " + ClickedItem.file.getName() 
                        +"\n"+"Absolute Path: "+abspath+"\n"+typeoffile+" Size: "
                        +length+" bytes\n"+"Permissions:\n";
                Object [] checkbuttons = {readbutton, writebutton, execbutton};
                JOptionPane.showOptionDialog(null,msg,"Properties",JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, checkbuttons, checkbuttons[0]);
                if(readbutton.isSelected()){ClickedItem.file.setReadable(true);}
                else{ClickedItem.file.setReadable(false);}
                if(writebutton.isSelected()){ClickedItem.file.setWritable(true);}
                else{ClickedItem.file.setWritable(false);}
                if(execbutton.isSelected()){ClickedItem.file.setExecutable(true);}
                else{ClickedItem.file.setExecutable(false);}
                filepan.revalidate();
                filepan.repaint();
            }
        }
    }
    
    private class AttachedFile extends JLabel{ //attach a file to its label and icon
        private File file;
        
        private AttachedFile(String text, Icon icon, int horizontalAlignment, File file) {
            super(text, icon, horizontalAlignment);
            this.file = file;
        }
    }
  
    public static void main(String[] args){
        FileBrowser fb = new FileBrowser();
        fb.setVisible(true);        
    }
}
