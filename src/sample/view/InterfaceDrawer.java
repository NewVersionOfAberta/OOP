package sample.view;

import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

public class InterfaceDrawer {
    public void DrawTextfields(ArrayList<TextField> textFields, AnchorPane parentPane){

        final boolean b = parentPane.getChildren().addAll(textFields);
    }
}
