/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.languages.pl_sql.editor.explorer.nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.languages.pl_sql.editor.oracletree.OConnectionClass;
import org.netbeans.modules.languages.pl_sql.editor.oracletree.OUser;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;

/**
 *
 * @author SUMsoft
 */
public final class OConnectionNodeChildFactory extends ChildFactory<OConnectionNode>
        implements ChangeListener {

    private final OConnectionClass parentInfo;

    public OConnectionNodeChildFactory(OConnectionClass parentInfo) {
        super();
        this.parentInfo = parentInfo;
        parentInfo.addChangeListener(
                WeakListeners.create(ChangeListener.class, this, parentInfo));

        stateChanged(new ChangeEvent(parentInfo));
    }

    @Override
    public Node[] createNodesForKey(OConnectionNode key) {
        //AbstractNode nd = new AbstractNode(Children.LEAF);
        OConnectionClass ocs = key.getOConnectionClass();
        TreeSet<OUser> tou = ocs.getUsers();
        ArrayList<OUserNode> al = new ArrayList<OUserNode>();
        for (OUser ou : tou) {
            OUserNode oun = new OUserNode(ou);
            al.add(oun);
        }
        return al.toArray(new Node[al.size()]);
    //nd.setDisplayName(key.toString());
    //return new Node[]{nd};
    }

    @Override
    protected boolean createKeys(List<OConnectionNode> keys) {
        //List<OConnectionClass> ocsl = OConnectionClass.LoadAllConnections();
        //for (OConnectionClass ocs : ocsl) {
        parentInfo.ReloadChilds();
        keys.add(new OConnectionNode(parentInfo));
        //}
        return true;
    }

    public void stateChanged(ChangeEvent e) {
        refresh(false);
    }
}
