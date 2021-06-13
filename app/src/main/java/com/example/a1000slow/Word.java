package com.example.a1000slow;

public class Word {
    private String enWord;
    private String enWordVoiceFileName;
    private String plWord;
    private String plWordVoiceFileName;

    public Word(String enWord, String enWordVoiceFileName, String plWord, String plWordVoiceFileName){
        this.enWord = enWord;
        this.enWordVoiceFileName = enWordVoiceFileName;
        this.plWord = plWord;
        this.plWordVoiceFileName = plWordVoiceFileName;
    }

    public String getEnWord(){
        return  enWord;
    }

    public String getEnWordVoiceFileName() {
        return enWordVoiceFileName;
    }

    public String getPlWord() {
        return plWord;
    }

    public String getPlWordVoiceFileName() {
        return plWordVoiceFileName;
    }
}
