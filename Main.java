package application;
	
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	private InfoCenter infoCenter;
	private TitleBoard titleBoard;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,UIConstants.APP_WIDTH,UIConstants.APP_HEIGHT);
			initlayout(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initlayout(BorderPane root) {
		initInfoCenter(root);
		initTitleBoard(root);
	}

	private void initTitleBoard(BorderPane root) {
		titleBoard = new TitleBoard(infoCenter);
		root.getChildren().add(titleBoard.getStackPane());
		
	}

	private void initInfoCenter(BorderPane root) {
		infoCenter = new InfoCenter();
		infoCenter.setStartButtonOnAction(startNewGame());
		root.getChildren().add(infoCenter.getStackPane());
		
	}
	
	private EventHandler<ActionEvent> startNewGame() {
		return new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				infoCenter.hideStartButton();
				infoCenter.updateMessage("Player X's turn");
				titleBoard.startNewGame();
			}
		};
	}

	public static void main(String[] args) {
		launch(args);
	}
}
