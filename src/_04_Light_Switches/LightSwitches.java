package _04_Light_Switches;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;

import game_tools.Game;
import game_tools.GameControlScene;

/*
 * Goal: Use binary operations to create a light show!
 * 
 * Implement the methods below using binary operations.
 * The 'lightsOnOff' variable is an 8-bit bitmap, meaning each bit is like an
 * on/off switch for one of the lights. A binary '1' indicates the light should
 * be on and a binary '0' indicates the light should be off. For example,
 *      bit   7    6    5     4      3     2    1    0
 *          cyan pink black yellow orange red green blue
 * where,
 *      lightsOnOff = 0b01010001    // blue, yellow, and pink are on
 *      lightsOnOff = 0x28          // orange and black are on
 *      lightsOnOff = 65            // green and cyan are on
 * 
 * The code is implemented correctly if the light pattern matches the pattern
 * in the LightSwitches.gif file in this folder.
 */
public class LightSwitches implements GameControlScene {
    static final int DISPLAY_WIDTH = 600;
    static final int DISPLAY_HEIGHT = 150;
    
    Deque<Runnable> workQueue;
    Game gameFrame;
    
    // Changing this array requires changing the code and instructions as well
    Color[] lightColors = {
             Color.BLUE,        // 0
             Color.GREEN,       // 1
             Color.RED,         // 2
             Color.ORANGE,      // 3
             Color.YELLOW,      // 4
             Color.BLACK,       // 5
             Color.PINK,        // 6
             Color.CYAN         // 7
            };

    // 8-bit bitmap. Leave as int so methods won't have to cast to a byte
    int lightsOnOff = 0;

    /*
     * This method should check if the specified light is on, example:
     * index = 6        // return true if pink is on (bit 6 == 1)
     */
    boolean isLightOn(int index) {
        return (lightsOnOff & (1 << index)) != 0;
    }
    
    /*
     * This method should only turn on 1 light, example:
     * index = 4        // turn off yellow only (set bit 4 = 1)
     */
    void turnLightOn(int index) {
        lightsOnOff |= (1 << index);
    }
    
    /*
     * This method should only turn off 1 light
     * index = 0        // turn off blue only (set bit 0 = 0)
     */
    void turnLightOff(int index) {
        lightsOnOff &= ~(1 << index);
    }
    
    /*
     * This method should be able to turn on multiple lights
     * lightsBitmap = 0b01100110  // lights 1, 2, 5, 6 on
     */
    void turnMultiLightsOn(int lightsBitmap) {
        lightsOnOff |= lightsBitmap;
    }
    
    /*
     * This method should be able to turn off multiple lights
     * lightsBitmap = 0b10000001  // lights 0, 7 off
     */
    void turnMultiLightsOff(int lightsBitmap) {
        lightsOnOff &= ~lightsBitmap;
    }
    
    /*
     * This method should toggle the state of multiple lights
     * example input:
     * lightsOnOff  = 0b10000001  // blue(0) and cyan(7) on
     * lightsBitmap = 0b10011001  // toggle lights 0, 3, 4, 7
     * output:
     * lightsOnOff  = 0b00011000  // blue(0) and cyan(7) off,
     *                               orange(3) and yellow(4) on
     */
    void toggleLights(int lightsBitmap) {
        lightsOnOff ^= lightsBitmap;
    }

    // Implementations for the light sequences based on comments and workQueue mechanism
    void runLightSequence1() {
        workQueue.add(()->turnMultiLightsOff(0xFF));
        workQueue.add(()->turnLightOn(0));
        workQueue.add(()->delayMs(200));
        
        for (int i = 0; i < lightColors.length - 1; i++) {
            final int iFinal = i;
            workQueue.add(()->turnLightOff(iFinal));
            workQueue.add(()->turnLightOn(iFinal + 1));
            workQueue.add(()->delayMs(200));
        }

        workQueue.add(()->turnLightOff(7));
    }
    
    void runLightSequence2() {
        workQueue.add(()->turnLightOn(7));
        workQueue.add(()->delayMs(200));
        
        for (int i = lightColors.length - 1; i > 0; i--) {
            final int iFinal = i;
            workQueue.add(()->turnLightOff(iFinal));
            workQueue.add(()->turnLightOn(iFinal - 1));
            workQueue.add(()->delayMs(200));
        }

        workQueue.add(()->turnLightOff(0));
    }
    
    void runLightSequence3() {
        workQueue.add(()->turnMultiLightsOff((1<<8) - 1));
        workQueue.add(()->delayMs(200));
        workQueue.add(()->turnMultiLightsOn(0b00011000));
        workQueue.add(()->delayMs(200));
        workQueue.add(()->turnMultiLightsOff(0b00011000));
        workQueue.add(()->turnMultiLightsOn(0b00100100));
        workQueue.add(()->delayMs(200));
        workQueue.add(()->turnMultiLightsOff(0b00100100));
        workQueue.add(()->turnMultiLightsOn(0b01000010));
        workQueue.add(()->delayMs(200));
        workQueue.add(()->turnMultiLightsOff(0b01000010));
        workQueue.add(()->turnMultiLightsOn(0b10000001));
        workQueue.add(()->delayMs(200));
        workQueue.add(()->turnMultiLightsOff((1<<8) - 1));
    }
    
    void runLightSequence4() {
        workQueue.add(()->turnMultiLightsOff((1<<8) - 1));
        workQueue.add(()->delayMs(500));
        workQueue.add(()->turnMultiLightsOn(0b10101010));
        workQueue.add(()->delayMs(500));
        workQueue.add(()->toggleLights((1<<8) - 1));
        workQueue.add(()->delayMs(500));
        workQueue.add(()->toggleLights(0b00001111));
        workQueue.add(()->delayMs(500));
        workQueue.add(()->toggleLights(0b11110000));
    }

    public LightSwitches() {
        workQueue = new ArrayDeque<>();
        gameFrame = new Game();
        gameFrame.start();
    }
    
    void delayMs(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Game loop method
    @Override
    public void update() {
        // Check if there is any work in the queue
        if (!workQueue.isEmpty()) {
            // Remove and run the next piece of work
            Runnable work = workQueue.poll();
            work.run();
        }
    }

    // Drawing method
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(10));
        int spacing = DISPLAY_WIDTH / (lightColors.length + 1);
        int y = DISPLAY_HEIGHT / 2;
        for (int i = 0; i < lightColors.length; i++) {
            int x = spacing * (i + 1);
            if (isLightOn(i)) {
                g.setColor(lightColors[i]);
                g.fillOval(x - 20, y - 20, 40, 40);
            }
            g.setColor(Color.BLACK);
            g.drawOval(x - 20, y - 20, 40, 40);
        }
    }

    // Key pressed event method
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            runLightSequence1();
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            runLightSequence2();
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            runLightSequence3();
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            runLightSequence4();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    public static void main(String[] args) {
        new LightSwitches();
    }

	@Override
	public void draw(Graphics arg0) {
		// TODO Auto-generated method stub
		
	}
}
