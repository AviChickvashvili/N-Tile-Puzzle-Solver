public class node {
	// Node class represents game state
	// for example each node will save his parent state,
	// and its current state 
	node parent;
	int h;
	int w;
	int[][]  current_state ;
    int cost;
    String path;
    int distance;
    boolean out;
    
    node(int[][] arr){
    	this.current_state = arr;
    	this.path="";
    	out = false;
    }
    
    public String toString() {
    	String text = "";
    	for (int i = 0; i < current_state.length; i++) {
			for (int j = 0; j < current_state[0].length; j++) {
				text+=current_state[i][j]+" ";
			}
			text+='\n';
    	}
    return text;
    }
    
	
}
