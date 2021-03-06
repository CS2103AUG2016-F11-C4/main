package seedu.testplanner.storage;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.dailyplanner.commons.events.model.DailyPlannerChangedEvent;
import seedu.dailyplanner.commons.events.storage.DataSavingExceptionEvent;
import seedu.dailyplanner.model.DailyPlanner;
import seedu.dailyplanner.model.ReadOnlyDailyPlanner;
import seedu.dailyplanner.model.UserPrefs;
import seedu.dailyplanner.storage.JsonUserPrefsStorage;
import seedu.dailyplanner.storage.Storage;
import seedu.dailyplanner.storage.StorageManager;
import seedu.dailyplanner.storage.XmlDailyPlannerStorage;
import seedu.testplanner.testutil.EventsCollector;
import seedu.testplanner.testutil.TypicalTestTask;

import java.io.IOException;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StorageManagerTest {

    private StorageManager storageManager;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();


    @Before
    public void setup() {
        storageManager = new StorageManager(getTempFilePath("ab"), getTempFilePath("prefs"));
    }


    private String getTempFilePath(String fileName) {
        return testFolder.getRoot().getPath() + fileName;
    }


    /*
     * Note: This is an integration test that verifies the StorageManager is properly wired to the
     * {@link JsonUserPrefsStorage} class.
     * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
     */

    @Test
    public void prefsReadSave() throws Exception {
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        DailyPlanner original = new TypicalTestTask().getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyDailyPlanner retrieved = storageManager.readDailyPlanner().get();
        assertEquals(original, new DailyPlanner(retrieved));
        //More extensive testing of AddressBook saving/reading is done in XmlAddressBookStorageTest
    }

    @Test
    public void getAddressBookFilePath(){
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void handleAddressBookChangedEvent_exceptionThrown_eventRaised() throws IOException {
        //Create a StorageManager while injecting a stub that throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlAddressBookStorageExceptionThrowingStub("dummy"), new JsonUserPrefsStorage("dummy"));
        EventsCollector eventCollector = new EventsCollector();
        storage.handleAddressBookChangedEvent(new DailyPlannerChangedEvent(new DailyPlanner()));
        assertTrue(eventCollector.get(0) instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlAddressBookStorageExceptionThrowingStub extends XmlDailyPlannerStorage{

        public XmlAddressBookStorageExceptionThrowingStub(String filePath) {
            super(filePath);
        }

        @Override
        public void saveAddressBook(ReadOnlyDailyPlanner addressBook, String filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
