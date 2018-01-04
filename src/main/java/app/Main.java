package app;

import domain.Event;
import fxml.CreateEvent;
import fxml.EventOverviewController;
import fxml.RemindEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import schedulers.ScedulerHandler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.util.Objects.isNull;

public class Main extends Application {

    public static DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .optionalStart().toFormatter();

    private ObservableList<Event> eventData = FXCollections.observableArrayList();
    private ObservableSet<Event> readyEvent = FXCollections.observableSet();

    private boolean isActiveReminderDialog;

    private Stage primaryStage;
    private BorderPane rootLayout;

    public Main() {
        eventData.add(new Event(LocalDateTime.now().minus(1, ChronoUnit.HOURS), "Event 1", false));
        eventData.add(new Event(LocalDateTime.now().plus(1, ChronoUnit.HOURS), "Event 2", false));
        eventData.add(new Event(LocalDateTime.now().plus(6, ChronoUnit.MINUTES), "Event 3", false));
        eventData.add(new Event(LocalDateTime.now().plus(1, ChronoUnit.DAYS), "Event 4", false));
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

    public boolean showCreateEventDialog(LocalDate localDate, Event event) {
        if (isNull(localDate)){
            return false;
        }
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/createEvent.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create/Update event");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            CreateEvent controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setLocalDate(localDate);
            controller.setMain(this);
            controller.setEvent(event);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Event> getEventData() {
        return eventData;
    }

    public ObservableSet<Event> getReadyEvent() {
        return readyEvent;
    }

    public void addReadyEvent(Event event) {
        readyEvent.add(event);
    }

    public boolean showReminderDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/fxml/remindEvent.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Remind events");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            RemindEvent controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMain(this);
            controller.getEventTable().setItems(FXCollections.observableArrayList(readyEvent));

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean isActiveReminderDialog() {
        return isActiveReminderDialog;
    }

    public void setActiveReminderDialog(boolean activeReminderDialog) {
        isActiveReminderDialog = activeReminderDialog;
    }
}
