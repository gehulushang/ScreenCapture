import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main extends Application {

    ImageView iv ;
    Stage primary;
    Stage stage;
    HBox hBox;
    BufferedImage bufferedImage;

    double Screen_startY;
    double Screen_endY;
    double Screen_endX;
    double Screen_startX;
    WritableImage wi ;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primary = primaryStage;
        FileChooser fileChooser = new FileChooser();
        // fileChooser.setInitialFileName("D:/java学习/JAVA项目/截图/src");
        File initialFile = new File("D:/java学习/JAVA项目/截图/src");

        fileChooser.setInitialDirectory(initialFile);
        Button bu = new Button("点击截图");

        Button storeButton = new Button("保存图片");
        Button buView = new Button("预览");

        iv = new ImageView();
        iv.setFitWidth(400);
        iv.setPreserveRatio(true);

        AnchorPane root = new AnchorPane();

        root.getChildren().addAll(bu,iv,storeButton,buView);

        AnchorPane.setLeftAnchor(bu,50.0);
        AnchorPane.setTopAnchor(bu,50.0);
        AnchorPane.setTopAnchor(buView,80.0);
        AnchorPane.setLeftAnchor(buView,50.0);


        AnchorPane.setTopAnchor(iv,100.0);
        AnchorPane.setLeftAnchor(iv,50.0);
        AnchorPane.setLeftAnchor(storeButton,150.0);
        AnchorPane.setTopAnchor(storeButton,50.0);



        Scene scene  = new Scene(root);
        primaryStage.setTitle("截图");
        primaryStage.setScene(scene);
        primaryStage.setHeight(600);
        primaryStage.setWidth(600);
        primaryStage.show();

        buView.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {


                iv.setImage(wi);
            }
        });

        bu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                show();
            }
        });
        storeButton.setOnAction(
                (final ActionEvent e) -> {
                    DirectoryChooser directoryChooser=new DirectoryChooser();
                    File file = directoryChooser.showDialog(stage);

                    NameUtil namingUti = new NameUtil();
                    String imgName  = namingUti.getName();
                    String pathName;

                    pathName = file.getAbsolutePath()+"/"+ imgName + ".png";


                    imgName ="C:/Users/Administrator/Desktop/ScreenCapture/out/"+ imgName + ".png";
                    try{
                        ImageIO.write(bufferedImage,"png",new File(pathName));
                    }catch (IOException ev){
                        ev.printStackTrace();
                    }

                });


        KeyCombination key = KeyCombination.valueOf("ctrl + alt + p");
        Mnemonic mc = new Mnemonic(bu,key);
        scene.addMnemonic(mc);
    }

    public void show(){
        primary.setIconified(true);

        AnchorPane an = new AnchorPane();
        an.setStyle("-fx-background-color: #B5B5B522");
        Scene scene = new Scene(an);

        stage = new Stage();

        stage.initStyle(StageStyle.TRANSPARENT);

        scene.setFill(Paint.valueOf("#ffffff00"));
        stage.setFullScreenExitHint("");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();

        drag(an);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()== KeyCode.ESCAPE){
                    stage.close();
                    primary.setIconified(false);
                }
            }
        });

    }

    public void drag(AnchorPane an){
        an.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                an.getChildren().clear();
                hBox = new HBox();
                hBox.setBackground(null);

                hBox.setBorder(new Border( new BorderStroke(Paint.valueOf("#CD3700"), BorderStrokeStyle.SOLID,null,new BorderWidths(2))));

                Screen_startX = event.getSceneX();
                Screen_startY = event.getScreenY();

                an.getChildren().add(hBox);

                AnchorPane.setTopAnchor(hBox,Screen_startY);
                AnchorPane.setLeftAnchor(hBox,Screen_startX);


                //System.out.println(Screen_startX+" "+Screen_startY);
            }
        });

        an.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                an.startFullDrag();
            }
        });

        an.setOnMouseDragOver(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                Label label = new Label();
                label.setAlignment(Pos.CENTER);
                label.setPrefHeight(50);
                label.setPrefWidth(200);

                an.getChildren().add(label);

                AnchorPane.setTopAnchor(label,Screen_startY-label.getPrefHeight());
                AnchorPane.setLeftAnchor(label,Screen_startX);
                label.setTextFill(Paint.valueOf("#ffffff"));
                label.setStyle("-fx-background-color: #000000");

                double screenX = event.getScreenX();
                double screenY = event.getScreenY();

                double width = screenX - Screen_startX;
                double height = screenY - Screen_startY;

                hBox.setPrefHeight(height);
                hBox.setPrefWidth(width);

                label.setText("宽度：" + width + "  高度："+ height );
            }
        });
        an.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
            @Override
            public void handle(MouseDragEvent event) {
                Screen_endX = event.getScreenX();
                Screen_endY = event.getScreenY();

                Button button = new Button("选择完成");
                hBox.getChildren().add(button);
                hBox.setAlignment(Pos.BOTTOM_RIGHT);

                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try{
                            getScreenImg();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    public void getScreenImg() throws Exception{
        stage.close();
        double w = Screen_endX - Screen_startX;
        double h = Screen_endY - Screen_startY;

        Robot robot = new Robot();
        Rectangle rec = new Rectangle((int)Screen_startX,(int)Screen_startY,(int)w,(int)h);
        bufferedImage= robot.createScreenCapture(rec);

        wi = SwingFXUtils.toFXImage(bufferedImage,null);

        Clipboard cb = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putImage(wi);
        cb.setContent(content);

        primary.setIconified(false);

    }

    public static void main(String[] args) {
        launch(args);
    }
}