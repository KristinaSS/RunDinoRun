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

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

        graphics.setColor(new Color(89, 81, 247));
        graphics.fillRect(0, 0, screenWidth, screenHeight*7/8);
        graphics.setColor(new Color(81, 147, 33));
        graphics.fillRect(0, screenHeight*7/8, screenWidth, screenHeight/8);
        graphics.setColor(Color.BLACK);
        graphics.drawLine(0, screenHeight*7/8, screenWidth, screenHeight*7/8);

        if(rock1 != null && rock2 != null) {
            graphics.drawImage(rock1.getRock(), rock1.getxLocation(), rock1.getyLocation(), null);
            graphics.drawImage(rock2.getRock(), rock2.getxLocation(), rock2.getyLocation(), null);
        }

        if(!isSplash && dino != null) {
            graphics.drawImage(dino.getDinosaur(), dino.getxLocation(), dino.getyLocation(), null);
        }

        try {
            graphics.setFont(primaryFont);
            FontMetrics metric = graphics.getFontMetrics(primaryFont);
            messageWidth = metric.stringWidth(message);
            scoreWidth = metric.stringWidth(String.format("%d", successfulJumps));
        }
        catch(Exception e) {
            graphics.setFont(failFont);
            FontMetrics metric = graphics.getFontMetrics(failFont);
            messageWidth = metric.stringWidth(message);
            scoreWidth = metric.stringWidth(String.format("%d", successfulJumps));
        }

        graphics.drawString(message, screenWidth/2-messageWidth/2, screenHeight/4);

        if(!isSplash) {
            graphics.drawString(String.format("%d", successfulJumps), screenWidth/2-scoreWidth/2, 50);
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
