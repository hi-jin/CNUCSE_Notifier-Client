package login;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.MainPageController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController implements Initializable {
	
	@FXML Label serverMsgLabel;
	@FXML TextField loginTextField;
	@FXML Button loginButton;
	@FXML TextField addUserTextField;
	@FXML Button addUserButton;
	
	@FXML public void login() {
		if(loginTextField.getText().equals("") || loginTextField.getText() == null) {
			serverMsgLabel.setText("아이디를 입력해주세요.");
			return;
		}
		try {
			PrintWriter out = new PrintWriter(new BufferedOutputStream(Main.getServer().getOutputStream()));
			out.println("login/#/" + loginTextField.getText());
			out.flush();
			Thread.sleep(1000);
			if(Listener.isLogined == 1) {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("../main/MainPage.fxml"));
				models.Controllers.mainPageController = (MainPageController) loader.getController();
				Parent login = loader.load();
				Scene scene = new Scene(login);
			    Stage primaryStage = (Stage)loginButton.getScene().getWindow(); // 현재 윈도우 가져오기
			    primaryStage.setScene(scene);
			}
			loginTextField.setText("");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@FXML public void addUser() {
		if(addUserTextField.getText().equals("") || addUserTextField.getText() == null) {
			return;
		}
		try {
			PrintWriter out = new PrintWriter(new BufferedOutputStream(Main.getServer().getOutputStream()));
			out.println("addUser/#/" + addUserTextField.getText());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		addUserTextField.setText("");
	}
	
	public Label getServerMsgLabel() {
		return serverMsgLabel;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serverMsgLabel.setWrapText(true);
		serverMsgLabel.setText(models.DataModel.serverMsg);
		if(Main.getServer() == null) {
			loginButton.setDisable(true);
			addUserButton.setDisable(true);
			models.DataModel.serverMsg = "서버와의 연결이 끊어졌습니다. 다시 실행해주세요. 문제가 지속된다면 관리자에게 문의해주세요.";
			serverMsgLabel.setText(models.DataModel.serverMsg);
		}
	}
}
