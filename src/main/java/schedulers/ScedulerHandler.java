package schedulers;

import app.Main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScedulerHandler {

    private Main main;

    public ScedulerHandler(Main main) {
        this.main = main;
    }

    public void setupScedulerHandler() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new ScedulerHandlerTask(this), 0, 30, TimeUnit.SECONDS);
    }

    public Main getMain() {
        return main;
    }

    private class ScedulerHandlerTask implements Runnable{

        private ScedulerHandler scedulerHandler;

        public ScedulerHandlerTask(ScedulerHandler scedulerHandler) {
            this.scedulerHandler = scedulerHandler;
        }

        @Override
        public void run() {
            scedulerHandler.getMain().getEventData().stream().filter(event -> !event.isDone())
                    .filter(event -> event.getDateTime().isBefore(LocalDateTime.now().plus(5, ChronoUnit.MINUTES)))
                    .forEach(event -> scedulerHandler.getMain().addReadyEvent(event));

            if (!scedulerHandler.getMain().getReadyEvent().isEmpty()){
                scedulerHandler.getMain().showReminderDialog();
            }
        }

    }
}
