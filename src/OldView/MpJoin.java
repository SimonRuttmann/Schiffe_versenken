package OldView;

//todo let player choose between self and ki

import Gui_View.HelpMethods;

import Gui_View.Main;
import Network.Client;
import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MpJoin implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private TextField playerName;
    @FXML
    private TextField hostIP;
    @FXML
    private Button connectButton;
    @FXML
    private ProgressIndicator loadingIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ActiveGameState.setAmIServer(false);
    }

    public void backToLastScene() throws IOException {
        Parent mpSelect = FXMLLoader.load(getClass().getResource("/OldView/mpSelect.fxml"));
        Main.primaryStage.setScene(new Scene(mpSelect));
        Main.primaryStage.show();
    }

    // todo -> read from text field, make connect in bg, show spinner -> use thread:
    public void connect() throws IOException {
        ActiveGameState.setAmIServer(false);
        loadingIndicator.setVisible(true);
        connectButton.setVisible(false);
        System.out.println("connectButton pressed");

        //todo thread - schauen mit simon - wird er benötigt - wenn ja hier?
        Thread searchHost = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Searching for Host");
                //todo schauen ob korrekte ip adresse
                ActiveGameState.setClient(new Client(hostIP.getText()));
                ActiveGameState.setRunning(true);

                if (true/*ActiveGameState.isRunning()*/) {
                    // platform run later -> sends task to GuiThread -> Gui does this as soon as this piece of code is reached
                    // -> this means: when connection is established, next window will appear - if failed: pop up
                    Platform.runLater(() -> {
                        Parent chooseSelfOrKI = null;
                        try {
                            chooseSelfOrKI = FXMLLoader.load(getClass().getResource("/OldView/chooseSelfOrKi.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (chooseSelfOrKI != null) {
                            Main.primaryStage.setScene(new Scene(chooseSelfOrKI));
                            Main.primaryStage.show();
                        }
                    });
                } else {
                    System.out.println("Connection could not be established");
                    // Method reference -> "Lambda could be replaced with method reference" -> done that
                    HelpMethods.connectionFailed();
                }

            }
        });
        searchHost.start();

    }
}