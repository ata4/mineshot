/*
 ** 2013 April 15
 **
 ** The author disclaims copyright to this source code.  In place of
 ** a legal notice, here is a blessing:
 **    May you do good and not evil.
 **    May you find forgiveness for yourself and forgive others.
 **    May you share freely, never taking more than you give.
 */
package info.ata4.minecraft.mineshot.profiler;

import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.relauncher.Side;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.profiler.Profiler;
import org.apache.commons.lang3.StringUtils;

/**
 * Profiler proxy that sends events to the Forge event bus for observation.
 * 
 * @author Nico Bergemann <barracuda415 at yahoo.de>
 */
public class ProfilerEventProxy extends Profiler {
    
    /**
     * Instance of the original profiler, used as proxy
     */
    private final Profiler proxy;
    
    /**
     * Deque of profiling sections
     */
    private final Deque<String> sectionDeque = new ArrayDeque<String>(8);
    
    /**
     * Section string pool
     */
    private final Map<Integer, String> sectionCache = new HashMap<Integer, String>(128);
    
   /**
     * Current profiling section
     */
    private String profilingSection = "";
    
    /**
     * Side of the profiler.
     */
    private final Side side;
    
    /**
     * Event bus to send the events to.
     */
    private final EventBus eventBus;
    
    public ProfilerEventProxy(Profiler proxy, EventBus eventBus, Side side) {
        this.side = side;
        this.eventBus = eventBus;
        
        // use the proxy only if the profiler is already modified
        Class profilerClass = proxy.getClass();
        if (profilerClass != getClass() && profilerClass != Profiler.class) {
            this.proxy = proxy;
        } else {
            this.proxy = null;
        }
    }
    
    private String getCurrentSection() {
        if (sectionDeque.isEmpty()) {
            return "";
        }

        if (sectionDeque.size() == 1) {
            return sectionDeque.getFirst();
        }

        // calculate hash from strings in the current deque
        int hash = 0;
        for (String section : sectionDeque) {
            hash += section.hashCode();
        }

        // get a pre-build string from the map if possible
        if (sectionCache.containsKey(hash)) {
            return sectionCache.get(hash);
        }

        // build the section string
        String section = StringUtils.join(sectionDeque, '.');

        // save it for re-use, building the string on every call would
        // produce a lot of memory garbage
        sectionCache.put(hash, section);

        return section;
    }
    
    @Override
    public void startSection(String section) {
        // required since clearProfiling isn't called when profiling is disabled
        if (section.equals("root")) {
            sectionDeque.clear();
        }

        sectionDeque.add(section);
        profilingSection = getCurrentSection();
        
        // send event
        ProfilerEvent event;
        if (side == Side.SERVER) {
            event = new ProfilerEvent.Server(profilingSection, false);
        } else {
            event = new ProfilerEvent.Client(profilingSection, false);
        }
        eventBus.post(event);
        
        if (proxy != null) {
            proxy.profilingEnabled = profilingEnabled;
            proxy.startSection(section);
        } else {
            super.startSection(section);
        }
    }
    
    /**
     * End section
     */
    @Override
    public void endSection() {
        profilingSection = getCurrentSection();

        // send event
        ProfilerEvent event;
        if (side == Side.SERVER) {
            event = new ProfilerEvent.Server(profilingSection, true);
        } else {
            event = new ProfilerEvent.Client(profilingSection, true);
        }
        eventBus.post(event);

        if (!sectionDeque.isEmpty()) {
            sectionDeque.removeLast();
        }

        if (proxy != null) {
            proxy.endSection();
        } else {
            super.endSection();
        }
    }
    
    @Override
    public void endStartSection(String section) {
        if (proxy != null) {
            proxy.endStartSection(section);
        }
        super.endStartSection(section);
    }

    @Override
    public void clearProfiling() {
        profilingSection = "";
        sectionDeque.clear();
        
        if (proxy != null) {
            proxy.clearProfiling();
        }
    }

    @Override
    public String getNameOfLastSection() {
        if (proxy != null) {
            return proxy.getNameOfLastSection();
        } else {
            return super.getNameOfLastSection();
        }
    }
}
