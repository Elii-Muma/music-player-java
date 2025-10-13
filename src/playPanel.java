import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

public class playPanel extends JPanel implements ActionListener, MouseMotionListener, MouseListener {

    // buttons
    JButton pause, resume, explore, next, prev, stop, repeatB, playlistB, muteB, soundB, repeat1, forward, shuffle;
    BufferedImage[][] animations;
    JLabel title, artist;
    String path2;
    Audio au;
    MPlayer player;
    JSlider slide;
    Items items;
    Rectangle longSlide, handleThingy;

    // Image bgImg;
    BufferedImage bgImg;
    public boolean playlistIsPressed = false, overHandle = false, pressedHandle = false, inBound = false,
            dragged = false;
    public boolean clicked = true;
    private String artistName = "_ _ _", songTitle = "_ _ _";
    public int index = 0;
    int input = 0;
    int convertedTime = 0;
    // slider coords
    public static int longX = 0;
    public static int longY = 0;
    public static int longWid = 0;
    public static int longHeight = 0;
    int min = 0;
    int sec = 00;
    int leftMin = 0;
    int leftSec = 00;
    double subFactor = 0;
    float now = 0;

    public static int shortX = 0;
    public static int shortY = 0;
    public static int shortWid = 0;
    public static int shortHeight = 0;

    // CONSTRUCTOR
    public playPanel(MPlayer p, Items items) {
        // setSize(500, 400);
        setBackground(new Color(197, 241, 172));
        setLayout(null);

        this.items = items;
        addMouseMotionListener(items);
        addMouseListener(items);

        player = p;

        buttons();

        // bgImg = LoadImg("C:\\Users\\ELIMUS\\Downloads\\bg.jpg");
        bgImg = LoadImgFromClass("/assets/BGtest2.jpg");

        initVar();
        this.longSlide = new Rectangle(longX, longY, longWid, longHeight);
        this.handleThingy = new Rectangle(shortX, shortY, shortWid, shortHeight);

        // screenLabels();
        loadAnimations();
        add(explore);
        add(pause);
        add(resume);
        // simpleLoop();
    }

    // BUTTONS
    private void buttons() {

        // EXPLORE
        this.explore = createButton("assets/add.png", "assets/rollAdd.png", "add");
        explore.setBounds(16, 316, 32, 32);

        // PAUSE
        this.pause = createButton("/assets/pause.png", "/assets/rollPause.png", "pause");
        pause.setBounds(251, 300, 64, 64);

        // RESUME
        this.resume = createButton("/assets/play.png", "/assets/rollPlay.png", "play/resume");
        resume.setBounds(185, 300, 64, 64);

        this.next = createButton("/assets/next.png", "/assets/rollNext.png", "next");
        next.setBounds(320, 300, 64, 32);

        this.prev = createButton("/assets/prev.png", "/assets/rollPrev.png", "prev");
        prev.setBounds(116, 300, 64, 32);

        this.stop = createButton("/assets/stop.png", "/assets/rollStop.png", "stop");
        stop.setBounds(116, 332, 64, 32);

        this.playlistB = createButton("/assets/playlist.png", "/assets/rollPlaylist.png", "playlist");
        playlistB.setBounds(414, 300, 32, 32);

        this.soundB = createButton("/assets/sound.png", "/assets/rollSound.png", "mute");
        soundB.setVisible(true);
        soundB.setBounds(446, 300, 32, 32);

        this.muteB = createButton("/assets/mute.png", "/assets/rollMute.png", "sound");
        muteB.setVisible(false);
        muteB.setBounds(446, 300, 32, 32);

        this.shuffle = createButton("/assets/shuffle.png", "/assets/rollShuffle.png", "sound");
        shuffle.setVisible(false);
        shuffle.setBounds(320, 332, 64, 32);

        this.repeatB = createButton("/assets/repeat.png", "/assets/rollRepeat.png", "repeat");
        repeatB.setVisible(false);
        repeatB.setBounds(320, 332, 64, 32);

        this.repeat1 = createButton("/assets/repeat1.png", "/assets/rollRepeat1.png", "sound");
        repeat1.setVisible(false);
        repeat1.setBounds(320, 332, 64, 32);

        this.forward = createButton("/assets/forward.png", "/assets/rollForward.png", "sound");
        forward.setVisible(true);
        forward.setBounds(320, 332, 64, 32);
    }

    public void initVar() {

        longX = 16;
        longY = 265;
        longWid = 460;
        longHeight = items.slider.getHeight();

        // INITALIZE THE SLIDERS VARIABLES

        shortY = 262;
        shortWid = items.mover.getWidth();
        shortHeight = items.mover.getHeight();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        shortX = (int) items.getHeightTick();
        // System.out.println(shortX);
        g.drawImage(bgImg, 0, 0, 500, 400, null);

        items.drawSlider(g);
        items.drawMover(g);
        /*
         * g.setColor(Color.black);
         * g.drawRect(shortX-2, shortY-3, shortWid+5, shortHeight+5);
         * 
         * g.setColor(Color.cyan);
         * g.drawRect(longX, longY, longWid, longHeight);
         */
        render(g);

        // draw time on screen
        renderTime(g);

        g.setColor(Color.black);
        String font = "dogica pixel";
        // 34 for the text length is ideal
        g.setFont(new Font(font, Font.PLAIN, 12));
        int xPos = (250 - ((artistName.length() * 10) / 2));
        g.drawString(artistName, xPos, 40);

        // ------//

        g.setFont(new Font(font, Font.BOLD, 14));
        int posX = (250 - ((songTitle.length() * 12) / 2));
        even_or_odd(songTitle.length());
        g.drawString(songTitle, posX, 20);

    }

    public void renderTime(Graphics g) {

        if (player.songPut = true && player.getCurrentSong() != null) {
            sec = player.getCurrentSong().getSec();
            min = player.getCurrentSong().getMin();
            player.songPut = false;
        }

        if (player.getCurrentSong() != null) {
        }

        String time = "";
        if (sec < 10) {
            time = min + ":0" + sec;
        } else
            time = min + ":" + sec;

        // time at the end
        g.setFont(new Font("dogica pixel", Font.PLAIN, 8));
        g.drawString(time, 450, 255);

        String leftTime = "";
        if (player.getCurrentSong() != null) {
            // leftTime = "" + (int)heightTick*0.079;

            leftSec = (int) ((items.getHeightTick() * player.getCurrentSong().getActualSongTime()) / 460);
            if ((leftSec % 60) == 0) {
            }
            // System.out.println(subFactor);
            if (leftSec >= 60) {
                leftMin = (int) leftSec / 60;
                int divide = leftSec / 60;
                subFactor = 60 * divide;
                System.out.println("SUB FACTOR: " + subFactor);
            } else {
                leftMin = 0;
                subFactor = 0;
            }

            if ((leftSec - subFactor) < 10) {
                leftTime = leftMin + ":0" + (int) (leftSec - subFactor);
            } else
                leftTime = leftMin + ":" + (int) (leftSec - subFactor);
        } else
            leftTime = leftMin + ":0" + leftSec;

        g.drawString(leftTime, 16, 255);

    }

    public void resetValues() {

        leftMin = 0;
        leftSec = 0;
        subFactor = 0;
    }

    public void render(Graphics g) {
        g.drawImage(animations[items.index][0], 154, 55, 192, 192, null);
    }

    public void update() {

        // items.update();

        if (player.isStopped) {
            items.resetTick();
            resetValues();
            System.out.println("reset");
            player.isStopped = false;
        }

        if (Items.changeInfo) {
            artistName = player.getCurrentSong().getArtist();
            songTitle = player.getCurrentSong().getTitle();
            resetValues();
            Items.changeInfo = false;
        }

        if (shortY >= longY) {
            shortY = longY;
        }

        longSlide.setLocation(longX, longY);
        handleThingy.setLocation(shortX, shortY);

        if (pressedHandle) {
            System.out.println("we've pressed yoouuu");
        }

        // REPEAT FUNCS
        if (forward.isVisible()) {
            player.forwardOnly();
        }
        if (repeatB.isVisible()) {
            player.repeatAll();
        }
        // System.out.println("shortX" + shortX);

        if (repeat1.isVisible()) {
            player.repeatOne();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pause) {
            player.pause();
        }

        if (e.getSource() == resume) {
            player.resume();
        }

        if (e.getSource() == stop) {
            player.stop();
        }

        if (e.getSource() == soundB) {
            player.plusVol();
            // muteB.setVisible(true);
            // soundB.setVisible(false);
        }

        if (e.getSource() == muteB) {
            System.out.println(player.getCurrentSong().getArtist());
            muteB.setVisible(false);
            soundB.setVisible(true);
        }

        if (e.getSource() == forward) {
            // player.minusVol();
            forward.setVisible(false);
            repeat1.setVisible(true);
        }

        if (e.getSource() == repeat1) {
            repeat1.setVisible(false);
            repeatB.setVisible(true);
        }

        if (e.getSource() == repeatB) {
            repeatB.setVisible(false);
            forward.setVisible(true);
        }

        if (e.getSource() == playlistB) {
            playlistIsPressed = true;
        }

        if (e.getSource() == next) {
            player.next();
        }

        if (e.getSource() == prev) {
            player.prev();
        }

        if (e.getSource() == explore) {

            // input = explorer.showOpenDialog(this);
            input = items.explorer.showOpenDialog(this);
            if (input == JFileChooser.APPROVE_OPTION) {
                File file = items.explorer.getSelectedFile();
                if (file != null) {

                    Audio au = new Audio(file.getAbsolutePath());
                    player.loadSong(au);
                    items.songAdded = true;
                    Items.changeInfo = true;
                    System.out.println("file nabbed:: " + file.getAbsolutePath());
                }
            }
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (((e.getX() >= (shortX - 3)) && (e.getX() <= (shortX + 17)))
                && ((e.getY() >= shortY) && (e.getY() <= shortY + 9))) {
            // pressedHandle = true;
        }

        if (((e.getX() >= (longX - 8)) && (e.getX() <= (longWid + 13)))) {
            System.out.println("in long thingy");
            inBound = true;
        } else {
            inBound = false;
        }
        if (inBound && pressedHandle) {
            System.out.println("move bitch");
            dragged = true;
            // items.setHeightTick(e.getX());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // System.out.println(e.getY() + "<<mouse : other>>" + shortY);

        // if(((e.getX() >= (longX-8)) && (e.getX() <= (longWid+13))) && ((e.getY() >=
        // longY) && (e.getY() <= longY+longHeight))){
        // System.out.println("in long thingy");
        // inBound = true;
        // }else{
        // inBound = false;
        // }
        // if(inBound && pressedHandle){
        // System.out.println("move bitch");
        // heightTick = e.getX();
        // }

        // System.out.println(heightTick + "<<tick :: xOfSlide>>" + e.getX());

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (((e.getX() >= (shortX - 3)) && (e.getX() <= (shortX + 17)))
                && ((e.getY() >= shortY) && (e.getY() <= shortY + 9))) {
            pressedHandle = true;
            // clicked = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // set the new position of the song

        pressedHandle = false;
        if (dragged || clicked) {
            player.playAt(e.getX());
            dragged = false;
            clicked = false;
        }

        // if(clicked && !dragged){
        // heightTick = e.getX();
        // System.out.println("tick pos: " + heightTick);
        // player.playAt(e.getX());
        // clicked = false;
        // }
        System.out.println("released at: " + e.getX());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public JButton createButton(String iconPath, String rollPath, String toolTip) {

        JButton b = new JButton(createIcon(iconPath));
        b.addActionListener(this);
        // make background transparent
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        // ------------------------------//
        b.setToolTipText(toolTip);
        b.setRolloverIcon(createIcon(rollPath));
        add(b);

        return b;
    }

    // create image
    private ImageIcon createIcon(String path) {
        URL img = getClass().getResource(path);
        // ImageIcon icon= new ImageIcon(img);
        if (img == null) {
            System.err.println("Image not found: " + path);
        } else {
            System.out.println("Image found at: " + img.toExternalForm());
        }
        return new ImageIcon(img);
    }

    public void loadAnimations() {// load animation arr

        //why didnt i uze a relative path?
        BufferedImage img = LoadImg(
                "C:\\Users\\Surr Elii\\Documents\\CODE\\PROJEKS_MAIN\\eclipse-workspace\\musicBox\\src\\assets\\disc_sheet.png");

        animations = new BufferedImage[5][1];// its[col][row]

        for (int i = 0; i < animations.length; i++) {
            for (int j = 0; j < animations[i].length; j++) {
                animations[i][j] = img.getSubimage(i * 256, j * 256, 256, 256);
            }
        }
    }

    public BufferedImage LoadImgFromClass(String path) {
        BufferedImage img = null;
        try {
            // Load image from classpath
            InputStream imgStream = getClass().getResourceAsStream(path);
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

    public BufferedImage LoadImg(String path) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return img;

    }

    boolean even_or_odd(int num) {

        int divide = num % 2;
        if (divide == 0) {
            // System.out.println("even number");
            return true; // its even
        } else
            // System.out.println("odd nummber");
            return false;
    }

}
