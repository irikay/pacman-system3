

package pacman_infd;

/**
 *
 * @author remcoruijsenaars
 */
class StopWatch {
    
    private long startTime;
    private long elapsedTime;
    
    private boolean isRunning;

    private final int INITIAL_ELAPSED_TIME = 0;
    
    StopWatch(){
        reset();
    }
    
    void reset(){
        elapsedTime = INITIAL_ELAPSED_TIME;
        isRunning = false;
    }
    
    void start(){
        if(!isRunning){
            isRunning = true;
            startTime = System.currentTimeMillis();
        }
    }
    
    void stop(){
        if(isRunning){
           isRunning = false;
           long stopTime = System.currentTimeMillis();
           elapsedTime = elapsedTime + stopTime - startTime;
        }
        
    }
    
    private long getElapsedTime(){
        if(isRunning){
            long endTime = System.currentTimeMillis();
            return elapsedTime + endTime - startTime;
        }else{
            return elapsedTime;
        
        }
    }
    
    String getElapsedTimeMinutesSeconds(){
        long time = getElapsedTime();
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        
        return String.format("%02d:%02d", minutes, seconds);
    }

}
