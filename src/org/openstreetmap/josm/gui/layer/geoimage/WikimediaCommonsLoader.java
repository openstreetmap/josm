// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.gui.layer.geoimage;

import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.data.Bounds;
import org.openstreetmap.josm.gui.MainApplication;
import org.openstreetmap.josm.gui.PleaseWaitRunnable;
import org.openstreetmap.josm.io.OsmTransferException;
import org.openstreetmap.josm.tools.Logging;
import org.openstreetmap.josm.tools.Mediawiki;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.openstreetmap.josm.tools.I18n.tr;

/**
 * Loads geocoded images from <a href="https://commons.wikimedia.org/">Wikimedia Commons</a> for the given bounding box.
 */
public class WikimediaCommonsLoader extends PleaseWaitRunnable {
    protected String apiUrl = "https://commons.wikimedia.org/w/api.php";
    protected GeoImageLayer layer;
    private final Bounds bounds;

    /**
     * Constructs a new {@code WikimediaCommonsLoader}
     * @param bounds The bounds to load
     */
    public WikimediaCommonsLoader(Bounds bounds) {
        super(tr("Load images from Wikimedia Commons"));
        this.bounds = bounds;
    }

    @Override
    protected void realRun() throws SAXException, IOException, OsmTransferException {
        List<ImageEntry> imageEntries = new ArrayList<>();
        try {
            new Mediawiki(apiUrl).searchGeoImages(bounds, (title, latLon) -> imageEntries.add(new WikimediaCommonsEntry(title, latLon)));
        } catch (ParserConfigurationException | XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
        Logging.info("Loaded {0} images from Wikimedia Commons", imageEntries.size());
        layer = new WikimediaCommonsLayer(imageEntries);
    }

    @Override
    protected void finish() {
        MainApplication.getLayerManager().addLayer(layer);
    }

    @Override
    protected void cancel() {
        // do nothing
    }

    public static class Action extends JosmAction {
        public Action() {
            super(tr("Load images from Wikimedia Commons"), "wikimedia_commons", null, null, false);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Bounds bounds = MainApplication.getMap().mapView.getRealBounds();
            MainApplication.worker.execute(new WikimediaCommonsLoader(bounds));
        }

        @Override
        protected void updateEnabledState() {
            setEnabled(MainApplication.isDisplayingMapView());
        }
    }

}
