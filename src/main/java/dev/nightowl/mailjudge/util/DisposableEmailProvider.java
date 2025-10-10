package dev.nightowl.mailjudge.util;

import lombok.extern.java.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Fetches and caches disposable email domains from GitHub.
 * Uses local file caching to minimize network requests.
 */
@Log
public class DisposableEmailProvider {
    private static final String GITHUB_URL = 
        "https://raw.githubusercontent.com/disposable-email-domains/disposable-email-domains/master/disposable_email_blocklist.conf";
    
    private static final String CACHE_DIR = System.getProperty("user.home") + "/.mailjudge";
    private static final String CACHE_FILE = "disposable-domains.txt";
    private static final Duration CACHE_DURATION = Duration.ofDays(7);
    
    private static volatile Set<String> cachedDomains;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    /**
     * Gets the set of disposable email domains.
     * Uses cached data if available and fresh, otherwise fetches from GitHub.
     * 
     * @return set of disposable domains
     */
    public static Set<String> getDisposableDomains() {
        lock.readLock().lock();
        try {
            if (cachedDomains != null) {
                return new HashSet<>(cachedDomains);
            }
        } finally {
            lock.readLock().unlock();
        }
        
        lock.writeLock().lock();
        try {
            // Double-check after acquiring write lock
            if (cachedDomains != null) {
                return new HashSet<>(cachedDomains);
            }
            
            cachedDomains = loadDomains();
            return new HashSet<>(cachedDomains);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    /**
     * Forces a refresh of the disposable domains list from GitHub.
     */
    public static void refresh() {
        lock.writeLock().lock();
        try {
            cachedDomains = fetchFromGitHub();
            saveToCacheFile(cachedDomains);
        } catch (Exception e) {
            log.warning("Failed to refresh disposable domains: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    private static Set<String> loadDomains() {
        Path cacheFilePath = getCacheFilePath();
        if (Files.exists(cacheFilePath)) {
            try {
                Instant lastModified = Files.getLastModifiedTime(cacheFilePath).toInstant();
                Duration age = Duration.between(lastModified, Instant.now());
                
                if (age.compareTo(CACHE_DURATION) < 0) {
                    log.fine("Loading disposable domains from cache");
                    return loadFromCacheFile(cacheFilePath);
                } else {
                    log.fine("Cache expired, fetching fresh data");
                }
            } catch (IOException e) {
                log.warning("Failed to read cache file: " + e.getMessage());
            }
        }
        
        try {
            Set<String> domains = fetchFromGitHub();
            saveToCacheFile(domains);
            return domains;
        } catch (Exception e) {
            log.warning("Failed to fetch from GitHub: " + e.getMessage());
            return getDefaultDomains();
        }
    }
    
    private static Set<String> fetchFromGitHub() throws IOException {
        log.info("Fetching disposable domains from GitHub");
        Set<String> domains = new HashSet<>();
        
        HttpURLConnection conn = (HttpURLConnection) new URL(GITHUB_URL).openConnection();
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setRequestProperty("User-Agent", "MailJudge/1.0");
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    domains.add(line);
                }
            }
        } finally {
            conn.disconnect();
        }
        
        log.info("Fetched " + domains.size() + " disposable domains");
        return domains;
    }
    
    private static Set<String> loadFromCacheFile(Path path) throws IOException {
        Set<String> domains = new HashSet<>();
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            line = line.trim().toLowerCase();
            if (!line.isEmpty() && !line.startsWith("#")) {
                domains.add(line);
            }
        }
        return domains;
    }
    
    private static void saveToCacheFile(Set<String> domains) {
        try {
            Path cacheDir = Paths.get(CACHE_DIR);
            Files.createDirectories(cacheDir);
            
            Path cacheFilePath = getCacheFilePath();
            Files.write(cacheFilePath, domains, StandardCharsets.UTF_8);
            log.fine("Saved " + domains.size() + " domains to cache");
        } catch (IOException e) {
            log.warning("Failed to save cache file: " + e.getMessage());
        }
    }
    
    private static Path getCacheFilePath() {
        return Paths.get(CACHE_DIR, CACHE_FILE);
    }
    
    private static Set<String> getDefaultDomains() {
        Set<String> domains = new HashSet<>();
        domains.add("10minutemail.com");
        domains.add("guerrillamail.com");
        domains.add("mailinator.com");
        domains.add("tempmail.com");
        domains.add("throwaway.email");
        domains.add("temp-mail.org");
        domains.add("getnada.com");
        domains.add("maildrop.cc");
        domains.add("trashmail.com");
        domains.add("yopmail.com");
        return domains;
    }
}
