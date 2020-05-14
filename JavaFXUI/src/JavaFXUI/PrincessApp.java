package JavaFXUI;

import JavaFXUI.Board.BoardController;
import JavaFXUI.RightInfo.RightController;
import JavaFXUI.TopInfo.TopController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.net.URL;

public class PrincessApp extends Application {

    private TopController topController;
    private RightController rightController;
    private BoardController boardController;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("topInfo.fxml");
        fxmlLoader.setLocation(url);
        Parent topInfo = fxmlLoader.load(url.openStream());
        topController = fxmlLoader.getController();
        topController.setModel(model);
        topController.setCheckBox();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("rightInfo.fxml");
        fxmlLoader.setLocation(url);
        Parent rightInfo = fxmlLoader.load(url.openStream());
        rightController = fxmlLoader.getController();
        rightController.setModel(model);
        rightController.initRight();

        fxmlLoader = new FXMLLoader();
        url = getClass().getResource("Board.fxml");
        fxmlLoader.setLocation(url);
        ScrollPane board = fxmlLoader.load(url.openStream());
        boardController = fxmlLoader.getController();
        boardController.setModel(model);
        boardController.initBoard();

        BorderPane main = new BorderPane();
        main.setTop(topInfo);
        main.setRight(rightInfo);
        main.setCenter(board);
        Scene scene = new Scene(main, 1200.0D, 680.0D);
        model.getStyleChooserProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue)
            {
                scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
                if(scene.getStylesheets().contains(getClass().getResource(model.getCSS()).toExternalForm()))
                {
                    scene.getStylesheets().remove(0,scene.getStylesheets().size()-1);
                    scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());
                }
            }

        });
        scene.getStylesheets().add(getClass().getResource(model.getCSS()).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setMinHeight(640.0D);
        primaryStage.setMinWidth(1090.0D);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
