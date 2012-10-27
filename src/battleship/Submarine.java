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
public class Submarine extends Ship {
    /**
     * Constructor. Creates an instance of the ship
     *
     */
    
    private static Submarine submarine;
    
    private Submarine(String[] Coordinates) {
        super("Submarine" , 3, Coordinates);
    }
    
    private Submarine() {
        super("Submarine", 3);
    }
    
    public static synchronized Submarine getSubmarine(String[] Cords) {
        if (submarine == null) {
            submarine = new Submarine(Cords);
        }
        return submarine;
    }
    
    public static synchronized Submarine getSubmarine() {
        if (submarine == null) {
            submarine = new Submarine();
        }
        return submarine;
    }
    
    
}
