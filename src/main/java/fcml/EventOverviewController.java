package fcml;


import app.Main;
import domain.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;

public class EventOverviewController {
    @FXML
    private TableView<Event> eventTable;
    @FXML
    private TableColumn<Event, String> timeColumn;
    @FXML
    private TableColumn<Event, String> descriptionColumn;

    // Ссылка на главное приложение.
    private Main main;

    /**
     * Конструктор.
     * Конструктор вызывается раньше метода initialize().
     */
    public EventOverviewController() {
    }

    /**
     * Инициализация класса-контроллера. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
        // Инициализация таблицы адресатов с двумя столбцами.
        timeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dateTimeProperty().get().format(DateTimeFormatter.ISO_TIME)));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
        // Добавление в таблицу данных из наблюдаемого списка
        eventTable.setItems(main.getEventData());
    }
}
