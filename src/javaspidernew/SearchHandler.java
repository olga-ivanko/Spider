/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaspidernew;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author homefulloflove
 */
public class SearchHandler implements SpiderListener, Runnable {

    private Set<String> allAddresses;
    private Set<String> visitedAddresses;

    private String host;

    public SearchHandler(String host) {
        this.host = host;
        this.allAddresses = Collections.synchronizedSet(new TreeSet<String>());
        this.visitedAddresses = Collections.synchronizedSet(new TreeSet<String>());
    }

    @Override
    public void run() {
        Sprider first = new Sprider("http://kneu.edu.ua/");
        Thread thrd = new Thread(first);
        thrd.start();
    }

    @Override
    public void searchComplete(String address, Set<String> foundAddresses) {
        visitedAddresses.add(address);
        allAddresses.addAll(foundAddresses);

        System.out.println("Search complete:");
        for (String addr : allAddresses) {
            System.out.println(addr);
        }

        Thread thrd = new Thread(this);
        thrd.start();
    }

}
