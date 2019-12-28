package main;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import login.Main;
import javafx.scene.image.ImageView;

public class MainPageController implements Initializable {

	@FXML Label serverMsgLabel;
	@FXML Label saveLabel;
	@FXML Label removeLabel;
	@FXML ListView<String> noticeListView;
	@FXML ImageView homeImage;
	@FXML ImageView bookImage;
	@FXML ImageView trashImage;
	
	private ObservableList<String> currentPostList;
	private ObservableList<String> currentPostLinkList;
	
	PrintWriter out = null;
	
	public Label getServerMsgLabel() {
		return serverMsgLabel;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			out = new PrintWriter(new BufferedOutputStream(Main.getServer().getOutputStream()));
			out.println("getMyPostList");
			out.println("getRemovedPostList");
			out.println("getCheckedPostList");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		serverMsgLabel.setWrapText(true);
		models.DataModel.serverMsg = "더블클릭하면 홈페이지로 이동됩니다.";
		serverMsgLabel.setText(models.DataModel.serverMsg);
		
		saveLabel.setOnMouseClicked(event -> {
			int selectedIndex;
			if((selectedIndex = noticeListView.getSelectionModel().getSelectedIndex()) < 0) {
				models.DataModel.serverMsg = "저장할 게시글을 선택하세요.";
				serverMsgLabel.setText(models.DataModel.serverMsg);
			} else {
				out.println("addCheckedPost/#/" + currentPostList.get(selectedIndex) + "/#/" + currentPostLinkList.get(selectedIndex));
				out.flush();
				if(currentPostLinkList.equals(models.DataModel.myPostLinkList)) {
					models.DataModel.checkedPostList.add(models.DataModel.myPostList.remove(selectedIndex));
					models.DataModel.checkedPostLinkList.add(models.DataModel.myPostLinkList.remove(selectedIndex));
				} else if(currentPostLinkList.equals(models.DataModel.removedPostLinkList)) {
					models.DataModel.checkedPostList.add(models.DataModel.removedPostList.remove(selectedIndex));
					models.DataModel.checkedPostLinkList.add(models.DataModel.removedPostLinkList.remove(selectedIndex));
				}
			}
		});
		
		removeLabel.setOnMouseClicked(event -> {
			int selectedIndex;
			if((selectedIndex = noticeListView.getSelectionModel().getSelectedIndex()) < 0) {
				models.DataModel.serverMsg = "삭제할 게시글을 선택하세요.";
				serverMsgLabel.setText(models.DataModel.serverMsg);
			} else {
				out.println("removePost/#/" + currentPostList.get(selectedIndex) + "/#/" + currentPostLinkList.get(selectedIndex));
				out.flush();
				if(currentPostLinkList.equals(models.DataModel.myPostLinkList)) {
					models.DataModel.removedPostList.add(models.DataModel.myPostList.remove(selectedIndex));
					models.DataModel.removedPostLinkList.add(models.DataModel.myPostLinkList.remove(selectedIndex));
				} else if(currentPostLinkList.equals(models.DataModel.checkedPostLinkList)) {
					models.DataModel.removedPostList.add(models.DataModel.checkedPostList.remove(selectedIndex));
					models.DataModel.removedPostLinkList.add(models.DataModel.checkedPostLinkList.remove(selectedIndex));
				}
			}
		});
		
		homeImage.visibleProperty().set(false);
		noticeListView.setItems(models.DataModel.myPostList);
		noticeListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> param) {
				XCell cell = new XCell();
				cell.prefWidthProperty().bind(noticeListView.widthProperty().subtract(20));
				cell.setMaxWidth(Control.USE_PREF_SIZE);
				cell.setOnMouseClicked(event -> {
					if(event.getClickCount() >= 2) {
						try {
							Desktop.getDesktop().browse(new URL(currentPostLinkList.get(cell.getIndex())).toURI());
						} catch (IOException | URISyntaxException e) {
							e.printStackTrace();
						}
					}
				});
				return cell;
			}
		});
		noticeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				serverMsgLabel.setText(newValue);
			}
		});
		
		currentPostList = models.DataModel.myPostList;
		currentPostLinkList = models.DataModel.myPostLinkList;
		
		homeImage.setOnMouseClicked(event -> {
			currentPostList = models.DataModel.myPostList;
			currentPostLinkList = models.DataModel.myPostLinkList;
			noticeListView.setItems(models.DataModel.myPostList);
			homeImage.visibleProperty().set(false);
			bookImage.visibleProperty().set(true);
			trashImage.visibleProperty().set(true);
		});
		
		bookImage.setOnMouseClicked(event -> {
			currentPostList = models.DataModel.checkedPostList;
			currentPostLinkList = models.DataModel.checkedPostLinkList;
			noticeListView.setItems(models.DataModel.checkedPostList);
			homeImage.visibleProperty().set(true);
			bookImage.visibleProperty().set(false);
			trashImage.visibleProperty().set(true);
		});
		
		trashImage.setOnMouseClicked(event -> {
			currentPostList = models.DataModel.removedPostList;
			currentPostLinkList = models.DataModel.removedPostLinkList;
			noticeListView.setItems(models.DataModel.removedPostList);
			homeImage.visibleProperty().set(true);
			bookImage.visibleProperty().set(true);
			trashImage.visibleProperty().set(false);
		});
	}
	
	static class XCell extends ListCell<String> {
		HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
//        Button linkButton = new Button("link");
        String lastItem;
        
        public XCell() {
            super();
            hbox.getChildren().addAll(label, pane);
//            hbox.getChildren().addAll(label, pane, linkButton);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);  // No text in label of super class
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item!=null ? item : "<null>");
                setGraphic(hbox);
            }
        }
	}
}
