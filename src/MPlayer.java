import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.JavaSoundAudioDevice;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class MPlayer extends PlaybackListener {

    private Audio currentSong; // will be newest song adde
    public ArrayList<Audio> cSong = new ArrayList<>();
    static int index = 0;
    public int playTime = 0;
    double time;
    // JLayer will handle playing the music
    double check = 0;
    private AdvancedPlayer advPlayer;
    AudioDevice device;
    public int pauseTime = 0;
    FloatControl vol;
    Float volume = 0.0f;
    public boolean isPaused = false, isPlaying = false, isStopped = false, wasStopped = false, jumpTo = false,
            songPut = false, isFinished = false, isRepeatOne = false, isRepeatAll = false;

    // --NOTES--??
    // -WHERE YOU SEE .ISEMPTY, IT WAS TO CHECK IF NULL

    public void loadSong(Audio audio) {
        // ---COMMENTS ARE PREV CHANGES---//
        // this.currentSong = audio;
        cSong.add(audio);
        this.currentSong = cSong.getLast();
        System.out.println(index);
        // AdvancedPlayer pl = new AdvancedPlayer()
        isStopped = false;
        isPlaying = true;
        isFinished = false;

        index++;
        if (!cSong.isEmpty() && isPaused) { // IF CURRENTSONG IS NOT NULL
            isPaused = false;
            playCurrentSong();
            return;
        }
        if (isPlaying) {
            stop();
            playCurrentSong();
            return;
        } else if (!cSong.isEmpty() && isPlaying) {
            stop();
            playCurrentSong();
            return;
        } else {
            //playCurrentSong();
            return;
        }

    }

    public void pause() {
        if (!cSong.isEmpty() && isPlaying) {
            isPaused = true;
            isPlaying = false;
            isStopped = false;
            isFinished = false;
            jumpTo = false;
            advPlayer.stop();
            advPlayer.close();
            // advPlayer = null;
        }
        System.out.println("stopped at: " + pauseTime);
    }

    public void stop() {

        if (advPlayer != null) {

            //isFinished = true;
            isStopped = true;
            isPaused = false;
            isPlaying = false;
            jumpTo = false;

            pauseTime = 0;

            advPlayer.close();
            advPlayer = null;
        }

    }

    public void playNew(int i) {
        stop();
        index = i;
        this.currentSong = cSong.get(i - 1);
        Items.changeInfo = true;
        resume();
    }

    public void prev() {
        if (!cSong.isEmpty()) {
            if(!isRepeatOne){
            int newIndex = 0;
            if (index == 1) {
                newIndex = cSong.size();
            } else
                newIndex = index - 1;

            playNew(newIndex);
          } else{
                playNew(index);
            }
        }
    }

    public void next() {
        if (!cSong.isEmpty()) {
            if(!isRepeatOne){
                int newIndex = 0;
                if (index == cSong.size()) {
                    newIndex = 1;
                } else
                    newIndex = index + 1;

                playNew(newIndex);
            }else{
                System.out.println("working here");
                playNew(index);
            }
        }
    }

    public void resume() {
        if (!cSong.isEmpty() && !isPlaying) {
            // isStopped = false;
            playCurrentSong();
            isFinished = false;
        }
        // isPaused = false;
        // advPlayer.play(pauseTime);
    }

    public void playAt(int time) {
        // jump to a certain position
        if (!cSong.isEmpty()) {
            jumpTo = true;
            isPaused = false;
            isStopped = false;
            isFinished = false;

            this.playTime = (int) ((time * cSong.get(index - 1).getNumOfFrames()) / 460);
            pauseTime = playTime;
            // advPlayer.close();
            if (!isPaused) {
                // advPlayer.stop();
                advPlayer.close();
                device.close();
                // device = null;
                // advPlayer = null;
                //playCurrentSong();
            }
            if (isPaused) {
                // advPlayer.stop();
                advPlayer.close();
                device.close();
                playCurrentSong();
                isPaused = false;
            }
            if (vol != null) {
                updateVol();
            }
            System.out.println("to be played at: " + playTime);
        }
        // return playTime;
    }

    public void forwardOnly() {
        repeatAll();
    }

    public void repeatAll(){
            isRepeatAll = true;
            isRepeatOne = false;
        if(isFinished){
            if(index == cSong.size()){
                index = 1;
                playNew(index);
            }else{
                index += 1;
                playNew(index);
            }
        }
    }

    public void repeatOne(){
            isRepeatAll = false;
            isRepeatOne = true;
        if(isFinished){
            playNew(index);
        }
    }

    public void close() {
        device.close();
    }

    public void createVol() {

        if (vol == null && !cSong.isEmpty()) {
            Class<JavaSoundAudioDevice> clazz = JavaSoundAudioDevice.class;
            Field[] fields = clazz.getDeclaredFields();
            try {
                SourceDataLine source = null;
                for (Field field : fields) {
                    if ("source".equals(field.getName())) {
                        field.setAccessible(true);
                        source = (SourceDataLine) field.get(this.device);
                        field.setAccessible(false);
                        this.vol = (FloatControl) source.getControl(FloatControl.Type.MASTER_GAIN);
                        // vol = (FloatControl) source.getControl(FloatControl.Type.VOLUME)
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setVolume(float gain) {

        if (this.vol != null && !cSong.isEmpty()) {
            float newGain = Math.min(Math.max(gain, vol.getMinimum()), vol.getMaximum());
            System.out.println("vol was: " + vol.getValue() + " __Then it'll be: " + newGain);

            vol.setValue((newGain));
        }
    }

    public void playCurrentSong() {
        isPlaying = true;
        songPut = true;
        try {

            // read mp3 audio data
            FileInputStream file = new FileInputStream(cSong.get(index - 1).getSongPath());// FOR NOW I SAY GET FIRST
                                                                                           // BUT NEXT YOU PASS INDEX
                                                                                           // FROM TABLE
            BufferedInputStream bInStr = new BufferedInputStream(file);

            this.device = FactoryRegistry.systemRegistry().createAudioDevice();

            // create new adv player
            advPlayer = new AdvancedPlayer(bInStr, device);
            advPlayer.setPlayBackListener(this);
            startMusicThread();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // control volume down here

    }

    public void updateVol() {
        // vol = null;
        volume -= 1;
        createVol();
        setVolume(volume);
    }

    public void minusVol() {

        volume -= 5;
        createVol();
        setVolume(volume);
        System.out.println("vol");

    }

    public void plusVol() {

        volume += 5;

        createVol();
        setVolume(volume);
        System.out.println("vol added");
    }

    public void makeCurrentSong(int i) {
        this.currentSong = cSong.get(i);
    }

    // thread that'll handle playing the music
    private void startMusicThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    if (isPaused && !isStopped) {
                        // System.out.println("hrreeee");
                        advPlayer.play(pauseTime, Integer.MAX_VALUE);
                    } else if (isStopped) {
                        // System.out.println("also heerreee");
                        advPlayer.play();
                    } else if (jumpTo) {
                        advPlayer.play(playTime, Integer.MAX_VALUE);
                        // jumpTo = false;
                    } else
                        advPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    public void playbackStarted(PlaybackEvent evt) {
        // gets called when music starts
    }

    @Override
    public void playbackFinished(PlaybackEvent evt) {
        // gets called when music stops
        if (isPaused) {
            time = evt.getFrame();
            this.pauseTime += (int) (time * cSong.get(index - 1).getMsPerMin());
            // this.check = 1000/pauseTime;
            // System.out.println("to be played at: " + playTime);
            System.out.println("paused at: " + pauseTime);
        } else if (isStopped) {
            isPlaying = false;
            System.out.println("song has been stopped");
        } else 
            isFinished = true;
        /*else {
            isPlaying = false;
            if (index == cSong.size()) {
                index = 1;
            } else
                //index += 1;
*/
            //playNew(index); //plays the next song in da queue
            //isFinished = true;



            if(index == cSong.size() && isPlaying){
                System.out.println("last song done");
            }

        //}
    }

    // GETTERS

    public Audio getCurrentSong() {
        return currentSong;
    }

    public AdvancedPlayer getAdvPlayer() {
        return advPlayer;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public ArrayList<Audio> getcSong() {
        return cSong;
    }

    public int getPauseTime() {
        return pauseTime;
    }

    public boolean isStopped() {
        return isStopped;
    }

    public boolean isWasStopped() {
        return wasStopped;
    }

    public void setCurrentSong(Audio currentSong) {
        this.currentSong = currentSong;
    }

    public void setcSong(ArrayList<Audio> cSong) {
        this.cSong = cSong;
    }

    public int getIndex() {
        return index;
    }

    public void setAdvPlayer(AdvancedPlayer advPlayer) {
        this.advPlayer = advPlayer;
    }

    public void setPauseTime(int pauseTime) {
        this.pauseTime = pauseTime;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void setPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public void setStopped(boolean isStopped) {
        this.isStopped = isStopped;
    }

    public void setWasStopped(boolean wasStopped) {
        this.wasStopped = wasStopped;
    }

    public double getTime() {
        return time;
    }

}
