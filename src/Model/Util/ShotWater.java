package Model.Util;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ShotWater implements IDrawable {

    private transient Label label;
    private boolean validShipPlacementMarker = true;

    public ShotWater() {
    }

    //IDrawable getter and setter

    @Override
    public Label getLabel() { return this.label; }

    @Override
    public void setLabel(Label label) { this.label = label; }

    @Override
    public void setValidShipPlacementMarker(boolean valid) { this.validShipPlacementMarker = valid; }

    @Override
    public boolean getValidShipPlacementMarker() { return this.validShipPlacementMarker; }

    //IDrawable methods

    @Override
    public void setLabelNonClickable() {
        if ( this.label == null) return;
        Platform.runLater(()-> this.label.setDisable(true));

    }

    @Override
    public void setLabelClickable() {
        if ( this.label == null ) return;
        Platform.runLater(()-> this.label.setDisable(false));

    }

    @Override
    public void draw(){
        if(this.label == null) return;

        Platform.runLater(() -> {
            ImageView image = new ImageView(new Image(getClass().getResourceAsStream("/Gui_View/images/waterHit.png")));

            // making ships resizeable, so they fit to the current label size
            image.fitWidthProperty().bind(label.widthProperty());
            image.fitHeightProperty().bind(label.heightProperty());
            this.label.setGraphic(image);
        });
    }

}
