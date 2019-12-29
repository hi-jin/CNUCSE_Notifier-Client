package login;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.nio.charset.Charset;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	private static Socket server = null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			models.Controllers.mainController = (MainController) loader.getController();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			System.setProperty("file.encoding","UTF-8");
			Field charset = Charset.class.getDeclaredField("defaultCharset");
			charset.setAccessible(true);
			charset.set(null,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			server = new Socket("cs-cnu.tk", 50000);
		} catch (IOException e) {
			server = null;
		}
		
		Listener listener = new Listener(server);
		listener.setDaemon(true);
		listener.start();
		
		launch(args);
	}

	public static Socket getServer() {
		return server;
	}
}
