import javax.swing.*;
import java.awt.*;

public class PlayGameScreen extends JPanel {
    private static final long serialVersionUID = 1L;

    private int screenWidth;
    private int screenHeight;
    private boolean isSplash = true;
    private int successfulJumps = 0;
    private String message = "Dino Runner";
    private Font primaryFont = new Font("Goudy Stout", Font.BOLD, 56);
    private Font failFont = new Font("Calibri", Font.BOLD, 56);
    private int messageWidth = 0;
    private int scoreWidth = 0;
    private Rock rock1, rock2;
    private Dino dino;

    public PlayGameScreen(int screenWidth, int screenHeight, boolean isSplash) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.isSplash = isSplash;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(new Color(89, 81, 247)); //color for the blue sky
        g.fillRect(0, 0, screenWidth, screenHeight*7/8); //create the sky rectangle
        g.setColor(new Color(147, 136, 9)); //brown color for ground
        g.fillRect(0, screenHeight*7/8, screenWidth, screenHeight/8); //create the ground rectangle
        g.setColor(Color.BLACK); //dividing line color
        g.drawLine(0, screenHeight*7/8, screenWidth, screenHeight*7/8); //draw the dividing line

        //objects must be instantiated before they're drawn!
        if(rock1 != null && rock2 != null) {
            g.drawImage(rock1.getRock(), rock1.getxLocation(), rock1.getyLocation(), null);
            g.drawImage(rock2.getRock(), rock2.getxLocation(), rock2.getyLocation(), null);
        }

        if(!isSplash && dino != null) {
            g.drawImage(dino.getDinosaur(), dino.getxLocation(), dino.getyLocation(), null);
        }

        //needed in case the primary font does not exist
        try {
            g.setFont(primaryFont);
            FontMetrics metric = g.getFontMetrics(primaryFont);
            messageWidth = metric.stringWidth(message);
            scoreWidth = metric.stringWidth(String.format("%d", successfulJumps));
        }
        catch(Exception e) {
            g.setFont(failFont);
            FontMetrics metric = g.getFontMetrics(failFont);
            messageWidth = metric.stringWidth(message);
            scoreWidth = metric.stringWidth(String.format("%d", successfulJumps));
        }

        g.drawString(message, screenWidth/2-messageWidth/2, screenHeight/4);

        if(!isSplash) {
            g.drawString(String.format("%d", successfulJumps), screenWidth/2-scoreWidth/2, 50);
        }
    }

    public void setRock(Rock rock1, Rock rock2){
        this.rock1 = rock1;
        this.rock2 = rock2;
    }

    public void sendText(String message){
        this.message = message;
    }

    public void incrementJump() {
        successfulJumps++;
    }

    public int getScore() {
        return successfulJumps;
    }

    public void setDino(Dino dino) {
        this.dino = dino;
    }
}
