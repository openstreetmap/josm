// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.command.conflict;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.util.Collection;
import java.util.List;

import javax.swing.Icon;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.data.conflict.Conflict;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.OsmPrimitive;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.tools.ImageProvider;

/**
 * Represents the resolution of conflicts in the node list of two {@link Way}s.
 *
 */
public class WayNodesConflictResolverCommand extends ConflictResolveCommand {
    /** the conflict to resolve */
    private final Conflict<Way> conflict;

    /** the list of merged nodes. This becomes the list of news of my way after the
     *  command is executed
     */
    private final List<Node> mergedNodeList;

    /**
     * @param conflict the conflict data set
     * @param mergedNodeList the list of merged nodes
     */
    @SuppressWarnings("unchecked")
    public WayNodesConflictResolverCommand(Conflict<? extends OsmPrimitive> conflict, List<Node> mergedNodeList) {
        this.conflict = (Conflict<Way>) conflict;
        this.mergedNodeList = mergedNodeList;
    }

    @Override
    public String getDescriptionText() {
        return tr("Resolve conflicts in node list of way {0}", conflict.getMy().getId());
    }

    @Override
    public Icon getDescriptionIcon() {
        return ImageProvider.get("data", "object");
    }

    @Override
    public boolean executeCommand() {
        // remember the current state of 'my' way
        //
        super.executeCommand();

        // replace the list of nodes of 'my' way by the list of merged nodes
        //
        for (Node n:mergedNodeList) {
            if (!getLayer().data.getNodes().contains(n)) {
                Main.warn(tr("Main dataset does not include node {0}", n.toString()));
            }
        }
        conflict.getMy().setNodes(mergedNodeList);
        rememberConflict(conflict);
        return true;
    }

    @Override
    public void fillModifiedData(Collection<OsmPrimitive> modified, Collection<OsmPrimitive> deleted,
            Collection<OsmPrimitive> added) {
        modified.add(conflict.getMy());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((conflict == null) ? 0 : conflict.hashCode());
        result = prime * result + ((mergedNodeList == null) ? 0 : mergedNodeList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        WayNodesConflictResolverCommand other = (WayNodesConflictResolverCommand) obj;
        if (conflict == null) {
            if (other.conflict != null)
                return false;
        } else if (!conflict.equals(other.conflict))
            return false;
        if (mergedNodeList == null) {
            if (other.mergedNodeList != null)
                return false;
        } else if (!mergedNodeList.equals(other.mergedNodeList))
            return false;
        return true;
    }
}
