import java.util.Stack;
import BasicIO.*;
/*COSC 1P03
 *ASSIGNMENT #5
 *Name: Angus Webb
 *Student #: 7575178
 *
 * Class KeyChain: accepts a text file maze and uses recursion to locate the missing keys.
 *
 */
public class Keychain {
    char[][] plans; //the array for the floor plans
    int startRow; //Starting row
    int startCol; //Starting column
    Stack<Character> directions= new Stack<>(); //keeps track of directions taken to reach keys

    private void loadFloorPlans() { //loads floor plans into the char array
        ASCIIDataFile file = new ASCIIDataFile();
        String row;
        int height = file.readInt();
        int width = file.readInt();
        plans = new char[height][width]; //create room of height * width
        for (int i=0; i<height; i++) {
            row = file.readLine(); //read in a row from the floor
            plans[i] = row.toCharArray(); //convert row to array and store in its corresponding index
            if (row.indexOf('M')>=0) {
                startRow = i; //find column and row where M is, and start there
                startCol = row.indexOf('M');
                System.out.println("Start at "+startRow+","+startCol+".");
            }
        }
        System.out.println("File transcription complete!\n");
    }
    private void solve() {
        System.out.println("Initial State:");
        printFloorPlans();

        if (recursiveSolve(startRow, startCol)) { //if the keys are ultimately found...
            System.out.println("\nFinal Layout:"); //...print out the new layout
            printFloorPlans();
            System.out.print("Findeth yon keys: ");
            System.out.println(directions); //print directions to keys
        }
        else { //if keys are not located
            System.out.println("\nOh no! The keys are lost to us!");
            printFloorPlans(); //Displaying anyway, since we presumably modified the floor plans
        }
        System.exit(0);
    }
    private boolean recursiveSolve(int x, int y) { //x: row, y: column

        int[] xMoves = {x-1, x, x+1, x}; //the x positions of each move in order U,R,D,L
        int[] yMoves = {y, y+1, y, y-1}; //the y positions of each move in order U,R,D,L
        char[] moveType = {'1', '2', '3', '4'};  //1 for up, 2 for right, 3 for down, 4 for left
        char[] moveType2 = {'U', 'R', 'D', 'L'}; //to add to stack of directions, for printing purposes

        for (int i = 0; i < 4; i++) { //4 times, for each direction
            if (plans[xMoves[i]][yMoves[i]] == 'K'){ //if the key is found, return true
                plans[x][y] = moveType[i]; //replace current position with 1-4 depending on how you got to new position
                directions.push(moveType2[i]); //add the corresponding move letter to the directions stack
                return true;
            }
            if (plans[xMoves[i]][yMoves[i]] == 'S'){ //if an S is found,
                plans[x][y] = moveType[i]; //replace current position with 1-4 depending on how you got to new position
                directions.push(moveType2[i]); //add the corresponding move letter to the directions stack
                return recursiveSolve(xMoves[i], yMoves[i]);
            }
        }
        /** If the method gets to this point, that means we're at a dead end and have to backtrack.*/
        for (int i = 0; i < 4; i++) {
            char spot = plans[xMoves[i]][yMoves[i]]; //the next spot we're looking for
            if (spot == '1' || spot == '2' || spot == '3' || spot == '4') {
                plans[x][y] = 'O'; //change current spot to an obstacle
                if (!directions.empty()){
                    directions.pop(); //pop the last direction off the stack since we're backtracking
                }
                return recursiveSolve(xMoves[i], yMoves[i]);
            }
        }
        /** If the method gets to this point, then the keys aren't in this room.*/
        return false;
    }
    private void printFloorPlans() { //prints out floor plan
        for (char[] row:plans) {
            for (char c:row) System.out.print(c);
            System.out.println();
        }
    }
    public static void main(String[] args){
        Keychain key = new Keychain();
        key.loadFloorPlans();
        key.solve();
    }
}