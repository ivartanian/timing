package fxml;

import app.Main;
import domain.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static app.Main.FORMATTER;

public class RemindEvent {
    private Stage dialogStage;
    private boolean okClicked = false;
    // Ссылка на главное приложение.
    private Main main;
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, String> timeColumn;
    @FXML
    private TableColumn<Event, String> descriptionColumn;

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {

        // Инициализация таблицы событий с двумя столбцами.
        timeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dateTimeProperty().get().format(FORMATTER)));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }

    @FXML
    private void handleOk() {
        main.getReadyEvent().forEach(event -> event.setDone(true));
        main.getReadyEvent().clear();
        main.setActiveReminderDialog(false);
        okClicked = true;
        dialogStage.close();

    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        main.setActiveReminderDialog(false);
        dialogStage.close();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setOkClicked(boolean okClicked) {
        this.okClicked = okClicked;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public TableView<Event> getEventTable() {
        return eventTable;
    }

    public void setEventTable(TableView<Event> eventTable) {
        this.eventTable = eventTable;
    }
}
