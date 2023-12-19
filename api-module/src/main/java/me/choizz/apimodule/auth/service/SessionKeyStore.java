package me.choizz.apimodule.auth.service;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SessionKeyStore {

    private final ConcurrentHashMap<String, String> sessionKeyStore = new ConcurrentHashMap<>();

    public void removeValue(final String email) {
        sessionKeyStore.remove(email);
    }

    public void addValue(final String email, final String sessionKey) {
        sessionKeyStore.put(email, sessionKey);
    }

    public String getValue(final String email) {
       return sessionKeyStore.get(email);
    }

    public boolean isEmpty(){
        return sessionKeyStore.isEmpty();
    }

    public Enumeration<String> getKeys(){
        return sessionKeyStore.keys();
    }
}
