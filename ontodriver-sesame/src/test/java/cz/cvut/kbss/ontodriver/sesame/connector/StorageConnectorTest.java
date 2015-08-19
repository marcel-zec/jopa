package cz.cvut.kbss.ontodriver.sesame.connector;

import cz.cvut.kbss.ontodriver.OntologyStorageProperties;
import cz.cvut.kbss.ontodriver.sesame.SesameDataSource;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author ledvima1
 */
public class StorageConnectorTest {

    private static final String DRIVER = SesameDataSource.class.getName();

    private File repositoryFolder;

    private StorageConnector connector;

    @After
    public void tearDown() throws Exception {
        if (connector != null && connector.isOpen()) {
            connector.close();
        }
        if (repositoryFolder != null && repositoryFolder.exists()) {
            deleteRecursive(repositoryFolder);
        }
    }

    private void deleteRecursive(File path) {
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                deleteRecursive(f);
            }
        }
        path.delete();
    }

    @Test
    public void nonExistingParentFoldersAreCreatedWhenStorageIsInitialized() throws Exception {
        String projectRootPath = new File("pom.xml").getAbsolutePath();
        projectRootPath = projectRootPath.substring(0, projectRootPath.lastIndexOf(File.separator));
        final URI fileUri = URI
                .create("file://" + projectRootPath + File.separator + "internal" + File.separator + "folder" +
                        File.separator +
                        "repositories" + File.separator + "repositoryTest");
        final File parentDir = new File(projectRootPath + File.separator + "internal");
        assertFalse(parentDir.exists());
        final OntologyStorageProperties storageProperties = OntologyStorageProperties.driver(DRIVER)
                                                                                     .physicalUri(fileUri).build();
        connector = new StorageConnector(storageProperties, Collections.emptyMap());
        assertTrue(parentDir.exists());
        this.repositoryFolder = parentDir;
        final File repositoryDir = new File(fileUri);
        assertTrue(repositoryDir.exists());
    }
}