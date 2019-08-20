package com.driveme.driveme;

public class SelectedTime {
    public static String getSelectedTime() {
        return selectedTime;
    }

    public static void setSelectedTime(String selectedTime) {
        SelectedTime.selectedTime = selectedTime;
    }

    private static String selectedTime;
}
