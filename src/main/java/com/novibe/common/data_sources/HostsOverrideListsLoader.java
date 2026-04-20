package com.novibe.common.data_sources;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class HostsOverrideListsLoader extends ListLoader<HostsOverrideListsLoader.BypassRoute> {

    public record BypassRoute(String ip, String website) {
    }

    @Override
    protected String listType() {
        return "Override";
    }

    @Override
    protected Predicate<String> filterRelatedLines() {
        return line -> line != null
                && !line.isBlank()
                && !line.startsWith("#")
                && !HostsBlockListsLoader.isBlock(line);
    }

    @Override
    protected BypassRoute toObject(String line) {
        if (line == null) return null;

        line = line.trim();
        if (line.isEmpty() || line.startsWith("#")) return null;

        String[] parts = line.split("\\s+");
        if (parts.length < 2) return null;

        String ip = parts[0];
        String website = removeWWW(parts[1]);

        return new BypassRoute(ip, website);
    }

}
