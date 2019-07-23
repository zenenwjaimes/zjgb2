package com.slasherx.zjgb;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.slasherx.zjgb.peripherals.*;

/**
 * Simple JavaFX App
 *
 */
public class App extends Application
{
	final public static String WINDOW_TITLE = "zjgb";
	final public static int WINDOW_WIDTH = 300;
	final public static int WINDOW_HEIGHT = 250;
	final public static Logger logger = LogManager.getRootLogger();
	
	public GameBoy gb;
	public Thread gbThread;

	
    public static void main( String[] args )
    {
        launch(args);
    }

	@Override
	public void start(Stage primaryStage) throws Exception {		
		gb = new GameBoy(logger, new RomLoader(logger, "/Users/slasherx/test.gb"));
		gbThread = new Thread(gb);
		
        Button btn = new Button();
        btn.setText("Run CPU Instructions");
        
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if (gbThread.isAlive()) {
                	if (gb.isRunning()) {
                    	gb.setRunning(false);
                	} else {
                    	gb.setRunning(true);
                	}
            	} else {
                	gb.setRunning(true);
            		gbThread.start();
            	}
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        Scene scene = new Scene(root, App.WINDOW_WIDTH, App.WINDOW_HEIGHT);

        primaryStage.setTitle(App.WINDOW_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		// Clean up the main loop before trying to exit
		// otherwise we get a stuck thread
		gb.setCanceled(true);
		
		gbThread.interrupt();
		gbThread.join();
	}
}
