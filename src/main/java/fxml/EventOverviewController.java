package fxml;


import app.Main;
import domain.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static app.Main.FORMATTER;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;

public class EventOverviewController {



    @FXML
    private DatePicker datePicker;
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

        LocalDate now = LocalDate.now();

        datePicker.setValue(now);

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).

        // Инициализация таблицы событий с двумя столбцами.
        timeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().dateTimeProperty().get().format(FORMATTER)));
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
    }

    /**
     * Вызывается главным приложением, которое даёт на себя ссылку.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;

        LocalDate now = LocalDate.now();
        // Добавление в таблицу данных из наблюдаемого списка
        showEventByDate(now);
    }

    private void showEventByDate(LocalDate now) {
        FilteredList<Event> filteredData = new FilteredList<>(main.getEventData(), p -> true);
        filteredData.setPredicate(event -> now.isEqual(event.getDateTime().toLocalDate()));
        eventTable.setItems(filteredData);
    }

    @FXML
    private void onChangedDate() {
        LocalDate value = datePicker.getValue();
        showEventByDate(value);
    }

    @FXML
    private void onClickedAddEvent() {
        main.showCreateEventDialog(datePicker.getValue());
    }

}
