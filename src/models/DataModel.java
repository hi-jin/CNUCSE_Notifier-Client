package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel {

	public static String serverMsg = "안녕하세요. 프로그램 사용에 불편사항이 있으시면 ekwk1284@naver.com으로 연락해주세요.";
	
	public static ObservableList<String> myPostList = FXCollections.observableArrayList();
	public static ObservableList<String> myPostLinkList = FXCollections.observableArrayList();

	public static ObservableList<String> removedPostList = FXCollections.observableArrayList();
	public static ObservableList<String> removedPostLinkList = FXCollections.observableArrayList();

	public static ObservableList<String> checkedPostList = FXCollections.observableArrayList();
	public static ObservableList<String> checkedPostLinkList = FXCollections.observableArrayList();
}
