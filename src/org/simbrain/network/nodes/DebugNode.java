
package org.simbrain.network.nodes;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.simbrain.network.NetworkPanel;

import edu.umd.cs.piccolo.PNode;

import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;

/**
 * Debug node.
 */
public final class DebugNode
    extends ScreenElement {

    /**
     * Create a new debug node.
     */
    public DebugNode(NetworkPanel net, final double x, final double y) {

        super(net);
        offset(x, y);

        setPickable(true);
        setChildrenPickable(false);

        PText label = new PText("Debug");
        PNode rect = PPath.createRectangle(0.0f, 0.0f, 50.0f, 50.0f);

        label.offset(7.5d, 21.0d);

        rect.addChild(label);
        addChild(rect);

        setBounds(rect.getBounds());
    }


    /** @see ScreenElement */
    protected boolean hasToolTipText() {
        return true;
    }

    /** @see ScreenElement */
    protected String getToolTipText() {
        return "debug";
    }

    /** @see ScreenElement */
    protected boolean hasContextMenu() {
        return true;
    }

    /** @see ScreenElement */
    protected JPopupMenu getContextMenu() {

        JPopupMenu contextMenu = new JPopupMenu();

        // add actions
        contextMenu.add(new JMenuItem("Debug node"));
        contextMenu.add(new JMenuItem("Node specific context menu item"));
        contextMenu.add(new JMenuItem("Node specific context menu item"));

        return contextMenu;
    }
}