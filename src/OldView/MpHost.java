package OldView;

import Gui_View.HelpMethods;
import Gui_View.Main;
import Network.*;
import Player.ActiveGameState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// close client beim zurück -> AcitveGameStatet.getSclient()/Server().closeConnection(); TODO VERY IMPORTANT -> auch beim Client

public class MpHost implements Initializable {

    @FXML
    private Button backButton;
    @FXML
    private Label ipAddressLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ActiveGameState.setAmIServer(true);
        IServer server = new Server();
        ipAddressLabel.setText("getIPAddress(): " + server.getIPAddress() + "\ngetAllIPAddress(): " + java.util.Arrays.asList(server.getAllIPAddress()).toString());
        System.out.println(server.getIPAddress());
        System.out.println(java.util.Arrays.asList(server.getAllIPAddress()).toString()); //todo in label schreiben
        // todo problem bei mehrfachem aufruf -> java.net.BindException: Address already in use (Bind failed)

        // new Thread for Connecting
        Thread offerConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Connection offered - waiting for paring");
                if (server.startSeverConnection()) { //todo SockeTimoutException abfangen -> throw -> falls BackButton gedrückt, Server schließen
                    ActiveGameState.setServer(server); //todo das sonst irgendwann alter server für gameabbruch sorgt... (SP soweiso oder MP neu)
                    ActiveGameState.setRunning(true);

                    // platform run later -> sends task to GuiThread -> Gui does this as soon as this piece of code is reached
                    // -> this means: when connection is established, next window will appear - if failed: pop up
                    Platform.runLater(() -> {
                        Parent a = null;
                        try {
                            a = FXMLLoader.load(getClass().getResource("/OldView/newOrLoad.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (a != null) {
                            Main.primaryStage.setScene(new Scene(a));
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
        offerConnection.start();
    }

    public void backToLastScene() throws IOException {
        // todo shutdown server + close everything -> we need a server.terminate
        Parent mpSelect = FXMLLoader.load(getClass().getResource("/OldView/mpSelect.fxml"));
        Main.primaryStage.setScene(new Scene(mpSelect));
        Main.primaryStage.show();
    }
}