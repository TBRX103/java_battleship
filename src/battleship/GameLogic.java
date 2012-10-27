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
 * GameLogic class. This class is the main backbone of all logic based processing
 * required for the game to run according to the rules, as well as does some
 * process handling for sending out packets.
 * @author Ben
 */
public class GameLogic {

    static int gameStatus = 0; //0 = No Game in Progress, 1=Planning Phase (Place 
                        //ships, allow chatting, 3 = Player's Turn 4 = Opponent
                        //Turn 5 = Game over, player won 6 = Game over, player
                        //lost 
    
   
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_COLUMNS = 10;
    private static boolean GameReadyToStart=false;
    private static Destroyer des = Destroyer.getDestroyer();
    private static Cruiser cru = Cruiser.getCruiser();
    private static Submarine sub = Submarine.getSubmarine();
    private static Battleship bat = Battleship.getBattleship();
    private static Carrier car = Carrier.getCarrier();
    private static Connection c;
    private static ChatFrame cf;
    private static String firedCords;
    private static PlayerGrid pg;
    private static int lastButtonX;
    private static int lastButtonY;
    private static boolean buttonNoClick = true;
    private static boolean gameOver = true;
    private static BattleshipButton[][] playerButton;
    private static String[] Occupied = new String[50];
    private static int OccupiedCounter = 0;
    private static EnemyGrid eg;
    private static BattleshipButton[][] enemyButton;
    private static boolean PlayerShipsPlaced = false;
    private static boolean EnemyShipsPlaced = false;
  
    /**
     * Get the status identifier of the game
     * @return value of gameStatus
     */
    public int getStatus() {
        return gameStatus;
    }
    
    /**
     * Sets the value of the game status. Avoid usage where necessary not 
     * used currently.
     * @param x 
     */
    public void setStatus(int x) {
        gameStatus = x;
    }
    
    /**
     * Passes in reference to the current PlayerGrid
     * @param g PlayerGrid
     */
    public void setPlayerGrid (PlayerGrid g) {
        pg = g;
    }
    
    /**
     * Passes in reference to the current EnemyGrid
     * @param g EnemyGrid
     */
    
    public void setEnemyGrid  (EnemyGrid g) {
        eg = g;
    }
    
    /**
     * Checks to see if a game over has occurred clientside. If so, it will
     * continue processing the game over.
     */
    public static void checkGameOver() {
        if (des.isDestroyed() && cru.isDestroyed() &&
            sub.isDestroyed() && bat.isDestroyed() &&
            car.isDestroyed()) {
            gameStatus =6;
            gameOver = true;
            processGameOver(); }
    }
    
    /**
     * Forces a game over, whether invoked by a forefit or a natural game over.
     */
    public static void processGameOver() {
        c.sendPacket(String.valueOf(c.GAME_OVER_YOU_WIN));
        cf.appendChat("Game Over! Your fleet has been destroyed!");
        gameOver = true; 
        gameStatus = 6;
        
    }
    
    /**
     * Invoked when the player has won, sends a congratulatory message.
     */
    public void playerWins() {
        gameStatus = 5;
        cf.appendChat("Congratulations! You've won the battle!");
        gameOver=true;
    }
        
    /**
     * Invoked when a new game is scheduled to start. Buggy. Will cause the
     * program to crash after the first game is complete for unknown reasons
     * at this time.
     */
    public static void newGame() {
        gameStatus =1;
        cf.appendChat("A new game is in progress, place your ships!");
        GameReadyToStart = false;
        cru.reset();
        des.reset();
        sub.reset();
        bat.reset();
        car.reset();
        PlayerShipsPlaced=false;
        EnemyShipsPlaced=false;
        Occupied = new String[50];
        OccupiedCounter = 0;
        boolean cordsOK = false;
        getButtonCords(cru);
        getButtonCords(des);
        getButtonCords(sub);
        getButtonCords(bat);
        getButtonCords(car);
        
        playerHasPlacedShips();
        
    }
  
    /**
     * Starts the game and prepares the initial flow of the game.
     */
    public static void startGame() {
        cf.appendChat("The battle has begun!");
        if (c.isServer) {
        cf.appendChat("You go first!");
        gameStatus = 3; }
        else {
        cf.appendChat("Opponent has the first move...");
        gameStatus = 4; }
        
        
    }
    
    /**
     * Once the player has placed their ships, this handles all of the
     * needed processing.
     */
    private static void playerHasPlacedShips() {
        PlayerShipsPlaced = true;
        c.sendPacket(String.valueOf(c.STATUS_MESSAGE + "Enemy has placed their ships!"));
        c.sendPacket(String.valueOf(c.ENEMY_SHIPS_PLACED));
        if (EnemyShipsPlaced && PlayerShipsPlaced) {
            startGame();
        }
        
    }
    /**
     * Once the enemy has placed their ships, this handles all of the
     * needed processing.
     */
    
   public static void enemeyHasPlacedShips() {
       EnemyShipsPlaced = true;
       if (PlayerShipsPlaced && EnemyShipsPlaced) 
           startGame();
   }
   
   /**
    * Was meant to handle the first turn events, currently unused.
    */
   public static void firstTurn() {
       if (!c.isServer)
           return;
       
       
   }

   /** 
    * Passes in reference to the current Player Buttons on the screen.
    * @param b BatlteshipButtons array
    */
    public static void setPlayerButtons(BattleshipButton[][] b) {
        playerButton = b;
    }
   /** 
    * Passes in reference to the current Enemy Buttons on the screen.
    * @param b BatlteshipButtons array
    */
    public static void setEnemyButtons(BattleshipButton[][] b) {
        enemyButton = b;
    }
    
    /** 
     * Used get input from the user of where they would like to place the
     * starting point of their ship.
     * @param ship Current ship being processed.
     */    
    private static void getButtonCords(Ship ship) {

        boolean boundsOK = false;
        while (!boundsOK) {
        int x1,x2,y1,y2,size;
        size = ship.getMaxHealth();
        cf.appendChat("---Please place the starting point of: " 
                + ship.getName() + " ("+size+" spaces)");
        while (buttonNoClick) {
            
            try {
 
        Thread.currentThread().sleep(100); }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        x1 = lastButtonX;
        y1 = lastButtonY;
        
        playerButton[x1][y1].setStatus(1);
        playerButton[x1][y1].updateImg();
        
        cf.appendChat("---Please place the ending point of: " + ship.getName());
        
        
        
        buttonNoClick = true;
        
        while (buttonNoClick) {
            
            try {
                
        Thread.currentThread().sleep(100); }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        buttonNoClick = true;
        x2 = lastButtonX;
        y2 = lastButtonY;
        playerButton[x2][y2].setStatus(1);
        playerButton[x2][y2].updateImg();
        
        boundsOK = setBounds(x1,x2,y1,y2,size,ship);
        if (!boundsOK)
            cf.appendChat("Invalid placement, try again.");
                           }
        
           
    }

    /**
     * Checks the bounds of the user's input range. If the spot is available,
     * the ship will be placed. If not, the user will have to try again.
     * @param x1 Beginning X Point
     * @param x2 Ending X Point
     * @param y1 Beginning Y Point
     * @param y2 Ending Y Point
     * @param size Size of the ship
     * @param ship Actual ship object.
     * @return 
     */
    public static boolean setBounds(int x1, int x2, int y1, int y2, int size,Ship ship) {
         paintButtons();
         String Cords;
         String[] shipCords = new String[size];
         int shipCordsCounter =0;
        //Should never happen..but if it does..
        if((x1 > BOARD_ROWS || x1 < 0) || (x2 > BOARD_ROWS || x2 < 0) ||
           (y1 > BOARD_COLUMNS || y1 < 0) || (y2 > BOARD_COLUMNS || y2 < 0)) { 
            cf.appendChat("Piece placed out of existence...");
        
            return false; }
        
        if (x1 == x2) {
            
        if((y1 + size-1 == y2)) {
           for(;y1<=y2;y1++)  {
               
              Cords = x1 + "," +y1; 
               if(shipCordCheck(Cords))
                   return false;
                   
               playerButton[x1][y1].setStatus(1);
               playerButton[x1][y1].updateImg();
               shipCords[shipCordsCounter] = Cords;
               shipCordsCounter++;
               
           }
           ship.setCoordinates(shipCords);
           
            return true; }
            
        if (y1 - size+1 == y2 )  {
           for(;y1>=y2;y1--)  {
               
                Cords = x1 + "," +y1;
               if(shipCordCheck(Cords))
                   return false;
              
               playerButton[x1][y1].setStatus(1);
               playerButton[x1][y1].updateImg();
               shipCords[shipCordsCounter] = Cords;
               shipCordsCounter++;
               
           }
           ship.setCoordinates(shipCords);
            
            return true;
        } 
        
        }
        
        
        if (y1 ==y2) {
           if((x1 + size-1 == x2)) {
               
               
               
               for (; x1<=x2; x1++)  {
                Cords = x1 + "," +y1;
                if(shipCordCheck(Cords))
                   return false;
               playerButton[x1][y1].setStatus(1);
               playerButton[x1][y1].updateImg();
               shipCords[shipCordsCounter] =Cords;
               shipCordsCounter++;
               
           }
           ship.setCoordinates(shipCords);             
               
               
               
            return true;  } 
           
        if (x1 - size+1 == x2 ) {
               for (; x1>=x2; x1--)  {
               Cords = x1 + "," +y1;
               if(shipCordCheck(Cords))
                   return false;
               
               playerButton[x1][y1].setStatus(1);
               playerButton[x1][y1].updateImg();
               shipCords[shipCordsCounter] =Cords;
               shipCordsCounter++;
               
           }
           ship.setCoordinates(shipCords);             
               
               
               
            return true;  } 
        
        
        }
        
        ship.reset();
        
        return false;
        
    }
    /**
     * Invoked when a button is pressed. This allows the GameClient to receive
     * the coordinates of the button press.
     * @param x X position of button
     * @param y Y position of button
     */
    public static void setButtonCords(int x,int y) {
           lastButtonX = x;
           lastButtonY = y;
           buttonNoClick = false;
        
                
    }
    

    /**
     * Default constructor, passes in current Connection
     * @param con Current Connection
     */
    public  GameLogic(Connection con) {
        c=con;
        
    }
    
    /**
     * Sets the current chat frame.
     * @param c ChatFrame
     */
    public void setChatFrame(ChatFrame c) {
        cf = c;
    }
    
    /**
     * Used to get the amount of rows of the battlefield. Can be scaled.
     * @return # of Rows on the board
     */
    public int getRows() {
        return BOARD_ROWS;
    }
    /**
     * Used to get the amount of columns of the battlefield. Can be scaled.
     * @return # of Columns on the board
     */
    public int getColumns() {
        return BOARD_COLUMNS;
    }
    
    /**
     * checks to see if coordinates are in use by a ship
     * @param x
     * @param y
     * @return true if a ship is occupied, false if the spot is free
     */
    public static boolean shipCordCheck(String cords) {
         
        
        
        if (cru.checkCoordinates(cords))
            return true;
        if (des.checkCoordinates(cords))
            return true;
        if (sub.checkCoordinates(cords))
            return true;
        if (bat.checkCoordinates(cords))
            return true;
        if (car.checkCoordinates(cords))
            return true;
        
        
       return false;
        
    }
    
    /**
     * When the player fires an attack, this is invoked.
     * @param cords Coordinates of attack
     * @return returns 2 to mark the area has been attacked, will be updated
     * later if the result is a hit.
     */
    public static int playerFire(String cords) {
        firedCords = cords;
        int x=2;
        gameStatus = 4;
        c.sendPacket(String.valueOf(c.FIRE)+cords);
        
        return x;
        
    }
   
    /**
     * Updates the button that launched an attack. Sets the image of the button
     * corresponding to what has happened.
     * @param r value of results
     */
    public static void fireResults(int r) {
       int bx = Integer.parseInt(firedCords.substring(0,1));
       int by = Integer.parseInt(firedCords.substring(2,3));
       enemyButton[bx][by].setStatus(r);
       enemyButton[bx][by].updateImg();
    }
    
    /**
     * When the player has been attacked, this handles the buttons
     * and potential ship damage. Returns a string representing the events.
     * @param cord Coordinates of attack.
     * @return String of information regarding the outcome
     */
    public static String enemyFire(String cord) {
       boolean washit = false;
       int bx = Integer.parseInt(cord.substring(0,1));
       int by = Integer.parseInt(cord.substring(2,3));
       playerButton[bx][by].setStatus(2);
        
        String ret="Miss.";
        
        if (cru.enemyFire(cord)) {
            washit = true;
            playerButton[bx][by].setStatus(3);
            ret = "Hit!";
        if (cru.isDestroyed()) {
            ret+=" Cruiser Destroyed!";
        }
        }
        if (des.enemyFire(cord)) {
            washit = true;
            playerButton[bx][by].setStatus(3);
            ret = "Hit!";
        if (des.isDestroyed()) {
            ret+=" Destroyer Destroyed!";
        }
        }        
        if (sub.enemyFire(cord)) {
            washit = true;
            playerButton[bx][by].setStatus(3);
            ret = "Hit!";
        if (sub.isDestroyed()) {
            ret+=" Submarine Destroyed!";
        }
        }
        if (bat.enemyFire(cord)) {
            washit = true;
            playerButton[bx][by].setStatus(3);
            ret = "Hit!";
        if (bat.isDestroyed()) {
            ret+=" Battleship Destroyed!";
        }
        }        
        if (car.enemyFire(cord)) {
            washit = true;
            playerButton[bx][by].setStatus(3);
            ret = "Hit!";
        if (car.isDestroyed()) {
            ret+=" Carrier Destroyed!";
        }
        }
        
        String local = "Enemy scored a  " + ret;
        cf.appendChat(local);
        
        
        gameStatus=3;
        
        playerButton[bx][by].updateImg();
        
        if (washit) {
            c.sendPacket(String.valueOf(c.FIRE_HIT));
            
        }
        else 
            c.sendPacket(String.valueOf(c.FIRE_MISS));
        
        c.sendPacket(String.valueOf(c.STATUS_MESSAGE)+ret);
        checkGameOver();
     return ret;   
    }
    
    /**
     * Repaints all the buttons on the Player board
     */
    public static void paintButtons() {
        for (int x=0; x<BOARD_ROWS;x++) {
            for (int y=0;y<BOARD_COLUMNS;y++) {
                String c = x + "," + y;
                if(shipCordCheck(c)) {
                    playerButton[x][y].setStatus(1);
                    playerButton[x][y].updateImg();
                }
                else {
                    playerButton[x][y].setStatus(0);
                    playerButton[x][y].updateImg();
                }
            }
        }
    }

    
}
