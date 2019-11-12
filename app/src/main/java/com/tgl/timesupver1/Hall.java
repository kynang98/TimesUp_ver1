package com.tgl.timesupver1;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Hall {
    private int Hall_ID;
    private String Hall_Code;
    private String Invigilator_ID;
    private Date Time_Created;
    private boolean Status;
    private String Question_Code;

    Random rand = new Random();
    public Hall(String Invigilator_ID){
        this.Invigilator_ID = Invigilator_ID;
        Hall_Code = Invigilator_ID + rand.nextInt(10);
        Time_Created = Calendar.getInstance().getTime();

    }



}
