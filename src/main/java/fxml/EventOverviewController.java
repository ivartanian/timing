package fxml;


import app.Main;
import domain.Event;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import schedulers.ScedulerHandler;

import java.time.LocalDate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static app.Main.FORMATTER;
import static java.util.Objects.nonNull;

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

//        Обновляет цвет
        eventTable.setRowFactory(param -> new TableRow<Event>() {
            @Override
            protected void updateBounds() {
                super.updateBounds();
                Event item = getItem();
                if (nonNull(item)) {
                    boolean done = item.isDone();
                    if (done)
                        this.setStyle("-fx-background-color:#90ee90");
                    else
                        this.setStyle("-fx-background-color:#f0eb6a");
                }
            }
        });
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

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> Platform.runLater(new ScedulerHandler(main)), 0, 10, TimeUnit.SECONDS);

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
        main.showCreateEventDialog(datePicker.getValue(), null);
    }

    @FXML
    private void onMouseClickedEditEvent(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Event selectedItem = eventTable.getSelectionModel().getSelectedItem();
            if (nonNull(selectedItem)) {
                main.showCreateEventDialog(selectedItem.getDateTime().toLocalDate(), selectedItem);
            }
        }
    }

}
