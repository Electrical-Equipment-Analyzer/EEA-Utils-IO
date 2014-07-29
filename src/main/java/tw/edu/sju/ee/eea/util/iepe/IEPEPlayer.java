/*
 * Copyright (C) 2014 Leo
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package tw.edu.sju.ee.eea.util.iepe;

import tw.edu.sju.ee.eea.util.iepe.io.IepeInputStream;
import tw.edu.sju.ee.eea.util.iepe.io.QuantizationStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Leo
 */
public class IEPEPlayer implements Runnable {

    private SourceDataLine audioOut = null;
    private PipedInputStream pipeIn;
    private PipedOutputStream pipeOut;

    private float sampleRate;
    private int sampleSizeInBits;
    private int channels;
    private int frameSize;
    private float frameRate;
    
    public IEPEPlayer(float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate) throws IOException {
        this.sampleRate = sampleRate;
        this.sampleSizeInBits = sampleSizeInBits;
        this.channels = channels;
        this.frameSize = frameSize;
        this.frameRate = frameRate;
        init();
    }

    private void init() throws IOException {
        this.pipeIn = new PipedInputStream(1024);
        this.pipeOut = new PipedOutputStream(pipeIn);
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        boolean filter = false;
        for (Mixer.Info mixerInfo : mixerInfos) {
            if (!mixerInfo.getName().matches(".*HDMI.*")) {
                continue;
            }
            filter = true;
        }
        for (Mixer.Info mixerInfo : mixerInfos) {
            if (filter && !mixerInfo.getName().matches(".*HDMI.*")) {
                continue;
            }
            System.out.println(mixerInfo.getName());
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            Line.Info[] lineInfos = mixer.getSourceLineInfo();
            for (Line.Info lineInfo : lineInfos) {
                try {
                    audioOut = (SourceDataLine) mixer.getLine(lineInfo);
                    break;
                } catch (LineUnavailableException lue) {
                } catch (ClassCastException cce) {
                }
            }
            if (audioOut != null) {
                try {
                    AudioFormat currentFormat = audioOut.getFormat();
                    audioOut.open(new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sampleRate, sampleSizeInBits, channels, frameSize, frameRate, currentFormat.isBigEndian()));
//                        audioOut.open(new AudioInputStream(qs, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 128000, 8, 1, 1, 128000, currentFormat.isBigEndian()), 10240000));
                    break;      //  Viable line found -- search no more!
                } catch (LineUnavailableException lue) {
                    audioOut = null;
                    continue;   //  Try another line or mixer
                }
            }
        }
        if (audioOut == null) {
            System.out.println("Unable Play Sounds");
            return;
        }
    }

    public OutputStream getOutputStream() {
        return pipeOut;
    }

    @Override
    public void run() {
        audioOut.start();
        QuantizationStream qs = new QuantizationStream(new IepeInputStream(pipeIn), sampleSizeInBits);
        try {
            while (!Thread.interrupted()) {
                byte[] buffer = qs.readQuantization();
                audioOut.write(buffer, 0, buffer.length);
            }
        } catch (IOException ex) {
        }
        audioOut.stop();
        pipeOut = null;
        pipeIn = null;
    }

}
