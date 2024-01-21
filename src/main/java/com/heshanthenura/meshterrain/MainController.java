package com.heshanthenura.meshterrain;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableFloatArray;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Mesh;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainController implements Initializable {

    @FXML
    private AnchorPane background;
    @FXML
    private MeshView meshView;
    @FXML
    private Button viewChangeBtn;
    @FXML
    private Slider lengthSlider;
    @FXML
    private Slider colSlider;
    @FXML
    private Slider rowSlider;
    @FXML
    private Slider heightSlider;
    @FXML
    private Slider radiusSlider;
    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane controlsPane;

    TriangleMesh mesh = new TriangleMesh();
    Stage stage;
    PerspectiveCamera camera = new PerspectiveCamera();
    ArrayList<Face> facesArray = new ArrayList<>();
    int length = 100;
    int columns = 4;
    int rows = 1;
    int faces = columns * rows * 2;
    Logger logger = Logger.getLogger("info");
    PreviousEvenFacePoint previousEvenFacePoint = new PreviousEvenFacePoint(0, 0, 0);
    PreviousOddFacePoint previousOddFacePoint = new PreviousOddFacePoint(0, 0, 0);

    double mouseX = 0;
    double mouseY = 0;

    double meshRotate;
    double zoomFactor = 1.0;
    double mountainHeight = 600;
    double mountainRadius = 80;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            logger.setLevel(Level.OFF);
            background.getScene().setCamera(camera);
            stage = (Stage) background.getScene().getWindow();

            backgroundListeners();
            new Thread(() -> {
                Platform.runLater(() -> {
                    setMesh();
                });
            }).start();
            cameraMovements();

        });
    }

    public void cameraMovements() {
        background.setOnMousePressed(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });

        background.setOnMouseDragged(e -> {
            double deltaX = (e.getX() - mouseX) / 1;
            double deltaY = (e.getY() - mouseY) / 1;

            meshRotate += deltaY;

            meshView.setRotationAxis(Rotate.X_AXIS);
            meshView.setRotate(meshRotate);

            mouseX = e.getX();
            mouseY = e.getY();
        });
    }

    public void backgroundListeners() {

        lengthSlider.setValue(length);
        colSlider.setValue(columns);
        rowSlider.setValue(rows);
        heightSlider.setValue(mountainHeight);
        radiusSlider.setValue(mountainRadius);
        background.requestFocus();
        background.setOnKeyPressed(e -> {
            System.out.println("Pressed");
            if (e.getCode().getName().equals("Esc")) {
                System.exit(0);
            }
        });
        viewChangeBtn.setOnMouseClicked(e -> {
            if (meshView.getDrawMode().toString().equals("FILL")) {
                meshView.setDrawMode(DrawMode.LINE);
                viewChangeBtn.setText("FILL");
            } else {
                meshView.setDrawMode(DrawMode.FILL);
                viewChangeBtn.setText("LINE");
            }
        });
        lengthSlider.valueProperty().addListener((e) -> {
            new Thread(() -> {
                Platform.runLater(() -> {
                    length = (int) lengthSlider.getValue();
                    setMesh();
                });
            }).start();
        });
        colSlider.valueProperty().addListener((e) -> {

            new Thread(() -> {
                Platform.runLater(() -> {
                    columns = (int) colSlider.getValue();
                    setMesh();
                });
            }).start();
        });
        rowSlider.valueProperty().addListener((e) -> {
            new Thread(() -> {
                rows = (int) rowSlider.getValue();
                Platform.runLater(() -> {
                    setMesh();
                });
            }).start();
        });
        background.setOnScroll(e -> {
            double delta = e.getDeltaY();
            double scaleFactor = (delta > 0) ? 1.1 : 0.9;  // Adjust the scale factor as needed

            zoomFactor *= scaleFactor;

            // Apply the scaling to the MeshView
            meshView.setScaleX(meshView.getScaleX() * scaleFactor);
            meshView.setScaleY(meshView.getScaleY() * scaleFactor);
            meshView.setScaleZ(meshView.getScaleZ() * scaleFactor);
        });
        heightSlider.valueProperty().addListener((e) -> {
            new Thread(() -> {
                mountainHeight = (int) heightSlider.getValue();
                Platform.runLater(() -> {
                    setMesh();
                });
            }).start();
        });
        radiusSlider.valueProperty().addListener((e) -> {
            new Thread(() -> {
                mountainRadius = (int) radiusSlider.getValue();
                Platform.runLater(() -> {
                    setMesh();
                });
            }).start();
        });
    }

    public void setMesh() {
        mesh = new TriangleMesh();
        mesh.getPoints().clear();
        mesh.getTexCoords().clear();
        mesh.getFaces().clear();
        facesArray.clear();
        previousEvenFacePoint = new PreviousEvenFacePoint(0, 0, 0);
        previousOddFacePoint = new PreviousOddFacePoint(0, 0, 0);

        mesh.getTexCoords().addAll(0, 0);
        meshView.setMaterial(new PhongMaterial(Color.AQUA));
        meshView.setMesh(mesh);
        meshView.toBack();


        background.getChildren().remove(meshView);

        addPoints();
        addFaces();


        AnchorPane.setTopAnchor(meshView, (background.getHeight() - meshView.getBoundsInLocal().getHeight()) / 2);
        AnchorPane.setLeftAnchor(meshView, (background.getWidth() - meshView.getBoundsInLocal().getWidth()) / 2);


        background.getChildren().add(meshView);
        meshView.toBack();
        controlsPane.toFront();


        meshView.setRotationAxis(Rotate.X_AXIS);
        meshView.setRotate(meshRotate);


    }

    void addPoints() {
        faces = columns * rows * 2;

        double centerX = (columns * length) / 2;
        double centerY = (rows * length) / 2;



        for (int r = 0; r < rows + 1; r++) {
            for (int c = 0; c < columns + 1; c++) {
                calculateZ(centerX,centerY,c*length,r*length);
                Point p = new Point(c * length, r * length, calculateZ(centerX,centerY,c*length,r*length));
                mesh.getPoints().addAll(p.getX(), p.getY(), p.getZ());
            }
        }
    }


    void addFaces() {
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
//                    facesArray.add(new Face(f, f, 0, (f + columns + 1), 0, (f + columns + 2), 0));
                    mesh.getFaces().addAll(f, 0, (f + columns + 1), 0, (f + columns + 2), 0);
                } else {
                    previousEvenFacePoint.setF(previousEvenFacePoint.getF() + 1);
                    previousEvenFacePoint.setS(previousEvenFacePoint.getS() + 1);
                    previousEvenFacePoint.setT(previousEvenFacePoint.getT() + 1);
                    logger.info("Face: " + f + "- " + previousEvenFacePoint.getF() + "," + previousEvenFacePoint.getS() + "," + previousEvenFacePoint.getT());
//                    facesArray.add(new Face(f, previousEvenFacePoint.getF(), 0, previousEvenFacePoint.getS(), 0, previousEvenFacePoint.getT(), 0));
                    mesh.getFaces().addAll(previousEvenFacePoint.getF(), 0, previousEvenFacePoint.getS(), 0, previousEvenFacePoint.getT(), 0);

                }
            } else {
                if (f == faces - 1) {
                    previousOddFacePoint.setF(previousOddFacePoint.getF() + 1);
                    previousOddFacePoint.setS(previousOddFacePoint.getS() + 1);
                    previousOddFacePoint.setT(previousOddFacePoint.getT() + 1);
                    logger.info("Face: " + f + "- " + previousOddFacePoint.getF() + "," + previousOddFacePoint.getS() + "," + previousOddFacePoint.getT());
//                    facesArray.add(new Face(f, previousOddFacePoint.getF(), 0, previousOddFacePoint.getS(), 0, previousOddFacePoint.getT(), 0));
                    mesh.getFaces().addAll(previousOddFacePoint.getF(), 0, previousOddFacePoint.getS(), 0, previousOddFacePoint.getT(), 0);

                } else if (f == 1) {
                    previousOddFacePoint.setF(0);
                    previousOddFacePoint.setS(2 + columns);
                    previousOddFacePoint.setT(1);
                    logger.info("Face: " + f + "- " + previousOddFacePoint.getF() + "," + previousOddFacePoint.getS() + "," + previousOddFacePoint.getT());
//                    facesArray.add(new Face(f, previousOddFacePoint.getF(), 0, previousOddFacePoint.getS(), 0, previousOddFacePoint.getT(), 0));
                    mesh.getFaces().addAll(previousOddFacePoint.getF(), 0, previousOddFacePoint.getS(), 0, previousOddFacePoint.getT(), 0);

                } else {
                    previousOddFacePoint.setF(previousOddFacePoint.getF() + 1);
                    previousOddFacePoint.setS(previousOddFacePoint.getS() + 1);
                    previousOddFacePoint.setT(previousOddFacePoint.getT() + 1);
                    logger.info("Face: " + f + "- " + previousOddFacePoint.getF() + "," + previousOddFacePoint.getS() + "," + previousOddFacePoint.getT());
//                    facesArray.add(new Face(f, previousOddFacePoint.getF(), 0, previousOddFacePoint.getS(), 0, previousOddFacePoint.getT(), 0));
                    mesh.getFaces().addAll(previousOddFacePoint.getF(), 0, previousOddFacePoint.getS(), 0, previousOddFacePoint.getT(), 0);

                }
            }
        }

    }

    float calculateZ(double centerX, double centerY, double x, double y) {
        double radius = Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
        double z;
        if (radius > mountainRadius) {
            z = 0;
        } else {
            z = (mountainHeight * (1 - (Math.pow(radius, 2) / Math.pow(mountainRadius, 2))));
        }
        return (float) -z;
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
        return "Point{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
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
        return "Face{" + "faceNumber=" + faceNumber + ", x=" + x + ", xt=" + xt + ", y=" + y + ", yt=" + yt + ", z=" + z + ", zt=" + zt + '}';
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
        return "PreviousEvenFacePoint{" + "f=" + f + ", s=" + s + ", t=" + t + '}';
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
        return "PreviousOddFacePoint{" + "f=" + f + ", s=" + s + ", t=" + t + '}';
    }
}
