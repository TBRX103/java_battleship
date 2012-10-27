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
import java.awt.*;
import javax.swing.*;

/**
 * EnemyGrid Class. Used by the player to fire shots on the enemy.
 * @author Ben
 */
public class EnemyGrid extends JFrame {
    
   static Connection c;
   static  GameLogic gl;
    
    static BattleshipButton[][] button;
    
    /**
     * Default constructor
     * @param oc Value of reference of Connection
     * @param og Value of reference of GameLogic
     */
    public EnemyGrid(Connection oc, GameLogic og) {
        this.setTitle("Enemy Board");
        c = oc;
        gl = og;
        
        og.setEnemyGrid(this);
        button = new BattleshipButton[gl.getRows()][gl.getColumns()];
       
        
        //this.setDefaultLookAndFeelDecorated(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new GridLayout(gl.getRows(),gl.getColumns()));
        
        for (int row = 0; row < gl.getRows(); row++) {
            for (int col =0; col < gl.getColumns(); col++) {
                button[row][col] = new BattleshipButton(row,col,1,c,gl);
                this.add(button[row][col]);
            }
        }
        
        this.pack();
        this.setVisible(true);
        gl.setEnemyButtons(button);
    }
    
    
        
       
    
    
    
}
