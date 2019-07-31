import java.awt.*;
import java.awt.image.BufferedImage;

public class Dino {
    private Image dinosaur;
    private int xLocation = 0;
    private int yLocation = 0;

    public Dino(int initalWidth, int initialHeight) {
        dinosaur = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resource/dino.png.png"));
        scaleBird(initalWidth, initialHeight);
    }

    public void scaleBird(int width, int height) {
        dinosaur = dinosaur.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    public Image getDinosaur() {
        return dinosaur;
    }

    public int getDinosaurWidth() {
        try {
            return dinosaur.getWidth(null);
        } catch (Exception e) { //todo see a more concrete expression
            return -1;
        }
    }

    public int getDinosaurHeight() {
        try {
            return dinosaur.getHeight(null);
        } catch (Exception e) { //todo see a more concrete expression
            return -1;
        }
    }

    public int getxLocation() {
        return xLocation;
    }

    public void setxLocation(int xLocation) {
        this.xLocation = xLocation;
    }

    public int getyLocation() {
        return yLocation;
    }

    public void setyLocation(int yLocation) {
        this.yLocation = yLocation;
    }

    public Rectangle getRectangle() {
        return (new Rectangle(xLocation, yLocation, dinosaur.getWidth(null), dinosaur.getHeight(null)));
    }

    public BufferedImage getBufferedImage() {
        BufferedImage bufferedImage = new BufferedImage(dinosaur.getWidth(null), dinosaur.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(dinosaur, 0, 0, null);
        graphics.dispose();
        return bufferedImage;
    }
}
