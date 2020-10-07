package scorpio1331.github.io.DaytimeVote;

import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class DaytimeListenerTest {

    @Mock
    private JavaPlugin mockPlugin;
    @Mock
    private Server mockServer;
    @Mock
    private PluginManager mockPluginManager;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private DaytimeListener daytimeListenerUnderTest;

    @Before
    public void setUp() {
        //mock register events call
        doNothing().when(mockPluginManager).registerEvents(isA(Listener.class), isA(JavaPlugin.class));
        doReturn(mockPluginManager).when(mockServer).getPluginManager();
        doReturn(mockServer).when(mockPlugin).getServer();

        daytimeListenerUnderTest = new DaytimeListener(mockPlugin);
    }

    @Test
    public void getEventListeners() {
        // Run the test
        Object[] listeners =  daytimeListenerUnderTest.getEventListeners().toArray();

        // Verify the results
        Assert.assertArrayEquals(new Object[] {}, listeners);
    }

    @Test
    public void testAddEventListener() {
        // Setup
        final IHandlesDayNightCycle mockListener = mock(IHandlesDayNightCycle.class);

        // Run the test
        daytimeListenerUnderTest.AddEventListener(mockListener);

        // Verify the results
        Assert.assertArrayEquals(new IHandlesDayNightCycle[] {mockListener}, daytimeListenerUnderTest.getEventListeners().toArray());
    }

    @Test
    public void testRemoveEventListener() {
        // Setup
        final IHandlesDayNightCycle mockListener = mock(IHandlesDayNightCycle.class);

        // Run the test
        daytimeListenerUnderTest.AddEventListener(mockListener);
        boolean removed = daytimeListenerUnderTest.RemoveEventListener(mockListener);

        // Verify the results
        Assert.assertTrue(removed);
    }

    @Test
    public void testDayNightChanged() {
        // Setup
        final IHandlesDayNightCycle mockListener = mock(IHandlesDayNightCycle.class);
        ArgumentCaptor<DayNightEvent> valueCapture = ArgumentCaptor.forClass(DayNightEvent.class);
        doNothing().when(mockListener).HandleDaytimeEvent(valueCapture.capture());

        final DayNightEvent event = new DayNightEvent(true);
        daytimeListenerUnderTest.AddEventListener(mockListener);

        // Run the test
        daytimeListenerUnderTest.DayNightChanged(event);

        // Verify the results
        verify(mockListener, times(1)).HandleDaytimeEvent(event);
        Assert.assertTrue(valueCapture.getValue().isDay());
    }

}
