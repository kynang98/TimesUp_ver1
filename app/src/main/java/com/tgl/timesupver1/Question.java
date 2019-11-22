package com.tgl.timesupver1;

import java.util.LinkedList;

public class Question {
    public String question_no;
    public String question;
    public LinkedList<String> options = new LinkedList<String>();
    public String answer;

    public void set_no(String question_no){
        this.question_no = question_no;
    }

    public void set_question(String question){
        this.question = question;
    }

    public void add_option(String option){
        this.options.add(option);
    }

    public void set_answer(String answer){
        this.answer = answer;
    }
}
