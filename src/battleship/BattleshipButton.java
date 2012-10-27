/*
    Socket Battleship: A final project demonstrating my knowledgeability
    Copyright (C) 2011-2012  Benjamin Schellenberger

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package battleship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *BattleshipButton class. Used to give a virtual visual display of the internal
 * programming, as well as accept user input.
 * @author Ben
 */
public class BattleshipButton extends JButton {
    boolean hasBeenClicked= false;
    Connection con;
    static GameLogic gl;
    final int butX;
    final int butY;
    private int status = 0; // 0 Water, 1 Ship, 2 Miss, 3 ShipHit
    final private int type; // 0 = friendly, 1 = enemy
    private ImageIcon water = new ImageIcon(getClass().getResource("water.png"));
    private ImageIcon ship = new ImageIcon(getClass().getResource("ship.png"));
    private ImageIcon hit = new ImageIcon(getClass().getResource("hit.png"));
    private ImageIcon miss = new ImageIcon(getClass().getResource("miss.png"));
    ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
             


                switch (gl.getStatus()) {
                   
                    case 1:
                        if (type!=0)
                            return;
                        
                        gl.setButtonCords(butX,butY);
                        break;
                    
                    case 3:
                        if (type!=1)
                            return;
                        if (hasBeenClicked)
                            return;
                        
                        String ret = butX + "," + butY;
                        status = gl.playerFire(ret);
                        updateImg();
                        hasBeenClicked=true;
                        break; 
                
                   
                }
                
                
                
                
                
                return;}
    };
    
    /**
     * Creates an instance of BattleshipButton
     * @param x1 X Location of Button
     * @param y1 Y Location of Button
     * @param t Type of button, 0 for friendly, 1 for enemy
     * @param c Passes in the value of reference to the Connection.
     * @param g Passes in the value of the reference to the GameLogic.
     */
    public BattleshipButton(int x1,int y1, int t, Connection c, GameLogic g) {
        butX = x1;
        butY = y1;
        type = t;
        this.updateImg();
        this.setPreferredSize(new Dimension(32,32));
        this.addActionListener(listener);
        con = c;
        gl = g;  
        GameLogic gl = g;
    }
   /**
     * Creates an instance of BattleshipButton with only x and y coordinates.
     * Used for debugging purposes.
     * @param x1 X Location
     * @param y1 Y Location
     */
        public BattleshipButton(int x1,int y1) {
        butX = x1;
        butY = y1;
       // type = t;
        type = 0;
        this.updateImg();
        this.setPreferredSize(new Dimension(32,32));
        this.addActionListener(listener);
       // con = c;
       // gl = g;
        
            
        
        
    }
    /**
         * Sets status of this button
         * 
         * @param x 0 Water, 1 Ship, 2 Miss, 3 ShipHit
         */
public void setStatus(int x) {
        status = x;
        
    }
    /**
     * Used to redraw the image on the button.
     */
public void updateImg() {
        switch (status) {
           case 0:
               this.setIcon(water);
           break;
            case 1:
                this.setIcon(ship);
            break;
            case 2:
                this.setIcon(miss);
            break;
            case 3:
                this.setIcon(hit);
                
        }
    }
    
/**
 * Used to receive the coordinates of a button.
 * @return String, in the format of "X,Y";
 */
    public String getCoordinates() {
        return butX + "," + butY;
    }
    

}
