package login;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;

public class Listener extends Thread {

	Socket server;
	
	static int isLogined = 0;
	
	public Listener(Socket server) {
		this.server = server;
	}
	
	@Override
	public void run() {
		String line;
		String[] command;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
			String msg = "";
			while(true) {
				line = in.readLine();
				command = line.split("/#/");
				switch(command[0]) {
				case "addUser":
					if(command[1].equals("1")) {
						msg = "회원가입 완료";
					} else {
						msg = "회원가입 실패";
					}
					break;
				case "login":
					if(command[1].equals("1")) {
						msg = "로그인 완료";
						isLogined = 1;
					} else {
						msg = "로그인 실패";
						isLogined = 0;
					}
					break;
				case "getMyPostList":
					Platform.runLater(() -> {
						models.DataModel.myPostList.clear();
						models.DataModel.myPostLinkList.clear();
					});
					for(int i = 1; i < command.length; i+=2) {
//						TODO 이 방법 말고는 없나?
						final String post = command[i];
						final String postLink = command[i+1];
						Platform.runLater(() -> {
							models.DataModel.myPostList.add(post);
							models.DataModel.myPostLinkList.add(postLink);
						});
					}
					break;
				case "getRemovedPostList":
					Platform.runLater(() -> {
						models.DataModel.removedPostList.clear();
						models.DataModel.removedPostLinkList.clear();
						});
					for(int i = 1; i < command.length; i+=2) {
						final String post = command[i];
						final String postLink = command[i+1];
						Platform.runLater(() -> {
							models.DataModel.removedPostList.add(post);
							models.DataModel.removedPostLinkList.add(postLink);
							});
					}
					break;
				case "getCheckedPostList":
					Platform.runLater(() -> {
						models.DataModel.checkedPostList.clear();
						models.DataModel.checkedPostLinkList.clear();
						});
					for(int i = 1; i < command.length; i+=2) {
						final String post = command[i];
						final String postLink = command[i+1];
						Platform.runLater(() -> {
							models.DataModel.checkedPostList.add(post);
							models.DataModel.checkedPostLinkList.add(postLink);
							});
					}
					break;
				case "refresh":
					PrintWriter out = new PrintWriter(new BufferedOutputStream(Main.getServer().getOutputStream()));
					out.println("getMyPostList");
					out.println("getRemovedPostList");
					out.println("getCheckedPostList");
					out.flush();
				}
				models.DataModel.serverMsg = msg;
				Platform.runLater(() -> {
					if(models.Controllers.mainController != null) models.Controllers.mainController.getServerMsgLabel().setText(models.DataModel.serverMsg);
					if(models.Controllers.mainPageController != null) models.Controllers.mainPageController.getServerMsgLabel().setText(models.DataModel.serverMsg);
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
