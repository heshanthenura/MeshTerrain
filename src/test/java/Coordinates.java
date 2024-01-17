import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class Coordinates {

    static Logger logger = Logger.getLogger("info");
    static ArrayList<Point> pointsArray = new ArrayList<>();
    static ArrayList<Face> facesArray = new ArrayList<>();
    static int length = 100;
    static int columns = 4;
    static int rows = 1;
    static int faces = columns * rows * 2;
    static int pointsLength;
    static PreviousEvenFacePoint previousEvenFacePoint = new PreviousEvenFacePoint(0, 0, 0);
    static PreviousOddFacePoint previousOddFacePoint = new PreviousOddFacePoint(0, 0, 0);


    public static void main(String[] args) {

        for (int r = 0; r < rows + 1; r++) {
            for (int c = 0; c < columns + 1; c++) {
                Point p = new Point(c * length, r * length, 0);
                pointsArray.add(p);
            }
        }
        pointsLength = pointsArray.size();
        for (int f = 0; f < faces; f++) {
            if (f % (columns * 2) == 0) {
                previousEvenFacePoint.setF(previousEvenFacePoint.getF() + 1);
                previousEvenFacePoint.setS(previousEvenFacePoint.getS() + 1);
                previousEvenFacePoint.setT(previousEvenFacePoint.getT() + 1);
                previousOddFacePoint.setF(previousOddFacePoint.getF() + 1);
                previousOddFacePoint.setS(previousOddFacePoint.getS() + 1);
                previousOddFacePoint.setT(previousOddFacePoint.getT() + 1);
            }
            if (f % 2 == 0) {
                if (f == 0) {
                    logger.info("Face: " + f + "- " + f + "," + (f + columns + 1) + "," + (f + columns + 2));
                    previousEvenFacePoint.setF(f);
                    previousEvenFacePoint.setS(f + columns + 1);
                    previousEvenFacePoint.setT(f + columns + 2);
                    facesArray.add(new Face(f,f,0,(f+columns+1),0,(f+columns+2),0));
                } else {
                    previousEvenFacePoint.setF(previousEvenFacePoint.getF() + 1);
                    previousEvenFacePoint.setS(previousEvenFacePoint.getS() + 1);
                    previousEvenFacePoint.setT(previousEvenFacePoint.getT() + 1);
                    logger.info("Face: " + f + "- " + previousEvenFacePoint.getF() + "," + previousEvenFacePoint.getS() + "," + previousEvenFacePoint.getT());
                    facesArray.add(new Face(f,previousEvenFacePoint.getF(),0,previousEvenFacePoint.getS(),0, previousEvenFacePoint.getT(),0));
                }
            } else {
                if (f == faces - 1) {
                    previousOddFacePoint.setF(previousOddFacePoint.getF() + 1);
                    previousOddFacePoint.setS(previousOddFacePoint.getS() + 1);
                    previousOddFacePoint.setT(previousOddFacePoint.getT() + 1);
                    logger.info("Face: " + f + "- " + previousOddFacePoint.getF() + "," + previousOddFacePoint.getS() + "," + previousOddFacePoint.getT());
                    facesArray.add(new Face(f,previousOddFacePoint.getF(),0,previousOddFacePoint.getS(),0, previousOddFacePoint.getT(),0));
                } else if (f == 1) {
                    previousOddFacePoint.setF(0);
                    previousOddFacePoint.setS(2 + columns);
                    previousOddFacePoint.setT(1);
                    logger.info("Face: " + f + "- " + previousOddFacePoint.getF() + "," + previousOddFacePoint.getS() + "," + previousOddFacePoint.getT());
                    facesArray.add(new Face(f,previousOddFacePoint.getF(),0,previousOddFacePoint.getS(),0,previousOddFacePoint.getT(),0));
                } else {
                    previousOddFacePoint.setF(previousOddFacePoint.getF() + 1);
                    previousOddFacePoint.setS(previousOddFacePoint.getS() + 1);
                    previousOddFacePoint.setT(previousOddFacePoint.getT() + 1);
                    logger.info("Face: " + f + "- " + previousOddFacePoint.getF() + "," + previousOddFacePoint.getS() + "," + previousOddFacePoint.getT());
                    facesArray.add(new Face(f,previousOddFacePoint.getF(),0,previousOddFacePoint.getS(),0,previousOddFacePoint.getT(),0));

                }
            }
        }
        for(Face f:facesArray){
            logger.info(f.toString());
        }

    }
    public static String meshData(int l, int c, int r) {
        return "Columns: " + c + " Rows: " + r + " Length: " + l + " Faces: " + (r * c * 2) + " Heigth: " + (r * l) + " Width: " + (c * l);
    }
}

class Point {
    float x;
    float y;
    float z;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
class Face {

    int faceNumber;

    int x;

    int xt;

    int y;

    int yt;

    int z;

    int zt;

    public Face(int faceNumber, int x, int xt, int y, int yt, int z, int zt) {
        this.faceNumber = faceNumber;
        this.x = x;
        this.xt = xt;
        this.y = y;
        this.yt = yt;
        this.z = z;
        this.zt = zt;
    }

    public Face(int x, int xt, int y, int yt, int z, int zt) {
        this.x = x;
        this.xt = xt;
        this.y = y;
        this.yt = yt;
        this.z = z;
        this.zt = zt;
    }


    public int getFaceNumber() {
        return faceNumber;
    }

    public void setFaceNumber(int faceNumber) {
        this.faceNumber = faceNumber;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getXt() {
        return xt;
    }

    public void setXt(int xt) {
        this.xt = xt;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getYt() {
        return yt;
    }

    public void setYt(int yt) {
        this.yt = yt;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getZt() {
        return zt;
    }

    public void setZt(int zt) {
        this.zt = zt;
    }

    @Override
    public String toString() {
        return "Face{" +
                "faceNumber=" + faceNumber +
                ", x=" + x +
                ", xt=" + xt +
                ", y=" + y +
                ", yt=" + yt +
                ", z=" + z +
                ", zt=" + zt +
                '}';
    }
}
class PreviousEvenFacePoint {
    int f;
    int s;
    int t;

    public PreviousEvenFacePoint(int f, int s, int t) {
        this.f = f;
        this.s = s;
        this.t = t;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "PreviousEvenFacePoint{" +
                "f=" + f +
                ", s=" + s +
                ", t=" + t +
                '}';
    }
}
class PreviousOddFacePoint {
    int f;
    int s;
    int t;

    public PreviousOddFacePoint(int f, int s, int t) {
        this.f = f;
        this.s = s;
        this.t = t;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getS() {
        return s;
    }

    public void setS(int s) {
        this.s = s;
    }

    public int getT() {
        return t;
    }

    public void setT(int t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "PreviousOddFacePoint{" +
                "f=" + f +
                ", s=" + s +
                ", t=" + t +
                '}';
    }
}
