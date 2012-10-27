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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ben
 */

import java.awt.*;
import javax.swing.*;

/**
 * Player Grid. Used by the player to place ships, and view opponent attacks.
 * @author Ben
 */
public class PlayerGrid extends JFrame {
    
   static Connection c;
   static  GameLogic gl;
    
    static BattleshipButton[][] button;
    
    /**
     * Default constructor. Sets up the player's board.
     * @param oc Current Connection
     * @param og Current GameLogic
     */
    public PlayerGrid(Connection oc, GameLogic og) {
        this.setTitle("Your Board");
        c = oc;
        gl = og;
        
        og.setPlayerGrid(this);
        button = new BattleshipButton[gl.getRows()][gl.getColumns()];
       
        
        //this.setDefaultLookAndFeelDecorated(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new GridLayout(gl.getRows(),gl.getColumns()));
        
        for (int row = 0; row < gl.getRows(); row++) {
            for (int col =0; col < gl.getColumns(); col++) {
                button[row][col] = new BattleshipButton(row,col,0,c,gl);
                this.add(button[row][col]);
            }
        }
        
        this.pack();
        this.setVisible(true);
        gl.setPlayerButtons(button);
    }
    
    
        
       
    
    
    
}
