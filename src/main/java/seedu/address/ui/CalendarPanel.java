package seedu.address.ui;

import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

import seedu.address.model.events.EventSource;

/**
 * An Ui that stores the logged feedback from the program to the user.
 */
public class CalendarPanel extends UiPart<Region> {

    private static final String FXML = "CalendarPanel.fxml";

    private UiParser uiParser;
    private YearMonth yearMonth;
    private LocalDate calendarDate;
    private ArrayList<DayCard> currentMonthDayCards;

    @FXML
    private GridPane calendar;

    @FXML
    private Label month;

    /**
     * Constructor for ListPanel. Stores the event list, and task list[in v2.0].
     */
    public CalendarPanel(ObservableList<EventSource> eventList, UiParser uiParser) {
        super(FXML);
        this.yearMonth = YearMonth.now();
        this.calendarDate = LocalDate.now();
        this.currentMonthDayCards = new ArrayList<DayCard>();
        this.uiParser = uiParser;
        setCurrentMonth();
        fillIndexOfCalendar(calendarDate.getDayOfWeek().getValue(), eventList);
        createListener(eventList);
    }

    /**
     * Sets the label to be the current Month.
     */
    private void setCurrentMonth() {
        String currentMonth = yearMonth.getMonth().toString().toLowerCase();
        currentMonth = currentMonth.substring(0, 1).toUpperCase() + currentMonth.substring(1);
        month.setText(currentMonth);
    }

    /**
     * Fills the index of the calendar.
     * @param startingDay Indicates the starting day of the week
     * @param eventList Contains the EventSource
     */
    private void fillIndexOfCalendar(int startingDay, ObservableList<EventSource> eventList) {
        int index = 1;
        int totalDays = yearMonth.lengthOfMonth();
        for (int weeks = 0; weeks < 6; weeks++) {
            for (int days = 0; days < 7; days++) {
                if (weeks == 0 && days == 0) {
                    days = startingDay - 3;
                    continue;
                }
                if (index > totalDays) {
                    break;
                }
                DayCard todayCard = new DayCard(index, yearMonth.getMonthValue(), yearMonth.getYear(), eventList);
                calendar.add(todayCard.getRoot(), days, weeks);
                currentMonthDayCards.add(todayCard);
                index++;
            }
        }
    }

    private void createListener(ObservableList<EventSource> eventList) {
        eventList.addListener(new ListChangeListener<EventSource>() {
            @Override
            public void onChanged(Change<? extends EventSource> c) {
                System.out.println("I've just added");
                while (c.next()) {
                    for (EventSource addItem : c.getAddedSubList()) {
                        // Adds items
                        DayCard dayCard = currentMonthDayCards.get(
                                uiParser.getDay(addItem.getStartDateTime().getDateTime()) - 1);
                        if (dayCard.sameDateAsEvent(addItem, uiParser)) {
                            dayCard.addEventLabel(addItem);
                        }
                    }
                }
            }
        });
    }


}
