package window;

import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.triggers.TriggerResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

public class EventTriggerDelay extends Trigger<Object, TimeWindow> {

    private EventTriggerDelay() {
    }

    public static EventTriggerDelay create() {
        return new EventTriggerDelay();
    }

    @Override
    public TriggerResult onElement(
            Object element, long timestamp, TimeWindow window, TriggerContext ctx) {
        // Always fire when an element arrives
        return TriggerResult.FIRE;
    }

    @Override
    public TriggerResult onEventTime(long time, TimeWindow window, TriggerContext ctx) {
        // Fire only when the watermark reaches the end of the window
        if (time == window.getEnd()) {
            return TriggerResult.FIRE;
        } else {
            return TriggerResult.CONTINUE;
        }
    }

    @Override
    public TriggerResult onProcessingTime(long time, TimeWindow window, TriggerContext ctx) {
        // Continue processing time-based triggers
        return TriggerResult.CONTINUE;
    }

    @Override
    public void clear(TimeWindow window, TriggerContext ctx) {
        // Nothing to clear
    }

    @Override
    public boolean canMerge() {
        // Allow merging of windows
        return true;
    }

    @Override
    public void onMerge(TimeWindow window, OnMergeContext ctx) {
        // No special handling during window merging
    }

    @Override
    public String toString() {
        return "EventTriggerDelay()";
    }
}
