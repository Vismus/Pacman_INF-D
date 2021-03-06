

package pacman_infd.games;

/**
 *
 * @author remcoruijsenaars
 */
public class StopWatch {
    private long startTime;
    private long elapsedTime;
    
    private boolean isRunning;
    
    public StopWatch(){
        reset();
    }

    public StopWatch(long elapsedTime) {
        isRunning = false;
        this.elapsedTime = elapsedTime;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void reset(){
        elapsedTime = 0;
        isRunning = false;
    }
    
    public void start(){
        if (!isRunning) {
            isRunning = true;
            startTime = System.currentTimeMillis();
        }
    }
    
    public void stop() {
        if (isRunning) {
           isRunning = false;
           long stopTime = System.currentTimeMillis();
           elapsedTime = elapsedTime + stopTime - startTime;
        }
    }

    public void restart() {
        if (!isRunning) {
            isRunning = true;
            startTime = System.currentTimeMillis() - elapsedTime;
            elapsedTime = 0;
        }
    }
    
    public long getElapsedTime(){
        if(isRunning){
            long endTime = System.currentTimeMillis();
            return elapsedTime + endTime - startTime;
        }else{
            return elapsedTime;
        
        }
    }
    
    public String getElapsedTimeMinutesSeconds(){
        long time = getElapsedTime();
        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        
        return String.format("%02d:%02d", minutes, seconds);
    }

}
