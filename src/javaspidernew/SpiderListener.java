/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaspidernew;

import java.util.Set;

/**
 *
 * @author homefulloflove
 */
public interface SpiderListener {

    /**
     * Triger when source address finish scan.
     *
     * @param sourceAddress source address.
     * @param foundAddresses found addresses.
     */
    public void searchComplete(String sourceAddress, Set<String> foundAddresses);

}
