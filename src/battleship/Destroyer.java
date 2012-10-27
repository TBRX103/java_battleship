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

/**
 *
 * @author Ben
 */
public class Destroyer extends Ship {
    /**
     * Constructor. Creates an instance of the ship
     *
     */
    
    private static Destroyer destroyer;
    
    private Destroyer(String[] Coordinates) {
        super("Destroyer" , 3, Coordinates);
    }
    
    private Destroyer() {
        super("Destroyer", 3);
    }
    
    public static synchronized Destroyer getDestroyer(String[] Cords) {
        if (destroyer == null) {
            destroyer = new Destroyer(Cords);
        }
        return destroyer;
    }
    
    public static synchronized Destroyer getDestroyer() {
        if (destroyer == null) {
            destroyer = new Destroyer();
        }
        return destroyer;
    }
    
    
}
