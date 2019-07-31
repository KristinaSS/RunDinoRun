import java.awt.*;
import java.awt.image.BufferedImage;

public class Rock {
    private Image rock;
    private int xLocation = 0;
    private int yLocation = 0;

    public Rock(int initalWidth, int initialHeight) {
        rock = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("resource/rock.png.png"));
        scaleBird(initalWidth,initialHeight);
    }

    public void scaleBird(int width, int height){
        rock = rock.getScaledInstance(width,height,Image.SCALE_SMOOTH);
    }

    public Image getRock() {
        return rock;
    }
    public int getRockWidth(){
        try{
            return rock.getWidth(null);
        }catch (Exception e){ //todo see a more concrete expression
            return -1;
        }
    }
    public int getRockHeight(){
        try{
            return rock.getHeight(null);
        }catch (Exception e){ //todo see a more concrete expression
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
    public Rectangle getRectangle(){
        return (new Rectangle(xLocation,yLocation,rock.getWidth(null),rock.getHeight(null)));
    }
    public BufferedImage getBufferedImage(){
        BufferedImage bufferedImage = new BufferedImage(rock.getWidth(null),rock.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(rock,0,0,null);
        graphics.dispose();
        return bufferedImage;
    }
}
