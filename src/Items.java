import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Items implements MouseListener, MouseMotionListener {

    public boolean playlistIsPressed = false, songAdded = false, overHandle = false, pressedHandle = false,
            inBound = false, dragged = false;
    public static boolean changeInfo = false;
    BufferedImage slider, mover;
    public double heightTick = 16;
    public int otherY = 40;
    public double songFPS = 0;
    MPlayer player;
    int index = 0;
    JPanel listen;
    JFileChooser explorer;

    public Items(MPlayer player) {

        slider = LoadImg("C:\\Users\\Surr Elii\\Documents\\CODE\\MBox_assets\\reworked\\sliderv2.png");
        mover = LoadImg("C:\\Users\\Surr Elii\\Documents\\CODE\\MBox_assets\\reworked\\mover.png");
        this.player = player;
        
        explorer = new JFileChooser();
        explorer.setCurrentDirectory(new File("C:\\Users\\Surr Elii\\Music"));
        explorer.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
    }

    public void update() {
        
        if (player.isPlaying) {
            index++;
            // i dont know what math ive done here but it works. dont touch it
            // this is how many frames we move per second
            songFPS = (0.073 * (460 / player.getCurrentSong().getActualSongTime()));
            heightTick += songFPS;
            // convertedTime += (int) heightTick * player.getPauseTime();
            //System.out.println("FPS>> " + songFPS + ": tick>>" + heightTick);
            if (index == 4) {
                index = 0;
            }

        }

        if (player.isStopped) {
            resetTick();
            System.out.println("reset");
            player.isStopped = false;
        }

        // System.out.println("shortX" + shortX);

    }

    public void resetTick() {
        heightTick = 16;
    }

    public void drawMover(Graphics g) {

        g.drawImage(mover, (int) heightTick, playPanel.shortY, playPanel.shortWid, playPanel.shortHeight, null);

    }

    public void drawMover(Graphics g, int y) {

        g.drawImage(mover, (int) heightTick, y, playPanel.shortWid, playPanel.shortHeight, null);

    }

    public void drawSlider(Graphics g, int x, int y) {

        g.drawImage(slider, x, y, playPanel.longWid, playPanel.longHeight, null);
    }

    public void drawSlider(Graphics g) {

        g.drawImage(slider, playPanel.longX, playPanel.longY, playPanel.longWid, playPanel.longHeight, null);
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

    public BufferedImage getSlider() {
        return slider;
    }

    public BufferedImage getMover() {
        return mover;
    }

    public double getHeightTick() {
        return heightTick;
    }

    public double getSongFPS() {
        return songFPS;
    }

    public void setSlider(BufferedImage slider) {
        this.slider = slider;
    }

    public void setMover(BufferedImage mover) {
        this.mover = mover;
    }

    public void setHeightTick(double heightTick) {
        this.heightTick = heightTick;
    }

    public void setSongFPS(double songFPS) {
        this.songFPS = songFPS;
    }

    public void setPlayer(MPlayer player) {
        this.player = player;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // if(((e.getX() >= (shortX-3)) && (e.getX() <= (shortX+17))) && ((e.getY() >=
        // shortY) && (e.getY() <= shortY+9))){
        // //pressedHandle = true;
        // }

        if (((e.getX() >= (playPanel.longX - 8)) && (e.getX() <= (playPanel.longWid + 13)))) {
            //System.out.println("in long thingy");
            inBound = true;
        } else {
            inBound = false;
        }
        if (inBound && pressedHandle) {
           // System.out.println("move bitch");
            dragged = true;
            heightTick = e.getX();
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


        if (((e.getX() >= (heightTick - 3)) && (e.getX() <= (heightTick + 17)))
                && ((e.getY() >= otherY) && (e.getY() <= otherY + 9))) {
                    System.out.println("in other press");
            pressedHandle = true;
            // clicked = true;
        }

        //diff panel
        if (((e.getX() >= (playPanel.shortX - 3)) && (e.getX() <= (playPanel.shortX + 17)))
                && ((e.getY() >= playPanel.shortY) && (e.getY() <= playPanel.shortY + 9))) {
            pressedHandle = true;
            // clicked = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // set the new position of the song

        pressedHandle = false;
        if (dragged) {
            player.playAt(e.getX());
            dragged = false;
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

}
