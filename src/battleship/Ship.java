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
public abstract class Ship {
   
  final private int MaxHealth;  
  final static private String DAMAGE_NAME = "hit";
  final static private String OK_NAME = "ok";

  private String name;
  private int CurrentHealth;
  private String[][] Coordinates;

    
    /**
     * Constructor. Yay, we have a ship!
     *
     * @param n Name of the ship
     * @param h Health of the ship
     * @param Cords Coordinates of the ship
     */
  

    public Ship(String n, int h,String[] Cords) {
       
        MaxHealth = h;
        CurrentHealth = MaxHealth;
        name = n;
        Coordinates = new String [2][MaxHealth];
        
        for (int i = 0; i != Cords.length; i++) {
            Coordinates[0][i] = Cords [i]; 
            Coordinates[1][i] = OK_NAME; }
        
    
    }
    /**
     * Constructor, used to set the name and max health only.
     * @param n Name of ship
     * @param h Health of ship
     */
    public Ship(String n, int h) {
        MaxHealth = h;
        CurrentHealth = MaxHealth;
        name = n;
        Coordinates = new String [2][MaxHealth];
    }
    /**
     * Recalculates the health for the ship using the coordinate arrays
     */
    private void calculateHealth() { 
      int TempHealth = MaxHealth;
        for (int i = 0; i != Coordinates[1].length; i++) {
            if (Coordinates[1][i].compareTo(DAMAGE_NAME) == 0)
                TempHealth--; }
        
       CurrentHealth = TempHealth;
        
          }
    
    /**
     * Checks to see if see the ship has been destroyed
     * 
     * @return true if destroyed, false if not
     */
    public boolean isDestroyed() {
    
        if (CurrentHealth != 0)
            return false;
        else
            return true;
    }
    
    /**
     * Used to determine if opponent's move hits this ship. 
     * Will set the ship as hit in the array.
     * 
     * @param xy Opponent's attack coordinates
     * @return true if hit, false if miss.
     */
    public boolean checkCoordinates(String xy){        
        
        for (int i = 0; i < MaxHealth;i++) {
            
            
            
            //Should never happen, but if it does, stops execution.
            //1 of 1 Security Check
            if (Coordinates[1][i].compareToIgnoreCase(DAMAGE_NAME) == 0)
                return false;
           
            if (xy.compareToIgnoreCase(Coordinates[0][i]) == 0) {
                return true;
            }
            
        }
        
        return false;
        
    }
    
    public boolean enemyFire(String xy){        
        System.out.println(dumpCoordinates());
        for (int i = 0; i < MaxHealth;i++) {
            if (xy.compareToIgnoreCase(Coordinates[0][i]) == 0) {
                Coordinates[1][i] = DAMAGE_NAME;
                this.calculateHealth();
                return true;
            }
        }
        
        return false;
        
    }
    /**
     * Dumps the coordinates of the ship, and whether they've been hit or not.
     * 
     * @return {Coordinate,Status} for each coordinate.
     */
    public String dumpCoordinates() {
        
        String Out="";
        for (int i = 0; i != Coordinates[0].length; i++) 
            Out+= "{"+ Coordinates[0][i] + "," + Coordinates[1][i] + "} ";
        return Out;
    }
    
    /**
     * Returns the current health of this ship.
     * @return Health of ship
     */
    public int getHealth() {
        return CurrentHealth;
    }
    /**
     * Returns Maximum health, or Length of the ship
     * @return Health of the ship
     */
    public int getMaxHealth() {
        return MaxHealth;
    }
    
    /**
     * Passes in a String array of the coordinates of the ship
     * @param Cords 
     */
    public void setCoordinates(String[] Cords) {
       
        for (int i = 0; i != this.MaxHealth; i++) {
            Coordinates[0][i] = Cords[i]; 
            Coordinates[1][i] = OK_NAME;;
        }
        
    }
    /**
     * Gets name of the ship
     * @return Ship's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Disallows cloning this object, in order to
     * continue the Singleton pattern design
     */ 
    public Object clone() 
        throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    
       }
    
    /**
     * Clears the coordinates of this ship. Used for either a new game or to
     * clear an invalid coordinate entry.
     * @return 
     */
    private boolean clearCoordinates() {
          for (int i = 0; i != this.MaxHealth; i++) {
            Coordinates[0][i] = ""; 
            Coordinates[1][i] = OK_NAME; }
          return true;
    }
    
    /**
     * Resets the ship.
     */
    public void reset() {
        clearCoordinates();
        CurrentHealth = MaxHealth;
    }
   
    
}
