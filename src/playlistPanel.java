import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class playlistPanel extends JPanel implements ActionListener{

    JButton playlistB, nextB, prevB, pausB, playB, exploreB;
    JLabel winTitle;

    public boolean playlistBIsPressed = false, show = false, doOnce = false, doAgain = false;
    public static boolean infoUpdate = false;
    Border border;
    String title = "";
    ThaTable table;
    MPlayer mp;
    JPanel tPanel, itemPanel;
    Items items;
    int input = 0;
    BufferedImage img1 = null, img2 = null;

    public String testTitle = " ";


    public playlistPanel(MPlayer p, Items items){
        
        setBackground(new Color(151, 79, 104));
        setLayout(null);

        mp = p;
        this.items = items;
        //addMouseListener(items);
        //addMouseMotionListener(items);
        //itemPanel = new JPanel();

        itemPanel();
        tablePanel();
        buttons();

        img2 = LoadImg("C:\\Users\\ELIMUS\\Desktop\\MBox_assets\\reworked\\BGS.jpg");


    }

    public void itemPanel(){

        itemPanel = new JPanel(){

            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                //System.out.println("xxx: " + playPanel.shortX);
                if(mp.getCurrentSong() != null){
                    if(mp.getCurrentSong().getTitle().length() > 8){
                        title = mp.getCurrentSong().getTitle().substring(0, 6) + "...";
                    }else
                        title = mp.getCurrentSong().getTitle();
                    g.setFont(new Font("dogica pixel", Font.BOLD, 8));
                    g.setColor(new Color(198, 241, 172));
                    g.drawString("playing: " + title, 16, 10);
                   // System.out.println("shoudl be: " + title);
                }
                items.drawSlider(g, 16, 40);
                items.drawMover(g, 40);
            }
        };
        itemPanel.addMouseListener(items);
        itemPanel.addMouseMotionListener(items);
        itemPanel.setLayout(null);
        itemPanel.setBackground(new Color(151, 79, 104));
        itemPanel.setSize(500, 100);
        itemPanel.setBounds(0, 300, 500, 100);
        add(itemPanel);
    }

    public void update(){
        
        //items.update();
     
        if(show){
            //use this to access the songs
            //0 is the top column
            //if in this panel play, and the row index is not -1 play the selected row
            //or set the index of the current song = to the..you get it
           // System.out.println("chcking for click");
            if(table.getTable().getSelectedRow() != -1){

                    int cellNum = table.getTable().getSelectedRow()+1;
                    mp.playNew(cellNum);
                    //table.getTable().getSelectionModel().addSelectionInterval(cellNum, cellNum);
                    table.getTable().getSelectionModel().clearSelection();
            }
            //System.out.println(table.getTable().getSelectedRow());
        }

    }

    public void refreshData(){
       // table.updateData(makeData());
        tPanel.removeAll();
        tablePanel();
        System.out.println("refreshed");
    }

    String[][] makeData(){
        String artistName = " ";
        String songTitle = " ";
        String songLength = " ";

        
        String[][] rowData = new String[mp.cSong.size()][3];

        for(int i=0; i<mp.cSong.size(); i++){

            artistName = mp.getcSong().get(i).getArtist();
            songTitle = mp.getcSong().get(i).getTitle(); 
            songLength = mp.getcSong().get(i).getSongLen();
            
            rowData[i][0] = artistName;
            rowData[i][1] = songTitle;
            rowData[i][2] = songLength;

        }

        //String[][] rowData = {{artistName, songTitle, songLength}};
      //  System.out.println(artistName);
        
        return rowData;
    }


    public void tablePanel(){
        tPanel = new JPanel();

        tPanel.setSize(485, 300);
        tPanel.setLayout(new BorderLayout());
        tPanel.setBackground(new Color(151, 79, 104));
        tPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 4));

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(151, 79, 104));
        titlePanel.setLayout(new FlowLayout());
        winTitle = new JLabel("PLAYLIST");
        winTitle.setFont(new Font("dogica pixel", Font.BOLD, 14));
        titlePanel.add(winTitle);
        tPanel.add(titlePanel, BorderLayout.NORTH);

        table = new ThaTable();

        if(mp.getCurrentSong() != null){
            img1 =  mp.getCurrentSong().getArtwork();
        }else
            img1 = img2;

        tPanel.add(table.tableInit(makeData()), BorderLayout.CENTER);


        add(tPanel);

        tPanel.revalidate();
        tPanel.repaint();

    }

    public void buttons(){

        this.playB = createButton("/assets/Play2.png", "/assets/rollPlay2.png", "play");
        playB.setBounds(250, 0, 32, 32);
        itemPanel.add(playB);

        this.pausB = createButton("/assets/Pause2.png", "/assets/rollPause2.png", "pause");
        pausB.setBounds(218, 0, 32, 32);
        itemPanel.add(pausB);

        //nextbutton
        this.nextB = createButton("/assets/next.png", "/assets/rollNext.png", "next");
        nextB.setBounds(292, 0, 64, 32);
        itemPanel.add(nextB);

        this.prevB = createButton("/assets/prev.png", "/assets/rollPrev.png", "prev");
        prevB.setBounds(144, 0, 64, 32);
        itemPanel.add(prevB);

        //playListButton
        this.playlistB = createButton("/assets/playlist.png", "/assets/rollPlaylist.png", "playlist");
        playlistB.setBounds(414, 0, 32, 32);
        itemPanel.add(playlistB);

        this.exploreB = createButton("/assets/add.png", "/assets/rollAdd.png", "add");
        exploreB.setBounds(446, 0, 32, 32);
        itemPanel.add(exploreB);
    }

    public JButton createButton(String iconPath, String rollPath, String toolTip){
        
        JButton b = new JButton(createIcon(iconPath));
        b.addActionListener(this);
         //make background transparent
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        //------------------------------//       
        b.setToolTipText(toolTip);
        b.setRolloverIcon(createIcon(rollPath));
        add(b);

        return b;
    }

    private ImageIcon createIcon(String path){

        URL img = getClass().getResource(path);
        //ImageIcon icon= new ImageIcon(img);
         if (img == null) {
        System.err.println("Image not found: " + path);
        } else {
        System.out.println("Image found at: " + img.toExternalForm());
        }
        return new ImageIcon(img);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == playlistB){
            playlistBIsPressed = true;
        }

        if(e.getSource() == playB){
            mp.resume();
        }

        if(e.getSource() == pausB){
            mp.pause();
        }

        if(e.getSource() == nextB){
            mp.next();
        }

        if(e.getSource() == prevB){
            mp.prev();
        }

        if(e.getSource() == exploreB){

            //input = explorer.showOpenDialog(this);
            input = items.explorer.showOpenDialog(this);
            if(input == JFileChooser.APPROVE_OPTION){
                File file = items.explorer.getSelectedFile();
                if(file != null){

                    Audio au = new Audio(file.getAbsolutePath());
                    mp.loadSong(au);
                    items.songAdded = true;
                    Items.changeInfo = true;
                    refreshData();
                    //System.out.println("yurrr");
                }
            }
        }

    }


    public BufferedImage LoadImg(String path){
        BufferedImage img = null;
        try {
            // Load image from classpath
            InputStream imgStream = getClass().getResourceAsStream("/resources/" + path);
            if (imgStream != null) {
                img = ImageIO.read(imgStream);
            } else {
                System.err.println("Image not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
    
    
    //GETTERS AND SETTERS
    public JButton getPlaylistB() {
        return playlistB;
    }

    public void setPlaylistB(JButton playlistB) {
        this.playlistB = playlistB;
    }

    public boolean isPlaylistBIsPressed() {
        return playlistBIsPressed;
    }

    public void setPlaylistBIsPressed(boolean playlistBIsPressed) {
        this.playlistBIsPressed = playlistBIsPressed;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public Border getBorder() {
        return border;
    }

    public void setBorder(Border border) {
        this.border = border;
    }

    public MPlayer getMp() {
        return mp;
    }

    public void setMp(MPlayer mp) {
        this.mp = mp;
    }

    public JPanel gettPanel() {
        return tPanel;
    }

    public void settPanel(JPanel tPanel) {
        this.tPanel = tPanel;
    }

    public String getTestTitle() {
        return testTitle;
    }

    public void setTestTitle(String testTitle) {
        this.testTitle = testTitle;
    }
    
}
