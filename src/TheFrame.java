import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TheFrame extends JFrame implements Runnable{

    MPlayer mp;
    playPanel pp;
    playlistPanel playP;
    Thread loop;
    Items items;
    Image icon;
    TheFrame(){
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setTitle("Pixel BoomBox");
        setLocationRelativeTo(null);
        setResizable(false);
        URL img = getClass().getResource("/assets/disc.png");
        icon = Toolkit.getDefaultToolkit().getImage(img);
        setIconImage(icon);
        mp = new MPlayer();
        items = new Items(mp);
        pp = new playPanel(mp, items);
        playP = new playlistPanel(mp, items);
        
        //ADD TO FRAME
        add(pp);
        //switchBtwn();

        setVisible(true);
    }

    //SWITCH BETWEEN PANELS
    public void switchBtwn(JPanel p1, JPanel p2){

        if(getContentPane().getComponent(0) == p1){
            this.getContentPane().remove(p1);
            this.getContentPane().add(p2);
        }else{
            this.getContentPane().remove(p2);
            this.getContentPane().add(p1);
        }

        this.revalidate();
        this.repaint();
    }

    public void checkForSwitch(){

        if(pp.playlistIsPressed == true){
            System.out.println("switch to playlist");
            switchBtwn(pp, playP);
            playP.refreshData();

            playP.show = true;
            pp.playlistIsPressed = false;
        }

        if(playP.playlistBIsPressed == true){
            switchBtwn(pp, playP);
            playP.show = false;
            playP.playlistBIsPressed = false;
        }

    }


    public void startLoop(){
        loop = new Thread(this);
        loop.start();
    }

    void update(){

        items.update();
        playP.update();
        playP.itemPanel.repaint();
        checkForSwitch();
        pp.update();
        pp.repaint();
    }

    @Override
    public void run() {

		double timePerFrame = 1000000000/12; //HOW LONG WILL EACH FRAME SHOULD LAST
		double timePerUpdate = 1000000000/36; //TIME BETWEEN UPDATES,TIME OF THE FREQUENCY
		
		long prevTime = System.nanoTime();
		
		
		int frames = 0;
		int updates = 0; //when we update check the time we enter the iteration then store that time in the now variable then take that time
		long lastCheck = System.currentTimeMillis();
		
		double deltaU = 0;
		double deltaF =0;
		
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaU += (currentTime - prevTime) / timePerUpdate;
			deltaF += (currentTime - prevTime) / timePerFrame;
			prevTime = currentTime;
			
			if(deltaU >= 1) {
				//update();
				updates++;
				deltaU--;
			}
			
			if(deltaF >= 1) {
                update();
				frames++;
				deltaF--;
			}

			// if(System.currentTimeMillis() - lastCheck >= 1000) {
			// 	lastCheck = System.currentTimeMillis();
			// 	System.out.println("FPS: " + frames + " | UPS: " + updates );
			// 	frames = 0;
			// 	updates =0;
			// }
			
			
		}

    }
}
