package gameoflife;

class Main {

    static GUI gameInstance = new GUI();
    public static void main(String[] args) {
        gameInstance.setup();
    }

    public static void updateGame() { 
    //for (int a = 0 ; a < 100 ; a++) //for manual selection of how many iterations - testing
    //{
	    //analyze current generation
	    for (int i = 0 ; i < GUI.gridsize ; i++) //y axis
	    {
            for (int j = 0 ; j < GUI.gridsize ; j++) //x axis
            {
                int neighbours = gameInstance.numNeighbours(i, j);      //for each cell, check how many neighbours

                //then assign life status to 2nd array (what should happen to the cell next gen?)
                gameInstance.lifeUpdate(i, j, neighbours);  // updates ntilevalue, in GUI class  
            }
        } 

        gameInstance.newGenOut();   //output new generation based on 2nd array
    //}
    }

}   // end of class Main body
