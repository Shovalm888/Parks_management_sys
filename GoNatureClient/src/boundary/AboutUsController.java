package boundary;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
/**
 * this is the about GoNature Company About information class.
 * @author group 20
 * @version 1.0
 *
 */
public class AboutUsController {
/**
 * * @param goBack this is ImageView component for goBack to main page.
 */
    @FXML
    private ImageView goBack;
/**
 * this method display Go Nature home-page.
 * @param event the mouse event.
 */
    @FXML
    void goBack(MouseEvent event) {
    	goBack.getScene().getWindow().hide();
    }

}
