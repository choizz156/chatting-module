package me.choizz.chattingredismodule.session;

import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Set;
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

    public boolean isEmpty() {
        return sessionKeyStore.isEmpty();
    }

    public Enumeration<String> getKeys() {
        return sessionKeyStore.keys();
    }

    public void removeEntry(final String key) {
        Set<Entry<String, String>> entries = sessionKeyStore.entrySet();
        entries.removeIf(e -> e.getValue().equals(key));
    }

    public String findKey(final String value) {
        Set<Entry<String, String>> entries = sessionKeyStore.entrySet();

        return
            entries.stream()
                .filter(e -> e.getValue().equals(value))
                .map(Entry::getKey)
                .findAny().orElse("null");
    }
}
