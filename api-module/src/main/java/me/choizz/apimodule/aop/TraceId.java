package me.choizz.apimodule.aop;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum TraceId {
    TRACE_ID("trace.id");

    private final String name;

   public static String get(){
       return TRACE_ID.getName();
   }

   private String getName() {
        return name;
    }
}
