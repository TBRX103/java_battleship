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
public class Cruiser extends Ship {
    /**
     * Constructor. Creates an instance of the ship
     *
     */
    
    private static Cruiser cruiser;
    
    private Cruiser(String[] Coordinates) {
        super("Cruiser" , 2, Coordinates);
    }
    
    private Cruiser() {
        super("Cruiser", 2);
    }
    
    public static synchronized Cruiser getCruiser(String[] Cords) {
        if (cruiser == null) {
            cruiser = new Cruiser(Cords);
        }
        return cruiser;
    }
    
    public static synchronized Cruiser getCruiser() {
        if (cruiser == null) {
            cruiser = new Cruiser();
        }
        return cruiser;
    }
    
    
}
