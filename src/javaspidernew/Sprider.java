/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaspidernew;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author homefulloflove
 */
public class Sprider implements Runnable{

    private final Pattern domainPattern = Pattern.compile("(http[s]?://)?([^/]+)(/.*)?");
    private final Pattern tagPattern = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
    private final Pattern hrefPattern = Pattern.compile("href\\s*=\\s*['\"]([^'\"]+)['\"]");
    private final Pattern addrPattern = Pattern.compile("(//)?(.+)");

    private String targetAddress;
    private String domain;
    private Set<String> subAddressSet;

    public Sprider(String address) {
        this.targetAddress = address;
        this.subAddressSet = Collections.synchronizedSet(new LinkedHashSet<String>());
    }

    @Override
    public void run() {
        domain = findDomain(targetAddress);
        System.out.println(domain);

        String allText = readPage(targetAddress);

        Set<String> addresses = findAddresses(allText);

        filterAddresses(addresses);

        for (String addr : subAddressSet) {
            System.out.println(addr);
        }

    }

    /**
     * Filter found addresses and add them to {@link #subAddressSet}.
     *
     * @param addresses found addresses.
     */
    private void filterAddresses(Set<String> addresses) {
        for (String addr : addresses) {
            if (addr.matches("^[http|https].*")) {
                if (addr.contains(domain)) {
                    subAddressSet.add(addr);
                }
            } else {
                Matcher matcher = addrPattern.matcher(addr);
                if (matcher.find()) {

                    String group = matcher.group(2);

                    if (!group.startsWith("#") && !group.startsWith("javascript")) {
                        subAddressSet.add(addr);
                    }
                }
            }
        }
    }

    /**
     * Retrieve domain name from address.
     *
     * @param sourceAddress source address.
     * @return domain name or source address if not found.
     */
    private String findDomain(String sourceAddress) {
        Matcher matcher = domainPattern.matcher(sourceAddress);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return sourceAddress;
    }

    /**
     * Read text from address.
     *
     * @param sourceAddress source address.
     * @return all read text.
     */
    private String readPage(String sourceAddress) {
        StringBuilder allText = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(sourceAddress).openStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allText.append(line).append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Sprider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allText.toString();
    }

    /**
     * Find tags and retrieve addresses.
     *
     * @param sourceText source text.
     * @return set of addresses.
     */
    private Set<String> findAddresses(String sourceText) {
        Set<String> links = new LinkedHashSet<>();

        Matcher match = tagPattern.matcher(sourceText);
        while (match.find()) {
            String foundAddress = retrieveAddress(match.group(1));
            if (foundAddress != null) {
                links.add(foundAddress);
            }
        }

        return links;
    }

    /**
     * Find address in attributes.
     *
     * @param sourceAttributes source attributes. return found address otherwise
     * {@code null}.
     */
    private String retrieveAddress(String sourceAttributes) {
        Matcher attrMatch = hrefPattern.matcher(sourceAttributes);

        if (attrMatch.find()) {
            return attrMatch.group(1).trim();
        }

        return null;
    }

}
