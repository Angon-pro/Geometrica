package com.angon;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PrimaryController implements Initializable {

    private static double anchorLabelLayoutY = 107.0;
    private static double anchorTFLayoutY = 102.0;
    private static double anchorButtonLayoutY = 102.0;
    private static double anchorAddButtonLayoutY = 146.0;
    private static double anchorClearButtonLayoutY = 190.0;
    private static final double ANCHOR_LABEL_LAYOUT_X = 20.0;
    private static final double X_ANCHOR_TF_LAYOUT_X = 52.0;
    private static final double Y_ANCHOR_TF_LAYOUT_X = 153.0;
    private static final double ANCHOR_BUTTON_LAYOUT_X = 254.0;
    private static final double FONT_SIZE = 15.0;
    private static final double PREF_TF_HEIGHT = 31.0;
    private static final double PREF_TF_WIDTH = 85.0;

    private static final double SINGLE_SCROLL = 0.02791666666666665;
    private static int zooms = 0;
    private final AnimatedZoomOperator zoomOperator = new AnimatedZoomOperator();
    private static final double MAX_VARIATION = 290.0;
    private static int counter = 3;
    private final ArrayList<Point> points = new ArrayList<>();

    @FXML
    private ScrollPane scroll_pane_canvas;

    @FXML
    private Canvas canvas;

    @FXML
    private ScrollPane scroll_pane_data;

    @FXML
    private AnchorPane anchor_pane_data;

    @FXML
    private Button add_data_button;

    @FXML
    private Button clear_data_button;

    @FXML
    private TextField text_field_x1;

    @FXML
    private TextField text_field_y1;

    @FXML
    private TextField text_field_x2;

    @FXML
    private TextField text_field_y2;

    @FXML
    private TextField text_field_x3;

    @FXML
    private TextField text_field_y3;

    @FXML
    private Button delete_data_button1;

    @FXML
    private Button delete_data_button2;

    @FXML
    private Button delete_data_button3;

    @FXML
    private TextField text_field_square;

    @FXML
    private TextField text_field_perimeter;

    @FXML
    public void clickOnAddButton() {
        counter++;
        anchorTFLayoutY += 44;
        anchorLabelLayoutY += 44;
        anchorButtonLayoutY += 44;
        anchorAddButtonLayoutY += 44;
        anchorClearButtonLayoutY += 44;

        String xTextFieldId = "text_field_x" + counter;
        String yTextFieldId = "text_field_y" + counter;
        String labelId = "label_p" + counter;
        String buttonId = "delete_data_button" + counter;

        Label label = new Label("P" + counter);
        label.setId(labelId);
        label.setLayoutX(ANCHOR_LABEL_LAYOUT_X);
        label.setLayoutY(anchorLabelLayoutY);
        label.setFont(Font.font(FONT_SIZE));

        TextField xTextField = new TextField("0");
        xTextField.setId(xTextFieldId);
        xTextField.setLayoutX(X_ANCHOR_TF_LAYOUT_X);
        xTextField.setLayoutY(anchorTFLayoutY);
        xTextField.setPrefHeight(PREF_TF_HEIGHT);
        xTextField.setPrefWidth(PREF_TF_WIDTH);
        xTextField.setPromptText("x");
        xTextField.setFont(Font.font(FONT_SIZE));
        selectAllOnClick(xTextField);
        setRestrictions(xTextField);
        setOnEditUpdater(xTextField);

        TextField yTextField = new TextField("0");
        yTextField.setId(yTextFieldId);
        yTextField.setLayoutX(Y_ANCHOR_TF_LAYOUT_X);
        yTextField.setLayoutY(anchorTFLayoutY);
        yTextField.setPrefHeight(PREF_TF_HEIGHT);
        yTextField.setPrefWidth(PREF_TF_WIDTH);
        yTextField.setPromptText("y");
        yTextField.setFont(Font.font(FONT_SIZE));
        selectAllOnClick(yTextField);
        setRestrictions(yTextField);
        setOnEditUpdater(yTextField);

        Button deleteButton = new Button("Delete");
        deleteButton.setId(buttonId);
        deleteButton.setLayoutX(ANCHOR_BUTTON_LAYOUT_X);
        deleteButton.setLayoutY(anchorButtonLayoutY);
        deleteButton.setFont(Font.font(FONT_SIZE));
        setOnDeleteClickListener(deleteButton);
        if (counter > 4) anchor_pane_data.lookup("#delete_data_button" + (counter - 1)).setDisable(true);

        anchor_pane_data.getChildren().add(label);
        anchor_pane_data.getChildren().add(xTextField);
        anchor_pane_data.getChildren().add(yTextField);
        anchor_pane_data.getChildren().add(deleteButton);

        add_data_button.setLayoutY(anchorAddButtonLayoutY);
        clear_data_button.setLayoutY(anchorClearButtonLayoutY);
        scroll_pane_data.setVvalue(scroll_pane_data.getVmax());
        addPoint();
        text_field_square.setText(String.format("%.3f", calculateSquare(points)));
        text_field_perimeter.setText(String.format("%.3f", calculatePerimeter(points)));
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        redraw(graphicsContext);
    }

    @FXML
    public void clickOnClearButton() {
        if (counter > 3) {
            for (int i = counter; i > 3; i--) {
                anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#text_field_x" + i));
                anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#text_field_y" + i));
                anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#label_p" + i));
                anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#delete_data_button" + i));
                counter--;
                anchorTFLayoutY -= 44;
                anchorLabelLayoutY -= 44;
                anchorButtonLayoutY -= 44;
                anchorAddButtonLayoutY -= 44;
                anchorClearButtonLayoutY -= 44;
                add_data_button.setLayoutY(anchorAddButtonLayoutY);
                clear_data_button.setLayoutY(anchorClearButtonLayoutY);
            }
        }
        for (int i = 1; i <= 3; i++) {
            text_field_x1.setText("0");
            text_field_x2.setText("0");
            text_field_x3.setText("0");
            text_field_y1.setText("0");
            text_field_y2.setText("0");
            text_field_y3.setText("0");
        }
        initializePoints();
        text_field_square.setText(String.format("%.3f", calculateSquare(points)));
        text_field_perimeter.setText(String.format("%.3f", calculatePerimeter(points)));
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        redraw(graphicsContext);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scroll_pane_canvas.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll_pane_canvas.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll_pane_canvas.setPannable(true);
        scroll_pane_canvas.setHvalue(0.5);
        scroll_pane_canvas.setVvalue(0.5);
        disableScroll(scroll_pane_canvas);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        redraw(graphicsContext);
        setOnScrollZoom(canvas);
        scroll_pane_data.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        selectAllOnClick(text_field_x1);
        selectAllOnClick(text_field_x2);
        selectAllOnClick(text_field_x3);
        setRestrictions(text_field_x1);
        setRestrictions(text_field_x2);
        setRestrictions(text_field_x3);
        setOnEditUpdater(text_field_x1);
        setOnEditUpdater(text_field_x2);
        setOnEditUpdater(text_field_x3);
        selectAllOnClick(text_field_y1);
        selectAllOnClick(text_field_y2);
        selectAllOnClick(text_field_y3);
        setRestrictions(text_field_y1);
        setRestrictions(text_field_y2);
        setRestrictions(text_field_y3);
        setOnEditUpdater(text_field_y1);
        setOnEditUpdater(text_field_y2);
        setOnEditUpdater(text_field_y3);
        delete_data_button1.setDisable(true);
        delete_data_button2.setDisable(true);
        delete_data_button3.setDisable(true);
        selectAllOnClick(text_field_square);
        selectAllOnClick(text_field_perimeter);
        text_field_square.setEditable(false);
        text_field_perimeter.setEditable(false);
        initializePoints();
        text_field_square.setText(String.format("%.3f", calculateSquare(points)));
        text_field_perimeter.setText(String.format("%.3f", calculatePerimeter(points)));
    }

    private void selectAllOnClick(TextField textField) {
        textField.setOnMouseClicked(mouseEvent -> textField.selectAll());
    }

    private void setRestrictions(TextField textField) {
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (textField.getText().equals("")) {
                        textField.setText("0");
                    }
                    if (!newValue.matches("-?[0-9]*[.]?[0-9]{0,3}")) {
                        textField.setText(oldValue);
                    }
                    if (newValue.matches("-?[0-9]*[.]?[0-9]{0,3}")) {
                        if (!textField.getText().equals("-")) {
                            try {
                                double number = Double.parseDouble(newValue);
                                if ((number > 100000) || (number < -100000)) {
                                    textField.setText(oldValue);
                                }
                            } catch (NumberFormatException exception) {
                                System.out.println(exception.getMessage() + " at setRestrictions()");
                            }
                        }
                    }
                });
    }

    private void setOnEditUpdater(TextField textField) {
        String id = textField.getId();
        textField.textProperty().addListener(observable -> {
            String numberStr = "";
            char c1 = id.charAt(id.length() - 1);
            char c2 = id.charAt(id.length() - 2);
            char c3 = id.charAt(id.length() - 3);
            char c4 = id.charAt(id.length() - 4);
            if ((c3 != '_') && (c3 != 'x') && (c3 != 'y')) numberStr += c3;
            if ((c2 != 'x') && (c2 != 'y')) numberStr += c2;
            numberStr += c1;
            int number = Integer.parseInt(numberStr);
            boolean isX = (c4 == 'x') || (c3 == 'x') || (c2 == 'x');
            if (!textField.getText().equals("-")) {
                try {
                    if (isX) {
                        points.get(number - 1).setX(Double.parseDouble(textField.getText()));
                    } else {
                        points.get(number - 1).setY(Double.parseDouble(textField.getText()));
                    }
                    text_field_square.setText(String.format("%.3f", calculateSquare(points)));
                    text_field_perimeter.setText(String.format("%.3f", calculatePerimeter(points)));
                    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                    redraw(graphicsContext);
                } catch (NumberFormatException exception) {
                    System.out.println(exception.getMessage() + " at setOnEditPointUpdater()");
                }
            }
        });
    }

    private void setOnDeleteClickListener(Button button) {
        button.setOnAction((ActionEvent actionEvent) -> {
            int number = counter;
            anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#label_p" + number));
            anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#text_field_x" + number));
            anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#text_field_y" + number));
            anchor_pane_data.getChildren().remove(anchor_pane_data.lookup("#delete_data_button" + number));
            counter--;
            anchorTFLayoutY -= 44;
            anchorLabelLayoutY -= 44;
            anchorButtonLayoutY -= 44;
            anchorAddButtonLayoutY -= 44;
            anchorClearButtonLayoutY -= 44;
            add_data_button.setLayoutY(anchorAddButtonLayoutY);
            clear_data_button.setLayoutY(anchorClearButtonLayoutY);
            anchor_pane_data.lookup("#text_field_x" + (number - 1)).requestFocus();
            if (counter > 3) anchor_pane_data.lookup("#delete_data_button" + (number - 1)).setDisable(false);
            points.remove(number - 1);
            text_field_square.setText(String.format("%.3f", calculateSquare(points)));
            text_field_perimeter.setText(String.format("%.3f", calculatePerimeter(points)));
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            redraw(graphicsContext);
        });
    }

    private void initializePoints() {
        points.clear();
        for (int i = 0; i < counter; i++) {
            Point point = new Point();
            TextField xTextField = (TextField) anchor_pane_data.getChildren().get(4 * i + 1);
            TextField yTextField = (TextField) anchor_pane_data.getChildren().get(4 * i + 2);
            point.setX(Double.parseDouble(xTextField.getText()));
            point.setY(Double.parseDouble(yTextField.getText()));
            points.add(point);
        }
    }

    private void addPoint() {
        Point point = new Point();
        TextField xTextField = (TextField) anchor_pane_data.getChildren().get(4 * (counter - 1) + 3);
        TextField yTextField = (TextField) anchor_pane_data.getChildren().get(4 * (counter - 1) + 4);
        point.setX(Double.parseDouble(xTextField.getText()));
        point.setY(Double.parseDouble(yTextField.getText()));
        points.add(point);
    }

    private double calculateSquare(ArrayList<Point> points) {
        double square = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            square += points.get(i).getX() * points.get(i + 1).getY();
            square -= points.get(i).getY() * points.get(i + 1).getX();
        }
        square += points.get(points.size() - 1).getX() * points.get(0).getY();
        square -= points.get(points.size() - 1).getY() * points.get(0).getX();
        square = Math.abs(square);
        square /= 2;
        return square;
    }

    private double calculatePerimeter(ArrayList<Point> points) {
        double perimeter = 0;
        double detX, detY;
        double side;
        for (int i = 0; i < points.size() - 1; i++) {
            detX = points.get(i + 1).getX() - points.get(i).getX();
            detY = points.get(i + 1).getY() - points.get(i).getY();
            side = Math.sqrt(Math.pow(detX, 2) + Math.pow(detY, 2));
            perimeter += side;
        }
        detX = points.get(0).getX() - points.get(points.size() - 1).getX();
        detY = points.get(0).getY() - points.get(points.size() - 1).getY();
        side = Math.sqrt(Math.pow(detX, 2) + Math.pow(detY, 2));
        perimeter += side;
        return perimeter;
    }

    private void drawAxes(GraphicsContext graphicsContext) {
        graphicsContext.setStroke(Color.GRAY);
        graphicsContext.strokeLine(canvas.getWidth() / 2, 0, canvas.getWidth() / 2, canvas.getHeight());
        graphicsContext.strokeLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2);
    }

    private void drawShape(GraphicsContext graphicsContext, ArrayList<Point> points) {
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.setLineWidth(2.0);
        double maxValue = 0;
        for (Point point : points) {
            if (Math.abs(point.getX()) > maxValue) maxValue = Math.abs(point.getX());
            if (Math.abs(point.getY()) > maxValue) maxValue = Math.abs(point.getY());
        }
        double[] xs = new double[points.size()];
        double[] ys = new double[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xs[i] = points.get(i).getX() * MAX_VARIATION / maxValue + 960;
            ys[i] = -points.get(i).getY() * MAX_VARIATION / maxValue + 960;
        }
        graphicsContext.strokePolygon(xs, ys, points.size());
    }

    private void redraw(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawAxes(graphicsContext);
        drawShape(graphicsContext, points);
    }

    private void setOnScrollZoom(Canvas canvas) {
        canvas.setOnScroll(scrollEvent -> {
            double zoomFactor = 1.5;
            if (scrollEvent.getDeltaY() <= 0) zoomFactor = 1 / zoomFactor;
            if (((zooms > 0) && (scrollEvent.getDeltaY() <= 0)) || ((zooms < 5) && (scrollEvent.getDeltaY() > 0)))
                zoomOperator.zoom(canvas, zoomFactor, scrollEvent.getSceneX(), scrollEvent.getSceneY());
            if ((scrollEvent.getDeltaY() > 0) && (zooms < 5)) zooms++;
            if ((scrollEvent.getDeltaY() <= 0) && (zooms > 0)) zooms--;
        });
    }

    private void disableScroll(ScrollPane scrollPane) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, scrollEvent -> {
            if (scrollEvent.getDeltaY() > 0) {
                scrollPane.setVvalue(scrollPane.getVvalue() + SINGLE_SCROLL);
            } else {
                scrollPane.setVvalue(scrollPane.getVvalue() - SINGLE_SCROLL);
            }
        });
    }
}
