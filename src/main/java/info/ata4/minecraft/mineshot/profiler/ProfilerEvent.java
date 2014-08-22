/*
 ** 2013 January 21
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.profiler;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;

/**
 * Event fired by the event proxy profiler.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ProfilerEvent extends Event {
    
    private final String section;
    private final Side side;
    private boolean end;

    public ProfilerEvent(String section, Side side, boolean end) {
        this.section = section;
        this.side = side;
        this.end = end;
    }

    public String getSection() {
        return section;
    }
    
    public Side getSide() {
        return side;
    }
    
    public boolean isEnd() {
        return end;
    }
    
    public static class Server extends ProfilerEvent {
        public Server(String section, boolean end) { super(section, Side.SERVER, end); }
    }
    
    public static class Client extends ProfilerEvent {
        public Client(String section, boolean end) { super(section, Side.CLIENT, end); }
    }
}
