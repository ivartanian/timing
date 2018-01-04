package fxml;

import app.Main;
import domain.Event;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static java.util.Objects.nonNull;

public class CreateEvent {

    private LocalDate localDate;
    private Stage dialogStage;
    private boolean okClicked = false;
    // Ссылка на главное приложение.
    private Main main;

    private Event event;

    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField hoursTextField;
    @FXML
    private TextField minutesTextField;
    @FXML
    private TextArea eventDescriptionTextArea;

    @FXML
    private void initialize() {
        hoursTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    hoursTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (!newValue.isEmpty()) {
                    Integer hours = Integer.valueOf(newValue);
                    if (hours < 0){
                        hoursTextField.setText("0");
                    }
                    if (hours > 23){
                        hoursTextField.setText("23");
                    }
                }
            }
        });
        minutesTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    minutesTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }else if (!newValue.isEmpty()) {
                    Integer minutes = Integer.valueOf(newValue);
                    if (minutes < 0){
                        minutesTextField.setText("0");
                    }
                    if (minutes > 59){
                        minutesTextField.setText("59");
                    }
                }
            }
        });
    }

    @FXML
    private void handleOk() {
        try {
            if (isInputValid()) {
                Integer hours = Integer.valueOf(hoursTextField.getText());
                Integer minutes = Integer.valueOf(minutesTextField.getText());
                Event event;
                if (nonNull(this.event)){
                    event = this.event;
                    event.setDateTime(LocalDateTime.of(datePicker.getValue(), LocalTime.of(hours, minutes)));
                    event.setDescription(eventDescriptionTextArea.getText());
                    event.setDone(false);
                }else {
                    event = new Event(LocalDateTime.of(datePicker.getValue(), LocalTime.of(hours, minutes)), eventDescriptionTextArea.getText(), false);
                    main.getEventData().add(event);
                }

                okClicked = true;
                dialogStage.close();
            }
        }finally {
            dialogStage.close();
        }

    }

    private boolean isInputValid() {
        return nonNull(datePicker.getValue()) && nonNull(hoursTextField) && nonNull(minutesTextField)
                && nonNull(eventDescriptionTextArea.getText()) && !eventDescriptionTextArea.getText().isEmpty();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
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

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
        datePicker.setValue(localDate);
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        if (nonNull(event)){
            datePicker.setValue(event.getDateTime().toLocalDate());
            hoursTextField.setText(String.valueOf(event.getDateTime().getHour()));
            minutesTextField.setText(String.valueOf(event.getDateTime().getMinute()));
            eventDescriptionTextArea.setText(event.getDescription());
        }
    }
}
