package kis.hackathon.winners.yalla;

/**
 * Created by user on 09/06/2016.
 */
public class Place {
    private double x;

    public double getY() {
        return y;
    }

    public Place(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    private double y;
}
