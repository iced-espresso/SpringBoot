package com.espresso.springboot.utils;

public class StringMasker {
    private String maskingStr;
    private int len;
    public StringMasker(String str){
        maskingStr = str;
        len = maskingStr.length();
        maskingStr = masking();
    }

    public String toString(){
        return maskingStr;
    }

    private Boolean isMasking(int idx){
        if(len < 3)
            return idx == len-1; // 2글자 이하일 경우 마지막 글자 마스킹
        else
            return (0<idx && idx < len-1); // 3글자 이상일 경우, 중간 글자 마스킹
    }

    private String masking(){
        StringBuilder maskStringBuilder = new StringBuilder(maskingStr);
        for(int idx=0;idx<maskStringBuilder.length();idx++)
        {
            if(isMasking(idx)){
                maskStringBuilder.setCharAt(idx, '*');
            }
        }
        maskingStr = maskStringBuilder.toString();
        return maskingStr;
    }

}
