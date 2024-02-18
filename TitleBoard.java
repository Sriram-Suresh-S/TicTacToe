package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class TitleBoard {
	
	private StackPane pane;
	private InfoCenter infoCenter;
	private Title[][] titles = new Title[3][3];
	private Line winningLine;
	
	private char playerTurn = 'X';
	private boolean isEndOfGame = false;
	
	public TitleBoard(InfoCenter infoCenter) {
		this.infoCenter = infoCenter;
		pane = new StackPane();
		pane.setMinSize(UIConstants.APP_WIDTH,UIConstants.TITLE_BOARD_HEIGHT);
		pane.setTranslateX(UIConstants.APP_WIDTH / 2);
		pane.setTranslateY((UIConstants.TITLE_BOARD_HEIGHT / 2) + UIConstants.INFO_CENTRE_HEIGHT);
		
		addallTitles();
		
		winningLine = new Line();
		pane.getChildren().add(winningLine);
	}
	
	private void addallTitles() {
		for(int row = 0; row < 3; row++) {
			for(int col =0; col <3;col++) {
				Title title = new Title();
				title.getStackPane().setTranslateX((col * 100) - 100);
				title.getStackPane().setTranslateY((row * 100) - 100);
				pane.getChildren().add(title.getStackPane());
				titles[row][col] = title;
			}
		}	
	}
	
	public void startNewGame() {
		isEndOfGame = false;
		playerTurn = 'X';
		for(int row = 0; row < 3; row++) {
			for(int col =0; col <3;col++) {
				titles[row][col].setValue("");
			}
		}
		winningLine.setVisible(false);
	}
	
	public void changePlayerTurn( ) {
		if (playerTurn == 'X') {
			playerTurn = 'O';
		}else {
			playerTurn = 'X';
		}
		infoCenter.updateMessage("Player" + playerTurn + "'s turn");
	}
	
	public String getPlayerTurn() {
		return String.valueOf(playerTurn);
	}

	public StackPane getStackPane() {
		return pane;
	}
	
	public void checkForWinner() {
		checkRowsForWinner();
		checkColsForWinner();
		checkTopLeftToBottomRightForWinner();
		checkTopRightToBottomLeftForWinner();
		checkForStalmate();
	}
	
	private void checkRowsForWinner() {
		for(int row =0;row<3;row++) {
			if (titles[row][0].getValue().equals(titles[row][1].getValue()) && 
					titles[row][0].getValue().equals(titles[row][2].getValue()) &&
					!titles[row][0].getValue().isEmpty()) {
				String winner = titles[row][0].getValue();
				endGame(winner,new WinningTitles(titles[row][0], titles[row][1],titles[row][2]));
				return;
			}
		}
		
	}

	private void checkColsForWinner() {
		if(!isEndOfGame) {
		for(int col =0;col<3;col++) {
			if (titles[0][col].getValue().equals(titles[1][col].getValue()) && 
					titles[0][col].getValue().equals(titles[2][col].getValue()) &&
					!titles[0][col].getValue().isEmpty()) {
				String winner = titles[0][col].getValue();
				endGame(winner,new WinningTitles(titles[0][col], titles[1][col],titles[2][col]));
				return;
			}
		}
	}
		
	}

	private void checkTopLeftToBottomRightForWinner() {
		if (!isEndOfGame) {
			if(titles[0][0].getValue().equals(titles[1][1].getValue()) && 
					titles[0][0].getValue().equals(titles[2][2].getValue()) && 
					!titles[0][0].getValue().isEmpty()) {
				String winner = titles[0][0].getValue();
				endGame(winner,new WinningTitles(titles[0][0], titles[1][1],titles[2][2]));
				return;
			}
		}
		
	}

	private void checkTopRightToBottomLeftForWinner() {
		if (!isEndOfGame) {
			if(titles[0][2].getValue().equals(titles[1][1].getValue()) && 
					titles[0][2].getValue().equals(titles[2][0].getValue()) && 
					!titles[0][2].getValue().isEmpty()) {
				String winner = titles[0][2].getValue();
				endGame(winner,new WinningTitles(titles[0][2], titles[1][1],titles[2][0]));
				return;
			}
		}
		
	}

	private void checkForStalmate() {
		if (!isEndOfGame) {
			for(int row =0 ;row<3; row++) {
				for(int col =0;col<3;col++) {
					if(titles[row][col].getValue().isEmpty()) {
						return;
					}
				}
			}
			
			isEndOfGame = true;
			infoCenter.updateMessage("Stalemate...");
			infoCenter.showStartButton();
		}
		
	}
	
	private void endGame(String winner,WinningTitles winningTitles) {
		isEndOfGame = true;
		drawWinningLine(winningTitles);
		infoCenter.updateMessage("Player "+ winner +" wins!");
		infoCenter.showStartButton();
	}
	
	private void drawWinningLine(WinningTitles winningTitles) {
		winningLine.setStartX(winningTitles.start.getStackPane().getTranslateX());
		winningLine.setStartY(winningTitles.start.getStackPane().getTranslateY());
		winningLine.setEndX(winningTitles.end.getStackPane().getTranslateX());
		winningLine.setEndY(winningTitles.end.getStackPane().getTranslateY());
		winningLine.setTranslateX(winningTitles.middle.getStackPane().getTranslateX());
		winningLine.setTranslateY(winningTitles.middle.getStackPane().getTranslateY());
		winningLine.setVisible(true);
	}

	private class WinningTitles {
		Title start;
		Title middle;
		Title end;
		
		public WinningTitles(Title start,Title middle,Title end) {
			this.start = start;
			this.middle = middle;
			this.end = end;
		}
	}
	
	private class Title {
		
		private StackPane pane;
		private Label label;
		
		public Title() {
			pane = new StackPane();
			pane.setMinSize(100, 100);
			
			Rectangle border = new Rectangle();
			border.setWidth(100);
			border.setHeight(100);
			border.setFill(Color.TRANSPARENT);
			border.setStroke(Color.BLACK);
			pane.getChildren().add(border);
			
			label = new Label("");
			label.setAlignment(Pos.CENTER);
			label.setFont(Font.font(24));
			pane.getChildren().add(label);
			
			pane.setOnMouseClicked(event -> {
				if (label.getText().isEmpty() && !isEndOfGame) {
					label.setText(getPlayerTurn());
					changePlayerTurn();
					checkForWinner();
				}
			});
		}
		
		
		public StackPane getStackPane() {
			return pane;
		}
		
		public String getValue() {
			return label.getText();
		}
		
		public void setValue(String value) {
			label.setText(value);
		}
	}
}
