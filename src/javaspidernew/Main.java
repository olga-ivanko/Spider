/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaspidernew;

/**
 *
 * @author homefulloflove
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      SearchHandler first = new SearchHandler("http://kneu.edu.ua/");
      
      Thread thrd = new Thread(first);
      thrd.start();
    }
    
}
