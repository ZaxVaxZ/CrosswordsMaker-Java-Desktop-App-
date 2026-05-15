package crosswordsconverter.view;

import java.io.File;
import java.util.Optional;

import crosswordsconverter.control.Controller;
import crosswordsconverter.structures.ReqID;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AppGUI extends Application {
//    ObservableList<FileDetails> data = FXCollections.observableArrayList();
//    ObservableList<RecievedFile> reData = FXCollections.observableArrayList();
	final int WINDOW_WIDTH = 1050;
	final int WINDOW_HEIGHT = 650;
	int TABLE_WIDTH = 10;
	int TABLE_HEIGHT = 10;
	int word_len;
	String path = "";
	int curr_row = -1, curr_col = -1, curr_word = -1;
	boolean draggingOnTable = false, erasing = false, unsaved_changes=false;
	Label table[][];
	Label item_label;
	boolean blacked[][];
	Button btn;
	int y;
	Scene main_scene;
	TreeView tv;
	TreeItem horizontal_node;
	TreeItem vertical_node;
	TreeItem horizontal[];
	TreeItem vertical[];
	TreeItem words[][][];
	boolean bool_horizontal[][];
	boolean bool_vertical[][];
	String text_horizontal[][][];
	String text_vertical[][][];
	TextField tf_horizontal[][];
	TextField tf_vertical[][];
	TextArea ta_horizontal[][][];
	TextArea ta_vertical[][][];
	CheckBox reverse_cb_h[][];
	CheckBox reverse_cb_v[][];
	Label labels_t[];
	Label labels_r[];
	Stage window;
	TextField width_tf, height_tf;
    @Override
    public void start(Stage primaryStage) throws Exception {
    	try {
        //*************************** Main Window Setup ********************************//
    	window = primaryStage;
    	window.centerOnScreen();
    	window.setOnCloseRequest((WindowEvent event) -> {try{handleFileExit(event);}catch(Exception e){showAlert(e.getMessage(), true);}});
        //*************************** Window Elements Setup *************************//
    	Init(true);
    	//*************************** Show Window **********************************//
    	window.show();
    	} catch(Exception e) {
    		showAlert(e.getMessage(), false);
    	}
    }
    public static void main(String[] args) {
        launch(args);
    }    
	private void showAlert(String text, boolean warning) {
		Alert alert;
		if(warning) {
			alert = new Alert(Alert.AlertType.WARNING);
	    	alert.setTitle("Warning");
		}
		else {
			alert = new Alert(Alert.AlertType.ERROR);
	    	alert.setTitle("Error");
	    	alert.setHeaderText("Error Occured!");
		}
    	alert.setContentText(text);
    	alert.showAndWait();
	}
	private int countWordsIn(int index, boolean vertical) {
		int cnt = 0;
		int dist = 0;
		if(vertical) {
			for(int i=0;i<TABLE_HEIGHT;i++) {
				dist++;
				if(blacked[i][index]) {
					if(dist > 2) {
						cnt++;
					}
					dist = 0;
				}
			}
			if(dist > 1) {
				cnt++;
			}
		}
		else {
			for(int j=0;j<TABLE_WIDTH;j++) {
				dist++;
				if(blacked[index][j]) {
					if(dist > 2) {
						cnt++;
					}
					dist = 0;
				}
			}
			if(dist > 1) {
				cnt++;
			}
		}
		return cnt;
	}
	public void handleTreeViewing(Event event) {
		int found = -1;
		for(int i=0;i<TABLE_HEIGHT;i++) {
			if(horizontal[i]!=event.getSource()) {
				boolean within = false;
				for(int j=0;j<horizontal[i].getChildren().size();j++) {
					if(horizontal[i].getChildren().get(j)==event.getSource()) {
						within = true;
						vertical_node.setExpanded(false);
						curr_word = j;
						curr_row = i;
						curr_col = -1;
						int cnt = 0; 
						y=-1;
						int dist = 0;
						word_len=0;
						for(int q=0;q<TABLE_WIDTH;q++) {
							if(!blacked[curr_row][q]) table[curr_row][q].setBackground(new Background(new BackgroundFill(Color.rgb(250, 250, 250), CornerRadii.EMPTY, Insets.EMPTY)));
							if(blacked[curr_row][q]) {
								if(dist > 1) {
									if(cnt==curr_word) {
										word_len = dist;
										y = q-1;
									}
									cnt++;
								}
								dist = 0;
							}
							else dist++;
						}
						if(word_len==0) word_len = dist;
						if(y==-1) y = TABLE_WIDTH-1;
						for(int q=y;q>=0;q--) {
							if(!blacked[curr_row][q]) table[curr_row][q].setBackground(new Background(new BackgroundFill(Color.rgb(150, 250, 250), CornerRadii.EMPTY, Insets.EMPTY)));
							else break; 
						}
						tf_horizontal[i][j].setText(text_horizontal[curr_row][curr_word][0]);
						tf_horizontal[i][j].textProperty().addListener(new ChangeListener<String>() {
							@Override
							public void changed(ObservableValue observable,
									String oldValue, String newValue) {
								unsaved_changes = !(text_horizontal[curr_row][curr_word][0].equals(tf_horizontal[curr_row][curr_word].getText()));
							}
						});
						ta_horizontal[i][j][0].setText(text_horizontal[curr_row][curr_word][1]);
						ta_horizontal[i][j][0].textProperty().addListener(new ChangeListener<String>() {
							@Override
							public void changed(ObservableValue observable,
									String oldValue, String newValue) {
								unsaved_changes = !(text_horizontal[curr_row][curr_word][1].equals(ta_horizontal[curr_row][curr_word][0].getText()));
							}
						});
						ta_horizontal[i][j][1].setText(text_horizontal[curr_row][curr_word][2]);
						ta_horizontal[i][j][1].textProperty().addListener(new ChangeListener<String>() {
							@Override
							public void changed(ObservableValue observable,
									String oldValue, String newValue) {
								unsaved_changes = !(text_horizontal[curr_row][curr_word][2].equals(ta_horizontal[curr_row][curr_word][1].getText()));
							}
						});
						GridPane gp_rand = (GridPane) ((TreeItem)words[0][i][j].getChildren().get(words[0][i][j].getChildren().size()-1)).getValue();
						gp_rand.getChildren().clear();
						gp_rand.setAlignment(Pos.CENTER);
						gp_rand.setPadding(new Insets(10, 0, 0, 0));
						gp_rand.setHgap(140);
						gp_rand.add(btn, 0, 0, 1, 1);
						gp_rand.add(reverse_cb_h[i][j], 1, 0, 1, 1);
						reverse_cb_h[i][j].pressedProperty().addListener((ObservableValue) -> {
							bool_horizontal[curr_row][curr_word] = !reverse_cb_h[curr_row][curr_word].isSelected();
						});
						btn.setOnAction((ActionEvent e) -> {try{
							int dif = word_len-removeWhiteSpace(tf_horizontal[curr_row][curr_word].getText()).length();
							if(dif != 0) {
								showAlert("The word length does not match the number of empty spaces!\nThere are "+
							+Math.abs(dif)+" letters "+(dif<0?"extra\n":"missing\n")+"Word Not Confirmed", true);
								return;
							}
							for(int q=y;q>=0;q--) {
								if(!blacked[curr_row][q]) {
									if(bool_horizontal[curr_row][curr_word]) {
										table[curr_row][q].setText(removeWhiteSpace(tf_horizontal[curr_row][curr_word].getText()).substring(y - q, y - q + 1));
									}
									else {
										table[curr_row][q].setText(removeWhiteSpace(tf_horizontal[curr_row][curr_word].getText()).substring(word_len-(y-q+1), word_len-(y-q+1)+1));
									}
								}
								else break; 
							}
							text_horizontal[curr_row][curr_word][0] = tf_horizontal[curr_row][curr_word].getText();
							text_horizontal[curr_row][curr_word][1] = ta_horizontal[curr_row][curr_word][0].getText();
							text_horizontal[curr_row][curr_word][2] = ta_horizontal[curr_row][curr_word][1].getText();
						}catch(Exception ex){showAlert(ex.getMessage(), true);}});
					}
					else {
						((TreeItem)horizontal[i].getChildren().get(j)).setExpanded(false);
					}
				}
				if(!within) {
					horizontal[i].setExpanded(false);
					for(int q=0;q<TABLE_WIDTH;q++) {
						if(!blacked[i][q]) table[i][q].setBackground(new Background(new BackgroundFill(Color.rgb(150, 150, 150), CornerRadii.EMPTY, Insets.EMPTY)));
					}
				}
				else found=curr_row;
			}
			else {
				vertical_node.setExpanded(false);
				for(int q=0;q<TABLE_WIDTH;q++) {
					if(!blacked[i][q]) table[i][q].setBackground(new Background(new BackgroundFill(Color.rgb(250, 250, 250), CornerRadii.EMPTY, Insets.EMPTY)));
				}
				curr_col = -1;
				curr_row = i;
				found=curr_row;
				horizontal[i].getChildren().clear();
				for(int j=0;j<countWordsIn(i, false);j++) {
					item_label = new Label("Word "+(j+1));
			    	item_label.setFont(new Font(18));
					words[0][i][j] = new TreeItem(item_label);
					Label lbl;
					lbl = new Label("The Word:");
					lbl.setFont(new Font(16));
					words[0][i][j].getChildren().add(new TreeItem(lbl));
					if(tf_horizontal[i][j]==null) {
						tf_horizontal[i][j] = new TextField();
						tf_horizontal[i][j].setMaxSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT - 30);
						tf_horizontal[i][j].setFont(new Font(16));
					}
					words[0][i][j].getChildren().add(new TreeItem(tf_horizontal[i][j]));
					lbl = new Label("The Hint:");
					lbl.setFont(new Font(16));
					words[0][i][j].getChildren().add(new TreeItem(lbl));
					if(ta_horizontal[i][j][0]==null) {
						ta_horizontal[i][j][0] = new TextArea();
						ta_horizontal[i][j][0].setPrefSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_horizontal[i][j][0].setMaxSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_horizontal[i][j][0].setFont(new Font(16));
						ta_horizontal[i][j][0].setWrapText(true);
					}
					words[0][i][j].getChildren().add(new TreeItem(ta_horizontal[i][j][0]));
					lbl = new Label("The Additional Information:");
					lbl.setFont(new Font(16));
					words[0][i][j].getChildren().add(new TreeItem(lbl));
					if(ta_horizontal[i][j][1]==null) {
						ta_horizontal[i][j][1] = new TextArea();
						ta_horizontal[i][j][1].setPrefSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_horizontal[i][j][1].setMaxSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_horizontal[i][j][1].setFont(new Font(16));
						ta_horizontal[i][j][1].setWrapText(true);
					}
					words[0][i][j].getChildren().add(new TreeItem(ta_horizontal[i][j][1]));
					reverse_cb_h[i][j] = new CheckBox("Reversed");
					reverse_cb_h[i][j].setSelected(bool_horizontal[i][j]);
					reverse_cb_h[i][j].setFont(new Font(16));
					GridPane gp_rand = new GridPane();
					words[0][i][j].getChildren().add(new TreeItem(gp_rand));
					horizontal[i].getChildren().add(words[0][i][j]);
				}
			}
		}
		if(found!=-1) {
			for(int i=0;i<TABLE_HEIGHT;i++) {
				if(i!=found) greyOut(i, false, false);
			}
			return;
		}
		for(int i=0;i<TABLE_WIDTH;i++) {
			if(vertical[i]!=event.getSource()) {
				boolean within = false;
				for(int j=0;j<vertical[i].getChildren().size();j++) {
					if(vertical[i].getChildren().get(j)==event.getSource()) {
						within = true;
						horizontal_node.setExpanded(false);
						curr_word = j;
						curr_col = i;
						curr_row = -1;
						int cnt = 0;
						y=-1;
						word_len = 0;
						int dist = 0;
						for(int q=0;q<TABLE_HEIGHT;q++) {
							if(!blacked[q][curr_col]) table[q][curr_col].setBackground(new Background(new BackgroundFill(Color.rgb(250, 250, 250), CornerRadii.EMPTY, Insets.EMPTY)));
							if(blacked[q][curr_col]) {
								if(dist > 1) {
									if(cnt==curr_word) {
										word_len = dist;
										y = q-1;
									}
									cnt++;
								}
								dist = 0;
							}
							else dist++;
						}
						if(word_len==0) word_len = dist;
						if(y==-1) y = TABLE_HEIGHT-1;
						for(int q=y;q>=0;q--) {
							if(!blacked[q][curr_col]) table[q][curr_col].setBackground(new Background(new BackgroundFill(Color.rgb(150, 250, 250), CornerRadii.EMPTY, Insets.EMPTY)));
							else break; 
						}
						tf_vertical[j][i].setText(text_vertical[curr_word][curr_col][0]);
						tf_vertical[j][i].textProperty().addListener(new ChangeListener<String>() {
						    @Override
						    public void changed(ObservableValue observable,
						            String oldValue, String newValue) {
						        unsaved_changes = !(text_vertical[curr_word][curr_col][0].equals(tf_vertical[curr_word][curr_col].getText()));
						    }
						});
						ta_vertical[j][i][0].setText(text_vertical[curr_word][curr_col][1]);
						ta_vertical[j][i][0].textProperty().addListener(new ChangeListener<String>() {
						    @Override
						    public void changed(ObservableValue observable,
						            String oldValue, String newValue) {
						        unsaved_changes = !(text_vertical[curr_word][curr_col][1].equals(ta_vertical[curr_word][curr_col][0].getText()));
						    }
						});
						ta_vertical[j][i][1].setText(text_vertical[curr_word][curr_col][2]);
						ta_vertical[j][i][1].textProperty().addListener(new ChangeListener<String>() {
						    @Override
						    public void changed(ObservableValue observable,
						            String oldValue, String newValue) {
						        unsaved_changes = !(text_vertical[curr_word][curr_col][2].equals(ta_vertical[curr_word][curr_col][1].getText()));
						    }
						});
						GridPane gp_rand = (GridPane) ((TreeItem)words[1][j][i].getChildren().get(words[1][j][i].getChildren().size()-1)).getValue();
						gp_rand.getChildren().clear();
						gp_rand.setAlignment(Pos.CENTER);
						gp_rand.setPadding(new Insets(10, 0, 0, 0));
						gp_rand.setHgap(140);
						gp_rand.add(btn, 0, 0, 1, 1);
						gp_rand.add(reverse_cb_v[j][i], 1, 0, 1, 1);
						reverse_cb_v[j][i].pressedProperty().addListener((ObservableValue) -> {
							bool_vertical[curr_word][curr_col] = !reverse_cb_v[curr_word][curr_col].isSelected();
						});
						btn.setOnAction((ActionEvent e) -> {try{
							int dif = word_len-removeWhiteSpace(tf_vertical[curr_word][curr_col].getText()).length();
							if(dif != 0) {
								showAlert("The word length does not match the number of empty spaces!\nThere are "+
							+Math.abs(dif)+" letters "+(dif<0?"extra\n":"missing\n")+"Word Not Confirmed", true);
								return;
							}
							for(int q=y;q>=0;q--) {
								if(!blacked[q][curr_col]) {
									if(bool_vertical[curr_word][curr_col]) {
										table[q][curr_col].setText(""+removeWhiteSpace(tf_vertical[curr_word][curr_col].getText()).charAt(y - q));
									}
									else {
										table[q][curr_col].setText(""+removeWhiteSpace(tf_vertical[curr_word][curr_col].getText()).charAt(word_len-(y-q+1)));
									}
								}
								else break; 
							}
							text_vertical[curr_word][curr_col][0] = tf_vertical[curr_word][curr_col].getText();
							text_vertical[curr_word][curr_col][1] = ta_vertical[curr_word][curr_col][0].getText();
							text_vertical[curr_word][curr_col][2] = ta_vertical[curr_word][curr_col][1].getText();
						}catch(Exception ex){showAlert(ex.getMessage(), true);}});
					}
					else {
						((TreeItem)vertical[i].getChildren().get(j)).setExpanded(false);
					}
				}
				if(!within) {
					vertical[i].setExpanded(false);
					for(int q=0;q<TABLE_HEIGHT;q++) {
						if(!blacked[q][i]) table[q][i].setBackground(new Background(new BackgroundFill(Color.rgb(150, 150, 150), CornerRadii.EMPTY, Insets.EMPTY)));
					}
				}
			}	
			else {
				curr_row = -1;
				curr_col = i;
				horizontal_node.setExpanded(false);
				for(int q=0;q<TABLE_HEIGHT;q++) {
					if(!blacked[q][i]) table[q][i].setBackground(new Background(new BackgroundFill(Color.rgb(250, 250, 250), CornerRadii.EMPTY, Insets.EMPTY)));
				}
				vertical[i].getChildren().clear();
				for(int j=0;j<countWordsIn(i, true);j++) {
					item_label = new Label("Word "+(j+1));
			    	item_label.setFont(new Font(18));
					words[1][j][i] = new TreeItem(item_label);
					Label lbl;
					lbl = new Label("The Word:");
					lbl.setFont(new Font(16));
					words[1][j][i].getChildren().add(new TreeItem(lbl));
					if(tf_vertical[j][i]==null) {
						tf_vertical[j][i] = new TextField();
						tf_vertical[j][i].setMaxSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT - 30);
						tf_vertical[j][i].setFont(new Font(16));
					}
					words[1][j][i].getChildren().add(new TreeItem(tf_vertical[j][i]));
					lbl = new Label("The Hint:");
					lbl.setFont(new Font(16));
					words[1][j][i].getChildren().add(new TreeItem(lbl));
					if(ta_vertical[j][i][0]==null) {
						ta_vertical[j][i][0] = new TextArea();
						ta_vertical[j][i][0].setPrefSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_vertical[j][i][0].setMaxSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_vertical[j][i][0].setFont(new Font(16));
						ta_vertical[j][i][0].setWrapText(true);
					}
					words[1][j][i].getChildren().add(new TreeItem(ta_vertical[j][i][0]));
					lbl = new Label("The Additional Information:");
					lbl.setFont(new Font(16));
					words[1][j][i].getChildren().add(new TreeItem(lbl));
					if(ta_vertical[j][i][1]==null) {
						ta_vertical[j][i][1] = new TextArea();
						ta_vertical[j][i][1].setPrefSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_vertical[j][i][1].setMaxSize(WINDOW_WIDTH / 2 - 85, WINDOW_HEIGHT / 4 - 30);
						ta_vertical[j][i][1].setFont(new Font(16));
						ta_vertical[j][i][1].setWrapText(true);
					}
					words[1][j][i].getChildren().add(new TreeItem(ta_vertical[j][i][1]));
					reverse_cb_v[j][i] = new CheckBox("Reversed");
					reverse_cb_v[j][i].setSelected(bool_vertical[j][i]);
					reverse_cb_v[j][i].setFont(new Font(16));
					GridPane gp_rand = new GridPane();
					words[1][j][i].getChildren().add(new TreeItem(gp_rand));
					vertical[i].getChildren().add(words[1][j][i]);
				}
			}
		}
	}
	public void handleTopBorders(Event event) {
		vertical_node.setExpanded(true);
		for(int i=0;i<TABLE_WIDTH;i++) {
			if(labels_t[i] == event.getSource()) {
				vertical[TABLE_WIDTH-i-1].setExpanded(true);
			}
		}
	}
	public void handleRightBorders(Event event) {
		horizontal_node.setExpanded(true);
		for(int i=0;i<TABLE_HEIGHT;i++) {
			if(labels_r[i] == event.getSource()) {
				horizontal[i].setExpanded(true);
			}
		}
	}
	public void handleFileNew(ActionEvent event) {
		if(unsaved_changes) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    	alert.setTitle("Unsaved Changes");
	    	alert.setHeaderText(null);
	    	alert.setContentText("There are unsaved changes, Are you sure you want to discard the changes and open a new file?");
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if(result.get() != ButtonType.OK && result.get() != ButtonType.YES) {
	    		return;
	    	}
		}
		TABLE_HEIGHT = TABLE_WIDTH = 10;
		Init(true);
	}
	public void handleFileSave(ActionEvent event) {
		File f;
		Controller.set_table(blacked);
		Controller.set_reverse_h(bool_horizontal);
		Controller.set_reverse_v(bool_vertical);
		Controller.set_horizontal(text_horizontal);
		Controller.set_vertical(text_vertical);
		if(path.equals("")) {
			FileChooser fc = new FileChooser();
			fc.setTitle("Save File");
			fc.getExtensionFilters().add(new ExtensionFilter("Crosswords Converter Files", "*.ccfo"));
			f = fc.showSaveDialog(window);
			if(f != null) {
				path = f.getAbsolutePath();
				window.setTitle("CrosswordsConverter - "+f.getName());
			}
		}
		try {
			Controller.Request(ReqID.SAVE, path);
			unsaved_changes = false;
		} catch (Exception e) {
			showAlert(e.getMessage(), false);
		}
	}
	public void handleFileSaveAs(ActionEvent event) {
		File f;
		Controller.set_table(blacked);
		Controller.set_reverse_h(bool_horizontal);
		Controller.set_reverse_v(bool_vertical);
		Controller.set_horizontal(text_horizontal);
		Controller.set_vertical(text_vertical);
		FileChooser fc = new FileChooser();
		fc.setTitle("Save File As");
		fc.getExtensionFilters().add(new ExtensionFilter("Crosswords Converter Files", "*.ccfo"));
		f = fc.showSaveDialog(window);
		if(f != null) {
			path = f.getAbsolutePath();
			window.setTitle("CrosswordsConverter - "+f.getName());
		}
		try {
			Controller.Request(ReqID.SAVE, path);
			unsaved_changes = false;
		} catch (Exception e) {
			showAlert(e.getMessage(), false);
		}
	}
	public void handleFileLoad(ActionEvent event) {
		if(unsaved_changes) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    	alert.setTitle("Unsaved Changes");
	    	alert.setHeaderText(null);
	    	alert.setContentText("There are unsaved changes, Are you sure you want to discard the changes and open a new file?");
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if(result.get() != ButtonType.OK && result.get() != ButtonType.YES) {
	    		return;
	    	}
		}
		File f;
		FileChooser fc = new FileChooser();
		fc.setTitle("Load File");
		fc.getExtensionFilters().add(new ExtensionFilter("Crosswords Converter Files", "*.ccfo"));
		f = fc.showOpenDialog(window);
		if(f != null && !f.isDirectory()) {
			path = f.getAbsolutePath();
			window.setTitle("CrosswordsConverter - "+f.getName());
		}
		else return;
		try {
			Controller.Request(ReqID.LOAD, path);
			Setup();
		} catch (Exception e) {
			showAlert(e.getMessage(), false);
		}
	}
	public void handleFileSettings(ActionEvent event) {
		int SET_WIDTH = 350;
		int SET_HEIGHT = 210;
		Stage settings = new Stage();
		settings.setTitle("Settings");
		VBox vb_main = new VBox();
		GridPane gp_set_above = new GridPane();
		GridPane gp_set_below = new GridPane();
		Slider width_slider = new Slider(2, 20, TABLE_WIDTH);
		width_slider.setMinWidth(250);
		width_slider.setBlockIncrement(1);
		width_slider.valueProperty().addListener((ObservableValue) ->{
			width_tf.setText(Integer.toString((int)(((DoubleProperty)ObservableValue).get())));
		});
		BorderPane bp_width_slider = new BorderPane();
		bp_width_slider.setCenter(width_slider);
		Slider height_slider = new Slider(2, 20, TABLE_HEIGHT);
		height_slider.setMinWidth(250);
		height_slider.setBlockIncrement(1);
		height_slider.valueProperty().addListener((ObservableValue) ->{
			height_tf.setText(Integer.toString((int)(((DoubleProperty)ObservableValue).get())));
		});
		BorderPane bp_height_slider = new BorderPane();
		bp_height_slider.setCenter(height_slider);
		width_tf = new TextField();
		width_tf.setMaxWidth(50);
		width_tf.setFont(new Font(14));
		width_tf.setText(Integer.toString((int)width_slider.getValue()));
		BorderPane bp_width_tf = new BorderPane();
		bp_width_tf.setCenter(width_tf);
		height_tf = new TextField();
		height_tf.setMaxWidth(50);
		height_tf.setFont(new Font(14));
		height_tf.setText(Integer.toString((int)width_slider.getValue()));
		BorderPane bp_height_tf = new BorderPane();
		bp_height_tf.setCenter(height_tf);
		Button ok_btn = new Button("OK");
		ok_btn.setMinWidth(100);
		ok_btn.setFont(new Font(14));
		ok_btn.setOnAction((ActionEvent ae) -> {
			if(unsaved_changes) {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		    	alert.setTitle("Unsaved Changes");
		    	alert.setHeaderText(null);
		    	alert.setContentText("There are unsaved changes, Are you sure you want to discard the changes and start a new file?");
		    	Optional<ButtonType> result = alert.showAndWait();
		    	if(result.get() != ButtonType.OK && result.get() != ButtonType.YES) {
		    		settings.close();
		    		return;
		    	}
			}
			TABLE_WIDTH = (int)width_slider.getValue();
			TABLE_HEIGHT = (int)height_slider.getValue();
			Init(true);
			settings.close();
		});
		BorderPane bp_ok_btn = new BorderPane();
		bp_ok_btn.setCenter(ok_btn);
		Button cancel_btn = new Button("Cancel");
		cancel_btn.setMinWidth(100);
		cancel_btn.setFont(new Font(14));
		cancel_btn.setOnAction((ActionEvent ae) -> {
			settings.close();
		});
		BorderPane bp_cancel_btn = new BorderPane();
		bp_cancel_btn.setCenter(cancel_btn);
		gp_set_above.add(width_slider, 0, 0, 1, 1);
		gp_set_above.add(height_slider, 0, 1, 1, 1);
		gp_set_above.add(width_tf, 1, 0, 1, 1);
		gp_set_above.add(height_tf, 1, 1, 1, 1);
		gp_set_above.setVgap(20);
		gp_set_above.setHgap(10);
		gp_set_above.setAlignment(Pos.CENTER);
		gp_set_below.add(ok_btn, 0, 2, 1, 1);
		gp_set_below.add(cancel_btn, 5, 2, 1, 1);
		gp_set_below.setVgap(20);
		gp_set_below.setHgap(10);
		gp_set_below.setAlignment(Pos.CENTER);
		vb_main.getChildren().add(gp_set_above);
		vb_main.getChildren().add(gp_set_below);
		vb_main.setAlignment(Pos.CENTER);
		Scene set_scene = new Scene(vb_main, SET_WIDTH, SET_HEIGHT);
		settings.setScene(set_scene);
		settings.showAndWait();
	}
	public void handleFileExit(Event event) {
		if(unsaved_changes) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	    	alert.setTitle("Unsaved Changes");
	    	alert.setHeaderText(null);
	    	alert.setContentText("There are unsaved changes, Are you sure you want to discard the changes and exit?");
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if(result.get() != ButtonType.OK && result.get() != ButtonType.YES) {
	    		event.consume();
	    		return;
	    	}
		}
		window.close();
	}
	public void handleHelpHowToUse(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Help");
    	alert.setHeaderText(null);
    	alert.setContentText("\"How to use\" is currently unavailable. It will be added in later versions, we apologize for the inconvenience.");
    	alert.showAndWait();
	}
	public void handleHelpCredits(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Credits");
    	alert.setHeaderText(null);
    	alert.setContentText("This program was created entirely by Ej Ham");
    	alert.showAndWait();
	}
	public void handleHelpInfo(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
    	alert.setTitle("Program Info");
    	alert.setHeaderText("Crosswords Converter by Ej Ham");
    	alert.setContentText("Release Version 1.0.0");
    	alert.showAndWait();
	}
	public void Init(boolean everything) {
		if(everything) {
			blacked = new boolean[TABLE_HEIGHT][TABLE_WIDTH];
			text_horizontal = new String[TABLE_HEIGHT][TABLE_WIDTH][3];
			text_vertical = new String[TABLE_HEIGHT][TABLE_WIDTH][3];
			for(int i=0;i<TABLE_HEIGHT;i++) {
	    		for(int j=0;j<TABLE_WIDTH;j++) {
	    			for(int k=0;k<3;k++) {
	        			text_horizontal[i][j][k] = "";
	        			text_vertical[i][j][k] = "";
	        		}
	    		}
	    	}
			bool_horizontal = new boolean[TABLE_HEIGHT][TABLE_WIDTH];
			bool_vertical = new boolean[TABLE_HEIGHT][TABLE_WIDTH];
			path = "";
	    	window.setTitle("CrosswordsConverter - Untitled");
		}
		curr_row = -1;
		curr_col = -1;
		curr_word = -1;
		draggingOnTable = false;
		erasing = false;
		unsaved_changes=false;
		table = new Label[TABLE_HEIGHT][TABLE_WIDTH];
		horizontal = new TreeItem[TABLE_HEIGHT];
		vertical = new TreeItem[TABLE_WIDTH];
		words = new TreeItem[2][TABLE_HEIGHT][TABLE_WIDTH];
		tf_horizontal = new TextField[TABLE_HEIGHT][TABLE_WIDTH];
		tf_vertical = new TextField[TABLE_HEIGHT][TABLE_WIDTH];
		ta_horizontal = new TextArea[TABLE_HEIGHT][TABLE_WIDTH][2];
		ta_vertical = new TextArea[TABLE_HEIGHT][TABLE_WIDTH][2];
		reverse_cb_h = new CheckBox[TABLE_HEIGHT][TABLE_WIDTH];
		reverse_cb_v = new CheckBox[TABLE_HEIGHT][TABLE_WIDTH];
		labels_t = new Label[TABLE_WIDTH];
		labels_r = new Label[TABLE_HEIGHT];
		Controller.set_table(blacked);
		Controller.set_reverse_h(bool_horizontal);
		Controller.set_reverse_v(bool_vertical);
		Controller.set_horizontal(text_horizontal);
		Controller.set_vertical(text_vertical);	
		VBox vbox_main = new VBox();
    	HBox hbox_main = new HBox();
    	SplitPane split_pane_main = new SplitPane();
    	split_pane_main.setOrientation(Orientation.HORIZONTAL);
    	StackPane sp_left = new StackPane();
    	StackPane sp_right = new StackPane();
    	sp_left.setMinWidth(WINDOW_WIDTH/2);
    	sp_right.setMinWidth(WINDOW_WIDTH/2);
    	sp_left.setMaxWidth(WINDOW_WIDTH/2);
    	sp_right.setMaxWidth(WINDOW_WIDTH/2);
    	sp_left.setMinHeight(WINDOW_HEIGHT - 30);
    	sp_right.setMinHeight(WINDOW_HEIGHT - 30);
    	sp_left.setMaxHeight(WINDOW_HEIGHT - 30);
    	sp_right.setMaxHeight(WINDOW_HEIGHT - 30);
    	//*** Left ***
    	GridPane gp_table = new GridPane();
    	gp_table.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
    	gp_table.setMinWidth(WINDOW_WIDTH/2);
    	gp_table.setMinHeight(WINDOW_WIDTH/2);
    	gp_table.setMaxWidth(WINDOW_WIDTH/2);
    	gp_table.setMaxHeight(WINDOW_WIDTH/2);
		gp_table.setPadding(new Insets(1, 1, 1, 1));
		gp_table.setVgap(1);
		gp_table.setHgap(1);
		gp_table.setAlignment(Pos.CENTER);
    	for(int i=0;i<TABLE_HEIGHT+1;i++) {
    		for(int j=0;j<TABLE_WIDTH+1;j++) {
    			Label cell_label = new Label();
    			cell_label.setPrefSize(WINDOW_WIDTH/2, WINDOW_WIDTH/2);
    			cell_label.setAlignment(Pos.CENTER);
    			int fsz, maxi;
    			maxi = Math.max(TABLE_WIDTH, TABLE_HEIGHT);
    			if(maxi >= 10) {
    				fsz = 35 - maxi;
    			}
    			else {
    				fsz = 5*(10-maxi)+25;
    			}
    			cell_label.setFont(Font.font("TimesNewRoman", javafx.scene.text.FontWeight.EXTRA_BOLD, (double)fsz));
    			if(i==TABLE_HEIGHT&&j<TABLE_WIDTH) {
    				cell_label.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 200), CornerRadii.EMPTY, Insets.EMPTY)));
    				cell_label.setText(""+(TABLE_WIDTH-j));
    				cell_label.setTextFill(Color.rgb(255, 255, 255));
    				cell_label.setOnMouseClicked((Event event) -> {try{handleTopBorders(event);}catch(Exception e){showAlert(e.getMessage(), true);}});
    				labels_t[j] = cell_label;
        			gp_table.add(labels_t[j], j, 0, 1, 1);
    				continue;
    			}
    			if(j==TABLE_WIDTH&&i<TABLE_HEIGHT) {
    				cell_label.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 200), CornerRadii.EMPTY, Insets.EMPTY)));
    				cell_label.setText(""+(i+1));
    				cell_label.setTextFill(Color.rgb(255, 255, 255));
    				cell_label.setOnMouseClicked((Event event) -> {try{handleRightBorders(event);}catch(Exception e){showAlert(e.getMessage(), true);}});
    				labels_r[i] = cell_label;
        			gp_table.add(labels_r[i], j, i+1, 1, 1);
    				continue;
    			}
    			if(i==TABLE_HEIGHT&&j==TABLE_WIDTH) {
    				cell_label.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 100), CornerRadii.EMPTY, Insets.EMPTY)));
    				cell_label.setOnMouseClicked((MouseEvent event) -> {try{
    					for(int u=0;u<TABLE_HEIGHT;u++)
    						for(int v=0;v<TABLE_WIDTH;v++)
    							for(int k=0;k<2;k++)
    								if(words[k][u][v]!=null) words[k][u][v].setExpanded(false);
    					for(int u=0;u<TABLE_HEIGHT;u++)
    						horizontal[u].setExpanded(false);
    					for(int u=0;u<TABLE_WIDTH;u++)
    						vertical[u].setExpanded(false);
    					horizontal_node.setExpanded(false);
    					vertical_node.setExpanded(false);
    				}catch(Exception e){showAlert(e.getMessage(), true);}});
        			gp_table.add(cell_label, j, 0, 1, 1);
    				continue;
    			}
    			table[i][j] = cell_label;
    			gp_table.add(table[i][j], TABLE_WIDTH-j-1, i+1, 1, 1);
    			table[i][j].setBackground(new Background(new BackgroundFill((blacked[i][j]?Color.rgb(0, 0, 0):Color.rgb(150, 150, 150)), CornerRadii.EMPTY, Insets.EMPTY)));
    			table[i][j].setOnMousePressed(new EventHandler <MouseEvent>()
		        {
		            public void handle(MouseEvent event)
		            {
		            	if(!((Label)event.getSource()).getText().equals(""))
		            		return;
		            	unsaved_changes = true;
		            	((Control)event.getSource()).setMouseTransparent(true);
		            	for(int i=0;i<TABLE_HEIGHT;i++) {
		            		for(int j=0;j<TABLE_WIDTH;j++) {
		            			if(event.getSource() == table[i][j]) {
		            				erasing = blacked[i][j];
		            				blacked[i][j] = !blacked[i][j];
		            				if(blacked[i][j]) {
		            					((Control)event.getSource()).setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		            				}
		            				else {
		            					((Control)event.getSource()).setBackground(new Background(new BackgroundFill(Color.rgb(150, 150, 150), CornerRadii.EMPTY, Insets.EMPTY)));
		            				}
		            			}
		            		}
		            	}		                
		                event.setDragDetect(true);
		            }
		        });
		 
    			table[i][j].setOnMouseReleased(new EventHandler <MouseEvent>()
		        {
		            public void handle(MouseEvent event)
		            {
		            	if(!((Label)event.getSource()).getText().equals(""))
		            		return;
		            	unsaved_changes = true;
		            	((Control)event.getSource()).setMouseTransparent(false);
		            }
		        });
		 
    			table[i][j].setOnMouseDragged(new EventHandler <MouseEvent>()
		        {
		            public void handle(MouseEvent event)
		            {
		            	if(!((Label)event.getSource()).getText().equals(""))
		            		return;
		            	unsaved_changes = true;
		            	event.setDragDetect(false);
		            }
		        });
		 
    			table[i][j].setOnDragDetected(new EventHandler <MouseEvent>()
		        {
		            public void handle(MouseEvent event)
		            {
		            	if(!((Label)event.getSource()).getText().equals(""))
		            		return;
		            	unsaved_changes = true;
		            	((Control)event.getSource()).startFullDrag();
		            }
		        });
		 
    			table[i][j].setOnMouseDragEntered(new EventHandler <MouseDragEvent>()
		        {
		            public void handle(MouseDragEvent event)
		            {
		            	if(!((Label)event.getSource()).getText().equals(""))
		            		return;
		            	unsaved_changes = true;
		            	for(int i=0;i<TABLE_HEIGHT;i++) {
		            		for(int j=0;j<TABLE_WIDTH;j++) {
		            			if(event.getSource() == table[i][j]) {
		            				blacked[i][j] = !erasing;
		            				if(blacked[i][j]) {
		            					((Control)event.getSource()).setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0), CornerRadii.EMPTY, Insets.EMPTY)));
		            				}
		            				else {
		            					((Control)event.getSource()).setBackground(new Background(new BackgroundFill(Color.rgb(150, 150, 150), CornerRadii.EMPTY, Insets.EMPTY)));
		            				}
		            			}
		            		}
		            	}	
		            }
		        });
    		}
    	}
    	
    	//*** Right ***
    	btn = new Button("Confirm");
    	btn.setFont(new Font(16));
    	tv = new TreeView();
    	tv.setPrefSize(WINDOW_WIDTH/2, WINDOW_HEIGHT - 30);
    	//tv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleTreeClicked(newValue));
    	TreeItem tv_main_node = new TreeItem();
    	item_label = new Label("Horizontal");
    	item_label.setFont(new Font(18));
    	horizontal_node = new TreeItem(item_label);
    	horizontal_node.setExpanded(true);
    	horizontal_node.addEventHandler(horizontal_node.branchCollapsedEvent(), (Event event) -> {
    		if(event.getSource()==horizontal_node) {
    			boolean vopen=false;
    			for(int i=0;i<TABLE_WIDTH;i++) {
    				vopen = vertical[i].isExpanded();
    			}
    			if(!vopen) greyOut(-1, false, false);
        		for(int i=0;i<TABLE_HEIGHT;i++) {
        			horizontal[i].setExpanded(false);
        		}
    		}
    		else {
    			for(int q=0;q<TABLE_HEIGHT;q++) {
    				if(horizontal[q]==event.getSource()&&!horizontal[q].isExpanded()) {
    					greyOut(q, false, false);
    					for(int j=0;j<TABLE_WIDTH;j++) {
    						if(words[0][q][j]!=null) words[0][q][j].setExpanded(false);
    					}
    					break;
    				}
    				else {
    					for(int j=0;j<TABLE_WIDTH;j++) {
    						if(words[0][q][j] == event.getSource()) { 
    							greyOut(q, false, true);
    							return;
    						}
    					}
    				}
    			}
    		}
    	});
    	for(int i=0;i<TABLE_HEIGHT;i++) {
    		item_label = new Label("Row "+(i+1));
        	item_label.setFont(new Font(18));
    		horizontal[i] = new TreeItem(item_label);
    		horizontal[i].addEventHandler(horizontal[i].branchExpandedEvent(), (Event event) -> {try {handleTreeViewing(event);}catch(Exception e){showAlert(e.getMessage(), true);}});
    		for(int k=0;k<countWordsIn(i, false);k++) {
				words[0][i][k] = new TreeItem("Word "+(k+1));
				horizontal[i].getChildren().add(words[0][i][k]);
			}
    		horizontal_node.getChildren().add(horizontal[i]);
    	}
    	tv_main_node.getChildren().add(horizontal_node);
    	item_label = new Label("-----------------------------------------------");
    	item_label.setFont(new Font(18));
    	TreeItem separator_node = new TreeItem(item_label);
    	tv_main_node.getChildren().add(separator_node);
    	item_label = new Label("Vertical");
    	item_label.setFont(new Font(18));
    	vertical_node = new TreeItem(item_label);
    	vertical_node.setExpanded(true);
    	vertical_node.addEventHandler(vertical_node.branchCollapsedEvent(), (Event event) -> {
    		if(event.getSource() == vertical_node) {
    			boolean hopen=false;
    			for(int i=0;i<TABLE_HEIGHT;i++) {
    				hopen = horizontal[i].isExpanded();
    			}
    			if(!hopen) greyOut(-1, false, false);
        		for(int i=0;i<TABLE_WIDTH;i++) {
        			vertical[i].setExpanded(false);
        		}
    		}
    		else {
    			for(int q=0;q<TABLE_WIDTH;q++) {
    				if(vertical[q]==event.getSource()&&!vertical[q].isExpanded()) {
    					greyOut(q, true, false);
    					for(int j=0;j<TABLE_HEIGHT;j++) {
    						if(words[1][j][q]!=null) words[1][j][q].setExpanded(false);
    					}
    					break;
    				}
    				else {
    					for(int j=0;j<TABLE_HEIGHT;j++) {
    						if(words[1][j][q] == event.getSource()) {
    							greyOut(q, true, true);
    							return;
    						}
    					}
    				}
    			}    			
    		}
    	});
    	for(int i=0;i<TABLE_WIDTH;i++) {
    		item_label = new Label("Column "+(i+1));
        	item_label.setFont(new Font(18));
    		vertical[i] = new TreeItem(item_label);
    		vertical[i].addEventHandler(vertical[i].branchExpandedEvent(), (Event event) -> {try{handleTreeViewing(event);}catch(Exception e){showAlert(e.getMessage(), true);}});
    		for(int k=0;k<countWordsIn(i, true);k++) {
				words[1][k][i] = new TreeItem("Word "+(k+1));
				vertical[i].getChildren().add(words[1][k][i]);
			}
    		vertical_node.getChildren().add(vertical[i]);
    	}
    	tv_main_node.getChildren().add(vertical_node);
    	tv.setRoot(tv_main_node);
    	tv.setShowRoot(false);
    	sp_left.getChildren().add(gp_table);
    	sp_right.getChildren().add(tv);
    	split_pane_main.getItems().add(sp_left);
    	split_pane_main.getItems().add(sp_right);
    	hbox_main.getChildren().add(split_pane_main);
    	MenuBar menu_bar_main = new MenuBar(); 
    	Menu menu_f = new Menu("File");
    	MenuItem menu_f_new = new MenuItem("New");
    	menu_f_new.setOnAction((ActionEvent ae) -> {try{handleFileNew(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	MenuItem menu_f_save = new MenuItem("Save");
    	menu_f_save.setOnAction((ActionEvent ae) -> {try{handleFileSave(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	MenuItem menu_f_save_as = new MenuItem("Save As...");
    	menu_f_save_as.setOnAction((ActionEvent ae) -> {try{handleFileSaveAs(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	MenuItem menu_f_load = new MenuItem("Load");
    	menu_f_load.setOnAction((ActionEvent ae) -> {try{handleFileLoad(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	SeparatorMenuItem menu_f_nsl_sep = new SeparatorMenuItem();
    	MenuItem menu_f_settings = new MenuItem("Settings");
    	menu_f_settings.setOnAction((ActionEvent ae) -> {try{handleFileSettings(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	SeparatorMenuItem menu_f_exit_sep = new SeparatorMenuItem();
    	MenuItem menu_f_exit = new MenuItem("Exit\t\t\t\t\t"); 
    	menu_f_exit.setOnAction((ActionEvent ae) -> {try{handleFileExit(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	Menu menu_h = new Menu("Help");
    	MenuItem menu_h_howto = new MenuItem("How to use");
    	menu_h_howto.setOnAction((ActionEvent ae) -> {try{handleHelpHowToUse(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	SeparatorMenuItem menu_h_info_sep = new SeparatorMenuItem();
    	MenuItem menu_h_credits = new MenuItem("Credits");
    	menu_h_credits.setOnAction((ActionEvent ae) -> {try{handleHelpCredits(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
    	MenuItem menu_h_info = new MenuItem("Info\t\t\t\t\t");
    	menu_h_info.setOnAction((ActionEvent ae) -> {try{handleHelpInfo(ae);}catch(Exception e){showAlert(e.getMessage(), true);}});
        //*************************** Adding All Elements *************************//
    	menu_f.getItems().add(menu_f_new);
    	menu_f.getItems().add(menu_f_save);
    	menu_f.getItems().add(menu_f_save_as);
    	menu_f.getItems().add(menu_f_load);
    	menu_f.getItems().add(menu_f_nsl_sep);
    	menu_f.getItems().add(menu_f_settings);
    	menu_f.getItems().add(menu_f_exit_sep);
    	menu_f.getItems().add(menu_f_exit);
    	menu_bar_main.getMenus().add(menu_f);
    	menu_h.getItems().add(menu_h_howto);
    	menu_h.getItems().add(menu_h_info_sep);
    	menu_h.getItems().add(menu_h_credits);
    	menu_h.getItems().add(menu_h_info);
    	menu_bar_main.getMenus().add(menu_h);
    	vbox_main.getChildren().add(menu_bar_main);
    	vbox_main.getChildren().add(hbox_main);
        //*************************** Showing the Window *************************//
    	main_scene = new Scene(vbox_main, WINDOW_WIDTH, WINDOW_HEIGHT);
    	window.setResizable(false);
    	window.setScene(main_scene);
	}
	public void Setup() {
		unsaved_changes = false;
		blacked = Controller.get_table();
		text_horizontal = Controller.get_horizontal();
		text_vertical = Controller.get_vertical();
		bool_horizontal = Controller.get_reverse_h();
		bool_vertical = Controller.get_reverse_v();
		if(blacked==null||text_horizontal==null||text_vertical==null||bool_horizontal==null||bool_vertical==null) {
			showAlert("Couldn't correctly load the file!", true);
			Init(true);
			return;
		}
		TABLE_HEIGHT = blacked.length;
		TABLE_WIDTH = blacked[0].length;
		Init(false);
		for(int i=0;i<TABLE_HEIGHT;i++) {
			for(int j=0;j<TABLE_WIDTH;j++) {
				if(text_horizontal[i][j][0].equals("")) 
					continue;
				int s,e;
				s = getWordRange(i, j+1, false)[0];
				e = getWordRange(i, j+1, false)[1];
				if(bool_horizontal[i][j]) {
					for(int q=e;q>=s;q--) {
						table[i][e-q+s].setText(""+removeWhiteSpace(text_horizontal[i][j][0]).charAt(q-s));
					}
				}
				else {
					for(int q=s;q<=e;q++) {
						table[i][q].setText(""+removeWhiteSpace(text_horizontal[i][j][0]).charAt(q-s));
					}
				}
			}
		}
		for(int i=0;i<TABLE_WIDTH;i++) {
			for(int j=0;j<TABLE_HEIGHT;j++) {
				if(text_vertical[j][i][0].equals("")) 
					continue;
				int s,e;
				s = getWordRange(i, j+1, true)[0];
				e = getWordRange(i, j+1, true)[1];
				if(bool_vertical[j][i]) {
					for(int q=e;q>=s;q--) {
						table[e-q+s][i].setText(""+removeWhiteSpace(text_vertical[j][i][0]).charAt(q-s));
					}
				}
				else {
					for(int q=s;q<=e;q++) {
						table[q][i].setText(""+removeWhiteSpace(text_vertical[j][i][0]).charAt(q-s));
					}
				}
			}
		}
	}
	public int[] getWordRange(int index, int word, boolean vertical) {
		int coords[] = {0, 0};
		int cnt=0, dist=0;
		int y=-1;
		if(vertical) {
			for(int i=0;i<TABLE_HEIGHT;i++) {
				if(blacked[i][index]) {
					if(dist > 1) {
						cnt++;
						if(cnt==word) {
							y=i-1;
						}
					}
					dist = 0;
				}
				else dist++;
			}
			if(y==-1) y = TABLE_HEIGHT-1;
			coords[1] = y;
			for(int i=y;i>=0;i--) {
				if(blacked[i][index]) {
					coords[0] = i+1;
					break;
				}
				else if(i==0) {
					return coords;
				}
			}
		}
		else {
			for(int i=0;i<TABLE_WIDTH;i++) {
				if(blacked[index][i]) {
					if(dist > 1) {
						cnt++;
						if(cnt==word) {
							y=i-1;
						}
					}
					dist = 0;
				}
				else dist++;
			}
			if(y==-1) y = TABLE_WIDTH-1;
			coords[1] = y;
			for(int i=y;i>=0;i--) {
				if(blacked[index][i]) {
					coords[0] = i+1;
					break;
				}
				else if(i==0) {
					return coords;
				}
			}
		}
		return coords;
	}
	public void greyOut(int index, boolean vertical, boolean white) {
		if(index == -1) {
			for(int i=0;i<TABLE_HEIGHT;i++) {
				for(int j=0;j<TABLE_WIDTH;j++) {
					if(!white) table[i][j].setBackground(new Background(new BackgroundFill((blacked[i][j]?Color.rgb(0, 0, 0):Color.rgb(150, 150, 150)), CornerRadii.EMPTY, Insets.EMPTY)));
					else table[i][j].setBackground(new Background(new BackgroundFill((blacked[i][j]?Color.rgb(0, 0, 0):Color.rgb(250, 250, 250)), CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		}
		else {
			if(vertical) {
				for(int i=0;i<TABLE_HEIGHT;i++) {
					if(!white) table[i][index].setBackground(new Background(new BackgroundFill((blacked[i][index]?Color.rgb(0, 0, 0):Color.rgb(150, 150, 150)), CornerRadii.EMPTY, Insets.EMPTY)));
					else table[i][index].setBackground(new Background(new BackgroundFill((blacked[i][index]?Color.rgb(0, 0, 0):Color.rgb(250, 250, 250)), CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
			else {
				for(int i=0;i<TABLE_WIDTH;i++) {
					if(!white) table[index][i].setBackground(new Background(new BackgroundFill((blacked[index][i]?Color.rgb(0, 0, 0):Color.rgb(150, 150, 150)), CornerRadii.EMPTY, Insets.EMPTY)));
					else table[index][i].setBackground(new Background(new BackgroundFill((blacked[index][i]?Color.rgb(0, 0, 0):Color.rgb(250, 250, 250)), CornerRadii.EMPTY, Insets.EMPTY)));
				}
			}
		}
	}
	public String removeWhiteSpace(String s) {
		String str = "";
		for(int i=0;i<s.length();i++) {
			if(s.charAt(i)!=' '&&s.charAt(i)!='\t')
				str+=s.charAt(i);
		}
		return str;
	}
}