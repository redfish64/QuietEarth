package com.rareventure.quietcraft;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utils for strings and regexs
 */
public class StringUtil {
    /**
     * Given a regex, or an exact matching token, returns the list that matches it.
     * Specifically, if the regex exactly matches an item from stream, returns a
     * single item list containing it. Otherwise returns a list of all regex matches.
     *
     * @param regex
     * @param choices
     * @return
     */
    public static List<String> findRegexMatches(String regex, Collection<? extends String> choices) {
        if(choices.stream().anyMatch(v -> v.equals(regex)))
            return Arrays.asList(regex);

        Pattern p = Pattern.compile(regex);
        List<String> l = choices.stream().
                filter(k2 -> p.matcher(k2).find()).collect(Collectors.toList());

        return l;
    }

    public static String findSingleRegex(String regex, Collection<? extends String> s) {
        List<String> l = findRegexMatches(regex, s);
        if(l.size() == 1)
            return l.get(0);
        return null;
    }
}
