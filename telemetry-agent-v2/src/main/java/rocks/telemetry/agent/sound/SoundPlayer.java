package rocks.telemetry.agent.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import static java.lang.Integer.MAX_VALUE;

/**
 * Not thread safe
 */
public class SoundPlayer {

    private byte[] buf = null;
    private float frequency = 44100; //44100 sample points per 1 second
    private SourceDataLine sdl = null;
    Integer volume = 50;
    Integer hz = 0;

    public SoundPlayer() throws LineUnavailableException {
        AudioFormat af = new AudioFormat(frequency, 8, 1, true, false);
        sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
    }

    public void play() {
        new Thread(() -> {
            int i = 1;
            buf = new byte[1];
            for (; i >= 0; ) {
                double angle = (i / frequency) * hz * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * volume);
                sdl.write(buf, 0, 1);
                i = i == MAX_VALUE ? 1 : i + 1;
            }
        }).start();
    }

    public void setup(Integer change) {
        hz = (int) ((change / 14000f) * 50);
        if (change > 7000) {
            volume = (int) (50 - (change / 14000f) * 50);
        }
    }

    public void stop() {
        sdl.drain();
        sdl.stop();
    }
}