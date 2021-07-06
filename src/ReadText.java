

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.Stack;

public class ReadText {
	//////////////////////
	// global variables //
	//////////////////////
	Hashtable<String, node > op_list = new Hashtable<String, node>();
	Hashtable<String, node > cl_list = new Hashtable<String, node>();
	Queue<node> qu = new LinkedList<node>();

	boolean isCutoff= false;
	String cutoff = "false";
	String result = "";

	static int counnnnnnt = 0;
	static int LEFT =5;
	static int UP =6;
	static int RIGHT =7;
	static int DOWN =8;

	boolean isClose=false;

	String file;
	String ans;
	int count_lines;
	int helper;
	int helper2;
	String start ;
	String goal ;

	int costs;
	String algo;
	boolean with_time;
	boolean with_open;
	int h;
	int w;
	int[][]  start_state ;
	int[][]  goal_state ;
	int[][] answer =new int[h][w];
	//////////////////////


	//constructor
	public ReadText(String file) {
		this.file = file;
		this.ans="";
		start ="";
		goal="";
	}

	/*
	 this function reads the input file 
	 and saves the required information 
	 to be able to start the algorithms.
	 */
	public void printText() {
		try {
			File myObj = new File(file);
			Scanner myReader = new Scanner(myObj);

			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(count_lines==0) {
					data=data.trim();
					algo=data;
					count_lines++;
				}	
				else if(count_lines==1) {
					if(data.contains("with")) {
						with_time=true;

					}
					else {
						with_time=false;
					}
					count_lines++;
				}
				else if(count_lines==2) {
					if(data.contains("no")) {
						with_open=false;
					}
					else {
						with_open=true;
					}
					count_lines++;


				}
				else if(count_lines==3) {
					String [] size = new String[2];
					size = data.split("x");
					h=Integer.parseInt(size[0]);
					w=Integer.parseInt(size[1]);

					start_state = new int[h][w];
					goal_state = new int[h][w];

					helper=count_lines+h+1;
					count_lines++;

				}
				else if((count_lines) < helper ) {
					start +=data+'\n';
					count_lines++;


				}
				else if(data.contains("Goal") ) {
					helper2= count_lines+h+1;
					count_lines++;

				}
				else if(count_lines<helper2) {
					goal +=data+'\n';
					count_lines++;

				}


			}

			for (int i = 0; i < start_state.length; i++) {
				for (int j = 0; j < start_state[0].length; j++) {

					int count=0;

					while(start.charAt(count)!='\n') {
						if(start.charAt(count)=='_') 
						{

							start_state[i][j]=-1;
							count++;
							j++;
							continue;
						}
						if(start.charAt(count)==',') 
						{
							count++;
						}
						else 
						{
							String num = "";
							while(start.charAt(count)!=',' && start.charAt(count)!='\n') {
								num+=start.charAt(count);
								count++;
							}
							int x1 = Integer.parseInt(num);
							start_state[i][j]=x1;
							j++;
						}
					}
					start = start.substring(count+1);
				}
			}

			for (int i = 0; i < goal_state.length; i++) {
				for (int j = 0; j < goal_state[0].length; j++) {

					int count=0;

					while(goal.charAt(count)!='\n') {
						if(goal.charAt(count)=='_') 
						{

							goal_state[i][j]=-1;
							count++;
							j++;
							continue;
						}
						if(goal.charAt(count)==',') 
						{
							count++;
						}
						else 
						{
							String num = "";
							while(goal.charAt(count)!=',' && goal.charAt(count)!='\n') {
								num+=goal.charAt(count);
								count++;
							}
							int x1 = Integer.parseInt(num);
							goal_state[i][j]=x1;
							j++;
						}
					}
					goal = goal.substring(count+1);
				}
			}

			count_lines++;
			myReader.close();

			// here we call the requested algoritham
			// that was given from the input file
			switch (algo) {
			case "BFS":
				BFS(start_state, goal_state);
				break;
			case "DFID":
				DFID(start_state, goal_state);
			case "A*":
				A_STAR(start_state, goal_state);
				break;
			case "IDA*":
				IDA_STAR(start_state, goal_state);
				break;
			case "DFBnB":
				DFBNB(start_state, goal_state);
				break;
			default:
				break;
			}



		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}
	/*
	 * this funciton checks all the possible moves 
	 * thats the current game state given can do.
	 * we will use this function for all the developed nodes
	 * and check their possible and legal movements
	 * we return the moves in array of size 12
	 * which every place resambles a movement
	 * for example 
	 * arr[0] != -1  means the given state can do the move LEFT LEFT
	 * arr[4] == -1 means the given state cant do the move LEFT (with the first '_')
	 */
	public int[] check_moves(int[][] curr_state) {
		int[] arr = new int[12];
		// LL - UU - RR - DD
		arr[0]=-1;
		arr[1]=-1;
		arr[2]=-1;
		arr[3]=-1;
		isClose=false;
		boolean flag =false;
		for (int i = 0; i < curr_state.length; i++) {
			for (int j = 0; j < curr_state[i].length; j++) {
				if(curr_state[i][j]==-1 && flag==false) {
					//trying check close
					if(i-1>=0 && curr_state[i-1][j]==-1) {
						isClose=true;
					}else if(i+1<curr_state.length && curr_state[i+1][j]==-1) {
						isClose=true;
					}else if(j-1>=0 && curr_state[i][j-1]==-1) {
						isClose=true;
					}else if(j+1<curr_state[0].length && curr_state[i][j+1]==-1) {
						isClose=true;
					}


					// DOWN
					if(i-1>=0 && curr_state[i-1][j]!=-1) {
						arr[7]=8;
					}
					else {
						arr[7]=-1;
					}
					// UP
					if(i+1<curr_state.length && curr_state[i+1][j]!=-1) {
						arr[5]=6;
					}else {
						arr[5]=-1;
					}
					// RIGHT
					if(j-1>=0 && curr_state[i][j-1]!=-1) {
						arr[6]=7;
					}else {
						arr[6]=-1;
					}
					// LEFT
					if(j+1<curr_state[0].length && curr_state[i][j+1]!=-1) {
						arr[4]=5;
					}else {
						arr[4]=-1;
					}
					flag= true;

				}
				else if(curr_state[i][j]==-1 && flag==true ) {
					// DOWN
					if(i-1>=0 && curr_state[i-1][j]!=-1) {
						arr[11]=12;
					}
					else{
						arr[11]=-1;
					}
					// UP
					if(i+1<curr_state.length && curr_state[i+1][j]!=-1) {
						arr[9]=10;
					}else {
						arr[9]=-1;
					}
					// RIGHT
					if(j-1>=0 && curr_state[i][j-1]!=-1) {
						arr[10]=11;
					}else {
						arr[10]=-1;
					}
					// LEFT
					if(j+1<curr_state[0].length && curr_state[i][j+1]!=-1) {
						arr[8]=9;
					}else {
						arr[8]=-1;
					}
				}
			}
		}

		if(arr[4]!=-1 && arr[8]!=-1 && isClose==true) {
			arr[0]=1;
		}
		if(arr[5]!=-1 && arr[9]!=-1 && isClose==true) {
			arr[1]=2;
		}
		if(arr[6]!=-1 && arr[10]!=-1 && isClose==true) {
			arr[2]=3;
		}
		if(arr[7]!=-1 && arr[11]!=-1 && isClose==true) {
			arr[3]=4;
		}

		return arr;
	}
	/*
	 * just the BFS algorithm using the pseudo code from class
	 */
	public void BFS(int[][] curr , int[][] goal) {


		long startTime = System.currentTimeMillis();

		node start = new node(curr);
		start.path="";
		counnnnnnt++;
		qu.add(start);

		while(!qu.isEmpty()) {

			start = qu.poll();

			op_list.put(start.toString(), start);

			isClose = false;
			/*
			 * checking possible moves
			 */
			int[] moves = check_moves(start.current_state);
			
			int _i = -1 ,_j=-1 ,_k=-1 ,_l=-1;
			/*
			 * checking where are the '_'
			 * and how many of them exists
			 * and saves their indexes
			 */
			for (int i = 0; i < start.current_state.length; i++) {
				for (int j = 0; j < start.current_state[0].length; j++) {
					if(start.current_state[i][j]==-1 && (_i==-1 && _j==-1) ) {
						_i=i;
						_j=j;
					}
					if(start.current_state[i][j]==-1 && (_i!=i || _j!=j) ) {
						_k=i;
						_l=j;
					}
				}
			}

			/*
			 * going through the array returned from checkmoves() function
			 * every slot in array resembles possible move if its != -1
			 */	
			for (int i = 0; i < moves.length; i++) {
				if(moves[i]!=-1) {
					/*
					 * if the move is possible we call swapDouble() or swap()
					 * and update the matrix and the inddexes of '_'
					 */
					if(i<=3 && isClose==true) {
						answer = swapDouble(start.current_state, _i, _j, _k, _l , moves[i]);
					}
					if(i>3 && i<8) {
						answer = swap(start.current_state, _i, _j, moves[i]);
					}
					if(i>=8) {
						answer = swap(start.current_state, _k, _l, moves[i]);
					}
					/*
					 * creating the node from the movement we just made
					 * its the son of its current father
					 * meaning its a new game state , because we made a move on the board
					 */
					node temp = new node(answer);
					temp.parent=start;
					/*
					 * calculating the path 
					 */
					if(i<=3 && isClose==true) {
						if(moves[i]==1) {
							temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_k]+"L-";
						}else if(moves[i]==2) {
							temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"U-";		
						}else if(moves[i]==3) {
							temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"R-";
						}else if(moves[i]==4) {
							temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"D-";
						}
					}
					if(i>3 && i<8) {
						if(moves[i]==5) {
							temp.path+=temp.parent.path+answer[_i][_j]+"L-";
						}else if(moves[i]==6) {
							temp.path+=temp.parent.path+answer[_i][_j]+"U-";		
						}else if(moves[i]==7) {
							temp.path+=temp.parent.path+answer[_i][_j]+"R-";
						}else if(moves[i]==8) {
							temp.path+=temp.parent.path+answer[_i][_j]+"D-";
						}
					}
					if(i>=8) {
						if(moves[i]==9) {
							temp.path+=temp.parent.path+answer[_k][_l]+"L-";
						}else if(moves[i]==10) {
							temp.path+=temp.parent.path+answer[_k][_l]+"U-";		
						}else if(moves[i]==11) {
							temp.path+=temp.parent.path+answer[_k][_l]+"R-";
						}else if(moves[i]==12) {
							temp.path+=temp.parent.path+answer[_k][_l]+"D-";
						}
					}

					/*
					 * Calculating cost
					 */
					if(moves[i]>=5) {
						temp.cost=temp.parent.cost+5;
					}else if(moves[i]==1 || moves[i]==3) {
						temp.cost=temp.parent.cost+6;
					}else if(moves[i]==2 || moves[i]==4) {
						temp.cost=temp.parent.cost+7;
					}
					/*
					 * a little optimization if '_' is not on the same places as given in goal state
					 * we skip this process
					 * saves runtime
					 */
					if(null == op_list.get(temp.toString()) && null == cl_list.get(temp.toString()))
					{
						boolean flag = false; 

						int _ii= 0 ,_jj=0 ,_kk=0 ,_ll=0;
						switch(moves[i]) {
						case 1:
							_jj=_j+1;
							_ll=_l+1;

							_kk=_k;
							_ii=_i;
							break;
						case 2:
							_ii=_i+1;
							_kk=_k+1;

							_jj=_j;
							_ll=_l;
							break;
						case 3:
							_jj=_j-1;
							_ll=_l-1;

							_kk=_k;
							_ii=_i;
							break;
						case 4:
							_ii=_i-1;
							_kk=_k-1;	

							_jj=_j;
							_ll=_l;
							break;
						case 5:
							_jj=_j+1;

							_kk=_k;
							_ll=_l;
							_ii=_i;
							break;
						case 6:
							_ii=_i+1;

							_jj=_j;
							_kk=_k;
							_ll=_l;
							break;
						case 7:
							_jj=_j-1;

							_ii=_i;
							_kk=_k;
							_ll=_l;
							break;
						case 8:
							_ii=_i-1;

							_jj=_j;
							_kk=_k;
							_ll=_l;
							break;
						case 9:
							_ll=_l+1;

							_kk=_k;
							_ii=_i;
							_jj=_j;
							break;
						case 10:
							_kk=_k+1;

							_ll=_l;
							_ii=_i;
							_jj=_j;
							break;
						case 11:
							_ll=_l-1;

							_kk=_k;
							_ii=_i;
							_jj=_j;
							break;
						case 12:
							_kk=_k-1;

							_ll=_l;
							_ii=_i;
							_jj=_j;
							break;
						default:
						}
						/*
						 * just psudo code conditons
						 */
						if(_k!=-1 && _l!=-1 && goal_state[_ii][_jj]==-1 && goal_state[_kk][_ll]==-1) {
							flag = check_goal(temp.current_state);

						}
						else if(_k!=-1 && _l!=-1 && goal_state[_kk][_ll]==-1) {
							flag = check_goal(temp.current_state);

						}
						else if(_i!=-1 && _j!=-1 && goal_state[_ii][_jj]==-1) {
							flag = check_goal(temp.current_state);

						}
						/*
						 * we found the goal state
						 */
						if(flag==true) {
							try {
								FileWriter myWriter = new FileWriter("output.txt");
								counnnnnnt++;

								temp.path = temp.path.substring(0,temp.path.length()-1);

								System.out.println(temp.path);
								myWriter.write(temp.path+"\n");

								System.out.println("Num: "+counnnnnnt);
								myWriter.write("Num: "+counnnnnnt+"\n");

								System.out.println("Cost: "+temp.cost);
								myWriter.write("Cost: "+temp.cost);

								if(with_time==true) {
									long endTime   = System.currentTimeMillis();
									System.out.println("time: " +(endTime - startTime)/1000F);
									myWriter.write("\ntime: "+(endTime - startTime)/1000F);
								}

								myWriter.close();
							}catch (Exception e) {
								// TODO: handle exception
							}


							return;
						}else {
							counnnnnnt++;
							if(with_open==true) {
								System.out.println(temp.toString());
							}
							op_list.put(temp.toString() , temp);
							qu.add(temp);

						}		
					}				
				}	
			}
			if(with_open==true) {
				System.out.println( start.toString());
			}
			op_list.remove(start.toString());
			cl_list.put(start.toString(), start);

		}
	}
	public void DFID(int[][] curr , int[][] goal) {
		long startTime = System.currentTimeMillis();
		int depth = 1;
		node start = new node(curr);
		while(cutoff.equals("false")) {
			Hashtable<String, node> hash_DFID = new Hashtable<String, node>();
			/*
			 * call to the recursive function
			 */
			cutoff = limited_DFS(start , goal ,depth , hash_DFID );
//			System.out.println(depth);
			if(cutoff!="false" && cutoff!="true") {
				try {
					FileWriter myWriter = new FileWriter("output.txt");

					cutoff = cutoff.substring(0,cutoff.length()-1);

					System.out.println(cutoff);
					myWriter.write(cutoff+"\n");

					System.out.println("Num: "+counnnnnnt);
					myWriter.write("Num: "+counnnnnnt+"\n");

					System.out.println("Cost: "+costs);
					myWriter.write("Cost: "+costs);

					if(with_time==true) {
						long endTime   = System.currentTimeMillis();
						System.out.println("time: " +(endTime - startTime)/1000F);
						myWriter.write("\ntime: "+(endTime - startTime)/1000F);
					}

					myWriter.close();
				}catch (Exception e) {
					// TODO: handle exception
				}
				break;
			}
			else {
				cutoff="false";
			}
			depth++;
		}


	}
	public String limited_DFS(node curr , int[][] goal , int depth , Hashtable<String, node> hash_DFID) {
		if(check_goal(curr.current_state)==true) {
			return curr.path;
		}
		else {
			if(depth == 0) {
				return cutoff;
			}else {

				hash_DFID.put(curr.toString(), curr);
				cutoff = "false";

				isClose = false;

				/*
				 * checking possible moves
				 */
				int[] moves = check_moves(curr.current_state);
				int _i = -1 ,_j=-1 ,_k=-1 ,_l=-1;
				/*
				 * checking where are the '_'
				 * and how many of them exists
				 * and saves their indexes
				 */
				for (int i = 0; i < curr.current_state.length; i++) {
					for (int j = 0; j < curr.current_state[0].length; j++) {
						if(curr.current_state[i][j]==-1 && (_i==-1 && _j==-1) ) {
							_i=i;
							_j=j;
						}
						if(curr.current_state[i][j]==-1 && (_i!=i || _j!=j) ) {
							_k=i;
							_l=j;
						}
					}
				}
				/*
				 * going through the array returned from checkmoves() function
				 * every slot in array resembles possible move if its != -1
				 */
				for (int i = 0; i < moves.length; i++) {
					if(moves[i]!=-1 && moves[i]!=0) {
						/*
						 * if the move is possible we call swapDouble() or swap()
						 * and update the matrix and the inddexes of '_'
						 */
						if(i<=3 && isClose==true) {
							answer = swapDouble(curr.current_state, _i, _j, _k, _l , moves[i]);
						}
						if(i>3 && i<8) {
							answer = swap(curr.current_state, _i, _j, moves[i]);
						}
						if(i>=8) {
							answer = swap(curr.current_state, _k, _l, moves[i]);
						}
						/*
						 * creating the node from the movement we just made
						 * its the son of its current father
						 * meaning its a new game state , because we made a move on the board
						 */
						node temp = new node(answer);
						temp.parent=curr;
						/*
						 * calculating the path 
						 */
						if(i<=3 && isClose==true) {
							if(moves[i]==1) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_k]+"L-";
							}else if(moves[i]==2) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"U-";		
							}else if(moves[i]==3) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"R-";
							}else if(moves[i]==4) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"D-";
							}
						}
						if(i>3 && i<8) {
							if(moves[i]==5) {
								temp.path+=temp.parent.path+answer[_i][_j]+"L-";
							}else if(moves[i]==6) {
								temp.path+=temp.parent.path+answer[_i][_j]+"U-";		
							}else if(moves[i]==7) {
								temp.path+=temp.parent.path+answer[_i][_j]+"R-";
							}else if(moves[i]==8) {
								temp.path+=temp.parent.path+answer[_i][_j]+"D-";
							}
						}
						if(i>=8) {
							if(moves[i]==9) {
								temp.path+=temp.parent.path+answer[_k][_l]+"L-";
							}else if(moves[i]==10) {
								temp.path+=temp.parent.path+answer[_k][_l]+"U-";		
							}else if(moves[i]==11) {
								temp.path+=temp.parent.path+answer[_k][_l]+"R-";
							}else if(moves[i]==12) {
								temp.path+=temp.parent.path+answer[_k][_l]+"D-";
							}
						}

						/*
						 * Calculating cost
						 */
						if(moves[i]>=5) {
							temp.cost=temp.parent.cost+5;
						}else if(moves[i]==1 || moves[i]==3) {
							temp.cost=temp.parent.cost+6;
						}else if(moves[i]==2 || moves[i]==4) {
							temp.cost=temp.parent.cost+7;
						}				

						/*
						 * just pseudo code conditions
						 */
						if(hash_DFID.get(temp.toString())!=null) {
							continue;
						}
						else {
							result = limited_DFS(temp, goal, depth-1, hash_DFID);
						}
						if(result == cutoff) {
							cutoff= "true";
						}else if(result!="false") {
							return result;
						}
					}
				}
				hash_DFID.remove(curr.toString());
				if(cutoff=="true") {
					return cutoff;
				}
				else {
					return "false";
				}
			}
		}
	}
	/*
	 * generic swap fucntion only swaps one '_'
	 */
	public int[][] swap(int[][] arr , int i , int j ,int move){

		int[][] ans = copy_arr(arr); 

		if(move==5) {
			int help = ans[i][j];
			ans[i][j]= ans[i][j+1];
			ans[i][j+1] = help;
		}else if(move==6) {
			int help = ans[i][j];
			ans[i][j]= ans[i+1][j];
			ans[i+1][j] = help;
		}else if(move==7) {

			int help = ans[i][j];
			ans[i][j]= ans[i][j-1];
			ans[i][j-1] = help;

		}else if(move==8) {

			int help = ans[i][j];
			ans[i][j]= ans[i-1][j];
			ans[i-1][j] = help;
		}
		if(move==9) {
			int help = ans[i][j];
			ans[i][j]= ans[i][j+1];
			ans[i][j+1] = help;
		}else if(move==10) {
			int help = ans[i][j];
			ans[i][j]= ans[i+1][j];
			ans[i+1][j] = help;
		}else if(move==11) {

			int help = ans[i][j];
			ans[i][j]= ans[i][j-1];
			ans[i][j-1] = help;

		}else if(move==12) {

			int help = ans[i][j];
			ans[i][j]= ans[i-1][j];
			ans[i-1][j] = help;
		}


		return ans;

	}
	/*
	 * generic swap fucntion only swaps two
	 * when they are close '_'   or  '_''_'
	 * 					   '_'
	 */
	public int[][] swapDouble(int[][] arr , int i , int j ,int k , int l ,int move){
		int[][] ans = copy_arr(arr); 

		if(move==1) {
			int help = ans[i][j];
			ans[i][j]= ans[i][j+1];
			ans[i][j+1] = help;

			int help2 = ans[k][l];
			ans[k][l]= ans[k][l+1];
			ans[k][l+1]= help2;

		}else if(move==2) {

			int help = ans[i][j];
			ans[i][j]= ans[i+1][j];
			ans[i+1][j] = help;

			int help2 = ans[k][l];
			ans[k][l]= ans[k+1][l];
			ans[k+1][l] = help2;
		}else if(move==3) {

			int help = ans[i][j];
			ans[i][j]= ans[i][j-1];
			ans[i][j-1] = help;

			int help2 = ans[k][l];
			ans[k][l]= ans[k][l-1];
			ans[k][l-1] = help2;

		}else if(move==4) {

			int help = ans[i][j];
			ans[i][j]= ans[i-1][j];
			ans[i-1][j] = help;

			int help2 = ans[k][l];
			ans[k][l]= ans[k-1][l];
			ans[k-1][l] = help2;
		}



		return ans;
	}
	/*
	 * generic function that copies the array into new array
	 */
	public int[][] copy_arr(int[][] arr){
		int[][] copy = new int[arr.length][arr[0].length];

		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				copy[i][j]=arr[i][j];
			}
		}
		return copy;
	}
	/*
	 * generic function checks if the given array is 
	 * the same as the goal state arry , if it does returns true
	 * else false
	 */
	public boolean check_goal(int[][] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[0].length; j++) {
				if(arr[i][j]!=goal_state[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	/*
	 * Calculates manhattan for every block in the array
	 * whats its distance from his desired place in the goal array
	 */
	public int heuristic(int[][] curr) {

		int total_cost = 0;
		for (int i = 0; i < curr.length; i++) {
			for (int j = 0; j < curr[0].length; j++) {

				int temp = curr[i][j];

				for (int k = 0; k < goal_state.length; k++) {
					for (int l = 0; l < goal_state[0].length; l++) {
						//goal 	
						if(goal_state[k][l]==temp ) {//&& temp!=-1
							int distance = Math.abs(i-k) + Math.abs(j-l); 
							total_cost+=distance;
						}
					}
				}
			}
		}

		return total_cost;

	}
	public void A_STAR(int[][] curr, int[][] goals ) {

		long startTime = System.currentTimeMillis();

		Hashtable<String, node> op_list = new Hashtable<String, node>();
		Hashtable<String, node> cl_list = new Hashtable<String, node>();
		PriorityQueue<node> pq = new PriorityQueue<node>(new nodeComperator());

		node start = new node(curr);
		start.path="";
		start.distance=0;
		pq.add(start);

		while(!pq.isEmpty()) {
			start = pq.poll();
			/*
			 * we found goal state
			 */
			if(check_goal(start.current_state)==true) {

				try {
					FileWriter myWriter = new FileWriter("output.txt");
					start.path = start.path.substring(0,start.path.length()-1);
					counnnnnnt++;
					System.out.println(start.path);
					myWriter.write(start.path+"\n");

					System.out.println("Num: "+counnnnnnt);
					myWriter.write("Num: "+counnnnnnt+"\n");

					System.out.println("Cost: "+start.cost);
					myWriter.write("Cost: "+start.cost);

					if(with_time==true) {
						long endTime   = System.currentTimeMillis();
						System.out.println("time: " +(endTime - startTime)/1000F);
						myWriter.write("\ntime: "+(endTime - startTime)/1000F);
					}

					myWriter.close();
				}catch (Exception e) {
					// TODO: handle exception
				}

				break;
			}else {
				op_list.remove(start.toString());
				cl_list.put(start.toString(), start);
				counnnnnnt++;

				/*
				 * checking possible moves
				 */
				int[] moves = check_moves(start.current_state);
				int _i = -1 ,_j=-1 ,_k=-1 ,_l=-1;
				/*
				 * checking where are the '_'
				 * and how many of them exists
				 * and saves their indexes
				 */
				for (int i = 0; i < start.current_state.length; i++) {
					for (int j = 0; j < start.current_state[0].length; j++) {
						if(start.current_state[i][j]==-1 && (_i==-1 && _j==-1) ) {
							_i=i;
							_j=j;
						}
						if(start.current_state[i][j]==-1 && (_i!=i || _j!=j) ) {
							_k=i;
							_l=j;
						}
					}
				}

				if(_i==0 && _j==1 && _k==1 && _l ==0) {
					int x2=2;
				}
				/*
				 * going through the array returned from checkmoves() function
				 * every slot in array resembles possible move if its != -1
				 */
				for (int i = 0; i < moves.length; i++) {
					if(moves[i]!=-1) {
						/*
						 * if the move is possible we call swapDouble() or swap()
						 * and update the matrix and the inddexes of '_'
						 */
						if(i<=3 && isClose==true) {
							answer = swapDouble(start.current_state, _i, _j, _k, _l , moves[i]);
						}
						if(i>3 && i<8) {
							answer = swap(start.current_state, _i, _j, moves[i]);
						}
						if(i>=8) {
							answer = swap(start.current_state, _k, _l, moves[i]);
						}
						/*
						 * creating the node from the movement we just made
						 * its the son of its current father
						 * meaning its a new game state , because we made a move on the board
						 */
						node temp = new node(answer);
						temp.parent=start;
						/*
						 * calculating the path 
						 */						
						if(i<=3 && isClose==true) {
							if(moves[i]==1) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_k]+"L-";
							}else if(moves[i]==2) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"U-";		
							}else if(moves[i]==3) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"R-";
							}else if(moves[i]==4) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"D-";
							}
						}
						if(i>3 && i<8) {
							if(moves[i]==5) {
								temp.path+=temp.parent.path+answer[_i][_j]+"L-";
							}else if(moves[i]==6) {
								temp.path+=temp.parent.path+answer[_i][_j]+"U-";		
							}else if(moves[i]==7) {
								temp.path+=temp.parent.path+answer[_i][_j]+"R-";
							}else if(moves[i]==8) {
								temp.path+=temp.parent.path+answer[_i][_j]+"D-";
							}
						}
						if(i>=8) {
							if(moves[i]==9) {
								temp.path+=temp.parent.path+answer[_k][_l]+"L-";
							}else if(moves[i]==10) {
								temp.path+=temp.parent.path+answer[_k][_l]+"U-";		
							}else if(moves[i]==11) {
								temp.path+=temp.parent.path+answer[_k][_l]+"R-";
							}else if(moves[i]==12) {
								temp.path+=temp.parent.path+answer[_k][_l]+"D-";
							}
						}
						/*
						 * Calculating cost
						 */						
						if(moves[i]>=5) {
							temp.cost=temp.parent.cost+5;
						}else if(moves[i]==1 || moves[i]==3) {
							temp.cost=temp.parent.cost+6;
						}else if(moves[i]==2 || moves[i]==4) {
							temp.cost=temp.parent.cost+7;
						}

						temp.distance = (int)(temp.cost*0.195+ 0.75*heuristic(temp.current_state));
						/*
						 * just pseudo code conditions
						 */
						if(null == op_list.get(temp.toString()) && null == cl_list.get(temp.toString())) {
							//							temp.distance = (int)(temp.cost*0.2+ 0.8*heuristic(temp.current_state));
							op_list.put(temp.toString() , temp);
							pq.add(temp);
						}
						else if(null != op_list.get(temp.toString())){
							node temp2 = op_list.get(temp.toString());
							if(temp.distance<temp2.distance) {
								pq.remove(temp2);
								pq.add(temp);
								op_list.put(temp2.toString(),temp);
							}
						}	
					}
				}
			}
		}
	}
	public void IDA_STAR(int[][] curr, int[][] goals) {

		long startTime = System.currentTimeMillis();

		Hashtable<String, node> op_list = new Hashtable<String, node>();
		Stack<node> stk = new Stack<node>();
		node start = new node(curr);
		start.distance =heuristic(curr); 

		int t = start.distance;

		while(t!=Integer.MAX_VALUE) {
			op_list.put(start.toString(),start);
			stk.add(start);
			int MINF = Integer.MAX_VALUE;

			while(!stk.isEmpty()) {
				start = stk.pop();
				if(start.out==true) {
					op_list.remove(start.toString());
				}
				else {
					start.out=true;
					op_list.put(start.toString(), start);
					stk.add(start);
					/*
					 * checking possible moves
					 */
					int[] moves = check_moves(start.current_state);
					int _i = -1 ,_j=-1 ,_k=-1 ,_l=-1;
					/*
					 * checking where are the '_'
					 * and how many of them exists
					 * and saves their indexes
					 */
					for (int i = 0; i < start.current_state.length; i++) {
						for (int j = 0; j < start.current_state[0].length; j++) {
							if(start.current_state[i][j]==-1 && (_i==-1 && _j==-1) ) {
								_i=i;
								_j=j;
							}
							if(start.current_state[i][j]==-1 && (_i!=i || _j!=j) ) {
								_k=i;
								_l=j;
							}
						}
					}
					/*
					 * going through the array returned from checkmoves() function
					 * every slot in array resembles possible move if its != -1
					 */
					for (int i = 0; i < moves.length; i++) {
						if(moves[i]!=-1 && moves[i]!=0) {
							/*
							 * if the move is possible we call swapDouble() or swap()
							 * and update the matrix and the inddexes of '_'
							 */
							if(i<=3 && isClose==true) {
								answer = swapDouble(start.current_state, _i, _j, _k, _l , moves[i]);
							}
							if(i>3 && i<8) {
								answer = swap(start.current_state, _i, _j, moves[i]);
							}
							if(i>=8) {
								answer = swap(start.current_state, _k, _l, moves[i]);
							}
							/*
							 * creating the node from the movement we just made
							 * its the son of its current father
							 * meaning its a new game state , because we made a move on the board
							 */
							node temp = new node(answer);
							temp.parent=start;
							/*
							 * calculating the path 
							 */
							if(i<=3 && isClose==true) {
								if(moves[i]==1) {
									temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_k]+"L-";
								}else if(moves[i]==2) {
									temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"U-";		
								}else if(moves[i]==3) {
									temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"R-";
								}else if(moves[i]==4) {
									temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"D-";
								}
							}
							if(i>3 && i<8) {
								if(moves[i]==5) {
									temp.path+=temp.parent.path+answer[_i][_j]+"L-";
								}else if(moves[i]==6) {
									temp.path+=temp.parent.path+answer[_i][_j]+"U-";		
								}else if(moves[i]==7) {
									temp.path+=temp.parent.path+answer[_i][_j]+"R-";
								}else if(moves[i]==8) {
									temp.path+=temp.parent.path+answer[_i][_j]+"D-";
								}
							}
							if(i>=8) {
								if(moves[i]==9) {
									temp.path+=temp.parent.path+answer[_k][_l]+"L-";
								}else if(moves[i]==10) {
									temp.path+=temp.parent.path+answer[_k][_l]+"U-";		
								}else if(moves[i]==11) {
									temp.path+=temp.parent.path+answer[_k][_l]+"R-";
								}else if(moves[i]==12) {
									temp.path+=temp.parent.path+answer[_k][_l]+"D-";
								}
							}

							/*
							 * Calculating cost
							 */
							if(moves[i]>=5) {
								temp.cost=temp.parent.cost+5;
							}else if(moves[i]==1 || moves[i]==3) {
								temp.cost=temp.parent.cost+6;
							}else if(moves[i]==2 || moves[i]==4) {
								temp.cost=temp.parent.cost+7;
							}


							/*
							 * just pseudo code conditions
							 */
							temp.distance = heuristic(temp.current_state)+temp.cost;
							if(temp.distance > t) {

								MINF = Math.min(temp.distance, MINF);

								continue;

							} 
							if(null!=op_list.get(temp.toString()) &&op_list.get(temp.toString()).out==true ) {
								continue;
							}
							if(null!=op_list.get(temp.toString()) &&op_list.get(temp.toString()).out==false ) {
								if(op_list.get(temp.toString()).distance > temp.distance) {
									stk.remove(op_list.get(temp.toString()));
									op_list.remove(temp.toString());

								}else {
									continue;
								}
							}
							/*
							 * we found goal state
							 */
							if(check_goal(temp.current_state)==true) {
								try {

									FileWriter myWriter = new FileWriter("output.txt");
									temp.path = temp.path.substring(0,temp.path.length()-1);
									counnnnnnt++;
									System.out.println(temp.path);
									myWriter.write(temp.path+"\n");

									System.out.println("Num: "+counnnnnnt);
									myWriter.write("Num: "+counnnnnnt+"\n");

									System.out.println("Cost: "+temp.cost);
									myWriter.write("Cost: "+temp.cost);

									if(with_time==true) {
										long endTime   = System.currentTimeMillis();
										System.out.println("time: " +(endTime - startTime)/1000F);
										myWriter.write("\ntime: "+(endTime - startTime)/1000F);
									}

									myWriter.close();
								}catch (Exception e) {
									// TODO: handle exception
								}

								return;
							}
							counnnnnnt++;
							op_list.put(temp.toString(), temp);
							stk.add(temp);
						}
					}
				}	
			}
			t = MINF;
			start.out=false;
		}
		//return null

	}
	public void DFBNB(int[][] curr, int[][] goals) {

		long startTime = System.currentTimeMillis();

		Stack<node> stk = new Stack<node>();
		Stack<node> stk2 = new Stack<node>();
		Hashtable<String, node> op_list = new Hashtable<String, node>();

		int t = Integer.MAX_VALUE;

		node star = new node(curr);
		stk.add(star);
		op_list.put(star.toString(), star);


		while(!stk.isEmpty()) {
			node start = stk.pop();

			if(start.out==true) {
				op_list.remove(start.toString());
			}
			else {
				start.out = true;
				stk.add(start);
				op_list.put(start.toString(),start);
				PriorityQueue<node> pq = new PriorityQueue<node>(new nodeComperator());
				/*
				 * checking possible moves
				 */
				int[] moves = check_moves(start.current_state);
				int _i = -1 ,_j=-1 ,_k=-1 ,_l=-1;
				/*
				 * checking where are the '_'
				 * and how many of them exists
				 * and saves their indexes
				 */
				for (int i = 0; i < start.current_state.length; i++) {
					for (int j = 0; j < start.current_state[0].length; j++) {
						if(start.current_state[i][j]==-1 && (_i==-1 && _j==-1) ) {
							_i=i;
							_j=j;
						}
						if(start.current_state[i][j]==-1 && (_i!=i || _j!=j) ) {
							_k=i;
							_l=j;
						}
					}
				}
				/*
				 * going through the array returned from checkmoves() function
				 * every slot in array resembles possible move if its != -1
				 */
				for (int i = 0; i < moves.length; i++) {
					if(moves[i]!=-1 && moves[i]!=0) {
						/*
						 * if the move is possible we call swapDouble() or swap()
						 * and update the matrix and the inddexes of '_'
						 */
						if(i<=3 && isClose==true) {
							answer = swapDouble(start.current_state, _i, _j, _k, _l , moves[i]);
						}
						if(i>3 && i<8) {
							answer = swap(start.current_state, _i, _j, moves[i]);
						}
						if(i>=8) {
							answer = swap(start.current_state, _k, _l, moves[i]);
						}
						/*
						 * creating the node from the movement we just made
						 * its the son of its current father
						 * meaning its a new game state , because we made a move on the board
						 */
						node temp = new node(answer);
						temp.parent=start;
						/*
						 * calculating the path 
						 */
						if(i<=3 && isClose==true) {
							if(moves[i]==1) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_k]+"L-";
							}else if(moves[i]==2) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"U-";		
							}else if(moves[i]==3) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"R-";
							}else if(moves[i]==4) {
								temp.path+=temp.parent.path+answer[_i][_j]+"&"+answer[_k][_l]+"D-";
							}
						}
						if(i>3 && i<8) {
							if(moves[i]==5) {
								temp.path+=temp.parent.path+answer[_i][_j]+"L-";
							}else if(moves[i]==6) {
								temp.path+=temp.parent.path+answer[_i][_j]+"U-";		
							}else if(moves[i]==7) {
								temp.path+=temp.parent.path+answer[_i][_j]+"R-";
							}else if(moves[i]==8) {
								temp.path+=temp.parent.path+answer[_i][_j]+"D-";
							}
						}
						if(i>=8) {
							if(moves[i]==9) {
								temp.path+=temp.parent.path+answer[_k][_l]+"L-";
							}else if(moves[i]==10) {
								temp.path+=temp.parent.path+answer[_k][_l]+"U-";		
							}else if(moves[i]==11) {
								temp.path+=temp.parent.path+answer[_k][_l]+"R-";
							}else if(moves[i]==12) {
								temp.path+=temp.parent.path+answer[_k][_l]+"D-";
							}
						}

						/*
						 * Calculating cost
						 */
						if(moves[i]>=5) {
							temp.cost=temp.parent.cost+5;
						}else if(moves[i]==1 || moves[i]==3) {
							temp.cost=temp.parent.cost+6;
						}else if(moves[i]==2 || moves[i]==4) {
							temp.cost=temp.parent.cost+7;
						}

						temp.distance = (int)((heuristic(temp.current_state)*3)+temp.cost);
						pq.add(temp);

					}
				}
				
				Iterator<node> iter = pq.iterator();
				while (iter.hasNext()) {
					node child = iter.next();
					/*
					 * just pseudo code conditions
					 */
					if(child.distance >= t) {
						while(!pq.isEmpty())
						{
							if(!pq.peek().toString().equals(child.toString()))
							{
								stk2.add(pq.poll());
							}
							else {
								pq.clear();
							}
						}
						while(!stk2.isEmpty())
						{
							pq.add(stk2.pop());
						}
						iter = pq.iterator();
					}
					else if(op_list.get(child.toString())!=null && op_list.get(child.toString()).out==true) {
						pq.remove(child);
						iter = pq.iterator();
					}
					else if(op_list.get(child.toString())!=null && !op_list.get(child.toString()).out==false) {
						if((op_list.get(child.toString()).distance) <=   child.distance) {
							pq.remove(child);
							iter = pq.iterator();
						}
						else
						{
							stk.remove(op_list.get(child.toString()));
							op_list.remove(child.toString());
						}
					}
					else if(check_goal(child.current_state))
					{
						t = child.distance;
						child.path = child.path.substring(0, child.path.length()-1);
						result = child.path;
						costs = child.cost;
						
						while(!pq.isEmpty())
						{
							if(!pq.peek().toString().equals(child.toString()))
							{
								stk2.add(pq.poll());
							}
							else {
								pq.clear();
							}
						}
						while(!stk2.isEmpty())
						{
							pq.add(stk2.pop());
						}
						iter = pq.iterator();
					}
				}
				while(!pq.isEmpty())
				{
					stk2.add(pq.poll());
				}
				while(!stk2.empty())
				{	counnnnnnt++;
				node uNode = stk2.pop();
				stk.add(uNode);
				op_list.put(uNode.toString(), uNode);
				}
			}
		}

		//		System.out.println(path);
		//		System.out.println("cost: "+co);
		//		System.out.println("result: "+result);
		try {

			FileWriter myWriter = new FileWriter("output.txt");
			//			ans = ans.substring(0,ans.length()-1);
			counnnnnnt++;

			System.out.println(result);
			myWriter.write(result+"\n");

			System.out.println("Num: "+counnnnnnt);
			myWriter.write("Num: "+counnnnnnt+"\n");

			System.out.println("Cost: "+costs);
			myWriter.write("Cost: "+costs);

			if(with_time==true) {
				long endTime   = System.currentTimeMillis();
				System.out.println("time: " +(endTime - startTime)/1000F);
				myWriter.write("\ntime: "+(endTime - startTime)/1000F);
			}

			myWriter.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

}
/*
 * just used this for my priority queue 
 * to sort by low to high
 * first node will be with lowest cost
 */
class nodeComperator implements Comparator<node>{

	public int compare(node s1, node s2) {
		if (s1.distance > s2.distance)
			return 1;
		else if (s1.distance < s2.distance)
			return -1;
		return 0;
	}
}
