package mummymaze.actions;

import agent.Action;
import mummymaze.MummyMazeState;

public class ActionStay extends Action<MummyMazeState>{

    public ActionStay(){
        super(1);
    }

    @Override
    public void execute(MummyMazeState state){
        state.stay();
        state.setAction(this);
    }

    @Override
    public boolean isValid(MummyMazeState state){
        return true;
    }
}