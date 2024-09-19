package util;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Factory {

	public Text createText(String input) {
		Text text = new Text(input);
		text.setFill(Color.BLACK);
		return text;
	}

	public Label createLabel(String input) {
		Label label = new Label(input);
		label.setTextFill(Color.BLACK);
		return label;
	}
}
