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
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;


/**
 *
 * @author Ben
 */
public class Connection extends Thread  {
    public final static int PLAYER_READY = 400;
    public final static int PLAYER_TURN = 410;
    public final static int ENEMY_TURN = 420;
    public final static int ENEMY_SHIPS_PLACED = 250;
    public final static int GAME_OVER_YOU_WIN= 999;
    public final static int GAME_OVER_PLAYER_LOSES = 900;
    public final static int FIRE = 600;
    public final static int FIRE_HIT=601;
    public final static int FIRE_MISS=602;
    public final static int CHAT = 300;
    public final static int START_GAME=666;
    public final static int STATUS_MESSAGE=404;
    public final static int NEW_GAME=800;
    private String sentCords;
    private String recievedCords;
    int output;
    boolean status;
    DatagramSocket socket = null;  
    boolean packetReady = false;
    InetAddress address;
    boolean isServer;
    DatagramPacket outPacket = null;
    DatagramPacket inPacket = null;
    int clientPort = 4445;
    int serverPort = 6000;
    int buffersize = 256;
    byte[] buf = new byte[buffersize];
    static ChatFrame cf;
    String tempString;
    static PlayerGrid pg;
    static EnemyGrid eg;
    static GameLogic gl;
    public Connection(boolean server) {
        isServer = server;
        if (!isServer) {
          try {
             String temp = JOptionPane.showInputDialog("What IP address"
                     + ", domain, or hostname do you wish to connect to?");
            address = InetAddress.getByName(temp);
            
            try {
            socket = new DatagramSocket(clientPort); }
            catch (SocketException e) {
                e.printStackTrace();
            }       
            
            
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
          tempString = "100";
          buf = tempString.getBytes();
          outPacket = new DatagramPacket (buf,buf.length,address,serverPort);
          
          try {
              socket.send(outPacket);
          }
          
          catch (IOException e) {
              e.printStackTrace();
          }
          
     
        
        }
        else {
            System.out.println("Waiting for connection...");
            try {
            socket = new DatagramSocket(serverPort);
            inPacket = new DatagramPacket(buf,buf.length);      
            socket.receive(inPacket);
            System.out.println("Got connection packet!");
            address = inPacket.getAddress();
            clientPort = inPacket.getPort();
            tempString = "101";
            buf = tempString.getBytes();
            outPacket = new DatagramPacket(buf,buf.length,address,clientPort);
            socket.send(outPacket);
            }
            catch (IOException e) {
               e.printStackTrace();
              
            }
            
           
        
        }
        
        gl = new GameLogic(this);
        cf = new ChatFrame(this,gl);
        cf.setVisible(true);
        this.start();
        pg = new PlayerGrid(this, gl);
        eg = new EnemyGrid(this, gl);
        gl.setChatFrame(cf);
        gl.newGame();
       
    }
    /**
     * Called by the ChatFrame class to start a new game
     * and is bugged. Does not work, causes the program to lock up
     * if executed after a game has ended.
     * Unable to determine. Do not use.
     */
 public void newGame() {
     gl.newGame();
 }    
 //All the packet handling
 
 /**
  * Parses the data of an incoming packet.
  * Packet data are strings that contain a 3 digit 
  * identifier, and are then parsed out and the data
  * is passed to where it needs to go, or methods invoked
  * @param data Byte array of a string who's contents is "XXXDATAHERE"
  * where XXX is the 3 digit identifier.
  */
 public void parsePacket(byte[] data) {
 
     String temp = new String(data);
     String subString = temp.substring(0, 3);
     int sw = Integer.parseInt(subString); 
     switch (sw) {
         case CHAT:
             cf.appendChat(temp.replaceFirst(String.valueOf(CHAT), "Enemy:"));
             break;
         case STATUS_MESSAGE:
             cf.appendChat(temp.replaceFirst(String.valueOf(STATUS_MESSAGE),""));
             break;
         case ENEMY_SHIPS_PLACED:
             gl.enemeyHasPlacedShips();
             break;
         case FIRE:
            recievedCords = temp.replaceFirst(String.valueOf(FIRE),"");
            String temp1 = gl.enemyFire(recievedCords.trim());
            
             break;
         case FIRE_HIT:
             gl.fireResults(3);
             break;
         case FIRE_MISS:
             gl.fireResults(2);
             break;
         case GAME_OVER_YOU_WIN:
             gl.playerWins();
             break;
         case NEW_GAME:
             gl.newGame();
             break;
             
             
     }
    
 }
 
 /**
  * Sends a packet to the currently established connection.
  * @param data String that consists of a 3 digit identifier followed by data
  * Ex: "300This is a chat message".
  * @return returns 1 if successful;
  */
 public int sendPacket(String data) {
          
          byte[] tempBuf = new byte[buffersize];
          tempBuf = data.getBytes();
          outPacket.setData(tempBuf);
          try {
              socket.send(outPacket);
          }
          catch (IOException e) {
              e.printStackTrace();
              //sendPacket(data);   
              System.err.println("ERROR: Unable to send packet");
          }
          return 1;
      }
        
 /**
  * Used to stop the thread from running. Currently unimplemented.
  */
public void end() {
        
    status = false;
}
    
/**
 * Main thread execution. Its main concern is packet handling.
 */
public void run() {

    while (status = true) {
            
            try {
                byte[] tempBuf = new byte[buffersize];
                inPacket = new DatagramPacket(tempBuf, tempBuf.length);
                socket.receive(inPacket);           
           tempBuf =  inPacket.getData();
           parsePacket(tempBuf);
            }
            
            catch (IOException e) {
                e.printStackTrace();
                
            }        
            
        }
    
        }
   
    }

    


