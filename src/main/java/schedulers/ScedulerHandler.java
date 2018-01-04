package schedulers;

import app.Main;
import fxml.EventOverviewController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.TimerTask;

public class ScedulerHandler extends TimerTask {

    private Main main;

    public ScedulerHandler(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        main.getEventData().stream().filter(event -> !event.isDone())
                .filter(event -> event.getDateTime().isBefore(LocalDateTime.now().plus(5, ChronoUnit.MINUTES)))
                .forEach(event -> main.addReadyEvent(event));

        System.out.println("Handle schedule");
        if (!main.isActiveReminderDialog() && !main.getReadyEvent().isEmpty()){
            System.out.println("Shoving dialog");
            main.setActiveReminderDialog(true);
            main.showReminderDialog();
        }
    }

    public Main getMain() {
        return main;
    }

}
