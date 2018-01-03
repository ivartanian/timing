package app;

import domain.Event;
import fcml.EventOverviewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Main extends Application {

    private ObservableList<Event> eventData = FXCollections.observableArrayList();

    private Stage primaryStage;
    private AnchorPane rootLayout;

    public Main() {
        eventData.add(new Event(LocalDateTime.now().minus(1, ChronoUnit.HOURS), "Event 1"));
        eventData.add(new Event(LocalDateTime.now().plus(1, ChronoUnit.HOURS), "Event 2"));
        eventData.add(new Event(LocalDateTime.now().plus(6, ChronoUnit.MINUTES), "Event 3"));
        eventData.add(new Event(LocalDateTime.now().plus(1, ChronoUnit.DAYS), "Event 4"));
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Timing");

        initRootLayout();

    }

    /**
     * Инициализирует корневой макет.
     */
    public void initRootLayout() {
        try {
            // Загружаем корневой макет из fxml файла.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/listEvents.fxml"));
            rootLayout = loader.load();

            // Даём контроллеру доступ к главному приложению.
            EventOverviewController controller = loader.getController();
            controller.setMain(this);

            // Отображаем сцену, содержащую корневой макет.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Event> getEventData() {
        return eventData;
    }

}
