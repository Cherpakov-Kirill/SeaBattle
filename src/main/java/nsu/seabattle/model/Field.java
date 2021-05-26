package nsu.seabattle.model;

import nsu.seabattle.model.player.Shot;
import nsu.seabattle.model.ship.Position;
import nsu.seabattle.model.ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final char[][] field;
    private final int width;
    private final int height;

    private void clearField(){
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                field[i][j] = '-';
            }
        }
    }

    public Field(int width, int height) {
        this.width = width;
        this.height = height;
        this.field = new char[width][height];
        clearField();
    }

    public void addShip(Ship ship) {
        Position current;
        Position last;
        if (ship.start().x == ship.end().x) {
            if (ship.start().y < ship.end().y) {
                current = new Position(ship.start());
                last = ship.end();
            } else {
                current = new Position(ship.end());
                last = ship.start();
            }
            while(!current.equals(last)){
                field[current.y][current.x] = '#';
                current.y++;
            }
        } else {
            if (ship.start().x < ship.end().x) {
                current = new Position(ship.start());
                last = ship.end();
            } else {
                current = new Position(ship.end());
                last = ship.start();
            }
            while(!current.equals(last)) {
                field[current.y][current.x] = '#';
                current.x++;
            }
        }
        field[current.y][current.x] = '#';
    }

    public Shot shoot(Position position) {
        switch (field[position.y][position.x]) {
            case '#' -> {
                field[position.y][position.x] = '*';
                if (!injuredShipIsAlive(position)) {
                    setKilledShip(position);
                    return Shot.KILL;
                }
                return Shot.HIT;
            }
            case '~', '@', '*' -> {
                return Shot.REPEAT;
            }
            case '-' -> {
                field[position.y][position.x] = '~';
                return Shot.MISS;
            }
        }
        return Shot.REPEAT;
    }

    private void setKilledShip(Position position){
        if (position.x >= width || position.x < 0) return;
        if (position.y >= height || position.y < 0) return;
        if(field[position.y][position.x] == '*'){
            field[position.y][position.x] = '@';
            setKilledShip(new Position(position.x-1,position.y));
            setKilledShip(new Position(position.x+1,position.y));
            setKilledShip(new Position(position.x,position.y-1));
            setKilledShip(new Position(position.x,position.y+1));
        }
    }

    private boolean isAliveShip(List<Position> injuredPositions, Position position){
        if (position.x >= width || position.x < 0) return false;
        if (position.y >= height || position.y < 0) return false;
        if(field[position.y][position.x] == '#') return true;
        if (field[position.y][position.x] == '*') {
            if(!injuredPositions.contains(position)){
                injuredPositions.add(position);
                return isAliveShipAroundPosition(injuredPositions, position);
            }
        }
        return false;
    }

    private boolean isAliveShipAroundPosition(List<Position> injuredPositions, Position position){
        if(field[position.y][position.x] == '#') return true;
        if(isAliveShip(injuredPositions, new Position(position.x-1,position.y))) return true;
        if(isAliveShip(injuredPositions, new Position(position.x+1,position.y))) return true;
        if(isAliveShip(injuredPositions, new Position(position.x,position.y-1))) return true;
        return isAliveShip(injuredPositions, new Position(position.x, position.y + 1));
    }

    private boolean injuredShipIsAlive(Position position){
        List<Position> injuredPositions = new ArrayList<>();
        injuredPositions.add(position);
        return isAliveShipAroundPosition(injuredPositions, position);
    }

    public boolean hasAliveShips() {
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                if(field[i][j] == '#') return true;
            }
        }
        return false;
    }

    public String getField(boolean hideShips) {
        StringBuilder string = new StringBuilder();
        for(int i=0;i<height;i++){
            for(int j=0;j<width;j++){
                char sym = field[i][j];
                if(sym == '#' && hideShips) {
                    string.append('-');
                }
                else string.append(sym);
            }
        }
        return string.toString();
    }
}
