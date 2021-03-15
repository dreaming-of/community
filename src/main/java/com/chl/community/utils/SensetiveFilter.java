package com.chl.community.utils;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensetiveFilter {
    private class TrieNode {
        private boolean hasEnd = false;
        private Map<Character, TrieNode> sons = new HashMap<>();

        public boolean isHasEnd() {
            return hasEnd;
        }

        public void setHasEnd(boolean hasEnd) {
            this.hasEnd = hasEnd;
        }

        public void addSon(Character c, TrieNode node) {
            sons.put(c, node);
        }

        public TrieNode getSon(Character key) {
            return sons.get(key);
        }
    }

    private final static Logger log = LoggerFactory.getLogger(SensetiveFilter.class);

    private final static String REPLACEMENT = "***";

    private TrieNode root = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败： " + e.getMessage());
        }

    }

    private void addKeyword(String keyword) {
        TrieNode t = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode son = t.getSon(c);
            if (son == null) {
                son = new TrieNode();
                t.addSon(c, son);
            }
            t = son;
        }
        t.setHasEnd(true);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text))
            return null;

        TrieNode t = root;
        int l = 0;
        int r = 0;
        StringBuilder sb = new StringBuilder();
        while (r < text.length()) {
            char c = text.charAt(r);
            if (isSymbol(c)) {
                if (t == root) {
                    sb.append(c);
                    ++l;
                }
                ++r;
                continue;
            }

            t = t.getSon(c);
            if (t == null) {
                sb.append(c);
                r = ++l;
                t = root;
            } else if (t.isHasEnd()) {
                sb.append(REPLACEMENT);
                l = ++r;
                t = root;
            } else {
                r ++;
            }
        }
        sb.append(text.substring(l));
        return sb.toString();
    }

    private boolean isSymbol(Character c) {
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2e80 || c > 0x9fff);
    }
}
